package bbb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import bbb.PlayersHandler.Player;

public class GUIHandler extends JFrame {
	
	// A part of BeanBetBot
	// Copyright 2019 Ben Massey
	// https://github.com/BenjaminMassey/BeanBoyBot
	
	private static final long serialVersionUID = 1L;
	
	private static GUIHandler frame;
	private static JPanel panel;
	private static JPanel main;
	private static JButton startButton;
	
	public static boolean approval = false;
	
	public static void createWindow(String name, String icon) {
		// Create and set up the window
        frame = new GUIHandler(name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Display the window
        ImageIcon ico = new ImageIcon(icon);
        frame.setIconImage(ico.getImage());
        frame.pack();
        frame.setVisible(true);
        frame.setSize(250,650);
	}
	
	public GUIHandler(String name) {
        super(name);

        panel = generatepanel();
        
        main = new JPanel();
        main.setLayout(new CardLayout());

        main.add(panel);
        
        add(main);
        
        
        // Handle exit
        addWindowListener(new WindowListener() {
        	public void windowClosing(WindowEvent we) {
        		System.exit(1);
        		if(TwitchChat.connected) {
        			try{
        				TwitchChat.deactivate();
        			}catch(Exception e) {
        				System.err.println("Oops: " + e);
        			}
        		}
        	}
        	public void windowIconified(WindowEvent we) {}
			public void windowActivated(WindowEvent we) {}
			public void windowClosed(WindowEvent we) {}
			public void windowDeactivated(WindowEvent we) {}
			public void windowDeiconified(WindowEvent we) {}
			public void windowOpened(WindowEvent we) {}
        });
    }
	
	private static JButton generateStartButton() {
		JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent ae) {
        		// Stop the bot
        		if(TwitchChat.connected) {
        			try{
        				TwitchChat.deactivate();
        				Player.saveAll();
						startButton.setText("Start");
        			}catch(Exception e) {
        				System.err.println("Oops: " + e);
        			}
        		}
        		// Start the bot
        		else {
					try{
						AccountsManager.updateAll();
						TwitchChat.initialize();
						startButton.setText("Stop");
					}catch(Exception e) {
						System.err.println("Error: " + e);
						JOptionPane.showMessageDialog(null,"Failed to initialize...perhaps no internet?");
					}
        		}
        	}
        });
        return startButton;
	}
	
	private static JPanel generatepanel() {
		
		JPanel jp = new JPanel();
		jp.setLayout(new GridLayout(26,1));
        
        // Put on a title label
        jp.add(new JLabel("                BeanBetBot Twitch Bot                ", SwingConstants.CENTER));
		
		// Entry for what channel the bot should be in
        JTextField chatChannel = new JTextField(20);
        if(!AccountsManager.getChatChannel().substring(1).equals("ailed D:"))
        	chatChannel.setText(AccountsManager.getChatChannel().substring(1));
        jp.add(chatChannel);
        // Button to confirm channel
        JButton channelButton = new JButton("Set Chat Channel");
        jp.add(channelButton);
        channelButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent ae) {
        		AccountsManager.setChatChannel(chatChannel.getText());
        	}
        });
        
        // Entry for the bot's name
        JTextField botName = new JTextField(20);
        if(!AccountsManager.getBotName().equals("Failed D:"))
        	botName.setText(AccountsManager.getBotName());
        jp.add(botName);
        // Button to confirm bot's name
        JButton botNameButton = new JButton("Set Bot Name");
        jp.add(botNameButton);
        botNameButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent ae) {
        		AccountsManager.setBotName(botName.getText());
        	}
        });
        
        // Entry for bot oauth code (will be obscured very simply)
        JTextField botOauth = new JTextField(20);
        if(!AccountsManager.getBotOauth().equals("Failed D:"))
        	botOauth.setText("****************");
        jp.add(botOauth);
        // Button to confirm bot's oauth code
        JButton botOauthButton = new JButton("Set Bot Oauth");
        jp.add(botOauthButton);
        botOauthButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent ae) {
        		AccountsManager.setBotOauth(botOauth.getText());
        		botOauth.setText("****************");
        	}
        });
        
        // Blank space for spacing
        JLabel blank = new JLabel("");
        jp.add(blank);

		// Entry to set the option A
		JTextField firstOption = new JTextField(20);
		firstOption.setText("Win");
		jp.add(firstOption);

		JButton firstOptionButton = new JButton("Set A");
		jp.add(firstOptionButton);
		firstOptionButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				BetHandler.options[0] = firstOption.getText();
				BetHandler.output();
			}
		});

		// Entry to set the option B
		JTextField secondOption = new JTextField(20);
		secondOption.setText("Loss");
		jp.add(secondOption);

		JButton secondOptionButton = new JButton("Set B");
		jp.add(secondOptionButton);
		secondOptionButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				BetHandler.options[1] = secondOption.getText();
				BetHandler.output();
			}
		});

		// Blank space for spacing
		blank = new JLabel("");
		jp.add(blank);

		// Entry to set the stakes
		JTextField odds = new JTextField(20);
		odds.setText("50");
		jp.add(odds);

		JButton setOddsButton = new JButton("Set Odds for A");
		jp.add(setOddsButton);
		setOddsButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				//System.out.println("DEBUG: " + setStakesButton.getText());
				BetHandler.oddsA = Integer.parseInt(odds.getText());
				BetHandler.output();
			}
		});

		// Blank space for spacing
		blank = new JLabel("");
		jp.add(blank);
        
        // Entry to set the stakes
        JTextField stakes = new JTextField(20);
		stakes.setText("100");
        jp.add(stakes);

		JButton setStakesButton = new JButton("Set Stakes");
		jp.add(setStakesButton);
		setStakesButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				//System.out.println("DEBUG: " + setStakesButton.getText());
				BetHandler.stakes = Integer.parseInt(stakes.getText());
				BetHandler.output();
			}
		});
        
        // Blank space for spacing
        blank = new JLabel("");
        jp.add(blank);

		JButton openButton = new JButton("Toggle Open");
		jp.add(openButton);
		openButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				BetHandler.open = !BetHandler.open;
				if (BetHandler.open) {
					TwitchChat.outsideMessage("Betting is open!");
				}
				else {
					TwitchChat.outsideMessage("Betting is closed!");
				}
				BetHandler.output();
			}
		});

		// Blank space for spacing
		blank = new JLabel("");
		jp.add(blank);
        
        JButton payAButton = new JButton("Pay A");
        jp.add(payAButton);
		payAButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent ae) {
				BetHandler.sellout('A');
				TwitchChat.outsideMessage("A won! Paid out.");
				BetHandler.output();
        	}
        });

		JButton payBButton = new JButton("Pay B");
		jp.add(payBButton);
		payBButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				BetHandler.sellout('B');
				TwitchChat.outsideMessage("B won! Paid out.");
				BetHandler.output();
			}
		});

		JButton refundButton = new JButton("Refund");
		jp.add(refundButton);
		refundButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				BetHandler.sellout('R');
				TwitchChat.outsideMessage("Oops! Refund given.");
				BetHandler.output();
			}
		});
        
        // Blank space for spacing
        blank = new JLabel("");
        jp.add(blank);
        
        // Button that toggles the bot on and off
		startButton = generateStartButton();
        jp.add(startButton);
        
        return jp;
        
	}
	
}
