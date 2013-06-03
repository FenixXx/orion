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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Orion. If not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 * 
 * @author      Daniele Pantaleone
 * @version     1.1
 * @copyright   Daniele Pantaleone, 10 February, 2013
 * @package     com.orion.console
 **/

package com.orion.console;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.orion.bot.Orion;
import com.orion.domain.Client;
import com.orion.exception.RconException;

public class UrT42Console extends UrT41Console implements Console {
    
    /**
     * Object constructor.
     * 
     * @author Daniele Pantaleone 
     * @param  address The remote server address
     * @param  port The virtual port on which the server is accepting connections
     * @param  password The server RCON password
     * @param  orion Orion object reference
     * @throws UnknownHostException If the IP address of a host could not be determined
     * @throws RconException If the RCON utility object fails in being initialized
     **/
    public UrT42Console(String address, int port, String password, Orion orion) throws UnknownHostException, RconException {
        
        super(address, port, password, orion);
    
        // Retrieve Urban Terror 4.2 specific CVARs
        this.game.setAuthEnable(this.getCvar("auth_enable", boolean.class));
        this.game.setAuthOwners(this.getCvar("auth_owners", int.class));
        
    }
    
    
    /**
     * Ban a client from the server using the FS Auth System
     * 
     * @author Daniele Pantaleone
     * @param  client The client to be banned
     * @param  days The number of days of the ban
     * @param  hours The number of hours of the ban
     * @param  mins The number of minutes of the ban
     * @throws UnsupportedOperationException If the RCON command is not supported by the console implementation
     **/
    @Override
    public void authban(Client client, int days, int hours, int mins) throws UnsupportedOperationException {
        
        if (!this.game.isAuthEnabled()) {
            throw new UnsupportedOperationException("Unable to execute auth-ban RCON command. Auth system is disabled");
        }
        
        if (this.game.getAuthOwners() == null) {
            throw new UnsupportedOperationException("Unable to execute auth-ban RCON command. Auth owners has not been set");
        }
        
        this.rcon.sendNoRead("auth-ban " + client.getSlot() + " " + days + " " + hours + " " + mins);
        
    }
    
    
    /**
     * Ban a client from the server using the FS Auth System
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the client to be banned
     * @param  days The number of days of the ban
     * @param  hours The number of hours of the ban
     * @param  mins The number of minutes of the ban
     * @throws UnsupportedOperationException If the RCON command is not supported by the console implementation
     **/
    @Override
    public void authban(int slot, int days, int hours, int mins) throws UnsupportedOperationException {
        
        if (!this.game.isAuthEnabled()) {
            throw new UnsupportedOperationException("Unable to execute auth-ban RCON command. Auth system is disabled");
        }
        
        if (this.game.getAuthOwners() == null) {
            throw new UnsupportedOperationException("Unable to execute auth-ban RCON command. Auth owners has not been set");
        }
        
        this.rcon.sendNoRead("auth-ban " + slot + " " + days + " " + hours + " " + mins);
    
    }
    
    
    /**
     * Fetch FS Auth System informations for the specified client
     * 
     * @author Daniele Pantaleone
     * @param  client The client whose informations needs to be retrieved
     * @throws UnsupportedOperationException If the RCON command is not supported by the console implementation
     * @return A Map<String, String> containing the auth-whois command result
     **/
    @Override
    public Map<String, String> authwhois(Client client) throws UnsupportedOperationException {
        
        if (!this.game.isAuthEnabled()) {
            throw new UnsupportedOperationException("Unable to execute auth-whois RCON command. Auth system is disabled");
        }
        
        Map<String, String> data = new HashMap<String, String>();
        String result = this.rcon.sendRead("auth-whois " + client.getSlot());
        
        if (result == null) {
            this.log.debug("Unable to parse auth-whois command response for client " + client.getSlot() + ": RCON UDP socket returned NULL");
            return null;
        }
        
        // Remove color codes from the obtained string
        result = result.replaceAll("\\^[0-9]{1}", "");
        
        // Collecting FS Auth System informations
        Pattern pattern = Pattern.compile("^auth:\\s*id:\\s*(?<slot>\\d+)\\s*-\\s*name:\\s*(?<name>\\w+)\\s*-\\s*login:\\s*(?<login>\\w*)\\s*-\\s*notoriety:\\s*(?<notoriety>.*)\\s*-\\s*level:\\s*(?<level>\\d+)\\s*-\\s*(?<rank>.*)$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(result);
        
        if (!matcher.matches()) {
            this.log.debug("Unable to parse auth-whois command response for client " + client.getSlot() + ": declared pattern doesn't match RCON response");
            return null;
        }
        
        data.put("slot",        matcher.group("slot"));
        data.put("name",        matcher.group("name"));
        data.put("login",       matcher.group("login").isEmpty() ? null : matcher.group("login"));
        data.put("notoriety",   matcher.group("login").isEmpty() ? null : matcher.group("notoriety"));
        data.put("level",       matcher.group("login").isEmpty() ? null : matcher.group("level"));
        data.put("rank",        matcher.group("login").isEmpty() ? null : matcher.group("rank"));
        
        return data;
        
    }
    
    
    /**
     * Fetch FS Auth System informations for the specified client
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the client whose informations needs to be retrieved
     * @throws UnsupportedOperationException If the RCON command is not supported by the console implementation
     * @return A Map<String, String> containing the auth-whois command result
     **/
    @Override
    public Map<String, String> authwhois(int slot) throws UnsupportedOperationException {
        
        if (!this.game.isAuthEnabled()) {
            throw new UnsupportedOperationException("Unable to execute auth-whois RCON command. Auth system is disabled");
        }
        
        Map<String, String> data = new HashMap<String, String>();
        String result = this.rcon.sendRead("auth-whois " + slot);
        
        if (result == null) {
            this.log.debug("Unable to parse auth-whois command response for client " + slot + ": RCON UDP socket returned NULL");
            return null;
        }
        
        // Remove color codes from the obtained string
        result = result.replaceAll("\\^[0-9]{1}", "");
        
        // Collecting FS Auth System informations
        Pattern pattern = Pattern.compile("^auth:\\s*id:\\s*(?<slot>\\d+)\\s*-\\s*name:\\s*(?<name>\\w+)\\s*-\\s*login:\\s*(?<login>\\w*)\\s*-\\s*notoriety:\\s*(?<notoriety>.*)\\s*-\\s*level:\\s*(?<level>\\d+)\\s*-\\s*(?<rank>.*)$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(result);
        
        if (!matcher.matches()) {
            this.log.debug("Unable to parse auth-whois command response for client " + slot + ": declared pattern doesn't match RCON response");
            return null;
        }
        
        data.put("slot",        matcher.group("slot"));
        data.put("name",        matcher.group("name"));
        data.put("login",       matcher.group("login").isEmpty() ? null : matcher.group("login"));
        data.put("notoriety",   matcher.group("login").isEmpty() ? null : matcher.group("notoriety"));
        data.put("level",       matcher.group("login").isEmpty() ? null : matcher.group("level"));
        data.put("rank",        matcher.group("login").isEmpty() ? null : matcher.group("rank"));
        
        return data;
        
    }
    
    
    /**
     * Kick the specified client from the server by specifying a reason
     * 
     * @author Daniele Pantaleone
     * @param  client The client who is going to be kicked from the server
     * @param  reason The reason why the client is going to be kicked
     **/
    @Override
    public void kick(Client client, String reason) {
        if (reason == null) this.kick(client);
        else this.rcon.sendNoRead("kick " + client.getSlot() + " " + reason);
    }
    
    
    /**
     * Kick the specified client from the server by specifying a reason
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the player who is going to be kicked from the server
     * @param  reason The reason why the player with the specified slot is going to be kicked
     **/
    @Override
    public void kick(int slot, String reason) throws UnsupportedOperationException {
        if (reason == null) this.kick(slot);
        else this.rcon.sendNoRead("kick " + slot + " " + reason);
    }
    
    
    /**
     * Instantly kill a player
     * 
     * @author Daniele Pantaleone
     * @param  client The client who is going to be killed
     * @throws UnsupportedOperationException If the RCON command is not supported by the console implementation
     **/
    @Override
    public void kill(Client client) throws UnsupportedOperationException {
        this.rcon.sendNoRead("smite " + client.getSlot());
    }
    
    
    /**
     * Instantly kill a player
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the player who is going to be killed
     * @throws UnsupportedOperationException If the RCON command is not supported by the console implementation
     **/
    @Override
    public void kill(int slot) throws UnsupportedOperationException {
        this.rcon.sendNoRead("smite " + slot);
    }
     
}