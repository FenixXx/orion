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
 * @copyright   Daniele Pantaleone, 03 February, 2013
 * @package     com.orion.urt
 **/

package com.orion.urt;

import java.util.List;

public class Game {
	
    public String    	 fs_game;
    public String    	 fs_basepath;
    public String    	 fs_homepath;
    
    public Boolean       auth_enable;
    public Integer       auth_owners;
    
    public String    	 mapname;
    public Gametype  	 gametype;
    public String    	 mapcycle;
     
    public int       	 minPing    = -1;
    public int       	 maxPing    = -1;
    public int       	 maxClients = -1;
    
    public List<String>  mapList;
    
}