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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Orion. If not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 * 
 * @author      Daniele Pantaleone, Mathias Van Malderen
 * @version     1.1
 * @copyright   Daniele Pantaleone, Mathias Van Malderen, 02 July, 2012
 * @package     com.orion.event
 **/

package com.orion.event;

public abstract class Event {
    
    private final EventType type;
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone 
     * @param  type The <tt>EventType</tt>
     **/
    public Event(EventType type) {
        this.type = type;
    }
    
    
    /**
     * Return the type of the <tt>Event</tt>
     * 
     * @author Daniele Pantaleone
     * @return The type of the <tt>Event</tt>
     **/
    public EventType getType() {
        return this.type;
    }
      
}