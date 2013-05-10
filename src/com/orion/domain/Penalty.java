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
 * @copyright   Daniele Pantaleone, 05 October, 2012
 * @package     com.orion.domain
 **/

package com.orion.domain;

import org.joda.time.DateTime;

import com.orion.bot.PenaltyType;

public class Penalty {
    
    public int id;
    public Client client;
    public Client admin;
    public PenaltyType type;
    public boolean active;
    public String reason;
    public DateTime time_add;
    public DateTime time_edit;
    public DateTime time_expire;
  
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who suffered this <tt>Penalty</tt>
     * @param  type The type of the <tt>Penalty</tt>
     **/
    public Penalty(Client client, PenaltyType type) {
        this.client = client;
        this.type = type;
        this.active = true;
    }
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who suffered this <tt>Penalty</tt>
     * @param  type The type of the <tt>Penalty</tt>
     * @param  reason The reason why this <tt>Penalty</tt> has been issued
     **/
    public Penalty(Client client, PenaltyType type, String reason) {
        this.client = client;
        this.type = type;
        this.active = true;
        this.reason = reason;
    }
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who suffered this <tt>Penalty</tt>
     * @param  type The type of the <tt>Penalty</tt>
     * @param  time_expire The <tt>DateTime</tt> when this <tt>Penalty</tt> will expire
     **/
    public Penalty(Client client, PenaltyType type, DateTime time_expire) {
        this.client = client;
        this.type = type;
        this.active = true;
        this.time_expire = time_expire;
    }
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who suffered this <tt>Penalty</tt>
     * @param  type The type of the <tt>Penalty</tt>
     * @param  reason The reason why this <tt>Penalty</tt> has been issued
     * @param  time_expire The <tt>DateTime</tt> when this <tt>Penalty</tt> will expire
     **/
    public Penalty(Client client, PenaltyType type, String reason, DateTime time_expire) {
        this.client = client;
        this.type = type;
        this.active = true;
        this.reason = reason;
        this.time_expire = time_expire;
    }
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who suffered this <tt>Penalty</tt>
     * @param  admin The <tt>Client</tt> who issued this <tt>Penalty</tt>
     * @param  type The type of the <tt>Penalty</tt>
     * @param  reason The reason why this <tt>Penalty</tt> has been issued
     **/
    public Penalty(Client client, Client admin, PenaltyType type, String reason) {
        this.client = client;
        this.admin = admin;
        this.type = type;
        this.active = true;
        this.reason = reason;
    }
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who suffered this <tt>Penalty</tt>
     * @param  admin The <tt>Client</tt> who issued this <tt>Penalty</tt>
     * @param  type The type of the <tt>Penalty</tt>
     * @param  reason The reason why this <tt>Penalty</tt> has been issued
     * @param  time_expire The <tt>DateTime</tt> when this <tt>Penalty</tt> will expire
     **/
    public Penalty(Client client, Client admin, PenaltyType type, String reason, DateTime time_expire) {
        this.client = client;
        this.admin = admin;
        this.type = type;
        this.active = true;
        this.reason = reason;
        this.time_expire = time_expire;
    }

    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  id The <tt>Penalty</tt> id
     * @param  client The <tt>Client</tt> who suffered this <tt>Penalty</tt>
     * @param  type The type of the <tt>Penalty</tt>
     * @param  active <tt>true</tt> if this <tt>Penalty</tt> is active, <tt>false</tt> otherwise
     * @param  reason The reason why this <tt>Penalty</tt> has been issued
     * @param  time_add The <tt>DateTime</tt> when this <tt>Penalty</tt> has been created
     * @param  time_edit The <tt>DateTime</tt> when this <tt>Penalty</tt> has been last edited
     * @param  time_expire The <tt>DateTime</tt> when this <tt>Penalty</tt> will expire
     **/
    public Penalty(int id, Client client, PenaltyType type, boolean active, String reason, DateTime time_add, DateTime time_edit, DateTime time_expire) {
        this.id = id;
        this.client = client;
        this.type = type;
        this.active = active;
        this.reason = reason;
        this.time_add = time_add;
        this.time_edit = time_edit;
        this.time_expire = time_expire;
    }
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  id The <tt>Penalty</tt> id
     * @param  client The <tt>Client</tt> who suffered this <tt>Penalty</tt>
     * @param  admin The <tt>Client</tt> who issued this <tt>Penalty</tt>
     * @param  type The type of the <tt>Penalty</tt>
     * @param  active <tt>true</tt> if this <tt>Penalty</tt> is active, <tt>false</tt> otherwise
     * @param  reason The reason why this <tt>Penalty</tt> has been issued
     * @param  time_add The <tt>DateTime</tt> when this <tt>Penalty</tt> has been created
     * @param  time_edit The <tt>DateTime</tt> when this <tt>Penalty</tt> has been last edited
     * @param  time_expire The <tt>DateTime</tt> when this <tt>Penalty</tt> will expire
     **/
    public Penalty(int id, Client client, Client admin, PenaltyType type, boolean active, String reason, DateTime time_add, DateTime time_edit, DateTime time_expire) {
        this.id = id;
        this.client = client;
        this.admin = admin;
        this.type = type;
        this.active = active;
        this.reason = reason;
        this.time_add = time_add;
        this.time_edit = time_edit;
        this.time_expire = time_expire;
    }
    
    
    /**
     * <tt>String</tt> object representation
     * 
     * @author Daniele Pantaleone
     * @return A <tt>String</tt> representing the content of this object
     **/
    public String toString() {
        return "[ id : " + this.id + " | client : " + this.client.id + " | admin : " + this.admin.id + " | type : " + this.type.name() + " | active : " + this.active + " | reason: " + this.reason + " | time_add : " + this.time_add.toString() + " | time_edit : " + this.time_edit.toString() + " | time_expire : " + ((this.time_expire != null) ? this.time_expire.toString() : "PERMANENT") + " ]";
    }
    
}