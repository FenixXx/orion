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
 * @author      Mathias Van Malderen, Daniele Pantaleone
 * @version     1.1
 * @copyright   Mathias Van Malderen, 05 October, 2012
 * @package     com.orion.domain
 **/

package com.orion.domain;

import org.joda.time.DateTime;

public class Alias {
    
    private int id;
    private Client client;
    private String name;
    private int num_used;
    private DateTime time_add;
    private DateTime time_edit;
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> this <tt>Alias</tt> belongs to
     * @param  name The <tt>Alias</tt> name
     **/
    public Alias(Client client, String name) {
        this.setClient(client);
        this.setName(name);
        this.setNumUsed(this.getNumUsed() + 1);
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
        this.setId(id);
        this.setClient(client);
        this.setName(name);
        this.setNumUsed(num_used);
        this.setTimeAdd(time_add);
        this.setTimeEdit(time_edit);
    }

    
    /**
     * Return the alias id
     * 
     * @author Daniele Pantaleone
     * @return The alias id
     **/
    public int getId() {
        return this.id;
    }
    
    
    /**
     * Return the <tt>Client</tt> who this alias belongs to
     * 
     * @author Daniele Pantaleone
     * @return The <tt>Client</tt> who this alias belongs to
     **/
    public Client getClient() {
        return this.client;
    }
    
    
    /**
     * Return the alias name
     * 
     * @author Daniele Pantaleone
     * @return The alias name
     **/
    public String getName() {
        return this.name;
    }
    
    
    /**
     * Tell the amount of time this alias has been used by the associated <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @return The amount of time this alias has been used
     **/
    public int getNumUsed() {
        return this.num_used;
    }
    
    
    /**
     * Return a <tt>DateTime</tt> object representing the time when the alias has been first created
     * 
     * @author Daniele Pantaleone
     * @return A <tt>DateTime</tt> object representing the time when the alias has been first created
     **/
    public DateTime getTimeAdd() {
        return this.time_add;
    }
    
    
    /**
     * Return a <tt>DateTime</tt> object representing the alias last time edit
     * 
     * @author Daniele Pantaleone
     * @return A <tt>DateTime</tt> object representing the alias last time edit
     **/
    public DateTime getTimeEdit() {
        return this.time_edit;
    }

    
    /**
     * Set the alias id
     * 
     * @author Daniele Pantaleone
     * @param  id The alias id
     **/
    public void setId(int id) {
        this.id = id;
    }

   
    /**
     * Set the <tt>Client</tt> who this alias belongs to
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who this alias belongs to
     **/
    public void setClient(Client client) {
        this.client = client;
    }

    
    /**
     * Set the alias name
     * 
     * @author Daniele Pantaleone
     * @param  name The alias name
     **/
    public void setName(String name) {
        // Remove color codes from the client name
        this.name = name.replaceAll("\\^[0-9]{1}", "");
    }
    

    /**
     * Set the amount of time this alias has been used
     * 
     * @author Daniele Pantaleone
     * @param  num_used The amount of time this alias has been used by the associated <tt>Client</tt>
     **/
    public void setNumUsed(int num_used) {
        this.num_used = num_used;
    }

    
    /**
     * Set the alias time add
     * 
     * @author Daniele Pantaleone
     * @param  time_add A <tt>DateTime</tt> object representing the time when the alias has been first created
     **/
    public void setTimeAdd(DateTime time_add) {
        this.time_add = time_add;
    }

    
    /**
     * Set the alias last time edit
     * 
     * @author Daniele Pantaleone
     * @param  time_edit A <tt>DateTime</tt> object representing the alias last time edit
     **/
    public void setTimeEdit(DateTime time_edit) {
        this.time_edit = time_edit;
    }
    
    
    /**
     * <tt>String</tt> object representation
     * 
     * @author Daniele Pantaleone
     * @return A <tt>String</tt> representing the content of this object
     **/
    public String toString() {
        return "[ id : " + this.getId() + " | client : " + this.getClient().getId() + " | name : " + this.getName() + " | num_used : " + this.getNumUsed() + " | time_add : " + this.getTimeAdd().toString() + " | time_edit : " + this.getTimeEdit().toString() + " ]";   
    }
    
}