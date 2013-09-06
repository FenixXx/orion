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
 * @copyright   Daniele Pantaleone, 30 January, 2013
 * @package     com.orion.dao
 **/

package com.orion.dao;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.orion.bot.Orion;
import com.orion.domain.Client;
import com.orion.domain.IpAlias;
import com.orion.storage.DataSourceManager;

public class IpAliasDaoMySql implements IpAliasDao {
    
    private final DateTimeZone timezone;
    private final DataSourceManager storage;
    
    private PreparedStatement statement;
    private ResultSet resultset;
    
    private static final String LOAD_BY_CLIENT = "SELECT `id`, `ip`, `num_used`, `time_add`, `time_edit` FROM `ipaliases` WHERE `client_id` = ? ORDER BY `num_used` DESC, `time_edit` DESC"; ;
    private static final String LOAD_BY_CLIENT_IP = "SELECT `id`, `ip`, `num_used`, `time_add`, `time_edit` FROM `ipaliases` WHERE `client_id` = ? AND `ip` = ?";                                         
    private static final String INSERT = "INSERT INTO `ipaliases` (`client_id`, `ip`, `time_add`, `time_edit`) VALUES (?,?,?,?)";
    private static final String UPDATE = "UPDATE `ipaliases` SET `client_id` = ?, `ip` = ?, `num_used` = ?, `time_edit` = ? WHERE `id` = ?";
    private static final String DELETE = "DELETE FROM `ipaliases` WHERE `id` = ?";
        
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  orion <tt>Orion</tt> object reference
     **/
    public IpAliasDaoMySql(Orion orion) {
        this.timezone = orion.timezone;
        this.storage = orion.storage;
    }
    
    
    /**
     * Return a collection of <tt>IpAlias</tt> objects matching the given <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> object on which to perform the search
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using a <tt>Client</tt> IP address
     * @return A collection of </tt>IpAlias</tt> objects matching the given <tt>Client</tt>
     **/
    public List<IpAlias> loadByClient(Client client) throws ClassNotFoundException, SQLException, UnknownHostException {
        
        this.statement = this.storage.getConnection().prepareStatement(LOAD_BY_CLIENT);
        this.statement.setInt(1, client.getId());
        this.resultset = this.statement.executeQuery();
        List<IpAlias> collection = this.getCollectionFromResultSet(this.resultset, client);
        this.resultset.close();
        this.statement.close();
        
        return collection;
            
    }
    
    
    /**
     * Return an <tt>IpAlias</tt> object matching the given <tt>Client</tt> id and IP address
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> object on which to perform the search
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using the <tt>Client</tt> IP address
     * @return An <tt>IpAlias</tt> object matching the given <tt>Client</tt> id and IP address or <tt>null</tt> if we have no match
     **/
    public IpAlias loadByClientIp(Client client) throws ClassNotFoundException, SQLException, UnknownHostException {
        
        this.statement = this.storage.getConnection().prepareStatement(LOAD_BY_CLIENT_IP);
        this.statement.setInt(1, client.getId());
        this.statement.setString(2, client.getIp().getHostAddress());
        this.resultset = this.statement.executeQuery();
        
        if (!this.resultset.next()) {    
            this.resultset.close();
            this.statement.close();
            return null;
        }
        
        IpAlias ipalias = this.getObjectFromCursor(this.resultset, client);
        this.resultset.close();
        this.statement.close();
        
        return ipalias;
            
    }
    

    
    /**
     * Create a new entry in the database for the current object
     * 
     * @author Daniele Pantaleone
     * @param  ipalias The <tt>IpAlias</tt> object whose informations needs to be stored
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the insert query fails somehow
     **/
    public void insert(IpAlias ipalias) throws ClassNotFoundException, SQLException { 
        
        this.statement = this.storage.getConnection().prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
        this.statement.setInt(1, ipalias.getClient().getId());
        this.statement.setString(2, ipalias.getIp().getHostAddress());
        this.statement.setLong(3, ipalias.getTimeAdd().getMillis());
        this.statement.setLong(4, ipalias.getTimeEdit().getMillis());
        
        // Executing the statement.
        this.statement.executeUpdate();
        
        // Retrieving the generated primary key
        this.resultset = this.statement.getGeneratedKeys();
        if (!this.resultset.next()) throw new SQLException("Unable to retrieve generated primary key from `ipaliases` table");
        
        // Storing the new generated client id
        ipalias.setId(this.resultset.getInt(1));
        
        this.resultset.close();
        this.statement.close();
        
    }
    
    
    /**
     * Update domain object in the database
     * 
     * @author Daniele Pantaleone
     * @param  ipalias The <tt>IpAlias</tt> object whose informations needs to be updated
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the update query fails somehow
     **/
    public void update(IpAlias ipalias) throws ClassNotFoundException, SQLException { 
        
        this.statement = this.storage.getConnection().prepareStatement(UPDATE);
        this.statement.setInt(1, ipalias.getClient().getId());
        this.statement.setString(2, ipalias.getIp().getHostAddress());
        this.statement.setInt(3, ipalias.getNumUsed());
        this.statement.setLong(4, ipalias.getTimeEdit().getMillis());
        this.statement.setInt(5, ipalias.getId());
        
        // Executing the statement.
        this.statement.executeUpdate();
        this.statement.close();
        
    }
    
    
    /**
     * Delete domain object from the database.
     * 
     * @author Daniele Pantaleone
     * @param  ipalias The <tt>IpAlias</tt> object whose informations needs to be deleted
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the delete query fails somehow
     **/
    public void delete(IpAlias ipalias) throws ClassNotFoundException, SQLException { 
        
        this.statement = this.storage.getConnection().prepareStatement(DELETE);
        this.statement.setInt(1, ipalias.getId());
        
        // Executing the statement.
        this.statement.executeUpdate();
        this.statement.close();
        
    }
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////// AUXILIARY METHODS /////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    /**
     * Return a collection of <tt>IpAlias</tt> objects accessible through the <tt>List</tt> interface
     * 
     * @author Daniele Pantaleone
     * @param  resultset The <tt>ResultSet</tt> object from which to fetch data
     * @param  client The <tt>Client</tt> who those <tt>IpAlias</tt> objects belong to
     * @throws SQLException If one of the <tt>ResultSet</tt> objects is not consistent
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using a <tt>Client</tt> IP address
     * @return A collection of <tt>IpAlias</tt> objects matching the given <tt>ResultSet</tt>
     **/
    private List<IpAlias> getCollectionFromResultSet(ResultSet resultset, Client client) throws SQLException, UnknownHostException {
        
        List<IpAlias> collection = new LinkedList<IpAlias>();
        
        while (resultset.next()) { 
            // Appending all the objects to the collection.
            IpAlias ipalias = this.getObjectFromCursor(resultset, client);
            collection.add(ipalias);
        }       
        
        return Collections.unmodifiableList(collection);

    }
    
    
    /**
     * Return an <tt>IpAlias</tt> object matching the given <tt>ResultSet</tt> tuple
     * 
     * @author Daniele Pantaleone
     * @param  resultset The <tt>ResultSet</tt> object from which to build the <tt>IpAlias</tt> object
     * @param  client The <tt>Client</tt> who this <tt>IpAlias</tt> belongs to
     * @throws SQLException If the <tt>ResultSet</tt> object is not consistent
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using the <tt>Client</tt> IP address
     * @return An <tt>Alias</tt> object matching the given <tt>ResultSet</tt> tuple
     **/
    private IpAlias getObjectFromCursor(ResultSet resultset, Client client) throws SQLException, UnknownHostException {
        
        return new IpAlias(resultset.getInt("id"), 
                           client, 
                           InetAddress.getByName(resultset.getString("ip")), 
                           resultset.getInt("num_used"),  
                           new DateTime(resultset.getLong("time_add"), this.timezone), 
                           new DateTime(resultset.getLong("time_edit"), this.timezone));
        
    }

    
}