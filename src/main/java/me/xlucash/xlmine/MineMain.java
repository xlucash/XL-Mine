package me.xlucash.xlmine;

import me.xlucash.xlmine.commands.MineCommand;
import me.xlucash.xlmine.config.ConfigManager;
import me.xlucash.xlmine.database.DatabaseManager;
import me.xlucash.xlmine.hooks.PlaceholderAPIHook;
import me.xlucash.xlmine.hooks.VaultHook;
import me.xlucash.xlmine.listeners.BlockBreakListener;
import me.xlucash.xlmine.listeners.BlockPlaceListener;
import me.xlucash.xlmine.listeners.InventoryClickListener;
import me.xlucash.xlmine.listeners.ServerLoadListener;
import me.xlucash.xlmine.utils.CoalPriceManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MineMain extends JavaPlugin {
    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private VaultHook vaultHook;
    private PlaceholderAPIHook placeholderAPIHook;
    private CoalPriceManager coalPriceManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.loadConfig();

        databaseManager = new DatabaseManager(this);

        vaultHook = new VaultHook();
        vaultHook.setupEconomy();

        placeholderAPIHook = new PlaceholderAPIHook(this);
        placeholderAPIHook.register();

        coalPriceManager = new CoalPriceManager(configManager, this, databaseManager);

        getServer().getPluginManager().registerEvents(new ServerLoadListener(configManager.getWorldName()), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this, configManager, databaseManager), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(configManager), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(databaseManager, configManager), this);

        getCommand("gornik").setExecutor(new MineCommand(databaseManager, configManager));
    }

    @Override
    public void onDisable() {
        if(databaseManager != null) {
            databaseManager.closeDatabaseConnection();
        }
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
