package main;

public class Pepper extends Assets {
    private static final int PROFIT = 8;
    private static final int PENALTY = 4;

    Pepper() {
        super.profit = PROFIT;
        super.penalty = PENALTY;
        super.type = "illegal";
        super.name = "Pepper";
    }

}
