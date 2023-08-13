package me.akraml.gamesbot.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.akraml.gamesbot.utility.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Storage {

    public static final ExecutorService DATABASE_POOL = Executors.newCachedThreadPool();
    private HikariDataSource dataSource;

    public Storage() {
        try {
            final HikariConfig config = new HikariConfig();
            config.setDriverClassName("org.h2.Driver");
            config.setJdbcUrl("jdbc:h2:~/database.db");
            config.setUsername("gamesbot");
            config.setPassword("");
            this.dataSource = new HikariDataSource(config);
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS games_points (TAG VARCHAR(64), ID BIGINT, POINTS INT)"
            )) {
                statement.executeUpdate();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public int getPoints(String tag, long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM games_points WHERE ID = ?"
        )) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("POINTS");
                }
                try (PreparedStatement statement1 = connection.prepareStatement(
                        "INSERT INTO games_points (TAG, ID, POINTS) VALUES (?, ?, ?)"
                )) {
                    statement1.setString(1, tag);
                    statement1.setLong(2, id);
                    statement1.setInt(3, 0);
                    statement1.executeUpdate();
                }
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    public void setPoints(long id, int points) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                "UPDATE games_points SET POINTS = ? WHERE ID = ?"
        )) {
            statement.setInt(1, points);
            statement.setLong(2, id);
            statement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public List<Pair<String, Integer>> getTop() {
        final List<Pair<String, Integer>> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM games_points ORDER BY POINTS DESC LIMIT 10"
        ); ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                list.add(new Pair<>(resultSet.getString("TAG"), resultSet.getInt("POINTS")));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return list;
    }

    public int getPosition(long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                "SELECT COUNT(*) AS position FROM games_points WHERE POINTS > (SELECT POINTS FROM games_points WHERE ID = ?)"
        )) {
            statement.setLong(1, id);
            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("position");
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }

}
