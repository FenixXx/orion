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
 * @author      Daniele Pantaleone
 * @version     1.0
 * @copyright   Daniele Pantaleone, 18 April, 2013
 * @package     com.orion.plugin
 **/

package com.orion.plugin;

import java.util.List;

import com.orion.bot.Orion;
import com.orion.exception.ParserException;
import com.orion.utility.Configuration;

public class AdvPlugin extends Plugin {
    
    public long rate;
    public long delay;
    public List<String> ads;
    
    public int count = -1;
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  config Plugin <tt>Configuration</tt> object
     * @param  orion <tt>Orion</tt> object reference
     **/
    public AdvPlugin(Configuration config, Orion orion) {
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
        
        try {
            
            this.rate = this.config.getTime("settings", "rate", 30000);
            this.delay = this.config.getTime("settings", "delay", 60000);
            this.ads = this.config.getList("ads");
        
        } catch (ParserException e) {
            
            // Logging the Exception
            this.error("Malformed configuration file. Disabling the plugin", e);
            this.setEnabled(false);
            
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
        
        this.debug("Starting plugin...");
        this.addCron("adv", "advBroadcast", this.delay, this.rate);
        
    }
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////// HELPER METHODS //////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    /**
     * Broadcast an ADV message in the in-game chat
     * 
     * @author Daniele Pantaleone
     **/
    public void advBroadcast() {
        
        // Retrieve the correct adv index for the next spam
        this.count = (this.count >= this.ads.size()) ? 0 : this.count + 1;
        
        // Check if the adv is not an empty message
        // Otherwise, remove it from the list and increase the counter
        while (this.ads.get(this.count).trim().isEmpty()) {
            this.ads.remove(this.count);
            this.count++;
        }
        
        // Display the adv message in the game chat
        this.console.say(this.ads.get(this.count));
        
    }
    
    
}
