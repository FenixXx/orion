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

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import com.orion.urt.Team;

public class Client {
    
	// Fields on storage
    public int id;
    public Group group;
    public String name;
    public int connections;
    public InetAddress ip;
    public String guid;
    public String auth;
    
    public DateTime time_add;
    public DateTime time_edit;
    
    // Fields on object
    public Integer slot;
    public String gear;
    public Team team;
    
    public boolean bot;
    
    // To store plugins' variables
    private Map<String, Object> vars;
    
    
    /**
     * Object contructor
     * 
     * @author Daniele Pantaleone
     **/
    public Client() {
        
        this.name = "EmptyNameDefault";
        this.team = Team.FREE;
        this.slot = null;
        this.bot = false;
        this.vars = new HashMap<String, Object>();
    
    }
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  ip The <tt>Client</tt> IP address
     * @param  guid The <tt>Client</tt> guid <tt>String</tt>
     * @param  bot A <tt>boolean</tt> value which specify if this client is a bot
     **/
    public Client(InetAddress ip, String guid, boolean bot) {
        
        this.name = "EmptyNameDefault";
        this.ip = ip;
        this.guid = guid;
        this.bot = bot;
        this.team = Team.FREE;
        this.slot = null;
        this.vars = new HashMap<String, Object>();
        
    }
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  group The <tt>Group</tt> this <tt>Client</tt> belongs to
     * @param  guid The <tt>Client</tt> guid <tt>String</tt>
     **/
    public Client(Group group, String guid) {
        
        this.name = "EmptyNameDefault";
        this.group = group;
        this.guid = guid;
        this.team = Team.FREE;
        this.slot = null;
        this.bot = false;
        this.vars = new HashMap<String, Object>();
        
    }
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  group The <tt>Group</tt> this <tt>Client</tt> belongs to
     * @param  guid The <tt>Client</tt> guid <tt>String</tt>
     * @param  auth The <tt>Client</tt> auth login
     **/
    public Client(Group group, String guid, String auth) {
        
        this.name = "EmptyNameDefault";
        this.group = group;
        this.guid = guid;
        this.auth = auth;
        this.team = Team.FREE;
        this.slot = null;
        this.bot = false;
        this.vars = new HashMap<String, Object>();
        
    }
 
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  id The <tt>Client</tt> id
     * @param  group The <tt>Group</tt> this <tt>Client</tt> belongs to
     * @param  name The <tt>Client</tt> name
     * @param  connections A counter which hold the number of time this <tt>Client</tt> connected to the game server
     * @param  ip The <tt>Client</tt> IP address
     * @param  guid The <tt>Client</tt> guid <tt>String</tt>
     * @param  time_add The <tt>DateTime</tt> when this <tt>Client</tt> has been created
     * @param  time_edit The <tt>DateTime</tt> when this <tt>Client</tt> has been last edited
     **/
    public Client(int id, Group group, String name, int connections, InetAddress ip, String guid, DateTime time_add, DateTime time_edit) {
        
        this.id = id;
        this.group = group;
        this.name = name;
        this.connections = connections;
        this.ip = ip;
        this.guid = guid;
        this.time_add = time_add;
        this.time_edit = time_edit;
        this.team = Team.FREE;
        this.auth = null;
        this.slot = null;
        this.bot = false;
        this.vars = new HashMap<String, Object>();
        
    }
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  id The <tt>Client</tt> id
     * @param  group The <tt>Group</tt> this client belongs to
     * @param  name The <tt>Client</tt> name
     * @param  connections A counter which hold the number of time this <tt>Client</tt> connected to the game server
     * @param  ip The <tt>Client</tt> IP address
     * @param  guid The <tt>Client</tt> guid <tt>String</tt>
     * @param  auth The <tt>Client</tt> auth login
     * @param  time_add The <tt>DateTime</tt> when this <tt>Client</tt> has been created
     * @param  time_edit The <tt>DateTime</tt> when this <tt>Client</tt> has been last edited
     **/
    public Client(int id, Group group, String name, int connections, InetAddress ip, String guid, String auth, DateTime time_add, DateTime time_edit) {
        
        this.id = id;
        this.group = group;
        this.name = name;
        this.connections = connections;
        this.ip = ip;
        this.guid = guid;
        this.auth = auth;
        this.time_add = time_add;
        this.time_edit = time_edit;
        this.team = Team.FREE;
        this.slot = null;
        this.bot = false;
        this.vars = new HashMap<String, Object>();
        
    }
    
    
    /**
     * Remove and return the variable associated to the given key from the <tt>HashMap</tt>
     * 
     * @author Daniele Pantaleone
     * @param  key The key from which to perform the delete operation
     * @return The <tt>Client</tt> variable associated to the specified key or <tt>null</tt> if there is no match
     **/
    public Object delVar(String key) {
        return this.vars.remove(key);
    }
    
    
    /**
     * Return the <tt>Client</tt> variable associated to the given key
     * 
     * @author Daniele Pantaleone
     * @param  key The key from which to fetch the variable in the <tt>HashMap</tt>
     * @return The <tt>Client</tt> variable associated to the specified key or <tt>null</tt> if there is no match
     **/
    public Object getVar(String key) {
        return this.vars.containsKey(key) ? this.vars.get(key) : null;
    }
    
    
    /**
     * Tells if the <tt>Client</tt> is in the specified <tt>Group</tt>
     * 
     * @author Daniele Pantaleone
     * @param  group The <tt>Group</tt> object to match
     * @return <tt>true</tt> if the <tt>Client</tt> belong to the specified <tt>Group</tt>, <tt>false</tt> otherwise
     **/
    public boolean inGroup(Group group) {
    	return this.group.level == group.level ? true : false;
    }
    
    
    /**
     * Tells wheter the given key has a dynamic created variable associated
     * 
     * @author Daniele Pantaleone
     * @param  key The key from which to perform the search in the <tt>HashMap</tt>
     * @return <tt>true</tt> if the key matches a variable in the <tt>HashMap</tt>, <tt>false</tt> otherwise
     **/
    public boolean isVar(String key) {
        return this.vars.containsKey(key);
    }
    
    
    /**
     * Store a dynamically created variable in the <tt>Client</tt> object for later use
     * 
     * @author Daniele Pantaleone
     * @param  key The key to which associate the given variable
     * @param  value The variable to store
     **/
    public void setVar(String key, Object value) {
        this.vars.put(key, value);
    }
    
    
    /**
     * String object representation
     * 
     * @author Daniele Pantaleone
     * @return A <tt>String</tt> representing the content of this object
     **/
    public String toString() {
        if (this.bot) return "[ id : BOT_" + this.slot + " | name : " + this.name + " | level : " + this.group.level + " ]";      
        return "[ id : " + this.id + " | name : " + this.name + " | level : " + this.group.level + " | ip : " + this.ip.getHostAddress() + " | guid : " + this.guid + " | auth : " + this.auth + " | connections: " + this.connections + " | time_add: " + this.time_add.toString() + " | time_edit: " + this.time_edit.toString() + " ]";
    }
    
}