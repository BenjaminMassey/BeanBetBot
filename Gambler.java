package bbb;

import java.util.Random;
import bbb.PlayersHandler.Player;

public class Gambler {

    public static void gamble(String player, String message) {
        // Have a player randomly be given or randomly taken away a given amount

        // Parse the amount out of the message
        String amountStr = "";
        for(int i = 0; i < message.length(); i++) {
            if (i > 7)
                amountStr += message.toCharArray()[i];
        }
        int amount = 0;
        try {
            amount = Math.abs(Integer.parseInt(amountStr));
        }catch(Exception e) {
            System.err.println("Oops: " + e);
        }

        if(Player.getPoints(player) < amount) // Doesn't have enough points
            TwitchChat.outsidePM(player, "Gamble " + amount + " with " + Player.getPoints(player) + " Kappa");

        else { // Has enough points
            Random rng = new Random();
            double result = rng.nextDouble();
            double chance = 0.5;
            //if (player.equalsIgnoreCase("chaoticmeatbali")) { chance = 0.4; }

            // Gamble amount for it to display in chat
            double cap = Player.getPoints(player) * 0.5;
            if (cap < 100)
                cap = 100;

            if(result < chance) {
                TwitchChat.outsidePM(player, player + ", you won " + amount + " points! PogChamp");
                if(amount > cap)
                    TwitchChat.outsideMessage(player + " won " + amount + " points! PogChamp");
                Player.addPoints(player, amount);
            }
            else {
                TwitchChat.outsidePM(player, player + ", you lost " + amount + " points... FeelsBadMan");
                if (amount > cap)
                    TwitchChat.outsideMessage(player + " lost " + amount + " points... FeelsBadMan");
                Player.removePoints(player, amount);
            }
        }
    }
}
