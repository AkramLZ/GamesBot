package me.akraml.gamesbot.game.impl;

import me.akraml.gamesbot.game.AbstractGame;
import me.akraml.gamesbot.game.GameManager;
import me.akraml.gamesbot.utility.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

public class SentencesGame extends AbstractGame {

    private final GameManager gameManager;
    private final List<String> sentences = new ArrayList<>();
    private final Random random = new Random();
    private String latestAnswer;

    public SentencesGame(GameManager gameManager) {
        super("-جمل");
        this.gameManager = gameManager;
        // for debug purpose
        sentences.add("من علمني حرفا صرت له عبدا");
        sentences.add("من طلب العلا سهر الليالي");
        sentences.add("عبدالحق اشعري مجنون ياخي");
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
        gameManager.getGameTimer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (gameManager.getGameTime().incrementAndGet() >= 15) {
                    gameManager.handleTimeout(event);
                }
            }
        }, 0L, 1000L);
    }

    @Override
    public boolean answerMatches(String answer) {
        return latestAnswer != null && latestAnswer.equalsIgnoreCase(answer);
    }
}
