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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.Hours;


import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.orion.bot.Orion;
import com.orion.domain.Callvote;
import com.orion.domain.Client;
import com.orion.event.ClientCallvoteEvent;
import com.orion.event.ClientConnectEvent;
import com.orion.event.ClientGearChangeEvent;
import com.orion.event.ClientJumpRunCanceledEvent;
import com.orion.event.ClientJumpRunStartedEvent;
import com.orion.event.ClientJumpRunStoppedEvent;
import com.orion.event.ClientPositionLoadEvent;
import com.orion.event.ClientPositionSaveEvent;
import com.orion.event.ClientRadioEvent;
import com.orion.event.ClientVoteEvent;
import com.orion.event.SurvivorWinnerEvent;
import com.orion.event.TeamSurvivorWinnerEvent;
import com.orion.exception.ClientNotFoundException;
import com.orion.urt.Gametype;
import com.orion.urt.Hitlocation;
import com.orion.urt.Item;
import com.orion.urt.Mod;
import com.orion.urt.Team;

public class UrT42Parser extends UrT41Parser implements Parser {
    
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
    
    
    static {
        
        ////////////////////////////////////
        // BEGIN LOADING URT42 GAME TYPES //
        ////////////////////////////////////
        gametypeByCode.put(0, Gametype.FFA);
        gametypeByCode.put(1, Gametype.LMS);
        gametypeByCode.put(3, Gametype.TDM);
        gametypeByCode.put(4, Gametype.TS);
        gametypeByCode.put(5, Gametype.FTL);
        gametypeByCode.put(6, Gametype.CAH);
        gametypeByCode.put(7, Gametype.CTF);
        gametypeByCode.put(8, Gametype.BOMB);
        gametypeByCode.put(9, Gametype.JUMP);
        //////////////////////////////////
        // END LOADING URT42 GAME TYPES //
        //////////////////////////////////
        
        
        ///////////////////////////////////////
        // BEGIN LOADING URT42 HIT LOCATIONS //
        ///////////////////////////////////////
        hitlocationByCode.put(1,  Hitlocation.HL_HEAD);
        hitlocationByCode.put(4,  Hitlocation.HL_HELMET);
        hitlocationByCode.put(5,  Hitlocation.HL_TORSO);
        hitlocationByCode.put(6,  Hitlocation.HL_VEST);
        hitlocationByCode.put(7,  Hitlocation.HL_ARM_LEFT);
        hitlocationByCode.put(8,  Hitlocation.HL_ARM_RIGHT);
        hitlocationByCode.put(9,  Hitlocation.HL_LEGS);
        hitlocationByCode.put(12, Hitlocation.HL_BODY);
        /////////////////////////////////////
        // END LOADING URT41 HIT LOCATIONS //
        /////////////////////////////////////
        
        
        ///////////////////////////////
        // BEGIN LOADING URT42 ITEMS //
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
        itemByCode.put('f', Item.UT_WP_GLOCK);
        
        itemByName.put("team_CTF_redflag",        Item.ITEM_CTF_RED_FLAG);
        itemByName.put("team_CTF_blueflag",       Item.ITEM_CTF_RED_FLAG);
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
        itemByName.put("ut_weapon_glock",         Item.UT_WP_GLOCK);       
        itemByName.put("ut_weapon_grenade_he",    Item.UT_WP_GRENADE_HE);
        itemByName.put("ut_weapon_grenade_smoke", Item.UT_WP_GRENADE_SMOKE);
        itemByName.put("ut_weapon_bomb",          Item.UT_WP_BOMB);
        /////////////////////////////
        // END LOADING URT42 ITEMS //
        /////////////////////////////
        
        
        ////////////////////////////////////////
        // BEGIN LOADING URT42 MEANS OF DEATH //
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
        modByKillCode.put(27, Mod.UT_MOD_SMOKEGRENADE);
        modByKillCode.put(28, Mod.UT_MOD_SR8);
        modByKillCode.put(30, Mod.UT_MOD_AK103);
        modByKillCode.put(31, Mod.UT_MOD_SPLODED);
        modByKillCode.put(32, Mod.UT_MOD_SLAPPED);
        modByKillCode.put(33, Mod.UT_MOD_SMITED);
        modByKillCode.put(34, Mod.UT_MOD_BOMBED);
        modByKillCode.put(35, Mod.UT_MOD_NUKED);
        modByKillCode.put(36, Mod.UT_MOD_NEGEV);
        modByKillCode.put(37, Mod.UT_MOD_HK69_HIT);
        modByKillCode.put(38, Mod.UT_MOD_M4);
        modByKillCode.put(39, Mod.UT_MOD_GLOCK);
        modByKillCode.put(40, Mod.UT_MOD_FLAG);
        modByKillCode.put(41, Mod.UT_MOD_GOOMBA);
        
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
        modByHitCode.put(11, Mod.UT_MOD_HEGRENADE);
        modByHitCode.put(14, Mod.UT_MOD_SR8);
        modByHitCode.put(15, Mod.UT_MOD_AK103);
        modByHitCode.put(17, Mod.UT_MOD_NEGEV);
        modByHitCode.put(19, Mod.UT_MOD_M4);
        modByHitCode.put(20, Mod.UT_MOD_GLOCK);
        modByHitCode.put(22, Mod.UT_MOD_KICKED);
        modByHitCode.put(23, Mod.UT_MOD_KNIFE);
        //////////////////////////////////////
        // END LOADING URT42 MEANS OF DEATH //
        //////////////////////////////////////
        
        
        ///////////////////////////////
        // BEGIN LOADING URT42 TEAMS //
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
        teamsByGametype.put(Gametype.LMS,  Team.FREE);
        teamsByGametype.put(Gametype.LMS,  Team.SPECTATOR);
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
        teamsByGametype.put(Gametype.JUMP, Team.FREE);
        teamsByGametype.put(Gametype.JUMP, Team.SPECTATOR);
        /////////////////////////////
        // END LOADING URT42 TEAMS //
        /////////////////////////////  
        
        
        //////////////////////////////////
        // BEGIN LOADING URT42 PATTERNS //
        //////////////////////////////////
        patterns.put("BombDefused",             Pattern.compile("^\\s*\\d+:\\d+\\s?Bomb\\swas\\sdefused\\sby\\s(?<slot>\\d+)!$", Pattern.CASE_INSENSITIVE));
        patterns.put("BombHolder",              Pattern.compile("^\\s*\\d+:\\d+\\s?Bombholder\\sis\\s+(?<slot>\\d+)$", Pattern.CASE_INSENSITIVE));
        patterns.put("BombPlanted",             Pattern.compile("^\\s*\\d+:\\d+\\s?Bomb\\swas\\splanted\\sby\\s(?<slot>\\d+)!$", Pattern.CASE_INSENSITIVE));
        patterns.put("Callvote",                Pattern.compile("^\\s*\\d+:\\d+\\s?Callvote:\\s?(?<slot>\\d+)\\s?-\\s?\\\"(?<type>\\w+)\\s?(?<data>.*)\\\"$", Pattern.CASE_INSENSITIVE));
        patterns.put("ClientBegin",             Pattern.compile("^\\s*\\d+:\\d+\\s?ClientBegin:\\s(?<slot>\\d+)$", Pattern.CASE_INSENSITIVE));
        patterns.put("ClientConnect",           Pattern.compile("^\\s*\\d+:\\d+\\s?ClientConnect:\\s(?<slot>\\d+)$", Pattern.CASE_INSENSITIVE));
        patterns.put("ClientDisconnect",        Pattern.compile("^\\s*\\d+:\\d+\\s?ClientDisconnect:\\s(?<slot>\\d+)$", Pattern.CASE_INSENSITIVE));
        patterns.put("ClientJumpRunCanceled",   Pattern.compile("^\\s*\\d+:\\d+\\s?ClientJumpRunCanceled:\\s(?<slot>\\d+)\\s-\\sway:\\s(?<way>\\d+).*$", Pattern.CASE_INSENSITIVE));
        patterns.put("ClientJumpRunStarted",    Pattern.compile("^\\s*\\d+:\\d+\\s?ClientJumpRunStarted:\\s(?<slot>\\d+)\\s-\\sway:\\s(?<way>\\d+).*$", Pattern.CASE_INSENSITIVE));
        patterns.put("ClientJumpRunStopped",    Pattern.compile("^\\s*\\d+:\\d+\\s?ClientJumpRunStopped:\\s(?<slot>\\d+)\\s-\\sway:\\s(?<way>\\d+)\\s-\\stime:\\s(?<time>\\d+).*$", Pattern.CASE_INSENSITIVE));
        patterns.put("ClientLoadPosition",      Pattern.compile("^\\s*\\d+:\\d+\\s?ClientLoadPosition:\\s(?<slot>\\d+)\\s-\\s(?<x>-?\\d+\\.\\d+)\\s-\\s(?<y>-?\\d+\\.\\d+)\\s-\\s(?<z>-?\\d+\\.\\d+)\\s-\\s\\\"(?<location>.*)\\\"$", Pattern.CASE_INSENSITIVE));
        patterns.put("ClientSavePosition",      Pattern.compile("^\\s*\\d+:\\d+\\s?ClientSavePosition:\\s(?<slot>\\d+)\\s-\\s(?<x>-?\\d+\\.\\d+)\\s-\\s(?<y>-?\\d+\\.\\d+)\\s-\\s(?<z>-?\\d+\\.\\d+)\\s-\\s\\\"(?<location>.*)\\\"$", Pattern.CASE_INSENSITIVE));
        patterns.put("ClientUserinfo",          Pattern.compile("^\\s*\\d+:\\d+\\s?ClientUserinfo:\\s(?<slot>\\d+)\\s(?<infostring>.*)$", Pattern.CASE_INSENSITIVE));
        patterns.put("ClientUserinfoChanged",   Pattern.compile("^\\s*\\d+:\\d+\\s?ClientUserinfoChanged:\\s*(?<slot>\\d+)\\s*(?<infostring>.*)$", Pattern.CASE_INSENSITIVE));
        patterns.put("Exit",                    Pattern.compile("^\\s*\\d+:\\d+\\s?Exit:\\sTimelimit hit.$", Pattern.CASE_INSENSITIVE));
        patterns.put("Flag",                    Pattern.compile("^\\s*\\d+:\\d+\\s?Flag:\\s(?<slot>\\d+)\\s(?<action>\\d+):\\s(?<text>.*)$", Pattern.CASE_INSENSITIVE));
        patterns.put("FlagReturn",              Pattern.compile("^\\s*\\d+:\\d+\\s?Flag\\sReturn:\\s(?<team>.*)$", Pattern.CASE_INSENSITIVE));
        patterns.put("Hit",                     Pattern.compile("^\\s*\\d+:\\d+\\s?Hit:\\s(?<victim>\\d+)\\s(?<attacker>\\d+)\\s(?<hitlocation>\\d+)\\s(?<weapon>\\d+):\\s(.*)$", Pattern.CASE_INSENSITIVE));
        patterns.put("Item",                    Pattern.compile("^\\s*\\d+:\\d+\\sItem:\\s(?<slot>\\d+)\\s(?<item>.*)$", Pattern.CASE_INSENSITIVE));
        patterns.put("InitGame",                Pattern.compile("^\\s*\\d+:\\d+\\s?InitGame:\\s(?<infostring>.*)$", Pattern.CASE_INSENSITIVE));
        patterns.put("InitRound",               Pattern.compile("^\\s*\\d+:\\d+\\s?InitRound:\\s(?<infostring>.*)$", Pattern.CASE_INSENSITIVE));
        patterns.put("Kill",                    Pattern.compile("^\\s*\\d+:\\d+\\s?Kill:\\s(?<attacker>\\d+)\\s(?<victim>\\d+)\\s(?<weapon>\\d+):\\s(.*)$", Pattern.CASE_INSENSITIVE));
        patterns.put("Radio",                   Pattern.compile("^\\s*\\d+:\\d+\\s?Radio:\\s?(?<slot>\\d+)\\s?-\\s?(?<group>\\d+)\\s?-\\s?(?<id>\\d+)\\s?-\\s?\\\"(?<location>.*)\\\"\\s?-\\s?\\\"(?<message>.*)\\\"$", Pattern.CASE_INSENSITIVE));
        patterns.put("Say",                     Pattern.compile("^\\s*\\d+:\\d+\\s?say:\\s(?<slot>\\d+)\\s(?<name>.*):\\s?(?<message>.*)$", Pattern.CASE_INSENSITIVE));
        patterns.put("SayTell",                 Pattern.compile("^\\s*\\d+:\\d+\\s?saytell:\\s(?<slot>\\d+)\\s(?<target>\\d+)\\s(?<name>.*):\\s?(?<message>.*)$", Pattern.CASE_INSENSITIVE));
        patterns.put("SayTeam",                 Pattern.compile("^\\s*\\d+:\\d+\\s?sayteam:\\s(?<slot>\\d+)\\s(?<name>.*):\\s?(?<message>.*)$", Pattern.CASE_INSENSITIVE));
        patterns.put("ShutdownGame",            Pattern.compile("^\\s*\\d+:\\d+\\s?ShutdownGame:$", Pattern.CASE_INSENSITIVE));
        patterns.put("SurvivorWinner",          Pattern.compile("^\\s*\\d+:\\d+\\s?SurvivorWinner:\\s(?<data>.*)$", Pattern.CASE_INSENSITIVE));
        patterns.put("Vote",                    Pattern.compile("^\\s*\\d+:\\d+\\s?Vote:\\s?(?<slot>\\d+)\\s?-\\s?(?<data>\\d+)$", Pattern.CASE_INSENSITIVE));
        patterns.put("Warmup",                  Pattern.compile("^\\s*\\d+:\\d+\\s?Warmup:$", Pattern.CASE_INSENSITIVE));
        ////////////////////////////////
        // END LOADING URT42 PATTERNS //
        ////////////////////////////////
        
    }


    /**
     * Object constructor
     * 
     * @author Daniele Pantaleone 
     * @param  orion Orion object reference
     **/
    public UrT42Parser(Orion orion) {
        super(orion);        
    }
    
    
    /**
     * Return the <tt>Gametype</tt> matching the given code
     * 
     * @author Daniele Pantaleone
     * @param  code The <tt>Gametype</tt> code
     * @throws IndexOutOfBoundsException If the <tt>Gametype</tt> code is not mapped
     * @return The <tt>Gametype</tt> matching the given code
     **/
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
     * Helper method for Callvote
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onCallvote(Matcher matcher) {
        
        // 0:00 Callvote: 3 - "map ut42_jupiter"
        // 0:00 Callvote: 3 - "cyclemap"
        
        try {
            
            Client client = this.clients.getBySlot(Integer.valueOf(matcher.group("slot")));
            if (client == null) throw new ClientNotFoundException("cannot retrieve client on slot " + matcher.group("slot"));
            
            Callvote callvote = new Callvote.Builder(client, matcher.group("type"))
                                            .data(((matcher.group("data") != null) && (!matcher.group("data").isEmpty())) ? matcher.group("data") : null)
                                            .build();
            
            this.log.trace("[EVENT] EVT_CLIENT_CALLVOTE [ client : " + client.getSlot() + " | type : " + matcher.group("type") + " | data : " + matcher.group("data") + " ]");
            this.eventBus.post(new ClientCallvoteEvent(client, callvote));
            
        } catch (ClientNotFoundException e) {
            
            // Logging the Exception
            this.log.error("[EVENT] EVT_CLIENT_CALLVOTE", e);
            return;
        
        }

    }
    
    
    /**
     * Helper method for ClientJumpRunCanceled
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onClientJumpRunCanceled(Matcher matcher) {
        
        // 0:00 ClientJumpRunCanceled: 0 - way: 2
        // 0:00 ClientJumpRunCanceled: 0 - way: 2 - attempt: 1 of 5
        
        try {
            
            Client client = this.clients.getBySlot(Integer.valueOf(matcher.group("slot")));
            if (client == null) throw new ClientNotFoundException("cannot retrieve client on slot " + matcher.group("slot"));
            this.log.trace("[EVENT] EVT_CLIENT_JUMP_RUN_CANCELED [ client : " + client.getSlot() + " | way : " + matcher.group("way") + " ]");
            this.eventBus.post(new ClientJumpRunCanceledEvent(client, Integer.parseInt(matcher.group("way"))));
            
        } catch (ClientNotFoundException e)  {
            
            // Logging the Exception
            this.log.error("[EVENT] EVT_CLIENT_JUMP_RUN_CANCELED", e);
            return;
        
        }
        
    }
    
    
    /**
     * Helper method for ClientJumpRunStarted
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onClientJumpRunStarted(Matcher matcher) {
        
        // 0:00 ClientJumpRunStarted: 0 - way: 2 - attempt: 1 of 5
        // 0:00 ClientJumpRunStarted: 0 - way: 2
        
        try {
            
            Client client = this.clients.getBySlot(Integer.valueOf(matcher.group("slot")));
            if (client == null) throw new ClientNotFoundException("cannot retrieve client on slot " + matcher.group("slot"));
            this.log.trace("[EVENT] EVT_CLIENT_JUMP_RUN_STARTED [ client : " + client.getSlot() + " | way : " + matcher.group("way") + " ]");
            this.eventBus.post(new ClientJumpRunStartedEvent(client, Integer.parseInt(matcher.group("way"))));
            
        } catch (ClientNotFoundException e)  {
            
            // Logging the Exception
            this.log.error("[EVENT] EVT_CLIENT_JUMP_RUN_STARTED", e);
            return;
        
        }
        
    }
    
    
    /**
     * Helper method for ClientJumpRunStopped
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onClientJumpRunStopped(Matcher matcher) {
        
        // 0:00 ClientJumpRunStopped: 0 - way: 2 - time: 4850 - attempt: 1 of 5
        // 0:00 ClientJumpRunStopped: 0 - way: 2 - time: 4850
        
        try {
            
            Client client = this.clients.getBySlot(Integer.valueOf(matcher.group("slot")));
            if (client == null) throw new ClientNotFoundException("cannot retrieve client on slot " + matcher.group("slot"));
            this.log.trace("[EVENT] EVT_CLIENT_JUMP_RUN_STOPPED [ client : " + client.getSlot() + " | way : " + matcher.group("way") + " | millis : " + matcher.group("time") + " ]");
            this.eventBus.post(new ClientJumpRunStoppedEvent(client, Integer.parseInt(matcher.group("way")), Integer.parseInt(matcher.group("time"))));
            
        } catch (ClientNotFoundException e)  {
            
            // Logging the Exception
            this.log.error("[EVENT] EVT_CLIENT_JUMP_RUN_STOPPED", e);
            return;
        
        }
        
    }
    
    
    /**
     * Helper method for ClientLoadPosition
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onClientLoadPosition(Matcher matcher) {
        
        // 0:00 ClientLoadPosition: 0 - -7558.291015 - -79.125061 - 160.125000 - "Easy Slide (N6)"
        // 0:00 ClientLoadPosition: 0 - 758.278015 - 12.125061 - 10.405003 - "Marble Courtyard"
        
        try {
            
            Client client = this.clients.getBySlot(Integer.valueOf(matcher.group("slot")));
            if (client == null) throw new ClientNotFoundException("cannot retrieve client on slot " + matcher.group("slot"));
            this.log.trace("[EVENT] EVT_CLIENT_POSITION_LOAD [ client : " + client.getSlot() + " | x : " + matcher.group("x") + " | y : " + matcher.group("y") + " | z : " + matcher.group("z") + " | location : " + matcher.group("location") + " ]");
            this.eventBus.post(new ClientPositionLoadEvent(client, Float.parseFloat(matcher.group("x")), Float.parseFloat(matcher.group("y")), Float.parseFloat(matcher.group("z")), matcher.group("location")));
            
        } catch (ClientNotFoundException e)  {
            
            // Logging the Exception
            this.log.error("[EVENT] EVT_CLIENT_POSITION_LOAD", e);
            return;
        
        }
        
    }
    
    
    /**
     * Helper method for ClientSavePosition
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onClientSavePosition(Matcher matcher) {
        
        // 0:00 ClientSavePosition: 0 - -7558.291015 - -79.125061 - 160.125000 - "Easy Slide (N6)"
        // 0:00 ClientSavePosition: 0 - 758.278015 - 12.125061 - 10.405003 - "Marble Courtyard"
        
        try {
            
            Client client = this.clients.getBySlot(Integer.valueOf(matcher.group("slot")));
            if (client == null) throw new ClientNotFoundException("Cannot retrieve client on slot " + matcher.group("slot"));
            this.log.trace("[EVENT] EVT_CLIENT_POSITION_SAVE [ client : " + client.getSlot() + " | x : " + matcher.group("x") + " | y : " + matcher.group("y") + " | z : " + matcher.group("z") + " | location : " + matcher.group("location") + " ]");
            this.eventBus.post(new ClientPositionSaveEvent(client, Float.parseFloat(matcher.group("x")), Float.parseFloat(matcher.group("y")), Float.parseFloat(matcher.group("z")), matcher.group("location")));
            
        } catch (ClientNotFoundException e)  {
            
            // Logging the Exception
            this.log.error("[EVENT] EVT_CLIENT_POSITION_SAVE", e);
            return;
        
        }
        
    }
    
    
    /**
     * Helper method for ClientUserinfo
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    @Override
    public void onClientUserinfo(Matcher matcher) { 
        
        // 0:00 ClientUserinfo: 9 \ip\93.84.143.218:27960\name\DsP**Unhitman....
        // 0:00 ClientUserinfo: 8 \ip\87.166.159.201:27960\name\SickHippster!nc....
        
        Client client = this.clients.getBySlot(Integer.valueOf(matcher.group("slot")));
        Map<String, String> userinfo = this.parseInfoString(matcher.group("infostring"));
        
        if (client == null) {
            
            Map<String, String> authinfo = null;
            
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
                
                // If the Frozen Sand auth system is enabled attempt to
                // authenticate the client using data retrieved from the auth system
                if (this.game.isAuthEnabled()) {
                    
                    // Querying Frozen Sand Auth Server to obtain client auth_login
                    authinfo = this.console.authwhois(Integer.parseInt(matcher.group("slot")));
                    
                    // Check if we got a proper auth login returned by the auth system
                    if ((authinfo != null) && (authinfo.get("login") != null)) {
                        
                        try {
                            
                            this.log.debug("Attempt to authenticate client on slot " + matcher.group("slot") + " [ auth : " + authinfo.get("login") + " ]");
                            client = this.clients.getByAuth(authinfo.get("login"));
                            
                        } catch (ClassNotFoundException | UnknownHostException | SQLException e) {
                            
                            // Logging the Exception
                            this.log.error("[EVENT] EVT_CLIENT_CONNECT", e);
                            return;
                            
                        }
                        
                        if (client != null) {
                            
                            // We got a proper client object. Double check the client guid and if it doesn't match update the guid value
                            this.log.debug("Client connecting on slot " + matcher.group("slot") + " authenticated " + client.toString());
                            
                            if (!client.getGuid().equals(userinfo.get("cl_guid"))) {
                                
                                // auth_login/cl_guid mismatch. Updating!
                                this.log.warn("Stored client GUID doesn't match retrieved userinfo GUID for client on slot " + matcher.group("slot") + " [ " + client.getGuid() + " -> " + userinfo.get("cl_guid") + " ]");
                                this.log.warn("Updating client on slot " + matcher.group("slot") + " GUID with the new value: " + userinfo.get("cl_guid"));
                                client.setGuid(userinfo.get("cl_guid"));
                            
                            }
                            
                        } else {
                            
                            try {
                                
                                // Authenticating the client using the normal cl_guid
                                this.log.debug("Unable to authenticate client on slot " + matcher.group("slot") + " [ auth : " + authinfo.get("login") + " ]");
                                this.log.debug("Attempt to authenticate client on slot " + matcher.group("slot") + " [ cl_guid : " + userinfo.get("cl_guid") + " ]");
                                client = this.clients.getByGuid(userinfo.get("cl_guid"));
                                
                                if (client != null) {
                                    // Found a match for the connecting client in the storage. Update the AUTH login value
                                    // so upon a new connection, that field will be used for the client authentication
                                    this.log.debug("Client connecting on slot " + matcher.group("slot") + " authenticated " + client.toString());
                                    this.log.debug("Updating client on slot " + matcher.group("slot") + " AUTH with the value retrieved from the Frozen Sand auth system [ auth : " + authinfo.get("login") + " ]");
                                    client.setAuth(authinfo.get("login"));
                                } else {
                                    // Unable to find a match in the storage for the connecting client
                                    // We'll handle this as a new client connection on this game server
                                    this.log.debug("No match found in the storage for client connecting on slot " + matcher.group("slot") + ". We'll be handled as a new client " + " [ cl_guid : " + userinfo.get("cl_guid") + " | auth : " + authinfo.get("login") + " ]");
                                }
                            
                            } catch (ClassNotFoundException | UnknownHostException | SQLException e) {
                                
                                // Logging the Exception
                                this.log.error("[EVENT] EVT_CLIENT_CONNECT", e);
                                return;
                                
                            }
                            
                        }

                    } else {
                        
                        if (authinfo != null) {
                            // We got a server response to the RCON command. However we were not be able to retrieve the client auth_login.
                            // Means that the server accept auth_notoriety 0 and the client is not authed using his account on urbanterror.info. 
                            // We'll backup by authenticating the client using his cl_guid.
                            this.log.debug("Client connecting on slot " + matcher.group("slot") + " is not authenticated by the Frozen Sand auth system");
                        }
                        
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

            }
            
            
            try {
                
                if (client == null) {   
                    
                    // We didn't managed to find a match in our database for the connecting client
                    // More over the connecting client is not a bot, otherwise we would have generated the object
                    // We will generate a new client object for the client and save it into our database
                    client = new Client.Builder(InetAddress.getByName(userinfo.get("ip").split(":", 2)[0]), userinfo.get("cl_guid"))
                                       .auth(((authinfo != null) && (authinfo.get("login") != null)) ? authinfo.get("login") : null)
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
     * Helper method for Radio
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onRadio(Matcher matcher) { 
        
        // 0:00 Radio: 3 - 9 - 7 - "1. Off With their Head!" - "Oh, you idiot"
        // 0:00 Radio: 6 - 5 - 1 - "Challenge: No Save" - "Enemy spotted at Challenge: No Save"
          
        try {
            
            Client client = this.clients.getBySlot(Integer.valueOf(matcher.group("slot")));
            if (client == null) throw new ClientNotFoundException("cannot retrieve client on slot " + matcher.group("slot"));
            this.log.trace("EVT_CLIENT_RADIO intercepted [ client : " + client.getSlot() + " | msg_group : " + matcher.group("group") + " | msg_id : " + matcher.group("id") + " | location : " + matcher.group("location") + " | message : " + matcher.group("message") + " ]");
            this.eventBus.post(new ClientRadioEvent(client, Integer.parseInt(matcher.group("group")), Integer.parseInt(matcher.group("id")), matcher.group("location"), matcher.group("message")));
            
        } catch (ClientNotFoundException e) {
            
            // Logging the Exception
            this.log.error("[EVENT] EVT_CLIENT_RADIO", e);
            return;
        
        }
        
    }
    
    
    /**
     * Helper method for SurvivorWinner
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    @Override
    public void onSurvivorWinner(Matcher matcher) { 
        
        // 0:00 SurvivorWinner: Red
        // 0:00 SurvivorWinner: 1
        
        if (matcher.group("data").matches("\\d+")) {
            
            try {
                
                Client client = this.clients.getBySlot(Integer.parseInt(matcher.group("data")));
                if (client == null) throw new ClientNotFoundException("cannot retrieve client on slot " + matcher.group("data"));
                this.log.trace("[EVENT] EVT_SURVIVOR_WINNER [ client : " + client.getSlot() + " ]");
                this.eventBus.post(new SurvivorWinnerEvent(client));
                
            } catch (ClientNotFoundException e) {
                
                // Logging the Exception
                this.log.error("[EVENT] EVT_SURVIVOR_WINNER", e);
                return;
            
            }
            
        } else {
            
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
          
    }
    
    
    /**
     * Helper method for Vote
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onVote(Matcher matcher) { 
        
        // 0:00 Vote: 4 - 1
        // 0:00 Vote: 3 - 2
        
        try {
            
            Client client = this.clients.getBySlot(Integer.valueOf(matcher.group("slot")));
            if (client == null) throw new ClientNotFoundException("cannot retrieve client on slot " + matcher.group("slot"));
            
            this.log.trace("[EVENT] EVT_CLIENT_VOTE [ client : " + client.getSlot() + " | data : " + matcher.group("data") + " ]");
            this.eventBus.post(new ClientVoteEvent(client, Integer.parseInt(matcher.group("data"))));
            
        } catch (ClientNotFoundException e) {
            
            // Logging the Exception
            this.log.error("[EVENT] EVT_CLIENT_VOTE", e);
            return;
        
        }
      
    }
    
    
    ////////////////////////
    // END HELPER METHODS //
    ////////////////////////
    
    
    /**
     * Parse a Urban Terror log message.
     * Will generate an <tt>Event</tt> if necessary and push it in the <tt>Event</tt> queue.
     * 
     * @author Daniele Pantaleone
     * @param  line A log line
     **/
    @Override
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
                this.log.error(e.getCause());
            
            }
            
            break;
            
        }
           
    }

}
