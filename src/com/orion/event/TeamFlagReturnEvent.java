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
 * @copyright   Daniele Pantaleone, 02 February, 2013
 * @package     com.orion.event
 **/

package com.orion.event;

import com.orion.urt.Team;

public class TeamFlagReturnEvent extends Event {

    private final Team team;
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  team The <tt>Team</tt> whose flag has been returned
     **/
    public TeamFlagReturnEvent(Team team) {
        this.team = team;
    }
    
    
    /**
     * Return the <tt>Team</tt> whose flag has been returned
     * 
     * @author Daniele Pantaleone
     * @return The <tt>Team</tt> whose flag has been returned
     **/
    public Team getTeam() {
        return this.team;
    }
    
}
 