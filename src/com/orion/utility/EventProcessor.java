/**
 * Copyright (c) 2012 Daniele Pantaleone, Mathias Van Malderen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
