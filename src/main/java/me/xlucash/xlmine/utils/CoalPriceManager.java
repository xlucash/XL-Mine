package me.xlucash.xlmine.utils;

import me.xlucash.xlmine.MineMain;
import me.xlucash.xlmine.config.ConfigManager;
import me.xlucash.xlmine.database.DatabaseManager;
import org.bukkit.Bukkit;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Random;

public class CoalPriceManager {
    private final ConfigManager configManager;
    private final DatabaseManager databaseManager;
    public static double currentPrice;
    private final MineMain plugin;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public CoalPriceManager(ConfigManager configManager, MineMain plugin, DatabaseManager databaseManager) {
        this.configManager = configManager;
        this.databaseManager = databaseManager;
        this.plugin = plugin;
        currentPrice = databaseManager.getCoalPrice();
        schedulePriceUpdate();
    }

    public void updatePrice() {
        double min = configManager.getMinCoalPrice();
        double max = configManager.getMaxCoalPrice();
        double randomPrice = new Random().nextDouble(min, max);

        currentPrice = randomPrice;
        String formattedPrice = df.format(currentPrice);

        databaseManager.setCoalPrice(currentPrice);
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage("           §m      §7[ §8§lKOPALNIA §7]§7§m     ");
        Bukkit.broadcastMessage("       §7Nowa cena wegla: §a" + formattedPrice + "$§7/kg.");
        Bukkit.broadcastMessage(" ");
    }

    public static double getCurrentPrice() {
        return currentPrice;
    }

    public static String getTimeUntilNextUpdate() {
        Calendar calendar = Calendar.getInstance();
        int minutes = 59 - calendar.get(Calendar.MINUTE);
        int seconds = 59 - calendar.get(Calendar.SECOND);
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void schedulePriceUpdate() {
        Bukkit.getScheduler().runTaskTimer(plugin, this::updatePrice, getTimeUntilNextHour(), 72000L);
    }

    private long getTimeUntilNextHour() {
        Calendar calendar = Calendar.getInstance();
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        int ticksUntilNextHour = (59 - minutes) * 1200 + (60 - seconds) * 20;
        return ticksUntilNextHour;
    }
}
