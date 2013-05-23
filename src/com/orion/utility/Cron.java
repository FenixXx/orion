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
 * @package     com.orion.utility
 **/

package com.orion.utility;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.TimerTask;

import org.apache.commons.logging.Log;

import com.orion.bot.Orion;
import com.orion.plugin.Plugin;

public class Cron extends TimerTask {
    
    private final Log log;
    private final Plugin plugin;
    private final Method method;
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  orion <tt>Orion</tt> object reference
     * @param  handler The name of the <tt>Method</tt> to look for execution
     * @param  plugin The plugin object from which to invoke the <tt>Method</tt>
     * @throws NoSuchMethodException If a matching <tt>Method</tt> is not found in the given <tt>Plugin</tt> object
     * @throws SecurityException If there is a violation while calling a non visible <tt>Method</tt> (private/protected)
     **/
    public Cron(Orion orion, String handler, Plugin plugin) throws NoSuchMethodException, SecurityException {
        
        // Retrieving the method from the plugin object
        this.method = plugin.getClass().getMethod(handler);
        this.plugin = plugin;
        this.log = orion.log;
        
    }
    
    
    /**
     * Runnable implementation
     * 
     * @author Daniele Pantaleone
     **/
    @Override
    public void run() {
        
        try {
            
            // Invoking the method
            this.method.invoke(this.plugin);
            
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            
            // Logging the Exception
            this.log.error("Unable to execute cronjob", e);
            return;
            
        }
        
    }

}
