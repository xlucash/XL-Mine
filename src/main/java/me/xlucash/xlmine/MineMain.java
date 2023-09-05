package me.xlucash.xlmine;

import me.xlucash.xlmine.config.ConfigManager;
import me.xlucash.xlmine.database.DatabaseConnectionManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MineMain extends JavaPlugin {
    private ConfigManager configManager;
    private DatabaseConnectionManager databaseManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.loadConfig();

        databaseManager = new DatabaseConnectionManager(this);
    }

    @Override
    public void onDisable() {
        if(databaseManager != null) {
            databaseManager.closeDatabaseConnection();
        }
    }
}
