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

public class ArticlesGame extends AbstractGame {

    private final GameManager gameManager;
    private final List<String> sentences = new ArrayList<>();
    private final Random random = new Random();
    private String latestAnswer;

    public ArticlesGame(GameManager gameManager) {
        super("-مقالات");
        this.gameManager = gameManager;
        // for debug purpose
        sentences.add("يروى ان اعرابيا تخنث فراوده اخاه قائلا هاك وماك مالي اراك مطئطئا راسكا رافع وراكا");
        sentences.add("فرد عليه مبتسما هاك وماك وما ادراكا فلو عرف النائك لذة المنتاك لنتاك");
        sentences.add("فاعجب الاعرابي بكلام اخيه فتخنث معه");
    }

    @Override
    public void handle(MessageReceivedEvent event) {
        this.lastStart = System.currentTimeMillis();
        final String message = sentences.get(random.nextInt(sentences.size()));
        latestAnswer = message;
        final MessageEmbed embed = new EmbedBuilder()
                .setAuthor("ألعاب", null, event.getGuild().getIconUrl())
                .setTitle("مقال")
                .setDescription("**✦ " + StringUtils.addSymbolBetweenSpaces(message) + "**")
                .setFooter("أكتب المقالة بدون رموز")
                .setColor(0xADD8E6)
                .build();
        event.getChannel().sendMessageEmbeds(embed).queue();
        gameManager.getGameTimer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (gameManager.getGameTime().incrementAndGet() >= 20) {
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
