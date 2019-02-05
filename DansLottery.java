package bbb;

import bbb.PlayersHandler.Player;

import java.util.ArrayList;
import java.util.Random;

public class DansLottery {
    public static ArrayList<Player> entered;

    public static int cost;

    public static void intialize () {
        entered = new ArrayList<>();
        cost = 100;
    }

    public static boolean addPlayer(String name) {
        ArrayList<Player> players = Player.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).name.equals(name) && players.get(i).points >= cost) {
                players.get(i).points -= cost;
                entered.add(players.get(i));
                return true;
            }
        }
        return false;
    }

    public static void payout() {
        ArrayList<Player> players = Player.getPlayers();
        int winnings = cost * entered.size() * 10;
        Random rng = new Random();
        int result = rng.nextInt(entered.size());
        String winner = entered.get(result).name;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).name.equals(winner)) {
                players.get(i).points += winnings;
                TwitchChat.outsideMessage(winner + " won the lottery for " + winnings + " !");
            }
        }
    }
}
