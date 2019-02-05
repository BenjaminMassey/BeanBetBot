package bbb;

import java.util.ArrayList;
import bbb.PlayersHandler.Player;

public class BetHandler {

    public static boolean open;
    public static int[] betters;
    public static String[] options;
    public static int stakes;
    public static int oddsA;

    public static void initialize() {
        open = false;
        betters = new int[] {0, 0};
        options = new String[] {"Win", "Loss"};
        stakes = 100;
        oddsA = 50;
        output();
    }

    public static boolean placeBet(String name, char side) {
        ArrayList<Player> players = Player.getPlayers();
        if(open) {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).name.equals(name)) {
                    if (players.get(i).points - stakes >= 0 && players.get(i).state == 0) {
                        players.get(i).points -= stakes;
                        if (side == 'A') {
                            players.get(i).state = 1;
                            betters[0] += 1;
                        } else {
                            players.get(i).state = 2;
                            betters[1] += 1;
                        }
                        players.get(i).investment = stakes;
                        Player.saveAll();
                        output();
                        return true;
                    }
                    if (players.get(i).state == 1 && side == 'B') {
                        players.get(i).state = 2;
                        betters[0]--;
                        betters[1]++;
                        output();
                        return true;
                    } else if (players.get(i).state == 2 && side == 'A') {
                        players.get(i).state = 1;
                        betters[0]++;
                        betters[1]--;
                        output();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void sellout(char winner) {

        // 'R' = Refund, 'A' = A won, 'B' = B won

        ArrayList<Player> players = Player.getPlayers();

        for (int i = 0; i < players.size(); i++) {

            if(winner == 'R') {
                if (players.get(i).state != 0) {
                    int payout = players.get(i).investment;
                    TwitchChat.outsidePM(players.get(i).name, "Refund! Got your money back.");
                    players.get(i).points += payout;
                    players.get(i).state = 0;
                    players.get(i).investment = 0;
                }
            }
            else {
                if ((players.get(i).state == 1 && winner == 'A') || (players.get(i).state == 2 && winner == 'B')) {
                    int payout;
                    if (winner == 'A') {
                        payout = players.get(i).investment * Math.round((1.0f / (oddsA / 100.0f)));
                    }
                    else {
                        payout = players.get(i).investment * Math.round((1.0f / ((100 - oddsA) / 100.0f)));
                    }
                    TwitchChat.outsidePM(players.get(i).name, "Congrats, you won!");
                    players.get(i).points += payout;
                    players.get(i).state = 0;
                    players.get(i).investment = 0;
                } else if (players.get(i).state == 1 || players.get(i).state == 2) {
                    int payout = 0;
                    TwitchChat.outsidePM(players.get(i).name, "Oops, better luck next time!");
                    players.get(i).points += payout;
                    players.get(i).state = 0;
                    players.get(i).investment = 0;
                }
            }
        }
        betters = new int[]{0, 0};
        Player.saveAll();
    }

    public static boolean unbet(String name) {
        ArrayList<Player> players = Player.getPlayers();

        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).name.equals(name) && open && players.get(i).state != 0) {
                players.get(i).points += players.get(i).investment;
                betters[players.get(i).state - 1]--;
                players.get(i).state = 0;
                output();
                return true;
            }
        }
        return false;
    }

    public static void output() {
        if (open) {
            FileHandler.writeToFile("Output", "!BetGame\nStakes: " + stakes +
                    "\nA: " + options[0] + " (" + oddsA + "%): " + betters[0] + "\nB: " + options[1] +
                    " (" + (100 - oddsA) + "%): " + betters[1] + "\n[OPEN]");
        }
        else {
            FileHandler.writeToFile("Output", "!BetGame\nStakes: " + stakes +
                    "\nA: " + options[0] + " (" + oddsA + "%): " + betters[0] + "\nB: " + options[1] +
                    " (" + (100 - oddsA) + "%): " + betters[1] + "\n[CLOSED]");
        }
    }
}
