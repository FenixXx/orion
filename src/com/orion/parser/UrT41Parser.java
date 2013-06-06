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
 * @copyright   Daniele Pantaleone, 12 February, 2013
 * @package     com.orion.parser
 **/

package com.orion.parser;

import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.joda.time.DateTime;
import org.joda.time.Hours;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.eventbus.EventBus;
import com.orion.bot.Orion;
import com.orion.command.Command;
import com.orion.console.Console;
import com.orion.control.ClientC;
import com.orion.control.GroupC;
import com.orion.domain.Client;
import com.orion.event.ClientBombDefusedEvent;
import com.orion.event.ClientBombHolderEvent;
import com.orion.event.ClientBombPlantedEvent;
import com.orion.event.ClientConnectEvent;
import com.orion.event.ClientDamageEvent;
import com.orion.event.ClientDamageSelfEvent;
import com.orion.event.ClientDamageTeamEvent;
import com.orion.event.ClientDisconnectEvent;
import com.orion.event.ClientFlagCapturedEvent;
import com.orion.event.ClientFlagDroppedEvent;
import com.orion.event.ClientFlagReturnedEvent;
import com.orion.event.ClientGearChangeEvent;
import com.orion.event.ClientItemPickupEvent;
import com.orion.event.ClientJoinEvent;
import com.orion.event.ClientKillEvent;
import com.orion.event.ClientKillSelfEvent;
import com.orion.event.ClientKillTeamEvent;
import com.orion.event.ClientNameChangeEvent;
import com.orion.event.ClientSayEvent;
import com.orion.event.ClientSayPrivateEvent;
import com.orion.event.ClientSayTeamEvent;
import com.orion.event.ClientTeamChangeEvent;
import com.orion.event.GameExitEvent;
import com.orion.event.GameRoundStartEvent;
import com.orion.event.GameWarmupEvent;
import com.orion.event.TeamFlagReturnEvent;
import com.orion.event.TeamSurvivorWinnerEvent;
import com.orion.exception.ClientNotFoundException;
import com.orion.exception.ExpectedParameterException;
import com.orion.urt.Game;
import com.orion.urt.Gametype;
import com.orion.urt.Hitlocation;
import com.orion.urt.Item;
import com.orion.urt.Mod;
import com.orion.urt.Team;

public class UrT41Parser implements Parser {
    
    private static final Map<String, Pattern>       patterns           = new LinkedHashMap<String, Pattern>();
    private static final Map<Integer, Gametype>     gametypeByCode     = new HashMap<Integer, Gametype>();
    private static final Map<Integer, Hitlocation>  hitlocationByCode  = new HashMap<Integer, Hitlocation>();
    private static final Map<Character, Item>       itemByCode         = new HashMap<Character, Item>();
    private static final Map<String, Item>          itemByName         = new HashMap<String, Item>();
    private static final Map<Integer, Mod>          modByKillCode      = new HashMap<Integer, Mod>();
    private static final Map<Integer, Mod>          modByHitCode       = new HashMap<Integer, Mod>();
    private static final Map<Integer, Team>         teamByCode         = new HashMap<Integer, Team>();
    private static final Map<String, Team>          teamByName         = new HashMap<String, Team>();
    private static final Multimap<Gametype, Team>   teamsByGametype    = LinkedListMultimap.create();
    
    protected final Orion orion;
    protected final Console console;
    protected final ClientC clients;
    protected final GroupC groups;
    protected final Log log;
    
    protected EventBus eventBus;
    
    protected BlockingQueue<Command> commandqueue;
    
    protected Game game;
    
    
    static {
        
        ////////////////////////////////////
        // BEGIN LOADING URT41 GAME TYPES //
        ////////////////////////////////////
        gametypeByCode.put(0, Gametype.FFA);
        gametypeByCode.put(3, Gametype.TDM);
        gametypeByCode.put(4, Gametype.TS);
        gametypeByCode.put(5, Gametype.FTL);
        gametypeByCode.put(6, Gametype.CAH);
        gametypeByCode.put(7, Gametype.CTF);
        gametypeByCode.put(8, Gametype.BOMB);
        //////////////////////////////////
        // END LOADING URT41 GAME TYPES //
        //////////////////////////////////
        
        
        ///////////////////////////////////////
        // BEGIN LOADING URT41 HIT LOCATIONS //
        ///////////////////////////////////////
        hitlocationByCode.put(0, Hitlocation.HL_HEAD);
        hitlocationByCode.put(1, Hitlocation.HL_HELMET);
        hitlocationByCode.put(2, Hitlocation.HL_TORSO);
        hitlocationByCode.put(3, Hitlocation.HL_KEVLAR);
        hitlocationByCode.put(4, Hitlocation.HL_ARMS);
        hitlocationByCode.put(5, Hitlocation.HL_LEGS);
        hitlocationByCode.put(6, Hitlocation.HL_BODY);
        /////////////////////////////////////
        // END LOADING URT41 HIT LOCATIONS //
        /////////////////////////////////////
        
        
        ///////////////////////////////
        // BEGIN LOADING URT41 ITEMS //
        ///////////////////////////////
        itemByCode.put('A', Item.ITEM_EMPTY);
        itemByCode.put('R', Item.ITEM_VEST);
        itemByCode.put('S', Item.ITEM_NVG);
        itemByCode.put('T', Item.ITEM_MEDKIT);
        itemByCode.put('U', Item.ITEM_SILENCER);
        itemByCode.put('V', Item.ITEM_LASER);
        itemByCode.put('W', Item.ITEM_HELMET);
        itemByCode.put('X', Item.ITEM_EXTRAMMO);
        itemByCode.put('F', Item.UT_WP_BERETTA);
        itemByCode.put('G', Item.UT_WP_DEAGLE);
        itemByCode.put('H', Item.UT_WP_SPAS12);
        itemByCode.put('I', Item.UT_WP_MP5K);
        itemByCode.put('J', Item.UT_WP_UMP45);
        itemByCode.put('K', Item.UT_WP_HK69);
        itemByCode.put('L', Item.UT_WP_LR300);
        itemByCode.put('M', Item.UT_WP_G36);
        itemByCode.put('N', Item.UT_WP_PSG1);
        itemByCode.put('O', Item.UT_WP_GRENADE_HE);
        itemByCode.put('Q', Item.UT_WP_GRENADE_SMOKE);
        itemByCode.put('Z', Item.UT_WP_SR8);
        itemByCode.put('a', Item.UT_WP_AK103);
        itemByCode.put('c', Item.UT_WP_NEGEV);
        itemByCode.put('e', Item.UT_WP_M4);
        
        itemByName.put("team_CTF_redflag",        Item.ITEM_CTF_RED_FLAG);
        itemByName.put("team_CTF_blueflag",       Item.ITEM_CTF_BLUE_FLAG);
        itemByName.put("team_CTF_neutralflag",    Item.ITEM_CTF_NEUTRAL_FLAG);
        itemByName.put("ut_item_vest",            Item.ITEM_VEST);
        itemByName.put("ut_item_nvg",             Item.ITEM_NVG);
        itemByName.put("ut_item_medkit",          Item.ITEM_MEDKIT);
        itemByName.put("ut_item_silencer",        Item.ITEM_SILENCER);
        itemByName.put("ut_item_laser",           Item.ITEM_LASER);
        itemByName.put("ut_item_helmet",          Item.ITEM_HELMET);
        itemByName.put("ut_item_extraammo",       Item.ITEM_EXTRAMMO);
        itemByName.put("ut_weapon_knife",         Item.UT_WP_KNIFE);
        itemByName.put("ut_weapon_beretta",       Item.UT_WP_BERETTA);
        itemByName.put("ut_weapon_deagle",        Item.UT_WP_DEAGLE);
        itemByName.put("ut_weapon_spas12",        Item.UT_WP_SPAS12);
        itemByName.put("ut_weapon_mp5k",          Item.UT_WP_MP5K);
        itemByName.put("ut_weapon_ump45",         Item.UT_WP_UMP45);
        itemByName.put("ut_weapon_hk69",          Item.UT_WP_HK69);
        itemByName.put("ut_weapon_lr",            Item.UT_WP_LR300);
        itemByName.put("ut_weapon_g36",           Item.UT_WP_G36);
        itemByName.put("ut_weapon_psg1",          Item.UT_WP_PSG1);
        itemByName.put("ut_weapon_sr8",           Item.UT_WP_SR8);
        itemByName.put("ut_weapon_ak103",         Item.UT_WP_AK103);
        itemByName.put("ut_weapon_negev",         Item.UT_WP_NEGEV);
        itemByName.put("ut_weapon_m4",            Item.UT_WP_M4);
        itemByName.put("ut_weapon_grenade_he",    Item.UT_WP_GRENADE_HE);
        itemByName.put("ut_weapon_grenade_smoke", Item.UT_WP_GRENADE_SMOKE);
        itemByName.put("ut_weapon_bomb",          Item.UT_WP_BOMB);
        /////////////////////////////
        // END LOADING URT41 ITEMS //
        /////////////////////////////
        
        
        ////////////////////////////////////////
        // BEGIN LOADING URT41 MEANS OF DEATH //
        ////////////////////////////////////////
        modByKillCode.put(0,  Mod.MOD_UNKNOWN);
        modByKillCode.put(1,  Mod.MOD_WATER);
        modByKillCode.put(2,  Mod.MOD_SLIME);
        modByKillCode.put(3,  Mod.MOD_LAVA);
        modByKillCode.put(4,  Mod.MOD_CRUSH);
        modByKillCode.put(5,  Mod.MOD_TELEFRAG);
        modByKillCode.put(6,  Mod.MOD_FALLING);
        modByKillCode.put(7,  Mod.MOD_SUICIDE);
        modByKillCode.put(8,  Mod.MOD_TARGET_LASER);
        modByKillCode.put(9,  Mod.MOD_TRIGGER_HURT);
        modByKillCode.put(10, Mod.MOD_CHANGE_TEAM);
        modByKillCode.put(11, Mod.UT_MOD_WEAPON);
        modByKillCode.put(12, Mod.UT_MOD_KNIFE);
        modByKillCode.put(13, Mod.UT_MOD_KNIFE_THROWN);
        modByKillCode.put(14, Mod.UT_MOD_BERETTA);
        modByKillCode.put(15, Mod.UT_MOD_DEAGLE);
        modByKillCode.put(16, Mod.UT_MOD_SPAS);
        modByKillCode.put(17, Mod.UT_MOD_UMP45);
        modByKillCode.put(18, Mod.UT_MOD_MP5K);
        modByKillCode.put(19, Mod.UT_MOD_LR300);
        modByKillCode.put(20, Mod.UT_MOD_G36);
        modByKillCode.put(21, Mod.UT_MOD_PSG1);
        modByKillCode.put(22, Mod.UT_MOD_HK69);
        modByKillCode.put(23, Mod.UT_MOD_BLED);
        modByKillCode.put(24, Mod.UT_MOD_KICKED);
        modByKillCode.put(25, Mod.UT_MOD_HEGRENADE);
        modByKillCode.put(28, Mod.UT_MOD_SR8);
        modByKillCode.put(30, Mod.UT_MOD_AK103);
        modByKillCode.put(31, Mod.UT_MOD_SPLODED);
        modByKillCode.put(32, Mod.UT_MOD_SLAPPED);
        modByKillCode.put(33, Mod.UT_MOD_BOMBED);
        modByKillCode.put(34, Mod.UT_MOD_NUKED);
        modByKillCode.put(35, Mod.UT_MOD_NEGEV);
        modByKillCode.put(37, Mod.UT_MOD_HK69_HIT);
        modByKillCode.put(38, Mod.UT_MOD_M4);
        modByKillCode.put(39, Mod.UT_MOD_FLAG);
        modByKillCode.put(40, Mod.UT_MOD_GOOMBA);
        
        modByHitCode.put(0,  Mod.MOD_UNKNOWN);
        modByHitCode.put(1,  Mod.UT_MOD_KNIFE);
        modByHitCode.put(2,  Mod.UT_MOD_BERETTA);
        modByHitCode.put(3,  Mod.UT_MOD_DEAGLE);
        modByHitCode.put(4,  Mod.UT_MOD_SPAS);
        modByHitCode.put(5,  Mod.UT_MOD_MP5K);
        modByHitCode.put(6,  Mod.UT_MOD_UMP45);
        modByHitCode.put(7,  Mod.UT_MOD_HK69);
        modByHitCode.put(8,  Mod.UT_MOD_LR300);
        modByHitCode.put(9,  Mod.UT_MOD_G36);
        modByHitCode.put(10, Mod.UT_MOD_PSG1);
        modByHitCode.put(14, Mod.UT_MOD_SR8);
        modByHitCode.put(15, Mod.UT_MOD_AK103);
        modByHitCode.put(17, Mod.UT_MOD_NEGEV);
        modByHitCode.put(19, Mod.UT_MOD_M4);
        modByHitCode.put(21, Mod.UT_MOD_HEGRENADE);
        modByHitCode.put(22, Mod.UT_MOD_KNIFE_THROWN);
        //////////////////////////////////////
        // END LOADING URT41 MEANS OF DEATH //
        //////////////////////////////////////
        
        
        ///////////////////////////////
        // BEGIN LOADING URT41 TEAMS //
        ///////////////////////////////     
        teamByCode.put( 0, Team.FREE);
        teamByCode.put( 1, Team.RED);
        teamByCode.put( 2, Team.BLUE);
        teamByCode.put( 3, Team.SPECTATOR);
        
        teamByName.put("FREE",       Team.FREE);
        teamByName.put("RED",        Team.RED);
        teamByName.put("BLUE",       Team.BLUE);
        teamByName.put("SPECTATOR",  Team.SPECTATOR);

        teamsByGametype.put(Gametype.FFA,  Team.FREE);
        teamsByGametype.put(Gametype.FFA,  Team.SPECTATOR);
        teamsByGametype.put(Gametype.TDM,  Team.RED);
        teamsByGametype.put(Gametype.TDM,  Team.BLUE);
        teamsByGametype.put(Gametype.TDM,  Team.SPECTATOR);
        teamsByGametype.put(Gametype.TS,   Team.RED);
        teamsByGametype.put(Gametype.TS,   Team.BLUE);
        teamsByGametype.put(Gametype.TS,   Team.SPECTATOR);
        teamsByGametype.put(Gametype.FTL,  Team.RED);
        teamsByGametype.put(Gametype.FTL,  Team.BLUE);
        teamsByGametype.put(Gametype.FTL,  Team.SPECTATOR);
        teamsByGametype.put(Gametype.CAH,  Team.RED);
        teamsByGametype.put(Gametype.CAH,  Team.BLUE);
        teamsByGametype.put(Gametype.CAH,  Team.SPECTATOR);
        teamsByGametype.put(Gametype.CTF,  Team.RED);
        teamsByGametype.put(Gametype.CTF,  Team.BLUE);
        teamsByGametype.put(Gametype.CTF,  Team.SPECTATOR);
        teamsByGametype.put(Gametype.BOMB, Team.RED);
        teamsByGametype.put(Gametype.BOMB, Team.BLUE);
        teamsByGametype.put(Gametype.BOMB, Team.SPECTATOR);
        /////////////////////////////
        // END LOADING URT41 TEAMS //
        /////////////////////////////  
        
        
        //////////////////////////////////
        // BEGIN LOADING URT41 PATTERNS //
        //////////////////////////////////
        patterns.put("BombDefused",             Pattern.compile("^\\s*\\d+:\\d+\\s?Bomb\\swas\\sdefused\\sby\\s(?<slot>\\d+)!$", Pattern.CASE_INSENSITIVE));
        patterns.put("BombHolder",              Pattern.compile("^\\s*\\d+:\\d+\\s?Bombholder\\sis\\s+(?<slot>\\d+)$", Pattern.CASE_INSENSITIVE));
        patterns.put("BombPlanted",             Pattern.compile("^\\s*\\d+:\\d+\\s?Bomb\\swas\\splanted\\sby\\s(?<slot>\\d+)!$", Pattern.CASE_INSENSITIVE));
        patterns.put("ClientBegin",             Pattern.compile("^\\s*\\d+:\\d+\\s?ClientBegin:\\s(?<slot>\\d+)$", Pattern.CASE_INSENSITIVE));
        patterns.put("ClientConnect",           Pattern.compile("^\\s*\\d+:\\d+\\s?ClientConnect:\\s(?<slot>\\d+)$", Pattern.CASE_INSENSITIVE));
        patterns.put("ClientDisconnect",        Pattern.compile("^\\s*\\d+:\\d+\\s?ClientDisconnect:\\s(?<slot>\\d+)$", Pattern.CASE_INSENSITIVE));
        patterns.put("ClientUserinfo",          Pattern.compile("^\\s*\\d+:\\d+\\s?ClientUserinfo:\\s(?<slot>\\d+)\\s(?<infostring>.*)$", Pattern.CASE_INSENSITIVE));
        patterns.put("ClientUserinfoChanged",   Pattern.compile("^\\s*\\d+:\\d+\\s?ClientUserinfoChanged:\\s*(?<slot>\\d+)\\s*(?<infostring>.*)$", Pattern.CASE_INSENSITIVE));
        patterns.put("Exit",                    Pattern.compile("^\\s*\\d+:\\d+\\s?Exit:\\sTimelimit hit.$", Pattern.CASE_INSENSITIVE));
        patterns.put("Flag",                    Pattern.compile("^\\s*\\d+:\\d+\\s?Flag:\\s(?<slot>\\d+)\\s(?<action>\\d+):\\s(?<text>.*)$", Pattern.CASE_INSENSITIVE));
        patterns.put("FlagReturn",              Pattern.compile("^\\s*\\d+:\\d+\\s?Flag\\sReturn:\\s(?<team>.*)$", Pattern.CASE_INSENSITIVE));
        patterns.put("Hit",                     Pattern.compile("^\\s*\\d+:\\d+\\s?Hit:\\s(?<victim>\\d+)\\s(?<attacker>\\d+)\\s(?<hitlocation>\\d+)\\s(?<weapon>\\d+):\\s(.*)$", Pattern.CASE_INSENSITIVE));
        patterns.put("Item",                    Pattern.compile("^\\s*\\d+:\\d+\\s?Item:\\s(?<slot>\\d+)\\s(?<item>.*)$", Pattern.CASE_INSENSITIVE));
        patterns.put("InitGame",                Pattern.compile("^\\s*\\d+:\\d+\\s?InitGame:\\s(?<infostring>.*)$", Pattern.CASE_INSENSITIVE));
        patterns.put("InitRound",               Pattern.compile("^\\s*\\d+:\\d+\\s?InitRound:\\s(?<infostring>.*)$", Pattern.CASE_INSENSITIVE));
        patterns.put("Kill",                    Pattern.compile("^\\s*\\d+:\\d+\\s?Kill:\\s(?<attacker>\\d+)\\s(?<victim>\\d+)\\s(?<weapon>\\d+):\\s(.*)$", Pattern.CASE_INSENSITIVE));
        patterns.put("Say",                     Pattern.compile("^\\s*\\d+:\\d+\\s?say:\\s(?<slot>\\d+)\\s(?<name>.*):\\s?(?<message>.*)$", Pattern.CASE_INSENSITIVE));
        patterns.put("SayTell",                 Pattern.compile("^\\s*\\d+:\\d+\\s?saytell:\\s(?<slot>\\d+)\\s(?<target>\\d+)\\s(?<name>.*):\\s?(?<message>.*)$", Pattern.CASE_INSENSITIVE));
        patterns.put("SayTeam",                 Pattern.compile("^\\s*\\d+:\\d+\\s?sayteam:\\s(?<slot>\\d+)\\s(?<name>.*):\\s?(?<message>.*)$", Pattern.CASE_INSENSITIVE));
        patterns.put("SurvivorWinner",          Pattern.compile("^\\s*\\d+:\\d+\\s?SurvivorWinner:\\s(?<data>.*)$", Pattern.CASE_INSENSITIVE));
        patterns.put("ShutdownGame",            Pattern.compile("^\\s*\\d+:\\d+\\s?ShutdownGame:$", Pattern.CASE_INSENSITIVE));
        patterns.put("Warmup",                  Pattern.compile("^\\s*\\d+:\\d+\\s?Warmup:$", Pattern.CASE_INSENSITIVE));
        ////////////////////////////////
        // END LOADING URT41 PATTERNS //
        ////////////////////////////////
        
    }
    
    
    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone 
     * @param  orion <tt>Orion</tt> object reference
     **/
    public UrT41Parser(Orion orion) {
        
        this.orion = orion;
        this.console = orion.console;
        this.clients = orion.clients;
        this.groups = orion.groups;
        this.eventBus = orion.eventBus;
        this.commandqueue = orion.commandqueue;
        this.game = orion.game;
        this.log = orion.log;
        
        // Printing a log message so the user can check if he's using the correct parser
        this.log.debug("Parser initialized: com.orion.parser." + orion.config.getString("orion", "game") + "Parser");
        
    }
     
    
    /**
     * Return the <tt>Gametype</tt> matching the given code
     * 
     * @author Daniele Pantaleone
     * @param  code The <tt>Gametype</tt> code
     * @throws IndexOutOfBoundsException If the <tt>Gametype</tt> code is not mapped
     * @return The <tt>Gametype</tt> matching the given code
     **/
    public Gametype getGametypeByCode(Integer code) throws IndexOutOfBoundsException {
        
        if (!gametypeByCode.containsKey(code)) 
            throw new IndexOutOfBoundsException("Unable to retrieve a Gametype using the given code: " + code);
        
        return gametypeByCode.get(code);
    }
    
    
    /**
     * Return the <tt>Hitlocation</tt> matching the given code
     * 
     * @author Daniele Pantaleone
     * @param  code The <tt>Hitlocation</tt> code
     * @throws IndexOutOfBoundsException If the <tt>Hitlocation</tt> code is not mapped
     * @return The <tt>Hitlocation</tt> matching the given code
     **/
    public Hitlocation getHitlocationByCode(Integer code) throws IndexOutOfBoundsException {
        
        if (!hitlocationByCode.containsKey(code)) 
            throw new IndexOutOfBoundsException("Unable to retrieve a Hitlocation using the given code: " + code);
        
        return hitlocationByCode.get(code);
    }
    
    
    /**
     * Return the <tt>Item</tt> matching the given gear code
     * 
     * @author Daniele Pantaleone
     * @param  code The <tt>Item</tt> gear code
     * @throws IndexOutOfBoundsException If the <tt>Item</tt> gear code is not mapped
     * @return The <tt>Item</tt> matching the given gear code
     **/
    public Item getItemByCode(Character code) throws IndexOutOfBoundsException {
        
        if (!itemByCode.containsKey(code)) 
            throw new IndexOutOfBoundsException("Unable to retrieve an Item using the given code: " + code);
        
        return itemByCode.get(code);
    }
    
    
    /**
     * Return the <tt>Item</tt> matching the given name
     * 
     * @author Daniele Pantaleone
     * @param  name The <tt>Item</tt> name
     * @throws IndexOutOfBoundsException If the <tt>Item</tt> name is not mapped
     * @return The <tt>Item</tt> matching the given name
     **/
    public Item getItemByName(String name) throws IndexOutOfBoundsException {
        
        if (!itemByName.containsKey(name)) 
            throw new IndexOutOfBoundsException("Unable to retrieve an Item using the given name: " + name);
        
        return itemByName.get(name);
    }
    
    
    /**
     * Return the <tt>Mod</tt> matching the given kill code
     * 
     * @author Daniele Pantaleone
     * @param  killcode The <tt>Mod</tt> kill code
     * @throws IndexOutOfBoundsException If the <tt>Mod</tt> kill code is not mapped
     * @return The <tt>Mod</tt> matching the given kill code
     **/
    public Mod getModByKillCode(int code) throws IndexOutOfBoundsException {
        
        if (!modByKillCode.containsKey(code)) 
            throw new IndexOutOfBoundsException("Unable to retrieve a Mod using the given kill code: " + code);
        
        return modByKillCode.get(code);
    }
    
    
    /**
     * Return the <tt>Mod</tt> matching the given hit code
     * 
     * @author Daniele Pantaleone
     * @param  hitcode The <tt>Mod</tt> hit code
     * @throws IndexOutOfBoundsException If the <tt>Mod</tt> hit code code is not mapped
     * @return The <tt>Mod</tt> matching the given hit code
     **/
    public Mod getModByHitCode(int code) throws IndexOutOfBoundsException {
        
        if (!modByHitCode.containsKey(code)) 
            throw new IndexOutOfBoundsException("Unable to retrieve a Mod using the given hit code: " + code);
     
        return modByHitCode.get(code);
    }
    
    
    /**
     * Return the <tt>Team</tt> matching the given code.
     * 
     * @author Daniele Pantaleone
     * @param  code The <tt>Team</tt> code
     * @throws IndexOutOfBoundsException If the <tt>Team</tt> code is not mapped
     * @return The <tt>Team</tt> matching the given code
     **/
    public Team getTeamByCode(Integer code) throws IndexOutOfBoundsException {
       
        if (!teamByCode.containsKey(code)) 
            throw new IndexOutOfBoundsException("Unable to retrieve a Team using the given code: " + code);
        
        return teamByCode.get(code);
    }
    
    
    /**
     * Return the Team by matching the given name.
     * 
     * @author Daniele Pantaleone
     * @param  name The <tt>Team</tt> name
     * @throws IndexOutOfBoundsException If the <tt>Team</tt> name is not mapped
     * @return The <tt>Team</tt> matching the given name
     **/
    public Team getTeamByName(String name) throws IndexOutOfBoundsException {
        
        name = name.toUpperCase();
        
        switch(name) {
            
            case "F":      name = "FREE";       break;
            case "R":      name = "RED";        break;
            case "B":      name = "BLUE";       break;
            case "S":      name = "SPECTATOR";  break;
            case "SPEC":   name = "SPECTATOR";  break;
        
        }   
        
        if (!teamByName.containsKey(name)) 
            throw new IndexOutOfBoundsException("Unable to retrieve a Team using the given name: " + name);
        
        return teamByName.get(name);
    }
    
    
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
    public List<Team> getAvailableTeams() {
        
        if (this.game.getGametype() == null) {
            // Retrieve the current g_gametype before returning the collection
            this.game.setGametype(this.getGametypeByCode(this.console.getCvar("g_gametype", Integer.class)));
        }
        
        return new LinkedList<Team>(teamsByGametype.get(this.game.getGametype()));
    
    }
        
    
    //////////////////////////
    // BEGIN HELPER METHODS //
    //////////////////////////
    
    
    /**
     * Helper method for BombDefused
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onBombDefused(Matcher matcher) {
        
        // 0:00 Bomb was defused by 3!
        // 0:00 Bomb was defused by 11!
        
        try {
            
            Client client = this.clients.getBySlot(Integer.valueOf(matcher.group("slot")));
            if (client == null) throw new ClientNotFoundException("cannot retrieve client on slot " + matcher.group("slot"));
            this.log.trace("[EVENT] EVT_CLIENT_BOMB_DEFUSED [ client : " + client.getSlot() + " ]");
            this.eventBus.post(new ClientBombDefusedEvent(client));
            
        } catch (ClientNotFoundException e) {        
            
            // Logging the Exception
            this.log.error("[EVENT] EVT_CLIENT_BOMB_DEFUSED", e);   
            return;
            
        }
        
    }
    
    
    /**
     * Helper method for BombHolder
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onBombHolder(Matcher matcher) {
        
        // 0:00 Bombholder is 1
        // 0:00 Bombholder is 15
        
         try {            
             
            Client client = this.clients.getBySlot(Integer.valueOf(matcher.group("slot")));
            if (client == null) throw new ClientNotFoundException("cannot retrieve client on slot " + matcher.group("slot"));
            this.log.trace("[EVENT] EVT_CLIENT_BOMB_HOLDER [ client : " + client.getSlot() + " ]");
            this.eventBus.post(new ClientBombHolderEvent(client));
            
         } catch (ClientNotFoundException e) {            
            
            // Logging the Exception
            this.log.error("[EVENT] EVT_CLIENT_BOMB_HOLDER", e);
            return;
            
         }
         
    }
    
    
    /**
     * Helper method for BombPlanted
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onBombPlanted(Matcher matcher) {
        
        // 0:00 Bomb was planted by 3!
        // 0:00 Bomb was planted by 11!
        
        try {
            
            Client client = this.clients.getBySlot(Integer.valueOf(matcher.group("slot")));
            if (client == null) throw new ClientNotFoundException("cannot retrieve client on slot " + matcher.group("slot"));
            this.log.trace("[EVENT] EVT_CLIENT_BOMB_PLANTED [ client : " + client.getSlot() + " ]");
            this.eventBus.post(new ClientBombPlantedEvent(client));
            
        } catch (ClientNotFoundException e) {
            
            // Logging the Exception
            this.log.error("[EVENT] EVT_CLIENT_BOMB_PLANTED", e);
            return;
            
        }
        
    }
    
    
    /**
     * Helper method for ClientBegin
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onClientBegin(Matcher matcher) {
        
        // 0:00 ClientBegin: 0
        // 0:00 ClientBegin: 4
        
        try {
            
            Client client = this.clients.getBySlot(Integer.valueOf(matcher.group("slot")));
            if (client == null) throw new ClientNotFoundException("cannot retrieve client on slot " + matcher.group("slot"));
            this.log.trace("[EVENT] EVT_CLIENT_JOIN [ client : " + client.getSlot() + " ]");
            this.eventBus.post(new ClientJoinEvent(client));
            
        } catch (ClientNotFoundException e)  {
            
            // Logging the Exception
            this.log.error("[EVENT] EVT_CLIENT_JOIN", e);
            return;
        
        }
        
    }
    
    
    /**
     * Helper method for ClientConnect
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onClientConnect(Matcher matcher) {
        
        // 0:00 ClientConnect: 0
        // 0:00 ClientConnect: 4
        
        // Just log the new client connection. We'll handle this somewhere else...
        this.log.debug("Client connecting on slot " + matcher.group("slot") + ". Ready to parse userinfo string...");

    }
    
    
    /**
     * Helper method for ClientDisconnect
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onClientDisconnect(Matcher matcher){
        
        // 0:00 ClientDisconnect: 0
        // 0:00 ClientDisconnect: 4
        
        try {
            
            Client client = this.clients.removeBySlot(Integer.parseInt(matcher.group("slot")));
            if (client == null) throw new ClientNotFoundException("cannot retrieve client on slot " + matcher.group("slot"));
            this.log.trace("[EVENT] EVT_CLIENT_DISCONNECT [ client : " + client.getSlot() + " ]");
            this.eventBus.post(new ClientDisconnectEvent(client));
            
        } catch (ClientNotFoundException e)  {
            
            // Logging the Exception
            this.log.error("[EVENT] EVT_CLIENT_DISCONNECT", e);
            return;
        
        }
        
    }
    
    
    /**
     * Helper method for ClientUserinfo
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onClientUserinfo(Matcher matcher) { 
        
        // 0:00 ClientUserinfo: 9 \ip\93.84.143.218:27960\name\DsP**Unhitman....
        // 0:00 ClientUserinfo: 8 \ip\87.166.159.201:27960\name\SickHippster!nc....
        
        Client client = this.clients.getBySlot(Integer.valueOf(matcher.group("slot")));
        Map<String, String> userinfo = this.parseInfoString(matcher.group("infostring"));
        
        if (client == null) {
            
            // Checking if we have a BOT connecting to the game server
            if (!(userinfo.containsKey("cl_guid")) && (userinfo.containsKey("skill"))) {
                
                try {
                    
                    // We have a BOT connecting. We need to handle this in a different way...
                    client = new Client.Builder(InetAddress.getByName("0.0.0.0"), "BOT_" + matcher.group("slot"))
                                       .bot(true)
                                       .build();
                    
                    this.log.debug("Client connecting on slot " + matcher.group("slot") + " has been detected as a BOT");
                    
                } catch (UnknownHostException e) {
                    
                    // Logging the Exception
                    this.log.error("[EVENT] EVT_CLIENT_CONNECT", e);
                    return;
                
                }
                
            } else {
                
                try {
                    
                    this.log.debug("Attempt to authenticate client on slot " + matcher.group("slot") + " [ cl_guid : " + userinfo.get("cl_guid") + " ]");
                    client = this.clients.getByGuid(userinfo.get("cl_guid"));
                    
                    if (client != null) {
                        // We got already informations for this client
                        // We'll use such info for the authentication and we'll updated the client object with some new ones
                        this.log.debug("Client connecting on slot " + matcher.group("slot") + " authenticated " + client.toString());
                    } else {
                        // Unable to find a match in the storage for the connecting client
                        // We'll handle this as a new client connection on this game server
                        this.log.debug("No match found in the storage for client connecting on slot " + matcher.group("slot") + ". We'll be handled as a new client " + " [ cl_guid : " + userinfo.get("cl_guid") + " ]");
                    }
                    
                } catch (ClassNotFoundException | UnknownHostException | SQLException e) {
                    
                    // Logging the Exception
                    this.log.error("[EVENT] EVT_CLIENT_CONNECT", e);
                    return;
                    
                }
            
            }
            
            
            try {
                    
                if (client == null) {   
                    
                    // We didn't managed to find a match in our database for the connecting client
                    // More over the connecting client is not a bot, otherwise we would have generated the object
                    // We will generate a new client object for the client and save it into our database
                    client = new Client.Builder(InetAddress.getByName(userinfo.get("ip").split(":", 2)[0]), userinfo.get("cl_guid"))
                                       .group(this.groups.getByKeyword("guest"))
                                       .build();
                    
                }
                
                client.setSlot(Integer.valueOf(matcher.group("slot")));
                
                // Update the client number of connections just if the client is a new client or if disconnected at last one hour ago
                if ((client.getTimeEdit() == null) || (Hours.hoursBetween(client.getTimeEdit(), new DateTime(this.orion.timezone)).getHours() > 1)) {
                    client.setConnections(client.getConnections() + 1);
                } 
                
                if (userinfo.containsKey("name"))
                    client.setName(userinfo.get("name"));
     
                if (userinfo.containsKey("gear"))
                    client.setGear(userinfo.get("gear"));
                
                if (userinfo.containsKey("team"))    
                    client.setTeam(getTeamByName(userinfo.get("team")));
                
                // Saving the client
                this.clients.add(client);
                this.clients.save(client);
                
                this.log.trace("[EVENT] EVT_CLIENT_CONNECT [ client : " + client.getSlot() + " ]");
                this.eventBus.post(new ClientConnectEvent(client));
            
            } catch (UnknownHostException | ClassNotFoundException | SQLException e) {
                
                // Logging the Exception
                this.log.error("[EVENT] EVT_CLIENT_CONNECT", e);
                return;
                
            }
                
        } else {

            // Checking for possible gear change
            if (userinfo.containsKey("gear")) {
                
                String gear = userinfo.get("gear");
                if ((client.getGear() == null) || (!client.getGear().equals(gear))) {
               
                    client.setGear(gear);
                    this.log.trace("[EVENT] EVT_CLIENT_GEAR_CHANGE [ client : " + client.getSlot() + " ]");
                    this.eventBus.post(new ClientGearChangeEvent(client));
               
                }
            
            }
        
        }  
    
    }
    
    
    /**
     * Helper method for ClientUserinfoChanged
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onClientUserinfoChanged(Matcher matcher) {
        
        //0:00 ClientUserinfoChanged: 0 n\{B.D}LEGIONARIA\t\0\r\1\tl\0\a0\255\a1\0\a2\255
        //0:00 ClientUserinfoChanged: 1 n\ExcessivePlayer\t\0\r\2\tl\0\a0\0\a1\0\a2\0
        
        try {
            
            Client client = this.clients.getBySlot(Integer.valueOf(matcher.group("slot")));
            if (client == null) throw new ClientNotFoundException("cannot retrieve client on slot " + matcher.group("slot"));
            
            // Parsing userinfo string so we can generate our custom events
            Map<String, String> userinfo = parseInfoString(matcher.group("infostring"));
            
            // Checking for a possible name change
            if (userinfo.containsKey("n")) {
                
                String name = userinfo.get("n").replaceAll("\\^[0-9]{1}", "");
                if (!client.getName().toLowerCase().equals(name.toLowerCase())) {
                    
                    client.setName(name);
                    this.log.trace("[EVENT] EVT_CLIENT_NAME_CHANGE [ client : " + client.getSlot() + " ]");
                    this.eventBus.post(new ClientNameChangeEvent(client));
                    
                }
            }
            
            // Checking for a possible team change
            if (userinfo.containsKey("t")) {
                   
                try {
                    
                    Team team = getTeamByCode(Integer.parseInt(userinfo.get("t")));
                    
                    if (client.getTeam() != team) {
                    
                        client.setTeam(team);
                        this.log.trace("[EVENT] EVT_CLIENT_TEAM_CHANGE [ client : " + client.getSlot() + " ]");
                        this.eventBus.post(new ClientTeamChangeEvent(client));
                        
                    }
                    
                } catch (IndexOutOfBoundsException e) {
                    
                    // Logging the Exception
                    this.log.error("[EVENT] EVT_CLIENT_TEAM_CHANGE", e);
                    return;
                
                }
                
            }
            
        } catch (ClientNotFoundException e) {
            
            // Logging the Exception
            this.log.error("[EVENT] EVT_CLIENT_NAME_CHANGE | EVT_CLIENT_TEAM_CHANGE", e);
            return;
            
        }
        
    }
    
    
    /**
     * Helper method for Flag
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onFlag(Matcher matcher) {
        
        // 0:00 Flag: 0 2: team_CTF_redflag
        // 0:00 Flag: 0 0: team_CTF_blueflag
        
        try {
            
            Client client = this.clients.getBySlot(Integer.valueOf(matcher.group("slot")));
            if (client == null) throw new ClientNotFoundException("cannot retrieve client on slot " + matcher.group("slot"));
            
            switch (Integer.parseInt(matcher.group("action"))) {
                
                case 0:
                    this.log.trace("[EVENT] EVT_CLIENT_FLAG_DROPPED [ client : " + client.getSlot() + " ]");
                    this.eventBus.post(new ClientFlagDroppedEvent(client));
                    break;
                    
                case 1:
                    this.log.trace("[EVENT] EVT_CLIENT_FLAG_RETURNED [ client : " + client.getSlot() + " ]");
                    this.eventBus.post(new ClientFlagReturnedEvent(client));
                    break;
                    
                case 2:
                    this.log.trace("[EVENT] EVT_CLIENT_FLAG_CAPTURED [ client : " + client.getSlot() + " ]");
                    this.eventBus.post(new ClientFlagCapturedEvent(client));
                    break;
            
            }
        
        } catch (ClientNotFoundException | IndexOutOfBoundsException e) {
           
            // Logging the Exception
            this.log.error("[EVENT] EVT_CLIENT_FLAG_DROPPED | EVT_CLIENT_FLAG_RETURNED | EVT_CLIENT_FLAG_CAPTURED", e);
            return;
        
        }
    }
    
    
    /**
     * Helper method for Flag Return
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onFlagReturn(Matcher matcher) {
        
        // 0:00 Flag Return: BLUE
        // 0:00 Flag Return: RED
        
        try {
            
            this.log.trace("[EVENT] EVT_TEAM_FLAG_RETURN [ team : " + matcher.group("team") + " ]");
            this.eventBus.post(new TeamFlagReturnEvent(getTeamByName(matcher.group("team"))));
        
        } catch (IndexOutOfBoundsException e) {
            
            // Logging the Exception
            this.log.error("[EVENT] EVT_TEAM_FLAG_RETURN", e);
            return;
        
        }
    }
    
    
    /**
     * Helper method for Hit
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onHit(Matcher matcher) {
        
        // 0:00 Hit: 12 7 1 19: [Gore]Pinhead hit Fapking in the Helmet
        // 0:00 Hit: 13 10 0 8: WizardOfGore hit Fenix in the Head
        
        try {
            
            Client victim = this.clients.getBySlot(Integer.parseInt(matcher.group("victim")));
            if (victim == null) throw new ClientNotFoundException("cannot retrieve victim client on slot " + matcher.group("victim"));
            
            Client attacker = this.clients.getBySlot(Integer.parseInt(matcher.group("attacker")));
            if (attacker == null) throw new ClientNotFoundException("cannot retrieve attacker client on slot " + matcher.group("attacker"));
            
            if (attacker == victim) {
                
                ClientDamageSelfEvent event = new ClientDamageSelfEvent(victim, getModByHitCode(Integer.parseInt(matcher.group("weapon"))), getHitlocationByCode(Integer.parseInt(matcher.group("hitlocation"))));
                this.log.trace("[EVENT] EVT_CLIENT_DAMAGE_SELF [ client : " + event.getClient().getSlot() + " | mod : " + event.getMod().name() + " | hitlocation : " + event.getHitLocation().name() + " ]");
                this.eventBus.post(event);
                
            } else if ((attacker.getTeam() == victim.getTeam()) && (attacker.getTeam() != Team.SPECTATOR) && (attacker.getTeam() != Team.FREE)) {

                ClientDamageTeamEvent event = new ClientDamageTeamEvent(attacker, victim, getModByHitCode(Integer.parseInt(matcher.group("weapon"))), getHitlocationByCode(Integer.parseInt(matcher.group("hitlocation"))));
                this.log.trace("[EVENT] EVT_CLIENT_DAMAGE_TEAM [ client : " + event.getClient().getSlot() + " | victim : " + event.getVictim().getSlot() + " | mod : " + event.getMod().name() + " | hitlocation : " + event.getHitLocation().name() + " ]");
                this.eventBus.post(event);
                
            } else {
                
                ClientDamageEvent event = new ClientDamageEvent(attacker, victim, getModByHitCode(Integer.parseInt(matcher.group("weapon"))), getHitlocationByCode(Integer.parseInt(matcher.group("hitlocation"))));
                this.log.trace("[EVENT] EVT_CLIENT_DAMAGE [ client : " + event.getClient().getSlot() + " | victim : " + event.getVictim().getSlot() + " | mod : " + event.getMod().name() + " | hitlocation : " + event.getHitLocation().name() + " ]");
                this.eventBus.post(event);
                
            }
            
        } catch (ClientNotFoundException | IndexOutOfBoundsException e) {
            
            // Logging the Exception
            this.log.error("[EVENT] EVT_CLIENT_DAMAGE_SELF | EVT_CLIENT_DAMAGE_TEAM | EVT_CLIENT_DAMAGE", e);
            return;
        
        }
 
    }
    
    
    /**
     * Helper method for onInitGame
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onInitGame(Matcher matcher) {
        
        // 0:00 InitGame: \sv_allowdownload\0\g_matchmode\1\g_gametype\4\sv_maxclients\20\sv_floodprotect\0...
        // 0:00 InitGame: \sv_allowdownload\0\g_matchmode\1\g_gametype\4\sv_maxclients\20\sv_floodprotect\0...
        Map<String, String> userinfo = this.parseInfoString(matcher.group("infostring")); 
        
        // Update game variables in order to have always updated informations in plugins
        if (userinfo.containsKey("g_gametype"))    this.game.setGametype(this.getGametypeByCode(Integer.parseInt(userinfo.get("g_gametype"))));  
        if (userinfo.containsKey("g_mapcycle"))    this.game.setMapcycle(userinfo.get("g_mapcycle"));  
        if (userinfo.containsKey("mapname"))       this.game.setMapName(userinfo.get("mapname"));  
        if (userinfo.containsKey("sv_minping"))    this.game.setMinPing(Integer.parseInt(userinfo.get("sv_minping")));  
        if (userinfo.containsKey("sv_maxping"))    this.game.setMaxPing(Integer.parseInt(userinfo.get("sv_maxping")));  
        if (userinfo.containsKey("sv_maxclients")) this.game.setMaxClients(Integer.parseInt(userinfo.get("sv_maxclients")));

        this.log.trace("[EVENT] EVT_GAME_ROUND_START [ infostring : " + matcher.group("infostring") + " ]");
        this.eventBus.post(new GameRoundStartEvent());
            
    }
    
    
    /**
     * Helper method for InitRound
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onInitRound(Matcher matcher) {
        
        // 0:00 InitRound: \sv_allowdownload\0\g_matchmode\1\g_gametype\4\sv_maxclients\20\sv_floodprotect\0...
        // 0:00 InitRound: \sv_allowdownload\0\g_matchmode\1\g_gametype\4\sv_maxclients\20\sv_floodprotect\0...
        Map<String, String> userinfo = this.parseInfoString(matcher.group("infostring")); 
        
        // Update game variables in order to have always updated informations in plugins
        if (userinfo.containsKey("g_gametype"))    this.game.setGametype(this.getGametypeByCode(Integer.parseInt(userinfo.get("g_gametype"))));  
        if (userinfo.containsKey("g_mapcycle"))    this.game.setMapcycle(userinfo.get("g_mapcycle"));  
        if (userinfo.containsKey("mapname"))       this.game.setMapName(userinfo.get("mapname"));  
        if (userinfo.containsKey("sv_minping"))    this.game.setMinPing(Integer.parseInt(userinfo.get("sv_minping")));  
        if (userinfo.containsKey("sv_maxping"))    this.game.setMaxPing(Integer.parseInt(userinfo.get("sv_maxping")));  
        if (userinfo.containsKey("sv_maxclients")) this.game.setMaxClients(Integer.parseInt(userinfo.get("sv_maxclients")));

        this.log.trace("[EVENT] EVT_GAME_ROUND_START [ infostring : " + matcher.group("infostring") + " ]");
        this.eventBus.post(new GameRoundStartEvent());

    }
    
    
    /**
     * Helper method for Item
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onItem(Matcher matcher) {
        
        // 0:00 Item: 0 ut_item_deagle
        // 0:00 Item: 1 ut_item_medkit
        
        try {
            
            Client client = this.clients.getBySlot(Integer.valueOf(matcher.group("slot")));
            if (client == null) throw new ClientNotFoundException("cannot retrieve client on slot " + matcher.group("slot"));
            ClientItemPickupEvent event = new ClientItemPickupEvent(client, getItemByName(matcher.group("item")));
            this.log.trace("[EVENT] EVT_CLIENT_ITEM_PICKUP [ client : " + event.getClient().getSlot() + " | item : " + event.getItem().name() + " ]");
            this.eventBus.post(event);
            
        } catch (ClientNotFoundException | IndexOutOfBoundsException e) {
            
            // Logging the Exception
            this.log.error("[EVENT] EVT_CLIENT_ITEM_PICKUP", e);
            return;
            
        }
        
    }
    
    
    /**
     * Helper method for Kill
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onKill(Matcher matcher) { 
        
        // 0:00 Kill: 0 1 16: Fenix killed WizardOfGore by UT_MOD_SPAS
        // 0:00 Kill: 14 4 21: Fenix killed Fapking by UT_MOD_PSG1
        
        try {
            
            Mod mod = this.getModByKillCode(Integer.parseInt(matcher.group("weapon")));
            
            switch (mod) {
            
                case MOD_CHANGE_TEAM:
                    return;
            
                case MOD_WATER:
                case MOD_SLIME:
                case MOD_LAVA:
                case MOD_CRUSH:
                case MOD_FALLING:
                case MOD_SUICIDE:
                case MOD_TRIGGER_HURT:
                case UT_MOD_SPLODED:
                        
                    
                    Client client = this.clients.getBySlot(Integer.parseInt(matcher.group("victim")));
                    if (client == null) throw new ClientNotFoundException("cannot retrieve victim client on slot " + matcher.group("victim"));
                    this.log.trace("[EVENT] EVT_CLIENT_KILL_SELF [ client : " + client.getSlot() + " | mod : " + mod.name() + " ]");
                    this.eventBus.post(new ClientKillSelfEvent(client, mod));
                    
                    break;
    
                default:
                    
                    Client attacker = this.clients.getBySlot(Integer.parseInt(matcher.group("attacker")));
                    if (attacker == null) throw new ClientNotFoundException("cannot retrieve attacker client on slot " + matcher.group("attacker"));
                    
                    Client victim = this.clients.getBySlot(Integer.parseInt(matcher.group("victim")));
                    if (victim == null) throw new ClientNotFoundException("cannot retrieve victim client on slot " + matcher.group("victim"));
                    
                    if ((attacker == victim) && (attacker.getTeam() != Team.SPECTATOR)) {
                        this.log.trace("[EVENT] EVT_CLIENT_KILL_SELF [ client : " + attacker.getSlot() + " | mod : " + mod.name() + " ]");
                        this.eventBus.post(new ClientKillSelfEvent(attacker, mod));
                    } else if ((attacker.getTeam() == victim.getTeam()) && (attacker.getTeam() != Team.SPECTATOR) && (attacker.getTeam() != Team.FREE)) {
                        this.log.trace("[EVENT] EVT_CLIENT_KILL_TEAM intercepted [ client : " + attacker.getSlot() + " | victim : " + victim.getSlot() + " | mod : " + mod.name() + " ]");
                        this.eventBus.post(new ClientKillTeamEvent(attacker, victim, mod));
                    } else {
                        this.log.trace("[EVENT] EVT_CLIENT_KILL intercepted [ client : " + attacker.getSlot() + " | victim : " + victim.getSlot() + " | mod : " + mod.name() + " ]");
                        this.eventBus.post(new ClientKillEvent(attacker, victim, mod));
                    }
                    
                    break;
           
            }
            
            
        } catch (ClientNotFoundException | IndexOutOfBoundsException e) {
            
            // Logging the Exception
            this.log.error("[EVENT] EVT_CLIENT_KILL_SELF | EVT_CLIENT_KILL_TEAM | EVT_CLIENT_KILL", e);
            return;
            
        } 
        
    }
    
    
    /**
     * Helper method for Say
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onSay(Matcher matcher) {
        
        //0:00 say: 8 denzel: lol
        //0:00 say: 9 .:MS-T:.BstPL: this name is quite a challenge
        
        try {
            
            Client client = this.clients.getBySlot(Integer.valueOf(matcher.group("slot")));
            
            if ((client == null) || (!client.getName().equals(matcher.group("name")))) {
                
                // This bug usually happen when running the bot on a server which is not com_dedicated
                // It's a well known bug. We'll try to get the client object by matching the client name              
                List<Client> collection = this.clients.getByName(matcher.group("name"));
                if (collection.size() == 0) throw new ClientNotFoundException("cannot retrieve client with name " + matcher.group("name"));
                if (collection.size() > 1) throw new ClientNotFoundException("multiple clients matching name " + matcher.group("name"));
                
                // We got it
                client = collection.get(0);   
            
            }
            
            String message = matcher.group("message").trim();
            if (message.isEmpty()) throw new ExpectedParameterException("message is empty");
            
            // Check if an Orion command has been issued
            if ((message.startsWith("!")) || (message.startsWith("@")) || (message.startsWith("&"))) {
                
                //if (message.substring(0, 2).equals("!!")) {
                //    // Ugly hack to make !! an alias for !say
                //    // Used for b3 command syntax compatibility
                //    message = "!say "+ (message.substring(2));
                //}
                
                //// Orion command intercepted
                //String data[] = message.substring(1).split(" ", 2);  
                //Command command = new Command(client, Prefix.getByChar(message.charAt(0)), data[0].toLowerCase(), (data.length > 1) ? data[1].toLowerCase() : null);
                //this.log.trace("Orion command intercepted [ client : " + command.client.slot + " | prefix : " + command.prefix + " | handle : " + command.handle + " | params : " + command.params + " ]");
                //this.commandqueue.put(command);
                
            } else {
                
                this.log.trace("[EVENT] EVT_CLIENT_SAY [ client : " + client.getSlot() + " | message : " + message + " ]");
                this.eventBus.post(new ClientSayEvent(client, message));
                
            }
            
        } catch (ClientNotFoundException | ExpectedParameterException e) {
            
            // Logging the Exception
            this.log.error("[EVENT] EVT_CLIENT_SAY", e);
            return;
        
        }
        
    }
    
    
    /**
     * Helper method for SayTell
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onSayTell(Matcher matcher) {
        
        // 0:00 saytell: 15 16 repelSteeltje: nno
        // 0:00 saytell: 4 7 Fenix: ahahahh it's cool isn't it?
        
        try {
            
            Client client = this.clients.getBySlot(Integer.valueOf(matcher.group("slot")));
            
            if ((client == null) || (!client.getName().equals(matcher.group("name")))) {
                
                // This bug usually happen when running the bot on a server which is not com_dedicated
                // It's a well known bug. We'll try to get the client object by matching the client name              
                List<Client> collection = this.clients.getByName(matcher.group("name"));
                if (collection.size() == 0) throw new ClientNotFoundException("cannot retrieve client with name " + matcher.group("name"));
                if (collection.size() > 1) throw new ClientNotFoundException("multiple clients matching name " + matcher.group("name"));
                
                // We got it
                client = collection.get(0);   
            
            }
            
            Client target = this.clients.getBySlot(Integer.parseInt(matcher.group("target")));
            if (target == null) throw new ClientNotFoundException("cannot retrieve target client on slot " + matcher.group("target"));
            
            String message = matcher.group("message").trim();
            if (message.isEmpty()) throw new ExpectedParameterException("message is empty");
            
            // Check if an Orion command has been issued
            if ((message.startsWith("!")) || (message.startsWith("@")) || (message.startsWith("&"))) {
                
                //if (message.substring(0, 2).equals("!!")) {
                //    // Ugly hack to make !! an alias for !say
                //    // Used for b3 command syntax compatibility
                //    message = "!say "+ (message.substring(2));
                //}
                
                //// Orion command intercepted
                //String data[] = message.substring(1).split(" ", 2);  
                //Command command = new Command(client, Prefix.getByChar(message.charAt(0)), data[0].toLowerCase(), (data.length > 1) ? data[1].toLowerCase() : null);
                //this.log.trace("Orion command intercepted [ client : " + command.client.slot + " | prefix : " + command.prefix + " | handle : " + command.handle + " | params : " + command.params + " ]");
                //this.commandqueue.put(command);
                
            } else {
                
                this.log.trace("[EVENT] EVT_CLIENT_SAY_PRIVATE [ client : " + client.getSlot() + " | target : " + target.getSlot() + " | message : " + message + " ]");
                this.eventBus.post(new ClientSayPrivateEvent(client, target, message));
                
            }
            
        } catch (ClientNotFoundException | ExpectedParameterException e) {
            
            // Logging the Exception
            this.log.error("[EVENT] EVT_CLIENT_SAY_PRIVATE", e);
            return;
        
        }
        
    }
    
    
    /**
     * Helper method for SayTeam
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onSayTeam(Matcher matcher) {
        
        // 0:00 say: 8 denzel: lol
        // 0:00 say: 9 .:MS-T:.BstPL: this name is quite a challenge
        
        try {
            
            Client client = this.clients.getBySlot(Integer.valueOf(matcher.group("slot")));
            
            if ((client == null) || (!client.getName().equals(matcher.group("name")))) {
                
                // This bug usually happen when running the bot on a server which is not com_dedicated
                // It's a well known bug. We'll try to get the client object by matching the client name              
                List<Client> collection = this.clients.getByName(matcher.group("name"));
                if (collection.size() == 0) throw new ClientNotFoundException("cannot retrieve client with name " + matcher.group("name"));
                if (collection.size() > 1) throw new ClientNotFoundException("multiple clients matching name " + matcher.group("name"));
                
                // We got it
                client = collection.get(0);   
            
            }
            
            String message = matcher.group("message").trim();
            if (message.isEmpty()) throw new ExpectedParameterException("message is empty");
            
            // Check if an Orion command has been issued
            if ((message.startsWith("!")) || (message.startsWith("@")) || (message.startsWith("&"))) {
                
                //if (message.substring(0, 2).equals("!!")) {
                //    // Ugly hack to make !! an alias for !say
                //    // Used for b3 command syntax compatibility
                //    message = "!say "+ (message.substring(2));
                //}
                
                //// Orion command intercepted
                //String data[] = message.substring(1).split(" ", 2);  
                //Command command = new Command(client, Prefix.getByChar(message.charAt(0)), data[0].toLowerCase(), (data.length > 1) ? data[1].toLowerCase() : null);
                //this.log.trace("Orion command intercepted [ client : " + command.client.slot + " | prefix : " + command.prefix + " | handle : " + command.handle + " | params : " + command.params + " ]");
                //this.commandqueue.put(command);
                
            } else {
                
                this.log.trace("[EVENT] EVT_CLIENT_SAY_TEAM [ client : " + client.getSlot() + " | message : " + message + " ]");
                this.eventBus.post(new ClientSayTeamEvent(client, message));
                
            }
            
        } catch (ClientNotFoundException | ExpectedParameterException e) {
            
            // Logging the Exception
            this.log.error("[EVENT] EVT_CLIENT_SAY_TEAM", e);
            return;
        
        }
        
    }
    
    
    /**
     * Helper method for ShutdownGame
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onShutdownGame(Matcher matcher) { 
        
        // 0:00 ShutdownGame:
        // 0:00 ShutdownGame:
        
        this.game.setMapList(null);
        this.game.setMapName(null);
        this.game.setGametype(null);
        this.game.setMapcycle(null);
        this.game.setMaxClients(-1);
        this.game.setMaxPing(-1);
        this.game.setMinPing(-1);
        
        this.log.trace("[EVENT] EVT_GAME_EXIT");
        this.eventBus.post(new GameExitEvent());
    
    }
    
    
    /**
     * Helper method for SurvivorWinner
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onSurvivorWinner(Matcher matcher) { 
        
        // 0:00 SurvivorWinner: Red
        // 0:00 SurvivorWinner: Blue
        
        try {
            
            Team team = this.getTeamByName(matcher.group("data"));
            this.log.trace("[EVENT] EVT_TEAM_SURVIVOR_WINNER [ team : " + team.name() + " ]");
            this.eventBus.post(new TeamSurvivorWinnerEvent(team));
        
        } catch (IndexOutOfBoundsException e) {
                
            // Logging the Exception
            this.log.error("[EVENT] EVT_TEAM_SURVIVOR_WINNER", e);
            return;
            
       }
          
    }
    
    
    /**
     * Helper method for Warmup
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onWarmup(Matcher matcher) { 
        
        // 0:00 Warmup:
        // 0:00 Warmup:
        
        this.log.trace("[EVENT] EVT_GAME_WARMUP");
        this.eventBus.post(new GameWarmupEvent());

    }
    
    
    ////////////////////////
    // END HELPER METHODS //
    ////////////////////////
    
    
    /**
     * Return an <tt>LinkedHashMap</tt> containing the informations of the info string provided.
     * InfoString format: \ip\110.143.73.144:27960\challenge\1052098110\qport\51418\protocol\68...
     * 
     * @author Daniele Pantaleone
     * @param  info The infostring to be parsed 
     * @return A <tt>HashMap</tt> with the result of the dumped userinfo
     **/
    public Map<String,String> parseInfoString(String info) {
        
        Map<String, String> userinfo = new HashMap<String, String>();
        
        if (info.charAt(0) == '\\')
            info = info.substring(1);
        
        // Splitting the infostring
        String lines[] = info.split("\\\\");
        
        for (int i = 0; i < lines.length; i += 2 ) {
            // Adding all the matches in our map
            userinfo.put(lines[i].toLowerCase(), lines[i+1]);
        }

        return userinfo;
        
    }
    
    
    /**
     * Parse a Urban Terror log message.
     * Will generate an <tt>Event</tt> if necessary and push it in the <tt>Event</tt> queue.
     * 
     * @author Daniele Pantaleone
     * @param  line A log line
     **/
    public void parseLine(String line) {
        
        Matcher matcher = null;
        
        // Iterating through all the patters trying to find a match
        for (Map.Entry<String, Pattern> entry : patterns.entrySet()) { 
            
            // Getting a matcher for the current line
            matcher = entry.getValue().matcher(line);
            if (!matcher.matches()) continue;
            
            try {
                
                this.getClass()
                    .getMethod("on" + entry.getKey(), Matcher.class)
                    .invoke(this, matcher);
                
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException | NoSuchMethodException e) {
                
                // We are not able to parse the correct line. Possible issues are: 
                // no method defined for the given pattern, an invalid argument modifier
                // specified or an invalid argument passed to the method as parameter.
                this.log.error(e);
            
            }
            
            break;
            
        }
           
    }
}
