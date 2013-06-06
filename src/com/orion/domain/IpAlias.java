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
 * @version     1.1
 * @copyright   Daniele Pantaleone, 20 January, 2013
 * @package     com.orion.domain
 **/

package com.orion.domain;

import java.net.InetAddress;

import org.joda.time.DateTime;

public class IpAlias {
    
    public int id;
    public Client client;
    public InetAddress ip ;
    public int num_used;
    public DateTime time_add;
    public DateTime time_edit;
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> this <tt>Alias</tt> belongs to
     * @param  ip The <tt>InetAddress</tt> object associated to the <tt>Client</tt> ip address
     **/
    public IpAlias(Client client, InetAddress ip) {
        this.setClient(client);
        this.setIp(ip);
        this.setNumUsed(this.getNumUsed() + 1);
    }
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  id The <tt>Alias</tt> id
     * @param  client The <tt>Client</tt> this <tt>Alias</tt> belongs to
     * @param  ip The <tt>InetAddress</tt> object associated to the <tt>Client</tt> ip address
     * @param  num_used A counter which holds the number of time this <tt>Alias</tt> has been used
     * @param  time_add The <tt>DateTime</tt> when this <tt>Alias</tt> has been created
     * @param  time_edit The <tt>DateTime</tt> when this <tt>Alias</tt> has been last edited
     **/
    public IpAlias(int id, Client client, InetAddress ip, int num_used, DateTime time_add, DateTime time_edit) {
        this.setId(id);
        this.setClient(client);
        this.setIp(ip);
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
     * Return the <tt>Client</tt> who this ip alias belongs to
     * 
     * @author Daniele Pantaleone
     * @return The <tt>Client</tt> who this ip alias belongs to
     **/
    public Client getClient() {
        return this.client;
    }
    
    
    /**
     * Return the <tt>InetAddress</tt> associated to this ip alias
     * 
     * @author Daniele Pantaleone
     * @return The ip address
     **/
    public InetAddress getIp() {
        return this.ip;
    }
    
    
    /**
     * Tell the amount of this ip alias has been used by the associated <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @return The amount of time this ip alias has been used
     **/
    public int getNumUsed() {
        return this.num_used;
    }
    
    
    /**
     * Return a <tt>DateTime</tt> object representing the time when the ip alias has been first created
     * 
     * @author Daniele Pantaleone
     * @return A <tt>DateTime</tt> object representing the time when the ip alias has been first created
     **/
    public DateTime getTimeAdd() {
        return this.time_add;
    }
    
    
    /**
     * Return a <tt>DateTime</tt> object representing the ip alias last time edit
     * 
     * @author Daniele Pantaleone
     * @return A <tt>DateTime</tt> object representing the ip alias last time edit
     **/
    public DateTime getTimeEdit() {
        return this.time_edit;
    }

    
    /**
     * Set the ip alias id
     * 
     * @author Daniele Pantaleone
     * @param  id The alias id
     **/
    public void setId(int id) {
        this.id = id;
    }

   
    /**
     * Set the <tt>Client</tt> who this ip alias belongs to
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who this alias belongs to
     **/
    public void setClient(Client client) {
        this.client = client;
    }

    
    /**
     * Set the ip alias ip address
     * 
     * @author Daniele Pantaleone
     * @param  name The ip address of the <tt>Client</tt> this ip alias belongs to
     **/
    public void setIp(InetAddress ip) {
        this.ip = ip;
    }
    

    /**
     * Set the amount of time this ip alias has been used
     * 
     * @author Daniele Pantaleone
     * @param  num_used The amount of time this ip alias has been used by the associated <tt>Client</tt>
     **/
    public void setNumUsed(int num_used) {
        this.num_used = num_used;
    }

    
    /**
     * Set the ip alias time add
     * 
     * @author Daniele Pantaleone
     * @param  time_add A <tt>DateTime</tt> object representing the time when the ip alias has been first created
     **/
    public void setTimeAdd(DateTime time_add) {
        this.time_add = time_add;
    }

    
    /**
     * Set the ip alias last time edit
     * 
     * @author Daniele Pantaleone
     * @param  time_edit A <tt>DateTime</tt> object representing the ip alias last time edit
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
        return "[ id : " + this.getId() + " | client : " + this.getClient().getId() + " | ip : " + this.getIp().getHostAddress() + " | num_used : " + this.getNumUsed() + " | time_add : " + this.getTimeAdd().toString() + " | time_edit : " + this.getTimeEdit().toString() + " ]";   
    }
    
}