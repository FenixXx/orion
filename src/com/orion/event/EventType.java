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
