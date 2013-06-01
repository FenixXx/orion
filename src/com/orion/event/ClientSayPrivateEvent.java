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
 * @copyright   Daniele Pantaleone, 15 July, 2012
 * @package     com.orion.event
 **/

package com.orion.event;

import com.orion.domain.Client;

public class ClientSayPrivateEvent extends Event {

    private final Client client;
    private final Client target;
    private final String message;
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone 
     * @param  client The <tt>Client</tt> who said something
     * @param  target The <tt>Client</tt> on which the /tell command has been performed
     * @param  message The sentence said
     **/
    public ClientSayPrivateEvent(Client client, Client target, String message) {
        super(EventType.EVT_CLIENT_SAY_PRIVATE);
        this.client = client;
        this.target = target;
        this.message = message;
    }
    
    
    /**
     * Return the <tt>Client</tt> who said something
     * 
     * @author Daniele Pantaleone
     * @return The <tt>Client</tt> who said something
     **/
    public Client getClient() {
        return this.client;
    }
    
    
    /**
     * Return the <tt>Client</tt> on which the /tell command has been performed
     * 
     * @author Daniele Pantaleone
     * @return The <tt>Client</tt> on which the /tell command has been performed
     **/
    public Client getTarget() {
        return this.target;
    }
    
    
    /**
     * Return the sentence said
     * 
     * @author Daniele Pantaleone
     * @return The sentence said
     **/
    public String getMessage() {
        return this.message;
    }
   
}