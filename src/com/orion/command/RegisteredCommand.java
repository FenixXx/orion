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
