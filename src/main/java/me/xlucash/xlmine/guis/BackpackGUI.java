package me.xlucash.xlmine.guis;

import me.xlucash.xlmine.MineMain;
import me.xlucash.xlmine.config.ConfigManager;
import me.xlucash.xlmine.database.DatabaseManager;
import me.xlucash.xlmine.hooks.VaultHook;
import me.xlucash.xlmine.utils.CoalPriceManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;

public class BackpackGUI {
    private final DatabaseManager databaseManager;
    private final ConfigManager configManager;
    private BukkitTask refreshTask;
    private Inventory inv;
    private Player currentPlayer;

    public BackpackGUI(DatabaseManager databaseManager, ConfigManager configManager) {
        this.databaseManager = databaseManager;
        this.configManager = configManager;
        this.inv = Bukkit.createInventory(null, 27, "Plecak z weglem");
    }

    public void open(Player player) {
        currentPlayer = player;

        refreshGUI();
        player.openInventory(inv);

        refreshTask = Bukkit.getScheduler().runTaskTimer(MineMain.getPlugin(MineMain.class), this::refreshGUI, 0L, 20L); // co sekundę
    }

    public void close() {
        if (refreshTask != null) {
            refreshTask.cancel();
        }
    }

    private void refreshGUI() {
        if (currentPlayer == null) {
            return;
        }

        int coalAmount = databaseManager.getCoalAmount(currentPlayer.getUniqueId());

        ItemStack backpackItem = new ItemStack(Material.CHEST);
        ItemMeta meta = backpackItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§7Twój plecak");
            meta.setLore(Arrays.asList("§fZapełnienie: §a" + (double) coalAmount/1000 + "kg §f/ §a" + (double) configManager.getMaxBackpackCapacity()/1000 + "kg"));
            backpackItem.setItemMeta(meta);
        }

        ItemStack sellButton = new ItemStack(Material.GOLD_INGOT);
        ItemMeta sellMeta = sellButton.getItemMeta();
        if (sellMeta != null) {
            sellMeta.setDisplayName("§fSprzedaj węgiel");
            double pricePerKg = CoalPriceManager.getCurrentPrice();
            double totalPrice = coalAmount / 1000.0 * pricePerKg;
            sellMeta.setLore(Arrays.asList("§fCena za kg: " + CoalPriceManager.getCurrentPrice() + "$", "§fCałkowita cena: " + totalPrice + "$", "§fNastępna aktualizacja za: " + CoalPriceManager.getTimeUntilNextUpdate()));
            sellButton.setItemMeta(sellMeta);
        }

        inv.setItem(13, backpackItem);
        inv.setItem(15, sellButton);
    }
}
