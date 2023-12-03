package me.xlucash.xlmine.database;

import me.xlucash.xlmine.MineMain;
import me.xlucash.xlmine.config.ConfigManager;
import me.xlucash.xlmine.database.impl.MySQLDatabase;
import me.xlucash.xlmine.database.impl.SQLiteDatabase;

import java.sql.*;
import java.util.UUID;

public class DatabaseManager {
    private Database database;
    private final ConfigManager configManager;

    public DatabaseManager(MineMain plugin) {
        this.configManager = new ConfigManager(plugin);

        String databaseType = configManager.getDatabaseType();
        if (databaseType.equalsIgnoreCase("mysql")) {
            database = new MySQLDatabase(configManager);
        } else if (databaseType.equalsIgnoreCase("sqlite")) {
            database = new SQLiteDatabase();
        }

        database.setup();
        createTablesIfNotExist();
    }

    private void createTablesIfNotExist() {
        try (Connection connection = database.getConnection(); Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS miners (uuid VARCHAR(36) PRIMARY KEY, coal INT, lastSold TIMESTAMP)");
                statement.execute("CREATE TABLE IF NOT EXISTS coal_price (price DOUBLE)");
                statement.execute("CREATE TABLE IF NOT EXISTS coal_event (coal INT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getDatabaseConnection() {
        database.getConnection();
    }

    public void closeDatabaseConnection() {
        if(database != null) {
            database.closeConnection();
        }
    }

    public boolean addCoalToPlayer(UUID playerUUID, int amount) {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT coal FROM miners WHERE uuid = ?")) {
            statement.setString(1, playerUUID.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int currentCoal = resultSet.getInt("coal");

                if (currentCoal >= configManager.getMaxBackpackCapacity()) {
                    return false;
                }

                int newCoalAmount = Math.min(currentCoal + amount, configManager.getMaxBackpackCapacity());

                try (PreparedStatement updateStatement = connection.prepareStatement("UPDATE miners SET coal = ? WHERE uuid = ?")) {
                    updateStatement.setInt(1, newCoalAmount);
                    updateStatement.setString(2, playerUUID.toString());
                    updateStatement.executeUpdate();
                }
            } else {
                int newCoalAmount = Math.min(amount, configManager.getMaxBackpackCapacity());

                try (PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO miners (uuid, coal) VALUES (?, ?)")) {
                    insertStatement.setString(1, playerUUID.toString());
                    insertStatement.setInt(2, newCoalAmount);
                    insertStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    public int getCoalAmount(UUID playerUUID) {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT coal FROM miners WHERE uuid = ?")) {
            statement.setString(1, playerUUID.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("coal");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void setCoalAmount(UUID playerUUID, int amount) {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE miners SET coal = ? WHERE uuid = ?")) {
            statement.setInt(1, amount);
            statement.setString(2, playerUUID.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getCoalPrice() {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT price FROM coal_price LIMIT 1")) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return configManager.getMinCoalPrice();
    }

    public void setCoalPrice(double price) {
        try (Connection connection = database.getConnection()) {
            try (PreparedStatement updateStatement = connection.prepareStatement("UPDATE coal_price SET price = ?")) {
                updateStatement.setDouble(1, price);
                int affectedRows = updateStatement.executeUpdate();

                if (affectedRows == 0) {
                    try (PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO coal_price (price) VALUES (?)")) {
                        insertStatement.setDouble(1, price);
                        insertStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addCoalToEventBackpack(int amount) {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT coal FROM coal_event")) {
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int currentCoal = resultSet.getInt("coal");

                if (currentCoal >= configManager.getCoalGoal()) {
                    return false;
                }

                if (currentCoal + amount > configManager.getCoalGoal()) {
                    return false;
                }

                int newCoalAmount = Math.min(currentCoal + amount, configManager.getCoalGoal());

                try (PreparedStatement updateStatement = connection.prepareStatement("UPDATE coal_event SET coal = ?")) {
                    updateStatement.setInt(1, newCoalAmount);
                    updateStatement.executeUpdate();
                }
            } else {
                int newCoalAmount = Math.min(amount, configManager.getCoalGoal());

                try (PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO coal_event (coal) VALUES (?)")) {
                    insertStatement.setInt(1, newCoalAmount);
                    insertStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    public int getEventCoalAmount() {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT coal FROM coal_event LIMIT 1")) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("coal");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
