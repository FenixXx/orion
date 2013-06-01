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
 * @copyright   Daniele Pantaleone, 8 September, 2012
 * @package     com.orion.event
 **/

package com.orion.event;

import com.orion.domain.Client;

public class ClientVoteEvent extends Event {

    private final Client client;
    private final int data;
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone 
     * @param  client The <tt>Client</tt> who voted
     * @param  data The vote value
     **/
    public ClientVoteEvent(Client client, int data) {  
        super(EventType.EVT_CLIENT_VOTE);
        this.client = client;
        this.data = data;
    }
    
    
    /**
     * Return the <tt>Client</tt> who voted
     * 
     * @author Daniele Pantaleone
     * @return The <tt>Client</tt> who voted
     **/
    public Client getClient() {
        return this.client;
    }
    
    
    /**
     * Return the vote value
     * 
     * @author Daniele Pantaleone
     * @return The vote value
     **/
    public int getData() {
        return this.data;
    }
    
       
}