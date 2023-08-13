package me.akraml.gamesbot;

import me.akraml.gamesbot.game.GameChannel;
import me.akraml.gamesbot.game.GameManager;
import me.akraml.gamesbot.game.impl.*;
import me.akraml.gamesbot.game.impl.command.HelpCommand;
import me.akraml.gamesbot.game.impl.command.TopCommand;
import me.akraml.gamesbot.storage.Storage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;

public class GamesBotBootstrap {

    private JDA botInstance;
    private YamlConfiguration config;
    private final Storage storage;
    private final GameManager gameManager = new GameManager();
    private static GamesBotBootstrap INSTANCE;

    public GamesBotBootstrap() {
        INSTANCE = this;
        storage = new Storage();
        final File configFile = new File("config.yml");
        if (!configFile.exists()) {
            try (final InputStream stream = GamesBot.class.getResourceAsStream("/config.yml")) {
                if (stream != null) Files.copy(stream, configFile.toPath());
            } catch (Exception exception) {
                GamesBot.getLogger().log(Level.SEVERE, "Failed to copy config", exception);
                System.exit(1);
                return;
            }
        }
        try {
            config = YamlConfiguration.loadConfiguration(configFile);
        } catch (Exception exception) {
            GamesBot.getLogger().log(Level.SEVERE, "Failed to load config", exception);
            System.exit(1);
        }
    }

    public static GamesBotBootstrap getInstance() {
        return INSTANCE;
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public Storage getStorage() {
        return storage;
    }

    public void start() {
        botInstance = JDABuilder
                .createDefault(getConfig().getString("token"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();
        botInstance.addEventListener(new MessageListener(this));
        for (final long channelId : getConfig().getLongList("channels")) {
            gameManager.registerChannel(channelId);
            final GameChannel gameChannel = gameManager.getChannel(channelId);
            gameChannel.registerGames(
                    new SentencesGame(gameChannel),
                    new DismantlingGame(gameChannel),
                    new DismantleGame(gameChannel),
                    new ArticlesGame(gameChannel),
                    new TranslateGame(gameChannel),
                    new FastGame(gameChannel),
                    new FlagsGame(gameChannel),
                    // Commands
                    new TopCommand(),
                    new HelpCommand()
            );
        }
    }

    public void shutdown() {
        botInstance.shutdown();
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public void saveConfig() {
        try {
            config.save(new File("config.yml"));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
