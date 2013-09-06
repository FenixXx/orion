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
 * @copyright   Daniele Pantaleone, 5 September, 2013
 * @package     com.orion.misc
 **/

package com.orion.misc;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {

    
private static String PATTERN = "\\{\\w+\\}";
    
    
    /**
     * Return a formatted <tt>String</tt> by replacing all the tokens 
     * retrieved from the input <tt>String</tt> with the given arguments<br>
     * The <tt>String</tt> to be formatted MUST follow the following pattern:
     * 
     * <pre>
     *      This is an {name} message
     *      Wow, I'm soo {attribute}
     * </pre>
     * 
     * Basically all the tokens must be embraced between <tt>{ }</tt>
     * 
     * @author Daniele Pantaleone
     * @param  format The <tt>String</tt> where to perform the substitution
     * @param  args A set of arguments to place inside the <tt>String</tt> to be returned
     * @throws NullPointerException If the input <tt>String</tt> is <tt>null</tt>
     * @return A formatted <tt>String</tt> obtained  by replacing all the tokens 
     *         retrieved from the input <tt>String</tt> with the given arguments
     **/
    public static String format(String format, Object ... args) {
        
        // Be sure not to have a null input format string
        if (format == null) throw new NullPointerException();
        
        int i = 0;
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(format);
        
        while (((matcher.find()) && (i < args.length))) {
            
            format = format.replaceFirst(matcher.group(0)
                                                .replaceAll("\\{", "\\\\{")
                                                .replaceAll("\\}", "\\\\}"), 
                                                 String.valueOf(args[i]));
            i++;
        
        }
        
        return format;
        
    }
    
    
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
                while (endIndex  > message.length()) { 
                    endIndex--; 
                }
                
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
