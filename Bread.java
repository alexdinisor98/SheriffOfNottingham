package main;

public class Bread extends Assets {
    private static final int PROFIT = 4;
    private static final int PENALTY = 2;

    Bread() {
        super.profit = PROFIT;
        super.penalty = PENALTY;
        super.type = "legal";
        super.name = "Bread";
    }

}
