package me.akraml.gamesbot.game;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface Game {

    String getCommand();

    void handle(MessageReceivedEvent event);

    boolean isStarted();

    void setStarted(boolean started);

    boolean answerMatches(String answer);

    long getLastStart();

    boolean canBypassCurrentGame();

}
