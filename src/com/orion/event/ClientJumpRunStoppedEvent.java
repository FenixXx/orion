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
 * @copyright   Daniele Pantaleone, 1 February, 2013
 * @package     com.orion.event
 **/

package com.orion.event;

import com.orion.domain.Client;

public class ClientJumpRunStoppedEvent extends Event {

    private final Client client;
    private final int way;
    private final int millis;
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who who started the jump run
     * @param  way The number of the jump run way
     * @param  millis The time performed by the client
     **/
    public ClientJumpRunStoppedEvent(Client client, int way, int millis) {  
        super(EventType.EVT_CLIENT_JUMP_RUN_STOPPED);
        this.client = client;
        this.way = way;
        this.millis = millis;
    }
    
    
    /**
     * Return the <tt>Client</tt> who started the jump run
     * 
     * @author Daniele Pantaleone
     * @return The <tt>Client</tt> who started the jump run
     **/
    public Client getClient() {
        return this.client;
    }
    
    
    /**
     * Return the number of the jump way
     * 
     * @author Daniele Pantaleone
     * @return The number of the jump way
     **/
    public int getWay() {
        return this.way;
    }
    
    
    /**
     * Return the amount of milliseconds of the jump run
     * 
     * @author Daniele Pantaleone
     * @return The amount of milliseconds of the jump run
     **/
    public int getMillis() {
        return this.millis;
    }
    
}