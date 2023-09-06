package me.xlucash.xlmine.listeners;

import me.xlucash.xlmine.config.ConfigManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
    private final ConfigManager configManager;

    public BlockPlaceListener(ConfigManager configManager) {
        this.configManager = configManager;
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().getWorld().getName().equals(configManager.getWorldName())) {
            event.setCancelled(true);
            return;
        }
    }
}
