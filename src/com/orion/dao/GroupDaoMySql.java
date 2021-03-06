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
 * @package     com.orion.dao
 **/

package com.orion.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.orion.domain.Group;
import com.orion.storage.DataSourceManager;

public class GroupDaoMySql implements GroupDao {
    
    private final DataSourceManager storage;
    
    private PreparedStatement statement;
    private ResultSet resultset;
    
    private static final String LOAD_BY_ID        = "SELECT `id`, `name`, `keyword`, `level` FROM `groups` WHERE `id` = ?";
    private static final String LOAD_BY_NAME      = "SELECT `id`, `name`, `keyword`, `level` FROM `groups` WHERE `name` = ?";
    private static final String LOAD_BY_KEYWORD   = "SELECT `id`, `name`, `keyword`, `level` FROM `groups` WHERE `keyword` = ?";
    private static final String LOAD_BY_LEVEL     = "SELECT `id`, `name`, `keyword`, `level` FROM `groups` WHERE `level` = ?";
    private static final String LOAD_ALL          = "SELECT `id`, `name`, `keyword`, `level` FROM `groups`";
    private static final String INSERT            = "INSERT INTO `groups` (`id`, `name`, `keyword`, `level`) VALUES (?,?,?,?)";
    private static final String UPDATE            = "UPDATE `groups` SET `name` = ?, `keyword` = ?, `level` = ? WHERE `id` = ?";
    private static final String DELETE            = "DELETE FROM `groups` WHERE `id` = ?";
        
    
    /**
     * Object constructor.
     * 
     * @author Daniele Pantaleone
     * @param  storage The <tt>DataSourceManager</tt> interface
     **/
    public GroupDaoMySql(DataSourceManager storage) {
        this.storage = storage;
    }
    
    
    /**
     * Return the <tt>Group</tt> object matching the specified <tt>Group</tt> id
     * 
     * @author Daniele Pantaleone
     * @param  id The <tt>Group</tt> object primary key
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @return A <tt>Group</tt> object matching the given id or <tt>null</tt> if we have no match
     **/
    public Group loadById(int id) throws ClassNotFoundException, SQLException {
        
        this.statement = this.storage.getConnection().prepareStatement(LOAD_BY_ID);
        this.statement.setInt(1, id);
        this.resultset = this.statement.executeQuery();
        
        if (!this.resultset.next()) {    
            this.resultset.close();
            this.statement.close();
            return null;
        }
        
        // Retrieving the group object from the ResultSet tuple
        Group group = this.getObjectFromCursor(resultset);
        
        this.resultset.close();
        this.statement.close();
        
        return group;
            
    }
    
    
    /**
     * Return the <tt>Group</tt> object matching the specified name
     * 
     * @author Daniele Pantaleone
     * @param  name The Group object name
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @return A <tt>Group</tt> object matching the given <tt>name</tt> or <tt>null</tt> if we have no match
     **/
    public Group loadByName(String name) throws ClassNotFoundException, SQLException {
        
        this.statement = this.storage.getConnection().prepareStatement(LOAD_BY_NAME);
        this.statement.setString(1, name);
        this.resultset = this.statement.executeQuery();
        
        if (!this.resultset.next()) {    
            this.resultset.close();
            this.statement.close();
            return null;
        }
        
        // Retrieving the group object from the ResultSet tuple
        Group group = this.getObjectFromCursor(resultset);
        
        this.resultset.close();
        this.statement.close();
        
        return group;
            
    }
    
    
    /**
     * Return the <tt>Group</tt> object matching the specified keyword
     * 
     * @author Daniele Pantaleone
     * @param  keyword The <tt>Group</tt> object keyword
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @return A <tt>Group</tt> object matching the given keyword or <tt>null</tt> if we have no match
     **/
    public Group loadByKeyword(String keyword) throws ClassNotFoundException, SQLException {
        
        this.statement = this.storage.getConnection().prepareStatement(LOAD_BY_KEYWORD);
        this.statement.setString(1, keyword);
        this.resultset = this.statement.executeQuery();
        
        if (!this.resultset.next()) {    
            this.resultset.close();
            this.statement.close();
            return null;
        }
        
        // Retrieving the group object from the ResultSet tuple
        Group group = this.getObjectFromCursor(resultset);
        
        this.resultset.close();
        this.statement.close();
        
        return group;
            
    }
    
    
    /**
     * Return the <tt>Group</tt> object matching the specified level
     * 
     * @author Daniele Pantaleone
     * @param  level The <tt>Group</tt> object level
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @return A <tt>Group</tt> object matching the given level or <tt>null</tt> if we have no match
     **/
    public Group loadByLevel(int level) throws ClassNotFoundException, SQLException {
        
        this.statement = this.storage.getConnection().prepareStatement(LOAD_BY_LEVEL);
        this.statement.setInt(1, level);
        this.resultset = this.statement.executeQuery();
        
        if (!this.resultset.next()) {    
            this.resultset.close();
            this.statement.close();
            return null;
        }
        
        // Retrieving the group object from the ResultSet tuple
        Group group = this.getObjectFromCursor(resultset);
        
        this.resultset.close();
        this.statement.close();
        
        return group;
            
    }
    
    
    /**
     * Return a collection of all the <tt>Group</tt> objects retrieved from the storage level
     * 
     * @author Daniele Pantaleone
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @return A collection of all the <tt>Group</tt> objects retrieved from the storage level
     **/
    public List<Group> loadAll() throws ClassNotFoundException, SQLException  {
        
        this.statement = this.storage.getConnection().prepareStatement(LOAD_ALL);
        this.resultset = this.statement.executeQuery();
        List<Group> collection = this.getCollectionFromResultSet(resultset);
        this.resultset.close();
        this.statement.close();
        
        return collection;
        
    }
    
    
    /**
     * Create a new entry in the database for the current object
     * 
     * @author Daniele Pantaleone
     * @param  group The <tt>Group</tt> object whose informations needs to be stored
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the insert query fails somehow
     **/
    public void insert(Group group) throws ClassNotFoundException, SQLException { 
        this.statement = this.storage.getConnection().prepareStatement(INSERT);
        this.statement.setInt(1, group.getId());
        this.statement.setString(2, group.getName());
        this.statement.setString(3, group.getKeyword());
        this.statement.setInt(4, group.getLevel());
        this.statement.executeUpdate();
        this.statement.close();
    }
    
    
    /**
     * Update domain object in the database
     * 
     * @author Daniele Pantaleone
     * @param  group The <tt>Group</tt> object whose informations needs to be updated
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the update query fails somehow
     **/
    public void update(Group group) throws ClassNotFoundException, SQLException { 
        this.statement = this.storage.getConnection().prepareStatement(UPDATE);
        this.statement.setString(1, group.getName());
        this.statement.setString(2, group.getKeyword());
        this.statement.setInt(3, group.getLevel());
        this.statement.setInt(4, group.getId());
        this.statement.executeUpdate();
        this.statement.close(); 
    }
    
    
    /**
     * Delete domain object from the database
     * 
     * @author Daniele Pantaleone
     * @param  group The <tt>Group</tt> object whose informations needs to be deleted
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the delete query fails somehow
     **/
    public void delete(Group group) throws ClassNotFoundException, SQLException { 
        this.statement = this.storage.getConnection().prepareStatement(DELETE);
        this.statement.setInt(1, group.getId());
        this.statement.executeUpdate();
        this.statement.close();
    }
    
    
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////// AUXILIARY METHODS /////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    /**
     * Return a collection of <tt>Group</tt> objects accessible through the <tt>List</tt> interface
     * 
     * @author Daniele Pantaleone
     * @param  resultset The <tt>ResultSet</tt> object from which to fetch data
     * @throws SQLException If one of the <tt>ResultSet</tt> objects is not consistent
     * @return A collection of <tt>Group</tt> objects matching the given <tt>ResultSet</tt>
     **/
    private List<Group> getCollectionFromResultSet(ResultSet resultset) throws SQLException {
        
       List<Group> collection = new LinkedList<Group>();
        
        while (resultset.next()) { 
            // Appending all the objects to the collection.
            Group group = this.getObjectFromCursor(resultset);
            collection.add(group);
        }       
        
        return Collections.unmodifiableList(collection);

    }
    
    
    /**
     * Return a <tt>Group</tt> object matching the given <tt>ResultSet</tt> tuple
     * 
     * @author Daniele Pantaleone
     * @param  resultset The <tt>ResultSet</tt> object from which to build the <tt>Group</tt> object
     * @throws SQLException If the <tt>ResultSet</tt> object is not consistent
     * @return A <tt>Group</tt> object matching the given <tt>ResultSet</tt> tuple
     **/
    private Group getObjectFromCursor(ResultSet resultset) throws SQLException {
    	// Building and returning the initialized Group object
        return new Group(resultset.getInt("id"), resultset.getString("name"), resultset.getString("keyword"), resultset.getInt("level"));
    }

    
}