package main;

import java.util.List;

public class BribedPlayer extends BasicPlayer {
    private static final String BRIBED_NAME = "BRIBED";
    private static final int COINS = 50;
    private static final int ILLEGAL_PENALTY = 4;
    private static final int LEGAL_PENALTY = 2;
    private static final int BRIBE_FEW_ASSETS = 5;
    private static final int BRIBE_MORE_ASSETS = 10;
    private static final int MAX_ILLEGAL_ADDED = 5;

    String getPlayerName() {
        return BRIBED_NAME;
    }

    BribedPlayer() {
        this.coins = COINS;
    }

    final void addIllegalAssetInBagBribed() {
        int countIllegalAssetsAdded = 0;
        int id = 0;
        //daca jucatorul inca mai are bani
        if (this.coins > 0) {
            //nu exista carti ilegale
            if (countIllegals() == 0) {
                super.setDeclaredType();
            } else {
                int size = super.countIllegals();
                while (countIllegalAssetsAdded < MAX_ILLEGAL_ADDED
                        && countIllegalAssetsAdded < size) {
                    id = super.indexOfTheMostProfitableIllegalAsset();
                    this.assetsInBag.add(this.assetsInHand.get(id));
                    this.assetsInHand.remove(this.assetsInHand.get(id));
                    countIllegalAssetsAdded++;
                }
            }

            if (countIllegalAssetsAdded == 1 || countIllegalAssetsAdded == 2) {
                super.declaredType = "Apple";
                this.bribe = BRIBE_FEW_ASSETS;
                this.coins -= BRIBE_FEW_ASSETS;
            }
            if (countIllegalAssetsAdded > 2) {
                super.declaredType = "Apple";
                this.bribe = BRIBE_MORE_ASSETS;
                this.coins -= BRIBE_MORE_ASSETS;
            }
        } else {
            super.setDeclaredType();
        }
    }

    final void bribedSheriffInspects(final List<BasicPlayer> players,
                                     final List<Assets> assetsNames) {
        int legalPenalty = LEGAL_PENALTY;
        int illegalPenalty = ILLEGAL_PENALTY;

        //nici basic nici greedy NU ofera mita -> sunt inspectati
        for (int i = 0; i < players.size(); i++) {
            int numberOfAssetsInBag = players.get(i).assetsInBag.size();
            int countLegalAssetsIfHonest = 0;
            for (int j = 0; j < players.get(i).assetsInBag.size(); j++) {
                String typeOfAssetInBag = players.get(i).assetsInBag.get(j).name;
                if (players.get(i).declaredType.equals(typeOfAssetInBag)) {
                    countLegalAssetsIfHonest++;
                }
            }

            if (players.get(i) instanceof GreedyPlayer) {
                //daca jucatorul greedy a fost ONEST(toate cartile = declaredType)
                if (countLegalAssetsIfHonest == numberOfAssetsInBag) {
                    super.honestPlayerCaught(players.get(i), legalPenalty, numberOfAssetsInBag);
                } else {
                    //daca jucatorul greedy a fost MINCINOS
                    int caught = 0;
                    for (int j = 0; j < numberOfAssetsInBag; j++) {
                        //bun ilegal sau nedeclarat
                        if (players.get(i).assetsInBag.get(j).penalty == illegalPenalty
                                || !(players.get(i).assetsInBag.get(j).name
                                .equals(players.get(i).declaredType))) {
                            liarPlayerCaught(players.get(i), assetsNames, j);
                            caught = 1;

                        }
                    }
                    //adaug bunurile declarate
                    if (caught == 1) {
                        addLegalsOnMerchantstand(players.get(i));
                    }
                }
            } else {
                //daca jucatorul basic a fost ONEST(toate cartile = declaredType)
                if (countLegalAssetsIfHonest == numberOfAssetsInBag) {
                    super.honestPlayerCaught(players.get(i), legalPenalty, numberOfAssetsInBag);
                } else {
                    //daca avea doar carti ilegale si pune in sac doar una ilegala
                    int firstPenalty = players.get(i).assetsInBag.get(0).penalty;
                    if (firstPenalty == illegalPenalty
                            && players.get(i).declaredType.equals("Apple")) {
                        super.onlyOneIllegalAssetInBag(players.get(i), assetsNames, firstPenalty);
                    }
                }
            }
        }
    }
}
