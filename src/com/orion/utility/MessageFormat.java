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
 * @copyright   Daniele Pantaleone, 16 March, 2013
 * @package     com.orion.utility
 **/

package com.orion.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageFormat {
    
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
    
}
