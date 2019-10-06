package main;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BasicPlayer {

    protected List<Assets> assetsInHand = new LinkedList<>();
    protected List<Assets> assetsOnMerchantStand = new LinkedList<>();
    protected List<Assets> assetsInBag = new LinkedList<>();
    protected int bribe;
    protected int coins;
    protected String declaredType;
    private static final int COINS = 50;
    private static final int ASSETS_IN_BAG = 6;
    private static final int ILLEGAL_PENALTY = 4;
    private static final int LEGAL_PENALTY = 2;
    private static final int BONUS_SILK = 3;
    private static final int BONUS_BARREL = 2;
    private static final int BONUS_PEPPER = 2;
    private static final int MAXX = 9999;

    String getPlayerName() {
        return BASIC_NAME;
    }

    private static final String BASIC_NAME = "BASIC";

    BasicPlayer() {
        this.coins = COINS;
    }

    /**
     * setting the declared type in the bag
     */
    void setDeclaredType() {
        int id = 0;
        //only illegal cards
        //adding in the bag only an illegal card with the maximum profit
        if (countIllegals() == assetsInHand.size()) {
            this.declaredType = "Apple";
            id = indexOfTheMostProfitableIllegalAsset();
            assetsInBag.add(assetsInHand.get(id));
            assetsInHand.remove(assetsInHand.get(id));
        } else {
            int legalPenalty = LEGAL_PENALTY;
            id = indexOfTheMostFrequentAsset(legalPenalty);
            //searching for legal assets
            this.declaredType = assetsInHand.get(id).name;

            //adding all assets in the bag = declaredType
            for (int i = 0; i < assetsInHand.size(); i++) {
                if (assetsInHand.get(i).name.equals(declaredType)) {
                    assetsInBag.add(assetsInHand.get(i));
                    assetsInHand.remove(assetsInHand.get(i));
                    i--;
                }
            }
        }
    }
    /**
    * Get the index of the most frequent asset.
    */
    final int indexOfTheMostFrequentAsset(final int penalty) {
        // max frequency of an asset
        List<String> flavors = new LinkedList<>();
        //elements with the same penalty percentage
        for (int i = 0; i < ASSETS_IN_BAG; i++) {
            if (this.assetsInHand.get(i).penalty == penalty) {
                flavors.add(this.assetsInHand.get(i).name);
            }
        }
        Map<String, Integer> countAssets = new LinkedHashMap<String, Integer>();

        for (String asset : flavors) {
            countAssets.put(asset, countAssets.getOrDefault(asset, 0) + 1);
        }
        int freqMax = 0; 
        List<String> strings = new LinkedList<>();
        for (String key : countAssets.keySet()) {
            if (freqMax < countAssets.get(key)) {
                freqMax = countAssets.get(key);
            }
        }
        //adding in the list only assets with maximum frequency
        for (String key : countAssets.keySet()) {
            if (freqMax == countAssets.get(key)) {
                strings.add(key);
            }
        }
        int profitMax = 0;
        int id = 0;
        for (int k = 0; k < strings.size(); k++) {
            for (int index = 0; index < this.assetsInHand.size(); index++) {
                if (strings.get(k).equals(this.assetsInHand.get(index).name)
                        && profitMax < assetsInHand.get(index).profit) {
                    profitMax = this.assetsInHand.get(index).profit;
                    id = index;
                }
            }
        }
        return id;
    }

    /**
    * Inspecting bags as basic sheriff.
    */
    final void basicSheriffInspects(final List<BasicPlayer> players,
                                    final List<Assets> assetsNames) {
        int legalPenalty = LEGAL_PENALTY;
        int illegalPenalty = ILLEGAL_PENALTY;
        for (int i = 0; i < players.size(); i++) {
            int numberOfAssetsInBag = players.get(i).assetsInBag.size();

            if (players.get(i) instanceof GreedyPlayer) {
                // if greedy player was HONEST(all assets = declaredType)
                if (countLegalAssetsIfHonest(players.get(i)) == numberOfAssetsInBag) {
                    honestPlayerCaught(players.get(i), legalPenalty, numberOfAssetsInBag);

                } else {
                    // if greedy player was a LIAR.
                    int caught = 0;
                    for (int j = 0; j < numberOfAssetsInBag; j++) {
                        // legal asset or undeclared

                        if (players.get(i).assetsInBag.get(j).penalty == illegalPenalty
                                || !(players.get(i).assetsInBag.get(j).name
                                .equals(players.get(i).declaredType))) {
                            liarPlayerCaught(players.get(i), assetsNames, j);
                            caught = 1;

                        }
                    }
                    if (caught == 1) {
                        addLegalsOnMerchantstand(players.get(i));
                    }
                }

            } else {
                if (players.get(i) instanceof BribedPlayer) {

                    // if bribed player was HONEST(all assets = declaredType)
                    if (countLegalAssetsIfHonest(players.get(i))
                            == numberOfAssetsInBag) {
                        honestPlayerCaught(players.get(i), legalPenalty, numberOfAssetsInBag);
                    } else {
                        // only illegal cards expected
                        // put in the bag all illegal hold assets 
                        bribedPlayerCaught(players.get(i), assetsNames,
                                numberOfAssetsInBag, illegalPenalty);
                    }
                }
            }
        }
    }

    final void afterInspection(final List<Assets> assetsNames) {
        // make full hand again
        while (this.assetsInHand.size() < ASSETS_IN_BAG) {
            this.assetsInHand.add(assetsNames.get(0));
            assetsNames.remove(0);
        }
    }

    final int computeScoreWithoutBonus() {
        int score = 0;
        score += this.coins;

        for (int i = 0; i < this.assetsOnMerchantStand.size(); i++) {
            if (this.assetsOnMerchantStand.get(i).name.equals("Silk")) {
                for (int j = 0; j < BONUS_SILK; j++) {
                    Assets cheese = new Cheese();
                    this.assetsOnMerchantStand.add(cheese);
                }
            }

            if (this.assetsOnMerchantStand.get(i).name.equals("Pepper")) {
                for (int j = 0; j < BONUS_PEPPER; j++) {
                    Assets chicken = new Chicken();
                    this.assetsOnMerchantStand.add(chicken);
                }
            }

            if (this.assetsOnMerchantStand.get(i).name.equals("Barrel")) {
                for (int j = 0; j < BONUS_BARREL; j++) {
                    Assets bread = new Bread();
                    this.assetsOnMerchantStand.add(bread);
                }
            }
        }

        for (int k = 0; k < this.assetsOnMerchantStand.size(); k++) {
            score += this.assetsOnMerchantStand.get(k).profit;
        }

        return score;
    }

    final void honestPlayerCaught(final BasicPlayer player,
                                  final int legalPenalty, final int numberOfAssetsInBag) {
        // sheriff pays coins to the merchant
        player.coins += numberOfAssetsInBag * legalPenalty;
        this.coins -= numberOfAssetsInBag * legalPenalty;

        // put on the table all assets from the bag and empty it 
        player.assetsOnMerchantStand.addAll(player.assetsInBag);
        player.assetsInBag.clear();
    }

    final void liarPlayerCaught(final BasicPlayer player,
                                final List<Assets> assetsNames, final int indexInTheBag) {
        // sheriff wins penalty for every illegal / undeclared asset
        this.coins += player.assetsInBag.get(indexInTheBag).penalty;
        player.coins -= player.assetsInBag.get(indexInTheBag).penalty;

        // confiscated assets goe back to the deck of cards
        assetsNames.add(player.assetsInBag.get(indexInTheBag));
        player.assetsInBag.remove(indexInTheBag);
    }

    final void bribedPlayerCaught(final BasicPlayer player, final List<Assets> assetsNames,
                                  final int numberOfAssetsInBag, final int illegalPenalty) {
        // do not accept bribe -> bribe comes back in his own
        player.coins += player.bribe;
        player.bribe = 0;
        this.coins += numberOfAssetsInBag * illegalPenalty;
        player.coins -= numberOfAssetsInBag * illegalPenalty;

        // cards are confiscated -> they come back to the deck of cards
        assetsNames.addAll(player.assetsInBag);
        player.assetsInBag.clear();
    }

    final void onlyOneIllegalAssetInBag(final BasicPlayer player, final List<Assets> assetsNames,
                                        final int firstAssetPenalty) {
        player.coins -= firstAssetPenalty;
        this.coins += firstAssetPenalty;

        // the ilegal card is confiscated
        assetsNames.add(player.assetsInBag.get(0));
        player.assetsInBag.clear();
    }

    final int indexOfTheMostProfitableIllegalAsset() {
        int illegalPenalty = ILLEGAL_PENALTY;
        int index = MAXX;
        int profitMax = 0;
        for (int i = 0; i < this.assetsInHand.size(); i++) {
            if (this.assetsInHand.get(i).penalty == illegalPenalty
                    && profitMax < this.assetsInHand.get(i).profit) {
                profitMax = this.assetsInHand.get(i).profit;
                index = i;
            }
        }
        return index;
    }

    final int countIllegals() {
        // counting the illegal cards
        int count = 0;
        for (int i = 0; i < this.assetsInHand.size(); i++) {
            if (this.assetsInHand.get(i).penalty == ILLEGAL_PENALTY) {
                count++;
            }
        }
        return count;
    }

    final int countLegalAssetsIfHonest(final BasicPlayer player) {
        int count = 0;
        for (int j = 0; j < player.assetsInBag.size(); j++) {
            String typeOfAssetInBag = player.assetsInBag.get(j).name;
            if (player.declaredType.equals(typeOfAssetInBag)) {
                count++;
            }
        }
        return count;
    }

    final void addLegalsOnMerchantstand(final BasicPlayer player) {
        for (int j = 0; j < player.assetsInBag.size(); j++) {
            if (player.assetsInBag.get(j).name.equals(player.declaredType)) {
                player.assetsOnMerchantStand.add(player.assetsInBag.get(j));
                player.assetsInBag.remove(player.assetsInBag.get(j));
                j--;
            }
        }
    }

}
