package bountyhunter.bountyhunter.Listener;

import bountyhunter.bountyhunter.Achievements.AchievementsManager;
import bountyhunter.bountyhunter.Bounties.BountyManager;
import bountyhunter.bountyhunter.Bountyhunter;
import bountyhunter.bountyhunter.Config.ConfigManager;
import bountyhunter.bountyhunter.Menu.AchievementsMenu;
import bountyhunter.bountyhunter.Menu.BountyMenu;
import bountyhunter.bountyhunter.Menu.LeaderboardMenu;
import bountyhunter.bountyhunter.Utils.Utils;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MenuListener implements Listener {

    HashMap<UUID, Player> nameInput = new HashMap<>();
    HashMap<UUID, Double> amountInput = new HashMap<>();
    List<Player> playerToListenTo = new ArrayList<>();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if(clickedItem != null) {
            if(event.getView().getTitle().equals(Utils.color("&6&lMAIN MENU"))) {
                event.setCancelled(true);
                if(clickedItem.getType().equals(Material.WRITABLE_BOOK)) {
                    if(!BountyManager.hasCooldown(player) || player.hasPermission("bh.admin")) {
                        BountyMenu bountyMenu = new BountyMenu();
                        player.openInventory(bountyMenu.nameMenu());
                    }
                    else {
                        player.sendMessage(String.format(Utils.color("&cYou cant create a bounty while on cooldown - remaining time: %s"), Utils.formatTime(BountyManager.getCooldown(player))));
                    }

                }
                else if(clickedItem.getType().equals(Material.CREEPER_BANNER_PATTERN)) {
                    BountyMenu bountyMenu = new BountyMenu();
                    player.openInventory(bountyMenu.bountiesMenu(1));
                }
                else if(clickedItem.getItemMeta().getDisplayName().equals(Utils.color("&6&lLeaderboards"))) {
                    LeaderboardMenu leaderboardMenu = new LeaderboardMenu();
                    player.openInventory(leaderboardMenu.createMenu(player));
                }
                else if(clickedItem.getType().equals(Material.NAME_TAG)) {
                    AchievementsManager.updateAchievements(player);
                    AchievementsMenu achievementsMenu = new AchievementsMenu();
                    player.openInventory(achievementsMenu.createMenu(player));
                }
            }
            else if(event.getView().getTitle().equals(Utils.color("&6&lSET BOUNTY"))) {
                event.setCancelled(true);

                if(clickedItem.getType().equals(Material.CREEPER_BANNER_PATTERN)) {

                    Player target = nameInput.get(player.getUniqueId());
                    String reward = String.valueOf(amountInput.get(player.getUniqueId()));

                    target.sendMessage(Utils.color(String.format("&c%s put a bounty on you worth %s", player.getName(), Utils.formatMoney(Double.valueOf(reward)))));
                    player.sendMessage(Utils.color(String.format("&aSuccessfully put a bounty on %s", nameInput.get(player.getUniqueId()).getName())));

                    if(!player.hasPermission("bh.admin")) {
                        EconomyResponse response = Bountyhunter.getEconomy().withdrawPlayer(player, amountInput.get(player.getUniqueId()));
                        if(response.transactionSuccess()) {
                            player.sendMessage("Successful transaction");
                        }
                    }

                    nameInput.remove(player.getUniqueId());
                    amountInput.remove(player.getUniqueId());

                    player.closeInventory();
                }
            }
            else if(event.getView().getTitle().equals(Utils.color("&6&lINPUT A NAME"))) {
                event.setCancelled(true);

                if(clickedItem.getType().equals(Material.WRITABLE_BOOK)) {
                    player.closeInventory();

                    playerToListenTo.add(player);
                    player.sendMessage(Utils.color("&2Write a name"));
                }
            }
            else if(event.getView().getTitle().equals(Utils.color("&6&lINPUT AN AMOUNT"))) {
                event.setCancelled(true);

                if(clickedItem.getType().equals(Material.GOLD_INGOT)) {
                    player.closeInventory();

                    playerToListenTo.add(player);
                    player.sendMessage(Utils.color("&2Write an amount"));
                }
            }
            else if(event.getView().getTitle().contains(Utils.color("&6&lBounties"))) {
                int page = 0;

                if(!event.getInventory().getItem(0).getItemMeta().getDisplayName().contains("&6&lLEFT")) {
                    page = Integer.parseInt(event.getInventory().getItem(0).getItemMeta().getLocalizedName());
                } else {
                    page = Integer.parseInt(event.getInventory().getItem(8).getItemMeta().getLocalizedName());
                }

                if(event.getRawSlot() == 0) {

                    if(page != 1) {
                        BountyMenu bountyMenu = new BountyMenu();
                        player.openInventory(bountyMenu.bountiesMenu(page - 1));
                    }
                }
                else if(event.getRawSlot() == 8) {
                    BountyMenu bountyMenu = new BountyMenu();
                    player.openInventory(bountyMenu.bountiesMenu(page + 1));
                }

                event.setCancelled(true);
            }
            else if(event.getView().getTitle().contains(Utils.color("&6&lLEADERBOARDS"))) {
                if(clickedItem.getItemMeta().getDisplayName().equals(Utils.color("&7&lBounties claimed"))) {
                    LeaderboardMenu leaderboardMenu = new LeaderboardMenu();
                    player.openInventory(leaderboardMenu.createBountyClaimedMenu(player));
                }
                else if(clickedItem.getItemMeta().getDisplayName().equals(Utils.color("&7&lTotal reward"))) {
                    LeaderboardMenu leaderboardMenu = new LeaderboardMenu();
                    player.openInventory(leaderboardMenu.createRewardAmountMenu(player));
                }
            }
            else if(event.getView().getTitle().contains(Utils.color("&6&lHUNTER LEADERBOARD"))) {
                event.setCancelled(true);
            }
            else if(event.getView().getTitle().contains(Utils.color("&6&lREWARD LEADERBOARD"))) {
                event.setCancelled(true);
            }
            else if(event.getView().getTitle().contains(Utils.color("&6&lACHIEVEMENTS"))) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        for (int i = 0; i < playerToListenTo.size(); i++) {
            event.getRecipients().clear();

            if(nameInput.containsKey(player.getUniqueId())) {
                amountInput.put(player.getUniqueId(), Double.valueOf(event.getMessage()));
            }
            else {
                nameInput.put(player.getUniqueId(), Bukkit.getPlayer(event.getMessage()));
            }

            BukkitTask task = Bukkit.getScheduler().runTask(Bountyhunter.getInstance(), () -> {

                for (int x = 0; x < nameInput.size(); x++) {
                    if(nameInput.get(player.getUniqueId()) != null) {
                        BountyManager.createBounty(nameInput.get(player.getUniqueId()), player, 0);
                    }
                    else {
                        player.sendMessage(Utils.color("&cInvalid name"));
                        nameInput.remove(player.getUniqueId());

                        BountyMenu bountyMenu = new BountyMenu();
                        player.openInventory(bountyMenu.nameMenu());
                        playerToListenTo.remove(player);
                        return;
                    }

                    playerToListenTo.remove(player);

                    BountyMenu bountyMenu = new BountyMenu();
                    player.openInventory(bountyMenu.rewardMenu());
                }

                for (int x = 0; x < amountInput.size(); x++) {
                    if(amountInput.get(player.getUniqueId()) != null) {
                        double amount = amountInput.get(player.getUniqueId());

                        if(Bountyhunter.getEconomy().getBalance(player) >= amount || player.hasPermission("bh.admin")) {
                            if(amount <= ConfigManager.getMax() && amount >= ConfigManager.getMin()) {
                                BountyManager.addReward(nameInput.get(player.getUniqueId()), amount);
                            }
                            else {
                                player.sendMessage(String.format(Utils.color("&cThe reward amount needs to be within %s-%s"), Utils.formatMoney(ConfigManager.getMin()), Utils.formatMoney(ConfigManager.getMax())));
                                BountyMenu bountyMenu = new BountyMenu();
                                player.openInventory(bountyMenu.rewardMenu());
                                amountInput.remove(player.getUniqueId());
                                return;
                            }
                        }
                        else {
                            player.sendMessage(Utils.color("&cYou dont have enough money"));
                            BountyMenu bountyMenu = new BountyMenu();
                            player.openInventory(bountyMenu.rewardMenu());
                            amountInput.remove(player.getUniqueId());
                            return;
                        }

                    }
                    else {
                        player.sendMessage(Utils.color("&cInvalid amount"));
                        BountyMenu bountyMenu = new BountyMenu();
                        player.openInventory(bountyMenu.rewardMenu());
                        amountInput.remove(player.getUniqueId());
                        return;
                    }

                    playerToListenTo.remove(player);

                    BountyMenu bountyMenu = new BountyMenu();
                    player.openInventory(bountyMenu.createMenu(player));
                }

            });
        }
    }
}
