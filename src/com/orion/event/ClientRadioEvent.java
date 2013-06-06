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
 * @copyright   Daniele Pantaleone, 8 September, 2012
 * @package     com.orion.event
 **/

package com.orion.event;

import com.orion.domain.Client;

public class ClientRadioEvent extends Event {

    private final Client client;
    private final int msg_group;
    private final int msg_id;
    private final String location;
    private final String message;
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone 
     * @param  client The <tt>Client</tt> who issued the radio command
     * @param  msg_group The message group
     * @param  msg_id The message id
     * @param  location The map location name
     * @param  message The message attached to the radio command
     **/
    public ClientRadioEvent(Client client, int msg_group, int msg_id, String location, String message) {  
        super(EventType.EVT_CLIENT_RADIO);
        this.client = client;
        this.msg_group = msg_group;
        this.msg_id = msg_id;
        this.location = location;
        this.message = message;
    }
    
    
    /**
     * Return the <tt>Client</tt> who issued the radio command
     * 
     * @author Daniele Pantaleone
     * @return The <tt>Client</tt> who issued the radio command
     **/
    public Client getClient() {
        return this.client;
    }
    
    
    /**
     * Return the message group
     * 
     * @author Daniele Pantaleone
     * @return The message group
     **/
    public int getMsgGroup() {
        return this.msg_group;
    }
    
    
    /**
     * Return the message id
     * 
     * @author Daniele Pantaleone
     * @return The message id
     **/
    public int getMsgId() {
        return this.msg_id;
    }
    
    
    /**
     * Return the map location name
     * 
     * @author Daniele Pantaleone
     * @return The map location name
     **/
    public String getLocation() {
        return this.location;
    }
    
    
    /**
     * Return the message attached to the radio command
     * 
     * @author Daniele Pantaleone
     * @return The message attached to the radio command
     **/
    public String getMessage() {
        return this.message;
    }
       
}