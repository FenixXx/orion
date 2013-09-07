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
 * @version     1.2
 * @copyright   Daniele Pantaleone, 20 October, 2012
 * @package     com.orion.control
 **/

package com.orion.control;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;

import com.orion.bot.Orion;
import com.orion.dao.GroupDao;
import com.orion.dao.GroupDaoMySql;
import com.orion.domain.Group;

public class GroupCtl {
   
    private final Log log;
    private final GroupDao dao;
    private List<Group> groups;
    
    
    /**
     * Object constructor.
     * 
     * @author Daniele Pantaleone
     * @param  orion <tt>Orion</tt> object reference
     **/
    public GroupCtl(Orion orion) {
        this.log = orion.log;
        this.dao = new GroupDaoMySql(orion.storage);
        this.groups = new LinkedList<Group>();
    }
    
    
    /**
     * Return the <tt>Group</tt> object matching the specified <tt>Group</tt> id.
     * The search is performed at first on the prebuilt group list.
     * If it's empty it will be loaded from the storage level so the search can be performed.
     * 
     * @author Daniele Pantaleone
     * @param  id The <tt>Group</tt> object primary key
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @return A <tt>Group</tt> object matching the given id or <tt>null</tt> if we have no match
     **/
    public Group getById(int id) throws ClassNotFoundException, SQLException {
        
        if (this.groups == null || this.groups.size() == 0) 
            this.groups = this.dao.loadAll();
        
        for (Group group : this.groups)
            if (group.getId() == id)
                return group;
        
        return null;
        
    }
    
    
    /**
     * Return the <tt>Group</tt> matching the specified <tt>Group</tt> name.
     * The search is performed at first on the prebuilt group list.
     * If it's empty it will be loaded from the storage level so the search can be performed.
     * 
     * @author Daniele Pantaleone
     * @param  name The <tt>Group</tt> name
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @return A <tt>Group</tt> object matching the given name or <tt>null</tt> if we have no match
     **/
    public Group getByName(String name) throws ClassNotFoundException, SQLException {
        
        if (this.groups == null || this.groups.size() == 0) 
            this.groups = this.dao.loadAll();
        
        for (Group group : this.groups)
            if (group.getName().equals(name))
                return group;
        
        return null;
        
    }
    
    
    /**
     * Return the <tt>Group</tt> matching the specified <tt>Group</tt> keyword.
     * The search is performed at first on the prebuilt group list.
     * If it's empty it will be loaded from the storage level so the search can be performed.
     * 
     * @author Daniele Pantaleone
     * @param  id The <tt>Group</tt> object keyword
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @return A <tt>Group</tt> object matching the given keyword or <tt>null</tt> if we have no match
     **/
    public Group getByKeyword(String keyword) throws ClassNotFoundException, SQLException {
        
        if (this.groups == null || this.groups.size() == 0) 
            this.groups = this.dao.loadAll();
        
        // Lowercasing the keyword.
        keyword = keyword.toLowerCase();
        
        for (Group group : this.groups)
            if (group.getKeyword().equals(keyword))
                return group;
        
        return null;
        
    }
    
    
    /**
     * Return the <tt>Group</tt> matching the specified pattern.
     * The search is performed at first on the prebuilt group list.
     * If it's empty it will be loaded from the storage level so the search can be performed.
     * The pattern will be matched with the group <tt>level</tt> or the group <tt>keyword</tt>
     * 
     * @author Daniele Pantaleone
     * @param  id The <tt>Group</tt> object keyword
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @return A <tt>Group</tt> object matching the given keyword or null if we have no match
     **/
    public Group getByMagic(String pattern) throws ClassNotFoundException, SQLException {
        if (!pattern.matches("^\\d+$")) return this.getByKeyword(pattern);
        else return this.getByLevel(Integer.parseInt(pattern));
    }
    
    
    /**
     * Return the <tt>Group</tt> object matching the specified Group level.
     * The search is performed at first on the prebuilt group list.
     * If it's empty it will be loaded from the storage level so the search can be performed.
     * 
     * @author Daniele Pantaleone
     * @param  level The <tt>Group</tt> object level
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @return A <tt>Group</tt> object matching the given level or null if we have no match
     **/
    public Group getByLevel(int level) throws ClassNotFoundException, SQLException {
        
        if (this.groups == null || this.groups.size() == 0) 
            this.groups = this.dao.loadAll();
        
        for (Group group : this.groups)
            if (group.getLevel() == level)
                return group;
        
        return null;
        
    }
    
    
    /**
     * Insert a new <tt>Group</tt> object in the database.
     *
     * @author Daniele Pantaleone
     * @param  group The <tt>Group</tt> object to be saved in the database
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     **/
    public void insert(Group group) throws ClassNotFoundException, SQLException {
        this.log.trace("[SQL] INSERT `groups`: " + group.toString());
        this.dao.insert(group);
    }
    
    
    /**
     * Update the <tt>Group</tt> object informations in the database.
     *
     * @author Daniele Pantaleone
     * @param  group The <tt>Group</tt> object to be updated in the database
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     **/
    public void update(Group group) throws ClassNotFoundException, SQLException {
        this.log.trace("[SQL] UPDATE `groups`: " + group.toString());
        this.dao.update(group);
    }
    
    /**
     * Delete the <tt>Group</tt> object from the database.
     *
     * @author Daniele Pantaleone
     * @param  group The <tt>Client</tt> object to be deleted from the database
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     **/
    public void delete(Group group) throws ClassNotFoundException, SQLException {
        this.log.trace("[SQL] DELETE `groups`: " + group.toString());
        this.dao.delete(group);
    }
    
}