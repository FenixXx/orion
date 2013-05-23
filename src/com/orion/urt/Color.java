/**
 * This file is part of Orion source code
 * 
 * Copyright (C) 2012 [Gore]Clan - http://www.goreclan.net
 *
 * Orion is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Lesser Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Orion. If not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
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
