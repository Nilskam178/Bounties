package bountyhunter.bountyhunter;

import bountyhunter.bountyhunter.Achievements.AchievementsManager;
import bountyhunter.bountyhunter.Bounties.BountyManager;
import bountyhunter.bountyhunter.Command.BountyHunterCommand;
import bountyhunter.bountyhunter.Command.BountyHunterCommandTab;
import bountyhunter.bountyhunter.Config.ConfigManager;
import bountyhunter.bountyhunter.Leaderboard.LeaderboardManager;
import bountyhunter.bountyhunter.Listener.MenuListener;
import bountyhunter.bountyhunter.Listener.PlayerDeathListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bountyhunter extends JavaPlugin {

    private static Bountyhunter instance;

    private static Economy econ = null;

    @Override
    public void onEnable() {
        instance = this;
        System.out.println("BOUNTIES ENABLED");

        ConfigManager.setupConfig(this);
        BountyManager.setupFile(this);
        LeaderboardManager.setupFile(this);
        AchievementsManager.setupFile(this);

        getCommand("bounties").setExecutor(new BountyHunterCommand());
        getCommand("bounties").setTabCompleter(new BountyHunterCommandTab());

        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);

        if (!setupEconomy() ) {
            System.out.println("No economy plugin found. Disabling Vault.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    @Override
    public void onDisable() {

    }

    public static Bountyhunter getInstance() { return instance; }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }
}
