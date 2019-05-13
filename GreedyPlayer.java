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


    void addIllegalAssetInBag() {
        //daca are carti ilegale in mana sa o adauge pe cea mai profitabila in sac
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

            //accepta mita de la BRIBED si il lasa sa puna totul pe taraba
            if (comerciantPlayers.get(i) instanceof BribedPlayer) {
                if (countLegalAssetsIfHonest(comerciantPlayers.get(i))
                        == numberOfAssetsInBag) {
                    super.honestPlayerCaught(comerciantPlayers.get(i),
                            legalPenalty, numberOfAssetsInBag);

                } else {
                    //accepta mita de la BRIBED si il lasa sa puna totul pe taraba
                    this.acceptsBribe(comerciantPlayers.get(i));
                }
            } else {
                //daca jucatorul basic a fost ONEST(toate cartile = declaredType)
                if (countLegalAssetsIfHonest(comerciantPlayers.get(i))
                        == numberOfAssetsInBag) {
                    super.honestPlayerCaught(comerciantPlayers.get(i),
                            legalPenalty, numberOfAssetsInBag);
                } else {
                    //daca avea doar carti ilegale si pune in sac doar una ilegala
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

    void acceptsBribe(BasicPlayer comerciantPlayer) {
        //seriful primeste mita
        this.coins += comerciantPlayer.bribe;

        //comerciantul pune totul pe taraba
        comerciantPlayer.assetsOnMerchantStand.addAll(comerciantPlayer.assetsInBag);
        comerciantPlayer.assetsInBag.clear();
    }

}
