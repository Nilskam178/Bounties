package bountyhunter.bountyhunter.Listener;

import bountyhunter.bountyhunter.Achievements.AchievementsManager;
import bountyhunter.bountyhunter.Bounties.BountyManager;
import bountyhunter.bountyhunter.Bountyhunter;
import bountyhunter.bountyhunter.Config.ConfigManager;
import bountyhunter.bountyhunter.Leaderboard.LeaderboardManager;
import bountyhunter.bountyhunter.Utils.Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Economy economy = Bountyhunter.getEconomy();
        Player player = event.getEntity();

        if(player.getKiller() != null) {
            Player killer = player.getKiller();

            if(BountyManager.hasBounty(player)) {
                economy.depositPlayer(killer, BountyManager.getReward(player.getUniqueId()) * ((100 - ConfigManager.getTax()) / 100));
                //event.setDeathMessage(String.format(Utils.color("&7%s killed %s who had a bounty worth %s"), killer.getName(), player.getName(), Utils.formatMoney(BountyManager.getReward(player.getUniqueId()))));
                killer.sendMessage(String.format(Utils.color("&7You received %s for killing this player"), Utils.formatMoney(BountyManager.getReward(player.getUniqueId()) * ((100 - ConfigManager.getTax()) / 100))));

                LeaderboardManager.addBountiesClaimed(killer.getUniqueId(), 1);
                LeaderboardManager.addRewardAmount(killer.getUniqueId(), BountyManager.getReward(player.getUniqueId()) * ((100 - ConfigManager.getTax()) / 100));
                AchievementsManager.updateAchievements(killer);

                BountyManager.removeBounty(player);
            }
        }
    }
}
