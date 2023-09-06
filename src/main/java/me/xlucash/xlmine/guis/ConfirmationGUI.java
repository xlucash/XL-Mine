package me.xlucash.xlmine.guis;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ConfirmationGUI {
    public static void openFor(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "Potwierdzenie sprzedazy");

        ItemStack confirmButton = new ItemStack(Material.GREEN_WOOL);
        ItemMeta confirmMeta = confirmButton.getItemMeta();
        if (confirmMeta != null) {
            confirmMeta.setDisplayName("§aPotwierdź");
            confirmButton.setItemMeta(confirmMeta);
        }

        ItemStack cancelButton = new ItemStack(Material.RED_WOOL);
        ItemMeta cancelMeta = cancelButton.getItemMeta();
        if (cancelMeta != null) {
            cancelMeta.setDisplayName("§cAnuluj");
            cancelButton.setItemMeta(cancelMeta);
        }

        inv.setItem(11, confirmButton);
        inv.setItem(15, cancelButton);

        player.openInventory(inv);
    }
}
