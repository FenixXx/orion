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
 * @copyright   Daniele Pantaleone, 08 October, 2012
 * @package     co.orion.domain
 **/

package com.orion.domain;

import org.joda.time.DateTime;

public class Callvote {
    
    public int id;
    public Client client;
    public String type;
    public String data;
    public int yes;
    public int no;
    public DateTime time_add;
    public DateTime time_edit;
    
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> this <tt>Callvote</tt> belongs to
     * @param  type The <tt>Callvote</tt> type
     * @param  data The <tt>Callvote</tt> additional data
     **/
    public Callvote(Client client, String type, String data) {
    	this.client = client;
    	this.type = type;
    	this.data = data;
    	this.yes = 1;
    }
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  id The <tt>Callvote</tt> id
     * @param  client The <tt>Client</tt> this <tt>Callvote</tt> belongs to
     * @param  type The <tt>Callvote</tt> type
     * @param  data The <tt>Callvote</tt> additional data
     * @param  yes The amount of yes of this <tt>Callvote</tt>
     * @param  no The amount of no of this <tt>Callvote</tt>
     * @param  time_add The <tt>DateTime</tt> when this <tt>Callvote</tt> has been created
     * @param  time_edit The <tt>DateTime</tt> when this <tt>Callvote</tt> has been last edited
     **/
    public Callvote(int id, Client client, String type, String data, int yes, int no, DateTime time_add, DateTime time_edit) {
    	this.id = id;
    	this.client = client;
    	this.type = type;
    	this.data = data;
    	this.yes = yes;
    	this.no = no;
    	this.time_add = time_add;
    	this.time_edit = time_edit;
    }
    
    
    /**
     * String object representation
     * 
     * @author Daniele Pantaleone
     * @return A <tt>String</tt> representing the content of this object
     **/
    public String toString() {
        return "[ id : " + this.id + " | client : " + this.client.id + " | type : " + this.type + " | data : " + this.data + " | yes : " + this.yes + " | no : " + this.no + " | time_add : " + this.time_add.toString() + " | time_add : " + this.time_add.toString() + " ]";
    }
    
}