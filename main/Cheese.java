package main;

public class Cheese extends Assets {
    private static final int PROFIT = 3;
    private static final int PENALTY = 2;

    Cheese() {
        super.profit = PROFIT;
        super.penalty = PENALTY;
        super.type = "legal";
        super.name = "Cheese";
    }

}
