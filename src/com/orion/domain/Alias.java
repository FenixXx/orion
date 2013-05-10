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
 * @author      Mathias Van Malderen, Daniele Pantaleone
 * @version     1.0
 * @copyright   Mathias Van Malderen, 05 October, 2012
 * @package     com.orion.domain
 **/

package com.orion.domain;

import org.joda.time.DateTime;

public class Alias {
    
    public int id;
    public Client client;
    public String name;
    public int num_used;
    public DateTime time_add;
    public DateTime time_edit;
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> this <tt>Alias</tt> belongs to
     * @param  name The <tt>Alias</tt> name
     **/
    public Alias(Client client, String name) {
        this.client = client;
        this.name = name;
        this.num_used = 1;
    }
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  id The <tt>Alias</tt> id
     * @param  client The <tt>Client</tt> this <tt>Alias</tt> belongs to
     * @param  name The <tt>Alias</tt> name
     * @param  num_used A counter which holds the number of time this <tt>Alias</tt> has been used
     * @param  time_add The <tt>DateTime</tt> when this <tt>Alias</tt> has been created
     * @param  time_edit The <tt>DateTime</tt> when this <tt>Alias</tt> has been last edited
     **/
    public Alias(int id, Client client, String name, int num_used, DateTime time_add, DateTime time_edit) {
        this.id = id;
        this.client = client;
        this.name = name;
        this.num_used = num_used;
        this.time_add = time_add;
        this.time_edit = time_edit;
    }
    
    
    /**
     * <tt>String</tt> object representation
     * 
     * @author Daniele Pantaleone
     * @return A <tt>String</tt> representing the content of this object
     **/
    public String toString() {
        return "[ id : " + this.id + " | client : " + this.client.id + " | name : " + this.name + " | num_used : " + this.num_used + " | time_add : " + this.time_add.toString() + " | time_edit : " + this.time_edit.toString() + " ]";   
    }
    
}