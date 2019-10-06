package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class Main {
    private static final int THREE_PLAYERS_GAME = 3;
    private static final int TWO_PLAYERS_GAME = 2;

    private static final class GameInputLoader {
        private final String mInputPath;

        private GameInputLoader(final String path) {
            mInputPath = path;
        }

        public GameInput load() {
            List<Integer> assetsIds = new ArrayList<>();
            List<String> playerOrder = new ArrayList<>();

            try {
                BufferedReader inStream = new BufferedReader(new FileReader(mInputPath));
                String assetIdsLine = inStream.readLine().replaceAll("[\\[\\] ']", "");
                String playerOrderLine = inStream.readLine().replaceAll("[\\[\\] ']", "");

                for (String strAssetId : assetIdsLine.split(",")) {
                    assetsIds.add(Integer.parseInt(strAssetId));
                }

                for (String strPlayer : playerOrderLine.split(",")) {
                    playerOrder.add(strPlayer);
                }
                inStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return new GameInput(assetsIds, playerOrder);
        }
    }

    public static void main(final String[] args) {
        GameInputLoader gameInputLoader = new GameInputLoader(args[0]);
        GameInput gameInput = gameInputLoader.load();

        // TODO Implement the game logic.
        List<Integer> assetsIds = new ArrayList<>();
        List<String> playerOrder = new ArrayList<>();
        assetsIds = gameInput.getAssetIds();
        playerOrder = gameInput.getPlayerNames();

        List<Assets> assetsNames = new ArrayList<>();
        assetsNames = GameLogic.getAssetNames(assetsIds, assetsNames);

        int numberOfPlayers = playerOrder.size();
        List<BasicPlayer> players = new ArrayList<>();

        for (int i = 0; i < numberOfPlayers; i++) {
            if (playerOrder.get(i).equals("basic")) {
                BasicPlayer playerBasic = new BasicPlayer();
                playerBasic = GameLogic.getAssetsInHand(assetsNames, playerBasic);
                players.add(playerBasic);
            }
            if (playerOrder.get(i).equals("greedy")) {
                BasicPlayer playerGreedy = new GreedyPlayer();
                playerGreedy = GameLogic.getAssetsInHand(assetsNames, playerGreedy);
                players.add(playerGreedy);
            }
            if (playerOrder.get(i).equals("bribed")) {
                BasicPlayer playerBribed = new BribedPlayer();
                playerBribed = GameLogic.getAssetsInHand(assetsNames, playerBribed);
                players.add(playerBribed);
            }
        }
        List<BasicPlayer> comerciantPlayers = new ArrayList<>();

        // every round with sheriffs and merchants
        GameLogic.playGame(comerciantPlayers, numberOfPlayers, players, assetsNames);

        int[] score = new int[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            score[i] = players.get(i).computeScoreWithoutBonus();
        }

        // all asset types
        String[] flavors = GameLogic.getAllPossibleFlavors();

        if (numberOfPlayers == TWO_PLAYERS_GAME) {

            // como=pute frequency of the assets on the table
            Map<String, Integer> countAssets1 = GameLogic.getFrequency(players.get(0));
            Map<String, Integer> countAssets2 = GameLogic.getFrequency(players.get(1));

            Integer a, b;
            for (String asset : flavors) {
                a = countAssets1.get(asset);
                b = countAssets2.get(asset);

                a = GameLogic.convert(a);
                b = GameLogic.convert(b);

                KingQueenBonus.getFinalBonusTwoPlayers(asset, score, a, b, numberOfPlayers);
            }
        }

        if (numberOfPlayers == THREE_PLAYERS_GAME) {
            // frequency of the assets of the 3 players
            // on the table in the end
            Map<String, Integer> countAssets1 = GameLogic.getFrequency(players.get(0));
            Map<String, Integer> countAssets2 = GameLogic.getFrequency(players.get(1));
            Map<String, Integer> countAssets3 = GameLogic.getFrequency(players.get(2));

            Integer a, b, c;
            for (String asset : flavors) {
                a = countAssets1.get(asset);
                b = countAssets2.get(asset);
                c = countAssets3.get(asset);

                a = GameLogic.convert(a);
                b = GameLogic.convert(b);
                c = GameLogic.convert(c);

                KingQueenBonus.getFinalBonusThreePlayers(asset, score, a, b, c, numberOfPlayers);
            }
        }

        String[] playerNames = new String[numberOfPlayers];
        // sorting the scores
        GameLogic.getSortedScores(numberOfPlayers, players, score, playerNames);

        // displaying final scores
        for (int i = 0; i < numberOfPlayers; i++) {
            System.out.println(playerNames[i] + ": " + score[i]);
        }
    }
}

