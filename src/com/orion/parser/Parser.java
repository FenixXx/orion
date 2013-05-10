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
 * @copyright   Daniele Pantaleone, 19 January, 2013
 * @package     com.orion.parser
 **/

package com.orion.parser;

import java.util.List;
import java.util.Map;

import com.orion.urt.Gametype;
import com.orion.urt.Hitlocation;
import com.orion.urt.Item;
import com.orion.urt.Mod;
import com.orion.urt.Team;

public interface Parser {
    
    /**
     * Return the <tt>Gametype</tt> matching the given code
     * 
     * @author Daniele Pantaleone
     * @param  code The <tt>Gametype</tt> code
     * @throws IndexOutOfBoundsException If the <tt>Gametype</tt> code is not mapped
     * @return The <tt>Gametype</tt> matching the given code
     **/
    public abstract Gametype getGametypeByCode(Integer code) throws IndexOutOfBoundsException;
    
    
    /**
     * Return the <tt>Hitlocation</tt> matching the given code
     * 
     * @author Daniele Pantaleone
     * @param  code The <tt>Hitlocation</tt> code
     * @throws IndexOutOfBoundsException If the <tt>Hitlocation</tt> code is not mapped
     * @return The <tt>Hitlocation</tt> matching the given code
     **/
    public abstract Hitlocation getHitlocationByCode(Integer code) throws IndexOutOfBoundsException;
    
    
    /**
     * Return the <tt>Item</tt> matching the given gear code
     * 
     * @author Daniele Pantaleone
     * @param  code The <tt>Item</tt> gear code
     * @throws IndexOutOfBoundsException If the <tt>Item</tt> gear code is not mapped
     * @return The <tt>Item</tt> matching the given gear code
     **/
    public abstract Item getItemByCode(Character code) throws IndexOutOfBoundsException;
    
    
    /**
     * Return the <tt>Item</tt> matching the given name
     * 
     * @author Daniele Pantaleone
     * @param  name The <tt>Item</tt> name
     * @throws IndexOutOfBoundsException If the <tt>Item</tt> name is not mapped
     * @return The <tt>Item</tt> matching the given name
     **/
    public abstract Item getItemByName(String name) throws IndexOutOfBoundsException;
      
    
    /**
     * Return the <tt>Mod</tt> matching the given kill code
     * 
     * @author Daniele Pantaleone
     * @param  killcode The <tt>Mod</tt> kill code
     * @throws IndexOutOfBoundsException If the <tt>Mod</tt> kill code is not mapped
     * @return The <tt>Mod</tt> matching the given kill code
     **/
    public abstract Mod getModByKillCode(int code) throws IndexOutOfBoundsException;
    
    
    /**
     * Return the <tt>Mod</tt> matching the given hit code
     * 
     * @author Daniele Pantaleone
     * @param  hitcode The <tt>Mod</tt> hit code
     * @throws IndexOutOfBoundsException If the <tt>Mod</tt> hit code code is not mapped
     * @return The <tt>Mod</tt> matching the given hit code
     **/
    public abstract Mod getModByHitCode(int code) throws IndexOutOfBoundsException;
    
    
    /**
     * Return the <tt>Team</tt> matching the given code.
     * 
     * @author Daniele Pantaleone
     * @param  code The <tt>Team</tt> code
     * @throws IndexOutOfBoundsException If the <tt>Team</tt> code is not mapped
     * @return The <tt>Team</tt> matching the given code
     **/
    public abstract Team getTeamByCode(Integer code) throws IndexOutOfBoundsException;
    
    
    /**
     * Return the Team by matching the given name.
     * 
     * @author Daniele Pantaleone
     * @param  name The <tt>Team</tt> name
     * @throws IndexOutOfBoundsException If the <tt>Team</tt> name is not mapped
     * @return The <tt>Team</tt> matching the given name
     **/
    public abstract Team getTeamByName(String name) throws IndexOutOfBoundsException;
    
    
    /**
     * Return a <tt>List</tt> of available <tt>Team</tt> objects according to
     * the current played <tt>Gametype</tt>. If the <tt>Gametype</tt> has not 
     * been computed yet, it will retrieve the value from the server before 
     * returning the collection
     * 
     * @author Daniele Pantaleone
     * @return A <tt>List</tt> of available <tt>Team</tt> objects according to
     *         the current played <tt>Gametype</tt>
     **/
    public abstract List<Team> getAvailableTeams();
    
    
    /**
     * Return an <tt>LinkedHashMap</tt> containing the informations of the info string provided.
     * InfoString format: \ip\110.143.73.144:27960\challenge\1052098110\qport\51418\protocol\68...
     * 
     * @author Daniele Pantaleone
     * @param  info The infostring to be parsed 
     * @return A <tt>LinkedHashMap</tt> with the result of the dumped userinfo
     **/
    public abstract Map<String,String> parseInfoString(String info);
    
    
    /**
     * Parse a Urban Terror log message.
     * Will generate an <tt>Event</tt> if necessary and push it in the <tt>Event</tt> queue.
     * 
     * @author Daniele Pantaleone
     * @param  line A log line
     **/
    public abstract void parseLine(String line);

}
