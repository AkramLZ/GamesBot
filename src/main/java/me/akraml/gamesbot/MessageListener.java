package me.akraml.gamesbot;

import me.akraml.gamesbot.game.Game;
import me.akraml.gamesbot.game.GameChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class MessageListener implements EventListener {

    private final GamesBotBootstrap bootstrap;

    public MessageListener(GamesBotBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    public void onMessage(final MessageReceivedEvent event) {
        if (!event.isFromGuild())
            return;
        final String message = event.getMessage().getContentRaw();
        final GameChannel channel = bootstrap.getGameManager().getChannel(event.getChannel().getIdLong());
        if (channel == null)
            return;
        Game game = channel.getByCommand(message);
        if (game == null) {
            game = channel.getCurrentGame();
            if (game == null)
                return;
            if (game.answerMatches(message)) {
                channel.handleWin(event);
            }
            return;
        }
        if (channel.getCurrentGame() != null) {
            event.getMessage().reply("**لا يمكنك بدء لعبة جديدة في حين أن هناك لعبة اخرى قيد التشغيل!**").complete();
            return;
        }
        channel.setCurrentGame(game);
        game.handle(event);
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof MessageReceivedEvent) {
            onMessage((MessageReceivedEvent) event);
        }
    }
}
