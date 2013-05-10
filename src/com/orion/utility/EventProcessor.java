/**
 * This file is part of Orion source code
 * 
 * Copyright (C) 2012 [Gore]Clan - http://www.goreclan.net
 *
 * Orion is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Lesser Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Orion. If not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 * 
 * @author      Mathias Van Malderen, Daniele Pantaleone
 * @version     1.0
 * @copyright   Mathias Van Malderen, 04 February, 2012
 * @package     com.orion.utility
 **/

package com.orion.utility;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.logging.Log;

import com.google.common.collect.Multimap;
import com.orion.bot.Orion;
import com.orion.event.Event;
import com.orion.event.EventType;
import com.orion.event.RegisteredEvent;
import com.orion.exception.EventInterruptedException;
import com.orion.plugin.Plugin;

public class EventProcessor implements Runnable {
    
    private final Log log;
    private BlockingQueue<Event> eventqueue;
    private Multimap<EventType, RegisteredEvent> regevents;
    
    
    /**
     * Object constructor
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  orion <tt>Orion</tt> object reference
     **/
    public EventProcessor(Orion orion) {
        
        this.log = orion.log;
        this.eventqueue = orion.eventqueue;
        this.regevents = orion.regevents;
    
        // Printing some debugging info so the user can check if he's using the correct settings
        this.log.debug("Event processor initialized [ events : " + this.regevents.size() + " | buffer : " + (this.eventqueue.remainingCapacity() + this.eventqueue.size()) + " ]");
        
    }
    
    
    /**
     * Runnable implementation<br>
     * Will iterate throught all the events stored by the parser in the queue
     * It peeks an <tt>Event</tt> from the queue and process it over all
     * the mapped <tt>Methods</tt> retrieved from the registered events map
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     **/
    @Override
    public void run() {
        
        // Notifying Thread start in the log file.
    	this.log.debug("Starting event processor [ systime : " + System.currentTimeMillis() + " ]");
        
        while (true) {
            
            try {
                
                Event event = this.eventqueue.take();
                
                // If the thread was interrupted, then the event won't be processed
                // We'll notify it with an Exception so we can break the while cycle
                // and gracefully stop the running thread
                if (Thread.interrupted()) throw new InterruptedException();
                
                // Retrieving the collection of registered events for this event type
                Collection<RegisteredEvent> collection = this.regevents.get(event.type);
                
                // Skip if this event is not mapped over any method
                if ((collection == null) || (collection.size() == 0)) 
                    continue;
                
                // Iterating over all the RegisteredEvent
                for (RegisteredEvent regevent : collection) {
                    
                    try {
                        
                        Method method = regevent.method;
                        Plugin plugin = regevent.plugin;
                        
                        // Discard if disabled
                        if (!plugin.isEnabled())
                            continue;
                        
                        method.invoke(plugin, event);
                    
                    } catch (InvocationTargetException e) {
                    	
                    	// Don't log anything. A plugin requested to stop processing this event
                    	if (e.getCause().getClass().equals(EventInterruptedException.class))
                    		continue;
                    	
                    	// Logging the Exception and keep processing events anyway
                        this.log.error("[" + regevent.plugin.getClass().getSimpleName() + "] Unable to process event: " + event.type.name(), e);
                        continue;
     
                    } catch (IllegalAccessException | IllegalArgumentException e) {
                        
                        // Logging the Exception and keep processing events anyway
                        this.log.error("[" + regevent.plugin.getClass().getSimpleName() + "] Unable to process event: " + event.type.name(), e);
                        continue;
                        
                    }
                    
                }
                    
    
            } catch (InterruptedException e) {
                
            	// Thread has received interrupt signal
                // Breaking the cycle so it will terminate
                break;
            
            }
        
        }
        
        
        // Notify that the thread is shutting down
        this.log.debug("Stopping event processor [ systime : " + System.currentTimeMillis() + " ]");
    
    }

}
