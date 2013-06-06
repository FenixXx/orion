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
