package bbb;

import org.jibble.pircbot.*;

import bbb.PlayersHandler.Player;

import java.io.IOException;

public class TwitchChat extends PircBot {

	// A part of BeanBetBot
	// Copyright 2019 Ben Massey
	// https://github.com/BenjaminMassey/BeanBoyBot

	// This class handles actually reading from and talking to Twitch Chat

	// Code for Twitch Chat functionality based on tutorial from this channel:
	// https://www.youtube.com/channel/UCoQuKOXYxUBeNWbendo3sF

	public static boolean connected = false;
	private static String channel; // What channel the bot should talk to/read from
	private static TwitchChat bot;
	//private static bbb.TwitchChat whisperBot;

	public TwitchChat() {
		// Quick mini setup

		this.setName(AccountsManager.getBotName());
		this.isConnected();
	}

	public static void initialize() throws NickAlreadyInUseException, IOException, IrcException {
		// Set up the Twitch Bot to be in chat
		channel = AccountsManager.getChatChannel();
		connected = true;
		bot = new TwitchChat();
		bot.setVerbose(true);
		bot.connect("irc.twitch.tv", 6667, AccountsManager.getBotOauth());
		bot.sendRawLine("CAP REQ :twitch.tv/membership"); // Allows special stuff (viewer list)
		// Below is common permission but I don't need it yet
		//bot.sendRawLine("CAP REQ :twitch.tv/tags");
		bot.sendRawLine("CAP REQ :twitch.tv/commands"); // Need it to parse whispers
		bot.joinChannel(channel);

		TimeForPoints tfp = new TimeForPoints();

		new Thread(tfp).start();
		tfp.start();
	}

	public static void deactivate() throws IOException, IrcException {
		bot.disconnect();
		connected = false;
	}

	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		// React to a given message
		
		// Here are the commands that should be taken action for

		if (message.equalsIgnoreCase("!BetGame")) {
			messageChat("Now you can bet in chat with what I'm playing! "
					+ "First step is to enter the game with !join. Then you can bet "
					+ "on the two options, A and B, with !buyA and !buyB. "
					+ "Check your points with !points, and feel free to ask questions!");
		}

		if (message.equalsIgnoreCase("!join")) {
			boolean joined = Player.addPlayer(sender);
			if (joined)
				messageChat("Thanks for joining, " + sender + ". "
						+ "You start with 100 points. Have fun!");
			else {
				if(Player.playing(sender))
					messageChat("Silly " + sender + ", you're already in!");
				else
					messageChat("Sorry, " + sender + ", but failed to add you... D:");
			}
		}

		if (message.equalsIgnoreCase("!points")) {
			if(!Player.playing(sender))
				messageChat(sender + ", first you gotta !join.");
			else
				messageChat(sender + " has " + Player.getPoints(sender) +
						" points (Rank #" + Player.getPlacement(sender) + ")");
		}

		if(message.toLowerCase().startsWith("!bet") && BetHandler.open) {
			char side = message.charAt(4);
			if (side == ' ') {
				side = message.charAt(5);
			}
			side = Character.toUpperCase(side);
			if (side == 'A' || side == 'B') {
				boolean bought = BetHandler.placeBet(sender, side);
				if (bought)
					privateMessage(sender, "You bet for " + side + "!");
				else {
					if (!Player.playing(sender))
						messageChat(sender + ", first you gotta !join.");
					else {
						if (Player.getState(sender) > 0)
							privateMessage(sender, "You already bet, " + sender + "!");
						else
							privateMessage(sender, "Sorry, " + sender + ", but failed to buy... D:");
					}
				}
			}
			else {
				privateMessage(sender, "Side '" + side +
								"' doesn't make sense to me, try side 'A' or 'B'.");
			}
		}

		if (message.equalsIgnoreCase("!unbet")) {
			boolean result = BetHandler.unbet(sender);
			if (result) {
				privateMessage(sender, "Succesfully unbet.");
			}
			else {
				privateMessage(sender, "Failed to unbet, maybe you weren't bet in?");
			}
		}

	}

	protected void onUnknown(String line) {
		if (line.contains("WHISPER")) {
			String sender = line.split("!")[0].substring(1);
			String message = line.split("WHISPER ")[1].split(" :")[1];

			if (message.equalsIgnoreCase("!points")) {
				if(!Player.playing(sender))
					privateMessage(sender, "First you gotta !join.");
				else
					privateMessage(sender, "You have " + Player.getPoints(sender) +
							" points and are rank #" + Player.getPlacement(sender));
			}

			if (message.startsWith("!gamble ")) {
				if(!Player.playing(sender))
					privateMessage(sender, sender + ", first you gotta !join.");
				else
					Gambler.gamble(sender, message); // Messaging handled there, since need to accommodate for 0 points
			}

			if (message.startsWith("!give ")) {
				try {
					String[] pieces = message.split(" ");
					int points = Math.abs(Integer.parseInt(pieces[2]));
					String receiver = pieces[1];
					receiver = receiver.toLowerCase();
					if(Player.playing(receiver)) {
						String channelOwner = channel.substring(1);
						if (sender.equals(channelOwner)) {// If channel owner
							Player.addPoints(receiver, points); // Just give the points - not transfer
							messageChat(pieces[1] + " was blessed by THE " + channel.substring(1) + " himself!");
						}
						else { // Randy
							if(Player.getPoints(sender) > points) {
								Player.removePoints(sender, points);
								Player.addPoints(receiver, points);
								messageChat(sender + " gave " + receiver + " "
										+ points + " points.");
							}
							else
								privateMessage(sender, sender + ", you don't have that many "
										+ "points to give! D:");
						}
					}
					else
						privateMessage(sender, "Cannot find player " + receiver);
				}catch(Exception e) {
					privateMessage(sender, "Failed! Make sure to use this format: "
							+ "'!give RECIPIENT NUMPOINTS'");
				}
			}

			if(message.startsWith("!take")) {
				try {
					String[] pieces = message.split(" ");
					int points = Math.abs(Integer.parseInt(pieces[2]));
					String loser = pieces[1];
					loser = loser.toLowerCase();
					if(Player.playing(loser)) {
						String channelOwner = channel.substring(1);
						if (sender.equals(channelOwner)) {// If channel owner
							Player.removePoints(loser, points); // Just give the points - not transfer
							messageChat(pieces[1] + " was smited by THE " + channel.substring(1) + " himself!");
						}
					}
					else
						privateMessage(sender, "Cannot find player " + loser);
				}catch(Exception e) {
					privateMessage(sender, "Failed! Make sure to use this format: "
							+ "'!take RECIPIENT NUMPOINTS'");
				}
			}

			if(message.startsWith("!check")) {
				try {
					String player = message.substring(7);
					player = player.toLowerCase();
					privateMessage(sender, player + " has " + Player.getPoints(player)
							+ " points.");
				} catch(Exception e) {
					privateMessage(sender, "Failed to check...");
					System.err.println(e);
				}
			}

			if (message.equalsIgnoreCase("!flex")) {
				if(Player.getLeaderBoard().toLowerCase().contains(sender.toLowerCase()))
					messageChat("Damn, " + sender + " has " + Player.getPoints(sender)
							+ " points! PogChamp");
				else
					privateMessage(sender, "Pfff not even on the leaderboard smh");
			}

			if (message.equalsIgnoreCase("!contact"))
				privateMessage(sender, "contact@speedrunstocks.com");
		}
	}

	public static String[] getViewers() {
		try {
			User[] users = bot.getUsers(channel);
			String[] viewers = new String[users.length];
			for(int i = 0; i < users.length; i++)
				viewers[i] = users[i].getNick();
			return viewers;
		}catch(Exception e) {
			return new String[0];
		}
	}

	public static void outsideMessage(String message) {
		bot.messageChat(message);
	}
	
	public static void outsidePM(String person, String message) {
		bot.privateMessage(person, message);
	}
	
	private void privateMessage(String person, String message) {
		messageChat("/w " + person + " " + message);
	}

	private void messageChat(String message) {
		sendMessage(channel, message);
	} // Simply puts a string in chat

}