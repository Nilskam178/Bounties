package bountyhunter.bountyhunter.Leaderboard;

import bountyhunter.bountyhunter.Bountyhunter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class LeaderboardManager {
    private static File leaderboardFile;
    private static YamlConfiguration modifyLeaderboardFile;

    private static Bountyhunter main;

    public static void setupFile(Bountyhunter bountyhunter) {

        main = bountyhunter;

        try {
            initiateFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static YamlConfiguration getModifyLeaderboardFile() { return modifyLeaderboardFile; }
    public static File getLeaderboardFile() { return leaderboardFile; }

    private static void initiateFiles() throws IOException {
        leaderboardFile = new File(Bukkit.getServer().getPluginManager().getPlugin(main.getDescription().getName()).getDataFolder(), "leaderboard.yml");

        if(!leaderboardFile.exists()) {
            leaderboardFile.createNewFile();
        }

        modifyLeaderboardFile = YamlConfiguration.loadConfiguration(leaderboardFile);

        if (leaderboardFile.length() == 0)
            getModifyLeaderboardFile().createSection("Leaderboard");

        try {
            getModifyLeaderboardFile().save(getLeaderboardFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getBountiesClaimed(UUID uuid) {
        return getModifyLeaderboardFile().getInt("Leaderboard." + uuid + ".bountiesClaimed");
    }

    public static void addBountiesClaimed(UUID uuid, int amount) {
        getModifyLeaderboardFile().set("Leaderboard." + uuid + ".bountiesClaimed", getBountiesClaimed(uuid) + amount);

        saveFile();
    }

    public static HashMap<UUID, Integer> getBountiesClaimed() {
        List<String> list = new ArrayList<>();
        list.addAll(getModifyLeaderboardFile().getConfigurationSection("Leaderboard").getKeys(false));

        HashMap<UUID, Integer> bountiesClaimed = new HashMap<>();

        for (int i = 0; i < list.size(); i++) {
            bountiesClaimed.put(UUID.fromString(list.get(i)), getBountiesClaimed(UUID.fromString(list.get(i))));
        }

        return bountiesClaimed;
    }

    public static double getRewardAmount(UUID uuid) {
        return getModifyLeaderboardFile().getInt("Leaderboard." + uuid + ".rewardAmount");
    }

    public static void addRewardAmount(UUID uuid, double amount) {
        getModifyLeaderboardFile().set("Leaderboard." + uuid + ".rewardAmount", getRewardAmount(uuid) + amount);

        saveFile();
    }

    public static HashMap<UUID, Double> getRewardAmount() {
        List<String> list = new ArrayList<>();
        list.addAll(getModifyLeaderboardFile().getConfigurationSection("Leaderboard").getKeys(false));

        HashMap<UUID, Double> rewardsClaimed = new HashMap<>();

        for (int i = 0; i < list.size(); i++) {
            rewardsClaimed.put(UUID.fromString(list.get(i)), getRewardAmount(UUID.fromString(list.get(i))));
        }

        return rewardsClaimed;
    }

    private static void saveFile() {
        System.out.println("Saved file");

        try {
            getModifyLeaderboardFile().save(getLeaderboardFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
