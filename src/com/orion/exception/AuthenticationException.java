/**
 * Copyright (c) 2013, Daniele Pantaleone
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
 * @package     com.orion.exception
 **/

package com.orion.exception;

public class AuthenticationException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    
    /**
     * Build a <tt>RconException</tt> object
     * 
     * @author Daniele Pantaleone
     **/
    public AuthenticationException() {
        super();
    }
    
    
    /**
     * Build a <tt>RconException</tt> object
     * 
     * @author Daniele Pantaleone
     * @param  message The <tt>Exception</tt> message
     * @param  cause A <tt>Throwable</tt> cause
     **/
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
    /**
     * Build a <tt>RconException</tt> object
     * 
     * @author Daniele Pantaleone
     * @param  message The <tt>Exception</tt> message
     **/
    public AuthenticationException(String message) {
        super(message);
    }
    
    
    /**
     * Build a <tt>RconException</tt> object
     * 
     * @author Daniele Pantaleone
     * @param  cause A <tt>Throwable</tt> cause
     **/
    public AuthenticationException(Throwable cause) {
        super(cause);
    }
    
}