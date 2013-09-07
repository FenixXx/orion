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
 * @copyright   Daniele Pantaleone, 15 March, 2013
 * @package     com.orion.utility
 **/

package com.orion.misc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.orion.exception.ParserException;

public class TimeParser {
    
    
    /**
     * Return a human readable time notation given a short one.
     * The following format is accepted:
     * 
     * <pre>
     *    1s => 1 second
     *    1m => 1 minute
     *    1h => 1 hour
     *    1d => 1 day
     *    1w => 1 week
     *    1y => 1 year
     * </pre>
     * 
     * @author Daniele Pantaleone
     * @param  format The short human readable time notation
     * @throws ParserException If the input <tt>String</tt> is not well formatted
     * @return The human readable time notation corresponding to the given short one
     **/
    public static String getHumanReadableTime(String format) throws ParserException {
        
        // Retrieving value and the time string suffix
        Pattern pattern = Pattern.compile("^(?<unit>[\\d\\.]+)(?<suffix>[s|S|m|M|h|H|d|D|w|W|y|Y]*)$");
        Matcher matcher = pattern.matcher(format);
        
        // Check if the input format is correct before doing the parse job
        if (!matcher.matches()) throw new ParserException("Unable to parse string as human readable time format: " + format);
        
        String suffix = matcher.group("suffix").toLowerCase();
        double unit = Double.parseDouble(matcher.group("unit"));

        switch (suffix) {
            case "s": return unit + ((unit > 1) ? "seconds" : "second");
            case "m": return unit + ((unit > 1) ? "minutes" : "minute");
            case "h": return unit + ((unit > 1) ? "hours"   : "hour");
            case "d": return unit + ((unit > 1) ? "days"    : "day");
            case "w": return unit + ((unit > 1) ? "weeks"   : "week");
            case "y": return unit + ((unit > 1) ? "years"   : "year");
            default : return unit + ((unit > 1) ? "seconds" : "second");
        } 
        
    }
    
    
    /**
     * Return a <tt>String</tt> with the human readable time notation
     * given its value expressed in milliseconds
     * 
     * @author Daniele Pantaleone
     * @param  time The amount of milliseconds to convert
     * @return A <tt>String</tt> with the human readable time notation
     *         given its value expressed in milliseconds
     **/
    public static String getHumanReadableTime(long time) {
        
        if (time < 60000) {
            double unit = Math.round(time / 1000.0 * 10.0) / 10.0;
            return unit + (unit > 1 ? " seconds" : " second");
        } else if (time < 60000 * 60) {
            double unit = Math.round(time / 60000.0 * 10.0) / 10.0;
            return unit + (unit > 1 ? " minutes" : " minute");
        } else if (time < 60000 * 60 * 24) {
            double unit = Math.round(time / (60000.0 * 60) * 10.0) / 10.0;
            return unit + (unit > 1 ? " hours" : " hour");
        } else if (time < 60000 * 60 * 24 * 7) {
            double unit = Math.round(time / (60000.0 * 60 * 24) * 10.0) / 10.0;
            return unit + (unit > 1 ? " days" : " day");
        } else if (time < 60000 * 60 * 24 * 365) {
            double unit = Math.round(time / (60000.0 * 60 * 24 * 7) * 10.0) / 10.0;
            return unit + (unit > 1 ? " weeks" : " week");
        } else {
            double unit = Math.round(time / (60000.0 * 60 * 24 * 365) * 10.0) / 10.0;
            return unit + (unit > 1 ? " years" : " year");
        }

    }
    
    
    /**
     * Parse a human readable time <tt>String</tt> format and return the 
     * correspondent amount of milliseconds as <tt>long</tt> value.
     * The following format is accepted:
     * 
     * <pre>
     *    1s = second
     *    1m = minute
     *    1h = hour
     *    1d = day
     *    1w = week
     *    1y = year
     * </pre>
     * 
     * If the input <tt>String</tt> is a full numeric value, it's assumed
     * to be specified as an amount of seconds, so the parser will just
     * convert it to milliseconds
     * 
     * @author Daniele Pantaleone
     * @param  format The <tt>String</tt> to be parsed
     * @throws ParserException If the input <tt>String</tt> is not well formatted
     * @return The amount of milliseconds corresponding the input value
     **/
    public static long parseTime(String format) throws ParserException {
        
        // Retrieving value and the time string suffix
        Pattern pattern = Pattern.compile("^(?<unit>[\\d\\.]+)(?<suffix>[s|S|m|M|h|H|d|D|w|W|y|Y]*)$");
        Matcher matcher = pattern.matcher(format);
        
        // Check if the input format is correct before doing the parse job
        if (!matcher.matches()) throw new ParserException("Unable to parse string as human readable time format: " + format);
        
        String suffix = matcher.group("suffix").toLowerCase();
        double unit = Double.parseDouble(matcher.group("unit"));
        
        switch (suffix) {
            case "s": return (long) unit * 1000;
            case "m": return (long) unit * 1000 * 60;
            case "h": return (long) unit * 1000 * 60 * 60;
            case "d": return (long) unit * 1000 * 60 * 60 * 24;
            case "w": return (long) unit * 1000 * 60 * 60 * 24 * 7;
            case "y": return (long) unit * 1000 * 60 * 60 * 24 * 365;
            default : return (long) unit * 1000;
        } 
        
    }
    
    
    /**
     * Parse a human readable time <tt>String</tt> format and return the 
     * correspondent amount of milliseconds as <tt>Long</tt> value.
     * The following format is accepted:
     * 
     * <pre>
     *    1s = second
     *    1m = minute
     *    1h = hour
     *    1d = day
     *    1w = week
     *    1y = year
     * </pre>
     * 
     * If the input <tt>String</tt> is a full numeric value, it's assumed
     * to be specified as an amount of seconds, so the parser will just
     * convert it to milliseconds
     * 
     * @author Daniele Pantaleone
     * @param  format The <tt>String</tt> to be parsed
     * @throws ParserException If the input <tt>String</tt> is not well formatted
     * @return The amount of milliseconds corresponding the input value
     **/
    public static Long valueOf(String format) throws ParserException {
        
        // Retrieving value and the time string suffix
        Pattern pattern = Pattern.compile("^(?<unit>[\\d\\.]+)(?<suffix>[s|S|m|M|h|H|d|D|w|W|y|Y]*)$");
        Matcher matcher = pattern.matcher(format);
        
        // Check if the input format is correct before doing the parse job
        if (!matcher.matches()) throw new ParserException("Unable to parse string as human readable time format: " + format);
        
        String suffix = matcher.group("suffix").toLowerCase();
        double unit = Double.parseDouble(matcher.group("unit"));
        
        switch (suffix) {
            case "s": return Long.valueOf((long) unit * 1000);
            case "m": return Long.valueOf((long) unit * 1000 * 60);
            case "h": return Long.valueOf((long) unit * 1000 * 60 * 60);
            case "d": return Long.valueOf((long) unit * 1000 * 60 * 60 * 24);
            case "w": return Long.valueOf((long) unit * 1000 * 60 * 60 * 24 * 7);
            case "y": return Long.valueOf((long) unit * 1000 * 60 * 60 * 24 * 365);
            default : return Long.valueOf((long) unit * 1000);
        } 
        
    }
    

}
