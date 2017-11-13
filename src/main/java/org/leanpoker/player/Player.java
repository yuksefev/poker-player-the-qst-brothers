package org.leanpoker.player;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Player {

	static final String VERSION = "Default Java folding player";

	public static int betRequest(JsonElement request) {
		JsonArray players = request.getAsJsonObject().get("players").getAsJsonArray();
		int bet = 0;
		int i = 0;
		boolean betRequired = true;
		while (players.size() > i) {
			JsonObject playersJsonObject = players.get(i).getAsJsonObject();
			int betPlayer = playersJsonObject.get("bet").getAsBigInteger().intValue();
			if (playersJsonObject.get("name").getAsString().equalsIgnoreCase("The QST Brothers")) {
				betRequired = shouldSetBetValue(playersJsonObject);
			}

			if (bet < betPlayer) {
				bet = betPlayer;
			}
			i++;
		}
		if (betRequired) {
			return bet + 1;
		}
		return 0;
	}

	private static boolean shouldSetBetValue(JsonObject playersJsonObject) {
		JsonArray holeCardsArray = playersJsonObject.get("hole_cards").getAsJsonArray();
		List<String> rankValues = new ArrayList<>();
		for (int x = 0; x < holeCardsArray.size(); x++) {
			rankValues.add(holeCardsArray.get(x).getAsJsonObject().get("rank").getAsString());
		}
		if (rankValues.get(0).equalsIgnoreCase(rankValues.get(1))) {
			return true;
		}
		return false;
	}

	public static void showdown(JsonElement game) {

	}
}
