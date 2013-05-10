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
 * @package     com.orion.dao
 **/

package com.orion.dao;

import java.sql.SQLException;
import java.util.List;

import com.orion.domain.Group;

public interface GroupDao {
    
	/**
     * Return the <tt>Group</tt> object matching the specified <tt>Group</tt> id
     * 
     * @author Daniele Pantaleone
     * @param  id The <tt>Group</tt> object primary key
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @return A <tt>Group</tt> object matching the given id or <tt>null</tt> if we have no match
     **/
    public abstract Group loadById(int id) throws ClassNotFoundException, SQLException;
    
    
    /**
     * Return the <tt>Group</tt> object matching the specified name
     * 
     * @author Daniele Pantaleone
     * @param  name The Group object name
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @return A <tt>Group</tt> object matching the given <tt>name</tt> or <tt>null</tt> if we have no match
     **/
    public abstract Group loadByName(String name) throws ClassNotFoundException, SQLException;
    
    
    /**
     * Return the <tt>Group</tt> object matching the specified keyword
     * 
     * @author Daniele Pantaleone
     * @param  keyword The <tt>Group</tt> object keyword
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @return A <tt>Group</tt> object matching the given keyword or <tt>null</tt> if we have no match
     **/
    public abstract Group loadByKeyword(String keyword) throws ClassNotFoundException, SQLException;
    
    
    /**
     * Return the <tt>Group</tt> object matching the specified level
     * 
     * @author Daniele Pantaleone
     * @param  level The <tt>Group</tt> object level
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @return A <tt>Group</tt> object matching the given level or <tt>null</tt> if we have no match
     **/
    public abstract Group loadByLevel(int level) throws ClassNotFoundException, SQLException;
    
    
    /**
     * Return a collection of all the <tt>Group</tt> objects retrieved from the storage level
     * 
     * @author Daniele Pantaleone
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @return A collection of all the <tt>Group</tt> objects retrieved from the storage level
     **/
    public abstract List<Group> loadAll() throws ClassNotFoundException, SQLException;
    
    
    /**
     * Create a new entry in the database for the current object
     * 
     * @author Daniele Pantaleone
     * @param  group The <tt>Group</tt> object whose informations needs to be stored
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the insert query fails somehow
     **/
    public abstract void insert(Group group) throws ClassNotFoundException, SQLException;
    
    
    /**
     * Update domain object in the database
     * 
     * @author Daniele Pantaleone
     * @param  group The <tt>Group</tt> object whose informations needs to be updated
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the update query fails somehow
     **/
    public abstract void update(Group group) throws ClassNotFoundException, SQLException;
    
    
    /**
     * Delete domain object from the database
     * 
     * @author Daniele Pantaleone
     * @param  group The <tt>Group</tt> object whose informations needs to be deleted
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the delete query fails somehow
     **/
    public abstract void delete(Group group) throws ClassNotFoundException, SQLException;
    
}