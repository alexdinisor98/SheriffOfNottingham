package main;

import java.util.List;

public class GreedyPlayer extends BasicPlayer {
    private static final String GREEDY_NAME = "GREEDY";
    private static final int COINS = 50;
    private static final int ILLEGAL_PENALTY = 4;
    private static final int LEGAL_PENALTY = 2;

    String getPlayerName() {
        return GREEDY_NAME;
    }

    GreedyPlayer() {
        this.coins = COINS;
    }

    // if has illegal cards in hand, add the most profitable one in the bag 
    void addIllegalAssetInBag() {

        if (this.countIllegals() != 0) {
            int id = super.indexOfTheMostProfitableIllegalAsset();
            this.assetsInBag.add(assetsInHand.get(id));
            this.assetsInHand.remove(assetsInHand.get(id));
        }
    }

    final void greedySheriffInspects(final List<BasicPlayer> comerciantPlayers,
                                     final List<Assets> assetsNames) {

        int legalPenalty = LEGAL_PENALTY;
        int illegalPenalty = ILLEGAL_PENALTY;

        for (int i = 0; i < comerciantPlayers.size(); i++) {
            int numberOfAssetsInBag = comerciantPlayers.get(i).assetsInBag.size();

            // accept bribe from the BRIBED player and let him put everything on the table 
            if (comerciantPlayers.get(i) instanceof BribedPlayer) {
                if (countLegalAssetsIfHonest(comerciantPlayers.get(i))
                        == numberOfAssetsInBag) {
                    super.honestPlayerCaught(comerciantPlayers.get(i),
                            legalPenalty, numberOfAssetsInBag);

                } else {                    
                    this.acceptsBribe(comerciantPlayers.get(i));
                }
            } else {
                // if BASIC player was HONEST (all cards = dclared type)
                if (countLegalAssetsIfHonest(comerciantPlayers.get(i))
                        == numberOfAssetsInBag) {
                    super.honestPlayerCaught(comerciantPlayers.get(i),
                            legalPenalty, numberOfAssetsInBag);
                } else {
                    // if only illegal cards and puts only one illegal in the bag 
                    int firstAssetPenalty = comerciantPlayers.get(i).assetsInBag.get(0).penalty;
                    if (firstAssetPenalty == illegalPenalty &&
                            comerciantPlayers.get(i).declaredType.equals("Apple")) {
                        super.onlyOneIllegalAssetInBag(comerciantPlayers.get(i),
                                assetsNames, firstAssetPenalty);
                    }
                }
            }

        }
    }

    // accept the merchant's bribe
    void acceptsBribe(BasicPlayer comerciantPlayer) {
        //seriful primeste mita
        this.coins += comerciantPlayer.bribe;

        // the merchant puts everything on the table
        comerciantPlayer.assetsOnMerchantStand.addAll(comerciantPlayer.assetsInBag);
        comerciantPlayer.assetsInBag.clear();
    }

}
