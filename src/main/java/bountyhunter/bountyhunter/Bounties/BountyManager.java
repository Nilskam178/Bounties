package bountyhunter.bountyhunter.Bounties;

import bountyhunter.bountyhunter.Bountyhunter;
import bountyhunter.bountyhunter.Config.ConfigManager;
import bountyhunter.bountyhunter.Utils.Utils;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BountyManager {

    private static File bountyFile;
    private static YamlConfiguration modifyBountyFile;

    private static Bountyhunter main;
    public static HashMap<UUID, Integer> bountyCooldown = new HashMap<>();

    public static void setupFile(Bountyhunter bountyhunter) {

        main = bountyhunter;

        try {
            initiateFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static YamlConfiguration getModifyBountyFile() { return modifyBountyFile; }
    public static File getBountyFile() { return bountyFile; }

    private static void initiateFiles() throws IOException {
        bountyFile = new File(Bukkit.getServer().getPluginManager().getPlugin(main.getDescription().getName()).getDataFolder(), "bounties.yml");

        if(!bountyFile.exists()) {
            bountyFile.createNewFile();
        }

        modifyBountyFile = YamlConfiguration.loadConfiguration(bountyFile);

        if (bountyFile.length() == 0)
            getModifyBountyFile().createSection("Bounties");

        try {
            getModifyBountyFile().save(getBountyFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(Bountyhunter.getInstance(), () -> {

            for(Map.Entry<UUID, Integer> entry: bountyCooldown.entrySet()) {
                UUID uuid = entry.getKey();

                int cooldown = bountyCooldown.get(uuid);
                if(cooldown <= 0)
                    bountyCooldown.remove(uuid);

                bountyCooldown.put(uuid, cooldown - 1);
            }

        }, 1200, 1200);
    }

    public static boolean hasCooldown(Player player) {
        if(bountyCooldown.containsKey(player.getUniqueId()))
            return true;
        else
            return false;
    }

    public static int getCooldown(Player player) {
        return bountyCooldown.get(player.getUniqueId());
    }

    public static HashMap<UUID, Double> getBounties() {
        List<String> list = new ArrayList<>();
        list.addAll(getModifyBountyFile().getConfigurationSection("Bounties").getKeys(false));

        HashMap<UUID, Double> bounties = new HashMap<>();

        for (int i = 0; i < list.size(); i++) {
            bounties.put(UUID.fromString(list.get(i)), getReward(UUID.fromString(list.get(i))));
        }

        return bounties;
    }

    public static List<OfflinePlayer> getOwners(Player player) {
        List<String> list = getModifyBountyFile().getStringList("Bounties." + player.getUniqueId() + ".owners");

        List<OfflinePlayer> owners = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            owners.add(Bukkit.getOfflinePlayer(UUID.fromString(list.get(i))));
        }

        return owners;
    }

    public static boolean hasBounty(Player target) {
        for (int i = 0; i < getBounties().size(); i++) {
            if(getBounties().containsKey(target.getUniqueId())) {
                return true;
            }
        }

        return false;
    }

    public static void createBounty(Player target, Player owner, double reward) {
        if(!hasBounty(target)) {

            addOwner(owner, target);
            //getModifyBountyFile().set("Bounties." + target.getUniqueId() + ".owners", owner.getUniqueId().toString());
            getModifyBountyFile().set("Bounties." + target.getUniqueId() + ".reward", reward);
        }
        else if(hasBounty(target)) {
            addReward(target, reward);
        }

        bountyCooldown.put(owner.getUniqueId(), ConfigManager.getBountyCooldown());
        saveFile();
    }

        public static void addOwner(Player owner, Player target) {
        List<OfflinePlayer> offlinePlayers = getOwners(target);
        offlinePlayers.add(owner);

        List<UUID> owners = new ArrayList<>();
        for (int i = 0; i < offlinePlayers.size(); i++) {
            owners.add(offlinePlayers.get(i).getUniqueId());
        }

        getModifyBountyFile().set("Bounties." + target.getUniqueId() + ".owners", owners.toString());

        saveFile();
    }

    public static void addReward(Player target, double reward) {
        getModifyBountyFile().set("Bounties." + target.getUniqueId() + ".reward", (reward + getReward(target.getUniqueId())));

        saveFile();
    }

    public static void removeReward(Player target, double reward) {
        getModifyBountyFile().set("Bounties." + target.getUniqueId() + ".reward", (getReward(target.getUniqueId()) - reward));

        saveFile();
    }

    public static double getReward(UUID uuid) {
        return getModifyBountyFile().getDouble("Bounties." + uuid.toString() + ".reward");
    }

    public static void clearBounty(Player target) {
        getModifyBountyFile().set("Bounties." + target.getUniqueId() + ".reward", 0);

        saveFile();
    }

    public static void removeBounty(Player target) {
        getModifyBountyFile().set("Bounties." + target.getUniqueId(), null);

        saveFile();
    }

    private static void saveFile() {
        try {
            getModifyBountyFile().save(getBountyFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
