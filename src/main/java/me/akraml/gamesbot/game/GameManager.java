package me.akraml.gamesbot.game;

import java.util.HashMap;
import java.util.Map;

public class GameManager {

    private final Map<Long, GameChannel> channelMap = new HashMap<>();

    public void registerChannel(final long channelId) {
        channelMap.put(channelId, new GameChannel(channelId));
    }

    public GameChannel getChannel(long channelId) {
        return channelMap.get(channelId);
    }

}
