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
 * @version     1.1
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
    
    private int id;
    private Group group;
    private String name;
    private int connections;
    private InetAddress ip;
    private String guid;
    private String auth;
    
    private DateTime time_add;
    private DateTime time_edit;
    
    private Integer slot;
    private String gear;
    private Team team;
    
    private boolean bot;
    
    private Map<String, Object> vars;
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     **/
    public Client() {
        this.setName("EmptyNameDefault");
        this.setTeam(Team.FREE);
        this.setSlot(null);
        this.setBot(false);
        this.vars = new HashMap<String, Object>();
    }
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  ip The <tt>Client</tt> IP address
     * @param  guid The <tt>Client</tt> GUID <tt>String</tt>
     * @param  bot A <tt>boolean</tt> value which specify if this client is a bot
     **/
    public Client(InetAddress ip, String guid, boolean bot) {
        this.setName("EmptyNameDefault");
        this.setIp(ip);
        this.setGuid(guid);
        this.setBot(bot);
        this.setTeam(Team.FREE);
        this.setSlot(null);
        this.vars = new HashMap<String, Object>();
    }
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  group The <tt>Group</tt> this <tt>Client</tt> belongs to
     * @param  guid The <tt>Client</tt> GUID <tt>String</tt>
     **/
    public Client(Group group, String guid) {
        this.setName("EmptyNameDefault");
        this.setGroup(group);
        this.setGuid(guid);
        this.setTeam(Team.FREE);
        this.setSlot(null);
        this.setBot(false);
        this.vars = new HashMap<String, Object>();
    }
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  group The <tt>Group</tt> this <tt>Client</tt> belongs to
     * @param  guid The <tt>Client</tt> GUID <tt>String</tt>
     * @param  auth The <tt>Client</tt> auth login
     **/
    public Client(Group group, String guid, String auth) {
        this.setName("EmptyNameDefault");
        this.setGroup(group);
        this.setGuid(guid);
        this.setAuth(auth);
        this.setTeam(Team.FREE);
        this.setSlot(null);
        this.setBot(false);
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
     * @param  guid The <tt>Client</tt> GUID <tt>String</tt>
     * @param  time_add The <tt>DateTime</tt> when this <tt>Client</tt> has been created
     * @param  time_edit The <tt>DateTime</tt> when this <tt>Client</tt> has been last edited
     **/
    public Client(int id, Group group, String name, int connections, InetAddress ip, String guid, DateTime time_add, DateTime time_edit) {
        this.setId(id);
        this.setGroup(group);
        this.setName(name);
        this.setConnections(connections);
        this.setIp(ip);
        this.setGuid(guid);
        this.setTimeAdd(time_add);
        this.setTimeEdit(time_edit);
        this.setTeam(Team.FREE);
        this.setAuth(null);
        this.setSlot(null);
        this.setBot(false);
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
     * @param  guid The <tt>Client</tt> GUID <tt>String</tt>
     * @param  auth The <tt>Client</tt> auth login
     * @param  time_add The <tt>DateTime</tt> when this <tt>Client</tt> has been created
     * @param  time_edit The <tt>DateTime</tt> when this <tt>Client</tt> has been last edited
     **/
    public Client(int id, Group group, String name, int connections, InetAddress ip, String guid, String auth, DateTime time_add, DateTime time_edit) {
        this.setId(id);
        this.setGroup(group);
        this.setName(name);
        this.setConnections(connections);
        this.setIp(ip);
        this.setGuid(guid);
        this.setAuth(auth);
        this.setTimeAdd(time_add);
        this.setTimeEdit(time_edit);
        this.setTeam(Team.FREE);
        this.setSlot(null);
        this.setBot(false);
        this.vars = new HashMap<String, Object>();
    }
    
    
    /**
     * Return the client id
     * 
     * @author Daniele Pantaleone
     * @return The client id
     **/
    public int getId() {
        return this.id;
    }

    
    /**
     * Return the client <tt>Group</tt>
     * 
     * @author Daniele Pantaleone
     * @return The client <tt>Group</tt>
     **/
    public Group getGroup() {
        return this.group;
    }
    
    
    /**
     * Return the client name
     * 
     * @author Daniele Pantaleone
     * @return The client name
     **/
    public String getName() {
        return this.name;
    }
    
    
    /**
     * Return the amount of time the client has connected
     * 
     * @author Daniele Pantaleone
     * @return The client number of connections
     **/
    public int getConnections() {
        return this.connections;
    }
    
    
    /**
     * Return the client ip address
     * 
     * @author Daniele Pantaleone
     * @return A <tt>InetAddress</tt> representing the client IP address
     **/
    public InetAddress getIp() {
        return this.ip;
    }
    
    
    /**
     * Return the client GUID <tt>String</tt>
     * 
     * @author Daniele Pantaleone
     * @return The client GUID <tt>String</tt>
     **/
    public String getGuid() {
        return this.guid;
    }
    
    
    /**
     * Return the client auth login if the client is authed or <tt>null</tt> otherwise
     * 
     * @author Daniele Pantaleone
     * @return The client auth login <tt>String</tt>
     **/
    public String getAuth() {
        return this.auth;
    }
    
    
    /**
     * Return a <tt>DateTime</tt> object representing the time when the client has been first seen
     * 
     * @author Daniele Pantaleone
     * @return A <tt>DateTime</tt> object representing the time when the client has been first seen
     **/
    public DateTime getTimeAdd() {
        return this.time_add;
    }
    
    
    /**
     * Return a <tt>DateTime</tt> object representing the client last time edit
     * 
     * @author Daniele Pantaleone
     * @return A <tt>DateTime</tt> object representing the client last time edit
     **/
    public DateTime getTimeEdit() {
        return this.time_edit;
    }
    
    
    /**
     * Return the client slot number
     * 
     * @author Daniele Pantaleone
     * @return The client slot number
     **/
    public Integer getSlot() {
        return this.slot;
    }
    
    
    /**
     * Return the client gear <tt>String</tt>
     * 
     * @author Daniele Pantaleone
     * @return The client gear <tt>String</tt>
     **/
    public String getGear() {
        return this.gear;
    }
    
    
    /**
     * Return the client <tt>Team</tt>
     * 
     * @author Daniele Pantaleone
     * @return The client <tt>Team</tt>
     **/
    public Team getTeam() {
        return this.team;
    }
    
    
    /**
     * Tell whether this client is a bot
     * 
     * @author Daniele Pantaleone
     * @return <tt>true</tt> if this client is a bot, <tt>false</tt> otherwise
     **/
    public boolean isBot() {
        return this.bot;
    }

    
    /**
     * Set the client id
     * 
     * @author Daniele Pantaleone
     * @param  id The client id
     **/
    public void setId(int id) {
        this.id = id;
    }

    
    /**
     * Set the client <tt>Group</tt>
     * 
     * @author Daniele Pantaleone
     * @param  group The client <tt>Group</tt>
     **/
    public void setGroup(Group group) {
        this.group = group;
    }


    /**
     * Set the client name
     * 
     * @author Daniele Pantaleone
     * @param  name The client name
     **/
    public void setName(String name) {
        // Remove color codes from the client name
        this.name = name.replaceAll("\\^[0-9]{1}", "");
    }

    
    /**
     * Set the client number of connections
     * 
     * @author Daniele Pantaleone
     * @param  connections Amount of time this client has connected
     **/
    public void setConnections(int connections) {
        this.connections = connections;
    }

    
    /**
     * Set the client ip address
     * 
     * @author Daniele Pantaleone
     * @param  ip The client ip address
     */
    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    
    /**
     * Set the client GUID <tt>String</tt>
     * 
     * @author Daniele Pantaleone
     * @param  guid The client GUID <tt>String</tt>
     **/
    public void setGuid(String guid) {
        this.guid = guid;
    }

    
    /**
     * Set the client auth login
     * 
     * @author Daniele Pantaleone
     * @param  auth The client auth login
     **/
    public void setAuth(String auth) {
        this.auth = auth;
    }

    
    /**
     * Set the client time add
     * 
     * @author Daniele Pantaleone
     * @param  time_add A <tt>DateTime</tt> object representing the time when the client has been first seen
     **/
    public void setTimeAdd(DateTime time_add) {
        this.time_add = time_add;
    }

    
    /**
     * Set the client last time edit
     * 
     * @author Daniele Pantaleone
     * @param  time_edit A <tt>DateTime</tt> object representing the client last time edit
     **/
    public void setTimeEdit(DateTime time_edit) {
        this.time_edit = time_edit;
    }

    
    /**
     * Set the client slot number
     * 
     * @author Daniele Pantaleone
     * @param  slot The client slot number
     **/
    public void setSlot(Integer slot) {
        this.slot = slot;
    }

    
    /**
     * Set the client gear <tt>String</tt>
     * 
     * @author Daniele Pantaleone
     * @param  gear The client gear <tt>String</tt>
     **/
    public void setGear(String gear) {
        this.gear = gear;
    }

    
    /**
     * Set the client <tt>Team</tt>
     * 
     * @author Daniele Pantaleone
     * @param  team The client <tt>Team</tt>
     **/
    public void setTeam(Team team) {
        this.team = team;
    }

    
    /**
     * Flag or unflag this client as a bot
     * 
     * @author Daniele Pantaleone
     * @param  bot <tt>true</tt> if this client is a bot, <tt>false</tt> otherwise
     **/
    public void setBot(boolean bot) {
        this.bot = bot;
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
     * Tells whether the given key has a dynamic created variable associated
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
     * String object representation
     * 
     * @author Daniele Pantaleone
     * @return A <tt>String</tt> representing the content of this object
     **/
    public String toString() {    
        return "[ id : " + this.id + " | name : " + this.name + " | level : " + this.group.level + " | ip : " + this.ip.getHostAddress() + " | guid : " + this.guid + " | auth : " + this.auth + " | connections: " + this.connections + " | time_add: " + this.time_add.toString() + " | time_edit: " + this.time_edit.toString() + " ]";
    }
    
}