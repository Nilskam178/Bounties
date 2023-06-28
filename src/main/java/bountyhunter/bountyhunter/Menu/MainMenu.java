package bountyhunter.bountyhunter.Menu;

import bountyhunter.bountyhunter.Bounties.BountyManager;
import bountyhunter.bountyhunter.Bountyhunter;
import bountyhunter.bountyhunter.Utils.Utils;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainMenu implements Menu{

    public Inventory createMenu(Player player) {
        Inventory mainMenu = Bukkit.createInventory(null, 9, Utils.color("&6&lMAIN MENU"));

        ItemStack myBounty = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta myBountyMeta = (SkullMeta) myBounty.getItemMeta();
        myBountyMeta.setOwningPlayer(player);
        myBountyMeta.setDisplayName(Utils.color(String.format("&6&l%s", player.getName())));

        List<String> bountyLore = new ArrayList<>();
        bountyLore.add(Utils.color(String.format("&7Your bounty: %s", Utils.formatMoney(BountyManager.getReward(player.getUniqueId())))));
        bountyLore.add(Utils.color(String.format("&7Your balance: %s", Utils.formatMoney(Bountyhunter.getEconomy().getBalance(player)))));

        myBountyMeta.setLore(bountyLore);
        myBounty.setItemMeta(myBountyMeta);

        mainMenu.setItem(0, myBounty);

        ItemStack bountyButton = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta bountyButtonMeta = bountyButton.getItemMeta();
        bountyButtonMeta.setDisplayName(Utils.color("&6&lCreate a bounty"));
        bountyButton.setItemMeta(bountyButtonMeta);

        mainMenu.setItem(8, bountyButton);

        ItemStack leaderboardButton;
        SkullMeta leaderboardMeta;

        leaderboardButton = new ItemStack(Material.PLAYER_HEAD);
        leaderboardMeta = (SkullMeta) leaderboardButton.getItemMeta();
        GameProfile profile1 = new GameProfile(UUID.randomUUID(), null);
        profile1.getProperties().put("textures", new Property("textures",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTM0YTU5MmE3OTM5N2E4ZGYzOTk3YzQzMDkxNjk0ZmMyZmI3NmM4ODNhNzZjY2U4OWYwMjI3ZTVjOWYxZGZlIn19fQ=="));
        Field field;

        try {
            field = leaderboardMeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(leaderboardMeta, profile1);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        leaderboardMeta.setDisplayName(Utils.color("&6&lLeaderboards"));
        leaderboardButton.setItemMeta(leaderboardMeta);

        mainMenu.setItem(3, leaderboardButton);

        ItemStack bountyList = new ItemStack(Material.CREEPER_BANNER_PATTERN);
        ItemMeta bountyListMeta = bountyList.getItemMeta();
        bountyListMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        bountyListMeta.setDisplayName(Utils.color("&6&lBounties"));
        bountyList.setItemMeta(bountyListMeta);

        mainMenu.setItem(4, bountyList);

        ItemStack achievementsButton = new ItemStack(Material.NAME_TAG);
        ItemMeta achievementsMeta = achievementsButton.getItemMeta();
        achievementsMeta.setDisplayName(Utils.color("&6&lAchievements"));
        achievementsButton.setItemMeta(achievementsMeta);

        mainMenu.setItem(5, achievementsButton);

        return mainMenu;
    }
}
