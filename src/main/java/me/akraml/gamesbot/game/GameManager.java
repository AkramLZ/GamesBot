package me.akraml.gamesbot.game;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicInteger;

public class GameManager {

    private Timer gameTimer = new Timer();
    private final AtomicInteger gameTime = new AtomicInteger(0);
    private final Map<Class<?>, Game> gameMap = new HashMap<>();
    private Game currentGame;

    public void registerGame(Game game) {
        gameMap.put(game.getClass(), game);
    }

    public <G extends Game> G getGame(Class<G> gameClass) {
        return gameClass.cast(gameMap.get(gameClass));
    }

    public Game getByCommand(String command) {
        for (final Game game : gameMap.values()) {
            if (game.getCommand().equalsIgnoreCase(command)) {
                return game;
            }
        }
        return null;
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

    public Timer getGameTimer() {
        return gameTimer;
    }

    public AtomicInteger getGameTime() {
        return gameTime;
    }

    public void handleTimeout(MessageReceivedEvent event) {
        final MessageEmbed embed = new EmbedBuilder()
                .setDescription("**<:timeover:1136714218670932038> أنتهى زمن الإجابة**")
                .build();
        event.getChannel().sendMessageEmbeds(embed).queue();
        setCurrentGame(null);
        getGameTime().set(0);
        getGameTimer().cancel();
        gameTimer = new Timer();
    }

    public void handleWin(MessageReceivedEvent event) {
        final DecimalFormat decimalFormat = new DecimalFormat(".##");
        final long takenTime = System.currentTimeMillis() - currentGame.getLastStart();
        double takenTimeD = (double) takenTime;
        double toSeconds = takenTimeD / 1000;
        final String formattedTime = decimalFormat.format(toSeconds);
        final MessageEmbed embed = new EmbedBuilder()
                .setAuthor(event.getAuthor().getEffectiveName(), null, event.getAuthor().getAvatarUrl())
                .setDescription("**إجابة صحيحة** " + event.getAuthor().getAsMention() + " <:checked:1136706071579344896>"
                        + "\n"
                        + "\n<:win:1136706077103231037> **نقاطك: -1**"
                        + "\n"
                        + "\n<:timeout:1136706074817331220> **زمن الإجابة : " + formattedTime + " ثانية **"
                )
                .build();
        event.getChannel().sendMessageEmbeds(embed).queue();
        setCurrentGame(null);
        getGameTime().set(0);
        getGameTimer().cancel();
        gameTimer = new Timer();
    }

}
