package me.akraml.gamesbot.game.impl.command;

import me.akraml.gamesbot.GamesBotBootstrap;
import me.akraml.gamesbot.game.AbstractGame;
import me.akraml.gamesbot.utility.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class TopCommand extends AbstractGame {

    public TopCommand() {
        super("-توب");
    }

    @Override
    public void handle(MessageReceivedEvent event) {
        final GamesBotBootstrap bootstrap = GamesBotBootstrap.getInstance();
        List<Pair<String, Integer>> top = bootstrap.getStorage().getTop();
        final StringBuilder namesBuilder = new StringBuilder(), pointsBuilder = new StringBuilder();
        final EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(event.getGuild().getName(), null, event.getGuild().getIconUrl())
                .setTitle("**أفضل اللاعبين بالسيرفر**");
        int i = 1;
        for (Pair<String, Integer> pair : top) {
            if (i != 0) {
                namesBuilder.append("\n");
                pointsBuilder.append("\n");
            }
            namesBuilder.append("**#").append(i).append(".** ").append(pair.getKey());
            pointsBuilder.append(pair.getValue());
            i++;
        }
        builder.addField("**اللاعبين**", namesBuilder.toString(), true)
                        .addField("**النقاط**", pointsBuilder.toString(), true);
        event.getChannel().sendMessageEmbeds(builder.build()).queue();
    }

    @Override
    public boolean answerMatches(String answer) {
        return false;
    }

    @Override
    public boolean canBypassCurrentGame() {
        return true;
    }
}
