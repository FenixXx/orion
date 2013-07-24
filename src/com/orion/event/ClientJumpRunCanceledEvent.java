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
 * @version     1.3
 * @copyright   Daniele Pantaleone, 1 February, 2013
 * @package     com.orion.event
 **/

package com.orion.event;

import com.orion.domain.Client;

public class ClientJumpRunCanceledEvent extends Event {

    private final Client client;
    private final int way;
    private final Integer attempt_num;
    private final Integer attempt_max;
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> whose run has been canceled
     * @param  way The number of the jump way
     **/
    public ClientJumpRunCanceledEvent(Client client, int way) {  
        this.client = client;
        this.way = way;
        this.attempt_num = null;
        this.attempt_max = null;
    }
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> whose run has been canceled
     * @param  way The number of the jump way
     * @param  attempt_num The number of the attempt
     * @param  attempt_max The number of attempts allowed on the server
     **/
    public ClientJumpRunCanceledEvent(Client client, int way, int attempt_num, int attempt_max) {  
        this.client = client;
        this.way = way;
        this.attempt_num = attempt_num;
        this.attempt_max = attempt_max;
    }
    
    
    /**
     * Return the <tt>Client</tt> whose run has been canceled
     * 
     * @author Daniele Pantaleone
     * @return The <tt>Client</tt> whose run has been canceled
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
     * Return the number of the attempt
     * 
     * @author Daniele Pantaleone
     * @return The number of the attempt or <tt>null</tt> if they are unlimited
     **/
    public Integer getAttemptNum() {
        return this.attempt_num;
    }
    
    
    /**
     * Return the number of attempts allowed on the server
     * 
     * @author Daniele Pantaleone
     * @return The number of attempts allowed on the server 
     *         or <tt>null</tt> if they are unlimited
     **/
    public Integer getAttemptMax() {
        return this.attempt_max;
    }
    
}