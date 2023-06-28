package bountyhunter.bountyhunter.Menu;

import bountyhunter.bountyhunter.Bounties.BountyManager;
import bountyhunter.bountyhunter.Leaderboard.LeaderboardManager;
import bountyhunter.bountyhunter.Utils.Utils;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

public class LeaderboardMenu implements Menu{

    int[] slots = new int[] { 2, 3, 4, 5, 6, 11, 12, 13, 14, 15 };

    @Override
    public Inventory createMenu(Player player) {
        Inventory leaderboardMenu = Bukkit.createInventory(null, 9, Utils.color("&6&lLEADERBOARDS"));

        ItemStack bountyClaimButton;
        SkullMeta bountyClaimMeta;

        bountyClaimButton = new ItemStack(Material.PLAYER_HEAD);
        bountyClaimMeta = (SkullMeta) bountyClaimButton.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTM0YTU5MmE3OTM5N2E4ZGYzOTk3YzQzMDkxNjk0ZmMyZmI3NmM4ODNhNzZjY2U4OWYwMjI3ZTVjOWYxZGZlIn19fQ=="));
        Field field;

        try {
            field = bountyClaimMeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(bountyClaimMeta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        bountyClaimMeta.setDisplayName(Utils.color("&7&lBounties claimed"));
        bountyClaimButton.setItemMeta(bountyClaimMeta);

        leaderboardMenu.setItem(3, bountyClaimButton);

        ItemStack rewardTotalButton;
        SkullMeta rewardTotalMeta;

        rewardTotalButton = new ItemStack(Material.PLAYER_HEAD);
        rewardTotalMeta = (SkullMeta) rewardTotalButton.getItemMeta();
        GameProfile profile1 = new GameProfile(UUID.randomUUID(), null);
        profile1.getProperties().put("textures", new Property("textures",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTM0YTU5MmE3OTM5N2E4ZGYzOTk3YzQzMDkxNjk0ZmMyZmI3NmM4ODNhNzZjY2U4OWYwMjI3ZTVjOWYxZGZlIn19fQ=="));
        Field field1;

        try {
            field1 = rewardTotalMeta.getClass().getDeclaredField("profile");
            field1.setAccessible(true);
            field1.set(rewardTotalMeta, profile1);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        rewardTotalMeta.setDisplayName(Utils.color("&7&lTotal reward"));
        rewardTotalButton.setItemMeta(rewardTotalMeta);

        leaderboardMenu.setItem(5, rewardTotalButton);
        
        return leaderboardMenu;
    }
    
    public Inventory createBountyClaimedMenu(Player player) {
        Inventory leaderboardMenu = Bukkit.createInventory(null, 18, Utils.color("&6&lHUNTER LEADERBOARD"));
        List<ItemStack> allItems = new ArrayList<>();

        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta playerHeadMeta = (SkullMeta) playerHead.getItemMeta();
        playerHeadMeta.setOwningPlayer(player);

        playerHeadMeta.setDisplayName(String.format(Utils.color("&6&l%s"), player.getName()));
        List<String> playerHeadLore = new ArrayList<>();
        double playerValue = LeaderboardManager.getBountiesClaimed(player.getUniqueId());
        playerHeadLore.add(Utils.color("&7Bounties claimed: " + Utils.formatNumber(playerValue)));
        playerHeadMeta.setLore(playerHeadLore);

        playerHead.setItemMeta(playerHeadMeta);

        leaderboardMenu.setItem(0, playerHead);

        for (int i = 0; i < 10; i++) {
            ItemStack unknownPlayer;
            SkullMeta leftButtonMeta;

            unknownPlayer = new ItemStack(Material.PLAYER_HEAD);
            leftButtonMeta = (SkullMeta) unknownPlayer.getItemMeta();
            GameProfile profile1 = new GameProfile(UUID.randomUUID(), null);
            profile1.getProperties().put("textures", new Property("textures",
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmFkYzA0OGE3Y2U3OGY3ZGFkNzJhMDdkYTI3ZDg1YzA5MTY4ODFlNTUyMmVlZWQxZTNkYWYyMTdhMzhjMWEifX19"));
            Field field;

            try {
                field = leftButtonMeta.getClass().getDeclaredField("profile");
                field.setAccessible(true);
                field.set(leftButtonMeta, profile1);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            leftButtonMeta.setDisplayName(Utils.color("&7&lNONE"));
            unknownPlayer.setItemMeta(leftButtonMeta);

            allItems.add(unknownPlayer);
        }

        HashMap<UUID, Integer> bountiesClaimed = LeaderboardManager.getBountiesClaimed();
        List<Integer> allValues = new ArrayList<>();

        for (Map.Entry<UUID, Integer> entry : bountiesClaimed.entrySet()) {
            int amount = entry.getValue();

            allValues.add(amount);
        }

        Collections.sort(allValues);
        Collections.reverse(allValues);

        for (int i = 0; i < slots.length; i++) {
            leaderboardMenu.setItem(slots[i], allItems.get(i));
        }

        List<UUID> usedUUIDs = new ArrayList<>();
        int index = 0;

        for (int i = 0; i < allValues.size(); i++) {
            for(Map.Entry<UUID, Integer> entry: bountiesClaimed.entrySet()) {
                if(entry.getValue().equals(allValues.get(i))) {
                    UUID uuid = entry.getKey();

                    if(!usedUUIDs.contains(uuid)) {

                        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
                        headMeta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));

                        headMeta.setDisplayName(String.format(Utils.color("&6&l#%s - %s"), (index + 1) ,Bukkit.getOfflinePlayer(uuid).getName()));
                        List<String> headLore = new ArrayList<>();
                        double value = LeaderboardManager.getBountiesClaimed(uuid);
                        headLore.add(Utils.color("&7Bounties claimed: " + Utils.formatNumber(value)));
                        headMeta.setLore(headLore);

                        head.setItemMeta(headMeta);

                        leaderboardMenu.setItem(slots[index], head);

                        usedUUIDs.add(uuid);
                        index++;
                    }
                }
            }
        }

        return leaderboardMenu;
    }

    public Inventory createRewardAmountMenu(Player player) {
        Inventory leaderboardMenu = Bukkit.createInventory(null, 18, Utils.color("&6&lREWARD LEADERBOARD"));
        List<ItemStack> allItems = new ArrayList<>();

        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta playerHeadMeta = (SkullMeta) playerHead.getItemMeta();
        playerHeadMeta.setOwningPlayer(player);

        playerHeadMeta.setDisplayName(String.format(Utils.color("&6&l%s"), player.getName()));
        List<String> playerHeadLore = new ArrayList<>();
        double playerValue = LeaderboardManager.getRewardAmount(player.getUniqueId());
        playerHeadLore.add(Utils.color("&7Total reward: " + Utils.formatMoney(playerValue)));
        playerHeadMeta.setLore(playerHeadLore);

        playerHead.setItemMeta(playerHeadMeta);

        leaderboardMenu.setItem(0, playerHead);

        for (int i = 0; i < 10; i++) {
            ItemStack unknownPlayer;
            SkullMeta leftButtonMeta;

            unknownPlayer = new ItemStack(Material.PLAYER_HEAD);
            leftButtonMeta = (SkullMeta) unknownPlayer.getItemMeta();
            GameProfile profile1 = new GameProfile(UUID.randomUUID(), null);
            profile1.getProperties().put("textures", new Property("textures",
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmFkYzA0OGE3Y2U3OGY3ZGFkNzJhMDdkYTI3ZDg1YzA5MTY4ODFlNTUyMmVlZWQxZTNkYWYyMTdhMzhjMWEifX19"));
            Field field;

            try {
                field = leftButtonMeta.getClass().getDeclaredField("profile");
                field.setAccessible(true);
                field.set(leftButtonMeta, profile1);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            leftButtonMeta.setDisplayName(Utils.color("&7&lNONE"));
            unknownPlayer.setItemMeta(leftButtonMeta);

            allItems.add(unknownPlayer);
        }

        HashMap<UUID, Double> bountiesClaimed = LeaderboardManager.getRewardAmount();
        List<Double> allValues = new ArrayList<>();

        for (Map.Entry<UUID, Double> entry : bountiesClaimed.entrySet()) {
            double amount = entry.getValue();

            allValues.add(amount);
        }

        Collections.sort(allValues);
        Collections.reverse(allValues);

        for (int i = 0; i < slots.length; i++) {
            leaderboardMenu.setItem(slots[i], allItems.get(i));
        }

        List<UUID> usedUUIDs = new ArrayList<>();
        int index = 0;

        for (int i = 0; i < allValues.size(); i++) {
            for(Map.Entry<UUID, Double> entry: bountiesClaimed.entrySet()) {
                if(entry.getValue().equals(allValues.get(i))) {
                    UUID uuid = entry.getKey();

                    if(!usedUUIDs.contains(uuid)) {

                        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
                        headMeta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));

                        headMeta.setDisplayName(String.format(Utils.color("&6&l#%s - %s"), (index + 1) ,Bukkit.getOfflinePlayer(uuid).getName()));
                        List<String> headLore = new ArrayList<>();
                        double value = LeaderboardManager.getRewardAmount(uuid);
                        headLore.add(Utils.color("&7Total reward: " + Utils.formatMoney(value)));
                        headMeta.setLore(headLore);

                        head.setItemMeta(headMeta);

                        leaderboardMenu.setItem(slots[index], head);

                        usedUUIDs.add(uuid);
                        index++;
                    }
                }
            }
        }

        return leaderboardMenu;
    }
}

