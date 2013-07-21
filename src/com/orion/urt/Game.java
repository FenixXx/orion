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
 * @version     1.1.1
 * @copyright   Daniele Pantaleone, 03 February, 2013
 * @package     com.orion.urt
 **/

package com.orion.urt;

import java.util.List;

public class Game {
    
    private String fs_game;
    private String fs_basepath;
    private String fs_homepath;
    
    private boolean auth_enable;
    private Integer auth_owners;
    
    private String mapname;
    private String mapcycle;
    
    private Gametype gametype;
     
    private int minping = -1;
    private int maxping = -1;
    private int maxclients = -1;
    
    private List<String> maplist;
    
    
    /**
     * Return the fs_game name
     * 
     * @author Daniele Pantaleone
     * @return The fs_game CVAR value
     **/
    public String getFsGame() {
        return this.fs_game;
    }
    
    
    /**
     * Return the fs_basepath
     * 
     * @author Daniele Pantaleone
     * @return The fs_basepath CVAR value
     **/
    public String getFsBasePath() {
        return this.fs_basepath;
    }
    
    
    /**
     * Return the fs_homepath
     * 
     * @author Daniele Pantaleone
     * @return The fs_homepath CVAR value
     **/
    public String getFsHomePath() {
        return this.fs_homepath;
    }
    
    
    /**
     * Tells whether the auth system is enabled
     * 
     * @author Daniele Pantaleone
     * @return <tt>true</tt> if the auth system is enabled, <tt>false</tt> otherwise
     **/
    public boolean isAuthEnabled() {
        return this.auth_enable;
    }
    
    
    /**
     * Return the auth_owners
     * 
     * @author Daniele Pantaleone
     * @return The auth_owners CVAR value
     **/
    public Integer getAuthOwners() {
        return this.auth_owners;
    }
    
    
    /**
     * Return the current map name
     * 
     * @author Daniele Pantaleone
     * @return The current map name
     **/
    public String getMapname() {
        return this.mapname;
    }
    
    
    /**
     * Return the current mapcycle file name
     * 
     * @author Daniele Pantaleone
     * @return The current mapcycle file name
     **/
    public String getMapcycle() {
        return this.mapcycle;
    }
    
    
    /**
     * Return the current gametype
     * 
     * @author Daniele Pantaleone
     * @return The current gametype
     **/
    public Gametype getGametype() {
        return this.gametype;
    }
    
    
    /**
     * Return the sv_minping CVAR value
     * 
     * @author Daniele Pantaleone
     * @return The sv_minping CVAR value
     **/
    public int getMinPing() {
        return this.minping;
    }
    
    
    /**
     * Return the sv_maxping CVAR value
     * 
     * @author Daniele Pantaleone
     * @return The sv_maxping CVAR value
     **/
    public int getMaxPing() {
        return this.maxping;
    }
    
    
    /**
     * Return the sv_maxclients CVAR value
     * 
     * @author Daniele Pantaleone
     * @return The sv_maxclients CVAR value
     **/
    public int getMaxClients() {
        return this.maxclients;
    }
    
    
    /**
     * Return the current map list
     * 
     * @author Daniele Pantaleone
     * @return The current map list
     **/
    public List<String> getMapList() {
        return this.maplist;
    }
    
    
    /**
     * Set the fs_game name
     * 
     * @author Daniele Pantaleone
     * @param  fs_game The fs_game CVAR value
     **/
    public void setFsGame(String fs_game) {
        this.fs_game = fs_game;
    }

    
    /**
     * Set the fs_basepath
     * 
     * @author Daniele Pantaleone
     * @param  fs_basepath The fs_basepath CVAR value
     **/
    public void setFsBasePath(String fs_basepath) {
        this.fs_basepath = fs_basepath;
    }
    
    
    /**
     * Set the fs_homepath
     * 
     * @author Daniele Pantaleone
     * @param  fs_homepath The fs_homepath CVAR value
     **/
    public void setFsHomePath(String fs_homepath) {
        this.fs_homepath = fs_homepath;
    }

    
    /**
     * Set the auth_enable
     * 
     * @author Daniele Pantaleone
     * @param  auth_enable The auth_enable CVAR value converted as a <tt>boolean</tt>
     **/
    public void setAuthEnable(boolean auth_enable) {
        this.auth_enable = auth_enable;
    }

    
    /**
     * Set the auth_owners
     * 
     * @author Daniele Pantaleone
     * @param  auth_owners The auth_owners CVAR value
     **/
    public void setAuthOwners(Integer auth_owners) {
        this.auth_owners = auth_owners;
    }


    /**
     * Set the map name
     * 
     * @author Daniele Pantaleone
     * @param  mapname The current map name
     */
    public void setMapName(String mapname) {
        this.mapname = mapname;
    }

    
    /**
     * Set the current mapcycle file name
     * 
     * @author Daniele Pantaleone
     * @param  mapcycle The current mapcycle file name
     **/
    public void setMapcycle(String mapcycle) {
        this.mapcycle = mapcycle;
    }
    
    
    /**
     * Set the current gametype
     * 
     * @author Daniele Pantaleone
     * @param  gametype The currently played gametype
     **/
    public void setGametype(Gametype gametype) {
        this.gametype = gametype;
    }

    
    /**
     * Set the sv_minping CVAR value
     * 
     * @author Daniele Pantaleone
     * @param  minping the sv_minping CVAR value
     **/
    public void setMinPing(int minping) {
        this.minping = minping;
    }

    
    /**
     * Set the sv_maxping CVAR value
     * 
     * @author Daniele Pantaleone
     * @param  minping the sv_maxping CVAR value
     **/
    public void setMaxPing(int maxping) {
        this.maxping = maxping;
    }
    
    
    /**
     * Set the sv_maxclients CVAR value
     * 
     * @author Daniele Pantaleone
     * @param maxclients The sv_maxclients CVAR value
     **/
    public void setMaxClients(int maxclients) {
        this.maxclients = maxclients;
    }
    
    
    /**
     * Set the current map list
     * 
     * @author Daniele Pantaleone
     * @param maplist The current map list
     **/
    public void setMapList(List<String> maplist) {
        this.maplist = maplist;
    }
    
}