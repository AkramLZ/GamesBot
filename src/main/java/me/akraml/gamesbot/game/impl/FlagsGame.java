package me.akraml.gamesbot.game.impl;

import me.akraml.gamesbot.GamesBotBootstrap;
import me.akraml.gamesbot.game.AbstractGame;
import me.akraml.gamesbot.game.GameChannel;
import me.akraml.gamesbot.game.GameManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.*;

public class FlagsGame extends AbstractGame {

    private final GameChannel gameChannel;
    private final Map<String, String> flags = new HashMap<>();
    private final Random random = new Random();
    private String latestAnswer;

    public FlagsGame(GameChannel gameChannel) {
        super("-اعلام");
        this.gameChannel = gameChannel;
        // Load flags
        final GamesBotBootstrap bootstrap = GamesBotBootstrap.getInstance();
        for (final String string : bootstrap.getConfig().getStringList("games.flags")) {
            final String[] values = string.split("\\|");
            flags.put(values[0], values[1]);
        }
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
        gameChannel.getGameTimer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (gameChannel.getGameTime().incrementAndGet() >= 10) {
                    gameChannel.handleTimeout(event);
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