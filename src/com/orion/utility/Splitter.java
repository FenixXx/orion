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
