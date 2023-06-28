package bountyhunter.bountyhunter.Achievements;

import bountyhunter.bountyhunter.Bountyhunter;
import bountyhunter.bountyhunter.Leaderboard.LeaderboardManager;
import bountyhunter.bountyhunter.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AchievementsManager {
    private static File achievementFile;
    private static YamlConfiguration modifyAchievementFile;

    private static Bountyhunter main;

    public static void setupFile(Bountyhunter bountyhunter) {

        main = bountyhunter;

        try {
            initiateFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static YamlConfiguration getModifyAchievementFile() { return modifyAchievementFile; }
    public static File getAchievementFile() { return achievementFile; }

    private static void initiateFiles() throws IOException {
        achievementFile = new File(Bukkit.getServer().getPluginManager().getPlugin(main.getDescription().getName()).getDataFolder(), "achievements.yml");

        if(!achievementFile.exists()) {
            achievementFile.createNewFile();
        }

        modifyAchievementFile = YamlConfiguration.loadConfiguration(achievementFile);

        if (achievementFile.length() == 0)
            getModifyAchievementFile().createSection("Achievements");

        try {
            getModifyAchievementFile().save(getAchievementFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addAchievement(Player player, Achievements achievement) {
        List<String> achievements = getUnlockedAchievements(player);
        if(!hasAchievement(player, achievement.getName())) {
            achievements.add(achievement.getName());
            Bukkit.broadcastMessage(String.format(Utils.color("&f%s has earned the achievement &a[%s]"), player.getName(), achievement.getName()));
        }

        getModifyAchievementFile().set("Achievements." + player.getUniqueId(), achievements);
        saveFile();
    }

    public static List<String> getUnlockedAchievements(Player player) {
        List<String> achievements = new ArrayList<>();
        achievements.addAll(getModifyAchievementFile().getStringList("Achievements." + player.getUniqueId()));

        return achievements;
    }

    public static boolean hasAchievement(Player player, String name) {
        List<String> achievements = getUnlockedAchievements(player);
        return achievements.contains(name);

    }

    public static void updateAchievements(Player player) {
        double totalAmount = LeaderboardManager.getRewardAmount(player.getUniqueId());
        int totalBounties = LeaderboardManager.getBountiesClaimed(player.getUniqueId());

        int index = 0;
        for (Achievements achievement : Achievements.values()) {
            if(index > 6) {
                if(totalAmount >= achievement.getRequirement()) {
                    addAchievement(player, achievement);
                }
            }
            else {
                if(totalBounties >= achievement.getRequirement()) {
                    addAchievement(player, achievement);
                }
            }

            index++;
        }
    }

    private static void saveFile() {
        try {
            getModifyAchievementFile().save(getAchievementFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
