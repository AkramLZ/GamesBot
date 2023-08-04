package me.akraml.gamesbot.game.impl;

import me.akraml.gamesbot.GamesBotBootstrap;
import me.akraml.gamesbot.game.AbstractGame;
import me.akraml.gamesbot.game.GameChannel;
import me.akraml.gamesbot.game.GameManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.*;
import java.util.stream.Collectors;

public class TranslateGame extends AbstractGame {

    private final GameChannel gameChannel;
    private final Map<String, Set<String>> words = new HashMap<>();
    private final Random random = new Random();
    private Set<String> latestAnswer;

    public TranslateGame(GameChannel gameChannel) {
        super("-ترجم");
        this.gameChannel = gameChannel;
        // Load words
        final GamesBotBootstrap bootstrap = GamesBotBootstrap.getInstance();
        for (final String string : bootstrap.getConfig().getStringList("games.translate")) {
            final List<String> list = new ArrayList<>(Arrays.asList(string.split("\\|")));
            final String word = list.get(0);
            list.remove(0);
            words.put(word, new HashSet<>(list));
        }
    }

    @Override
    public void handle(MessageReceivedEvent event) {
        this.lastStart = System.currentTimeMillis();
        Map.Entry<String, Set<String>> answer = getRandomEntry();
        if (answer == null) return; // Impossible
        latestAnswer = answer.getValue();
        final MessageEmbed embed = new EmbedBuilder()
                .setAuthor("ألعاب", null, event.getGuild().getIconUrl())
                .setTitle("ترجم")
                .setDescription("㊙\uFE0F **ترجم الكلمة التالية :**" +
                        "\n" +
                        "\n**" + answer.getKey() + "**")
                .setFooter("English & Arabic")
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

    private Map.Entry<String, Set<String>> getRandomEntry() {
        Set<Map.Entry<String, Set<String>>> entrySet = words.entrySet();
        final int randomSlot = random.nextInt(entrySet.size());
        int i = 0;
        for (final Map.Entry<String, Set<String>> entry : entrySet) {
            if (i == randomSlot) {
                return entry;
            }
            i++;
        }
        // impossible
        return entrySet.stream().findFirst().orElse(null);
    }

    @Override
    public boolean answerMatches(String answer) {
        return latestAnswer != null && latestAnswer.contains(answer);
    }
}