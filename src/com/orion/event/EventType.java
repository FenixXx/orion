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
 * @author      Mathias Van Malderen
 * @version     1.0
 * @copyright   Mathias Van Malderen, 11 September, 2012
 * @package     com.orion.event
 **/

package com.orion.event;

public enum EventType {
    
    EVT_CLIENT_BOMB_DEFUSED,
    EVT_CLIENT_BOMB_HOLDER,
    EVT_CLIENT_BOMB_PLANTED,
    EVT_CLIENT_CALLVOTE,
    EVT_CLIENT_CONNECT,
    EVT_CLIENT_DAMAGE,
    EVT_CLIENT_DAMAGE_SELF,
    EVT_CLIENT_DAMAGE_TEAM,
    EVT_CLIENT_DISCONNECT,
    EVT_CLIENT_FLAG_CAPTURED,
    EVT_CLIENT_FLAG_DROPPED,
    EVT_CLIENT_FLAG_RETURNED,
    EVT_CLIENT_GEAR_CHANGE,
    EVT_CLIENT_ITEM_PICKUP,
    EVT_CLIENT_JOIN,
    EVT_CLIENT_JUMP_RUN_CANCELED,
    EVT_CLIENT_JUMP_RUN_STARTED,
    EVT_CLIENT_JUMP_RUN_STOPPED,
    EVT_CLIENT_KILL,
    EVT_CLIENT_KILL_SELF,
    EVT_CLIENT_KILL_TEAM,
    EVT_CLIENT_NAME_CHANGE,
    EVT_CLIENT_POSITION_LOAD,
    EVT_CLIENT_POSITION_SAVE,
    EVT_CLIENT_RADIO,
    EVT_CLIENT_SAY,
    EVT_CLIENT_SAY_PRIVATE,
    EVT_CLIENT_SAY_TEAM,
    EVT_CLIENT_TEAM_CHANGE,
    EVT_CLIENT_VOTE,
    EVT_GAME_EXIT,
    EVT_GAME_ROUND_START,
    EVT_GAME_WARMUP,
    EVT_TEAM_FLAG_RETURN,
    EVT_TEAM_SURVIVOR_WINNER,
    EVT_SURVIVOR_WINNER,
    
}
