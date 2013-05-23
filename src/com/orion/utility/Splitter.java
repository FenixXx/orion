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
 * @copyright   Daniele Pantaleone, 13 March, 2013
 * @package     com.orion.utility
 **/

package com.orion.utility;

import java.util.LinkedList;
import java.util.List;

public class Splitter {

    
    /**
     * Split a <tt>String</tt> into a <tt>List</tt> according to the 
     * maxLength parameter specified. If at the split index a non-space
     * character is found, will rollback until it finds the 1st non-space
     * character occurrence and use that index as our split index
     * 
     * @author Daniele Pantaleone
     * @param  message The <tt>String</tt> to be splitted
     * @param  maxLength The maximum length of each splitted <tt>String</tt>
     * @return A <tt>List</tt> containing the splitted message
     */
    public static List<String> split(String message, int maxLength) {
        
        int startIndex = 0;
        int endIndex = maxLength - 1;
        
        // Building the container for our tokens
        List<String> list = new LinkedList<String>();
        System.out.println(message);
        
        while (true) {
            
            if (endIndex >= message.length()) {
      
                // Rolling back until endIndex is not valid
                while (endIndex  > message.length()) { endIndex--; } 
                list.add(message.substring(startIndex, endIndex));
                break;
                
            } else {
                
                // Rolling back until we find an empty char for the split
                while (message.charAt(endIndex - 1) != ' ') { 
                	
                	endIndex--;
                	
                	// This is to check if the string we have to
                	// split is a CharSequence without spaces
                	// If we do not check it here we may have 
                	// and infinite loop which will crash the bot
                	if (endIndex <= startIndex) {
                		endIndex += maxLength - 1;
                		break;
                	}
                	
                }
                
                list.add(message.substring(startIndex, endIndex));
                startIndex = endIndex;
                endIndex += maxLength - 1;
                continue;
                
            }
            
        }
        
        return list;
        
    }

}
