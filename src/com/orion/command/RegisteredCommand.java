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
 * @copyright   Daniele Pantaleone, 15 February, 2013
 * @package     com.orion.command
 **/

package com.orion.command;

import java.lang.reflect.Method;

import com.orion.domain.Group;
import com.orion.plugin.Plugin;

public class RegisteredCommand {
    
    public Method method;
    public Plugin plugin;
    public Group  minGroup;
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  method The <tt>Method</tt> to be mapped
     * @param  plugin The <tt>Plugin</tt> object from which to invoke the method
     * @param  minGroup The minimum <tt>Group</tt> that can access this command
     **/
    public RegisteredCommand(Method method, Plugin plugin, Group minGroup) {
        this.method = method;
        this.plugin = plugin;
        this.minGroup = minGroup;
    }

}
