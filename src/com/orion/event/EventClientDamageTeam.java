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
 * @copyright   Daniele Pantaleone, 19 January, 2013
 * @package     com.orion.event
 **/

package com.orion.event;

import com.orion.domain.Client;
import com.orion.urt.Hitlocation;
import com.orion.urt.Mod;


public class EventClientDamageTeam extends Event {

    public final Client attacker;
    public final Client victim;
    public final Hitlocation location;
    public final Mod mod ;
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone 
     * @param  client The <tt>Client</tt> who did the hit
     * @param  victim The <tt>Client</tt> who suffered the hit
     * @param  mod The Urban Terror <tt>Mod</tt> for this hit
     * @param  location The <tt>Hitlocation</tt> object
     **/
    public EventClientDamageTeam(Client attacker, Client victim, Mod mod, Hitlocation location) {
        super(EventType.EVT_CLIENT_DAMAGE_TEAM);
        this.attacker = attacker;
        this.victim = victim;
        this.mod = mod;
        this.location = location;
        
    }
       
}