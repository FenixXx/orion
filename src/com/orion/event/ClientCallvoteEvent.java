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

import com.orion.domain.Callvote;
import com.orion.domain.Client;

public class ClientCallvoteEvent extends Event {

    private final Client client;
    private final Callvote callvote;
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who issued the vote
     * @param  callvote The <tt>Callvote</tt> object
     **/
    public ClientCallvoteEvent(Client client, Callvote callvote) {  
        super(EventType.EVT_CLIENT_CALLVOTE);
        this.client = client;
        this.callvote = callvote;
    }
    
    
    /**
     * Return the <tt>Client</tt> who issued the vote
     * 
     * @author Daniele Pantaleone
     * @return The <tt>Client</tt> who issued the vote
     **/
    public Client getClient() {
        return this.client;
    }
    
    
    /**
     * Return the <tt>Callvote</tt> object
     * 
     * @author Daniele Pantaleone
     * @return The <tt>Callvote</tt> object
     **/
    public Callvote getCallvote() {
        return this.callvote;
    }
    
}