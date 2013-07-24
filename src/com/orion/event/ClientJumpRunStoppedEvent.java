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
 * @version     1.2
 * @copyright   Daniele Pantaleone, 1 February, 2013
 * @package     com.orion.event
 **/

package com.orion.event;

import com.orion.domain.Client;

public class ClientJumpRunStoppedEvent extends Event {

    private final Client client;
    private final int way;
    private final int millis;
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who who started the jump run
     * @param  way The number of the jump run way
     * @param  millis The time performed by the client
     **/
    public ClientJumpRunStoppedEvent(Client client, int way, int millis) {  
        this.client = client;
        this.way = way;
        this.millis = millis;
    }
    
    
    /**
     * Return the <tt>Client</tt> who started the jump run
     * 
     * @author Daniele Pantaleone
     * @return The <tt>Client</tt> who started the jump run
     **/
    public Client getClient() {
        return this.client;
    }
    
    
    /**
     * Return the number of the jump way
     * 
     * @author Daniele Pantaleone
     * @return The number of the jump way
     **/
    public int getWay() {
        return this.way;
    }
    
    
    /**
     * Return the amount of milliseconds of the jump run
     * 
     * @author Daniele Pantaleone
     * @return The amount of milliseconds of the jump run
     **/
    public int getMillis() {
        return this.millis;
    }
    
}