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
 * @copyright   Daniele Pantaleone, 8 September, 2012
 * @package     com.orion.event
 **/

package com.orion.event;

import com.orion.domain.Client;

public class ClientVoteEvent extends Event {

    private final Client client;
    private final int data;
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone 
     * @param  client The <tt>Client</tt> who voted
     * @param  data The vote value
     **/
    public ClientVoteEvent(Client client, int data) {  
        this.client = client;
        this.data = data;
    }
    
    
    /**
     * Return the <tt>Client</tt> who voted
     * 
     * @author Daniele Pantaleone
     * @return The <tt>Client</tt> who voted
     **/
    public Client getClient() {
        return this.client;
    }
    
    
    /**
     * Return the vote value
     * 
     * @author Daniele Pantaleone
     * @return The vote value
     **/
    public int getData() {
        return this.data;
    }
    
       
}