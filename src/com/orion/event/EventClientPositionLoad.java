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
 * @copyright   Daniele Pantaleone, 12 February, 2013
 * @package     com.orion.event
 **/

package com.orion.event;

import com.orion.domain.Client;

public class EventClientPositionLoad extends Event {

    public final Client client;
    public final String location;
    public final float x;
    public final float y;
    public final float z;
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone 
     * @param  client The <tt>Client</tt> who loaded his position
     * @param  x The X coordinate
     * @param  y The Y coordinate
     * @param  z The Z coordinate
     * @param  location The map location
     **/
    public EventClientPositionLoad(Client client, float x, float y, float z, String location) {
        super(EventType.EVT_CLIENT_POSITION_LOAD);
        this.client = client;
        this.location = location;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
}