package me.akraml.gamesbot.game.impl;

import me.akraml.gamesbot.GamesBotBootstrap;
import me.akraml.gamesbot.game.AbstractGame;
import me.akraml.gamesbot.game.GameChannel;
import me.akraml.gamesbot.utility.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.*;

public class SentencesGame extends AbstractGame {

    private final GameChannel gameChannel;
    private final List<String> sentences = new ArrayList<>();
    private final Random random = new Random();
    private String latestAnswer;

    public SentencesGame(GameChannel gameChannel) {
        super("-جمل");
        this.gameChannel = gameChannel;
        // Load sentences
        final GamesBotBootstrap bootstrap = GamesBotBootstrap.getInstance();
        sentences.addAll(bootstrap.getConfig().getStringList("games.sentences"));
    }

    @Override
    public void handle(MessageReceivedEvent event) {
        this.lastStart = System.currentTimeMillis();
        final String message = sentences.get(random.nextInt(sentences.size()));
        latestAnswer = message;
        final MessageEmbed embed = new EmbedBuilder()
                .setAuthor("ألعاب", null, event.getGuild().getIconUrl())
                .setTitle("كتابة جمل")
                .setDescription("**✦ " + StringUtils.addSymbolBetweenSpaces(message) + "**")
                .setFooter("أكتب الجملة بدون رموز")
                .setColor(0xADD8E6)
                .build();
        event.getChannel().sendMessageEmbeds(embed).queue();
        gameChannel.getGameTimer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (gameChannel.getGameTime().incrementAndGet() >= 25) {
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
