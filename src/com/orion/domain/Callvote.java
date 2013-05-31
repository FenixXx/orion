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
 * @copyright   Daniele Pantaleone, 08 October, 2012
 * @package     co.orion.domain
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
     * Set the callvote time add.
     * 
     * @author Mathias Van Malderen
     * @param  time_add A <tt>DateTime</tt> object representing the time when the callvote has been raised
     **/
    public void setTimeAdd(DateTime time_add) {
        this.time_add = time_add;
    }

    
    /**
     * Set the callvote last time edit.
     * 
     * @author Mathias Van Malderen
     * @param  time_edit A <tt>DateTime</tt> object representing the callvote last time edit
     **/
    public void setTimeEdit(DateTime time_edit) {
        this.time_edit = time_edit;
    }
    
    
    /**
     * Return the callvote id.
     * 
     * @author Mathias Van Malderen
     * @return The callvote id
     **/
    public int getId() {
		return id;
	}


    /**
     * Set the callvote id.
     * 
     * @author Mathias Van Malderen
     * @return The callvote id
     **/
	public void setId(int id) {
		this.id = id;
	}

	
	/**
	 * Get client who raised the callvote.
	 * 
	 * @author Mathias Van Malderen
	 * @return The client who raised this callvote.
	 */
	public Client getClient() {
		return client;
	}

	
	/**
	 * Set client who raised the callvote.
	 * 
	 * @author Mathias Van Malderen
	 * @return The client who raised this callvote.
	 */
	public void setClient(Client client) {
		
		if (client == null)
			throw new NullPointerException("client");
		
		this.client = client;
	}
	

	/**
	 * Get the type of this <tt>Callvote</tt>
	 * 
	 * @author Mathias Van Malderen
	 * @return The type of this <tt>Callvote</tt>
	 */
	public String getType() {
		return this.type;
	}

	
	/**
	 * Set client who raised the callvote.
	 * 
	 * @author Mathias Van Malderen
	 * @return The client who raised this callvote.
	 */
	public void setType(String type) {
		
		if (type == null)
			throw new NullPointerException("type");
		
		this.type = type;
		
	}

	
	/**
	 * Get optional data associated with the callvote.
	 * 
	 * @author Mathias Van Malderen
	 * @return parameter value of the callvote if one was specified, null otherwise.
	 */
	public String getData() {
		return this.data;
	}


	/**
	 * Set optional data associated with the callvote.
	 * 
	 * @author Mathias Van Malderen
	 * @param data parameter value of the callvote
	 */
	public void setData(String data) {
		this.data = data;
	}

	
	/**
	 * Get the amount of yes votes for this <tt>Callvote</tt>
	 * 
	 * @author Mathias Van Malderen
	 * @param yes number of 'yes' votes
	 */
	public int getYes() {
		return this.yes;
	}


	/**
	 * Set the amount of yes answers for this <tt>Callvote</tt>
	 * 
	 * @author Mathias Van Malderen
	 * @param yes number of 'yes' votes
	 */
	public void setYes(int yes) {
		
		if (yes < 1)
			throw new IllegalArgumentException("number of 'yes' answers cannot be lower than 1");
		
		this.yes = yes;
		
	}


	/**
	 * Get the amount of no votes for this <tt>Callvote</tt>
	 * 
	 * @author Mathias Van Malderen
	 * @param no number of 'no' votes
	 */
	public int getNo() {
		return this.no;
	}
	
	
	/**
	 * Set the amount of no answers for this <tt>Callvote</tt>
	 * 
	 * @author Mathias Van Malderen
	 * @param no number of 'no' votes
	 */
	public void setNo(int no) {
		
		if (no < 0)
			throw new IllegalArgumentException("number of 'no' answers cannot be negative");
		
		this.no = no;
		
	}
	
	
    /**
     * Return a <tt>DateTime</tt> object representing the time when the callvote has been raised
     * 
     * @author Mathias Van Malderen
     * @return A <tt>DateTime</tt> object representing the time when the client has been raised
     **/
    public DateTime getTimeAdd() {
        return this.time_add;
    }
    
    
    /**
     * Return a <tt>DateTime</tt> object representing the callvote last time edit
     * 
     * @author Mathias Van Malderen
     * @return A <tt>DateTime</tt> object representing the callvote last time edit
     **/
    public DateTime getTimeEdit() {
        return this.time_edit;
    }


	/**
     * String object representation
     * 
     * @author Daniele Pantaleone
     * @return A <tt>String</tt> representing the content of this object
     **/
    public String toString() {
        return "[ id : " + this.id + " | client : " + this.client.getId() + " | type : " + this.type + " | data : " + this.data + " | yes : " + this.yes + " | no : " + this.no + " | time_add : " + this.time_add.toString() + " | time_add : " + this.time_add.toString() + " ]";
    }
    
    
    public static class Builder {
    	
    	private final Client client;
    	private final String type;
    	
        private int id;
        private String data;
        private int yes;
        private int no;
        private DateTime time_add;
        private DateTime time_edit;
    	
        
    	public Builder(Client client, String type) {
    		this.client = client;
    		this.type = type;
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
    	
    	
    	public Builder id(int value) {
    		this.id = value;
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