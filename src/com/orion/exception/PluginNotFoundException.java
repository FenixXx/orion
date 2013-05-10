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
 * @copyright   Daniele Pantaleone, 16 February, 2013
 * @package     com.orion.exception
 **/

package com.orion.exception;

public class PluginNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     **/
    public PluginNotFoundException() {
        super();
    }
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  message The <tt>Exception</tt> message
     * @param  cause A <tt>Throwable</tt> cause
     **/
    public PluginNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  message The <tt>Exception</tt> message
     **/
    public PluginNotFoundException(String message) {
        super(message);
    }
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  cause A <tt>Throwable</tt> cause
     **/
    public PluginNotFoundException(Throwable cause) {
        super(cause);
    }
    
}