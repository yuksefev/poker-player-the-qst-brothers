package org.leanpoker.player;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Player {

	static final String VERSION = "Default Java folding player";

	public static int betRequest(JsonElement request) {
		JsonArray players = request.getAsJsonObject().get("players").getAsJsonArray();
		int bet = 0;
		int ourBet = 0;
		int i = 0;
		boolean betRequired = true;
		while (players.size() > i) {
			JsonObject playersJsonObject = players.get(i).getAsJsonObject();
			int betPlayer = playersJsonObject.get("bet").getAsBigInteger().intValue();
			if (playersJsonObject.get("name").getAsString().equalsIgnoreCase("The QST Brothers")) {
				betRequired = shouldSetBetValue(request, playersJsonObject);
				ourBet = betPlayer;
			}

			if (bet < betPlayer) {
				bet = betPlayer;
			}
			i++;
		}
		if (betRequired) {
			return bet  + 1;
		}
		return 0;
	}

	private static boolean shouldSetBetValue(JsonElement request, JsonObject playersJsonObject) {
		boolean shouldBet = true;
		JsonArray holeCardsArray = playersJsonObject.get("hole_cards").getAsJsonArray();
		List<String> rankValues = new ArrayList<>();
		List<String> suitValues = new ArrayList<>();
		for (int x = 0; x < holeCardsArray.size(); x++) {
			rankValues.add(holeCardsArray.get(x).getAsJsonObject().get("rank").getAsString());
			suitValues.add(holeCardsArray.get(x).getAsJsonObject().get("suit").getAsString());
		}
		shouldBet = checkRankValue(rankValues);
		if(!shouldBet) {
			int communityCardsCount = getCommunityCardsCount(request);
			if(communityCardsCount == 0) {
				shouldBet = true;
			} else {
				shouldBet = checkRankValueForSecondRound(request, rankValues);
			}
		}
		return shouldBet;
	}
	
	private static boolean checkRankValueForSecondRound(JsonElement request, List<String> rankValues) {
		JsonArray communityCards = request.getAsJsonObject().get("community_cards").getAsJsonArray();
//		Set<String> cardsCheckSet = new HashSet<>();
		for (int x = 0; x < communityCards.size(); x++) {
			String rankCommunityCard = communityCards.get(x).getAsJsonObject().get("rank").getAsString();
			if(rankValues.contains(rankCommunityCard)) {
				return true;
			}
		}
		return false;
	}

	private static int getCommunityCardsCount(JsonElement request) {
		return request.getAsJsonObject().get("community_cards").getAsJsonArray().size();
	}
	
	private static boolean checkRankValue(List<String> rankValues) {
		return checkRankValueHighPrio(rankValues) || checkRankValueMiddlePrio(rankValues);
	}
	
	private static boolean checkRankValueHighPrio(List<String> rankValues) {
		if (rankValues.get(0).equalsIgnoreCase(rankValues.get(1))) {
			return true;
		} else if(!rankValues.get(0).matches("[0-9]") && !rankValues.get(1).matches("[0-9]")){
			return true;
		} 
		
		return false;
	}
	
	private static boolean checkRankValueMiddlePrio(List<String> rankValues) {
		if(rankValues.get(0).equalsIgnoreCase("A") || rankValues.get(1).equalsIgnoreCase("A")){
			return true;
		} 
		return false;
	}

	public static void showdown(JsonElement game) {
		
	}
}
