package me.xlucash.xlmine.hooks;

import me.xlucash.xlmine.MineMain;
import me.xlucash.xlmine.placeholders.MinePlaceholders;
import org.bukkit.Bukkit;

public class PlaceholderAPIHook {
    private static MineMain plugin;

    public PlaceholderAPIHook(MineMain plugin) {
        PlaceholderAPIHook.plugin = plugin;
    }

    public static void register() {
        if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new MinePlaceholders(plugin).register();
        }
    }

}
