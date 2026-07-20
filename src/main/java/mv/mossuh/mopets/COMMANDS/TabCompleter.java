package mv.mossuh.mopets.COMMANDS;

import mv.mossuh.mopets.MANAGERS.ConfigsManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("give", "giveall", "addexp", "setexp", "addlevel", "setlevel", "reload", "activepets", "check");
        } else if (args.length > 1) {
            String arg = args[0].toLowerCase();
            if (args.length == 2) {
                switch (arg) {
                    case "give":
                    case "giveall":
                        return ConfigsManager.getCodes();
                    case "addexp":
                    case "setexp":
                    case "addlevel":
                    case "setlevel":
                        return Arrays.asList("1", "2", "3", "4", "5");
                }
            } else if (args.length == 3) {
                switch (arg) {
                    case "give":
                    case "giveall":
                        return Arrays.asList("1", "2", "3", "4", "5");
                    case "addexp":
                    case "setexp":
                    case "addlevel":
                    case "setlevel":
                        return getPlayers();
                }
            } else if (args.length == 4) {
                if (arg.equals("give")) {
                    return getPlayers();
                }
            }
        }
        // en otro caso, no sugerimos nada
        return Collections.emptyList();
    }

    public List<String> getPlayers() {
        List<String> players = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            players.add(player.getName());
        }

        return players;
    }
}
