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
 * @copyright   Daniele Pantaleone, 12 February, 2013
 * @package     com.orion.parser
 **/

package com.orion.parser;

import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Arrays;
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

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.eventbus.EventBus;
import com.orion.bot.Orion;
import com.orion.command.Command;
import com.orion.command.Prefix;
import com.orion.console.Console;
import com.orion.control.ClientCtl;
import com.orion.control.GroupCtl;
import com.orion.domain.Callvote;
import com.orion.domain.Client;
import com.orion.domain.Group;
import com.orion.event.ClientBombDefusedEvent;
import com.orion.event.ClientBombHolderEvent;
import com.orion.event.ClientBombPlantedEvent;
import com.orion.event.ClientCallvoteEvent;
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
import com.orion.event.ClientJumpRunCanceledEvent;
import com.orion.event.ClientJumpRunStartedEvent;
import com.orion.event.ClientJumpRunStoppedEvent;
import com.orion.event.ClientKillEvent;
import com.orion.event.ClientKillSelfEvent;
import com.orion.event.ClientKillTeamEvent;
import com.orion.event.ClientNameChangeEvent;
import com.orion.event.ClientPositionLoadEvent;
import com.orion.event.ClientPositionSaveEvent;
import com.orion.event.ClientRadioEvent;
import com.orion.event.ClientSayEvent;
import com.orion.event.ClientSayPrivateEvent;
import com.orion.event.ClientSayTeamEvent;
import com.orion.event.ClientTeamChangeEvent;
import com.orion.event.ClientVoteEvent;
import com.orion.event.GameExitEvent;
import com.orion.event.GameRoundStartEvent;
import com.orion.event.GameStartEvent;
import com.orion.event.GameWarmupEvent;
import com.orion.event.SurvivorWinnerEvent;
import com.orion.event.TeamFlagReturnEvent;
import com.orion.event.TeamSurvivorWinnerEvent;
import com.orion.eventbus.OrionBus;
import com.orion.exception.ClientNotFoundException;
import com.orion.exception.ExpectedParameterException;
import com.orion.exception.ParserException;
import com.orion.exception.RconException;
import com.orion.urt.Cvar;
import com.orion.urt.Game;
import com.orion.urt.Gametype;
import com.orion.urt.Hitlocation;
import com.orion.urt.Item;
import com.orion.urt.Mod;
import com.orion.urt.Team;

public class UrT42Parser implements Parser {
    
    private static final Map<String, Pattern> patterns = new LinkedHashMap<String, Pattern>();
    private static final Map<Integer, Gametype> gametypeByCode = new HashMap<Integer, Gametype>();
    private static final Map<Integer, Hitlocation> hitlocationByCode = new HashMap<Integer, Hitlocation>();
    private static final Map<Character, Item> itemByCode = new HashMap<Character, Item>();
    private static final Map<String, Item> itemByName = new HashMap<String, Item>();
    private static final Map<Integer, Mod> modByKillCode = new HashMap<Integer, Mod>();
    private static final Map<Integer, Mod> modByHitCode = new HashMap<Integer, Mod>();
    private static final Map<Integer, Team> teamByCode = new HashMap<Integer, Team>();
    private static final Map<String, Team> teamByName = new HashMap<String, Team>();
    private static final Multimap<Gametype, Team> teamsByGametype = LinkedListMultimap.create();
    
    private final Orion orion;
    private final Console console;
    private final Log log;
    
    private final GroupCtl groupCtl;
    private final ClientCtl clientCtl;
    
    private OrionBus eventBus;
    
    private Game game;
    
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
        hitlocationByCode.put(2,  Hitlocation.HL_HELMET);
        hitlocationByCode.put(3,  Hitlocation.HL_TORSO);
        hitlocationByCode.put(4,  Hitlocation.HL_VEST);
        hitlocationByCode.put(5,  Hitlocation.HL_ARML);
        hitlocationByCode.put(6,  Hitlocation.HL_ARMR);
        hitlocationByCode.put(7,  Hitlocation.HL_GROIN);
        hitlocationByCode.put(8,  Hitlocation.HL_BUTT);
        hitlocationByCode.put(9,  Hitlocation.HL_LEGUL);
        hitlocationByCode.put(10, Hitlocation.HL_LEGUR);
        hitlocationByCode.put(11, Hitlocation.HL_LEGLL);
        hitlocationByCode.put(12, Hitlocation.HL_LEGLR);
        hitlocationByCode.put(13, Hitlocation.HL_FOOTL);
        hitlocationByCode.put(14, Hitlocation.HL_FOOTR);    
        /////////////////////////////////////
        // END LOADING URT42 HIT LOCATIONS //
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
        itemByCode.put('g', Item.UT_WP_COLT1911);
        itemByCode.put('h', Item.UT_WP_MAC11);
        
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
        itemByName.put("ut_weapon_colt1911",      Item.UT_WP_COLT1911); 
        itemByName.put("ut_weapon_mac11",         Item.UT_WP_MAC11); 
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
        modByKillCode.put(33, Mod.UT_MOD_SMITED);
        modByKillCode.put(34, Mod.UT_MOD_BOMBED);
        modByKillCode.put(35, Mod.UT_MOD_NUKED);
        modByKillCode.put(36, Mod.UT_MOD_NEGEV);
        modByKillCode.put(37, Mod.UT_MOD_HK69_HIT);
        modByKillCode.put(38, Mod.UT_MOD_M4);
        modByKillCode.put(39, Mod.UT_MOD_GLOCK);
        modByKillCode.put(40, Mod.UT_MOD_COLT1911);
        modByKillCode.put(41, Mod.UT_MOD_MAC11);
        modByKillCode.put(42, Mod.UT_MOD_FLAG);
        modByKillCode.put(43, Mod.UT_MOD_GOOMBA);
        
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
        modByHitCode.put(20, Mod.UT_MOD_GLOCK);
        modByHitCode.put(21, Mod.UT_MOD_COLT1911);
        modByHitCode.put(22, Mod.UT_MOD_MAC11);
        modByHitCode.put(24, Mod.UT_MOD_KICKED);
        modByHitCode.put(25, Mod.UT_MOD_KNIFE_THROWN);
        //////////////////////////////////////
        // END LOADING URT42 MEANS OF DEATH //
        //////////////////////////////////////
        
        
        ///////////////////////////////
        // BEGIN LOADING URT42 TEAMS //
        ///////////////////////////////     
        teamByCode.put(0, Team.FREE);
        teamByCode.put(1, Team.RED);
        teamByCode.put(2, Team.BLUE);
        teamByCode.put(3, Team.SPECTATOR);
        
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
        patterns.put("ClientJumpRunCanceled",   Pattern.compile("^\\s*\\d+:\\d+\\s?ClientJumpRunCanceled:\\s(?<slot>\\d+)\\s-\\sway:\\s(?<way>\\d+)(\\s-\\sattempt:\\s(?<anum>\\d+)\\sof\\s(?<amax>\\d+))?$", Pattern.CASE_INSENSITIVE));
        patterns.put("ClientJumpRunStarted",    Pattern.compile("^\\s*\\d+:\\d+\\s?ClientJumpRunStarted:\\s(?<slot>\\d+)\\s-\\sway:\\s(?<way>\\d+)(\\s-\\sattempt:\\s(?<anum>\\d+)\\sof\\s(?<amax>\\d+))?$", Pattern.CASE_INSENSITIVE));
        patterns.put("ClientJumpRunStopped",    Pattern.compile("^\\s*\\d+:\\d+\\s?ClientJumpRunStopped:\\s(?<slot>\\d+)\\s-\\sway:\\s(?<way>\\d+)\\s-\\stime:\\s(?<wtime>\\d+)(\\s-\\sattempt:\\s(?<anum>\\d+)\\sof\\s(?<amax>\\d+))?$", Pattern.CASE_INSENSITIVE));
        patterns.put("ClientLoadPosition",      Pattern.compile("^\\s*\\d+:\\d+\\s?ClientLoadPosition:\\s(?<slot>\\d+)\\s-\\s(?<x>-?\\d+\\.\\d+)\\s-\\s(?<y>-?\\d+\\.\\d+)\\s-\\s(?<z>-?\\d+\\.\\d+)$", Pattern.CASE_INSENSITIVE));
        patterns.put("ClientSavePosition",      Pattern.compile("^\\s*\\d+:\\d+\\s?ClientSavePosition:\\s(?<slot>\\d+)\\s-\\s(?<x>-?\\d+\\.\\d+)\\s-\\s(?<y>-?\\d+\\.\\d+)\\s-\\s(?<z>-?\\d+\\.\\d+)$", Pattern.CASE_INSENSITIVE));
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
     * @param  orion <tt>Orion</tt> object reference
     **/
    public UrT42Parser(Orion orion) {
        
        this.orion = orion;
        this.console = orion.console;
        this.log = orion.log;
        
        this.groupCtl = orion.groupCtl;
        this.clientCtl = orion.clientCtl;
        
        this.eventBus = orion.eventBus;

        this.game = orion.game;
          
        this.log.debug("Urban Terror 4.2 parser initialized");
        
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
            throw new IndexOutOfBoundsException("could not retrieve a Gametype using the given code: " + code);
        
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
            throw new IndexOutOfBoundsException("could not retrieve a Hitlocation using the given code: " + code);
        
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
            throw new IndexOutOfBoundsException("could not retrieve an Item using the given code: " + code);
        
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
            throw new IndexOutOfBoundsException("could not retrieve an Item using the given name: " + name);
        
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
            throw new IndexOutOfBoundsException("could not retrieve a Mod using the given kill code: " + code);
        
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
            throw new IndexOutOfBoundsException("could not retrieve a Mod using the given hit code: " + code);
     
        return modByHitCode.get(code);
    }
    
    
    /**
     * Return the <tt>Team</tt> matching the given code
     * 
     * @author Daniele Pantaleone
     * @param  code The <tt>Team</tt> code
     * @throws IndexOutOfBoundsException If the <tt>Team</tt> code is not mapped
     * @return The <tt>Team</tt> matching the given code
     **/
    public Team getTeamByCode(Integer code) throws IndexOutOfBoundsException {
       
        if (!teamByCode.containsKey(code)) 
            throw new IndexOutOfBoundsException("could not retrieve a Team using the given code: " + code);
        
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
            throw new IndexOutOfBoundsException("could not retrieve a Team using the given name: " + name);
        
        return teamByName.get(name);
    }
    
    
    /**
     * Return a <tt>List</tt> of available <tt>Team</tt> objects according to
     * the current played <tt>Gametype</tt>. If the <tt>Gametype</tt> has not 
     * been computed yet, it will retrieve the value from the server before 
     * returning the collection
     * 
     * @author Daniele Pantaleone
     * @throws RconException If we could not retrieve the current gametype
     * @return A <tt>List</tt> of available <tt>Team</tt> objects according to
     *         the current played <tt>Gametype</tt>
     **/
    public List<Team> getAvailableTeams() throws RconException {
        
        if (!this.game.isCvar("g_gametype"))
            this.game.setCvar(this.console.getCvar("g_gametype"));
        
        return new LinkedList<Team>(teamsByGametype.get(gametypeByCode.get(this.game.getCvar("g_gametype").getInt())));
    
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
            
            int slot = Integer.parseInt(matcher.group("slot"));
            Client client = this.clientCtl.getBySlot(slot);
            
            // Check to have a proper client object before the event generation
            checkNotNull(client, "could not retrieve client on slot %s", slot);
            
            this.eventBus.post(new ClientBombDefusedEvent(client));
            this.log.trace("[EVENT] ClientBombDefusedEvent [ client : " + client.getSlot() + " ]");
            
        } catch (NullPointerException e) {        
            
            // Logging the Exception
            this.log.error("[EVENT] ClientBombDefusedEvent", e);   
            
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
             
             int slot = Integer.parseInt(matcher.group("slot"));
             Client client = this.clientCtl.getBySlot(slot);
            
             // Check to have a proper client object before the event generation
             checkNotNull(client, "could not retrieve client on slot %s", slot);
            
             this.eventBus.post(new ClientBombHolderEvent(client));
             this.log.trace("[EVENT] ClientBombHolderEvent [ client : " + client.getSlot() + " ]");
            
         } catch (NullPointerException e) {            
            
             // Logging the Exception
             this.log.error("[EVENT] ClientBombHolderEvent", e);
            
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
            
            int slot = Integer.parseInt(matcher.group("slot"));
            Client client = this.clientCtl.getBySlot(slot);
            
            // Check to have a proper client object before the event generation
            checkNotNull(client, "could not retrieve client on slot %s", slot);
            
            this.eventBus.post(new ClientBombPlantedEvent(client));
            this.log.trace("[EVENT] ClientBombPlantedEvent [ client : " + client.getSlot() + " ]");
            
        } catch (NullPointerException e) {
            
            // Logging the Exception
            this.log.error("[EVENT] ClientBombPlantedEvent", e);
            
        }
        
    }
    
    
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
            
            int slot = Integer.parseInt(matcher.group("slot"));
            Client client = this.clientCtl.getBySlot(slot);
            
            // Check to have a proper client object before the event generation
            checkNotNull(client, "could not retrieve client on slot %s", slot);
            
            String type = matcher.group("type");
            String data = matcher.group("data") != null && !matcher.group("data").isEmpty() ? matcher.group("data") : null;
            
            // Check to have at least the callvote type
            checkNotNull(type, "could not retrieve callvote type");
            
            Callvote callvote = new Callvote.Builder(client, type)
                                            .data(data)
                                            .build();
            
            this.eventBus.post(new ClientCallvoteEvent(client, callvote));
            this.log.trace("[EVENT] ClientCallvoteEvent [ client : " + client.getSlot() + " | type : " + callvote.getType() + " | data : " + callvote.getData() + " ]");
            
        } catch (NullPointerException e) {
            
            // Logging the Exception
            this.log.error("[EVENT] ClientCallvoteEvent", e);
        
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
            
            int slot = Integer.parseInt(matcher.group("slot"));
            Client client = this.clientCtl.getBySlot(slot);
            
            // Check to have a proper client object before the event generation
            checkNotNull(client, "could not retrieve client on slot %s", slot);
            
            this.eventBus.post(new ClientJoinEvent(client));
            this.log.trace("[EVENT] ClientJoinEvent [ client : " + client.getSlot() + " ]");
            
        } catch (NullPointerException e)  {
            
            // Logging the Exception
            this.log.error("[EVENT] ClientJoinEvent", e);
        
        }
        
    }
    
    /**
     * Helper method for Say (matching command pattern)
     * 
     * @author Daniele Pantaleone
     * @param  client The <tt>Client</tt> who performed the command
     * @param  message The command string
     **/
    public void onClientCommand(Client client, String message) {
        
        if (message.substring(0, 2).equals("!!")) {
            // Ugly hack to make !! an alias for !say
            // Used for b3 command syntax compatibility
            message = "!say "+ (message.substring(2));
        }
        
        // Prefix prefix = Prefix.getByChar(message.charAt(0));
        // String data[] = message.substring(1).split(" ", 2);  
        // String handle = data[0].toLowerCase();
        // String params = data.length > 1 ? data[1].toLowerCase() : null;
        
        // Command command = new Command(client, prefix, handle, params);
        
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
        
        int slot = Integer.parseInt(matcher.group("slot"));
        this.log.debug("Client connecting on slot "+ slot +". Ready to parse ClientUserinfo line...");

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
            
            int slot = Integer.parseInt(matcher.group("slot"));
            Client client = this.clientCtl.removeBySlot(slot);
            
            // Check to have a proper client object before the event generation
            checkNotNull(client, "could not retrieve client on slot %s", slot);
            
            this.eventBus.post(new ClientDisconnectEvent(client));
            this.log.trace("[EVENT] ClientDisconnectEvent [ client : " + client.getSlot() + " ]");
            
        } catch (NullPointerException e)  {
            
            // Logging the Exception
            this.log.error("[EVENT] ClientDisconnectEvent", e);
        
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
            
            int slot = Integer.parseInt(matcher.group("slot"));
            Client client = this.clientCtl.getBySlot(slot);
            
            // Check to have a proper client object before the event generation
            checkNotNull(client, "could not retrieve client on slot %s", slot);
            
            int way = Integer.parseInt(matcher.group("way"));
            Integer anum = Integer.valueOf(matcher.group("anum"));
            Integer amax = Integer.valueOf(matcher.group("amax"));
            
           if (anum != null && amax != null) {
               this.eventBus.post(new ClientJumpRunCanceledEvent(client, way, anum, amax));
               this.log.trace("[EVENT] ClientJumpRunCanceledEvent [ client : " + client.getSlot() + 
                                                                " | way : " + way + 
                                                                " | attempt_num : " + anum + 
                                                                " | attempt_max : " + amax + " ]");
           } else {
               this.eventBus.post(new ClientJumpRunCanceledEvent(client, way));
               this.log.trace("[EVENT] ClientJumpRunCanceledEvent [ client : " + client.getSlot() + 
                                                                " | way : " + way + " ]");
           }
            
        } catch (NullPointerException e)  {
            
            // Logging the Exception
            this.log.error("[EVENT] ClientJumpRunCanceledEvent", e);
        
        }
        
    }
    
    
    /**
     * Helper method for ClientJumpRunStarted
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onClientJumpRunStarted(Matcher matcher) {
       
        // 0:00 ClientJumpRunStarted: 0 - way: 2
        // 0:00 ClientJumpRunStarted: 0 - way: 2 - attempt: 1 of 5
        
        try {
            
            int slot = Integer.parseInt(matcher.group("slot"));
            Client client = this.clientCtl.getBySlot(slot);
            
            // Check to have a proper client object before the event generation
            checkNotNull(client, "could not retrieve client on slot %s", slot);
            
            int way = Integer.parseInt(matcher.group("way"));
            Integer anum = Integer.valueOf(matcher.group("anum"));
            Integer amax = Integer.valueOf(matcher.group("amax"));
            
           if (anum != null && amax != null) {
               this.eventBus.post(new ClientJumpRunStartedEvent(client, way, anum, amax));
               this.log.trace("[EVENT] ClientJumpRunStartedEvent [ client : " + client.getSlot() + 
                                                               " | way : " + way + 
                                                               " | attempt_num : " + anum + 
                                                               " | attempt_max : " + amax + " ]");
           } else {
               this.eventBus.post(new ClientJumpRunStartedEvent(client, way));
               this.log.trace("[EVENT] ClientJumpRunStartedEvent [ client : " + client.getSlot() + 
                                                               " | way : " + way + " ]");
           }
            
        } catch (NullPointerException e)  {
            
            // Logging the Exception
            this.log.error("[EVENT] ClientJumpRunStartedEvent", e);
        
        }
        
    }
    
    
    /**
     * Helper method for ClientJumpRunStopped
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onClientJumpRunStopped(Matcher matcher) {
        
        // 0:00 ClientJumpRunStopped: 0 - way: 2 - time: 4850
        // 0:00 ClientJumpRunStopped: 0 - way: 2 - time: 4850 - attempt: 1 of 5
        
        try {
            
            int slot = Integer.parseInt(matcher.group("slot"));
            Client client = this.clientCtl.getBySlot(slot);
            
            // Check to have a proper client object before the event generation
            checkNotNull(client, "could not retrieve client on slot %s", slot);
            
            int way = Integer.parseInt(matcher.group("way"));
            int wtime = Integer.parseInt(matcher.group("wtime"));
            Integer anum = Integer.valueOf(matcher.group("anum"));
            Integer amax = Integer.valueOf(matcher.group("amax"));
            
           if (anum != null && amax != null) {
               this.eventBus.post(new ClientJumpRunStoppedEvent(client, way, wtime, anum, amax));
               this.log.trace("[EVENT] ClientJumpRunStoppedEvent [ client : " + client.getSlot() + 
                                                               " | way : " + way + 
                                                               " | way_time : " + wtime +
                                                               " | attempt_num : " + anum + 
                                                               " | attempt_max : " + amax + " ]");
           } else {
               this.eventBus.post(new ClientJumpRunStoppedEvent(client, way, wtime));
               this.log.trace("[EVENT] ClientJumpRunStoppedEvent [ client : " + client.getSlot() + 
                                                               " | way : " + way + 
                                                               " | way_time : " + wtime +" ]");
           }
            
        } catch (NullPointerException e)  {
            
            // Logging the Exception
            this.log.error("[EVENT] ClientJumpRunStoppedEvent", e);
        
        }
        
    }
    
    
    /**
     * Helper method for ClientLoadPosition
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onClientLoadPosition(Matcher matcher) {
        
        // 0:00 ClientLoadPosition: 0 - -7558.291015 - -79.125061 - 160.125000
        // 0:00 ClientLoadPosition: 0 - 758.278015 - 12.125061 - 10.405003
        
        try {
            
            int slot = Integer.parseInt(matcher.group("slot"));
            Client client = this.clientCtl.getBySlot(slot);
            
            // Check to have a proper client object before the event generation
            checkNotNull(client, "could not retrieve client on slot %s", slot);
            
            float x = Float.parseFloat(matcher.group("x"));
            float y = Float.parseFloat(matcher.group("y"));
            float z = Float.parseFloat(matcher.group("z"));
            
            this.eventBus.post(new ClientPositionLoadEvent(client, x, y, z));
            this.log.trace("[EVENT] ClientPositionLoadEvent [ client : " + client.getSlot() + 
                                                          " | x : " + x + 
                                                          " | y : " + y + 
                                                          " | z : " + z + " ]");
            
            
        } catch (NullPointerException e)  {
            
            // Logging the Exception
            this.log.error("[EVENT] ClientPositionLoadEvent", e);
        
        }
        
    }
    
    
    /**
     * Helper method for ClientSavePosition
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onClientSavePosition(Matcher matcher) {
        
        // 0:00 ClientSavePosition: 0 - -7558.291015 - -79.125061 - 160.125000
        // 0:00 ClientSavePosition: 0 - 758.278015 - 12.125061 - 10.405003
        
        try {
            
            int slot = Integer.parseInt(matcher.group("slot"));
            Client client = this.clientCtl.getBySlot(slot);
            
            // Check to have a proper client object before the event generation
            checkNotNull(client, "could not retrieve client on slot %s", slot);
            
            float x = Float.parseFloat(matcher.group("x"));
            float y = Float.parseFloat(matcher.group("y"));
            float z = Float.parseFloat(matcher.group("z"));
            
            this.eventBus.post(new ClientPositionSaveEvent(client, x, y, z));
            this.log.trace("[EVENT] ClientPositionSaveEvent [ client : " + client.getSlot() + 
                                                          " | x : " + x + 
                                                          " | y : " + y + 
                                                          " | z : " + z + " ]");
            
            
        } catch (NullPointerException e)  {
            
            // Logging the Exception
            this.log.error("[EVENT] ClientPositionSaveEvent", e);
        
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
        
        int slot = Integer.parseInt(matcher.group("slot"));
        Map<String, String> userinfo = this.parseInfoString(matcher.group("infostring"));
        
        try {
            
            // Check if this is a new client connection
            Client client = checkNotNull(this.clientCtl.getBySlot(slot));
           
            // Checking for possible gear change
            if (userinfo.containsKey("gear")) {
                
                String gear = userinfo.get("gear");
                if ((client.getGear() == null) || (!client.getGear().equals(gear))) {
                    client.setGear(gear);
                    this.eventBus.post(new ClientGearChangeEvent(client));
                    this.log.trace("[EVENT] ClientGearChangeEvent [ client : " + client.getSlot() + " ]");
                }
            
            }
            
        } catch (NullPointerException e1) {
            
            Client client = null;
            Map<String, String> authinfo = null;
            
            if (!(userinfo.containsKey("cl_guid")) && (userinfo.containsKey("skill"))) {
                
                try {
                    
                    // A bot is connecting to the server. We'll handle this in a different way
                    client = new Client.Builder(InetAddress.getByName("0.0.0.0"), "BOT_" + slot)
                                       .bot(true)
                                       .build();
                    
                    this.log.debug("Client connecting on slot " + slot + " has been detected as a BOT");
                    
                } catch (UnknownHostException e) {
                    
                    // Logging the Exception
                    this.log.error("[EVENT] ClientConnect", e);
                    return;
                
                }
                
            } else {
            
                try {
                    
                    // Trying to auth the client using 
                    // the Frozen Sand auth login at first
                    authinfo = this.console.authwhois(slot);
                    client = this.authByFsa(slot, userinfo, authinfo); 
                    
                } catch (UnsupportedOperationException | 
                         RconException | 
                         ParserException |
                         NullPointerException e2) {
                    
                    // Client info couldn't be retrieved using FSA. Possible reason will led here: auth system
                    // disabled, client not authed, client authed but it is his first connection, RCON connection
                    // failure. In all this cases we'll try to backup using the client GUID
                    this.log.debug("Could not authenticate client connecting on slot " + slot + " using FSA", e2);
                    
                    try {
                        
                        // Trying to auth the client using the GUID
                        client = this.authByGuid(slot, userinfo, authinfo);
                        
                    } catch (NullPointerException e3) {
                        
                        // Client info couldn't be retrieved using the GUID. At this point we can't do more
                        // We'll consider this connection as first one and we'll build the necessary structures
                        this.log.debug("Could not authenticate client connecting on slot " + slot + " using GUID", e2);
                        this.log.debug("Client connecting on slot " + slot + " will be handled as a new client...");
                    
                    }
                
                }
     
            }
            
           
            try {
                
                if (client == null) {   
                    
                    String guid = userinfo.get("g_guid");
                    String auth = authinfo != null && authinfo.get("login") != null ? authinfo.get("login") : null;
                    InetAddress ip = InetAddress.getByName(userinfo.get("ip").split(":", 2)[0]);
                    Group group = this.groupCtl.getByKeyword("guest");
                    
                    // Building a new client object using retrieved information 
                    client = new Client.Builder(ip, guid).auth(auth).group(group).build();
                    
                }
                
                client.setSlot(slot);
                
                if (userinfo.containsKey("name"))
                    client.setName(userinfo.get("name"));
     
                if (userinfo.containsKey("gear"))
                    client.setGear(userinfo.get("gear"));
                
                if (userinfo.containsKey("team"))    
                    client.setTeam(getTeamByName(userinfo.get("team")));
                
                // Update the client number of connections 
                // just if the client is a new client or if
                // he last connected more than one hour ago
                if ((client.getTimeEdit() == null) || 
                    (Hours.hoursBetween(client.getTimeEdit(), new DateTime()).getHours() > 1)) {
                    client.setConnections(client.getConnections() + 1);
                } 

                this.clientCtl.add(client);
                this.clientCtl.save(client);
                
                this.eventBus.post(new ClientConnectEvent(client));
                this.log.trace("[EVENT] ClientConnectEvent [ client : " + slot + " ]");
                
            } catch (UnknownHostException | ClassNotFoundException | SQLException e) {
                
                // Logging the Exception
                this.log.error("[EVENT] ClientConnectEvent", e);

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
            
            int slot = Integer.parseInt(matcher.group("slot"));
            Client client = this.clientCtl.getBySlot(slot);
            
            // Check to have a proper client object before the event generation
            checkNotNull(client, "could not retrieve client on slot %s", slot);
            
            // Parsing userinfo string so we can generate our custom events
            Map<String, String> userinfo = this.parseInfoString(matcher.group("infostring"));
            
            // Checking name change
            if (userinfo.containsKey("n")) {
                
                String name = userinfo.get("n").replaceAll("\\^[0-9]{1}", "");
                if (!client.getName().toLowerCase().equals(name.toLowerCase())) {
                    client.setName(name);
                    this.eventBus.post(new ClientNameChangeEvent(client));
                    this.log.trace("[EVENT] ClientNameChangeEvent [ client : " + client.getSlot() + " ]");
                }
            }
            
            // Checking team change
            if (userinfo.containsKey("t")) {
                   
                try {
                    
                    Team team = this.getTeamByCode(Integer.parseInt(userinfo.get("t")));
                    
                    if (client.getTeam() != team) {
                        client.setTeam(team);
                        this.eventBus.post(new ClientTeamChangeEvent(client));
                        this.log.trace("[EVENT] ClientTeamChangeEvent [ client : " + client.getSlot() + " ]");
                    }
                    
                } catch (IndexOutOfBoundsException e) {
                    
                    // Logging the Exception
                    this.log.error("[EVENT] ClientTeamChangeEvent", e);
                
                }
                
            }
            
        } catch (NullPointerException e) {
            
            // Logging the Exception
            this.log.error("[EVENT] ClientNameChangeEvent | ClientTeamChangeEvent", e);
            
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
            
            int slot = Integer.parseInt(matcher.group("slot"));
            int action = Integer.parseInt(matcher.group("action"));
            
            Client client = this.clientCtl.getBySlot(slot);
           
            switch (action) {
            
                case 0:
                    checkNotNull(client, "[EVENT] ClientFlagDroppedEvent: could not retrieve client on slot %s", slot);
                    this.eventBus.post(new ClientFlagDroppedEvent(client));
                    this.log.trace("[EVENT] ClientFlagDroppedEvent [ client : " + client.getSlot() + " ]");
                    break;
                case 1:
                    checkNotNull(client, "[EVENT] ClientFlagReturnedEvent: could not retrieve client on slot %s", slot);
                    this.eventBus.post(new ClientFlagReturnedEvent(client));
                    this.log.trace("[EVENT] ClientFlagReturnedEvent [ client : " + client.getSlot() + " ]");
                    break;
                case 2:
                    checkNotNull(client, "[EVENT] ClientFlagCapturedEvent: could not retrieve client on slot %s", slot);
                    this.eventBus.post(new ClientFlagCapturedEvent(client));
                    this.log.trace("[EVENT] ClientFlagCapturedEvent [ client : " + client.getSlot() + " ]");
                    break;
            
            
            
            }
        
        } catch (NullPointerException e) {
           
            // Logging the Exception
            this.log.error(e.getMessage());
        
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
            
            Team team = getTeamByName(matcher.group("team"));
            this.eventBus.post(new TeamFlagReturnEvent(team));
            this.log.trace("[EVENT] TeamFlagReturnEvent [ team : " + team.name() + " ]");
        
        } catch (IndexOutOfBoundsException e) {
            
            // Logging the Exception
            this.log.error("[EVENT] TeamFlagReturnEvent", e);
        
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
            
            int vslot = Integer.parseInt(matcher.group("victim"));
            int aslot = Integer.parseInt(matcher.group("attacker"));
            int hcode = Integer.parseInt(matcher.group("hitlocation"));
            int wcode = Integer.parseInt(matcher.group("weapon"));
            
            Client vclient = this.clientCtl.getBySlot(vslot);
            Client aclient = this.clientCtl.getBySlot(aslot);
            Hitlocation hitloc = this.getHitlocationByCode(hcode);
            Mod mod = this.getModByHitCode(wcode);
            
            if (aclient == vclient) {
                
                checkNotNull(vclient, "[EVENT] ClientDamageSelfEvent: could not retrieve victim client on slot %s", vslot);
                this.eventBus.post(new ClientDamageSelfEvent(vclient, mod, hitloc));
                this.log.trace("[EVENT] ClientDamageSelfEvent [ client : " + vclient.getSlot() + 
                                                            " | mod : " + mod.name() + 
                                                            " | hitlocation : " + hitloc.name() + " ]");
                
            } else if ((aclient.getTeam() == vclient.getTeam()) && (aclient.getTeam() != Team.SPECTATOR) && (aclient.getTeam() != Team.FREE)) {
                
                checkNotNull(vclient, "[EVENT] ClientDamageTeamEvent: could not retrieve victim client on slot %s", vslot);
                checkNotNull(aclient, "[EVENT] ClientDamageTeamEvent: could not retrieve attacker client on slot %s", vslot);
                this.eventBus.post(new ClientDamageTeamEvent(aclient, vclient, mod, hitloc));
                this.log.trace("[EVENT] ClientDamageTeamEvent [ attacker : " + aclient.getSlot() + 
                                                            " | victim : " + vclient.getSlot() + 
                                                            " | mod : " + mod.name() + 
                                                            " | hitlocation : " + hitloc.name() + " ]");
                
                
            } else {
                
                checkNotNull(vclient, "[EVENT] ClientDamageEvent: could not retrieve victim client on slot %s", vslot);
                checkNotNull(aclient, "[EVENT] ClientDamageEvent: could not retrieve attacker client on slot %s", vslot);
                this.eventBus.post(new ClientDamageEvent(aclient, vclient, mod, hitloc));
                this.log.trace("[EVENT] ClientDamageEvent [ attacker : " + aclient.getSlot() + 
                                                        " | victim : " + vclient.getSlot() + 
                                                        " | mod : " + mod.name() + 
                                                        " | hitlocation : " + hitloc.name() + " ]");
                
                
            }
            
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            
            // Logging the Exception
            this.log.error(e.getMessage());
        
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
        String infostring = matcher.group("infostring");
        Map<String, String> data = this.parseInfoString(infostring); 
        
        // Update the CVAR list with new values
        for (Map.Entry<String, String> entry : data.entrySet())
            this.game.setCvar(entry.getKey(), entry.getValue());
        
        this.eventBus.post(new GameStartEvent());
        this.log.trace("[EVENT] GameStartEvent [ data : " + infostring + " ]");       
            
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
        String infostring = matcher.group("infostring");
        Map<String, String> data = this.parseInfoString(infostring); 
        
        // Update the CVAR list with new values
        for (Map.Entry<String, String> entry : data.entrySet())
            this.game.setCvar(entry.getKey(), entry.getValue());
        
        this.eventBus.post(new GameRoundStartEvent());
        this.log.trace("[EVENT] GameRoundStartEvent [ data : " + infostring + " ]");

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
            
            int slot = Integer.parseInt(matcher.group("slot"));
            Client client = this.clientCtl.removeBySlot(slot);
            
            // Check to have a proper client object before the event generation
            checkNotNull(client, "could not retrieve client on slot %s", slot);
            
            Item item = getItemByName(matcher.group("item"));
            this.eventBus.post(new ClientItemPickupEvent(client, item));
            this.log.trace("[EVENT] ClientItemPickupEvent [ client : " + client.getSlot() + " | item : " + item.name() + " ]");
            
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            
            // Logging the Exception
            this.log.error("[EVENT] ClientItemPickupEvent", e);
            
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
            
            int vslot = Integer.parseInt(matcher.group("victim"));
            int aslot = Integer.parseInt(matcher.group("attacker"));
            int wcode = Integer.parseInt(matcher.group("weapon"));
            
            Client vclient = this.clientCtl.getBySlot(vslot);
            Client aclient = this.clientCtl.getBySlot(aslot);
            
            checkNotNull(aclient, "could not retrieve attacker client on slot %s", aslot);
            checkNotNull(vclient, "could not retrieve victim client on slot %s", vslot);
            
            Mod mod = this.getModByKillCode(wcode);

            switch (mod) {
            
                case MOD_CHANGE_TEAM:
                    // Team change is detected as a client kill
                    // However since it's not a proper player death
                    // we'll handle it somewhere else and exit here
                    return;
            
                case MOD_WATER:
                case MOD_SLIME:
                case MOD_LAVA:
                case MOD_CRUSH:
                case MOD_FALLING:
                case MOD_SUICIDE:
                case MOD_TRIGGER_HURT:
                case UT_MOD_SPLODED:
                    
                    this.eventBus.post(new ClientKillSelfEvent(vclient, mod));
                    this.log.trace("[EVENT] ClientKillSelfEvent [ victim : " + vclient.getSlot() + 
                                                              " | mod : " + mod.name() + " ]");
                    
                    break;
    
                default:
                    
                    if ((aclient == vclient) && (aclient.getTeam() != Team.SPECTATOR)) {
                        
                        this.eventBus.post(new ClientKillSelfEvent(vclient, mod));
                        this.log.trace("[EVENT] ClientKillSelfEvent [ victim : " + vclient.getSlot() + 
                                                                  " | mod : " + mod.name() + " ]");
                        
                    } else if ((aclient.getTeam() == vclient.getTeam()) && (aclient.getTeam() != Team.SPECTATOR) && (aclient.getTeam() != Team.FREE)) {
                        
                        this.eventBus.post(new ClientKillTeamEvent(aclient, vclient, mod));
                        this.log.trace("[EVENT] ClientKillTeamEvent [ victim : " + vclient.getSlot() + 
                                                                  " | attacker : " + aclient.getSlot() + 
                                                                  " | mod : " + mod.name() + " ]");
                        
                    } else {
                        
                        this.eventBus.post(new ClientKillEvent(aclient, vclient, mod));
                        this.log.trace("[EVENT] ClientKillEvent [ victim : " + vclient.getSlot() + 
                                                              " | attacker : " + aclient.getSlot() + 
                                                              " | mod : " + mod.name() + " ]");
                    
                    }
                    
                    break;
           
            }
            
            
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            
            // Logging the Exception
            this.log.error("[EVENT] ClientKillSelfEvent | ClientKillTeamEvent | ClientKillEvent", e);
            
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
            
            int slot = Integer.parseInt(matcher.group("slot"));
            Client client = this.clientCtl.removeBySlot(slot);
            
            // Check to have a proper client object before the event generation
            checkNotNull(client, "could not retrieve client on slot %s", slot);
            
            int msg_group = Integer.parseInt(matcher.group("group"));
            int msg_id = Integer.parseInt(matcher.group("id"));
            String location = matcher.group("location");
            String message = matcher.group("message");
            
            this.eventBus.post(new ClientRadioEvent(client, msg_group, msg_id, location, message));
            this.log.trace("[EVENT] ClientRadioEvent [ client : " + client.getSlot() + 
                                                   " | msg_group : " + msg_group + 
                                                   " | msg_id : " + msg_id + 
                                                   " | location : " + location + 
                                                   " | message : " + message + " ]");
            
        } catch (NullPointerException e) {
            
            // Logging the Exception
            this.log.error("[EVENT] ClientRadioEvent", e);
        
        }
        
    }
    
    
    /**
     * Helper method for Say
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onSay(Matcher matcher) {
        
        // 0:00 say: 8 denzel: lol
        // 0:00 say: 9 .:MS-T:.BstPL: this name is quite a challenge
        
        try {
            
            int slot = Integer.parseInt(matcher.group("slot"));
            Client client = this.clientCtl.removeBySlot(slot);
            
            if ((client == null) || (!client.getName().equals(matcher.group("name")))) {
                
                // Well known UrT bug          
                List<Client> collection = this.clientCtl.getByName(matcher.group("name"));
                
                if (collection.isEmpty() || collection.size() > 1)
                    throw new NullPointerException("could not retrieve client on slot " + slot);

                // Get the client
                client = collection.get(0);   
            
            }
            
            // Get the said sentence
            String message = matcher.group("message").trim();
            
            if (message == null || message.isEmpty()) 
                throw new NullPointerException("could not retrieve message");
            
            // Check if an Orion command has been issued
            if ((message.startsWith("!")) || (message.startsWith("@")) || (message.startsWith("&"))) {
            
                // Say event matches command pattern
                this.onClientCommand(client, message);
            
            } else {
                
                // Normal client say event
                this.eventBus.post(new ClientSayEvent(client, message));
                this.log.trace("[EVENT] ClientSayEvent [ client : " + client.getSlot() + 
                                                     " | message : " + message + " ]");
            
            }
            
        } catch (NullPointerException | ExpectedParameterException e) {
            
            // Logging the Exception
            this.log.error("[EVENT] ClientSayEvent", e);
        
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
            
            int cslot = Integer.parseInt(matcher.group("slot"));
            int tslot = Integer.parseInt(matcher.group("target"));
            Client client = this.clientCtl.removeBySlot(cslot);
            Client target = this.clientCtl.removeBySlot(tslot);
            
            if ((client == null) || (!client.getName().equals(matcher.group("name")))) {
                
                // Well known UrT bug          
                List<Client> collection = this.clientCtl.getByName(matcher.group("name"));
                
                if (collection.isEmpty() || collection.size() > 1)
                    throw new NullPointerException("could not retrieve client on slot " + cslot);

                // Get the client
                client = collection.get(0);   
            
            }
            
            // Check to have a proper client object before the event generation
            checkNotNull(client, "could not retrieve target client on slot %s", tslot);
            
            // Get the said sentence
            String message = matcher.group("message").trim();
            
            if (message == null || message.isEmpty()) 
                throw new NullPointerException("could not retrieve message");
            
            // Check if an Orion command has been issued
            if ((message.startsWith("!")) || (message.startsWith("@")) || (message.startsWith("&"))) {
            
                // Say event matches command pattern
                this.onClientCommand(client, message);
            
            } else {
                
                // Normal client say private event
                this.eventBus.post(new ClientSayPrivateEvent(client, target, message));
                this.log.trace("[EVENT] ClientSayPrivateEvent [ client : " + client.getSlot() + 
                                                            " | target : " + target.getSlot() + 
                                                            " | message : " + message + " ]");
            
            }
            
        } catch (NullPointerException | ExpectedParameterException e) {
            
            // Logging the Exception
            this.log.error("[EVENT] ClientSayPrivateEvent", e);
        
        }
        
    }
    
    
    /**
     * Helper method for SayTeam
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onSayTeam(Matcher matcher) {
        
        // 0:00 sayteam: 8 denzel: lol
        // 0:00 sayteam: 9 .:MS-T:.BstPL: this name is quite a challenge
        
        try {
            
            int slot = Integer.parseInt(matcher.group("slot"));
            Client client = this.clientCtl.removeBySlot(slot);
            
            if ((client == null) || (!client.getName().equals(matcher.group("name")))) {
                
                // Well known UrT bug          
                List<Client> collection = this.clientCtl.getByName(matcher.group("name"));
                
                if (collection.isEmpty() || collection.size() > 1)
                    throw new NullPointerException("could not retrieve client on slot " + slot);

                // Get the client
                client = collection.get(0);   
            
            }
            
            // Get the said sentence
            String message = matcher.group("message").trim();
            
            if (message == null || message.isEmpty()) 
                throw new NullPointerException("could not retrieve message");
            
            // Check if an Orion command has been issued
            if ((message.startsWith("!")) || (message.startsWith("@")) || (message.startsWith("&"))) {
            
                // Say event matches command pattern
                this.onClientCommand(client, message);
            
            } else {
                
                // Normal client say team event
                this.eventBus.post(new ClientSayTeamEvent(client, message));
                this.log.trace("[EVENT] ClientSayTeamEvent [ client : " + client.getSlot() + 
                                                         " | message : " + message + " ]");
            
            }
            
        } catch (NullPointerException | ExpectedParameterException e) {
            
            // Logging the Exception
            this.log.error("[EVENT] ClientSayTeamEvent", e);
        
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
        
        this.game.getMapList().clear();
        this.eventBus.post(new GameExitEvent());
        this.log.trace("[EVENT] GameExitEvent");
    
    }
    
    
    /**
     * Helper method for SurvivorWinner
     * 
     * @author Daniele Pantaleone
     * @param  matcher A <tt>Matcher</tt> object matching the log line
     **/
    public void onSurvivorWinner(Matcher matcher) { 
        
        // 0:00 SurvivorWinner: Red
        // 0:00 SurvivorWinner: 1
        
        if (matcher.group("data").matches("\\d+")) {
            
            try {
                
                int slot = Integer.parseInt(matcher.group("data"));
                Client client = this.clientCtl.getBySlot(slot);
                
                // Check to have a proper client object before the event generation
                checkNotNull(client, "could not retrieve client on slot %s", slot);
                
                this.eventBus.post(new SurvivorWinnerEvent(client));
                this.log.trace("[EVENT] SurvivorWinnerEvent [ client : " + client.getSlot() + " ]");
                
            } catch (NullPointerException e) {
                
                // Logging the Exception
                this.log.error("[EVENT] SurvivorWinnerEvent", e);

            }
            
        } else {
            
            try {
                
                Team team = this.getTeamByName(matcher.group("data"));
                this.eventBus.post(new TeamSurvivorWinnerEvent(team));
                this.log.trace("[EVENT] TeamSurvivorWinnerEvent [ team : " + team.name() + " ]");
            
            } catch (IndexOutOfBoundsException e) {
                    
                // Logging the Exception
                this.log.error("[EVENT] TeamSurvivorWinnerEvent", e);
                
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
            
            int slot = Integer.parseInt(matcher.group("data"));
            int data = Integer.parseInt(matcher.group("data"));
            Client client = this.clientCtl.getBySlot(slot);
            
            // Check to have a proper client object before the event generation
            checkNotNull(client, "could not retrieve client on slot %s", slot);
            
            this.eventBus.post(new ClientVoteEvent(client, data));
            this.log.trace("[EVENT] ClientVoteEvent [ client : " + client.getSlot() + " | data : " + data + " ]");
            
        } catch (NullPointerException e) {
            
            // Logging the Exception
            this.log.error("[EVENT] ClientVoteEvent", e);
        
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
        
        this.eventBus.post(new GameWarmupEvent());
        this.log.trace("[EVENT] GameWarmupEvent");
        
    }
    
    
    ////////////////////////
    // END HELPER METHODS //
    ////////////////////////
    
    
    /**
     * Return an initialized <tt>Client</tt> object<br>
     * Will fetch <tt>Client</tt> informations from the storage using the
     * FS auth system login and eventually perform necessary updates
     * 
     * @author Daniele Pantaleone
     * @param  slot The connecting <tt>Client</tt> slot
     * @param  userinfo The parsed <tt>Client</tt> userinfo
     * @param  authinfo The parsed <tt>Client</tt> auth data
     * @throws NullPointerException If the <tt>Client</tt> coudln't be retrieved from the storage using the FSA
     * @return An initialized <tt>Client</tt> object
     **/
    public Client authByFsa(int slot, Map<String, String> userinfo, Map<String, String> authinfo) throws NullPointerException {
        
        Client client = null;
        String login = authinfo.get("login");
        
        try {
           
            client = this.clientCtl.getByAuth(login);
            checkNotNull(client, "no match found in the storage for FSA: %s", login);
            
            this.log.debug("Client connecting on slot " + slot + "authenticated using FSA: " + login);
            this.log.trace("Welcome back " + client.getName() + " [@" + client.getId() + "] - Level: " + 
                            client.getGroup().getName() + " [" + client.getGroup().getLevel() + "]");
            
            // Check client GUID consistency
            String guid = userinfo.get("cl_guid");
            if (!client.getGuid().equals(guid)) {
                this.log.debug("Detected GUID mismatch for client @" + client.getId() + ": " + client.getGuid() + " != " + guid);
                this.log.debug("Updating client @" + client.getId() + " GUID value with the new one: " + guid);
                client.setGuid(guid);   
            }
            
            return client;
            
        } catch (ClassNotFoundException | UnknownHostException | SQLException e) {
            
            this.log.error("Could not fetch data from the storage layer", e);
            throw new NullPointerException("could not retrieve client data using FSA: " + login);
            
        }

    }
    
    
   /**
    * Return an initialized <tt>Client</tt> object<br>
    * Will fetch <tt>Client</tt> informations from the storage using the
    * GUID and eventually perform necessary updates
    * 
    * @author Daniele Pantaleone
    * @param  slot The connecting <tt>Client</tt> slot
    * @param  userinfo The parsed <tt>Client</tt> userinfo
    * @param  authinfo The parsed <tt>Client</tt> auth data
    * @throws NullPointerException If the <tt>Client</tt> coudln't be retrieved from the storage using the GUID
    * @return An initialized <tt>Client</tt> object
    **/
   public Client authByGuid(int slot, Map<String, String> userinfo, Map<String, String> authinfo) throws NullPointerException {
       
       Client client = null;
       String guid = userinfo.get("cl_guid");
       
       try {
           
           client = this.clientCtl.getByGuid(guid);
           checkNotNull(client, "no match found in the storage for GUID: %s", guid);
           
           this.log.debug("Client connecting on slot " + slot + "authenticated using GUID: " + guid);
           this.log.trace("Welcome back " + client.getName() + " [@" + client.getId() + "] - Level: " + 
                           client.getGroup().getName() + " [" + client.getGroup().getLevel() + "]");
           
           // Update auth field if necessary
           if ((authinfo != null) && (authinfo.get("login") != null)) {
               String login = authinfo.get("login");
               this.log.debug("Updating client @" + client.getId() + " AUTH login using FSA: " + login);
               client.setAuth(login);
           }
           
           return client;
           
       } catch (ClassNotFoundException | UnknownHostException | SQLException e) {
           
           this.log.error("Could not fetch data from the storage layer", e);
           throw new NullPointerException("could not retrieve client data using GUID: " + guid);
           
       }
       
   }
    
    
    /**
     * Return a <tt>Map</tt> containing the infostring (stored as <tt>key|value</tt>)<br>
     * InfoString format: \ip\110.143.73.144:27960\challenge\1052098110\qport\51418\protocol\68...
     * 
     * @author Daniele Pantaleone
     * @param  info The infostring to be parsed 
     * @return A <tt>Map</tt> containing the infostring (stored as <tt>key|value</tt>)
     **/
    public Map<String,String> parseInfoString(String info) {
        
        Map<String, String> userinfo = new HashMap<String, String>();
        
        if (info.charAt(0) == '\\')
            info = info.substring(1);
        
        String lines[] = info.split("\\\\");
        
        for (int i = 0; i < lines.length; i+=2)
            userinfo.put(lines[i].toLowerCase(), lines[i+1]);
      
        return userinfo;
        
    }
    
    
    /**
     * Parse a Urban Terror log line<br>
     * Will generate an <tt>Event</tt> if necessary and 
     * push it in the <tt>Event</tt> bus
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
            if (!matcher.matches()) 
                continue;
            
            try {
                
                this.getClass()
                    .getMethod("on" + entry.getKey(), Matcher.class)
                    .invoke(this, matcher);
                
            } catch (IllegalAccessException | 
                     IllegalArgumentException | 
                     InvocationTargetException | 
                     SecurityException | 
                     NoSuchMethodException e) {
                
                this.log.error(e.getCause());
            
            }
            
            break;
            
        }
           
    }

}
