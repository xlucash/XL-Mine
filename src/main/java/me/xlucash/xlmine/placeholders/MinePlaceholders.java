package me.xlucash.xlmine.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.xlucash.xlmine.MineMain;
import me.xlucash.xlmine.utils.CoalPriceManager;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class MinePlaceholders extends PlaceholderExpansion {
    private final MineMain plugin;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public MinePlaceholders(MineMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "xlmine";
    }

    @Override
    public String getAuthor() {
        return "xlucash";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }

        // %xlmine_price%
        if (identifier.equals("price")) {
            return CoalPriceManager.getCurrentPrice()+"$";
        }

        // %xlmine_backpack_capacity%
        if (identifier.equals("backpack_capacity")) {
            return df.format(plugin.getConfigManager().getMaxBackpackCapacity()/1000)+"kg";
        }

        // %xlmine_backpack_fill%
        if (identifier.equals("backpack_fill")) {
            return df.format((double) plugin.getDatabaseManager().getCoalAmount(player.getUniqueId())/1000)+"kg";
        }

        return null;
    }
}