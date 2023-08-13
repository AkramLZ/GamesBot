package me.akraml.gamesbot.game.impl.command;

import me.akraml.gamesbot.game.AbstractGame;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HelpCommand extends AbstractGame {

    public HelpCommand() {
        super("-اوامر");
    }

    @Override
    public void handle(MessageReceivedEvent event) {
        event.getChannel().sendMessage(
                "https://media.discordapp.net/attachments/1088518726095224913/1140156225040949248/commands.png"
        ).queue();
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
