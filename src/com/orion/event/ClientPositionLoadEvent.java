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
 * @copyright   Daniele Pantaleone, 12 February, 2013
 * @package     com.orion.event
 **/

package com.orion.event;

import com.orion.domain.Client;

public class ClientPositionLoadEvent extends Event {

    private final Client client;
    private final float x;
    private final float y;
    private final float z;
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone 
     * @param  client The <tt>Client</tt> who loaded a position
     * @param  x The X coordinate
     * @param  y The Y coordinate
     * @param  z The Z coordinate
     **/
    public ClientPositionLoadEvent(Client client, float x, float y, float z) {
        this.client = client;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    
    /**
     * Return the <tt>Client</tt> who loaded a position
     * 
     * @author Daniele Pantaleone
     * @return The <tt>Client</tt> who loaded a position
     **/
    public Client getClient() {
        return this.client;
    }
  
    
    /**
     * Return the X coordinate
     * 
     * @author Daniele Pantaleone
     * @return The X coordinate
     **/
    public float getX() {
        return this.x;
    }
    
    
    /**
     * Return the Y coordinate
     * 
     * @author Daniele Pantaleone
     * @return The Y coordinate
     **/
    public float getY() {
        return this.y;
    }
    
    
    /**
     * Return the Z coordinate
     * 
     * @author Daniele Pantaleone
     * @return The Z coordinate
     **/
    public float getZ() {
        return this.z;
    }
    
}