package bountyhunter.bountyhunter.Menu;

import bountyhunter.bountyhunter.Achievements.Achievements;
import bountyhunter.bountyhunter.Achievements.AchievementsManager;
import bountyhunter.bountyhunter.Leaderboard.LeaderboardManager;
import bountyhunter.bountyhunter.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AchievementsMenu implements Menu {

    int[] slots = new int[] { 1, 2, 3, 4, 5, 6, 7, 11, 12, 13, 14, 15 };

    @Override
    public Inventory createMenu(Player player) {
        Inventory achievementsMenu = Bukkit.createInventory(null, 18, Utils.color("&6&lACHIEVEMENTS"));

        List<String> completedAchievements = AchievementsManager.getUnlockedAchievements(player);

        int index = 0;
        for (Achievements achievement : Achievements.values()) {
            ItemStack achievementItem = new ItemStack(Material.CREEPER_BANNER_PATTERN);
            ItemMeta achievementMeta = achievementItem.getItemMeta();
            achievementMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

            if(completedAchievements.contains(achievement.getName())) {
                achievementMeta.setDisplayName(String.format(Utils.color("&a%s &6&l%s"), "✔", achievement.getName()));
            }
            else {
                achievementMeta.setDisplayName(String.format(Utils.color("&c%s &6&l%s"), "✖", achievement.getName()));
            }

            List<String> achievementLore = new ArrayList<>();
            if(index > 6) {
                achievementLore.add(String.format(Utils.color("&7(%s/%s) %s"),
                        Utils.formatMoney(LeaderboardManager.getRewardAmount(player.getUniqueId())),
                        Utils.formatMoney(achievement.getRequirement()),
                        "Money received"));
            }
            else {
                achievementLore.add(String.format(Utils.color("&7(%s/%s) %s"),
                        Utils.formatNumber(LeaderboardManager.getBountiesClaimed(player.getUniqueId())),
                        Utils.formatNumber(achievement.getRequirement()),
                        "Bounties claimed"));
            }

            achievementMeta.setLore(achievementLore);
            achievementItem.setItemMeta(achievementMeta);

            achievementsMenu.setItem(slots[index], achievementItem);

            index++;
        }

        return achievementsMenu;
    }
}
