package me.akraml.gamesbot;

import me.akraml.gamesbot.game.Game;
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
        System.out.println("[DEBUG] " + message);
        Game game = bootstrap.getGameManager().getByCommand(message);
        if (game == null) {
            game = bootstrap.getGameManager().getCurrentGame();
            if (game == null)
                return;
            if (game.answerMatches(message)) {
                bootstrap.getGameManager().handleWin(event);
            }
            return;
        }
        if (bootstrap.getGameManager().getCurrentGame() != null) {
            event.getMessage().reply("**لا يمكنك بدء لعبة جديدة في حين أن هناك لعبة اخرى قيد التشغيل!**").complete();
            return;
        }
        bootstrap.getGameManager().setCurrentGame(game);
        game.handle(event);
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof MessageReceivedEvent) {
            onMessage((MessageReceivedEvent) event);
        }
    }
}
