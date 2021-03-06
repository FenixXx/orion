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
 * @copyright   Daniele Pantaleone, 17 February, 2013
 * @package     com.orion.plugin
 **/

package com.orion.plugin;

import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.google.common.base.Joiner;
import com.google.common.eventbus.Subscribe;
import com.orion.annotation.Dependency;
import com.orion.annotation.Usage;
import com.orion.bot.Orion;
import com.orion.bot.PenaltyType;
import com.orion.command.Command;
import com.orion.command.Prefix;
import com.orion.comparator.ClientLevelComparator;
import com.orion.comparator.ClientSlotComparator;
import com.orion.console.UrT42Console;
import com.orion.domain.Alias;
import com.orion.domain.Client;
import com.orion.domain.Group;
import com.orion.domain.IpAlias;
import com.orion.domain.Penalty;
import com.orion.event.ClientConnectEvent;
import com.orion.event.ClientNameChangeEvent;
import com.orion.event.ClientTeamChangeEvent;
import com.orion.exception.CommandRuntimeException;
import com.orion.exception.CommandSyntaxException;
import com.orion.misc.RegisteredCommand;
import com.orion.misc.TimeParser;
import com.orion.urt.Color;
import com.orion.urt.Team;
import com.orion.utility.Configuration;
import com.orion.utility.MessageFormat;

public class AdminPlugin extends Plugin {
    
    public Map<String, String> messages;
    public Map<String, String> keywords;
    
    public int adminLevel;
    public int noReasonLevel;
    public long banDuration;
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone
     * @param  config Plugin <tt>Configuration</tt> object
     * @param  orion <tt>Orion</tt> object reference
     **/
    public AdminPlugin(Configuration config, Orion orion) {
        super(config, orion);
    }
    
    
    /**
     * Load the <tt>Plugin</tt> configuration file and fill <tt>Plugin</tt> attributes
     * 
     * @author Daniele Pantaleone
     **/
    @Override
    public void onLoadConfig() { 
        
        this.debug("Loading plugin configuration...");
        
        this.messages = this.config.getMap("messages");
        this.keywords = this.config.getMap("keywords");
        
        this.adminLevel = this.config.getInt("settings", "admin_level", 20);
        this.noReasonLevel = this.config.getInt("settings", "no_reason_level", 80);
        this.banDuration = this.config.getTime("settings", "ban_duration", 604800000);
        
    }
    
    
    /**
     * Perform operations on <tt>Plugin</tt> startup
     * Method usage:
     * 
     * <ul>
     *     <li>Register plugin commands</li>
     *     <li>Register plugin events</li>
     *     <li>Synchronize the plugin with the current game status</li>
     * </ul>
     * 
     * @author Daniele Pantaleone
     **/
    @Override
    public void onStartup()  { 
        
        this.debug("Starting plugin...");
        
        // Getting the command section from configuration file
        Map<String, String> commands = this.config.getMap("commands");
        
        // Iterating through all the commands so we can register them
        for (Map.Entry<String, String> entry : commands.entrySet()) {
            
            String alias  = null;
            String name   = entry.getKey();
            String group  = entry.getValue();
            
            if (name.contains("-")) {
                String handler[] = name.split("-", 2);
                name  = handler[0];
                alias = handler[1];
            }
            
            // Registering the command
            this.registerCommand(name, alias, group);
                
        }
        
        
        try {
            
            // Check if we need to enable !iamgod
            List<Client> collection = this.clients.getByGroupFull("superadmin");
            
            if (collection.size() == 0) {
                // Enabling the !iamgod command
                this.log.trace("Enabling !iamgod command. There are is no Super Admin on this server yet");
                this.registerCommand("iamgod", null,"guest");
            }
            
        } catch (ClassNotFoundException | UnknownHostException | SQLException e) {
            
            // Logging the Exception
            this.error("Unable to lookup for Super Admin. !iamgod command is not going to be enabled", e);
            
        }
        
        // Listen for produced events
        this.eventBus.register(this);
           
    }
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////// EVENTS SECTION //////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    /**
     * Performs operation on <tt>EVT_CLIENT_CONNECT</tt>
     * 
     * @author Daniele Pantaleone
     * @param  event The generated <tt>Event</tt>
     **/
    @Subscribe
    public void onClientConnect(ClientConnectEvent event) {
    
        // Copying the client reference
        Client client = event.getClient();
        
        ///////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////// PENALIES //////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////
        
        try {
            
            // Checking if the connecting Client has an active ban
            List<Penalty> penalties = this.penalties.getByClient(client, PenaltyType.BAN);
            
            if (penalties.size() != 0) {
                
                Penalty penalty = penalties.get(0);
                
                // Found at least one active BAN for the connecting client
                // We'll kick him again for the same reason he was banned
                this.debug("Connecting client " + client.getName() + "[@" + client.getId() + "] has an active BAN " + penalty.toString());
                this.debug("Kicking client " + client.getName() + "[@" + client.getId() + "] and stopping to process " + event.getType().name());
                this.console.kick(client, penalty.getReason()); 
 
            }
            
        } catch (ClassNotFoundException | SQLException | UnknownHostException | IndexOutOfBoundsException e) {
            
            // Logging the Exception. Using a more verbose log message so we can identify
            // in a better way the function where such Exception as been raised and catched
            this.error("Exception generated on " + event.getType().name(), e);
        
        }    
        
        ///////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////// ALIASES //////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////
        
        try {
            
            // Checking if we already got an alias matching the current
            // name for the given client. If not we are going to add
            // a new entry in our database to keep tracking it
            Alias alias = this.aliases.getByClientName(client);
            
            if (alias != null) {
                // Upgrade the alias usage
                alias.setNumUsed(alias.getNumUsed() + 1);
            } else {
                // Create a new alias for this client
                alias = new Alias(client, client.getName());
            }
            
            // Saving the Alias object in the storage
            this.aliases.save(alias);
            
            
        } catch (ClassNotFoundException | SQLException e) {
            
            // Logging the Exception. Using a more verbose log message so we can identify
            // in a better way the function where such Exception as been raised and catched
            this.error("Exception generated on " + event.getType().name(), e);
        
        }
        
        ///////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////// IP ALIASES /////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////
        
        try {
            
            // Checking if the connecting client already used
            // the current IP in the past. If not we are going to
            // add a new entry in our database to keep tracking it
            IpAlias ipalias = this.ipaliases.getByClientIp(client);
            
            if (ipalias != null) {
                // Upgrade the ipalias usage
                ipalias.setNumUsed(ipalias.getNumUsed() + 1);
            } else {
                // Create a new alias for this client
                ipalias = new IpAlias(client, client.getIp());
            }
            
            // Saving the IpAlias object in the storage
            this.ipaliases.save(ipalias);
            
        } catch (ClassNotFoundException | SQLException | UnknownHostException e) {
            
            // Logging the Exception. Using a more verbose log message so we can identify
            // in a better way the function where such Exception as been raised and catched
            this.error("Exception generated on " + event.getType().name(), e);
        
        }
        
    }
    
    
    /**
     * Performs operation on <tt>EVT_CLIENT_NAME_CHANGE</tt>
     * 
     * @author Daniele Pantaleone
     * @param  event The generated <tt>Event</tt>
     **/
    @Subscribe
    public void onClientNameChange(ClientNameChangeEvent event) {
        
        // Copying the client reference
        Client client = event.getClient();
        
        try {
            
            // Checking if we already got an alias matching the current
            // name for the given client. If not we are going to add
            // a new entry in our database to keep tracking it
            Alias alias = this.aliases.getByClientName(client);
            
            if (alias != null) {
                // Upgrade the alias usage
                alias.setNumUsed(alias.getNumUsed() + 1);
            } else {
                // Create a new alias for this client
                alias = new Alias(client, client.getName());
            }
            
            // Saving data
            this.aliases.save(alias);
            this.clients.save(client);
            
        } catch (ClassNotFoundException | SQLException e) {
            
            // Logging the Exception. Using a more verbose log message so we can identify
            // in a better way the function where such Exception as been raised and catched
            this.error("Exception generated on " + event.getType().name() , e);
        
        }
        
    }
    
    
    
    /**
     * Performs operation on <tt>EVT_CLIENT_TEAM_CHANGE</tt>
     * 
     * @author Daniele Pantaleone
     * @param  event The generated <tt>Event</tt>
     **/
    @Subscribe
    public void onClientTeamChange(ClientTeamChangeEvent event) {
    	
    	// Copying the client reference
        Client client = event.getClient();
        
        // Checking if the player is locked
        if (client.isVar("locked_team")) {
        	        
        	Team team = (Team) client.getVar("locked_team");
        	
        	// If this client is locked to a team
        	// don't let him move into another one
        	if ((team != client.getTeam())) {
        		this.console.forceteam(client, team);
        		this.console.tell(client, "You are locked to: " + Color.getByTeam(team) + team.name());
        	}
        	
        }
    
    }
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////// COMMANDS SECTION /////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    /**
     * Display the list of online admins
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     **/
    @Usage(message="Display the list of online admins", syntax="!admins")
    public void CmdAdmins(Command command) throws CommandSyntaxException {
        
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() != 0) throw new CommandSyntaxException("Invalid syntax");
        
        // Getting the list of online admins
        List<Client> admins = this.clients.getByMinGroup(this.adminLevel);
        
        if (admins.size() == 0) {
            // Informing the player that there are no admins online
            this.console.sayLoudOrPm(command, this.messages.get("no_admins"));
            return;
        }
        
        // Sorting the collection
        Comparator<Client> comparator = new ClientLevelComparator();
        Collections.sort(admins, comparator);
        
        // Building the message to be displayed
        List<String> collection = new LinkedList<String>();
        
        for (Client admin: admins) {
            // Appending all the admin details to our collection
            // using the following format: nickname [level]
            collection.add(admin.getName() + " [" + Color.GREEN + admin.getGroup().getLevel() + Color.WHITE + "]");
        }
        
        // Printing the admin list in the game chat
        this.console.sayLoudOrPm(command, MessageFormat.format(this.messages.get("admin_list"), Joiner.on(", ").join(collection)));
        
    }
    
    
    /**
     * Ban a player from the server
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     * @throws CommandRuntimeException If there is <tt>Runtime</tt> error while processing the command
     * @throws ClassNotFoundException If it's not possible to load the storage interface library
     * @throws SQLException If the connection with the database has been lost and cannot
     *                      be established anymore, or if the SQL query fails somehow
     **/
    @Usage(message="Ban a player from the server", syntax="!ban <client> [<reason>]")
    public void CmdBan(Command command) throws CommandSyntaxException, CommandRuntimeException, ClassNotFoundException, SQLException {
        
        String reason = null;
        Client client = command.client;
        
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() < 1) throw new CommandSyntaxException("Invalid syntax");
        
        // Retrieving the correct client
        Client sclient = this.clients.getByMagic(command.client, command.getParamString(0));
        if (sclient == null) return;
        
        if (client == sclient) {
            // The user is trying to ban himself
            throw new CommandRuntimeException(this.messages.get("ban_self"));
        }
        
        if ((client.getGroup().getLevel() <= sclient.getGroup().getLevel()) && (client != sclient)) {
            // The user is trying to kick an higher/equal level client
            throw new CommandRuntimeException(MessageFormat.format(this.messages.get("ban_denied"), sclient.getName()));
        }
        
        // Getting the ban reason if any
        reason = command.getParamStringConcat(1);
        
        if (reason == null) {
            
            if (client.getGroup().getLevel() < this.noReasonLevel) {
                // Informing the player that he MUST supply a reason for the ban
                throw new CommandSyntaxException("You must supply a reason");
            }
        
        } else {
            
            // Checking if a predefined keyword has been used as kick reason
            if (this.keywords.containsKey(reason.toLowerCase())) {
                // Replacing the reason string with the associated keyword value
                reason = this.keywords.get(reason.toLowerCase());
            }
                
        }
        
        ////////////////////////
        // PERFORMING THE BAN //
        ////////////////////////
        
        // Check if the Auth System is enabled and correctly configured
        // This will automatically exclude Urban Terror 4.1 console since the 
        // auth boolean flag will remain false upon console initialization
        if ((this.game.isAuthEnabled()) && (this.game.getAuthOwners() != null)) {
            
            // Check if the client is authed
            if (sclient.getAuth() != null) {
                
                int days = 0, hours = 0;
                int minutes = (int) (this.banDuration / 60000);
                while (minutes >= 60) { hours += 1; minutes -= 60; }
                while (hours >= 24) { days += 1; hours -= 24; } 
                
                // Sending the BAN through the auth server. We don't need
                // to kick the player from the server. The Auth System
                // will drop the player automatically after receiving the ban
                this.console.authban(sclient, days, hours, minutes);
                
            } else {
                
                // Client is not authed
                this.console.kick(sclient, reason);
             
            }
          
        } else {
            
            // Auth System not available
            this.console.kick(sclient, reason);
        }
        
        /////////////////////////
        // BAN IN-GAME DISPLAY //
        /////////////////////////        
        
        if (reason == null) {
            
            this.console.say(MessageFormat.format(this.messages.get("tempban"), 
                                                  sclient.getName(), 
                                                  client.getName(), 
                                                  TimeParser.getHumanReadableTime(this.banDuration)));
        }
        else {
            
            this.console.say(MessageFormat.format(this.messages.get("tempban_reason"), 
                                                  sclient.getName(), 
                                                  client.getName(), 
                                                  TimeParser.getHumanReadableTime(this.banDuration), 
                                                  reason));
        }
         
        /////////////////////////
        // STORING THE PENALTY //
        /////////////////////////
        
        this.penalties.insert(new Penalty.Builder(sclient, PenaltyType.BAN)
                                         .admin(client)
                                         .reason(reason)
                                         .timeExpire(new DateTime(this.orion.timezone).plus(this.banDuration))
                                         .build());
        
    }
    
    
    /**
     * Cycle the server current map
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     * @throws CommandRuntimeException If there is <tt>Runtime</tt> error while processing the command
     **/
    @Usage(message="Cycle the server current map", syntax="!cyclemap")
    public void CmdCyclemap(Command command) throws CommandSyntaxException, CommandRuntimeException {
                
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() != 0) throw new CommandSyntaxException("Invalid syntax");
        
        // Getting the nextmap name
        String nextmap = this.console.getNextMap();
        
        if (nextmap == null) {
            
            // We were not able to get the nextmap name
            // for a proper in-game chat display. Cycle anyway...
            this.console.sayLoudOrPm(command, "Cycling current map...");
            
            try { Thread.sleep(1200);
            } catch (InterruptedException e) {
                // Don't do anything since this sleep is not really necessary
                // It's just so players knows which map we are going to play
            }
            
            // Cycle current map
            this.console.cyclemap();
            
            
        }
        
        // Printing the nextmap name in the game chat so players will notice the cycle
        this.console.sayLoudOrPm(command, MessageFormat.format(this.messages.get("map_cycle"), nextmap));
        
        try { Thread.sleep(1200); } 
        catch (InterruptedException e) { }
        
        // Cycle current map
        this.console.cyclemap();

    }
    
    
    /**
     * Disable a plugin
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     * @throws CommandRuntimeException If there is <tt>Runtime</tt> error while processing the command
     **/
    @Usage(message="Disable a plugin", syntax="!disable <plugin>")
    public void CmdDisable(Command command) throws CommandSyntaxException, CommandRuntimeException {
                
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() != 1) throw new CommandSyntaxException("Invalid syntax");
        
        // Retrieving the plugin to be disabled
        String name = command.getParamString(0).toLowerCase();
        Plugin plugin = this.orion.plugins.get(name);
        
        if (name.equals("admin")) {
            // The user is trying to disable the Admin plugin. This one can't be disabled
            // If we allow so, we do not have the possibility to enable it again
            throw new CommandRuntimeException("Can't disable plugin: " + Color.RED + name);
        }
        
        if (plugin == null) {
            // The specified plugin doesn't exists or is not loaded
            throw new CommandRuntimeException("Unable to find plugin: " + Color.RED + name);
        }
        
        if (!plugin.isEnabled()) {
            // The specified plugin is already disabled
            throw new CommandRuntimeException("Plugin already disabled: " + Color.YELLOW + name);
        }
                
        // Disabling the plugin
        plugin.setEnabled(false);
        this.console.sayLoudOrPm(command, "Plugin disabled: " + Color.RED + name);
             
    }
    
    
    /**
     * Enable a plugin
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     * @throws CommandRuntimeException If there is <tt>Runtime</tt> error while processing the command
     **/
    @Usage(message="Enable a plugin", syntax="!enable <plugin>")
    public void CmdEnable(Command command) throws CommandSyntaxException, CommandRuntimeException {
        
        // Copying client reference
        Client client = command.client;
        
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() != 1) throw new CommandSyntaxException("Invalid syntax");
        
        // Retrieving the plugin to be disabled
        String name = command.getParamString(0).toLowerCase();
        Plugin plugin = this.orion.plugins.get(name);
                    
        if (plugin == null) {
            // The specified plugin doesn't exists or is not loaded
            throw new CommandRuntimeException("Unable to find plugin: " + Color.RED + name);
        }
        
        if (plugin.isEnabled()) {
            // The specified plugin is already disabled
            throw new CommandRuntimeException("Plugin already enabled: " + Color.YELLOW + name);
        }
        
        // Enabling the plugin
        plugin.setEnabled(true);
        this.console.tell(client, "Plugin enabled: " + Color.GREEN + name);
                
    }
    
    
    /**
     * Display the slot of a player
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     **/
    @Usage(message="Display the slot of a player", syntax="!find <client>")
    public void CmdFind(Command command) throws CommandSyntaxException {
        
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() != 1) throw new CommandSyntaxException("Invalid syntax");
        
        // Retrieving the correct client
        String search = command.getParamString(0);
        Client sclient = this.clients.getByMagic(command.client, search);
        if (sclient == null) return;
        
        // Printing the slot informations in the game chat
        this.console.sayLoudOrPm(command, "Found player matching " + Color.YELLOW + search + Color.WHITE + ": " + 
                                          sclient.getName() + " [" + Color.GREEN + sclient.getSlot() + Color.WHITE +"]");
        
    }
    
    
    /**
     * Force a player in the specified team
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     * @throws CommandRuntimeException If there is <tt>Runtime</tt> error while processing the command
     **/
    @Usage(message="Force a player in the specified team", syntax="!force <client> <team>")
    public void CmdForce(Command command) throws CommandSyntaxException, CommandRuntimeException {
        
    	Team team = null;
    	Client sclient = null;
    	Client client = command.client;
    	
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() != 2) throw new CommandSyntaxException("Invalid syntax");
        
        // Retrieving the correct client
        sclient = this.clients.getByMagic(command.client, command.getParamString(0));
        if (sclient == null) return;
        
        try {
        	
        	// Retrieving the correct team
        	team = this.parser.getTeamByName(command.getParamString(1));
        	
        } catch (IndexOutOfBoundsException e) {
        	
        	// Invalid team name specified. Display a short list
        	List<Team> collection = this.parser.getAvailableTeams();
        	throw new CommandRuntimeException("Invalid team specified. Try using: " + Color.YELLOW + Joiner.on(Color.WHITE + "|" + Color.YELLOW).join(collection));
        	
        }
        
        // Check if the specified Team is available
        List<Team> collection = this.parser.getAvailableTeams();
        
        if (!collection.contains(team)) {
        	// The specified Team is not available in the current played gametype
        	// Showing a list of available teams so the user can try again
        	throw new CommandRuntimeException("Invalid team specified. Try using: " + Color.YELLOW + Joiner.on(Color.WHITE + "|" + Color.YELLOW).join(collection));
        }
       
        // Checking if the player is locked
        if (sclient.isVar("locked_team")) {
        	
        	Team lockTeam = (Team) sclient.getVar("locked_team");
        	if (lockTeam != team) {
        		
        		// Inform that the player cannot be forced since he's locked to another team
        		this.console.tell(client, String.format("%s currently locked to: %s%s", ((client != sclient) ? sclient.getName() + " is" : "You are"), Color.getByTeam(lockTeam), lockTeam.name()));
        		return;
        	
        	}
        	
        }
        
        // Forcing the player
        this.console.forceteam(sclient, team);
        
        // Informing the player that he has been moved by an admin into another team
        this.console.tell(sclient, "You have been forced to: " + Color.getByTeam(team) + team.name());
        if (sclient != client) this.console.tell(client, sclient.getName() + " has been forced to: " + Color.getByTeam(team) + team.name());
        
    }
    
    
    /**
     * Display a CVAR value
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     * @throws CommandRuntimeException If there is <tt>Runtime</tt> error while processing the command
     **/
    @Usage(message="Display a CVAR value", syntax="!get <cvar>")
    public void CmdGet(Command command) throws CommandSyntaxException, CommandRuntimeException {
        
        // Copying client reference
        Client client = command.client;
        
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() != 1) throw new CommandSyntaxException("Invalid syntax");
        
        // Getting the CVAR value
        String cvar = command.getParamString(0);
        String value = this.console.getCvar(cvar);
        
        if (value == null) {
            // Not able to retrieve the CVAR value
            throw new CommandRuntimeException("Unable to retrieve " + Color.RED + cvar);
        }
        
        // Printing the CVAR value in the game chat
        this.console.tell(client, cvar + " is set to: " + Color.YELLOW + value);

    }
    
    
    /**
     * Display the list of available commands
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     * @throws CommandRuntimeException If there is <tt>Runtime</tt> error while processing the command
     **/
    @Usage(message="Display the list of available commands", syntax="!help [<command>]")
    public void CmdHelp(Command command) throws CommandSyntaxException, CommandRuntimeException {
        
        // Copying client reference
        Client client = command.client;
        
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() > 1) throw new CommandSyntaxException("Invalid syntax");
        
        // Displaying help text for the given command
        if (command.getParamNum() != 0) {
            
            String name = command.getParamString(0).toLowerCase();
            if (!this.regcommands.containsKey(name)) {
                // Informing the client that the command is not mapped
                throw new CommandRuntimeException(MessageFormat.format(this.messages.get("help_not_found"), command.prefix, command.handle));
            }
            
            // Getting the RegisteredCommand object
            RegisteredCommand regcommand = this.regcommands.get(name);
            
            // Checking correct client minLevel
            if (client.getGroup().getLevel() < regcommand.minGroup.getLevel()) {
                // Informing the client that he has not sufficient access for this command
                throw new CommandRuntimeException(MessageFormat.format(this.messages.get("help_no_access"), command.prefix.name, command.handle));
            }
            
            // Displaying the help text
            Usage usage = regcommand.method.getAnnotation(Usage.class);
            this.console.tell(command.client, usage.message());
            this.console.tell(command.client, "Usage: " + Color.YELLOW  + usage.syntax());
            
        } else {
            
            // Displaying a list of all the available commands
            List<String> collection = new LinkedList<String>();
            Map<String, RegisteredCommand> commands = this.regcommands.getMap1();
            
            for (Map.Entry<String, RegisteredCommand> entry : commands.entrySet()) {
                
                // No access for this command. Skip it!
                if (client.getGroup().getLevel() < entry.getValue().minGroup.getLevel())
                    continue;
                
                // Adding the command to the collection
                collection.add(entry.getKey());
                
            } 
            
            Collections.sort(collection);
            
            // Printing the command list in the game chat
            this.console.sayLoudOrPm(command, MessageFormat.format(this.messages.get("help_list"), Color.YELLOW + Joiner.on(Color.WHITE + ", " + Color.YELLOW).join(collection)));
            
        }
        
    }
    
    
    /**
     * Register yourself as Super Admin
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     * @throws CommandRuntimeException If there is <tt>Runtime</tt> error while processing the command
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using the <tt>Client</tt> IP address
     * @throws ClassNotFoundException If it's not possible to load the storage interface library
     * @throws SQLException If the connection with the database has been lost and cannot
     *                      be established anymore, or if the SQL query fails somehow
     **/
    @Usage(message="Register yourself as Super Admin", syntax="!iamgod")
    public void CmdIamgod(Command command) throws CommandSyntaxException, CommandRuntimeException, ClassNotFoundException, UnknownHostException, SQLException {
        
        // Copying client reference
        Client client = command.client;
        
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() != 0) throw new CommandSyntaxException("Invalid syntax");
        
        // Double check the Super Admin list
        List<Client> collection = this.clients.getByGroupFull("superadmin");
        
        if (collection.size() > 0) {
            // Found at least one Super Admin -> this command should not be enabled
            this.log.warn("Command !iamgod found enabled but there are already " + collection.size() + " Super Admin registered");
            this.log.debug("Unregistering !iamgod command");
            this.regcommands.remove("iamgod");
            return;
        }
        
        // Changing the Client group level and removing the command
        client.setGroup(this.groups.getByKeyword("superadmin"));
        this.trace(client.getName() + " [@" + client.getId() +"] has been registered as " + client.getGroup().getName() + " [" + client.getGroup().getLevel() + "]");
        this.debug("Unregistering !iamgod command");
        this.regcommands.remove("iamgod");
        
        // Printing the command result in the game chat
        this.console.tell(client, "You are now a " + Color.GREEN + client.getGroup().getName());
        
        // Saving the changes
        this.clients.save(client);

    }
    
    
    /**
     * Kick a player from the server
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     * @throws CommandRuntimeException If there is <tt>Runtime</tt> error while processing the command
     * @throws ClassNotFoundException If it's not possible to load the storage interface library
     * @throws SQLException If the connection with the database has been lost and cannot
     *                      be established anymore, or if the SQL query fails somehow
     **/
    @Usage(message="Kick a player from the server", syntax="!kick <client> [<reason>]")
    public void CmdKick(Command command) throws CommandSyntaxException, CommandRuntimeException, ClassNotFoundException, SQLException {
        
        String reason = null;
        Client client = command.client;
        
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() < 1) throw new CommandSyntaxException("Invalid syntax");
        
        // Retrieving the correct client
        Client sclient = this.clients.getByMagic(command.client, command.getParamString(0));
        if (sclient == null) return;
        
        if (client == sclient) {
            // The user is trying to kick himself
            throw new CommandRuntimeException(this.messages.get("kick_self"));
        }
        
        if ((client.getGroup().getLevel() <= sclient.getGroup().getLevel()) && (client != sclient)) {
            // The user is trying to kick an higher/equal level client
            throw new CommandRuntimeException(MessageFormat.format(this.messages.get("kick_denied"), sclient.getName()));
        }
        
        // Getting the kick reason if any
        reason = command.getParamStringConcat(1);
        
        if (reason == null) {
            
            if (client.getGroup().getLevel() < this.noReasonLevel) {
                // Informing the player that he MUST supply a reason for the kick
                throw new CommandSyntaxException("You must supply a reason");
            }
            
            // Kicking the player
            this.console.kick(sclient);
            this.console.say(MessageFormat.format(this.messages.get("kick"), sclient.getName(), client.getName()));
            
        } else {
            
            // Checking if a predefined keyword has been used as kick reason
            if (this.keywords.containsKey(reason.toLowerCase())) {
                // Replacing the reason string with the associated keyword value
                reason = this.keywords.get(reason.toLowerCase());
            }
            
            // Kicking the client
            this.console.kick(sclient, reason); 
            this.console.say(MessageFormat.format(this.messages.get("kick_reason"), sclient.getName(), client.getName(), reason));

        }
        
        // Storing the penalty in the storage
        this.penalties.insert(new Penalty.Builder(sclient, PenaltyType.KICK)
                                         .admin(client)
                                         .reason(reason)
                                         .build());
        
    }
    
    
    /**
     * Kill a player
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     * @throws CommandRuntimeException If there is <tt>Runtime</tt> error while processing the command
     **/
    @Usage(message="Kill a player", syntax="!kill [<client>]")
    @Dependency(console=UrT42Console.class)
    public void CmdKill(Command command) throws CommandSyntaxException, CommandRuntimeException {
        
        // Copying client reference
        Client client = command.client;
        Client sclient = null;

        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() > 1) throw new CommandSyntaxException("Invalid syntax");
        
        // Retrieving the correct client
        if (command.getParamNum() != 1) sclient = client;
        else sclient = this.clients.getByMagic(command.client, command.getParamString(0));
        
        if ((client.getGroup().getLevel() <= sclient.getGroup().getLevel()) && (client != sclient)) {
            // The user is trying to kill an higher/equal level client
            throw new CommandRuntimeException(MessageFormat.format(this.messages.get("kill_denied"), sclient.getName()));
        }
        
        // Performing the kill
        this.console.kill(sclient);
        
    }
    
    
    /**
     * Display the level of a player
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     * @throws CommandRuntimeException If there is <tt>Runtime</tt> error while processing the command
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using the <tt>Client</tt> IP address
     * @throws ClassNotFoundException If it's not possible to load the storage interface library
     * @throws SQLException If the connection with the database has been lost and cannot
     *                      be established anymore, or if the SQL query fails somehow
     **/
    @Usage(message="Display the level of a player", syntax="!leveltest [<client>]")
    public void CmdLeveltest(Command command) throws CommandSyntaxException, CommandRuntimeException, UnknownHostException, ClassNotFoundException, SQLException {
        
        // Copying client reference
        Client client = command.client;
        Client sclient = null;
        
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() > 1) throw new CommandSyntaxException("Invalid syntax");
        
        // Retrieving the correct client
        if (command.getParamNum() != 1) sclient = client;
        else sclient = this.clients.getByMagicFull(command.client, command.getParamString(0));
        
        // Exit if we don't have a proper client
        // on which to perform the leveltest
        if (sclient == null) return;
        
        this.console.sayLoudOrPm(command, MessageFormat.format(this.messages.get("level_test"), 
                                                               sclient.getName(), 
                                                               sclient.getId(), 
                                                               sclient.getGroup().getName(), 
                                                               sclient.getGroup().getLevel()));
        
    }
    
    
    /**
     * Display the list of online clients
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     **/
    @Usage(message="Display the list of online client", syntax="!list")
    public void CmdList(Command command) throws CommandSyntaxException {
        
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() != 0) throw new CommandSyntaxException("Invalid syntax");
        
        // Getting the list of online clients
        List<Client> clientlist = this.clients.getList();
        Comparator<Client> comparator = new ClientSlotComparator();
        Collections.sort(clientlist, comparator);
        
        // Building the message to be displayed
        List<String> collection = new LinkedList<String>();
        
        for (Client sclient: clientlist) {
            // Appending all the client details to our collection
            // using the following format: [slot] nickname
            collection.add("[" + Color.YELLOW + sclient.getSlot() + Color.WHITE + "] " + sclient.getName());
        }
        
        // Printing the client list in the game chat
        this.console.sayLoudOrPm(command, MessageFormat.format(this.messages.get("client_list"), Joiner.on(", ").join(collection)));
        
    }
    
    
    /**
     * Lock a player into the specified team
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     * @throws CommandRuntimeException If there is <tt>Runtime</tt> error while processing the command
     **/
    @Usage(message="Lock a player into the specified team", syntax="!lock <client> <team>")
    public void CmdLock(Command command) throws CommandSyntaxException, CommandRuntimeException {
        
    	Team team = null;
    	Client sclient = null;
    	Client client = command.client;
    	
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() != 2) throw new CommandSyntaxException("Invalid syntax");
        
        // Retrieving the correct client
        sclient = this.clients.getByMagic(command.client, command.getParamString(0));
        if (sclient == null) return;
        
        if ((client.getGroup().getLevel() <= sclient.getGroup().getLevel()) && (client != sclient)) {
            // The user is trying to lock an higher/equal level client
            throw new CommandRuntimeException(MessageFormat.format(this.messages.get("lock_denied"), sclient.getName()));
        }
        
        try {
        	
        	// Retrieving the correct team
        	team = this.parser.getTeamByName(command.getParamString(1));
        	
        } catch (IndexOutOfBoundsException e) {
        	
        	// Invalid team name specified. Display a short list
        	List<Team> collection = this.parser.getAvailableTeams();
        	throw new CommandRuntimeException("Invalid team specified. Try using: " + Color.YELLOW + Joiner.on(Color.WHITE + "|" + Color.YELLOW).join(collection));
        	
        }
        
        // Check if the specified Team is available
        List<Team> collection = this.parser.getAvailableTeams();
        
        if (!collection.contains(team)) {
        	// The specified Team is not available in the current played gametype
        	// Showing a list of available teams so the user can try again
        	throw new CommandRuntimeException("Invalid team specified. Try using: " + Color.YELLOW + Joiner.on(Color.WHITE + "|" + Color.YELLOW).join(collection));
        }
        
        
        // Storing the locked team in the client object
        // This will overwrite a previously set value
        sclient.setVar("locked_team", team);
         
        // Forcing the player
        this.console.forceteam(sclient, team);
        
        // Informing the player that he has been moved by an admin into another team
        this.console.tell(sclient, "You have been locked to: " + Color.getByTeam(team) + team.name());
        if (sclient != client) this.console.tell(client, sclient.getName() + " has been locked to: " + Color.getByTeam(team) + team.name());
        
    }
    
    
    /**
     * Lookup a client in the storage
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using the <tt>Client</tt> IP address
     * @throws ClassNotFoundException If it's not possible to load the storage interface library
     * @throws SQLException If the connection with the database has been lost and cannot
     *                      be established anymore, or if the SQL query fails somehow
     **/
    @Usage(message="Lookup a client in the storage", syntax="!lookup <client>")
    public void CmdLookup(Command command) throws CommandSyntaxException, UnknownHostException, ClassNotFoundException, SQLException {
        
        // Copying client reference
        Client client = command.client;
        
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() != 1) throw new CommandSyntaxException("Invalid syntax");
        
        // Retrieving the correct client
        Client sclient = this.clients.getByMagicFull(command.client, command.getParamString(0));
        if (sclient == null) return;
        
        // Printing lookup info in the game chat
        this.console.sayLoudOrPm(command, client.getName() + " [" + Color.YELLOW + "@" + client.getId() + Color.WHITE + "] - Level: " + Color.YELLOW + client.getGroup().getName());
        this.console.sayLoudOrPm(command, "Joined on: " + Color.YELLOW + client.getTimeAdd().toString(this.orion.timeformat, this.orion.locale));
        this.console.sayLoudOrPm(command, "Last seen: " + Color.YELLOW + client.getTimeEdit().toString(this.orion.timeformat, this.orion.locale));
        this.console.sayLoudOrPm(command, "Last known host: " + Color.YELLOW + client.getIp().getHostAddress());
        
    }
    
    
    /**
     * Change the server current map
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     * @throws CommandRuntimeException If there is <tt>Runtime</tt> error while processing the command
     **/
    @Usage(message="Change the server current map", syntax="!map <mapname>")
    public void CmdMap(Command command) throws CommandSyntaxException, CommandRuntimeException {
                
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() != 1) throw new CommandSyntaxException("Invalid syntax");
        
        // Get all the maps matching the given pattern
        String pattern = command.getParamString(0);
        List<String> collection = this.console.getMapSoundingLike(pattern);
        
        if (collection == null) {
            // Inform the player that something went wrong
            throw new CommandRuntimeException("Unable to retrieve map list");
        } else if (collection.isEmpty()) {
            // No map found matching the given pattern
            throw new CommandRuntimeException("No map found matching: " + Color.RED + pattern);
        } else if (collection.size() > 1) {
            // Too many maps found matching the given pattern
            throw new CommandRuntimeException("Do you mean: " + Color.YELLOW + Joiner.on(Color.WHITE + ", " + Color.YELLOW).join(collection) + Color.WHITE + "?");
        }
        
        // Found just our map
        String mapname = collection.get(0);
        
        // Printing the map name in the game chat so players will notice the change
        this.console.sayLoudOrPm(command, MessageFormat.format(this.messages.get("map_change"), mapname));
        
        try { Thread.sleep(1200);
        } catch (InterruptedException e) {
            // Don't do anything since this sleep is not really necessary
            // It's just so players knows which map we are going to play
        }
        
        // Changing the map
        this.console.map(mapname);
        
    }
    
    
    /**
     * List the available maps
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     * @throws CommandRuntimeException If there is <tt>Runtime</tt> error while processing the command
     **/
    @Usage(message="List the available maps", syntax="!maplist")
    public void CmdMaps(Command command) throws CommandSyntaxException, CommandRuntimeException {
                
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() != 0) throw new CommandSyntaxException("Invalid syntax");
        
        // Getting the map list
        if ((this.game.getMapList() == null) || (this.game.getMapList().size() == 0))
            this.game.setMapList(this.console.getMapList());

        if ((this.game.getMapList() == null) || (this.game.getMapList().size() == 0)) {
            // Inform the player that something went wrong
            throw new CommandRuntimeException("Unable to retrieve map list");
        }
        
        // Printing the map list in the game chat
        this.console.sayLoudOrPm(command, MessageFormat.format(this.messages.get("map_list"), Color.YELLOW + Joiner.on(Color.WHITE + ", " + Color.YELLOW).join(this.game.getMapList())));

    }
    
    
    /**
     * Display the nextmap name
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     * @throws CommandRuntimeException If there is <tt>Runtime</tt> error while processing the command
     **/
    @Usage(message="Display the nextmap name", syntax="!nextmap")
    public void CmdNextmap(Command command) throws CommandSyntaxException, CommandRuntimeException {
                
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() != 0) throw new CommandSyntaxException("Invalid syntax");
        
        // Getting the nextmap name
        String nextmap = this.console.getNextMap();
        
        if (nextmap == null) {
            // Inform the player that something went wrong
            throw new CommandRuntimeException("Unable to retrieve nextmap");
        }
        
        // Printing nextmap name in the game chat
        this.console.sayLoudOrPm(command, MessageFormat.format(this.messages.get("nextmap"), nextmap));

    }
    
    
    /**
     * Nuke a player
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     **/
    @Usage(message="Nuke a player", syntax="!nuke <client>")
    public void CmdNuke(Command command) throws CommandSyntaxException {
        
        // Copying client reference
        Client client = command.client;

        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() != 1) throw new CommandSyntaxException("Invalid syntax");
        
        // Retrieving the correct client
        Client sclient = this.clients.getByMagic(command.client, command.getParamString(0));
        if (sclient == null) return;
        
        if ((client.getGroup().getLevel() <= sclient.getGroup().getLevel()) && (client != sclient)) {
            // The user is trying to nuke an higher/equal level client
            throw new CommandRuntimeException(MessageFormat.format(this.messages.get("nuke_denied"), sclient.getName()));
        }
        
        // Performing the nuke
        this.console.nuke(sclient);

    }
    
    
    /**
     * Display bot info
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     * @throws CommandRuntimeException If there is <tt>Runtime</tt> error while processing the command
     **/
    @Usage(message="Display bot info", syntax="!orion")
    public void CmdOrion(Command command) throws CommandSyntaxException {
        
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() != 0) throw new CommandSyntaxException("Invalid syntax");
        
        // Printing bot info in the game chat. Format: BOTNAME vX.x [CODENAME] - WEBSITE
        this.console.sayLoudOrPm(command, Orion.BOTNAME + " " + Color.YELLOW + Orion.VERSION + Color.WHITE + " [" + Color.YELLOW + 
                                          Orion.CODENAME + Color.WHITE + "] - " + Color.BLACK + Orion.WEBSITE);
    
    }
    
    
    /**
     * Ban a player from the server permanently
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     * @throws CommandRuntimeException If there is <tt>Runtime</tt> error while processing the command
     * @throws ClassNotFoundException If it's not possible to load the storage interface library
     * @throws SQLException If the connection with the database has been lost and cannot
     *                      be established anymore, or if the SQL query fails somehow
     **/
    @Usage(message="Ban a player from the server permanently", syntax="!ban <client> [<reason>]")
    public void CmdPermban(Command command) throws CommandSyntaxException, CommandRuntimeException, ClassNotFoundException, SQLException {
        
        String reason = null;
        Client client = command.client;
        
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() < 1) throw new CommandSyntaxException("Invalid syntax");
        
        // Retrieving the correct client
        Client sclient = this.clients.getByMagic(command.client, command.getParamString(0));
        if (sclient == null) return;
        
        if (client == sclient) {
            // The user is trying to ban himself
            throw new CommandRuntimeException(this.messages.get("ban_self"));
        }
        
        if ((client.getGroup().getLevel() <= sclient.getGroup().getLevel()) && (client != sclient)) {
            // The user is trying to kick an higher/equal level client
            throw new CommandRuntimeException(MessageFormat.format(this.messages.get("ban_denied"), sclient.getName()));
        }
        
        // Getting the kick reason if any
        reason = command.getParamStringConcat(1);
        
        if (reason == null) {
            
            if (client.getGroup().getLevel() < this.noReasonLevel) {
                // Informing the player that he MUST supply a reason for the ban
                throw new CommandSyntaxException("You must supply a reason");
            }
        
        } else {
            
            // Checking if a predefined keyword has been used as kick reason
            if (this.keywords.containsKey(reason.toLowerCase())) {
                // Replacing the reason string with the associated keyword value
                reason = this.keywords.get(reason.toLowerCase());
            }
                
        }
        
        ////////////////////////
        // PERFORMING THE BAN //
        ////////////////////////
        
        // Add the IP to banlist
        this.console.ban(sclient);
        
        // Check if the Auth System is enabled and correctly configured
        // This will automatically exclude Urban Terror 4.1 console since the 
        // auth boolean flag will remain false upon console initialization
        if ((this.game.isAuthEnabled()) && (this.game.getAuthOwners() != null)) {
            
            // Check if the client is authed
            if (sclient.getAuth() != null) {
                
                // Sending the BAN through the auth server. We don't need
                // to kick the player from the server. The Auth System
                // will drop the player automatically after receiving the ban
                this.console.authban(sclient, 0, 0, 0);
                
            } else {
                
                // Client is not authed
                this.console.kick(sclient, reason);
             
            }
          
        } else {
            
            // Auth System not available
            this.console.kick(sclient, reason);
        }
        
        /////////////////////////
        // BAN IN-GAME DISPLAY //
        /////////////////////////        
        
        if (reason == null) {
            
            this.console.say(MessageFormat.format(this.messages.get("permban"), 
                                                  sclient.getName(), 
                                                  client.getName()));
            
        }
        else {
            
            this.console.say(MessageFormat.format(this.messages.get("permban_reason"), 
                                                  sclient.getName(), 
                                                  client.getName(), 
                                                  reason));
        }    
        
        /////////////////////////
        // STORING THE PENALTY //
        /////////////////////////
        
        this.penalties.insert(new Penalty.Builder(sclient, PenaltyType.BAN)
                                         .admin(client)
                                         .reason(reason)
                                         .build());
        
    }
    
    
    /**
     * Display the list of loaded plugins
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     * @throws CommandRuntimeException If there is <tt>Runtime</tt> error while processing the command
     **/
    @Usage(message="Display the list of loaded plugins", syntax="!plugins")
    public void CmdPlugins(Command command) throws CommandSyntaxException, CommandRuntimeException {
        
        // Copying client reference
        Client client = command.client;
        
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() != 0) throw new CommandSyntaxException("Invalid syntax");
        
        List<String> collection = new LinkedList<String>();
        
        for (Map.Entry<String, Plugin> entry : this.plugins.entrySet()) {
        	// Adding to in the collection the list of all the loaded plugins
        	// We'll denote with GREEN an enabled plugin, and with RED a disabled one
        	collection.add((entry.getValue().isEnabled() ? Color.GREEN : Color.RED) + entry.getKey());
        }
        
        // Printing the plugin list in the game chat
        this.console.tell(client, MessageFormat.format(this.messages.get("plugin_list"), Joiner.on(Color.WHITE + ", ").join(collection)));
        
    }
    
    
    /**
     * Change a user group
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     * @throws CommandRuntimeException If there is <tt>Runtime</tt> error while processing the command
     * @throws UnknownHostException If we can't generate an <tt>InetAddress</tt> object using the <tt>Client</tt> IP address
     * @throws ClassNotFoundException If it's not possible to load the storage interface library
     * @throws SQLException If the connection with the database has been lost and cannot
     *                      be established anymore, or if the SQL query fails somehow
     **/
    @Usage(message="Change a user group", syntax="!putgroup <client> <group>")
    public void CmdPutgroup(Command command) throws CommandSyntaxException, CommandRuntimeException, UnknownHostException, ClassNotFoundException, SQLException {
        
        // Copying client reference
        Client client = command.client;
        
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() != 2) throw new CommandRuntimeException("Invalid command syntax");
        
        // Retrieving the correct client
        Client sclient = this.clients.getByMagicFull(command.client, command.getParamString(0));
        if (sclient == null) return;
        
        
        // Checking if the target is a higher level client
        if ((client.getGroup().getLevel() <= sclient.getGroup().getLevel()) && (client != sclient)) {
            // The user is trying to change the group of a higher/equal level client
            throw new CommandRuntimeException(MessageFormat.format(this.messages.get("putgroup_denied"), sclient.getName()));
        }

        
        // Getting the group where to move the client
        Group newgroup = this.groups.getByKeyword(command.getParamString(1));
        Group oldgroup = null;
        
        if (newgroup == null) {
            // Not able to find a group. Must be a wrong keyword specified by the client
            throw new CommandRuntimeException("Invalid group specified: " + Color.RED + command.getParamString(1));
        }
        
        if ((newgroup.getLevel() >= client.getGroup().getLevel()) && (client.getGroup().getLevel() < 100)) {
            // The user is trying to change a client level, to a level beyond his reach
            throw new CommandRuntimeException("Group " + Color.RED + newgroup.getName() + Color.WHITE + " is beyond your reach");
        }
        
        if (sclient.inGroup(newgroup)) {
            // Target client is already in the specified group
            throw new CommandRuntimeException(MessageFormat.format(this.messages.get("putgroup_already"), sclient.getName(), newgroup.getName()));
        }
        
        // Changing the group
        oldgroup = sclient.getGroup();
        sclient.setGroup(newgroup);
        this.log.trace(sclient.getName() + "[@" + sclient.getId() + "] group has been changed by " + client.getName() + "[@" + client.getId() + "]: " + 
                       oldgroup.getName() + " [" + oldgroup.getLevel() +"] -> " + sclient.getGroup().getName() + " [" + sclient.getGroup().getLevel() + "]");
        
        // Informing both players on what happened
        this.console.tell(client, sclient.getName() + " has been added in group: " + Color.GREEN + sclient.getGroup().getName());
        this.console.tell(sclient, "You have been added in group: " + Color.GREEN + sclient.getGroup().getName());
        
        // Saving the changes 
        this.clients.save(sclient);
         
    }
    
    
    /**
     * Register yourself
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     * @throws CommandRuntimeException If there is <tt>Runtime</tt> error while processing the command
     * @throws ClassNotFoundException If it's not possible to load the storage interface library
     * @throws SQLException If the connection with the database has been lost and cannot
     *                      be established anymore, or if the SQL query fails somehow
     **/
    @Usage(message="Register yourself", syntax="!register")
    public void CmdRegister(Command command) throws CommandSyntaxException, CommandRuntimeException, ClassNotFoundException, SQLException {
        
        // Copying client reference
        Client client = command.client;
        
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() != 0) throw new CommandSyntaxException("Invalid syntax");

        // Getting the guest group so we can check if 
        // the client is already registered in the database
        Group guest = this.groups.getByKeyword("guest");
        
        if (client.getGroup().getLevel() >= guest.getLevel()) {
            // The user is already registered
            throw new CommandRuntimeException("You are already registered");
        }
        
        // Registering the user
        client.setGroup(this.groups.getByKeyword("user"));
        this.console.tell(client, MessageFormat.format(this.messages.get("client_registered"), client.getGroup().getName()));
        
        // Saving the new client group
        // permanently in the storage layer
        this.clients.save(client);
        
    }
    
    
    /**
     * Run a command as another <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     * @throws CommandRuntimeException If there is <tt>Runtime</tt> error while processing the command
     **/
    @Usage(message="Run a command as another client", syntax="!runas <client> <command>")
    public void CmdRunas(Command command) throws CommandSyntaxException, CommandRuntimeException {
        
        // Copying client reference
        String runcommand = null;
        Client client = command.client;
        
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() < 2) throw new CommandSyntaxException("Invalid syntax");
        
        // Retrieving the correct client
        Client sclient = this.clients.getByMagic(command.client, command.getParamString(0));
        if (sclient == null) return;
        
        if (client == sclient) {
            // Running the command as himself....meh!
            throw new CommandRuntimeException(this.messages.get("run_self"));
        }
        
        if ((client.getGroup().getLevel() < sclient.getGroup().getLevel()) && (client != sclient)) {
            // The user is trying to run a command as an higher level player
            throw new CommandRuntimeException(MessageFormat.format(this.messages.get("runas_denied"), sclient.getName()));
        }
        
        // Getting the command to be run as the target client
        runcommand = command.getParamStringConcat(1);
        
        if ((!runcommand.startsWith("!")) && (!runcommand.startsWith("@")) && (!runcommand.startsWith("&"))) {
            // Not a valid command to be run
            throw new CommandRuntimeException("You specified an invalid command syntax");
        }
        
        // Building the Command object to be stored in the queue
        String data[] = runcommand.substring(1).split(" ", 2);  
        Command newcommand = new Command(client, 
                                         Prefix.getByChar(runcommand.charAt(0)), 
                                         data[0].toLowerCase(), 
                                         data.length > 1 ? data[1].toLowerCase() : null, 
                                         true);
        
        // Checking if the issued command actually exists
        if (!this.regcommands.containsKey(newcommand.handle)) {
            // The command to be issued as the target user is not registered
            // Inform the client so he can eventually try again
            new CommandRuntimeException("Unable to find command: " + Color.YELLOW + command.prefix.name + Color.RED + newcommand.handle);
        }
        
        // Pushing the command to the command
        this.orion.commandqueue.add(newcommand);
        
    }
    
    
    /**
     * Print a message in the game chat
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     **/
    @Usage(message="Print a message in the game chat", syntax="!say <message>")
    public void CmdSay(Command command) throws CommandSyntaxException {
        
        // Copying client reference
        Client client = command.client;
        
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() < 1) throw new CommandSyntaxException("Invalid syntax");
        
        // Getting the message to be displayed
        String message = command.getParamStringConcat(0);
        
        // Printing the message in the game chat
        this.console.say(MessageFormat.format(this.messages.get("say"), client.getName(), message));
        
    }
    
    
    /**
     * Set a CVAR value
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     **/
    @Usage(message="Set a CVAR value", syntax="!set <cvar> <value>")
    public void CmdSet(Command command) throws CommandSyntaxException {
        
        // Copying client reference
        Client client = command.client;
        
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() < 2) throw new CommandSyntaxException("Invalid syntax");
        
        // Getting command parameters
        String cvar = command.getParamString(0);
        String value = command.getParamStringConcat(1);
        
        // Setting the CVAR value
        this.console.setCvar(cvar, value);
        this.console.tell(client, cvar + " changed to: " + Color.YELLOW + value);

    }
    
    
    /**
     * Change the server next map
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     * @throws CommandRuntimeException If there is <tt>Runtime</tt> error while processing the command
     **/
    @Usage(message="Change the server current map", syntax="!setnextmap <mapname>")
    public void CmdSetnextmap(Command command) throws CommandSyntaxException, CommandRuntimeException {
                
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() != 1) throw new CommandSyntaxException("Invalid syntax");
        
        // Get all the maps matching the given pattern
        String pattern = command.getParamString(0);
        List<String> collection = this.console.getMapSoundingLike(pattern);
        
        if (collection == null) {
            // Inform the player that something went wrong
            throw new CommandRuntimeException("Unable to retrieve map list");
        } else if (collection.isEmpty()) {
            // No map found matching the given pattern
            throw new CommandRuntimeException("No map found matching: " + Color.RED + pattern);
        } else if (collection.size() > 1) {
            // Too many maps found matching the given pattern
            throw new CommandRuntimeException("Do you mean: " + Color.YELLOW + Joiner.on(Color.WHITE + ", " + Color.YELLOW).join(collection) + Color.WHITE + "?");
        }
        
        // Found just our map
        String mapname = collection.get(0);
        
        // Setting the nextmap CVAR
        this.console.setCvar("g_nextmap", mapname);
        
        // Printing the map name in the game chat so players will notice the change
        this.console.sayLoudOrPm(command, MessageFormat.format(this.messages.get("nextmap"), mapname));
        
    }
    
    
    /**
     * Slap a player
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     * @throws CommandRuntimeException If there is <tt>Runtime</tt> error while processing the command
     **/
    @Usage(message="Slap a player", syntax="!slap <client> [<amount>]")
    public void CmdSlap(Command command) throws CommandSyntaxException, CommandRuntimeException {
        
        // Copying client reference
        Client client = command.client;
            
        int num = command.getParamNum();
        if (num < 1) throw new CommandSyntaxException("Invalid syntax");
        if (num > 2) throw new CommandSyntaxException("Too many parameters specified");
        
        // Retrieving the correct client
        Client sclient = this.clients.getByMagic(command.client, command.getParamString(0));
        if (sclient == null) return;
        
        if ((client.getGroup().getLevel() <= sclient.getGroup().getLevel()) && (client != sclient)) {
            // The user is trying to slap an higher/equal level client
            throw new CommandRuntimeException(MessageFormat.format(this.messages.get("slap_denied"), sclient.getName()));
        }
        
        if (num == 2) {
            
            try {
                
                int count = command.getParamInt(1);
                
                // Looping ensuring not to overcome 25 slaps
                // The cycle is slowed down by the RCON delay
                // so there is no need to add a sleep here
                for (int i = 0; i < count && count < 25; i++) {
                    // Sending the command.
                    this.console.slap(sclient);
                } 
                
            } catch (NumberFormatException e) {
                // Wrong user input. Rethrow our custom exception with help info so the client will notice
                throw new CommandSyntaxException("Invalid " + Color.YELLOW + "<amount>" + Color.WHITE + " specified");
            }
            
            return;
            
        }
        
        // Performing single slap
        this.console.slap(sclient);
        
    }
  
    
    /**
     * Shutdown the bot
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     **/
    @Usage(message="Shutdown the bot", syntax="!shutdown")
    public void CmdShutdown(Command command) throws CommandSyntaxException {
        
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() != 0) throw new CommandSyntaxException("Invalid syntax");
        
        this.orion.reader.interrupt();
        this.orion.commandproc.interrupt();
        this.console.sayLoudOrPm(command, "Shutting down...");
        
    }
    
    
    /**
     * Display the bot status
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     **/
    @Usage(message="Display the bot status", syntax="!status")
    public void CmdStatus(Command command) throws CommandSyntaxException {
        
        // Copying client reference
        Client client = command.client;
        
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() != 0) throw new CommandSyntaxException("Invalid syntax");

        // Print the bot status in the game chat
        this.console.tell(client, "Uptime [" + Color.RED + TimeParser.getHumanReadableTime(this.orion.uptime()) + Color.WHITE + "] - " + 
                                  "Storage [" + (this.orion.storage.isConnection() ? Color.GREEN + "connected" : Color.RED + "disconnected") + Color.WHITE + "]");
  
    }
    
    
    /**
     * Display the current datetime
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     **/
    @Usage(message="Display the current datetime", syntax="!time")
    public void CmdTime(Command command) throws CommandSyntaxException {
        
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() != 0) throw new CommandSyntaxException("Invalid syntax");
        
        // Formatting the datetime string
        DateTime datetime = new DateTime(this.orion.timezone);
        
        // Printing datetime informations in the game chat
        this.console.sayLoudOrPm(command, MessageFormat.format(this.messages.get("time"), datetime.toString(this.orion.timeformat, this.orion.locale)));
        
    }
    
    
    /**
     * Remove a team lock from a player
     * 
     * @author Daniele Pantaleone
     * @param  command The <tt>Command</tt> object
     * @throws CommandSyntaxException Used to signal a non-recoverable (fatal) syntax exception
     * @throws CommandRuntimeException If there is <tt>Runtime</tt> error while processing the command
     **/
    @Usage(message="Remove a team lock from a player", syntax="!unlock <client>")
    public void CmdUnlock(Command command) throws CommandSyntaxException, CommandRuntimeException {
        
    	Client sclient = null;
    	Client client = command.client;
    	
        // Checking if we got the correct number of parameters for the command execution
        if (command.getParamNum() != 1) throw new CommandSyntaxException("Invalid syntax");
        
        // Retrieving the correct client
        sclient = this.clients.getByMagic(command.client, command.getParamString(0));
        if (sclient == null) return;
        
        if (!sclient.isVar("locked_team")) {
        	// There is no team lock on the specified client
        	this.console.tell(client, "There is no team lock on " + Color.YELLOW + sclient.getName());
        	return;
        }
        
        // Removing the team lock
        sclient.delVar("locked_team");

        // Informing the player that he has been unlocked
        this.console.tell(sclient, "Your have been unlocked");
        if (sclient != client) this.console.tell(client, sclient.getName() + " has been unlocked");
        
    }
    
    
}
