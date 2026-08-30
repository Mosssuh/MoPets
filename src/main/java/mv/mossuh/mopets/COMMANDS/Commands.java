package mv.mossuh.mopets.COMMANDS;

import mv.mossuh.mocore.UTILITIES.UsefulMethods;
import mv.mossuh.mopets.DATA.Config.Config;
import mv.mossuh.mopets.MoPets;
import mv.mossuh.mopets.UTILITIES.UtilString;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {
    /*

    /mopets give <code> <amount> <player> <args...>
    /mopets giveall <code> <amount> <args...>

    /mopets addexp <amount> <player>
    /mopets setexp <amount> <player>

    /mopets addlevel <amount> <player>
    /mopets setlevel <amount> <player>

    /mopets reload

    /mopets activepets
    /mopets check

     */

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] strings) {
        if (strings.length > 0) {
            switch (strings[0].toLowerCase()) {
                case "activepets":
                case "check":
                    ActivePetsCommands.onCommand(sender, strings);
                    break;
                case "give":
                case "giveall":
                    GivePetCommands.onCommand(sender, strings);
                    break;
                case "addexp":
                case "setexp":
                    ExpCommands.onCommand(sender, strings);
                    break;
                case "addlevel":
                case "setlevel":
                    LevelCommands.onCommand(sender, strings);
                    break;
                case "reload":
                    MoPets.getConfigs().reload();
                    UtilString.get("&8[" + Config.PREFIX + "&8] &aConfiguration reloaded!").hex().sendMessage(sender);
                    break;
            }
        } else {
            if (UsefulMethods.hasPermission(sender, "mopets.admin")) {
                UtilString.get("&r").hex().sendMessage(sender);
                UtilString.get("&8---------------------------------------------------").hex().sendMessage(sender);
                UtilString.get("&r").hex().sendMessage(sender);
                UtilString.get("&b/mopets give <code> <amount> <player>").hex().sendMessage(sender);
                UtilString.get("&b/mopets giveall <code> <amount>").hex().sendMessage(sender);
                UtilString.get("&r").hex().sendMessage(sender);
                UtilString.get("&b/mopets addexp <exp> <player>").hex().sendMessage(sender);
                UtilString.get("&b/mopets setexp <exp> <player>").hex().sendMessage(sender);
                UtilString.get("&r").hex().sendMessage(sender);
                UtilString.get("&b/mopets addlevel <level> <player>").hex().sendMessage(sender);
                UtilString.get("&b/mopets setlevel <level> <player>").hex().sendMessage(sender);
                UtilString.get("&r").hex().sendMessage(sender);
                UtilString.get("&r").hex().sendMessage(sender);
                UtilString.get("&b/mopets activepets").hex().sendMessage(sender);
                UtilString.get("&b/mopets check").hex().sendMessage(sender);
                UtilString.get("&8---------------------------------------------------").hex().sendMessage(sender);
                UtilString.get("&r").hex().sendMessage(sender);
            }
        }
        return false;
    }
}
