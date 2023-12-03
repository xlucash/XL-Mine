package me.xlucash.xlmine.commands;

import me.xlucash.xlmine.config.ConfigManager;
import me.xlucash.xlmine.database.DatabaseManager;
import me.xlucash.xlmine.guis.EventGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EventCommand implements CommandExecutor {
    private final DatabaseManager databaseManager;
    private final ConfigManager configManager;

    public EventCommand(DatabaseManager databaseManager, ConfigManager configManager) {
        this.databaseManager = databaseManager;
        this.configManager = configManager;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(commandSender instanceof Player)) {
            return true;
        }

        Player player = (Player) commandSender;

        if (args.length > 0 && args[0].equalsIgnoreCase("wrzuc")) {
            if (args.length < 2) {
                commandSender.sendMessage("§cUżycie: /mikolaj wrzuc <ilość gramow>");
                return true;
            }

            int amount;
            int playerAmount = databaseManager.getCoalAmount(player.getUniqueId());
            int currentEventAmount = configManager.getCoalGoal() - databaseManager.getEventCoalAmount();
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                commandSender.sendMessage("§cIlość węgla musi być liczbą!");
                return true;
            }

            if (amount < 100) {
                commandSender.sendMessage("§cNie możesz wrzucić mniej niż 100 gramów węgla!");
                return true;
            }

            if (amount > playerAmount) {
                commandSender.sendMessage("§cNie możesz wrzucić więcej węgla niż posiadasz!");
                return true;
            }

            if (amount > currentEventAmount) {
                amount = currentEventAmount;
            }

            if(databaseManager.addCoalToEventBackpack(amount)) {
                commandSender.sendMessage("§aWrzuciłeś " + amount + " gramów węgla do §cPieca Mikołaja!");
                databaseManager.setCoalAmount(player.getUniqueId(), playerAmount - amount);
                if (databaseManager.getEventCoalAmount() == configManager.getCoalGoal()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), configManager.getGoalCommand());
                    Bukkit.broadcastMessage(" ");
                    Bukkit.broadcastMessage("           §m      §7[ §c§lPiec Mikolaja §7]§7§m     ");
                    Bukkit.broadcastMessage("       §fUdalo wam sie uzupelnic piec!");
                    Bukkit.broadcastMessage("       §fCaly serwer otrzymuje §6nagrode§f!");
                    Bukkit.broadcastMessage(" ");
                }
            } else {
                commandSender.sendMessage("§cPiec Mikołaja jest pełny!");
            }

            return true;
        } else {
            new EventGUI(databaseManager).openFor(player);
            return true;
        }
    }
}
