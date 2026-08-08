package mv.mossuh.mopets.UTILITIES;

import me.clip.placeholderapi.PlaceholderAPI;
import mv.mossuh.mocore.UTILITIES.ARGS.CommandArgs.CommandArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.UsefulMethods;
import mv.mossuh.mopets.CONFIGS.Config.Config;
import mv.mossuh.mopets.CONFIGS.Pets.Pet.ConfigPet;
import mv.mossuh.mopets.CONFIGS.Pets.PetIdentifier;
import mv.mossuh.mopets.PETS.Pet.Pet;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilString {

    private String string;
    public UtilString(String string) {
        this.string = string;
    }

    public static UtilString get(String string) {
        return new UtilString(string);
    }

    public UtilString hex() {
        if (string != null && !string.isEmpty()) {
            Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
            Matcher matcher = pattern.matcher(string);
            while (matcher.find()) {
                String hexCode = string.substring(matcher.start(), matcher.end());
                String replaceSharp = hexCode.replace('#', 'x');

                char[] ch = replaceSharp.toCharArray();
                StringBuilder builder = new StringBuilder("");
                for (char c : ch) {
                    builder.append("&" + c);
                }

                string = string.replace(hexCode, builder.toString());
                matcher = pattern.matcher(string);
            }
            string = ChatColor.translateAlternateColorCodes('&', string);
        }
        return this;
    }

    public UtilString replaceString(String target, String replacement) {
        if (string != null && !string.isEmpty()) {
            string = string.replace(target, replacement);
        }
        return this;
    }

    public UtilString setPlaceholders(Player player) {
        if (string != null && !string.isEmpty()) {
            if (player != null && player.isOnline()) {
                string = PlaceholderAPI.setPlaceholders(player, string);
            }
        }
        return this;
    }

    public UtilString setPlaceholders(LivingEntity entity) {
        if (string != null && !string.isEmpty()) {
            if (entity instanceof Player) {
                Player player = (Player) entity;
                string = PlaceholderAPI.setPlaceholders(player, string);
            }
        }
        return this;
    }

    public UtilString setPlaceholders(CommandSender sender) {
        if (string != null && !string.isEmpty()) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                string = PlaceholderAPI.setPlaceholders(player, string);
            }
        }
        return this;
    }

    public UtilString setPlaceholders(OfflinePlayer player) {
        if (string != null && !string.isEmpty()) {
            if (player != null) {
                string = PlaceholderAPI.setPlaceholders(player, string);
            }
        }
        return this;
    }

    public UtilString setPlaceholders(UUID uuid) {
        if (string != null && !string.isEmpty()) {
            if (uuid != null) {
                if (Bukkit.getPlayer(uuid) != null) {
                    Player player = Bukkit.getPlayer(uuid);
                    string = PlaceholderAPI.setPlaceholders(player, string);
                } else {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                    string = PlaceholderAPI.setPlaceholders(player, string);
                }
            }
        }
        return this;
    }

    public UtilString removeColors() {
        if (string != null && !string.isEmpty()) {
            string = string.replace('&', '§');
            string = ChatColor.stripColor(string);
        }
        return this;
    }


    public boolean evaluateString() {
        if (string != null && !string.isEmpty()) {
            return UsefulMethods.evaluateString(string);
        }
        return false;
    }
    public boolean isNumeric() {
        if (string != null && !string.isEmpty()) {
            try {
                Double.parseDouble(string);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    public boolean hasPermission(CommandSender sender) {
        if (string != null && !string.isEmpty()) {
            if (sender != null) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    return player.hasPermission(string);
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasPermission(Player player) {
        if (string != null && !string.isEmpty()) {
            if (player != null) {
                return player.hasPermission(string);
            }
        }
        return false;
    }

    public boolean hasPermission(UUID uuid) {
        if (string != null && !string.isEmpty()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                return player.hasPermission(string);
            }
        }
        return false;
    }


    public UtilString setVariables(List<VariableArg> variables) {
        if (string != null && !string.isEmpty() && !variables.isEmpty()) {
            for (VariableArg variable : variables) {
                if (variable.isVariable() && variable.isValue()) {
                    String variableString = variable.getVariable();
                    String valueString = variable.getValue();
                    if (string.contains(variableString)) {
                        string = string.replace(variableString, valueString);
                    }
                }
            }
        }
        return this;
    }

    public UtilString setDefaultPlayerVariables(Player player) {
        if (string != null && !string.isEmpty()) {
            if (player != null && player.isOnline()) {
                string = string.replace("%player%", player.getName());
            }
        }
        return this;
    }

    public UtilString setDefaultNumberRandomVariable() {
        if (string != null && !string.isEmpty()) {
            if (string.contains("{random_")) {
                Pattern pattern = Pattern.compile("\\{random_(\\d+)-(\\d+)}");
                Matcher matcher = pattern.matcher(string);
                if (matcher.find()) {
                    double min = Integer.parseInt(matcher.group(1));
                    double max = Integer.parseInt(matcher.group(2));
                    string = string.replace(matcher.group(0), UsefulMethods.random(min, max) + "");
                }
            }
        }
        return this;
    }

    public UtilString setMathPlaceholder() {
        string = UsefulMethods.setMathPlaceholder(string);
        return this;
    }

    public UtilString setChangeOutputPlaceholder() {
        string = UsefulMethods.setChangeOutputPlaceholder(string);
        return this;
    }

    public UtilString setTimeFormatter() {
        string = UsefulMethods.setTimeFormatter(string, Config.TIME_FORMAT);
        return this;
    }

    public UtilString setArgs(CommandArgs commandArgs) {
        if (string != null && !string.isEmpty()) {
            if (commandArgs.hasArgs()) {
                List<String> args = commandArgs.getArgs();
                for (int i = 0; i < args.size(); i++) {
                    string = string.replace("%args_" + (i+1) + "%", args.get(i));
                }
            }
        }
        return this;
    }


    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public UtilString setDefaultVariables(Pet pet) {
        if (string != null && !string.isEmpty()) {
            if (pet.isPet()) {
                ConfigPet configPet = pet.getConfigPet();
                PetIdentifier petIdentifier = configPet.getPetIdentifier();
                String code = petIdentifier.getCode();
                String tags = petIdentifier.getTagsAsString();
                List<VariableArg> variables = pet.getVariables();

                String level = String.valueOf(pet.getLevel());
                String exp = UsefulMethods.formatNumber(pet.getExp(), 2);
                String cost = UsefulMethods.formatNumber(pet.getCost(), 2);
                int maxLevel = configPet.getUpgrades().getMaxLevel();
                string = string.replace("%level%", level);
                string = string.replace("%exp%", exp);
                string = string.replace("%cost%", cost);
                string = string.replace("%max_level%", String.valueOf(maxLevel));
                string = string.replace("%tags%", tags);
                string = string.replace("%code%", code);
                for (VariableArg variable : variables) {
                    string = string.replace("%variable_{" + variable.getVariable() + "}%", variable.getValue());
                }
            }
        }
        return this;
    }

    public UtilString setDefaultVariables(ConfigPet configPet) {
        if (string != null && !string.isEmpty()) {
            if (configPet.isConfigPet()) {
                PetIdentifier petIdentifier = configPet.getPetIdentifier();
                List<VariableArg> defaultVariables = petIdentifier.getDefaultVariables();
                String code = petIdentifier.getCode();
                String tags = petIdentifier.getTagsAsString();
                int maxLevel = configPet.getUpgrades().getMaxLevel();
                String costPerLevel = UsefulMethods.formatNumber(configPet.getUpgrades().getCostPerLevel(), 2);
                string = string.replace("%level%", "1");
                string = string.replace("%exp%", "0");
                string = string.replace("%cost%", costPerLevel);
                string = string.replace("%max_level%", String.valueOf(maxLevel));
                string = string.replace("%tags%", tags);
                string = string.replace("%code%", code);
                for (VariableArg variable : defaultVariables) {
                    string = string.replace("%variable_{" + variable.getVariable() + "}%", variable.getValue());
                }
            }
        }
        return this;
    }


    public String apply() {
        return string;
    }

    public void sendMessage(CommandSender sender) {
        if (string != null && !string.isEmpty()) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage(string);
            } else {
                Bukkit.getConsoleSender().sendMessage(string);
            }
        }
    }

    public void sendMessage(Player player) {
        if (string != null && !string.isEmpty()) {
            if (player != null && player.isOnline()) {
                player.sendMessage(string);
            }
        }
    }

    public void sendMessage(UUID uuid) {
        if (string != null && !string.isEmpty()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                player.sendMessage(string);
            }
        }
    }

    public void sendMessageInConsole() {
        if (string != null && !string.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage(string);
        }
    }

    public void sendMessageToOnlinePlayers() {
        if (string != null && !string.isEmpty()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(string);
            }
        }
    }

}
