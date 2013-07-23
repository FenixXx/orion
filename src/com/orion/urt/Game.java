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
 * @copyright   Daniele Pantaleone, 03 February, 2013
 * @package     com.orion.urt
 **/

package com.orion.urt;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Game {
    
    private List<String> maplist;
    private Map<String, Cvar> cvarlist;
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     **/
    public Game() {
        this.maplist = new LinkedList<String>();
        this.cvarlist = new HashMap<String, Cvar>();  
    }
    
    
    /**
     * Return a <tt>List</tt> of maps available on the server<br>
     * NOTE: this method doesn't query the game server
     * 
     * @author Daniele Pantaleone
     * @return The current map list
     **/
    public List<String> getMapList() {
        return this.maplist;
    }

    
    /**
     * Set the <tt>List</tt> of maps available on the server<br>
     * NOTE: this method doesn't query the game server
     * 
     * @author Daniele Pantaleone
     * @param  maplist The current map list
     **/
    public void setMapList(List<String> maplist) {
        this.maplist = maplist;
    }
    
    
    /**
     * Test whether a CVAR is stored in the CVAR list<br>
     * NOTE: this method doesn't query the game server
     * 
     * @author Daniele Pantaleone
     * @param  name The CVAR name
     * @return <tt>true</tt> if the CVAR exists, <tt>false</tt> otherwise
     **/
    public boolean isCvar(String name) {
        return this.cvarlist.containsKey(name.toLowerCase());
    }
    
    
    /**
     * Return the <tt>Cvar</tt> associated to the given name<br>
     * NOTE: this method doesn't query the game server
     * 
     * @author Daniele Pantaleone
     * @param  name The CVAR name
     * @return The <tt>Cvar</tt> associated to the given name or <tt>null</tt>
     *         if the CVAR has not been retrieved and stored yet
     **/
    public Cvar getCvar(String name) {
        return this.isCvar(name) ? this.cvarlist.get(name) : null;
    }
    
    
    /**
     * Store a <tt>Cvar</tt> object in the CVAR list<br>
     * NOTE: this method doesn't query the game server
     * 
     * @author Daniele Pantaleone
     * @param  name The CVAR name
     * @param  cvar The <tt>Cvar</tt> object to be stored
     **/
    public void setCvar(String name, Cvar cvar) {
        this.cvarlist.put(name, cvar);
    }
    
    
    /**
     * Clear CVAR list and map list<br>
     * NOTE: this method doesn't query the game server
     * 
     * @author Daniele Pantaleone
     **/
    public void clear() {
        this.cvarlist.clear();
        this.maplist.clear();
    }
   
    
}