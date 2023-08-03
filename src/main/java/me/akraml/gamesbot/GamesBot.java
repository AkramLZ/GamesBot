package me.akraml.gamesbot;

import java.util.logging.Logger;

public class GamesBot {

    private static final Logger LOGGER = Logger.getLogger("main");

    public static void main(String... args) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tT] [%4$-3s] %5$s%n");
        GamesBotBootstrap bootstrap = new GamesBotBootstrap();
        bootstrap.start();
        Runtime.getRuntime().addShutdownHook(new Thread(bootstrap::shutdown));
    }

    public static Logger getLogger() {
        return LOGGER;
    }

}
