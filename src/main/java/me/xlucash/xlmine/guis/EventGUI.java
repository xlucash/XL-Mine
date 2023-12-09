package me.xlucash.xlmine.guis;

import me.xlucash.xlmine.database.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class EventGUI {

    private final DatabaseManager databaseManager;

    public EventGUI(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }
    public void openFor(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "§fPiec mikolaja");

        ItemStack confirmButton = new ItemStack(Material.FURNACE);
        ItemMeta confirmMeta = confirmButton.getItemMeta();
        if (confirmMeta != null) {
            confirmMeta.setDisplayName("§c§lPiec Mikołaja");
            confirmMeta.setLore(
                    Arrays.asList(
                            " ",
                            "§7Zapełnienie: §a" + (double) databaseManager.getEventCoalAmount()/1000 + "kg",
                            " ",
                            "§7Zbierzcie siły i pomóżcie §cMikołajowi §7uzbierać §a50 000 §7kilogramów węgla.",
                            "§7Jeżeli §aWam §7się uda to §6cały serwer §7zostanie nagrodzony specjalną nagrodą.",
                            "§7Dostępną pod §6/prezent",
                            " ",
                            "§7Piec uzupełniaj poprzez: §6/mikolaj wrzuc <ilość gramów>"
                            ));
            confirmButton.setItemMeta(confirmMeta);
        }

        inv.setItem(13, confirmButton);

        ItemStack blackGlassPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta glassMeta = blackGlassPane.getItemMeta();
        glassMeta.setDisplayName(" ");
        blackGlassPane.setItemMeta(glassMeta);

        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, blackGlassPane);
            }
        }

        player.openInventory(inv);
    }
}
