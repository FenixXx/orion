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
     * Set the <tt>Penalty</tt> id
     * 
     * @author Mathias Van Malderen
     * @param id The <tt>Penalty</tt> id
     **/
    public void setId(int id) {
        this.id = id;
    }
    
    
    /**
     * Return the <tt>Penalty</tt> id
     * 
     * @author Mathias Van Malderen
     * @return The <tt>Penalty</tt> id
     **/
    public int getId() {
        return id;
    }


    /**
     * Set the <tt>Client</tt> target of this <tt>Penalty</tt>
     * 
     * @author Mathias Van Malderen
     * @param  client The <tt>Client</tt> who is subject to this <tt>Penalty</tt>
     */
    public void setClient(Client client) {
        this.client = client;
    }
    
    
    /**
     * Get the <tt>Client</tt> target of this <tt>Penalty</tt>
     * 
     * @author Mathias Van Malderen
     * @return The <tt>Client</tt> who is subject to this <tt>Penalty</tt>
     */
    public Client getClient() {
        return client;
    }


    /**
     * Set the admin <tt>Client</tt> who issued this <tt>Penalty</tt>
     * 
     * @author Mathias Van Malderen
     * @param  admin The admin <tt>Client</tt> who issued this <tt>Penalty</tt>
     */
    public void setAdmin(Client admin) {
        this.admin = admin;
    }
    
    
    /**
     * Get the admin <tt>Client</tt> who issued this <tt>Penalty</tt>
     * 
     * @author Mathias Van Malderen
     * @return The admin <tt>Client</tt> by whom this <tt>Penalty</tt> was issued
     */
    public Client getAdmin() {
        return admin;
    }


    /**
     * Set the <tt>PenaltyType</tt> type of this <tt>Penalty</tt>
     * 
     * @author Mathias Van Malderen
     * @param type The <tt>PenaltyType</tt> type of this <tt>Penalty</tt>
     */
    public void setType(PenaltyType type) {
        this.type = type;
    }
    
    
    /**
     * Get the <tt>PenaltyType</tt> type of this <tt>Penalty</tt>
     * 
     * @author Mathias Van Malderen
     * @return The <tt>PenaltyType</tt> type of this <tt>Penalty</tt>
     */
    public PenaltyType getType() {
        return type;
    }


    /**
     * Set the active state of this <tt>Penalty</tt>
     * Penalties can be deactivated by calling <tt>setActive(false)</tt>
     * 
     * @author Mathias Van Malderen
     * @param active true to activate this <tt>Penalty</tt>, false to deactivate
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    
    
    /**
     * Get the whether this <tt>Penalty</tt> is active or not
     * 
     * @author Mathias Van Malderen
     * @return true if this <tt>Penalty</tt> is active, false if this <tt>Penalty</tt> is inactive
     */
    public boolean isActive() {
        return this.active;
    }
    
    
    /**
     * Set the reason for why this <tt>Penalty</tt> was issued
     * 
     * @author Mathias Van Malderen
     * @param reason The the reason for why this <tt>Penalty</tt> was issued
     */
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    
    /**
     * Get the reason for why this <tt>Penalty</tt> was issued
     * 
     * @author Mathias Van Malderen
     * @return The the reason for why this <tt>Penalty</tt> was issued
     */
    public String getReason() {
        return this.reason;
    }
    
    
    /**
     * Return a <tt>DateTime</tt> object representing the time when the <tt>Penalty</tt> has been created
     * 
     * @author Mathias Van Malderen
     * @return A <tt>DateTime</tt> object representing the time when the <tt>Penalty</tt> has been created
     **/
    public void setTimeAdd(DateTime time_add) {
        this.time_add = time_add;
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
     * Set the penalty's last time edit.
     * 
     * @author Mathias Van Malderen
     * @param  time_edit A <tt>DateTime</tt> object representing the <tt>Penalty</tt> last time edit
     **/
    public void setTimeEdit(DateTime time_edit) {
        this.time_edit = time_edit;
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
     * Set the <tt>Penalty</tt> expiry time.
     * 
     * @author Mathias Van Malderen
     * @param  time_expire A <tt>DateTime</tt> object representing the <tt>Penalty</tt> expiry time
     **/
    public void setTimeExpire(DateTime time_expire) {
        this.time_expire = time_expire;
    }

    
    /**
     * Set the <tt>Penalty</tt> expiry time.
     * 
     * @author Mathias Van Malderen
     * @return The <tt>Penalty</tt> expiry time.
     **/
    public DateTime getTimeExpire() {
        return time_expire;
    }


    /**
     * <tt>String</tt> object representation
     * 
     * @author Daniele Pantaleone
     * @return A <tt>String</tt> representing the content of this object
     **/
    public String toString() {
        return "[ id : " + this.id + " | client : " + this.client.getId() + " | admin : " + this.admin.getId() + " | type : " + this.type.name() + " | active : " + this.active + " | reason: " + this.reason + " | time_add : " + this.time_add.toString() + " | time_edit : " + this.time_edit.toString() + " | time_expire : " + ((this.time_expire != null) ? this.time_expire.toString() : "PERMANENT") + " ]";
    }
    
    
    public static class Builder {
        
        private final PenaltyType type;
        private final Client client;
        private final Client admin;
        
        private boolean active = true;
        private String reason = "";
        
        private int id;
        private DateTime time_add;
        private DateTime time_edit;
        private DateTime time_expire;
        
        
        public Builder(Client client, Client admin, PenaltyType type) {
            
            if (client == null) throw new NullPointerException("client cannot be null");
            if (admin == null) throw new NullPointerException("admin cannot be null");
            if (type == null) throw new NullPointerException("type cannot be null");
            
            this.client = client;
            this.admin = admin;
            this.type = type;
            
        }
        
        
        public Builder active(boolean value) {
            this.active = value;
            return this;
        }
        
        
        public Builder reason(String value) {
            this.reason = value;
            return this;
        }
        
        
        public Builder id(int value) {
            this.id = value;
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