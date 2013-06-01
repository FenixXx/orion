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
 * @copyright   Daniele Pantaleone, 02 July, 2012
 * @package     com.orion.event
 **/

package com.orion.event;

import com.orion.domain.Client;
import com.orion.urt.Mod;


public class ClientKillTeamEvent extends Event {

    public final Client client;
    public final Client victim;
    public final Mod mod;
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who performed the kill
     * @param  victim The <tt>Client</tt> who suffered the kill
     * @param  mod The Urban Terror <tt>Mod</tt> of the kill
     **/
    public ClientKillTeamEvent(Client client, Client victim, Mod mod) {
        super(EventType.EVT_CLIENT_KILL_TEAM);
        this.client = client;
        this.victim = victim;
        this.mod = mod;
    }
    
    
    /**
     * Return the <tt>Client</tt> who performed the kill
     * 
     * @author Daniele Pantaleone
     * @return The <tt>Client</tt> who performed the kill
     **/
    public Client getClient() {
        return this.client;
    }
    
    
    /**
     * Return the <tt>Client</tt> who suffered the kill
     * 
     * @author Daniele Pantaleone
     * @return The <tt>Client</tt> who suffered the kill
     **/
    public Client getVictim() {
        return this.victim;
    }
    
    
    /**
     * Return the <tt>Mod</tt> of the kill
     * 
     * @author Daniele Pantaleone
     * @return The <tt>Mod</tt> of the kill
     **/
    public Mod getMod() {
        return this.mod;
    }
  
}