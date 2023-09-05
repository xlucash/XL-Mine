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
    }

    public void reloadConfig() {
        plugin.reloadConfig();
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
}
