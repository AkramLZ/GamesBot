package me.akraml.gamesbot.game.impl;

import me.akraml.gamesbot.game.AbstractGame;
import me.akraml.gamesbot.game.GameManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.*;

public class FlagsGame extends AbstractGame {

    private final GameManager gameManager;
    private final Map<String, String> flags = new HashMap<>();
    private final Random random = new Random();
    private String latestAnswer;

    public FlagsGame(GameManager gameManager) {
        super("-اعلام");
        this.gameManager = gameManager;
        flags.put("باكستان", "https://media.discordapp.net/attachments/991426456938872853/1136722188137926746/437178805677981696.png");
    }

    @Override
    public void handle(MessageReceivedEvent event) {
        this.lastStart = System.currentTimeMillis();
        Map.Entry<String, String> answer = getRandomEntry();
        latestAnswer = answer.getKey();
        final MessageEmbed embed = new EmbedBuilder()
                .setAuthor("ألعاب", null, event.getGuild().getIconUrl())
                .setTitle("أعلام دول")
                .setDescription("**ماهو أسم الدولة الظاهره في الصورة.؟ \uD83C\uDFF3\uFE0F**" +
                        "\n")
                .setImage(answer.getValue())
                .setFooter("®Vast")
                .setColor(0xADD8E6)
                .build();
        event.getChannel().sendMessageEmbeds(embed).queue();
        gameManager.getGameTimer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (gameManager.getGameTime().incrementAndGet() >= 10) {
                    gameManager.handleTimeout(event);
                }
            }
        }, 0L, 1000L);
    }

    private Map.Entry<String, String> getRandomEntry() {
        Set<Map.Entry<String, String>> entrySet = flags.entrySet();
        final int randomSlot = random.nextInt(entrySet.size());
        int i = 0;
        for (final Map.Entry<String, String> entry : entrySet) {
            if (i == randomSlot) {
                return entry;
            }
            i++;
        }
        // impossible
        return entrySet.stream().findFirst().orElseThrow();
    }

    @Override
    public boolean answerMatches(String answer) {
        return latestAnswer != null && latestAnswer.equalsIgnoreCase(answer);
    }
}