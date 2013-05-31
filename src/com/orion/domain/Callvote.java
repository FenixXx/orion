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
 * @copyright   Daniele Pantaleone, 08 October, 2012
 * @package     com.orion.domain
 **/

package com.orion.domain;

import org.joda.time.DateTime;

public class Callvote {
    
    private int id;
    private Client client;
    private String type;
    private String data;
    private int yes;
    private int no;
    private DateTime time_add;
    private DateTime time_edit;
    
    
    /**
     * Object constructor
     * 
     * @author Mathias Van Malderen
     * @param  builder The <tt>Callvote</tt> object <tt>Builder</tt>
     **/
    private Callvote(Builder builder) {
        this.setId(builder.id);
        this.setClient(builder.client);
        this.setType(builder.type);
        this.setData(builder.data);
        this.setYes(builder.yes);
        this.setNo(builder.no);
        this.setTimeAdd(builder.time_add);
        this.setTimeEdit(builder.time_edit);
    }
    
    
    /**
     * Return the <tt>Callvote</tt> id
     * 
     * @author Mathias Van Malderen
     * @return The <tt>Callvote</tt> id
     **/
    public int getId() {
        return this.id;
    }
    
    
    /**
     * Return the <tt>Client</tt> who raised the <tt>Callvote</tt>
     * 
     * @author Mathias Van Malderen
     * @return The the <tt>Client</tt> who raised this <tt>Callvote</tt>
     **/
    public Client getClient() {
        return this.client;
    }
    
    
    /**
     * Return the type of this <tt>Callvote</tt>
     * 
     * @author Mathias Van Malderen
     * @return The type of this <tt>Callvote</tt>
     */
    public String getType() {
        return this.type;
    }
    
    
    /**
     * Return optional parameters associated with the <tt>Callvote</tt>
     * 
     * @author Mathias Van Malderen
     * @return The <tt>Callvote</tt> parameters if specified, <tt>null</tt> otherwise
     */
    public String getData() {
        return this.data;
    }
    
    
    /**
     * Return the amount of YES votes for this <tt>Callvote</tt>
     * 
     * @author Mathias Van Malderen
     * @return The amount of YES votes for this <tt>Callvote</tt>
     **/
    public int getYes() {
        return this.yes;
    }
    
    
    /**
     * Return the amount of NO votes for this <tt>Callvote</tt>
     * 
     * @author Mathias Van Malderen
     * @return The amount of NO votes for this <tt>Callvote</tt>
     **/
    public int getNo() {
        return this.no;
    }
    
    
    /**
     * Return a <tt>DateTime</tt> object representing the time when the <tt>Callvote</tt> has been raised
     * 
     * @author Mathias Van Malderen
     * @return A <tt>DateTime</tt> object representing the time when the <tt>Callvote</tt> has been raised
     **/
    public DateTime getTimeAdd() {
        return this.time_add;
    }
    
    
    /**
     * Return a <tt>DateTime</tt> object representing the <tt>Callvote</tt> last time edit
     * 
     * @author Mathias Van Malderen
     * @return A <tt>DateTime</tt> object representing the <tt>Callvote</tt> last time edit
     **/
    public DateTime getTimeEdit() {
        return this.time_edit;
    }
    
    
    /**
     * Set the <tt>Callvote</tt> id
     * 
     * @author Mathias Van Malderen
     * @param  id The <tt>Callvote</tt> id
     **/
    public void setId(int id) {
        this.id = id;
    }

    
    /**
     * Set <tt>Client</tt> who raised the <tt>Callvote</tt>
     * 
     * @author Mathias Van Malderen
     * @param  client The <tt>Client</tt> who raised this <tt>Callvote</tt>
     **/
    public void setClient(Client client) {
        if (client == null) throw new NullPointerException("Client detected as NULL object");
        this.client = client;
    }
    
    
    /**
     * Set the type of the <tt>Callvote</tt>
     * 
     * @author Mathias Van Malderen
     * @param  type The type of the <tt>Callvote</tt>
     **/
    public void setType(String type) {
        if (type == null) throw new NullPointerException("Type detected as NULL object");
        this.type = type;
    }

   
    /**
     * Set optional data associated with the <tt>Callvote</tt>
     * 
     * @author Mathias Van Malderen
     * @param  data Parameter value of the <tt>Callvote</tt>
     **/
    public void setData(String data) {
        this.data = data;
    }

    
    /**
     * Set the amount of YES answers for this <tt>Callvote</tt>
     * 
     * @author Mathias Van Malderen
     * @param  yes The number of YES votes
     */
    public void setYes(int yes) {
        if (yes < 1) throw new IllegalArgumentException("The number of YES answers cannot be lower than 1");
        this.yes = yes;
    }
    
    
    /**
     * Set the amount of NO answers for this <tt>Callvote</tt>
     * 
     * @author Mathias Van Malderen
     * @param  no The number of NO votes
     */
    public void setNo(int no) {
        if (no < 0) throw new IllegalArgumentException("number of 'no' answers cannot be negative");
        this.no = no;
    }
    
    
    /**
     * Set the <tt>Callvote</tt> time add
     * 
     * @author Mathias Van Malderen
     * @param  time_add A <tt>DateTime</tt> object representing the time when the <tt>Callvote</tt> has been raised
     **/
    public void setTimeAdd(DateTime time_add) {
        this.time_add = time_add;
    }

    
    /**
     * Set the <tt>Callvote</tt> last time edit
     * 
     * @author Mathias Van Malderen
     * @param  time_edit A <tt>DateTime</tt> object representing the <tt>Callvote</tt> last time edit
     **/
    public void setTimeEdit(DateTime time_edit) {
        this.time_edit = time_edit;
    }


    /**
     * String object representation
     * 
     * @author Daniele Pantaleone
     * @return A <tt>String</tt> representing the content of this object
     **/
    public String toString() {
        return "[ id : " + this.getId() + " | client : " + this.getClient().getId() + " | type : " + this.getType() + " | data : " + this.getData() + " | yes : " + this.getYes() + " | no : " + this.getNo() + " | time_add : " + this.getTimeAdd().toString() + " | time_add : " + this.getTimeEdit().toString() + " ]";
    }
    
    
    public static class Builder {
        
        private final Client client;
        private final String type;
        
        private int id;
        private String data;
        private int yes = 1;
        private int no;
        private DateTime time_add;
        private DateTime time_edit;
        
        
        public Builder(Client client, String type) {
            
            if (client == null) throw new NullPointerException("client cannot be null");
            if (type == null) throw new NullPointerException("type cannot be null");
            
            this.client = client;
            this.type = type;
            
        }
        
        
        public Builder id(int value) {
            this.id = value;
            return this;
        }
        
        
        public Builder data(String value) {
            this.data = value;
            return this;
        }
        
        
        public Builder yes(int value) {
            this.yes = value;
            return this;
        }
        
        
        public Builder no(int value) {
            this.yes = value;
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
        
        
        public Callvote build() {
            return new Callvote(this);
        }
        
    }
    
}