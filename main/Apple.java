package main;

public class Apple extends Assets {
    private static final int PROFIT = 2;
    private static final int PENALTY = 2;

    Apple() {
        super.profit = PROFIT;
        super.penalty = PENALTY;
        super.type = "legal";
        super.name = "Apple";
    }
}
