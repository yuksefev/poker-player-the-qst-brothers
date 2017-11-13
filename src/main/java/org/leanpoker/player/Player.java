package org.leanpoker.player;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.math.BigInteger;
import java.util.Map;

public class Player {

    static final String VERSION = "Default Java folding player";

    public static int betRequest(JsonElement request) {
    	JsonArray players = request.getAsJsonObject().get("players").getAsJsonArray();
    	int bet = 0;
    	int i = 0;
    	while(players.size() > i) {
    		int betPlayer = players.get(i).getAsJsonObject().get("bet").getAsBigInteger().intValue();
    		if(bet < betPlayer) {
    			bet = betPlayer;
    		}
    		i++;
    	}
    	return bet + 1;
    }

    public static void showdown(JsonElement game) {
    }
}
