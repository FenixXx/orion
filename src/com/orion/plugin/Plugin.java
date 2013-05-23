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
 * @author      Daniele Pantaleone, Mathias Van Malderen
 * @version     1.0
 * @copyright   Daniele Pantaleone, 15 February, 2013
 * @package     com.orion.plugin
 **/

package com.orion.plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Map;
import java.util.Timer;

import org.apache.commons.logging.Log;

import com.google.common.collect.Multimap;
import com.orion.annotation.Dependency;
import com.orion.bot.Orion;
import com.orion.command.Command;
import com.orion.command.RegisteredCommand;
import com.orion.console.Console;
import com.orion.console.UrT42Console;
import com.orion.control.AliasC;
import com.orion.control.CallvoteC;
import com.orion.control.ClientC;
import com.orion.control.GroupC;
import com.orion.control.IpAliasC;
import com.orion.control.PenaltyC;
import com.orion.domain.Group;
import com.orion.event.EventType;
import com.orion.event.RegisteredEvent;
import com.orion.exception.CommandRegisterException;
import com.orion.exception.PluginNotFoundException;
import com.orion.parser.Parser;
import com.orion.urt.Game;
import com.orion.utility.Configuration;
import com.orion.utility.Cron;
import com.orion.utility.MultiKeyMap;

public abstract class Plugin {
    
	protected final Orion orion;
    protected final Log log;
    protected final Console console;
    protected final Parser parser;
    protected final Configuration config;
    
    protected final GroupC groups;
    protected final ClientC clients;
    protected final AliasC aliases;
    protected final CallvoteC callvotes;
    protected final IpAliasC ipaliases;
    protected final PenaltyC penalties;
    
    protected Game game;
    
    protected Map<String, Timer> schedule;
    protected Map<String, Plugin> plugins;
    protected MultiKeyMap<String, String, RegisteredCommand> regcommands;
    protected Multimap<EventType, RegisteredEvent> regevents;
    
    protected boolean enabled = true;
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  config Plugin <tt>Configuration</tt> object
     * @param  orion <tt>Orion</tt> object reference
     **/
    public Plugin(Configuration config, Orion orion) {
        
    	this.orion = orion;
        this.log = orion.log;
        this.console = orion.console;
        this.parser = orion.parser;
        
        this.groups = orion.groups;
        this.clients = orion.clients;
        this.aliases = orion.aliases;
        this.callvotes = orion.callvotes;
        this.ipaliases = orion.ipaliases;
        this.penalties = orion.penalties;
        
        this.game = orion.game;
        
        this.schedule = orion.schedule;
        this.plugins = orion.plugins;
        this.regcommands = orion.regcommands;
        this.regevents = orion.regevents;
        
        this.config = config;
        
    }

    
    /**
     * Load the <tt>Plugin</tt> configuration file and fill <tt>Plugin</tt> attributes
     * 
     * @author Daniele Pantaleone
     **/
    public void onLoadConfig() { }
    
    
    /**
     * Perform operations on <tt>Plugin</tt> startup
     * Method usage:
     * 
     * <ul>
     *     <li>Register plugin commands</li>
     *     <li>Register plugin events</li>
     *     <li>Synchronize the plugin with the current game status</li>
     * </ul>
     * 
     * @author Daniele Pantaleone
     **/
    public void onStartup() { }
    
    
    /**
     * Tells wheter the <tt>Plugin</tt> is enabled ot not
     * 
     * @author Daniele Pantaleone
     * @return <tt>true</tt> if the <tt>Plugin</tt> is enabled, <tt>false</tt> otherwise
     **/
    public boolean isEnabled() {
        return enabled;
    }
    
    
    /**
     * Enable/Disable a <tt>Plugin</tt>
     * 
     * @author Daniele Pantaleone
     * @param  enabled <tt>true</tt> if we want to enable the plugin, <tt>false</tt> otherwise
     **/
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    
    /**
     * Register a <tt>Command</tt> for this <tt>Plugin</tt>
     * 
     * @author Daniele Pantaleone
     * @param  name The name of the <tt>Command</tt>
     * @param  alias An alias for the <tt>Command</tt>
     * @param  group The minimum <tt>Group</tt> that can access the <tt>Command</tt>
     **/
    protected void registerCommand(String name, String alias, String group) {
        
        try {
            
            // Checking name and alias value
            if ((name != null) && (name.trim().isEmpty())) name = null;
            if ((alias != null) && (alias.trim().isEmpty())) alias = null;
            
            // Checking if we got a proper command name as input
            // If it's detected as NULL the command will not be registered
            if (name == null) throw new CommandRegisterException("command name detected as NULL or empty string");
            
            // Trimming and lowercasing the name
            name = name.toLowerCase().trim();
            
            // Checking if the command name is already mapped over another method
            if (this.regcommands.containsKey(name)) throw new CommandRegisterException("command !" + name + " is already mapped over another command");
            
            // Checking if the command alias is already mapped over another method
            if (alias != null) {
                
                // Trimming and lowercasing the alias
                alias = alias.toLowerCase().trim();
                
                if (this.regcommands.containsKey(alias)) {
                    this.warn("Skipping alias registration for command !" + name +": alias !" + alias + " is already mapped over another command");
                    alias = null;
                }
       
            }
            
            // Getting the Group object
            Group minGroup = this.groups.getByMagic(group);
            
            // Checking minGroup
            if (minGroup == null) {
                minGroup = this.groups.getByKeyword("superadmin");
                this.warn("Minimum required group level detected as NULL for command !" + name + ". Casting to default: " + minGroup.name);
            }
            
            // Getting the plugin method
            Method method = this.getClass()
            					.getMethod("Cmd" + Character.toUpperCase(name.charAt(0)) + name.substring(1).toLowerCase(), 
            					 Command.class);
            
            
            if (method.isAnnotationPresent(Dependency.class)) {
            	
            	// The specified command has some dependancies that need
            	// to be checked before to register it in the command list
            	Dependency dependancy = method.getAnnotation(Dependency.class);
            	
            	if ((dependancy.console().equals(UrT42Console.class)) && (!this.console.getClass().equals(UrT42Console.class))) {
            		this.log.debug("Skipping command !" + name + " registration. !" + name + " is available as from Urban Terror 4.2");
            		return;
            	}
            	
            }
            
            RegisteredCommand command = new RegisteredCommand(method, this, minGroup);
            this.regcommands.put(name, alias, command);
            this.debug("Registered command [ name : " + name + " | alias : " + alias + " | minLevel : " + minGroup.level + " ]");
            
        } catch (ClassNotFoundException | SQLException | NoSuchMethodException | SecurityException e) {
            
            // Logging the exception
            this.error("Unable to register command !" + name, e);
        
        } catch (CommandRegisterException e) {
            
            // Log a warning so the user will notice
            this.warn("Unable to register command", e);
        
        }
        
    }
    
    
    /**
     * Register an <tt>Event</tt> for this <tt>Plugin</tt>
     * 
     * @author Daniele Pantaleone
     * @param  type The <tt>Event</tt> type
     * @param  handler The name of the <tt>Method</tt> to be run when such <tt>Event</tt> is processed
     * @param  param The input parameter of the <tt>Method</tt> to be run on <tt>Event</tt> processing
     **/
    protected void registerEvent(EventType type, String handler, Class<?> param) {
        
        try {
            
            Method method = this.getClass().getMethod(handler, param);
            RegisteredEvent regevent = new RegisteredEvent(method, this);
            
            // Check if the same event has already been registered
            // for this plugin and skip registration if necessary.
            if (this.regevents.containsEntry(type, regevent)) {
                this.debug("Skipping event registration: " + type + " is already registered for this plugin");
                return;
            }
 
            this.regevents.put(type, regevent);
            this.debug("Registered event: " + type);
            
        } catch (NoSuchMethodException | SecurityException e) {
            
            // Logging the exception
            this.error("Unable to register event: " + type, e);
            
        }
        
    }
    
    
    /**
     * Schedule the execution of a <tt>Method</tt><br>
     * For this to be working correctly, the <tt>Method</tt> to be executed
     * must have no input parameter specified. FIXME!
     * 
     * @author Daniele Pantaleone
     * @param  name A visual identifier which identifies the cron job
     * @param  handler The name of the <tt>Method</tt> to be added in the schedule
     * @param  delay Number of milliseconds before the 1st <tt>Method</tt> invoke
     * @param  period Number of milliseconds between each <tt>Method</tt> invoke
     **/
    protected void addCron(String name, final String handler, long delay, long period) {
         
        try {
            
            // Create a new Timer object for this cronjob
            // in which we will store the method executuon call
            this.schedule.put(name, new Timer(name));
            this.schedule.get(name).scheduleAtFixedRate(new Cron(this.orion, handler, this), delay, period);
            
        } catch (NoSuchMethodException | SecurityException e) {
            
            // Logging the Exception
            this.error("Unable to create cronjob [ method : " + handler + " ]", e);
            
        }
        
    }
    
    
    /**
     * Return the <tt>Plugin</tt> matching the given name
     * 
     * @author Daniele Pantaleone
     * @param  name The <tt>Plugin</tt> name
     * @throws PluginNotFoundException If the <tt>Plugin</tt> is not loaded
     * @return The pre-initialized <tt>Plugin</tt> matching the given name
     **/
    public Plugin getPlugin(String name) throws PluginNotFoundException {
        
        if (!this.plugins.containsKey(name))
            throw new PluginNotFoundException("Unable to find plugin [ " + Character.toUpperCase(name.charAt(0)) + name.substring(1).toLowerCase() + " ]");
        
        return this.plugins.get(name);
        
    }
    
    
    /**
     * Create a new object according to the specified <tt>Plugin</tt> name
     * Invoke the constructor and return back the initialized object
     * 
     * @author Daniele Pantaleone
     * @param  name The name of the <tt>Plugin</tt>
     * @param  config The <tt>Plugin</tt> configuration file object
     * @param  orion <tt>Orion</tt> object reference
     * @throws ClassNotFoundException If the <tt>Plugin</tt> class fails in being loaded at runtime 
     * @throws NoSuchMethodException If a matching <tt>Method</tt> is not found
     * @throws InvocationTargetException If the underlying constructor throws an <tt>Exception</tt>
     * @throws IllegalArgumentException If the number of actual and formal parameters differ or if an unwrapping conversion for primitive arguments fails 
     * @throws IllegalAccessException If plugin <tt>Constructor</tt> object is enforcing Java language access control and the underlying constructor is inaccessible 
     * @throws InstantiationException If the <tt>Class</tt> that declares the underlying constructor represents an abstract class
     * @throws SecurityException If there is a violation while calling a non visible method (private/protected)
     * @return An initialized <tt>Plugin</tt>
     **/
    public static Plugin getPlugin(String name, Configuration config, Orion orion) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        
        // Loading the class using the configuration file name. Invoking the constructor and returning a initialized Plugin object
        Class<?> pluginClass = Class.forName("com.orion.plugin." + Character.toUpperCase(name.charAt(0)) + name.substring(1).toLowerCase() + "Plugin");
        Constructor<?> construct = pluginClass.getConstructor(Configuration.class, Orion.class);
        Plugin plugin = (Plugin)construct.newInstance(config, orion);
        return plugin;
    
    }
    
    
    //////////////////////////////
    // CUSTOM LOGGING FUNCTIONS //
    //////////////////////////////
    
    
    /**
     * Log a "Debug" message
     * 
     * @author Daniele Pantaleone
     * @param  message The message to be logged
     */
    protected void debug(String message){
        this.log.debug("[" + this.getClass().getSimpleName() + "] " + message);
    }
    
    
    /**
     * Log a "Debug" message
     * 
     * @author Daniele Pantaleone
     * @param  message The message to be logged
     * @param  cause A <tt>Throwable</tt> cause
     */
    protected void debug(String message, Throwable cause){
        this.log.debug("[" + this.getClass().getSimpleName() + "] " + message, cause);
    }
    
    
    /**
     * Log a "Debug" message
     * 
     * @author Daniele Pantaleone
     * @param  cause A <tt>Throwable</tt> cause
     */
    protected void debug(Throwable cause){
        this.log.debug("[" + this.getClass().getSimpleName() + "]", cause);
    }
    
    
    /**
     * Log an "Error" message
     * 
     * @author Daniele Pantaleone
     * @param  message The message to be logged
     */
    protected void error(String message){
        this.log.error("[" + this.getClass().getSimpleName() + "] " + message);
    }
    
    
    /**
     * Log an "Error" message
     * 
     * @author Daniele Pantaleone
     * @param  message The message to be logged
     * @param  cause A <tt>Throwable</tt> cause
     */
    protected void error(String message, Throwable cause){
        this.log.error("[" + this.getClass().getSimpleName() + "] " + message, cause);
    }
    
    
    /**
     * Log a "Error" message
     * 
     * @author Daniele Pantaleone
     * @param  cause A <tt>Throwable</tt> cause
     */
    protected void error(Throwable cause){
        this.log.error("[" + this.getClass().getSimpleName() + "]", cause);
    }
    
    
    /**
     * Log a "Fatal" message
     * 
     * @author Daniele Pantaleone
     * @param  message The message to be logged
     */
    protected void fatal(String message){
        this.log.fatal("[" + this.getClass().getSimpleName() + "] " + message);
    }
    
    
    /**
     * Log a "Fatal" message
     * 
     * @author Daniele Pantaleone
     * @param  message The message to be logged
     * @param  cause A <tt>Throwable</tt> cause
     */
    protected void fatal(String message, Throwable cause){
        this.log.fatal("[" + this.getClass().getSimpleName() + "] " + message, cause);
    }


    /**
     * Log a "Fatal" message
     * 
     * @author Daniele Pantaleone
     * @param  cause A <tt>Throwable</tt> cause
     */
    protected void fatal(Throwable cause){
        this.log.fatal("[" + this.getClass().getSimpleName() + "]", cause);
    }
    
    
    /**
     * Log an "Info" message
     * 
     * @author Daniele Pantaleone
     * @param  message The message to be logged
     */
    protected void info(String message){
        this.log.info("[" + this.getClass().getSimpleName() + "] " + message);
    }
    
    
    /**
     * Log a "Info" message
     * 
     * @author Daniele Pantaleone
     * @param  message The message to be logged
     * @param  cause A <tt>Throwable</tt> cause
     */
    protected void info(String message, Throwable cause){
        this.log.info("[" + this.getClass().getSimpleName() + "] " + message, cause);
    }
    
    
    /**
     * Log a "Info" message
     * 
     * @author Daniele Pantaleone
     * @param  cause A <tt>Throwable</tt> cause
     */
    protected void info(Throwable cause){
        this.log.info("[" + this.getClass().getSimpleName() + "]", cause);
    }
    
    
    /**
     * Log a "Trace" message
     * 
     * @author Daniele Pantaleone
     * @param  message The message to be logged
     */
    protected void trace(String message){
        this.log.trace("[" + this.getClass().getSimpleName() + "] " + message);
    }
    
    
    /**
     * Log a "Trace" message
     * 
     * @author Daniele Pantaleone
     * @param  message The message to be logged
     * @param  cause A <tt>Throwable</tt> cause
     */
    protected void trace(String message, Throwable cause){
        this.log.trace("[" + this.getClass().getSimpleName() + "] " + message, cause);
    }
    
    
    /**
     * Log a "Trace" message
     * 
     * @author Daniele Pantaleone
     * @param  cause A <tt>Throwable</tt> cause
     */
    protected void trace(Throwable cause){
        this.log.trace("[" + this.getClass().getSimpleName() + "]", cause);
    }
    
    
    /**
     * Log a "Warn" message
     * 
     * @author Daniele Pantaleone
     * @param  message The message to be logged
     */
    protected void warn(String message){
        this.log.warn("[" + this.getClass().getSimpleName() + "] " + message);
    }
    
    
    /**
     * Log a "Warn" message
     * 
     * @author Daniele Pantaleone
     * @param  message The message to be logged
     * @param  cause A <tt>Throwable</tt> cause
     */
    protected void warn(String message, Throwable cause){
        this.log.warn("[" + this.getClass().getSimpleName() + "] " + message, cause);
    }
    
    
    /**
     * Log a "Warn" message
     * 
     * @author Daniele Pantaleone
     * @param  cause A <tt>Throwable</tt> cause
     */
    protected void warn(Throwable cause){
        this.log.warn("[" + this.getClass().getSimpleName() + "]", cause);
    }

}
