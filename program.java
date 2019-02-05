package bbb;

import java.io.IOException;

import bbb.PlayersHandler.Player;

public class program {
    public static void main(String[] main)  throws IOException {
        FileHandler.checkForFilesAndCreateIfNone();
        BetHandler.initialize();
        Player.initialize();
        AccountsManager.updateAll();
        GUIHandler.createWindow("Twitch Bot", "Bean.png"); // Start button on GUI handles other start ups
    }
}
