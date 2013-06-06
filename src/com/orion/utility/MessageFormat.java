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
