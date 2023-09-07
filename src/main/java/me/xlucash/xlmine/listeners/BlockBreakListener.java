package me.xlucash.xlmine.listeners;

import me.xlucash.xlmine.MineMain;
import me.xlucash.xlmine.config.ConfigManager;
import me.xlucash.xlmine.database.DatabaseManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Random;
import java.util.UUID;

public class BlockBreakListener implements Listener {
    private final ConfigManager configManager;
    private final MineMain plugin;
    private final DatabaseManager databaseManager;

    public BlockBreakListener(MineMain plugin, ConfigManager configManager, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.databaseManager = databaseManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getBlock().getWorld().getName().equals(configManager.getWorldName())) {
            return;
        }

        if(event.getBlock().getType() != Material.COAL_ORE) {
            event.setCancelled(true);
            return;
        }

        Material toolType = event.getPlayer().getInventory().getItemInMainHand().getType();
        if (!toolType.name().endsWith("_PICKAXE")) {
            event.setCancelled(true);
            return;
        }

        if (event.getBlock().getType() == Material.COAL_ORE) {
            event.setCancelled(true);
            event.getBlock().setType(Material.BEDROCK);

            UUID playerUUID = event.getPlayer().getUniqueId();
            int coalAmount = new Random().nextInt(configManager.getMinCoalDrop(), configManager.getMaxCoalDrop());
            if(databaseManager.addCoalToPlayer(playerUUID, coalAmount)) {
                event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§fWydobyłeś §a" + coalAmount + "g §fwęgla!"));
            } else {
                event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cTwój plecak jest pełen! Sprzedaj jego zawartość, aby kopać dalej!"));
            }

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                event.getBlock().setType(Material.COAL_ORE);
            }, 30 * 20);
        }
    }

}
