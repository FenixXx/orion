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
 * @copyright   Daniele Pantaleone, 15 July, 2012
 * @package     com.orion.event
 **/

package com.orion.event;

import com.orion.domain.Client;
import com.orion.urt.Hitlocation;
import com.orion.urt.Mod;


public class ClientDamageEvent extends Event {

    private final Client client;
    private final Client victim;
    private final Mod mod ;
    private final Hitlocation hitlocation;
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone 
     * @param  client The <tt>Client</tt> who performed the hit
     * @param  victim The <tt>Client</tt> who suffered the hit
     * @param  mod The Urban Terror <tt>Mod</tt> of the hit
     * @param  hitlocation The <tt>Hitlocation</tt> of the hit
     **/
    public ClientDamageEvent(Client client, Client victim, Mod mod, Hitlocation hitlocation) {
        this.client = client;
        this.victim = victim;
        this.mod = mod;
        this.hitlocation = hitlocation;
    }
    
    
    /**
     * Return the <tt>Client</tt> who performed the hit
     * 
     * @author Daniele Pantaleone
     * @return The <tt>Client</tt> who performed the hit
     **/
    public Client getClient() {
        return this.client;
    }
    
    
    /**
     * Return the <tt>Client</tt> who suffered the hit
     * 
     * @author Daniele Pantaleone
     * @return The <tt>Client</tt> who suffered the hit
     **/
    public Client getVictim() {
        return this.victim;
    }
    
    
    /**
     * Return the <tt>Mod</tt> of the hit
     * 
     * @author Daniele Pantaleone
     * @return The <tt>Mod</tt> of the hit
     **/
    public Mod getMod() {
        return this.mod;
    }
    
    
    /**
     * Return the <tt>Hitlocation</tt> of the hit
     * 
     * @author Daniele Pantaleone
     * @return The <tt>Hitlocation</tt> of the hit
     **/
    public Hitlocation getHitLocation() {
        return this.hitlocation;
    }
       
}