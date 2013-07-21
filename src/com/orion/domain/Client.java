/**
 * Copyright (c) 2012 Daniele Pantaleone, Mathias Van Malderen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 * @author      Daniele Pantaleone
 * @version     1.1.2
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
    private Client(Builder builder) {
        
        this.setId(builder.id);
        this.setGroup(builder.group);
        this.setName(builder.name);
        this.setConnections(builder.connections);
        this.setIp(builder.ip);
        this.setGuid(builder.guid);
        this.setAuth(builder.auth);
        this.setTimeAdd(builder.time_add);
        this.setTimeEdit(builder.time_edit);
        this.setSlot(builder.slot);
        this.setGear(builder.gear);
        this.setTeam(builder.team);
        this.setBot(builder.bot);
        
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
        
        if (group == null) {
            throw new NullPointerException("client group can't be NULL");
        }
        
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
        
        if (this.connections < 1) {
            throw new IllegalArgumentException("client number of connections can't be lower than 1");
        }
        
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
        this.guid = guid.toUpperCase();
    }

    
    /**
     * Set the client auth login
     * 
     * @author Daniele Pantaleone
     * @param  auth The client auth login
     **/
    public void setAuth(String auth) {
        this.auth = auth.toLowerCase();
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
        
        if (slot < 0) {
            throw new IllegalArgumentException("the slot number must be positive");
        }
        
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
     * Return the <tt>Client</tt> variable associated to the given key
     * 
     * @author Daniele Pantaleone
     * @param  key The key from which to fetch the variable in the <tt>HashMap</tt>
     * @param  c The <tt>Class</tt> onto which to perform the variable conversion
     * @return The <tt>Client</tt> variable associated to the specified key or <tt>null</tt> if there is no match
     **/
    @SuppressWarnings("unchecked")
    public <V> V getVar(String key, Class<V> c) {
        return this.vars.containsKey(key) ? (V) this.vars.get(key) : null;
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
        return this.getGroup().getLevel() == group.getLevel() ? true : false;
    }
    
    
    /**
     * String object representation
     * 
     * @author Daniele Pantaleone
     * @return A <tt>String</tt> representing this object
     **/
    public String toString() {   
        
        return "[ id : " + this.getId() + " |" +
                " name : " + this.getName() + " |" +
                " level : " + this.getGroup().getLevel() + " |" +
                " ip : " + this.getIp().getHostAddress() + " |" +
                " guid : " + this.getGuid() + " |" +
                " auth : " + this.getAuth() + " |" +
                " connections: " + this.getConnections() + " |" +
                " time_add: " + this.getTimeAdd().toString() + " |" +
                " time_edit: " + this.getTimeEdit().toString() + " ]";
        
    }
    
    
    public static class Builder {
        
        private final InetAddress ip;
        private final String guid;
        
        private int id;
        private Group group;
        private String name;
        private int connections;
        private String auth;
        
        private DateTime time_add;
        private DateTime time_edit;
        
        private Integer slot;
        private String gear;
        private Team team;
        
        private boolean bot = false;
      
        
        public Builder(InetAddress ip, String guid) {
            this.ip = ip;
            this.guid = guid;
        }
        
        
        public Builder id(int value) {
            this.id = value;
            return this;
        }
        
        
        public Builder group(Group value) {
            this.group = value;
            return this;
        }
        
        
        public Builder name(String value) {
            this.name = value;
            return this;
        }
        
        
        public Builder connections(int value) {
            this.connections = value;
            return this;
        }
        
        
        public Builder auth(String value) {
            this.auth = value;
            return this;
        }
        
        
        public Builder timeAdd(DateTime value) {
            this.time_add = value;
            return this;
        }
        
        
        public Builder timeEdit(DateTime value) {
            this.time_edit = value;
            return this;
        }
        
        
        public Builder slot(Integer value) {
            this.slot = value;
            return this;
        }
        
        
        public Builder gear(String value) {
            this.gear = value;
            return this;
        }
        
        
        public Builder team(Team value) {
            this.team = value;
            return this;
        }
        
        
        public Builder bot(boolean value) {
            this.bot = value;
            return this;
        }
        
        
        public Client build() {
            return new Client(this);
        }
        
    }
    
}