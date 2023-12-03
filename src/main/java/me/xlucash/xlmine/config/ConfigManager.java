package me.xlucash.xlmine.config;

import me.xlucash.xlmine.MineMain;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final MineMain plugin;
    private FileConfiguration config;

    public ConfigManager(MineMain plugin) {
        this.plugin = plugin;
        config = plugin.getConfig();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    public String getWorldName() {
        return config.getString("world-name");
    }

    public String getDatabaseType() {
        return config.getString("database.type");
    }

    public String getMySQLHost() {
        return config.getString("database.mysql.host");
    }

    public int getMySQLPort() {
        return config.getInt("database.mysql.port");
    }

    public String getMySQLDatabase() {
        return config.getString("database.mysql.database");
    }

    public String getMySQLUsername() {
        return config.getString("database.mysql.username");
    }

    public String getMySQLPassword() {
        return config.getString("database.mysql.password");
    }

    public int getMaxBackpackCapacity() {
        return config.getInt("backpack-capacity");
    }

    public int getMinCoalDrop() {
        return config.getInt("coal-amount.min");
    }

    public int getMaxCoalDrop() {
        return config.getInt("coal-amount.max");
    }

    public int getMinCoalPrice() {
        return config.getInt("coal-price.min");
    }

    public int getMaxCoalPrice() {
        return config.getInt("coal-price.max");
    }
    public String getGoalCommand() {
        return config.getString("event-goal-command");
    }
    public int getCoalGoal() { return config.getInt("event-goal-coal"); }
}
