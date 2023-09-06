package me.xlucash.xlmine.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

public class ServerLoadListener implements Listener {
    private final String worldName;

    public ServerLoadListener(String worldName) {
        this.worldName = worldName;
    }

    @EventHandler
    public void onServerLoad(ServerLoadEvent event) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) return;

        int worldSize = 200;
        for (int x = -worldSize; x <= worldSize; x++) {
            for (int z = -worldSize; z <= worldSize; z++) {
                for (int y = 0; y < world.getMaxHeight(); y++) {
                    if (world.getBlockAt(x, y, z).getType() == Material.BEDROCK) {
                        world.getBlockAt(x, y, z).setType(Material.COAL_ORE);
                    }
                }
            }
        }
    }
}
