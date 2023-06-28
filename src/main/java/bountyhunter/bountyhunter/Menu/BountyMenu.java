package bountyhunter.bountyhunter.Menu;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import bountyhunter.bountyhunter.Bounties.BountyManager;
import bountyhunter.bountyhunter.Config.ConfigManager;
import bountyhunter.bountyhunter.Utils.PageUtils;
import bountyhunter.bountyhunter.Utils.Utils;
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
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BountyMenu implements Menu {

    @Override
    public Inventory createMenu(Player player) {
        Inventory bountyMenu = Bukkit.createInventory(null, 9, Utils.color("&6&lSET BOUNTY"));

        ItemStack createButton = new ItemStack(Material.CREEPER_BANNER_PATTERN);
        ItemMeta createButtonMeta = createButton.getItemMeta();
        createButtonMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        createButtonMeta.setDisplayName(Utils.color("&7Create a bounty"));
        createButton.setItemMeta(createButtonMeta);

        bountyMenu.setItem(4, createButton);

        return bountyMenu;
    }

    public Inventory nameMenu() {
        Inventory bountyMenu = Bukkit.createInventory(null, 9, Utils.color("&6&lINPUT A NAME"));

        ItemStack nameInput = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta nameInputMeta = nameInput.getItemMeta();
        nameInputMeta.setDisplayName(Utils.color("&7Input a name"));
        nameInput.setItemMeta(nameInputMeta);

        bountyMenu.setItem(4, nameInput);

        return bountyMenu;
    }

    public Inventory rewardMenu() {
        Inventory bountyMenu = Bukkit.createInventory(null, 9, Utils.color("&6&lINPUT AN AMOUNT"));

        ItemStack rewardInput = new ItemStack(Material.GOLD_INGOT);
        ItemMeta rewardInputMeta = rewardInput.getItemMeta();
        rewardInputMeta.setDisplayName(Utils.color("&7Input an amount"));
        rewardInput.setItemMeta(rewardInputMeta);

        bountyMenu.setItem(4, rewardInput);

        return bountyMenu;
    }

    public Inventory bountiesMenu(int page) {
        int pageSize = ConfigManager.getListSize();
        Inventory bountyMenu = Bukkit.createInventory(null, pageSize, Utils.color("&6&lBounties - " + page));

        HashMap<UUID, Double> bounties = BountyManager.getBounties();
        List<ItemStack> allItems = new ArrayList<>();

        for (UUID key : bounties.keySet()) {
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta headMeta = (SkullMeta) head.getItemMeta();
            headMeta.setOwningPlayer(Bukkit.getOfflinePlayer(key));

            headMeta.setDisplayName(Utils.color("&6&l" + Bukkit.getOfflinePlayer(key).getName()));
            List<String> headLore = new ArrayList<>();
            headLore.add(Utils.color("&7Bounty: " + Utils.formatMoney(BountyManager.getReward(key))));
            headMeta.setLore(headLore);

            head.setItemMeta(headMeta);

            allItems.add(head);
        }

        ItemStack leftButton;
        SkullMeta leftButtonMeta;

        if(PageUtils.isPageValid(allItems, page - 1, pageSize - 2)) {
            leftButton = new ItemStack(Material.PLAYER_HEAD);
            leftButtonMeta = (SkullMeta) leftButton.getItemMeta();
            GameProfile profile1 = new GameProfile(UUID.randomUUID(), null);
            profile1.getProperties().put("textures", new Property("textures",
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ=="));
            Field field;

            try {
                field = leftButtonMeta.getClass().getDeclaredField("profile");
                field.setAccessible(true);
                field.set(leftButtonMeta, profile1);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            leftButtonMeta.setDisplayName(Utils.color("&7&lLEFT"));
            leftButtonMeta.setLocalizedName(page + "");
            leftButton.setItemMeta(leftButtonMeta);
            bountyMenu.setItem(0, leftButton);
        } else {
            leftButton = new ItemStack(Material.PLAYER_HEAD);
            leftButtonMeta = (SkullMeta) leftButton.getItemMeta();
            GameProfile profile1 = new GameProfile(UUID.randomUUID(), null);
            profile1.getProperties().put("textures", new Property("textures",
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ=="));
            Field field;

            try {
                field = leftButtonMeta.getClass().getDeclaredField("profile");
                field.setAccessible(true);
                field.set(leftButtonMeta, profile1);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            leftButtonMeta.setDisplayName(Utils.color("&7&lLEFT"));
            leftButtonMeta.setLocalizedName(page + "");
            leftButton.setItemMeta(leftButtonMeta);
            bountyMenu.setItem(0, leftButton);
        }

        ItemStack rightButton;
        SkullMeta rightButtonMeta;

        if(PageUtils.isPageValid(allItems, page + 1, pageSize - 2)) {
            rightButton = new ItemStack(Material.PLAYER_HEAD);
            rightButtonMeta = (SkullMeta) rightButton.getItemMeta();
            GameProfile profile2 = new GameProfile(UUID.randomUUID(), null);
            profile2.getProperties().put("textures", new Property("textures",
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19"));
            Field field;

            try {
                field = rightButtonMeta.getClass().getDeclaredField("profile");
                field.setAccessible(true);
                field.set(rightButtonMeta, profile2);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            rightButtonMeta.setDisplayName(Utils.color("&7&lRIGHT"));
            rightButtonMeta.setLocalizedName(page + "");
            rightButton.setItemMeta(rightButtonMeta);
            bountyMenu.setItem(8, rightButton);
        } else {
            rightButton = new ItemStack(Material.PLAYER_HEAD);
            rightButtonMeta = (SkullMeta) rightButton.getItemMeta();
            GameProfile profile2 = new GameProfile(UUID.randomUUID(), null);
            profile2.getProperties().put("textures", new Property("textures",
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19"));
            Field field;

            try {
                field = rightButtonMeta.getClass().getDeclaredField("profile");
                field.setAccessible(true);
                field.set(rightButtonMeta, profile2);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            rightButtonMeta.setDisplayName(Utils.color("&7&lRIGHT"));
            rightButtonMeta.setLocalizedName(page + "");
            rightButton.setItemMeta(rightButtonMeta);
            bountyMenu.setItem(8, rightButton);
        }

        for (ItemStack is : PageUtils.getPageItems(allItems, page, pageSize - 2)) {
            bountyMenu.setItem(bountyMenu.firstEmpty(), is);
        }

        return bountyMenu;
    }
}
