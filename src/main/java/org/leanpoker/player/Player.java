package org.leanpoker.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		shouldBet = checkRankValueHighPrio(rankValues);
		if(!shouldBet) {
			int communityCardsCount = getCommunityCardsCount(request);
			if(communityCardsCount == 0) {
				shouldBet = checkRankValueMiddlePrio(rankValues);
			} else {
				shouldBet = checkRankValueForSecondRound(request, rankValues) || checkSuitValue(request, playersJsonObject, suitValues);
			}
		}
		return shouldBet;
	}
	
	private static boolean checkSuitValue(JsonElement request, JsonObject playersJsonObject, List<String> suitValues) {
		// Check Suit of the Cards
		Map<String, Integer> suitMaps = getActualFarbenStats(request, playersJsonObject);
		if(suitMaps.get(suitValues.get(0)) >= 4 || suitMaps.get(suitValues.get(1)) >= 4) {
			return true;
		}
		return false;
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
	
	private static boolean checkRankValueHighPrio(List<String> rankValues) {
		if (rankValues.get(0).equalsIgnoreCase(rankValues.get(1))) {
			return true;
		} else if(!rankValues.get(0).matches("[0-9]") && !rankValues.get(1).matches("[0-9]")){
			return true;
		} 
		
		return false;
	}
	
	private static boolean checkRankValueMiddlePrio(List<String> rankValues) {
		if(checkRank(rankValues.get(0), "A", "K", "Q", "J") || checkRank(rankValues.get(1), "A", "K", "Q", "J")){
			return true;
		} 
		return false;
	}
	
	private static boolean checkRank(String value, String... rank) {
		for (int i = 0; i < rank.length; i++) {
			if(value.equalsIgnoreCase(rank[i])) {
				return true;
			}
		}
		return false;
	}

	private static int getCommunityCardsCount(JsonElement request) {
		return request.getAsJsonObject().get("community_cards").getAsJsonArray().size();
	}
	
	private static Map<String, Integer> getActualFarbenStats(JsonElement request, JsonObject playersJsonObject) {
        Map<String, Integer> result = new HashMap<>();

        // 1. read our cards:
        JsonArray holeCardsArray = playersJsonObject.get("hole_cards").getAsJsonArray();
        int actualSpades = 0;
        int actualHearts = 0;
        int actualClubs = 0;
        int actualOther = 0;

        for (int x = 0; x < holeCardsArray.size(); x++) {
            String suit = holeCardsArray.get(x).getAsJsonObject().get("suit").getAsString();

            if (suit.equals("spades")) {
                actualSpades++;
            } else if (suit.equals("clubs")) {
                actualClubs++;
            } else if (suit.equals("hearts")) {
                actualHearts++;
            } else {
                actualOther++;
            }
        }

        // read comm cards
        JsonArray communityCards = request.getAsJsonObject().get("community_cards").getAsJsonArray();
        for (int x = 0; x < communityCards.size(); x++) {
            String suit = communityCards.get(x).getAsJsonObject().get("suit").getAsString();
            if (suit.equals("spades")) {
                actualSpades++;
            } else if (suit.equals("clubs")) {
                actualClubs++;
            } else if (suit.equals("hearts")) {
                actualHearts++;
            } else {
                actualOther++;
            }
        }

        result.put("spades", new Integer(actualSpades));
        result.put("hearts", new Integer(actualHearts));
        result.put("clubs", new Integer(actualClubs));
        result.put("other", new Integer(actualOther));

        return result;
	}
	
	public static void showdown(JsonElement game) {
		
	}
}
