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
 * @copyright   Daniele Pantaleone, 29 January, 2013
 * @package     com.orion.storage
 **/

package com.orion.storage;

import java.sql.Connection;
import java.sql.SQLException;

public interface DataSourceManager {
    
    
    /**
     * Return the connection with the storage layer
     * 
     * @author Daniele Pantaleone
     * @throws ClassNotFoundException 
     * @throws SQLException If the connection with the storage layer fails in being executed
     * @return A <tt>Connection</tt> object with the underline DBMS
     **/
    public abstract Connection getConnection() throws ClassNotFoundException, SQLException;
   
    
    /**
     * Tells whether the connection with the storage layer is established<br>
     * If the connection has not been established yet, it will try to
     * establish a new one before computing the result
     * 
     * @author Daniele Pantaleone
     * @return <tt>true</tt> if the connection with the storage layer is established, 
     *         <tt>false</tt> otherwise
     **/
    public abstract boolean isConnection();

}
