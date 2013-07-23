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
 * @copyright   Daniele Pantaleone, 10 February, 2013
 * @package     com.orion.console
 **/

package com.orion.console;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.orion.command.Command;
import com.orion.domain.Client;
import com.orion.exception.ParserException;
import com.orion.exception.RconException;
import com.orion.urt.Cvar;
import com.orion.urt.Team;

public interface Console {
    
    /**
     * Ban a <tt>Client</tt> from the server using the FS Auth System
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the <tt>Client</tt> who is going to be banned
     * @param  days The number of days of the ban
     * @param  hours The number of hours of the ban
     * @param  mins The number of minutes of the ban
     * @throws UnsupportedOperationException If the Auth System has not been correctly initialized
     * @throws RconException If the RCON command fails in being executed
     **/
    public void authban(int slot, int days, int hours, int mins) throws UnsupportedOperationException, RconException;
    
    
    /**
     * Ban a <tt>Client</tt> from the server using the FS Auth System
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who is going to be banned
     * @param  days The number of days of the ban
     * @param  hours The number of hours of the ban
     * @param  mins The number of minutes of the ban
     * @throws UnsupportedOperationException If the Auth System has not been correctly initialized
     * @throws RconException If the RCON command fails in being executed
     **/
    public void authban(Client client, int days, int hours, int mins) throws UnsupportedOperationException, RconException;
    
    
    /**
     * Permban a <tt>Client</tt> from the server using the FS Auth System
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the <tt>Client</tt> who is going to be banned permanently
     * @throws UnsupportedOperationException If the Auth System has not been correctly initialized
     * @throws RconException If the RCON command fails in being executed
     **/
    public void authpermban(int slot) throws UnsupportedOperationException, RconException;
    
    
    /**
     * Permban a <tt>Client</tt> from the server using the FS Auth System
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who is going to be banned permanently
     * @throws UnsupportedOperationException If the Auth System has not been correctly initialized
     * @throws RconException If the RCON command fails in being executed
     **/
    public void authpermban(Client client) throws UnsupportedOperationException, RconException;
        
    
    /**
     * Fetch FS Auth System informations for the specified <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the <tt>Client</tt> whose informations needs to be retrieved
     * @throws UnsupportedOperationException If the Auth System has not been correctly initialized
     * @throws RconException If the RCON command fails in being executed
     * @throws ParserException If the auth-whois response couldn't be parsed correctly
     * @return A <tt>Map</tt> containing the auth-whois command response
     **/
    public Map<String, String> authwhois(int slot) throws UnsupportedOperationException, RconException, ParserException;
    
    
    /**
     * Fetch FS Auth System informations for the specified <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> whose informations needs to be retrieved
     * @throws UnsupportedOperationException If the Auth System has not been correctly initialized
     * @throws RconException If the RCON command fails in being executed
     * @throws ParserException If the auth-whois response couldn't be parsed correctly
     * @return A <tt>Map</tt> containing the auth-whois command response
     **/
    public Map<String, String> authwhois(Client client) throws UnsupportedOperationException, RconException, ParserException;
    
    
    /**
     * Ban an IP address from the server permanently
     * 
     * @author Daniele Pantaleone
     * @param  ip The IP address to be banned 
     * @throws RconException If the RCON command fails in being executed
     **/
    public void ban(String ip) throws RconException;
    
    
    /**
     * Ban a <tt>Client</tt> IP address from the server permanently
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> whose IP address is going to be banned
     * @throws RconException If the RCON command fails in being executed
     **/
    public void ban(Client client) throws RconException;
    
    
    /**
     * Write a bold message in the middle of the screen
     * 
     * @author Daniele Pantaleone
     * @param  message The message to be printed
     * @throws RconException If the RCON command fails in being executed
     **/
    public void bigtext(String message) throws RconException;
    
    
    /**
     * Broadcast a message in the top-left screen
     * 
     * @author Daniele Pantaleone
     * @param  message The message to be sent
     * @throws RconException If the RCON commands fails in being executed
     **/
    public void broadcast(String message) throws RconException;
    
    
    /**
     * Cycle the current map on the server
     * 
     * @author Daniele Pantaleone
     * @throws RconException If the RCON commands fails in being executed
     **/
    public void cyclemap() throws RconException;
    
    
    /**
     * Retrieve userinfo data for the specified <tt>Client</tt> slot number
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the <tt>Client</tt> whose informations needs to be retrieved
     * @throws RconException If the <tt>Client</tt> informations couldn't be retrieved
     * @return A <tt>Map</tt> containing userinfo data or <tt>null</tt> 
     *         if the <tt>Client</tt> is not connected anymore
     **/
    public Map<String, String> dumpuser(int slot) throws RconException;
    
    
    /**
     * Retrieve userinfo data for the specified <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> whose informations needs to be retrieved
     * @throws RconException If the <tt>Client</tt> informations couldn't be retrieved
     * @return A <tt>Map</tt> containing userinfo data or <tt>null</tt> 
     *         if the <tt>Client</tt> is not connected anymore
     **/
    public Map<String, String> dumpuser(Client client) throws RconException;
      
    
    /**
     * Force a <tt>Client</tt> in the blue team
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the <tt>Client</tt> who is going to be forced in the blue team
     * @throws RconException If the <tt>Client</tt> fails in being moved
     **/
    public void forceblue(int slot) throws RconException;
    
    
    /**
     * Force a <tt>Client</tt> in the blue team
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who is going to be forced in the blue team
     * @throws RconException If the <tt>Client</tt> fails in being moved
     **/
    public void forceblue(Client client) throws RconException;
    
    
    /**
     * Force a <tt>Client</tt> in the free team (aka autojoin)
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the <tt>Client</tt> who is going to be forced in the free team
     * @throws RconException If the <tt>Client</tt> fails in being moved
     **/
    public void forcefree(int slot) throws RconException;
    
    
    /**
     * Force a <tt>Client</tt> in the free team (aka autojoin)
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who is going to be forced in the free team
     * @throws RconException If the <tt>Client</tt> fails in being moved
     **/
    public void forcefree(Client client) throws RconException;
    
    
    /**
     * Force a <tt>Client</tt> in the red team
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the <tt>Client</tt> who is going to be forced in the red team
     * @throws RconException If the <tt>Client</tt> fails in being moved
     **/
    public void forcered(int slot) throws RconException;
    
    
    /**
     * Force a <tt>Client</tt> in the red team
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who is going to be forced in the red team
     * @throws RconException If the <tt>Client</tt> fails in being moved
     **/
    public void forcered(Client client) throws RconException;
    
    
    /**
     * Force a <tt>Client</tt> in the spectator team
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the <tt>Client</tt> who is going to be forced in the spectator team
     * @throws RconException If the <tt>Client</tt> fails in being moved
     **/
    public void forcespec(int slot) throws RconException;
    
    
    /**
     * Force a <tt>Client</tt> in the spectator team
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who is going to be forced in the spectator team
     * @throws RconException If the <tt>Client</tt> fails in being moved
     **/
    public void forcespec(Client client) throws RconException;
    
    
    /**
     * Force a <tt>Client</tt> in the specified team
     *
     * @author Daniele Pantaleone
     * @param  slot The slot of the <tt>Client</tt> who is going to be moved
     * @param  team The <tt>Team</tt> where to force the player in
     * @throws RconException If the <tt>Client</tt> fails in being moved
     **/
    public void forceteam(int slot, Team team) throws RconException;
    
    
    /**
     * Force a <tt>Client</tt> in the specified team
     *
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who is going to be moved
     * @param  team The <tt>Team</tt> where to force the player in
     * @throws RconException If the <tt>Client</tt> fails in being moved
     **/
    public void forceteam(Client client, Team team) throws RconException;
        
    
    /**
     * Retrieve a CVAR from the server
     * 
     * @author Daniele Pantaleone
     * @param  name The CVAR name
     * @throws RconException If the CVAR could not be retrieved form the server
     * @return The <tt>Cvar</tt> object associated to the given CVAR name or <tt>null</tt> 
     *         if such CVAR is not set on the server
     **/
    public Cvar getCvar(String name) throws RconException;
    
        
    /**
     * Return the current map name
     * 
     * @author Daniele Pantaleone
     * @throws RconException If the current map name couldn't be retrieved
     * @return The current map name 
     **/
    public String getMap() throws RconException;
    
    
    /**
     * Return a <tt>List</tt> of available maps
     * 
     * @author Daniele Pantaleone
     * @throws RconException If the map list couldn't be retrieved
     * @return A <tt>List</tt> of all the maps available on the server
     **/
    public List<String> getMapList() throws RconException;
    
    
    /**
     * Return a <tt>List</tt> of maps matching the given search key
     * 
     * @author Daniele Pantaleone
     * @param  search The name of the map to search (or a part of it)
     * @throws RconException If the list of available maps couldn't be computed
     * @return A <tt>List</tt> of maps matching the given search key
     **/
    public List<String> getMapSoundingLike(String search) throws RconException;
    
    
    /**
     * Return the name of the nextmap set on the server
     * 
     * @author Daniele Pantaleone
     * @throws RconException If an RCON command fails in being executed
     * @throws FileNotFoundException If the mapcycle file couldn't be found
     * @throws IOException If there is an error while reading the mapcycle file
     * @return The name of the nextmap set on the server or <tt>null</tt> 
     *         if it can't be computed
     **/
    public String getNextMap() throws RconException, FileNotFoundException, IOException;
    
 
    /**
     * Return a <tt>List</tt> containing the result of the <tt>/rcon players</tt> command
     * 
     * @author Daniele Pantaleone
     * @throws RconException If we couldn't fetch informations from the server
     * @return A <tt>List</tt> containing players informations
     **/
    public List<List<String>> getPlayers() throws RconException;
    
    
    /**
     * Return a <tt>List</tt> containing the result of the <tt>/rcon status</tt> command
     * 
     * @author Daniele Pantaleone
     * @throws RconException If we couldn't fetch informations from the server
     * @return A <tt>List</tt> containing status informations
     **/
    public List<List<String>> getStatus() throws RconException;
    
    
    /**
     * Kick the specified <tt>Client</tt> from the server
     * 
     * @author Daniele Pantaleone
     * @param  client The slot of the <tt>Client</tt> who is going to be kicked from the server
     * @throws RconException If the RCON command fails in being executed
     **/
    public void kick(int slot) throws RconException;
    
    
    /**
     * Kick the specified <tt>Client</tt> from the server
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who is going to be kicked from the server
     * @throws RconException If the RCON command fails in being executed
     **/
    public void kick(Client client) throws RconException;
    
    
    /**
     * Kick the specified <tt>Client</tt> from the server by specifying a reason
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the <tt>Client</tt> who is going to be kicked from the server
     * @param  reason The reason why the <tt>Client</tt> is going to be kicked
     * @throws RconException If the RCON command fails in being executed
     **/
    public void kick(int slot, String reason) throws RconException;
    
    
    /**
     * Kick the specified <tt>Client</tt> from the server by specifying a reason
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who is going to be kicked from the server
     * @param  reason The reason why the <tt>Client</tt> is going to be kicked
     * @throws RconException If the RCON command fails in being executed
     **/
    public void kick(Client client, String reason) throws RconException;
   
    
    /**
     * Instantly kill a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the <tt>Client</tt> who is going to be killed
     * @throws RconException If the RCON command fails in being executed
     **/
    public void kill(int slot) throws RconException;
    
    
    /**
     * Instantly kill a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who is going to be killed
     * @throws RconException If the RCON command fails in being executed
     **/
    public void kill(Client client) throws RconException;
    
    
    /**
     * Spawn the server onto a new level
     * 
     * @author Daniele Pantaleone
     * @param  mapname The name of the level to load
     * @throws RconException If the RCON command fails in being executed
     **/
    public void map(String mapname) throws RconException;
    
    
    /**
     * Mute a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone 
     * @param  slot The slot of the <tt>Client</tt> who is going to be muted
     * @throws RconException If the <tt>Client</tt> couldn't be muted
     **/
    public void mute(int slot) throws RconException;
    
    
    /**
     * Mute a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone 
     * @param  client The <tt>Client</tt> who is going to be muted
     * @throws RconException If the <tt>Client</tt> couldn't be muted
     **/
    public void mute(Client client) throws RconException;
    
    
    /**
     * Mute a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone 
     * @param  slot The slot of the <tt>Client</tt> who is going to be muted
     * @param  seconds The amount of seconds the <tt>Client</tt> will be muted
     * @throws RconException If the <tt>Client</tt> couldn't be muted
     **/
    public void mute(int slot, int seconds) throws RconException;
    
    
    /**
     * Mute a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone 
     * @param  client The <tt>Client</tt> who is going to be muted
     * @param  seconds The amount of seconds the <tt>Client</tt> will be muted
     * @throws RconException If the <tt>Client</tt> couldn't be muted
     **/
    public void mute(Client client, int seconds) throws RconException;
    

    /**
     * Nuke a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone 
     * @param  slot The slot of the <tt>Client</tt> who is going to be nuked
     * @throws RconException If the <tt>Client</tt> couldn't be nuked
     **/
    public void nuke(int slot) throws RconException;
    
    
    /**
     * Nuke a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone 
     * @param  client The <tt>Client</tt> who is going to be nuked
     * @throws RconException If the <tt>Client</tt> couldn't be nuked
     **/
    public void nuke(Client client) throws RconException;
    
    
    /**
     * Print a message in the game chat
     * 
     * @author Daniele Pantaleone
     * @param  message The message to be printed
     * @throws RconException If the RCON command fails in being executed
     **/
    public void say(String message) throws RconException;
    
    
    /**
     * Print an in-game message with visibility regulated by the command object
     * 
     * @author Daniele Pantaleone
     * @param  command The command issued
     * @param  message The message to be printed
     * @throws RconException If the RCON command fails in being executed
     **/
    public void sayLoudOrPm(Command command, String message) throws RconException;
      
    
    /**
     * Set a CVAR value
     * 
     * @author Daniele Pantaleone
     * @param  name The name of the CVAR
     * @param  value The value to assign to the CVAR
     * @throws RconException If the CVAR could not be set
     **/
    public void setCvar(String name, Object value) throws RconException;
    
    
    /**
     * Set a CVAR value
     * 
     * @author Daniele Pantaleone
     * @param  cvar The <tt>Cvar</tt> to be set on the server
     * @throws RconException If the CVAR could not be set
     **/
    public void setCvar(Cvar cvar) throws RconException;
    
    
    /**
     * Slap a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone 
     * @param  slot The slot of the <tt>Client</tt> who is going to be slapped
     * @throws RconException If the <tt>Client</tt> couldn't be slapped
     **/
    public void slap(int slot) throws RconException;
    
    
    /**
     * Slap a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone 
     * @param  client The <tt>Client</tt> who is going to be slapped
     * @throws RconException If the <tt>Client</tt> couldn't be slapped
     **/
    public void slap(Client client) throws RconException;

    
    /**
     * Start recording a server side demo of a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the <tt>Client</tt> whose demo is going to be recorded
     * @throws RconException If the demo recording couldn't be started
     **/
    public void startserverdemo(int slot) throws RconException;
    
    
    /**
     * Start recording a server side demo of a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> whose demo is going to be recorded
     * @throws RconException If the demo recording couldn't be started
     **/
    public void startserverdemo(Client client) throws RconException;
    
    
    /**
     * Start recording a server side demo of all the online clients
     * 
     * @author Daniele Pantaleone
     * @throws RconException If the demo recording couldn't be started
     **/
    public void startserverdemo() throws RconException;
    
    
    /**
     * Stop recording a server side demo of a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the <tt>Client</tt> whose demo is going to be stopped
     * @throws RconException If the demo recording couldn't be stopped
     **/
    public void stopserverdemo(int slot) throws RconException;
    
    
    /**
     * Stop recording a server side demo of a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> whose demo is going to be stopped
     * @throws RconException If the demo recording couldn't be stopped
     **/
    public void stopserverdemo(Client client) throws RconException;
    
    
    /**
     * Stop recording a server side demo of all the online clients
     * 
     * @author Daniele Pantaleone
     * @throws RconException If the demo recording couldn't be stopped
     **/
    public void stopserverdemo() throws RconException;
    
    
    /**
     * Send a private message to a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @param  slot The slot of the <tt>Client</tt> who will receive the message
     * @param  message The message to be sent
     * @throws RconException If the RCON command fails in being executed
     **/
    public void tell(int slot, String message) throws RconException;
    
    
    /**
     * Send a private message to a <tt>Client</tt>
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who will receive the message
     * @param  message The message to be sent
     * @throws RconException If the RCON command fails in being executed
     **/
    public void tell(Client client, String message) throws RconException;
    

    /**
     * Unban an IP address from the server
     * 
     * @author Daniele Pantaleone
     * @param  ip The IP address we want to unban
     * @throws RconException If the IP address couldn't be unbanned
     **/
    public void unban(String ip) throws RconException;
    
    
    /**
     * Unban a <tt>Client</tt> IP address from the server
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> whose IP address we want to unban
     * @throws RconException If the IP address couldn't be unbanned
     **/
    public void unban(Client client) throws RconException;
    
    
    /**
     * Write a message directly in the Urban Terror console<br>
     * Try to avoid the use of this command: use instead the other 
     * optimized methods available in this class
     * 
     * @author Daniele Pantaleone
     * @param  command The command to execute
     * @throws RconException If the RCON command fails in being executed
     * @return The server response to the RCON command
     **/
    public abstract String write(String command) throws RconException;
     
}