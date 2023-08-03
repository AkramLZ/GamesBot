package me.akraml.gamesbot;

import me.akraml.gamesbot.game.GameManager;
import me.akraml.gamesbot.game.impl.*;
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
    private final GameManager gameManager = new GameManager();

    public GamesBotBootstrap() {
        /*final File configFile = new File("config.yml");
        if (!configFile.exists()) {
            try (final InputStream stream = GamesBot.class.getResourceAsStream("config.yml")) {
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
        }*/
    }

    public void start() {
        botInstance = JDABuilder
                .createDefault("MTEzNjY5MzE5NjI4MjQwMDkxOQ.GUWDrn.wJmWcfu5ATtPF9-hj6s4pC2a2OehYr8hN1M3rQ")
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();
        botInstance.addEventListener(new MessageListener(this));
        gameManager.registerGame(new SentencesGame(gameManager));
        gameManager.registerGame(new DismantlingGame(gameManager));
        gameManager.registerGame(new DismantleGame(gameManager));
        gameManager.registerGame(new ArticlesGame(gameManager));
        gameManager.registerGame(new TranslateGame(gameManager));
        gameManager.registerGame(new FastGame(gameManager));
        gameManager.registerGame(new FlagsGame(gameManager));
    }

    public void shutdown() {

    }

    public GameManager getGameManager() {
        return gameManager;
    }

}
