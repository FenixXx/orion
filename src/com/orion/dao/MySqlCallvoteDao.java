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
 * @copyright   Daniele Pantaleone, 30 January, 2013
 * @package     com.orion.dao
 **/

package com.orion.dao;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.mysql.jdbc.Statement;
import com.orion.bot.Orion;
import com.orion.domain.Callvote;
import com.orion.domain.Client;
import com.orion.domain.Group;
import com.orion.storage.DataSourceManager;

public class MySqlCallvoteDao implements CallvoteDao {
    
    private final DateTimeZone timezone;
    private final DataSourceManager storage;
    
    private PreparedStatement statement;
    private ResultSet resultset;
    
    
    private static final String LOAD_BY_LIMIT = "SELECT `cv`.`id` AS `cv_id`, " +
                                                "`cv`.`type` AS `cv_type`, " +
                                                "`cv`.`data` AS `cv_data`, " +
                                                "`cv`.`yes` AS `cv_yes`, " +
                                                "`cv`.`no` AS `cv_no`, " +
                                                "`cv`.`time_add` AS `cv_time_add`, " +
                                                "`cv`.`time_edit` AS `cl_time_edit`, " +
                                                "`cl`.`name` AS `cl_name`, " +
                                                "`cl`.`connections` AS `cl_connections`, " +
                                                "`cl`.`ip` AS `cl_ip`, " +
                                                "`cl`.`guid` AS `cl_guid`, " +
                                                "`cl`.`auth` AS `cl_auth`, " +
                                                "`cl`.`time_add` AS `cl_time_add`, " +
                                                "`cl`.`time_edit` AS `cl_time_edit`, " +
                                                "`gr`.`id` AS `gr_id`, " +
                                                "`gr`.`name` AS `gr_name`, " +
                                                "`gr`.`keyword` AS `gr_keyword`, " +
                                                "`gr`.`level` AS `gr_level` " +
                                                "FROM `callvotes` AS `cv` " +
                                                "INNER JOIN `clients` AS `cl` ON `cv`.`client_id` = `cl`.`id`" +
                                                "ORDER BY `cv`.`time_add` DESC " +
                                                "LIMIT 0, ?";
        
    
    private static final String LOAD_BY_CLIENT = "SELECT `cv`.`id` AS `cv_id`, " +
                                                 "`cv`.`type` AS `cv_type`, " +
                                                 "`cv`.`data` AS `cv_data`, " +
                                                 "`cv`.`yes` AS `cv_yes`, " +
                                                 "`cv`.`no` AS `cv_no`, " +
                                                 "`cv`.`time_add` AS `cv_time_add`, " +
                                                 "`cv`.`time_edit` AS `cl_time_edit`, " +
                                                 "`cl`.`name` AS `cl_name`, " +
                                                 "`cl`.`connections` AS `cl_connections`, " +
                                                 "`cl`.`ip` AS `cl_ip`, " +
                                                 "`cl`.`guid` AS `cl_guid`, " +
                                                 "`cl`.`auth` AS `cl_auth`, " +
                                                 "`cl`.`time_add` AS `cl_time_add`, " +
                                                 "`cl`.`time_edit` AS `cl_time_edit`, " +
                                                 "`gr`.`id` AS `gr_id`, " +
                                                 "`gr`.`name` AS `gr_name`, " +
                                                 "`gr`.`keyword` AS `gr_keyword`, " +
                                                 "`gr`.`level` AS `gr_level` " +
                                                 "FROM `callvotes` AS `cv` " +
                                                 "INNER JOIN `clients` AS `cl` ON `cv`.`client_id` = `cl`.`id`" +
                                                 "INNER JOIN `groups` AS `gr` ON `cl`.`group_id` = `gr`.`id`" +
                                                 "WHERE `cv`.`client_id` = ? " +
                                                 "ORDER BY `cv`.`time_add` DESC";

    
    private static final String LOAD_BY_CLIENT_LIMIT = "SELECT `cv`.`id` AS `cv_id`, " +
                                                       "`cv`.`type` AS `cv_type`, " +
                                                       "`cv`.`data` AS `cv_data`, " +
                                                       "`cv`.`yes` AS `cv_yes`, " +
                                                       "`cv`.`no` AS `cv_no`, " +
                                                       "`cv`.`time_add` AS `cv_time_add`, " +
                                                       "`cv`.`time_edit` AS `cl_time_edit`, " +
                                                       "`cl`.`name` AS `cl_name`, " +
                                                       "`cl`.`connections` AS `cl_connections`, " +
                                                       "`cl`.`ip` AS `cl_ip`, " +
                                                       "`cl`.`guid` AS `cl_guid`, " +
                                                       "`cl`.`auth` AS `cl_auth`, " +
                                                       "`cl`.`time_add` AS `cl_time_add`, " +
                                                       "`cl`.`time_edit` AS `cl_time_edit`, " +
                                                       "`gr`.`id` AS `gr_id`, " +
                                                       "`gr`.`name` AS `gr_name`, " +
                                                       "`gr`.`keyword` AS `gr_keyword`, " +
                                                       "`gr`.`level` AS `gr_level` " +
                                                       "FROM `callvotes` AS `cv` " +
                                                       "INNER JOIN `clients` AS `cl` ON `cv`.`client_id` = `cl`.`id`" +
                                                       "INNER JOIN `groups` AS `gr` ON `cl`.`group_id` = `gr`.`id`" +
                                                       "WHERE `cv`.`client_id` = ? " +
                                                       "ORDER BY `cv`.`time_add` DESC " +
                                                       "LIMIT 0, 1";
    
    
    private static final String INSERT = "INSERT INTO `callvotes` (`client_id`, `type`, `data`, `yes`, `no`, `time_add`, `time_edit`) VALUES (?,?,?,?,?,?,?)";
    private static final String UPDATE = "UPDATE `callvotes` SET `client_id` = ?, `type` = ?, `data` = ?, `yes` = ?, `no` = ?, `time_edit` = ? WHERE `id` = ?";
    private static final String DELETE = "DELETE FROM `callvotes` WHERE `id` = ?";
        
    
    /**
     * Object constructor.
     * 
     * @author Daniele Pantaleone
     * @param  orion <tt>Orion</tt> object reference
     **/
    public MySqlCallvoteDao(Orion orion) {
        this.timezone = orion.timezone;
        this.storage = orion.storage;
    }
    
    
    /**
     * Return a collection of <tt>Callvote</tt> objects matching the given query limit
     * 
     * @author Daniele Pantaleone
     * @param  limit The number of </tt>Callvote</tt> objects to retrieve
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using a <tt>Client</tt> IP address
     * @return A collection of </tt>Callvote</tt> objects matching the given query limit
     **/
    public List<Callvote> loadByLimit(int limit) throws ClassNotFoundException, SQLException, UnknownHostException {
        
        this.statement = this.storage.getConnection().prepareStatement(LOAD_BY_LIMIT);
        this.statement.setInt(1, limit);
        this.resultset = this.statement.executeQuery();
        List<Callvote> collection = this.getCollectionFromResultSet(this.resultset);
        this.resultset.close();
        this.statement.close();
        
        return collection;
            
    }
    
    
    /**
     * Return a collection of <tt>Callvote</tt> objects matching the given <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> object on which to perform the search
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @return A collection of <tt>Callvote</tt> objects matching the given <tt>Client</tt>
     **/
    public List<Callvote> loadByClient(Client client) throws ClassNotFoundException, SQLException {
        
        this.statement = this.storage.getConnection().prepareStatement(LOAD_BY_CLIENT);
        this.statement.setInt(1, client.id);
        this.resultset = this.statement.executeQuery();
        List<Callvote> collection = this.getCollectionFromResultSet(this.resultset, client);
        this.resultset.close();
        this.statement.close();
        
        return collection;
            
    }
    
    
    /**
     * Return a collection of <tt>Callvote</tt> objects matching the given <tt>Client</tt> and the given query limit
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> object on which to perform the search
     * @param  limit The number of <tt>Callvote</tt> objects to retrieve
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the load query fails somehow
     * @return A collection of <tt>Callvote</tt> objects matching the given <tt>Client</tt> and the given query limit
     **/
    public List<Callvote> loadByClientLimit(Client client, int limit) throws ClassNotFoundException, SQLException {
        
        this.statement = this.storage.getConnection().prepareStatement(LOAD_BY_CLIENT_LIMIT);
        this.statement.setInt(1, client.id);
        this.statement.setInt(2, limit);
        this.resultset = this.statement.executeQuery();
        List<Callvote> collection = this.getCollectionFromResultSet(this.resultset, client);
        this.resultset.close();
        this.statement.close();
        
        return collection;
            
    }
    
    
    /**
     * Create a new entry in the database for the current object.
     * 
     * @author Daniele Pantaleone
     * @param  callvote The <tt>Callvote</tt> object whose informations needs to be stored
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the insert query fails somehow
     **/
    public void insert(Callvote callvote) throws ClassNotFoundException, SQLException { 
        
        this.statement = this.storage.getConnection().prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
        this.statement.setInt(1, callvote.client.id);
        this.statement.setString(2, callvote.type);
        if (callvote.data != null) this.statement.setString(3, callvote.data);
        else this.statement.setNull(3, Types.VARCHAR);
        this.statement.setInt(4, callvote.yes);
        this.statement.setInt(5, callvote.no);
        this.statement.setLong(6, callvote.time_add.getMillis());
        this.statement.setLong(7, callvote.time_edit.getMillis());
        
        // Executing the statement.
        this.statement.executeUpdate();
         
        // Retrieving the generated primary key
        this.resultset = this.statement.getGeneratedKeys();
        if (!this.resultset.next()) throw new SQLException("Unable to retrieve generated primary key from `callvotes` table");
        
        // Storing the new generated client id
        callvote.id = this.resultset.getInt(1);
        
        this.resultset.close();
        this.statement.close();
        
    }
    
    
    /**
     * Update domain object in the database
     * 
     * @author Daniele Pantaleone
     * @param  callvote The <tt>Callvote</tt> object whose informations needs to be updated
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the update query fails somehow
     **/
    public void update(Callvote callvote) throws ClassNotFoundException, SQLException { 
        
        this.statement = this.storage.getConnection().prepareStatement(UPDATE);
        this.statement.setInt(1, callvote.client.id);
        this.statement.setString(2, callvote.type);
        if (callvote.data != null) this.statement.setString(3, callvote.data);
        else this.statement.setNull(3, Types.VARCHAR);
        this.statement.setInt(4, callvote.yes);
        this.statement.setInt(5, callvote.no);
        this.statement.setLong(6, callvote.time_edit.getMillis());
        this.statement.setInt(7, callvote.id);
        
        // Executing the statement.
        this.statement.executeUpdate();
        this.statement.close();
        
    }
    
    
    /**
     * Delete domain object from the database
     * 
     * @author Daniele Pantaleone
     * @param  callvote The <tt>Callvote</tt> object whose informations needs to be deleted
     * @throws ClassNotFoundException If the JDBC driver fails in being loaded
     * @throws SQLException If the delete query fails somehow
     **/
    public void delete(Callvote callvote) throws ClassNotFoundException, SQLException { 
        
        this.statement = this.storage.getConnection().prepareStatement(DELETE);
        statement.setInt(1, callvote.id);
        
        // Executing the statement
        statement.executeUpdate();
        statement.close();
        
    }
    
    
    //////////////////////////////////////////////
    // BEGIN AUXILIARY METHODS
    //////////////////////////////////////////////
    
    
    /**
     * Return a collection of <tt>Callvote</tt> objects accessible through the <tt>List</tt> interface
     * 
     * @author Daniele Pantaleone
     * @param  resultset The <tt>ResultSet</tt> object from which to fetch data
     * @throws SQLException If one of the <tt>ResultSet</tt> objects is not consistent
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using a <tt>Client</tt> IP address
     * @return A collection of <tt>Callvote</tt> objects matching the given <tt>ResultSet</tt>
     **/
    private List<Callvote> getCollectionFromResultSet(ResultSet resultset) throws SQLException, UnknownHostException {
        
        List<Callvote> collection = new LinkedList<Callvote>();
        
        while (resultset.next()) { 
            // Appending all the objects to the collection.
            Callvote callvote = this.getObjectFromCursor(resultset);
            collection.add(callvote);
        }       
        
        return Collections.unmodifiableList(collection);

    }
    
    
    /**
     * Return a collection of <tt>Callvote</tt> objects accessible through the <tt>List</tt> interface
     * 
     * @author Daniele Pantaleone
     * @param  resultset The <tt>ResultSet</tt> object from which to fetch data
     * @param  client The <tt>Client</tt> object those <tt>Callvote</tt> belong to
     * @throws SQLException If one of the <tt>ResultSet</tt> objects is not consistent
     * @return A collection of <tt>Callvote</tt> objects matching the given <tt>ResultSet</tt>
     **/
    private List<Callvote> getCollectionFromResultSet(ResultSet resultset, Client client) throws SQLException {
        
        List<Callvote> collection = new LinkedList<Callvote>();
        
        while (resultset.next()) { 
            // Appending all the objects to the collection.
            Callvote callvote = this.getObjectFromCursor(resultset, client);
            collection.add(callvote);
        }       
        
        return Collections.unmodifiableList(collection);

    }
    
    
    /**
     * Return a <tt>Callvote</tt> object matching the given <tt>ResultSet</tt> tuple
     * 
     * @author Daniele Pantaleone
     * @param  resultset The <tt>ResultSet</tt> object from which to build the <tt>Callvote</tt> object
     * @throws SQLException If the <tt>ResultSet</tt> object is not consistent
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using the <tt>Client</tt> IP address
     * @return A <tt>Callvote</tt> object matching the given <tt>ResultSet</tt> tuple
     **/
    private Callvote getObjectFromCursor(ResultSet resultset) throws SQLException, UnknownHostException {

        Group group = new Group(resultset.getInt("gr_id"), 
                                resultset.getString("gr_name"), 
                                resultset.getString("gr_keyword"), 
                                resultset.getInt("gr_level"));
       
        Client client = new Client(resultset.getInt("cl_id"), 
                                   group, 
                                   resultset.getString("cl_name"), 
                                   resultset.getInt("cl_connections"), 
                                   InetAddress.getByName(resultset.getString("cl_ip")),
                                   resultset.getString("cl_guid"), 
                                   resultset.getString("cl_auth"), 
                                   new DateTime(resultset.getLong("cl_time_add"), this.timezone), 
                                   new DateTime(resultset.getLong("cl_time_edit"), this.timezone));
        
        return new Callvote(resultset.getInt("cv_id"), 
                            client, 
                            resultset.getString("cv_type"), 
                            resultset.getString("cv_data"), 
                            resultset.getInt("cv_yes"), 
                            resultset.getInt("cv_no"), 
                            new DateTime(resultset.getLong("cv_time_add"), this.timezone), 
                            new DateTime(resultset.getLong("cv_time_edit"), this.timezone));
    
    }
    
    
    /**
     * Return a <tt>Callvote</tt> object matching the given <tt>ResultSet</tt> tuple
     * 
     * @author Daniele Pantaleone
     * @param  resultset The <tt>ResultSet</tt> object from which to build the <tt>Callvote</tt> object
     * @param  client The <tt>Client</tt> object this <tt>Callvote</tt> belongs to
     * @throws SQLException If the <tt>ResultSet</tt> object is not consistent
     * @return A <tt>Callvote</tt> object matching the given <tt>ResultSet</tt> tuple
     **/
    private Callvote getObjectFromCursor(ResultSet resultset, Client client) throws SQLException {

        return new Callvote(resultset.getInt("cv_id"), 
                            client, 
                            resultset.getString("cv_type"), 
                            resultset.getString("cv_data"), 
                            resultset.getInt("cv_yes"), 
                            resultset.getInt("cv_no"), 
                            new DateTime(resultset.getLong("cv_time_add"), this.timezone), 
                            new DateTime(resultset.getLong("cv_time_edit"), this.timezone));

    }

}