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
 * @version     1.2.1
 * @copyright   Mathias Van Malderen, 04 February, 2012
 * @package     com.orion.misc
 **/

package com.orion.misc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.joda.time.DateTime;

import org.slf4j.Logger;

import com.orion.annotation.Usage;
import com.orion.command.Command;
import com.orion.console.Console;
import com.orion.exception.CommandRuntimeException;
import com.orion.exception.CommandSyntaxException;
import com.orion.exception.RconException;
import com.orion.plugin.Plugin;
import com.orion.urt.Color;

public class CommandProcessor implements Runnable {
   
    private final Logger log;
    private final Console console;
    
    private BlockingQueue<Command> commandBus;
    private Map<String, RegisteredMethod> regMethod;
    
    
    /**
     * Object constructor
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     * @param  log Main logger object reference
     * @param  console Main console object reference
     * @param  commandBus A <tt>BlockingQueue</tt> from where to fetch commands
     * @param  regMethod A <tt>MultiKeyMap</tt> which associate each <tt>Command</tt> to a method
     **/
    public CommandProcessor(Logger log,
                            Console console,
                            BlockingQueue<Command> commandBus,
                            Map<String, RegisteredMethod> regMethod) {
        
        this.log = log;
        this.console = console;
        this.commandBus = commandBus;
        this.regMethod = regMethod;
        
        this.log.debug("Command processor initialized: " + this.regMethod.size() +" commands registered");
        
    }
    
    
    /**
     * Runnable implementation<br>
     * Will iterate through all the events stored by the parser in the 
     * command queue. It peeks a command from the bus and process it over 
     * all the mapped Methods retrieved from the registered method <tt>Map</tt>
     * 
     * @author Mathias Van Malderen, Daniele Pantaleone
     **/
    @Override
    public void run() {
        
        this.log.debug("Command processor started: " + new DateTime().toString());
        
        while (true) {
            
            Command command = this.commandBus.peek();
            
            try {
                
                if (Thread.interrupted())
                    throw new InterruptedException();
                
                // Check if the command actually exists
                if (!this.regMethod.containsKey(command.getHandle())) {
                    this.console.sayPrivate(command.getClient(), "Could not find command: " + Color.YELLOW + command.getPrefix().getChar() + Color.RED + command.getHandle());
                    continue;
                }
                
                RegisteredMethod r = this.regMethod.get(command.getHandle());
                
                // Checking correct client minLevel
                if ((command.getClient().getGroup().getLevel() < r.getGroup().getLevel()) && (!command.isForce())) {
                    this.console.sayPrivate(command.getClient(), "You have no sufficient access to " + Color.YELLOW + command.getPrefix().getChar() + Color.RED + command.getHandle());
                    continue;
                }
                
                Method method = r.getMethod();
                Plugin plugin = r.getPlugin();
                
                // Discard if disabled
                if (!plugin.isEnabled()) {
                    this.console.sayPrivate(command.getClient(), "Could not execute command " + Color.YELLOW + command.getPrefix().getChar() + command.getHandle() + ": " + Color.RED + "plugin disabled");
                    continue;
                }
                       
                try {
                    
                    method.invoke(plugin, command);
        
                } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
                    

                    if (e.getCause().getClass().equals(CommandRuntimeException.class)) {
                        
                        // Displaying the error in the game chat
                        this.console.sayPrivate(command.getClient(), e.getCause().getMessage());
                        continue;
                        
                    }
                    
                    if (e.getCause().getClass().equals(CommandSyntaxException.class)) {
                
                        // Display a little help text so the user can 
                        // try again using the correct command syntax
                        Usage usage = method.getAnnotation(Usage.class);
                        this.console.sayPrivate(command.getClient(), e.getCause().getMessage());
                        this.console.sayPrivate(command.getClient(), "Usage: " + Color.YELLOW + usage.syntax());
                        continue;
                       
                    } 
                        
                    // Informing the client of the Exception and log it. We'll keep processing anyway...
                    this.console.sayPrivate(command.getClient(), "There was an " + Color.RED + "error" + Color.WHITE + " processing your command");
                    this.log.error("[" + r.getPlugin().getClass().getSimpleName() + "] Could not process command " + command.getPrefix().getChar() + command.getHandle(), e);
                    continue;
                
                }
            
            } catch (RconException e) {
                
                // Just log the exception since we cannot inform the client in this situation
                this.log.error("Could not process command " + command.getPrefix().getChar() + command.getHandle(), e);
                
            } catch (InterruptedException e) {
                
                // Stop processing
                break;
            
            }
        
        }
        
        this.log.debug("Command processor stopped: "+ new DateTime().toString() );
    
    }

}
