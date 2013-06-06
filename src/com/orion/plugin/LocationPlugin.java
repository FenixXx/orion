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
 * @author      Daniele Pantaleone
 * @version     1.0
 * @copyright   Daniele Pantaleone, 18 March, 2013
 * @package     com.orion.plugin
 **/

package com.orion.plugin;

import java.io.IOException;

import java.util.Map;

import com.maxmind.geoip.Country;
import com.maxmind.geoip.LookupService;
import com.orion.annotation.Usage;
import com.orion.bot.Orion;
import com.orion.command.Command;
import com.orion.domain.Client;
import com.orion.event.ClientConnectEvent;
import com.orion.event.EventType;
import com.orion.exception.CommandRuntimeException;
import com.orion.exception.CommandSyntaxException;
import com.orion.exception.EventInterruptedException;
import com.orion.utility.Configuration;
import com.orion.utility.MessageFormat;

public class LocationPlugin extends Plugin {
    
    public Map<String, String> messages;
    public LookupService geoip = null;
    public boolean announce;
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  config Plugin <tt>Configuration</tt> object
     * @param  orion <tt>Orion</tt> object reference
     **/
    public LocationPlugin(Configuration config, Orion orion) {
        super(config, orion);
    }
    
    
    /**
     * Load the <tt>Plugin</tt> configuration file and fill <tt>Plugin</tt> attributes
     * 
     * @author Daniele Pantaleone
     **/
    @Override
    public void onLoadConfig() { 
        
        this.debug("Loading plugin configuration...");
        
        this.messages = this.config.getMap("messages");
        this.announce = this.config.getBoolean("settings", "announce", true);
        
        try {
            
            // Loading the GeoIP.dat countries database on which to perform country queries
            // If the binary file cannot be loaded the plugin is going to be disabled on startup since it will just waste resources
            this.geoip = new LookupService(this.config.getString("settings", "geoip_path", "res/GeoIP.dat"), LookupService.GEOIP_MEMORY_CACHE);
            
        } catch (IOException e) {
            
            // Logging the Exception
            this.error("Unable to load GeoIP.dat country database", e);
            return;
            
        }
        
    }
    
    
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
    @Override
    public void onStartup()  { 
        
        if (this.geoip == null) {
            // Country database is not available
            // Disabling the plugin since it will not work
            this.warn("GeoIP.dat country database is not available. Disabling the plugin");
            this.setEnabled(false);
            return;
        }
        
        this.debug("Starting plugin...");
        
        // Getting the command section from configuration file
        Map<String, String> commands = this.config.getMap("commands");
        
        // Iteraring throug all the commands so we can register them
        for (Map.Entry<String, String> entry : commands.entrySet()) {
            
            String alias  = null;
            String name   = entry.getKey();
            String group  = entry.getValue();
            
            if (name.contains("-")) {
                String handler[] = name.split("-", 2);
                name  = handler[0];
                alias = handler[1];
            }
            
            // Registering the command
            this.registerCommand(name, alias, group);
                
        }
        
        // Registering our specific events
        this.registerEvent(EventType.EVT_CLIENT_CONNECT, "onClientConnect", ClientConnectEvent.class);
           
    }
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////// EVENTS SECTION //////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    /**
     * Performs operation on <tt>EVT_CLIENT_CONNECT</tt>
     * 
     * @author Daniele Pantaleone
     * @param  event The generated <tt>Event</tt>
     * @throws EventInterruptedException If the connecting <tt>Client</tt> is banned
     **/
    public void onClientConnect(ClientConnectEvent event) throws EventInterruptedException {
    
        // Copying the client reference
        Client client = event.client;
        
        // Storing the client country in the client object
        Country country = this.geoip.getCountry(client.ip);
        client.setVar("country", country);   
        
        // Exit if we do not have to announce
        // the player country in the game chat
        if (!this.announce) return;
        
        // Displaying the client country in the game chat using the configuration message pattern
        this.console.say(MessageFormat.format(this.messages.get("location_connect"), client.name, country.getName(), country.getCode()));
        
    }
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////// COMMANDS SECTION /////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    /**
     * Display the country of a player
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     * @throws CommandRuntimeException If there is <tt>Runtime</tt> error while processing the command
     **/
    @Usage(message="Display the country of a player", syntax="!country <client>")
    public void CmdCountry(Command command) throws CommandSyntaxException, CommandRuntimeException {
        
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() != 1) throw new CommandSyntaxException("Invalid syntax");
        
        // Retriving the correct client
        Client sclient = this.clients.getByMagic(command.client, command.getParamString(0));
        if (sclient == null) return;
        
        // Checking for a proper country object
        if (!sclient.isVar("country")) {
            // We do not have a Country object
            // Retrieve a new one for the specified client
            sclient.setVar("country", this.geoip.getCountry(sclient.ip));
        }
        
        // Retrieving the client country
        Country country = (Country) sclient.getVar("country");
        
        // Displaying the client country in the game chat using the configuration message pattern
        this.console.sayLoudOrPm(command, MessageFormat.format(this.messages.get("location_command"), sclient.name, country.getName(), country.getCode()));
        
    }
    

}
