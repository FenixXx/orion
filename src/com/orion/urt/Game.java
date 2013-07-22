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
 * @copyright   Daniele Pantaleone, 03 February, 2013
 * @package     com.orion.urt
 **/

package com.orion.urt;

import java.util.List;

public class Game {
    
    private Cvar fs_game;
    private Cvar fs_basepath;
    private Cvar fs_homepath;
    
    private Cvar auth_enable;
    private Cvar auth_owners;
    
    private Cvar mapname;
    private Cvar mapcycle;
    
    private Cvar gametype;
     
    private Cvar minping;
    private Cvar maxping;
    private Cvar maxclients;
    
    private List<String> maplist;
    
    
    /**
     * Return the fs_game name CVAR
     * 
     * @author Daniele Pantaleone
     * @return The fs_game CVAR
     **/
    public Cvar getFsGame() {
        return this.fs_game;
    }
    
    
    /**
     * Return the fs_basepath CVAR
     * 
     * @author Daniele Pantaleone
     * @return The fs_basepath CVAR
     **/
    public Cvar getFsBasePath() {
        return this.fs_basepath;
    }
    
    
    /**
     * Return the fs_homepath CVAR
     * 
     * @author Daniele Pantaleone
     * @return The fs_homepath CVAR
     **/
    public Cvar getFsHomePath() {
        return this.fs_homepath;
    }
    
    
    /**
     * Return the auth_enable CVAR
     * 
     * @author Daniele Pantaleone
     * @return The auth_enable
     **/
    public Cvar getAuthEnable() {
        return this.auth_enable;
    }
    
    
    /**
     * Return the auth_owners CVAR
     * 
     * @author Daniele Pantaleone
     * @return The auth_owners CVAR
     **/
    public Cvar getAuthOwners() {
        return this.auth_owners;
    }
    
    
    /**
     * Return the mapname CVAR
     * 
     * @author Daniele Pantaleone
     * @return The mapname CVAR
     **/
    public Cvar getMapname() {
        return this.mapname;
    }
    
    
    /**
     * Return the g_mapcycle CVAR
     * 
     * @author Daniele Pantaleone
     * @return The g_mapcycle CVAR
     **/
    public Cvar getMapcycle() {
        return this.mapcycle;
    }
    
    
    /**
     * Return the g_gametype CVAR
     * 
     * @author Daniele Pantaleone
     * @return The g_gametype CVAR
     **/
    public Cvar getGametype() {
        return this.gametype;
    }
    
    
    /**
     * Return the sv_minping CVAR
     * 
     * @author Daniele Pantaleone
     * @return The sv_minping CVAR
     **/
    public Cvar getMinPing() {
        return this.minping;
    }
    
    
    /**
     * Return the sv_maxping CVAR
     * 
     * @author Daniele Pantaleone
     * @return The sv_maxping CVAR
     **/
    public Cvar getMaxPing() {
        return this.maxping;
    }
    
    
    /**
     * Return the sv_maxclients CVAR
     * 
     * @author Daniele Pantaleone
     * @return The sv_maxclients CVAR
     **/
    public Cvar getMaxClients() {
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
     * @param  fs_game The fs_game CVAR
     **/
    public void setFsGame(Cvar fs_game) {
        this.fs_game = fs_game;
    }

    
    /**
     * Set the fs_basepath
     * 
     * @author Daniele Pantaleone
     * @param  fs_basepath The fs_basepath CVAR
     **/
    public void setFsBasePath(Cvar fs_basepath) {
        this.fs_basepath = fs_basepath;
    }
    
    
    /**
     * Set the fs_homepath
     * 
     * @author Daniele Pantaleone
     * @param  fs_homepath The fs_homepath CVAR
     **/
    public void setFsHomePath(Cvar fs_homepath) {
        this.fs_homepath = fs_homepath;
    }

    
    /**
     * Set the auth_enable
     * 
     * @author Daniele Pantaleone
     * @param  auth_enable The auth_enable CVAR
     **/
    public void setAuthEnable(Cvar auth_enable) {
        this.auth_enable = auth_enable;
    }

    
    /**
     * Set the auth_owners
     * 
     * @author Daniele Pantaleone
     * @param  auth_owners The auth_owners CVAR
     **/
    public void setAuthOwners(Cvar auth_owners) {
        this.auth_owners = auth_owners;
    }


    /**
     * Set the map name
     * 
     * @author Daniele Pantaleone
     * @param  mapname The mapname CVAR
     */
    public void setMapname(Cvar mapname) {
        this.mapname = mapname;
    }

    
    /**
     * Set the current mapcycle file name
     * 
     * @author Daniele Pantaleone
     * @param  mapcycle The g_mapcycle CVAR
     **/
    public void setMapcycle(Cvar mapcycle) {
        this.mapcycle = mapcycle;
    }
    
    
    /**
     * Set the current gametype
     * 
     * @author Daniele Pantaleone
     * @param  gametype The g_gametype CVAR
     **/
    public void setGametype(Cvar gametype) {
        this.gametype = gametype;
    }

    
    /**
     * Set the sv_minping CVAR
     * 
     * @author Daniele Pantaleone
     * @param  minping the sv_minping CVAR
     **/
    public void setMinPing(Cvar minping) {
        this.minping = minping;
    }

    
    /**
     * Set the sv_maxping CVAR
     * 
     * @author Daniele Pantaleone
     * @param  minping the sv_maxping CVAR
     **/
    public void setMaxPing(Cvar maxping) {
        this.maxping = maxping;
    }
    
    
    /**
     * Set the sv_maxclients CVAR
     * 
     * @author Daniele Pantaleone
     * @param maxclients The sv_maxclients CVAR
     **/
    public void setMaxClients(Cvar maxclients) {
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