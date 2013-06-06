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
