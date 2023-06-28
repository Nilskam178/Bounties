package bountyhunter.bountyhunter.Utils;

import bountyhunter.bountyhunter.Config.ConfigManager;
import org.bukkit.ChatColor;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Utils {
    public static String color(String msg) { return ChatColor.translateAlternateColorCodes('&', msg); }
    public static String formatMoney(double number) { DecimalFormat df = new DecimalFormat("#,###.##", new DecimalFormatSymbols(Locale.ENGLISH)); return ConfigManager.getPrefix() + df.format(number);}
    public static String formatNumber(double number) { DecimalFormat df = new DecimalFormat("#,###.##", new DecimalFormatSymbols(Locale.ENGLISH)); return df.format(number); }
    public static String formatTime(int minutes) {
        int remainingHours = minutes / 60;
        int remainingMinutes = minutes % 60;

        String result = "";
        if (remainingHours > 0) {
            result += remainingHours + "h ";
        }
        if (remainingMinutes > 0 || result.isEmpty()) {
            result += remainingMinutes + "min";
        }

        return result;
    }

}
