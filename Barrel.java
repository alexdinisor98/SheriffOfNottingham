package main;

public class Barrel extends Assets {
    private static final int PROFIT = 7;
    private static final int PENALTY = 4;

    Barrel() {
        super.profit = PROFIT;
        super.penalty = PENALTY;
        super.type = "illegal";
        super.name = "Barrel";
    }
}
