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
 * @author      Daniele Pantaleone
 * @version     1.1
 * @copyright   Daniele Pantaleone, 12 February, 2013
 * @package     com.orion.event
 **/

package com.orion.event;

import com.orion.domain.Client;

public class ClientPositionSaveEvent extends Event {

    private final Client client;
    private final String location;
    private final float x;
    private final float y;
    private final float z;
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone 
     * @param  client The <tt>Client</tt> who saved a position
     * @param  x The X coordinate
     * @param  y The Y coordinate
     * @param  z The Z coordinate
     * @param  location The map location name
     **/
    public ClientPositionSaveEvent(Client client, float x, float y, float z, String location) {
        super(EventType.EVT_CLIENT_POSITION_SAVE);
        this.client = client;
        this.location = location;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    
    /**
     * Return the <tt>Client</tt> who saved a position
     * 
     * @author Daniele Pantaleone
     * @return The <tt>Client</tt> who saved a position
     **/
    public Client getClient() {
        return this.client;
    }
    
    
    /**
     * Return the map location name
     * 
     * @author Daniele Pantaleone
     * @return The map location name
     **/
    public String getLocation() {
        return this.location;
    }
    
    
    /**
     * Return the X coordinate
     * 
     * @author Daniele Pantaleone
     * @return The X coordinate
     **/
    public float getX() {
        return this.x;
    }
    
    
    /**
     * Return the Y coordinate
     * 
     * @author Daniele Pantaleone
     * @return The Y coordinate
     **/
    public float getY() {
        return this.y;
    }
    
    
    /**
     * Return the Z coordinate
     * 
     * @author Daniele Pantaleone
     * @return The Z coordinate
     **/
    public float getZ() {
        return this.z;
    }
    
}