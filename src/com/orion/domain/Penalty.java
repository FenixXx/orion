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
 * @author      Daniele Pantaleone, Mathias Van Malderen
 * @version     1.1.1
 * @copyright   Daniele Pantaleone, 05 October, 2012
 * @package     com.orion.domain
 **/

package com.orion.domain;

import org.joda.time.DateTime;

import com.orion.bot.PenaltyType;

public class Penalty {
    
    private int id;
    private Client client;
    private Client admin;
    private PenaltyType type;
    private boolean active;
    private String reason;
    private DateTime time_add;
    private DateTime time_edit;
    private DateTime time_expire;
  
    
    /**
     * Object constructor
     * 
     * @author Mathias Van Malderen
     * @param  builder The <tt>Penalty</tt> object <tt>Builder</tt>
     **/
    private Penalty(Builder builder) {
        this.setId(builder.id);
        this.setClient(builder.client);
        this.setAdmin(builder.admin);
        this.setType(builder.type);
        this.setActive(builder.active);
        this.setReason(builder.reason);
        this.setTimeAdd(builder.time_add);
        this.setTimeEdit(builder.time_edit);
        this.setTimeExpire(builder.time_expire);
    }
    
    
    /**
     * Return the <tt>Penalty</tt> id
     * 
     * @author Mathias Van Malderen
     * @return The <tt>Penalty</tt> id
     **/
    public int getId() {
        return this.id;
    }
    
    
    /**
     * Return the <tt>Client</tt> target of this <tt>Penalty</tt>
     * 
     * @author Mathias Van Malderen
     * @return The <tt>Client</tt> who is subject to this <tt>Penalty</tt>
     **/
    public Client getClient() {
        return this.client;
    }
    
    
    /**
     * Return the admin <tt>Client</tt> who issued this <tt>Penalty</tt>
     * 
     * @author Mathias Van Malderen
     * @return The admin <tt>Client</tt> by whom this <tt>Penalty</tt> was issued
     **/
    public Client getAdmin() {
        return this.admin;
    }
    
    
    /**
     * Return the <tt>PenaltyType</tt> type of this <tt>Penalty</tt>
     * 
     * @author Mathias Van Malderen
     * @return The <tt>PenaltyType</tt> type of this <tt>Penalty</tt>
     **/
    public PenaltyType getType() {
        return this.type;
    }
    
    
    /**
     * Tells the whether this <tt>Penalty</tt> is active or not
     * 
     * @author Mathias Van Malderen
     * @return <tt>true</tt> if this <tt>Penalty</tt> is active, <tt>false</tt> otherwise
     **/
    public boolean isActive() {
        return this.active;
    }
    
    
    /**
     * Return the reason for why this <tt>Penalty</tt> was issued
     * 
     * @author Mathias Van Malderen
     * @return The reason why this <tt>Penalty</tt> was issued
     **/
    public String getReason() {
        return this.reason;
    }
    
    
    /**
     * Return a <tt>DateTime</tt> object representing the time when the <tt>Penalty</tt> has been created
     * 
     * @author Mathias Van Malderen
     * @return A <tt>DateTime</tt> object representing the time when the <tt>Penalty</tt> has been created
     **/
    public DateTime getTimeAdd() {
        return this.time_add;
    }
    
    
    /**
     * Return a <tt>DateTime</tt> object representing the <tt>Penalty</tt> last time edit
     * 
     * @author Mathias Van Malderen
     * @return A <tt>DateTime</tt> object representing the <tt>Penalty</tt> last time edit
     **/
    public DateTime getTimeEdit() {
        return this.time_edit;
    }
    
    
    /**
     * Set the <tt>Penalty</tt> expire time
     * 
     * @author Mathias Van Malderen
     * @return The <tt>Penalty</tt> expire time
     **/
    public DateTime getTimeExpire() {
        return time_expire;
    }
    
    
    /**
     * Set the <tt>Penalty</tt> id
     * 
     * @author Mathias Van Malderen
     * @param id The <tt>Penalty</tt> id
     **/
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Set the <tt>Client</tt> target of this <tt>Penalty</tt>
     * 
     * @author Mathias Van Malderen
     * @param  client The <tt>Client</tt> who is subject to this <tt>Penalty</tt>
     **/
    public void setClient(Client client) {
        if (client == null) throw new NullPointerException("Client cannot be null");
        this.client = client;
    }
    

    /**
     * Set the admin <tt>Client</tt> who issued this <tt>Penalty</tt>
     * 
     * @author Mathias Van Malderen
     * @param  admin The admin <tt>Client</tt> who issued this <tt>Penalty</tt>
     **/
    public void setAdmin(Client admin) {
        this.admin = admin;
    }
    

    /**
     * Set the <tt>PenaltyType</tt> type of this <tt>Penalty</tt>
     * 
     * @author Mathias Van Malderen
     * @param type The <tt>PenaltyType</tt> type of this <tt>Penalty</tt>
     **/
    public void setType(PenaltyType type) {
        if (type == null) throw new NullPointerException("The penalty type cannot be null");
        this.type = type;
    }
    

    /**
     * Set the active state of this <tt>Penalty</tt>
     * Penalties can be deactivated by calling <tt>setActive(false)</tt>
     * 
     * @author Mathias Van Malderen
     * @param active <tt>true</tt> to activate this <tt>Penalty</tt>, <tt>false</tt> to deactivate
     **/
    public void setActive(boolean active) {
        this.active = active;
    }
    
    
    /**
     * Set the reason for why this <tt>Penalty</tt> was issued
     * 
     * @author Mathias Van Malderen
     * @param reason The the reason for why this <tt>Penalty</tt> was issued
     **/
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    
    /**
     * Set the <tt>Penalty</tt> time add
     * 
     * @author Mathias Van Malderen
     * @param  time_add A <tt>DateTime</tt> object representing the time when the <tt>Penalty</tt> has been issued
     **/
    public void setTimeAdd(DateTime time_add) {
        this.time_add = time_add;
    }
    
    
    /**
     * Set the <tt>Penalty</tt> last time edit
     * 
     * @author Mathias Van Malderen
     * @param  time_edit A <tt>DateTime</tt> object representing the <tt>Penalty</tt> last time edit
     **/
    public void setTimeEdit(DateTime time_edit) {
        this.time_edit = time_edit;
    }
   
    
    /**
     * Set the <tt>Penalty</tt> expire time
     * 
     * @author Mathias Van Malderen
     * @param  time_expire A <tt>DateTime</tt> object representing the <tt>Penalty</tt> expire time
     **/
    public void setTimeExpire(DateTime time_expire) {
        this.time_expire = time_expire;
    }


    /**
     * <tt>String</tt> object representation
     * 
     * @author Daniele Pantaleone
     * @return A <tt>String</tt> representing the content of this object
     **/
    public String toString() {
        return "[ id : " + this.getId() + " | client : " + this.getClient().getId() + " | admin : " + ((this.getAdmin() != null) ? this.getAdmin().getId() : null) + " | type : " + this.getType().name() + " | active : " + this.isActive() + " | reason: " + this.getReason() + " | time_add : " + this.getTimeAdd().toString() + " | time_edit : " + this.getTimeEdit().toString() + " | time_expire : " + ((this.getTimeExpire() != null) ? this.getTimeExpire().toString() : "PERMANENT") + " ]";
    }
    
    
    public static class Builder {
        
        private final Client client;
        private final PenaltyType type;
        
        private int id;
        private Client admin;
        private boolean active = true;
        private String reason;
        private DateTime time_add;
        private DateTime time_edit;
        private DateTime time_expire;
        
        
        public Builder(Client client, PenaltyType type) {
            this.client = client;
            this.type = type;   
        }
        
        
        public Builder id(int value) {
            this.id = value;
            return this;
        }
        
        
        public Builder admin(Client value) {
            this.admin = value;
            return this;
        }
        
        
        public Builder active(boolean value) {
            this.active = value;
            return this;
        }
        
        
        public Builder reason(String value) {
            this.reason = value;
            return this;
        }
        
        
        public Builder timeAdd(DateTime value) {
            time_add = value;
            return this;
        }
        
        
        public Builder timeEdit(DateTime value) {
            time_edit = value;
            return this;
        }
        
        
        public Builder timeExpire(DateTime value) {
            time_expire = value;
            return this;
        }
        
        
        public Penalty build() {
            return new Penalty(this);
        }
        
    }
    
}