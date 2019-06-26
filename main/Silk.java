package main;

public class Silk extends Assets {
    private static final int PROFIT = 9;
    private static final int PENALTY = 4;

    Silk() {
        super.profit = PROFIT;
        super.penalty = PENALTY;
        super.type = "illegal";
        super.name = "Silk";
    }

}
