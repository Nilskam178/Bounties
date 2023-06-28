package bountyhunter.bountyhunter.Command;

import bountyhunter.bountyhunter.Achievements.Achievements;
import bountyhunter.bountyhunter.Achievements.AchievementsManager;
import bountyhunter.bountyhunter.Bounties.BountyManager;
import bountyhunter.bountyhunter.Bountyhunter;
import bountyhunter.bountyhunter.Config.ConfigManager;
import bountyhunter.bountyhunter.Menu.AchievementsMenu;
import bountyhunter.bountyhunter.Menu.BountyMenu;
import bountyhunter.bountyhunter.Menu.LeaderboardMenu;
import bountyhunter.bountyhunter.Menu.MainMenu;
import bountyhunter.bountyhunter.Utils.Utils;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BountyHunterCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;

            if(args.length > 0) {
                if(args[0].equals("create")) {
                    if(args.length == 1) {
                        BountyMenu bountyMenu = new BountyMenu();
                        player.openInventory(bountyMenu.nameMenu());
                    }
                    else {
                        Player target = Bukkit.getPlayer(args[1]);
                        double amount = Double.parseDouble(args[2]);

                        if(!BountyManager.hasCooldown(player) || player.hasPermission("bo.admin")) {
                            if(Bountyhunter.getEconomy().getBalance(player) >= amount) {
                                if(amount <= ConfigManager.getMax() && amount >= ConfigManager.getMin()) {
                                    BountyManager.createBounty(target, player, amount);

                                    target.sendMessage(Utils.color(String.format("&c%s put a bounty on you worth %s", player.getName(), Utils.formatMoney(amount))));
                                    player.sendMessage(Utils.color(String.format("&aSuccessfully put a bounty on %s", target.getName())));

                                    if(!player.hasPermission("bo.admin")) {
                                        EconomyResponse response = Bountyhunter.getEconomy().withdrawPlayer(player, amount);
                                        if(response.transactionSuccess()) {
                                            player.sendMessage("Successful transaction");
                                        }
                                    }
                                }
                                else {
                                    player.sendMessage(String.format(Utils.color("&cThe reward amount needs to be within %s-%s"), Utils.formatMoney(ConfigManager.getMin()), Utils.formatMoney(ConfigManager.getMax())));
                                    return false;
                                }
                            }
                        }
                        else {
                            player.sendMessage(String.format(Utils.color("&cYou cant create a bounty while on cooldown - remaining time: %s"), Utils.formatTime(BountyManager.getCooldown(player))));
                            return false;
                        }
                    }
                }
                else if(args[0].equals("amount")) {

                    if(args.length < 2) {
                        player.sendMessage(Utils.color(String.format("&7Your total bounty is: %s",Utils.formatMoney(BountyManager.getReward(player.getUniqueId())))));
                    }
                    else {
                        Player target = Bukkit.getPlayer(args[1]);
                        if(target != null) {
                            player.sendMessage(Utils.color(String.format("&7%s's total bounty is: %s", target.getName(), Utils.formatMoney(BountyManager.getReward(target.getUniqueId())))));
                        }
                    }

                }
                else if(args[0].equals("list")) {
                    BountyMenu bountyMenu = new BountyMenu();
                    player.openInventory(bountyMenu.bountiesMenu(1));
                }
                else if(args[0].equals("clear")) {
                    if(player.hasPermission("bo.admin")) {
                        player.sendMessage(Utils.color("&cCleared the bounty"));
                        BountyManager.removeBounty(Bukkit.getPlayer(args[1]));
                    }
                    else {
                        player.sendMessage(Utils.color("&cYou dont have permission to use this command"));
                    }
                }
                else if(args[0].equals("leaderboards")) {
                    LeaderboardMenu leaderboardMenu = new LeaderboardMenu();
                    player.openInventory(leaderboardMenu.createMenu(player));
                }
                else if(args[0].equals("reload")) {
                    AchievementsManager.updateAchievements(player);
                }
                else if(args[0].equals("achievements")) {
                    AchievementsManager.updateAchievements(player);
                    AchievementsMenu achievementsMenu = new AchievementsMenu();
                    player.openInventory(achievementsMenu.createMenu(player));
                }
                else if(args[0].equals("help")) {
                    List<String> message = new ArrayList<>();
                    message.add(String.format(Utils.color("&6--------------------------------------")));
                    message.add(String.format(Utils.color("&f→ Aliases: bounties, bounty, bo")));
                    message.add(String.format(Utils.color("&f→ Bounty menu - /bounties")));
                    message.add(String.format(Utils.color("&f→ Create a bounty - /bounties create <name> <amount>")));
                    message.add(String.format(Utils.color("&f→ Upon killing a player the killer will receive")));
                    message.add(String.format(Utils.color("&f  the bounty but a mandatory tax will be applied")));
                    message.add(String.format(Utils.color("&6--------------------------------------")));

                    for (String msg : message) {
                        player.sendMessage(msg);
                    }
                }
            }
            else {
                MainMenu mainMenu = new MainMenu();
                player.openInventory(mainMenu.createMenu(player));
            }
        }

        return false;
    }
}
