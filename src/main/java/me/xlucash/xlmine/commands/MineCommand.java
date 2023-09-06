package me.xlucash.xlmine.commands;

import me.xlucash.xlmine.config.ConfigManager;
import me.xlucash.xlmine.database.DatabaseManager;
import me.xlucash.xlmine.guis.BackpackGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MineCommand implements CommandExecutor {
    private final DatabaseManager databaseManager;
    private final ConfigManager configManager;

    public MineCommand(DatabaseManager databaseManager, ConfigManager configManager) {
        this.databaseManager = databaseManager;
        this.configManager = configManager;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(commandSender instanceof Player)) {
            return true;
        }

        Player player = (Player) commandSender;

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (!player.hasPermission( "mine.reload")) {
                player.sendMessage("§cNie masz uprawnien do tej komendy!");
                return true;
            }
            configManager.reloadConfig();
            player.sendMessage("§a[XL-Mine] §fPlugin reloaded!");
            Bukkit.getServer().getConsoleSender().sendMessage("§a[XL-Mine] §fPlugin reloaded!");
        } else {
            new BackpackGUI(databaseManager, configManager).open((Player) commandSender);
            return true;
        }
        return false;
    }
}
