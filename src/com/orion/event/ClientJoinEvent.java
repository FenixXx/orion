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
 * @version     1.1
 * @copyright   Daniele Pantaleone, 19 January, 2013
 * @package     com.orion.event
 **/

package com.orion.event;

import com.orion.domain.Client;

public class ClientJoinEvent extends Event {

    private final Client client;
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  client The joining <tt>Client</tt> object
     **/
    public ClientJoinEvent(Client client) {  
        super(EventType.EVT_CLIENT_JOIN);
        this.client = client;
    }
    
    
    /**
     * Return the joining <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @return The joining <tt>Client</tt>
     **/
    public Client getClient() {
        return this.client;
    }
    
}