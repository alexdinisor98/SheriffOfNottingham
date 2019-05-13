package main;

public class Chicken extends Assets {
    private static final int PROFIT = 4;
    private static final int PENALTY = 2;

    Chicken() {
        super.profit = PROFIT;
        super.penalty = PENALTY;
        super.type = "legal";
        super.name = "Chicken";
    }

}
