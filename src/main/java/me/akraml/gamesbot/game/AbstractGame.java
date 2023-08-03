package me.akraml.gamesbot.game;

public abstract class AbstractGame implements Game {

    private final String command;
    private boolean started;
    protected long lastStart = System.currentTimeMillis();

    public AbstractGame(String command) {
        this.command = command;
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public void setStarted(boolean started) {
        this.started = started;
    }

    @Override
    public long getLastStart() {
        return lastStart;
    }
}
