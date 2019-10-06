package main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameLogic {
    private static final int ID_APPLE = 0;
    private static final int ID_CHEESE = 1;
    private static final int ID_BREAD = 2;
    private static final int ID_CHICKEN = 3;
    private static final int ID_SILK = 10;
    private static final int ID_PEPPER = 11;
    private static final int ID_BARREL = 12;
    private static final int ASSETS_IN_BAG = 6;

    static List<Assets> getAssetNames(final List<Integer> assetsIds,
                                      final List<Assets> assetsNames) {
        Assets newAsset = null;
        for (int i = 0; i < assetsIds.size(); i++) {
            int id = assetsIds.get(i);
            if (id == ID_APPLE) {
                newAsset = new Apple();
                assetsNames.add(newAsset);
            }
            if (id == ID_CHEESE) {
                newAsset = new Cheese();
                assetsNames.add(newAsset);
            }
            if (id == ID_BREAD) {
                newAsset = new Bread();
                assetsNames.add(newAsset);
            }
            if (id == ID_CHICKEN) {
                newAsset = new Chicken();
                assetsNames.add(newAsset);
            }
            if (id == ID_SILK) {
                newAsset = new Silk();
                assetsNames.add(newAsset);
            }
            if (id == ID_PEPPER) {
                newAsset = new Pepper();
                assetsNames.add(newAsset);
            }
            if (id == ID_BARREL) {
                newAsset = new Barrel();
                assetsNames.add(newAsset);
            }
        }
        return assetsNames;
    }

    static final BasicPlayer getAssetsInHand(final List<Assets> assetsNames,
                                             final BasicPlayer player) {
        // get the assets from the deck of cards
        // add them in player's hand
        while (player.assetsInHand.size() < ASSETS_IN_BAG) {
            Assets newAsset = assetsNames.get(0);
            player.assetsInHand.add(newAsset);
            assetsNames.remove(assetsNames.get(0));
        }
        return player;
    }

    static final int convert(Integer a) {
        if (a != null) {
            a = Integer.parseInt(a.toString());
        } else {
            a = 0;
        }
        return a;
    }

    static final void getFlavorsFromTheList(final BasicPlayer player, final String[] flavors) {
        for (int i = 0; i < player.assetsOnMerchantStand.size(); i++) {
            flavors[i] = player.assetsOnMerchantStand.get(i).name;
        }
    }

    static final void playGame(final List<BasicPlayer> comerciantPlayers, final int numberOfPlayers,
                               final List<BasicPlayer> players, final List<Assets> assetsNames) {
        int evenRound = 0;
        for (int i = 1; i <= 2 * numberOfPlayers; i++) {
            comerciantPlayers.clear();
            int sheriffIndex = (i - 1) % numberOfPlayers;
            BasicPlayer sheriff = players.get(sheriffIndex);

            // add in the list of merchants the one who is not a sheriff
            for (int j = 0; j < numberOfPlayers; j++) {
                if (j != sheriffIndex) {
                    comerciantPlayers.add(players.get(j));
                }
            }

            for (int k = 0; k < comerciantPlayers.size(); k++) {
                // add assets in the bag and declare their type
                if (comerciantPlayers.get(k) instanceof BribedPlayer) {
                    ((BribedPlayer) comerciantPlayers.get(k)).addIllegalAssetInBagBribed();
                } else {
                    if (comerciantPlayers.get(k) instanceof GreedyPlayer) {
                        comerciantPlayers.get(k).setDeclaredType();
                        evenRound++;

                        // if greedy was a merchant on even round
                        if (evenRound % 2 == 0) {
                            ((GreedyPlayer) comerciantPlayers.get(k)).addIllegalAssetInBag();
                        }
                    } else {
                        comerciantPlayers.get(k).setDeclaredType();
                    }
                }

                // every sheriff inspects
                if (sheriff instanceof GreedyPlayer) {
                    ((GreedyPlayer) sheriff).greedySheriffInspects(comerciantPlayers, assetsNames);
                } else {
                    if (sheriff instanceof BribedPlayer) {
                        ((BribedPlayer) sheriff)
                                .bribedSheriffInspects(comerciantPlayers, assetsNames);
                    } else {
                        sheriff.basicSheriffInspects(comerciantPlayers, assetsNames);
                    }
                }
                comerciantPlayers.get(k).afterInspection(assetsNames);
            }
        }
    }

    static final Map<String, Integer> getFrequency(final BasicPlayer player) {
        String[] flavors = new String[player.assetsOnMerchantStand.size()];
        getFlavorsFromTheList(player, flavors);
        Map<String, Integer> countAssets = new HashMap<String, Integer>();
        //compute the maximum frequency of assets in the bag
        for (String asset : flavors) {
            countAssets.put(asset, countAssets.getOrDefault(asset, 0) + 1);
        }
        return countAssets;
    }

    static final String[] getAllPossibleFlavors() {
        String[] flavors = {"Apple", "Cheese", "Bread",
                "Chicken", "Silk", "Pepper", "Barrel"};

        return flavors;
    }

    static final void getSortedScores(final int numberOfPlayers, final List<BasicPlayer> players,
                                      final int[] score, final String[] playerNames) {
        for (int i = 0; i < numberOfPlayers; i++) {
            playerNames[i] = players.get(i).getPlayerName();
        }
        int auxScore = 0;
        String auxName = "";
        for (int i = 0; i < numberOfPlayers - 1; i++) {
            for (int j = i + 1; j < numberOfPlayers; j++) {
                if (score[i] < score[j]) {
                    auxScore = score[i];
                    score[i] = score[j];
                    score[j] = auxScore;
                    auxName = playerNames[i];
                    playerNames[i] = playerNames[j];
                    playerNames[j] = auxName;
                }
            }
        }
    }
}
