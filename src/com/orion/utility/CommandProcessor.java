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
import java.util.concurrent.BlockingQueue;

import org.apache.commons.logging.Log;

import com.orion.annotation.Usage;
import com.orion.bot.Orion;
import com.orion.command.Command;
import com.orion.command.RegisteredCommand;
import com.orion.console.Console;
import com.orion.exception.CommandRuntimeException;
import com.orion.exception.CommandSyntaxException;
import com.orion.plugin.Plugin;
import com.orion.urt.Color;

public class CommandProcessor implements Runnable {
   
    private final Log log;
    private final Console console;
    private BlockingQueue<Command> commandqueue;
    private MultiKeyMap<String, String, RegisteredCommand> regcommands;
    
    
    /**
     * Object constructor
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  orion <tt>Orion</tt> object reference
     **/
    public CommandProcessor(Orion orion) {
        
        this.log = orion.log;
        this.console = orion.console;
        this.commandqueue = orion.commandqueue;
        this.regcommands = orion.regcommands;
        
        // Printing some info in the log.
        this.log.debug("Command processor initialized [ commands : " + this.regcommands.getMap1().size() + " | buffer : " + (this.commandqueue.remainingCapacity() + this.commandqueue.size()) + " ]");
        
    }
    
    
    /**
     * Runnable implementation<br>
     * Will iterate through all the events stored by the parser in the 
     * command queue. It peeks a command from the queue and process it over 
     * all the mapped Methods retrieved from the registered command map
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     **/
    @Override
    public void run() {
        
        // Notifying Thread start in the log file
        this.log.debug("Starting command processor [ systime : " + System.currentTimeMillis() + " ]");
        
        while (true) {
            
            try {
                
                Command command = this.commandqueue.take();
                
                // If the thread was interrupted, then the event won't be processed
                // We'll notify it with an Exception so we can break the while cycle
                // and gracefully stop the running thread
                if (Thread.interrupted()) throw new InterruptedException();
                
                // Checking for a match in the HashMap.
                if (!this.regcommands.containsKey(command.handle)) {
                    // Informing the client that the command is not mapped
                    this.console.tell(command.client, "Unable to find command: " + Color.YELLOW + command.prefix.name + Color.RED + command.handle);
                    continue;
                }
                
                // Retrieving the correct RegisteredCommand from the HashMap
                RegisteredCommand regcommand = this.regcommands.get(command.handle);
                
                // Checking correct client minLevel
                if ((command.client.group.level < regcommand.minGroup.level) && (!command.force)) {
                    // Informing the client that he has not sufficiend access for this command
                    this.console.tell(command.client, "You have no sufficient access: " + Color.YELLOW + command.prefix.name + Color.RED + command.handle);
                    continue;
                }
                
                try {
                    
                    Method method = regcommand.method;
                    Plugin plugin = regcommand.plugin;
                    
                    // Discard if disabled
                    if (!plugin.isEnabled()) {
                        // Informing the client that the plugin is disabled
                        this.console.tell(command.client, "Unable to execute command " + Color.YELLOW + command.prefix.name + command.handle + ": " + Color.RED + "plugin disabled");
                        continue;
                    }
                    
                    
                    try {
                        
                        // Executing the command. If the command syntax is invalid,
                        // a CommandRuntimeException will be raised, but it will be
                        // incapsulated into an InvocationTargetException object.
                        method.invoke(plugin, command);
                        
                    } catch (InvocationTargetException e) {
                        
                    	if (e.getCause().getClass().equals(CommandRuntimeException.class)) {
                    	
                    		// Displaying the error in the game chat
                        	this.console.tell(command.client, e.getCause().getMessage());
                    		
                    	} else if (e.getCause().getClass().equals(CommandSyntaxException.class)) {
                    
                            // Display a little help text so the user can 
                            // try again using the correct command syntax
                            Usage usage = method.getAnnotation(Usage.class);
                        	this.console.tell(command.client, e.getCause().getMessage());
                            this.console.tell(command.client, "Usage: " + Color.YELLOW + usage.syntax());
                            continue;
                           
                        } else {
                            
                            // Informing the client of the Exception and log it. We'll keep processing anyway...
                            this.console.tell(command.client, "There was an error processing your command");
                            this.log.error("[" + regcommand.plugin.getClass().getSimpleName() + "] Unable to process command " + command.prefix.name + command.handle, e);
                            continue;
                            
                        }
                    
                    }
                    
                } catch (IllegalAccessException | IllegalArgumentException e) {
                    
                    // Informing the client of the Exception and log it. We'll keep processing anyway...
                    this.console.tell(command.client, "There was an error processing your command");
                    this.log.error("[" + regcommand.plugin.getClass().getSimpleName() + "] Unable to process command " + command.prefix.name + command.handle, e);
                    continue;
                
                }
            
            } catch (InterruptedException e) {
                
                // Thread has received interrupt signal.
                // Breaking the cycle so it will terminate.
                break;
            
            }
        
        }
        
        
        // Notify that the thread is shutting down.
        this.log.debug("Stopping command processor [ systime : " + System.currentTimeMillis() + " ]");
    
    }

}
