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

import com.orion.domain.Callvote;
import com.orion.domain.Client;

public class ClientCallvoteEvent extends Event {

    private final Client client;
    private final Callvote callvote;
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who issued the vote
     * @param  callvote The <tt>Callvote</tt> object
     **/
    public ClientCallvoteEvent(Client client, Callvote callvote) {  
        super(EventType.EVT_CLIENT_CALLVOTE);
        this.client = client;
        this.callvote = callvote;
    }
    
    
    /**
     * Return the <tt>Client</tt> who issued the vote
     * 
     * @author Daniele Pantaleone
     * @return The <tt>Client</tt> who issued the vote
     **/
    public Client getClient() {
        return this.client;
    }
    
    
    /**
     * Return the <tt>Callvote</tt> object
     * 
     * @author Daniele Pantaleone
     * @return The <tt>Callvote</tt> object
     **/
    public Callvote getCallvote() {
        return this.callvote;
    }
    
}