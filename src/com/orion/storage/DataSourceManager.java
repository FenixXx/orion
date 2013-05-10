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
 * @copyright   Daniele Pantaleone, 29 January, 2013
 * @package     com.orion.storage
 **/

package com.orion.storage;

import java.sql.Connection;
import java.sql.SQLException;

public interface DataSourceManager {
    
    
    /**
     * Return the connection with the underline DBMS
     * 
     * @author Daniele Pantaleone
     * @throws ClassNotFoundException 
     * @throws SQLException If the connection with the DBMS fails in being executed
     * @return A <tt>Connection</tt> object with the underline DBMS
     **/
    public abstract Connection getConnection() throws ClassNotFoundException, SQLException;
   
    
    /**
     * Tells wheter the connection with the underline DBMS is established.
     * If the connection has not been established yet, it will try to
     * establish a new one before computing the result.
     * 
     * @author Daniele Pantaleone
     * @return <tt>true</tt> if the database connection is established, 
     *         <tt>false</tt> otherwise
     **/
    public abstract boolean isConnection();

}
