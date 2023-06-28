package bountyhunter.bountyhunter.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class BountyHunterCommandTab implements TabCompleter {

    List<String> arguments = new ArrayList<>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(arguments.isEmpty()) {
            arguments.add("create");
            arguments.add("amount");
            arguments.add("list");
            arguments.add("clear");
            arguments.add("leaderboards");
            arguments.add("reload");
            arguments.add("achievements");
            arguments.add("help");
        }

        List<String> result = new ArrayList<>();
        if(args.length == 1) {
            for (String a : arguments) {
                if(a.toLowerCase().startsWith(args[0].toLowerCase()))
                    result.add(a);
            }
            return result;
        }

        return null;
    }
}
