package me.akraml.gamesbot.game.impl;

import me.akraml.gamesbot.GamesBotBootstrap;
import me.akraml.gamesbot.game.AbstractGame;
import me.akraml.gamesbot.game.GameChannel;
import me.akraml.gamesbot.utility.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

public class DismantlingGame extends AbstractGame {

    private final GameChannel gameChannel;
    private final List<String> sentences = new ArrayList<>();
    private final Random random = new Random();
    private String latestAnswer;

    public DismantlingGame(GameChannel gameChannel) {
        super("-تفكيك");
        this.gameChannel = gameChannel;
        // Load sentences
        final GamesBotBootstrap bootstrap = GamesBotBootstrap.getInstance();
        sentences.addAll(bootstrap.getConfig().getStringList("games.dismantling"));
    }

    @Override
    public void handle(MessageReceivedEvent event) {
        this.lastStart = System.currentTimeMillis();
        final String message = sentences.get(random.nextInt(sentences.size()));
        latestAnswer = StringUtils.dismantle(message);
        final MessageEmbed embed = new EmbedBuilder()
                .setAuthor("ألعاب", null, event.getGuild().getIconUrl())
                .setTitle("تفكيك")
                .setDescription("**✦ " + StringUtils.addSymbolBetweenSpaces(message) + "**")
                .setFooter("®Vast")
                .setColor(0xADD8E6)
                .build();
        event.getChannel().sendMessageEmbeds(embed).queue();
        gameChannel.getGameTimer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (gameChannel.getGameTime().incrementAndGet() >= 30) {
                    gameChannel.handleTimeout(event);
                }
            }
        }, 0L, 1000L);
    }

    @Override
    public boolean answerMatches(String answer) {
        return latestAnswer != null && (latestAnswer.equalsIgnoreCase(answer)
                || latestAnswer.equalsIgnoreCase(answer.replace("أ", "ا"))
                || latestAnswer.equalsIgnoreCase(answer.replace("إ", "ا"))
                || latestAnswer.equalsIgnoreCase(answer.replace("آ", "ا")));
    }
}
