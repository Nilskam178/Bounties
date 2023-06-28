package bountyhunter.bountyhunter.Config;

import bountyhunter.bountyhunter.Bountyhunter;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private static FileConfiguration config;

    public static void setupConfig(Bountyhunter bountyhunter) {
        ConfigManager.config = bountyhunter.getConfig();
        bountyhunter.saveDefaultConfig();
    }

    public static String getPrefix() {
        return config.getString("currency-prefix");
    }
    public static int getListSize() { return config.getInt("bounty-list-inventory-size"); }
    public static int getBountyCooldown() { return config.getInt("bounty-cooldown"); }
    public static double getMax() { return config.getDouble("max-bounty"); }
    public static double getMin() { return config.getDouble("min-bounty"); }
    public static double getTax() { return config.getDouble("bounty-tax"); }
}
