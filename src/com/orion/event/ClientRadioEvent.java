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

public class ClientRadioEvent extends Event {

    private final Client client;
    private final int msg_group;
    private final int msg_id;
    private final String location;
    private final String message;
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone 
     * @param  client The <tt>Client</tt> who issued the radio command
     * @param  msg_group The message group
     * @param  msg_id The message id
     * @param  location The map location name
     * @param  message The message attached to the radio command
     **/
    public ClientRadioEvent(Client client, int msg_group, int msg_id, String location, String message) {  
        super(EventType.EVT_CLIENT_RADIO);
        this.client = client;
        this.msg_group = msg_group;
        this.msg_id = msg_id;
        this.location = location;
        this.message = message;
    }
    
    
    /**
     * Return the <tt>Client</tt> who issued the radio command
     * 
     * @author Daniele Pantaleone
     * @return The <tt>Client</tt> who issued the radio command
     **/
    public Client getClient() {
        return this.client;
    }
    
    
    /**
     * Return the message group
     * 
     * @author Daniele Pantaleone
     * @return The message group
     **/
    public int getMsgGroup() {
        return this.msg_group;
    }
    
    
    /**
     * Return the message id
     * 
     * @author Daniele Pantaleone
     * @return The message id
     **/
    public int getMsgId() {
        return this.msg_id;
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
     * Return the message attached to the radio command
     * 
     * @author Daniele Pantaleone
     * @return The message attached to the radio command
     **/
    public String getMessage() {
        return this.message;
    }
       
}