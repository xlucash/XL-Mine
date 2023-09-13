package me.xlucash.xlmine.listeners;

import me.xlucash.xlmine.config.ConfigManager;
import me.xlucash.xlmine.database.DatabaseManager;
import me.xlucash.xlmine.guis.BackpackGUI;
import me.xlucash.xlmine.guis.ConfirmationGUI;
import me.xlucash.xlmine.hooks.VaultHook;
import me.xlucash.xlmine.utils.CoalPriceManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryClickListener implements Listener {
    private final DatabaseManager databaseManager;
    private ConfigManager configManager;

    public InventoryClickListener(DatabaseManager databaseManager, ConfigManager configManager) {
        this.databaseManager = databaseManager;
        this.configManager = configManager;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;

        String title = event.getView().getTitle();

        if (!(title.equals("§fPlecak gornika") || title.equals("§fPotwierdzenie sprzedazy"))) return;

        event.setCancelled(true);

        if (event.getClickedInventory() != event.getView().getTopInventory()) {
            return;
        }

        if (title.equals("§fPlecak gornika")) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.GOLD_INGOT) {
                Player player = (Player) event.getWhoClicked();
                int coalAmount = databaseManager.getCoalAmount(player.getUniqueId());
                if (coalAmount == 0) {
                    player.sendMessage("§cNie masz żadnego węgla do sprzedania!");
                    return;
                }
                ConfirmationGUI.openFor((Player) event.getWhoClicked());
            }
        } else if (title.equals("§fPotwierdzenie sprzedazy")) {
            if (event.getCurrentItem() != null) {
                if (event.getCurrentItem().getType() == Material.GREEN_WOOL) {
                    Player player = (Player) event.getWhoClicked();
                    int coalAmount = databaseManager.getCoalAmount(player.getUniqueId());
                    double pricePerKg = CoalPriceManager.getCurrentPrice();
                    double totalPrice = coalAmount / 1000.0 * pricePerKg;

                    if (VaultHook.econ != null) {
                        VaultHook.econ.depositPlayer(player, totalPrice);
                        databaseManager.setCoalAmount(player.getUniqueId(), 0);
                        player.sendMessage("§7Sprzedałeś zawartość plecaka za §a" + totalPrice + "$!");
                    }

                    player.closeInventory();
                } else if (event.getCurrentItem().getType() == Material.RED_WOOL) {
                    event.getWhoClicked().closeInventory();
                }
            }
        }
    }

    @EventHandler
    public void onInventoryExit(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals("§fPlecak gornika") || event.getView().getTitle().equals("§fPotwierdzenie sprzedazy")) {
            BackpackGUI.close();
        }
    }
}
