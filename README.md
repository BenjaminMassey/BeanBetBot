# BeanBetBot

-------------------------------------------------
Copyright 2019 Benjamin Massey
-------------------------------------------------

## Summary

This is a bot that runs in a Twitch.tv stream's chat, taking bets from viewers.
The streamer will decide some bet between option A and option B, at some stakes of points, with some related odds.
Players will then bet on their preferred option with either !betA or !betB within the betting window.
Then, determined by the odds, the winners and losers will be handled acoordingly based on which option the streamer chooses has won.

## Viewer Interaction

The whole point of this is to increase viewer interaction by allowing them to bet on stream events.
This betting is in context of "points". Every user starts out with 100 points, and can build up points through winning bets, or by simply watching.
The extent to which they can earn points is up to the streamer, who sets the stakes, odds and frequency of bets.

## Code

This particular project is essentially a modified version of another project I've made, [BeanBoyBot](https://www.github.com/BenjaminMassey/BeanBoyBot/).
A large portion of the code is simply copied from there.

This project is all written in Java, using Pircbot as base:
http://www.jibble.org/pircbot.php

Make sure to check out pircbot, it makes a lot of the silly Twitch interaction a whole lot easier.

You're going to need to have your own account setup to act as the Twitch bot, and figure out how to get an API key and whatnot for it.
Google should make all that fairly easy.

## Images

Image of the bot's interface:

![Image of the bot's interface](https://i.imgur.com/IkGj4jJ.png)

Usage Example Screenshot:

![Usage Example Screenshot:](https://i.imgur.com/31is2yS.jpg)

## Contact

Any and all questions should be directed to benjamin.w.massey@gmail.com - I'd love to talk about my silly little bot.

I also stream at https://www.twitch.tv/BeanSSBM and will use this bot sometimes - feel free to just hop in and ask questions, or send me a whisper.