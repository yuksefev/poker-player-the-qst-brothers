package org.leanpoker.player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
				betRequired = shouldSetBetValue(request, playersJsonObject);
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

	private static boolean shouldSetBetValue(JsonElement request, JsonObject playersJsonObject) {
		JsonArray holeCardsArray = playersJsonObject.get("hole_cards").getAsJsonArray();
		List<String> rankValues = new ArrayList<>();
		List<String> suitValues = new ArrayList<>();
		for (int x = 0; x < holeCardsArray.size(); x++) {
			rankValues.add(holeCardsArray.get(x).getAsJsonObject().get("rank").getAsString());
			suitValues.add(holeCardsArray.get(x).getAsJsonObject().get("suit").getAsString());
		}
		if (rankValues.get(0).equalsIgnoreCase(rankValues.get(1))) {
			return true;
		} else if(!rankValues.get(0).matches("[0-9]") && !rankValues.get(1).matches("[0-9]")){
			return true;
		} else if(rankValues.get(0).equalsIgnoreCase("A") || rankValues.get(1).equalsIgnoreCase("A")){
			return true;
		} else {
//			Map<String> cardsCheckSet = new HashSet<>();
//			JsonArray communityCards = request.getAsJsonObject().get("community_cards").getAsJsonArray();
//			if(communityCards.size() == 0) return false;
//			
//			for (int x = 0; x < communityCards.size(); x++) {
//				cardsCheckSet.add(communityCards.get(x).getAsJsonObject().get("suit").getAsString());
//			}
//			cardsCheckSet.add(suitValues.get(0));
//			cardsCheckSet.add(suitValues.get(1));
//			if(communityCards.size() == 3) {
//				if(cardsCheckSet.size() == 1) {
//					return true;
//				}
//			} else if(communityCards.size() == 4) {
//				if(cardsCheckSet.size() == 2) {
//					return true;
//				}
//			} else if(communityCards.size() == 5) {
//				if(cardsCheckSet.size() == 3) {
//					return true;
//				}
//			}
		}
		return false;
	}

	public static void showdown(JsonElement game) {
//		JsonArray players = game.getAsJsonObject().get("players").getAsJsonArray();
//		int bet = 0;
//		int i = 0;
//		boolean betRequired = true;
//		while (players.size() > i) {
//			JsonArray holeCardsArray = players.get(i).getAsJsonObject().get("hole_cards").getAsJsonArray();
//			List<String> suitValues = new ArrayList<>();
//			for (int x = 0; x < holeCardsArray.size(); x++) {
//				suitValues.add(holeCardsArray.get(x).getAsJsonObject().get("suit").getAsString());
//			}
//			
//		}
	}
}
