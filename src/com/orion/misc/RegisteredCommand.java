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
 * @version     1..1
 * @copyright   Daniele Pantaleone, 4 September, 2013
 * @package     com.orion.misc
 **/

package com.orion.misc;

import java.lang.reflect.Method;

import com.orion.domain.Group;
import com.orion.plugin.Plugin;

public class RegisteredCommand {
    
    private Method method;
    private Plugin plugin;
    private Group  group;
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  method The <tt>Method</tt> to be mapped
     * @param  plugin The <tt>Plugin</tt> object from which to invoke the method
     * @param  group The minimum <tt>Group</tt> that can access this command
     **/
    public RegisteredCommand(Method method, Plugin plugin, Group group) {
        this.setMethod(method);
        this.setPlugin(plugin);
        this.setGroup(group);
    }
    
    
    /**
     * Return the <tt>Method</tt> to be executed
     * 
     * @author Daniele Pantaleone
     * @return The <tt>Method</tt> to be executed
     **/
    public Method getMethod() {
        return method;
    }

    
    /**
     * Return the <tt>Plugin</tt> from which to execute the <tt>Method</tt>
     * 
     * @author Daniele Pantaleone
     * @return The <tt>Plugin</tt> from which to execute the <tt>Method</tt>
     **/
    public Plugin getPlugin() {
        return plugin;
    }
    
    
    /**
     * Return the minimum <tt>Group</tt> that can access this command
     * 
     * @author Daniele Pantaleone
     * @return The minimum <tt>Group</tt> that can access this command
     **/
    public Group getGroup() {
        return group;
    }
    
    
    /**
     * Set the <tt>Method</tt> to be executed
     * 
     * @author Daniele Pantaleone
     * @param  method The <tt>Method</tt> to be executed
     **/
    public void setMethod(Method method) {
        this.method = method;
    }

    
    /**
     * Set the <tt>Plugin</tt> from which to execute the <tt>Method</tt>
     * 
     * @author Daniele Pantaleone
     * @param  plugin The <tt>Plugin</tt> from which to execute the <tt>Method</tt>
     **/
    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }
    
    
    /**
     * Set the minimum <tt>Group</tt> that can access this command
     * 
     * @author Daniele Pantaleone
     * @param  group The minimum <tt>Group</tt> that can access this command
     **/
    public void setGroup(Group group) {
        this.group = group;
    }

}
