package main;

public class KingQueenBonus {
    private static final int KING_APPLE = 20;
    private static final int QUEEN_APPLE = 10;
    private static final int KING_CHEESE = 15;
    private static final int QUEEN_CHEESE = 10;
    private static final int KING_BREAD = 15;
    private static final int QUEEN_BREAD = 10;
    private static final int KING_CHICKEN = 10;
    private static final int QUEEN_CHICKEN = 5;

    static final void equalFrequencyOfAssets(final String asset, int[] score,
                                             final int numberOfPlayers) {
        if (asset.equals("Apple")) {
            for (int i = 0; i < numberOfPlayers; i++) {
                score[i] += KING_APPLE;
            }
        }
        if (asset.equals("Cheese")) {
            for (int i = 0; i < numberOfPlayers; i++) {
                score[i] += KING_CHEESE;
            }
        }
        if (asset.equals("Bread")) {
            for (int i = 0; i < numberOfPlayers; i++) {
                score[i] += KING_BREAD;
            }
        }
        if (asset.equals("Chicken")) {
            for (int i = 0; i < numberOfPlayers; i++) {
                score[i] += KING_CHICKEN;
            }
        }
    }

    static final void getKingBonus(final String asset, int[] score, final int order) {
        if (asset.equals("Apple")) {
            score[order] += KING_APPLE;
        }
        if (asset.equals("Cheese")) {
            score[order] += KING_CHEESE;
        }
        if (asset.equals("Bread")) {
            score[order] += KING_BREAD;
        }
        if (asset.equals("Chicken")) {
            score[order] += KING_CHICKEN;
        }
    }

    static final void getQueenBonus(final String asset, int[] score, final int order) {
        if (asset.equals("Apple")) {
            score[order] += QUEEN_APPLE;
        }
        if (asset.equals("Cheese")) {
            score[order] += QUEEN_CHEESE;
        }
        if (asset.equals("Bread")) {
            score[order] += QUEEN_BREAD;
        }
        if (asset.equals("Chicken")) {
            score[order] += QUEEN_CHICKEN;
        }
    }

    static final int getSecondGreatest(final int a, final int b, final int c) {
        if (a != b && a != c && b != c) {
            return a + b + c - Math.max(Math.max(b, c), a)
                    - Math.min(Math.min(a, b), c);
        }
        return Math.min(Math.min(a, b), c);
    }

    static final void getFinalBonusTwoPlayers(final String asset, int[] score, final int a,
                                              final int b, final int numberOfPlayers) {
        int orderA = 0;
        int orderB = 1;
        int maximumFrequency = Math.max(a, b);
        if (a == b && a == maximumFrequency) {
            KingQueenBonus.equalFrequencyOfAssets(asset, score, numberOfPlayers);
        } else {
            if (maximumFrequency == a) {
                KingQueenBonus.getKingBonus(asset, score, orderA);
                KingQueenBonus.getQueenBonus(asset, score, orderB);
            } else {
                if (maximumFrequency == b) {
                    KingQueenBonus.getKingBonus(asset, score, orderB);
                    KingQueenBonus.getQueenBonus(asset, score, orderA);
                }
            }
        }
    }

    static final void getFinalBonusThreePlayers(final String asset, final int[] score,
                                                final int a, final int b, final int c,
                                                final int numberOfPlayers) {
        //indexul jucatorilor din lista "players"
        int orderA = 0;
        int orderB = 1;
        int orderC = 2;

        int maximumFrequency = Math.max(Math.max(a, b), c);
        int secondGreatest = getSecondGreatest(a, b, c);

        if (a == b && a == c && a == maximumFrequency) {
            KingQueenBonus.equalFrequencyOfAssets(asset, score, numberOfPlayers);
        } else {
            if (a == maximumFrequency) {
                KingQueenBonus.getKingBonus(asset, score, orderA);
            }
            if (b == maximumFrequency) {
                KingQueenBonus.getKingBonus(asset, score, orderB);
            }
            if (c == maximumFrequency) {
                KingQueenBonus.getKingBonus(asset, score, orderC);
            }
            if (a == secondGreatest && a != maximumFrequency) {
                KingQueenBonus.getQueenBonus(asset, score, orderA);
            }
            if (b == secondGreatest && b != maximumFrequency) {
                KingQueenBonus.getQueenBonus(asset, score, orderB);
            }
            if (c == secondGreatest && c != maximumFrequency) {
                KingQueenBonus.getQueenBonus(asset, score, orderC);
            }
        }
    }
}
