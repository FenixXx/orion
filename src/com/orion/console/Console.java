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
 * @version     1.4
 * @copyright   Daniele Pantaleone, 10 February, 2013
 * @package     com.orion.console
 **/

package com.orion.console;

import java.util.List;

import net.goreclan.rcon.RconException;

import com.orion.command.Command;
import com.orion.domain.Client;
import com.orion.urt.Cvar;
import com.orion.urt.Team;

public interface Console {
    
    
    /**
     * Ban a <tt>Client</tt> from the server permanently
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who is going to be banned
     * @throws RconException If the RCON command fails in being executed
     * @throws NullPointerException If the given <tt>Client</tt> is <tt>null</tt>
     **/
    public abstract void ban(Client client) throws RconException; 
    
    
    /**
     * Cycle the current map on the server
     * 
     * @author Daniele Pantaleone
     * @throws RconException If the RCON commands fails in being executed
     **/
    public abstract void cyclemap() throws RconException;
    
    
    /**
     * Force a <tt>Client</tt> in the specified team
     *
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who is going to be moved
     * @param  team The <tt>Team</tt> where to force the player in
     * @throws RconException If the <tt>Client</tt> fails in being moved
     * @throws NullPointerException If the given <tt>Client</tt> is <tt>null</tt>
     **/
    public abstract void forceteam(Client client, Team team) throws RconException;
        
    
    /**
     * Retrieve a CVAR from the server
     * 
     * @author Daniele Pantaleone
     * @param  name The CVAR name
     * @throws RconException If the CVAR could not be retrieved form the server
     * @return The <tt>Cvar</tt> object associated to the given CVAR name or <tt>null</tt> 
     *         if such CVAR is not set on the server
     **/
    public abstract Cvar getCvar(String name) throws RconException;
    
        
    /**
     * Return the current map name
     * 
     * @author Daniele Pantaleone
     * @throws RconException If the current map name couldn't be retrieved
     * @return The current map name 
     **/
    public abstract String getMap() throws RconException;
    
    
    /**
     * Return a <tt>List</tt> of available maps
     * 
     * @author Daniele Pantaleone
     * @throws RconException If the map list couldn't be retrieved
     * @return A <tt>List</tt> of all the maps available on the server
     **/
    public abstract List<String> getMapList() throws RconException;
    
    
    /**
     * Return a <tt>List</tt> of maps matching the given search key
     * 
     * @author Daniele Pantaleone
     * @param  search The name of the map to search (or a part of it)
     * @throws RconException If the list of available maps couldn't be computed
     * @return A <tt>List</tt> of maps matching the given search key
     **/
    public abstract List<String> getMapSoundingLike(String search) throws RconException;
    
    
    /**
     * Return the name of the nextmap set on the server
     * 
     * @author Daniele Pantaleone
     * @throws RconException If an RCON command fails in being executed
     **/
    public abstract String getNextMap() throws RconException;


    /**
     * Kick the specified <tt>Client</tt> from the server
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who is going to be kicked from the server
     * @throws RconException If the RCON command fails in being executed
     **/
    public abstract void kick(Client client) throws RconException;
    
    
    /**
     * Kick the specified <tt>Client</tt> from the server by specifying a reason
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who is going to be kicked from the server
     * @param  reason The reason why the <tt>Client</tt> is going to be kicked
     * @throws RconException If the RCON command fails in being executed
     **/
    public abstract void kick(Client client, String reason) throws RconException;
   
  
    /**
     * Instantly kill a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who is going to be killed
     * @throws RconException If the RCON command fails in being executed
     **/
    public abstract void kill(Client client) throws RconException;
    
    
    /**
     * Spawn the server onto a new level
     * 
     * @author Daniele Pantaleone
     * @param  mapname The name of the level to load
     * @throws RconException If the RCON command fails in being executed
     **/
    public abstract void map(String mapname) throws RconException;
    
    
    /**
     * Mute a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone 
     * @param  client The <tt>Client</tt> who is going to be muted
     * @throws RconException If the <tt>Client</tt> couldn't be muted
     **/
    public abstract void mute(Client client) throws RconException;
  
    
    /**
     * Mute a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone 
     * @param  client The <tt>Client</tt> who is going to be muted
     * @param  seconds The amount of seconds the <tt>Client</tt> will be muted
     * @throws RconException If the <tt>Client</tt> couldn't be muted
     **/
    public abstract void mute(Client client, int seconds) throws RconException;
    
    
    /**
     * Nuke a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone 
     * @param  client The <tt>Client</tt> who is going to be nuked
     * @throws RconException If the <tt>Client</tt> couldn't be nuked
     **/
    public abstract void nuke(Client client) throws RconException;
    
    
    /**
     * Print a message in the game chat
     * 
     * @author Daniele Pantaleone
     * @param  message The message to be printed
     * @throws RconException If the RCON command fails in being executed
     **/
    public abstract void say(String message) throws RconException;
    
    
    /**
     * Write a bold message in the middle of the screen
     * 
     * @author Daniele Pantaleone
     * @param  message The message to be printed
     * @throws RconException If the RCON command fails in being executed
     **/
    public abstract void sayBig(String message) throws RconException;
    
    
    /**
     * Send a private message to a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who will receive the message
     * @param  message The message to be sent
     * @throws RconException If the RCON command fails in being executed
     **/
    public abstract void sayPrivate(Client client, String message) throws RconException;
    
    
    /**
     * Print an in-game message with visibility regulated by the command object
     * 
     * @author Daniele Pantaleone
     * @param  command The command issued
     * @param  message The message to be printed
     * @throws RconException If the RCON command fails in being executed
     **/
    public abstract void sayLoudOrPm(Command command, String message) throws RconException;
      
    
    /**
     * Set a CVAR value
     * 
     * @author Daniele Pantaleone
     * @param  name The name of the CVAR
     * @param  value The value to assign to the CVAR
     * @throws RconException If the CVAR could not be set
     **/
    public abstract void setCvar(String name, Object value) throws RconException;
    
    
    /**
     * Slap a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone 
     * @param  client The <tt>Client</tt> who is going to be slapped
     * @throws RconException If the <tt>Client</tt> couldn't be slapped
     **/
    public abstract void slap(Client client) throws RconException;
    
    
    /**
     * Unban a <tt>Client</tt> from the server
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> whose IP address we want to unban
     * @throws RconException If the <tt>Client</tt> could not be unbanned
     **/
    public abstract void unban(Client client) throws RconException;
    
    
    /**
     * Write an RCON command in the remote
     * console without returning the server response
     * 
     * @author Daniele Pantaleone
     * @param  command The command to execute
     * @throws RconException If the RCON command fails in being executed
     **/
    public abstract void write(String command) throws RconException;
    
    
    /**
     * Write an RCON command in the remote console. Will return
     * the server response if specified in the command execution,
     * otherwise it will return <tt>null</tt>
     * 
     * @author Daniele Pantaleone
     * @param  command The command to execute
     * @throws RconException If the RCON command fails in being executed
     * @return The server response to the RCON command if specified 
     *         in the command execution, otherwise <tt>null</tt>
     **/
    public abstract String write(String command, boolean read) throws RconException;
     
}
