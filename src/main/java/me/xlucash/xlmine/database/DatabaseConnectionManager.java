package me.xlucash.xlmine.database;

import me.xlucash.xlmine.MineMain;
import me.xlucash.xlmine.config.ConfigManager;
import me.xlucash.xlmine.database.impl.MySQLDatabase;
import me.xlucash.xlmine.database.impl.SQLiteDatabase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnectionManager {
    private Database database;
    private final ConfigManager configManager;

    public DatabaseConnectionManager(MineMain plugin) {
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
}
