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
 * @version     1.0
 * @copyright   Daniele Pantaleone, 17 February, 2013
 * @package     com.orion.urt
 **/

package com.orion.urt;

public class Color {

    public final static String RED       = "^1";
    public final static String GREEN     = "^2";
    public final static String YELLOW    = "^3";
    public final static String BLUE      = "^4";
    public final static String CYAN      = "^5";
    public final static String MAGENTA   = "^6";
    public final static String WHITE     = "^7";
    public final static String BLACK     = "^8";
    
    
    /**
     * Return the color matching the given <tt>Team</tt>
     * 
     * @author Daniele Pantaleone
     * @param  team The <tt>Team</tt> from which we need to compute the color
     **/
    public static String getByTeam(Team team) {
        
        switch (team) {
            case RED:        return RED;
            case BLUE:       return BLUE;
            case SPECTATOR:  return YELLOW;
            default:         return WHITE;
        }
        
    }
   
}
