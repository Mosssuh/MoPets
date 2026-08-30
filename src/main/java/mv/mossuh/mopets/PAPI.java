package mv.mossuh.mopets;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import mv.mossuh.moboosters.MODEL.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.UTILITIES.Enums.ApplicatorType;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;
import mv.mossuh.moboosters.UTILITIES.UtilMethods;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mopets.API.PetsAPI;
import mv.mossuh.mopets.DATA.Config.Config;
import mv.mossuh.mopets.MODEL.Pets.Config.ConfigPet;
import mv.mossuh.mopets.MODEL.Pets.PetIdentifier;
import mv.mossuh.mopets.MODEL.Pets.Pet;
import mv.mossuh.mopets.MODEL.Player.PetsPlayer;
import mv.mossuh.mopets.UTILITIES.UtilString;
import org.bukkit.entity.Player;

import java.util.*;

public class PAPI extends PlaceholderExpansion {
    private MoPets instance = MoPets.getInstance();

    /**
     * Because this is an internal class,
     * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
     * PlaceholderAPI is reloaded
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist(){
        return true;
    }
    /**
     * Since this expansion requires api access to the plugin "SomePlugin"
     * we must check if said plugin is on the server or not.
     *
     * @return true or false depending on if the required plugin is installed.
     */
    @Override
    public boolean canRegister(){
        return true;
    }

    /**
     * The name of the person who created this expansion should go here.
     *
     * @return The name of the author as a String.
     */
    @Override
    public String getAuthor(){
        return "Mossuh";
    }

    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest
     * method to obtain a value if a placeholder starts with our
     * identifier.
     * <br>This must be unique and can not contain % or _
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public String getIdentifier(){
        return "mopets";
    }

    /**
     * This is the version of this expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * @return The version as a String.
     */
    @Override
    public String getVersion(){
        return instance.getDescription().getVersion();
    }

    /**
     * This is the method called when a placeholder with our identifier
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param  player
     *         A {@link Player Player}.
     * @param  identifier
     *         A String containing the identifier/value.
     *
     * @return possibly-null String of the requested identifier.
     */

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        if (player == null) {
            return "";
        }

        UUID uuid = player.getUniqueId();

        String placeholder = identifier.toLowerCase();

        if (placeholder.startsWith("active_pets")) {
            // %mopets_active_pets%
            List<String> codes = new ArrayList<>();
            PetsPlayer petsPlayer = PetsAPI.getManager().getPlayer(uuid);
            if (petsPlayer.isPlayer() && petsPlayer.hasPets()) {
                List<Pet> pets = petsPlayer.getPets();
                for (Pet pet : pets) {
                    codes.add(pet.getConfigPet().getPetIdentifier().getCode());
                }
            }
            return String.join(", ", codes);
        } else if (placeholder.startsWith("active_tags")) {
            // %mopets_active_tags_<pet_code>%
            Set<String> tags = new HashSet<>();
            String code = placeholder.replace("active_tags_", "");
            PetsPlayer petsPlayer = PetsAPI.getManager().getPlayer(uuid);

            if (petsPlayer.isPlayer() && petsPlayer.hasPets()) {
                List<Pet> pets = petsPlayer.getPets();
                if (pets.isEmpty()) return String.join(", ", tags);

                for (Pet pet : pets) {
                    PetIdentifier petIdentifier = pet.getConfigPet().getPetIdentifier();
                    if (!code.isEmpty()) {
                        if (petIdentifier.getCode().equalsIgnoreCase(code)) return String.join(", ", tags);
                    } else {
                        tags.addAll(petIdentifier.getTags());
                    }
                }
            }
            return String.join(", ", tags);
        } else if (placeholder.startsWith("active_has_tag_")) {
            // %mopets_active_has_tag_<tag>_<pet code>%
            PetsPlayer petsPlayer = PetsAPI.getManager().getPlayer(uuid);
            List<Pet> pets = petsPlayer.getPets();

            String[] stringSplit = placeholder.replace("active_has_tag_", "").split("_", 2);
            if (stringSplit.length == 2) {
                String code = stringSplit[1];
                String tag = stringSplit[0];
                if (petsPlayer.isPlayer() && petsPlayer.hasPets()) {
                    for (Pet pet : pets) {
                        PetIdentifier petIdentifier = pet.getConfigPet().getPetIdentifier();
                        if (petIdentifier.getCode().equalsIgnoreCase(code)) {
                            return petIdentifier.hasTag(tag) + "";
                        }
                    }
                }
            } else if (stringSplit.length == 1) {
                String tag = stringSplit[0];
                if (petsPlayer.isPlayer() && petsPlayer.hasPets()) {
                    for (Pet pet : pets) {
                        PetIdentifier petIdentifier = pet.getConfigPet().getPetIdentifier();
                        if (petIdentifier.hasTag(tag)) {
                            return "true";
                        }
                    }
                    return "false";
                }
            }
            return "false";
        } else if (placeholder.startsWith("active_level")) {
            // %mopets_active_level_<pet code>%
            String code = placeholder.replace("active_level_", "");
            PetsPlayer petsPlayer = PetsAPI.getManager().getPlayer(uuid);
            int level = 0;
            if (petsPlayer.isPlayer() && petsPlayer.hasPets()) {
                List<Pet> pets = petsPlayer.getPets();
                for (Pet pet : pets) {
                    PetIdentifier petIdentifier = pet.getConfigPet().getPetIdentifier();
                    if (!code.isEmpty()) {
                        if (petIdentifier.getCode().equalsIgnoreCase(code)) return pet.getLevel()+"";
                    } else {
                        level += pet.getLevel();
                    }
                }
            }
            return level+"";
        } else if (placeholder.startsWith("active_exp")) {
            // %mopets_active_exp_<pet code>%
            String code = placeholder.replace("active_exp_", "");
            PetsPlayer petsPlayer = PetsAPI.getManager().getPlayer(uuid);
            double exp = 0;
            if (petsPlayer.isPlayer() && petsPlayer.hasPets()) {
                List<Pet> pets = petsPlayer.getPets();
                for (Pet pet : pets) {
                    PetIdentifier petIdentifier = pet.getConfigPet().getPetIdentifier();
                    if (!code.isEmpty()) {
                        if (petIdentifier.getCode().equalsIgnoreCase(code)) return pet.getExp()+"";
                    } else {
                        exp += pet.getExp();
                    }
                }
            }
            return exp+"";
        } else if (placeholder.startsWith("active_cost")) {
            // %mopets_active_cost_<pet code>%
            String code = placeholder.replace("active_cost_", "");
            PetsPlayer petsPlayer = PetsAPI.getManager().getPlayer(uuid);
            double cost = 0;
            if (petsPlayer.isPlayer() && petsPlayer.hasPets()) {
                List<Pet> pets = petsPlayer.getPets();

                for (Pet pet : pets) {
                    PetIdentifier petIdentifier = pet.getConfigPet().getPetIdentifier();
                    if (!code.isEmpty()) {
                        if (petIdentifier.getCode().equalsIgnoreCase(code)) return pet.getCost()+"";
                    } else {
                        cost += pet.getCost();
                    }
                }
            }
            return cost+"";
        } else if (placeholder.startsWith("active_max_level")) {
            // %mopets_active_max_level_<pet code>%
            String code = placeholder.replace("active_max_level_", "");
            PetsPlayer petsPlayer = PetsAPI.getManager().getPlayer(uuid);
            int maxLevel = 0;
            if (petsPlayer.isPlayer() && petsPlayer.hasPets()) {
                List<Pet> pets = petsPlayer.getPets();

                for (Pet pet : pets) {
                    PetIdentifier petIdentifier = pet.getConfigPet().getPetIdentifier();
                    if (!code.isEmpty()) {
                        if (petIdentifier.getCode().equalsIgnoreCase(code)) return pet.getConfigPet().getUpgrades().getMaxLevel()+"";
                    } else {
                        maxLevel += pet.getConfigPet().getUpgrades().getMaxLevel();
                    }
                }
            }
            return maxLevel+"";
        } else if (placeholder.startsWith("active_variables")) {
            // %mopets_active_variables_<pet code>%
            String code = placeholder.replace("active_variables_", "");
            PetsPlayer petsPlayer = PetsAPI.getManager().getPlayer(uuid);
            Set<VariableArg> variables = new HashSet<>();
            if (petsPlayer.isPlayer() && petsPlayer.hasPets()) {
                List<Pet> pets = petsPlayer.getPets();
                for (Pet pet : pets) {
                    PetIdentifier petIdentifier = pet.getConfigPet().getPetIdentifier();
                    if (!code.isEmpty()) {
                        if (petIdentifier.getCode().equalsIgnoreCase(code)) return pet.getVariablesAsString();
                    } else {
                        variables.addAll(pet.getVariables());
                    }
                }
            }
            return VariableArg.toString(new ArrayList<>(variables));
        } else if (placeholder.startsWith("active_variable_")) {
            // %mopets_active_variable_{<variable>}_<pet code>%
            PetsPlayer petsPlayer = PetsAPI.getManager().getPlayer(uuid);
            List<Pet> pets = petsPlayer.getPets();
            Set<String> values = new HashSet<>();

            String[] stringSplit = placeholder.replace("active_variable_", "").split("_", 2);
            if (stringSplit.length == 2) {
                String code = stringSplit[1];
                String variable = stringSplit[0].replaceFirst("\\{", "").replace("}", "");
                if (petsPlayer.isPlayer() && petsPlayer.hasPets()) {
                    for (Pet pet : pets) {
                        PetIdentifier petIdentifier = pet.getConfigPet().getPetIdentifier();
                        if (petIdentifier.getCode().equalsIgnoreCase(code)) {
                            return pet.getVariable(variable).getValue();
                        }
                    }
                }
            } else if (stringSplit.length == 1) {
                String variable = stringSplit[0].replaceFirst("\\{", "").replace("}", "");
                if (petsPlayer.isPlayer() && petsPlayer.hasPets()) {
                    for (Pet pet : pets) {
                        if (pet.hasVariable(variable)) {
                            values.add(pet.getVariable(variable).getValue());
                        }
                    }
                }
            }
            return String.join(", ", values);
        } else if (placeholder.startsWith("active_has_variable_")) {
            // %mopets_active_has_variable_{<variable>}_<pet code>%
            PetsPlayer petsPlayer = PetsAPI.getManager().getPlayer(uuid);
            List<Pet> pets = petsPlayer.getPets();

            String[] stringSplit = placeholder.replace("active_has_variable_", "").split("_", 2);
            if (stringSplit.length == 2) {
                String code = stringSplit[1];
                String variable = stringSplit[0].replaceFirst("\\{", "").replace("}", "");
                if (petsPlayer.isPlayer() && petsPlayer.hasPets()) {
                    for (Pet pet : pets) {
                        PetIdentifier petIdentifier = pet.getConfigPet().getPetIdentifier();
                        if (petIdentifier.getCode().equalsIgnoreCase(code)) {
                            return pet.hasVariable(variable) + "";
                        }
                    }
                }
            } else if (stringSplit.length == 1) {
                String variable = stringSplit[0].replaceFirst("\\{", "").replace("}", "");
                if (petsPlayer.isPlayer() && petsPlayer.hasPets()) {
                    for (Pet pet : pets) {
                        if (pet.hasVariable(variable)) return "true";
                    }
                }
            }
            return "false";
        } else if (placeholder.startsWith("active_boost_")) {
            // %mopets_active_boost_<booster type>_<applicator type>_<boosted>_<pet code>%
            PetsPlayer petsPlayer = PetsAPI.getManager().getPlayer(uuid);
            List<Pet> pets = petsPlayer.getPets();

            String[] valueSplit1 = placeholder.replace("active_boost_", "").split("_", 4);
            double boost = 0;

            if (valueSplit1.length == 4) {
                BoosterType boosterType = UtilMethods.getBoosterType(valueSplit1[0]);
                ApplicatorType applicatorType = ApplicatorType.convert(valueSplit1[1]);
                String boosted = valueSplit1[2];
                BoosterIdentifier boosterIdentifier = new BoosterIdentifier(Config.PLUGIN_NAME, boosterType, applicatorType, boosted);
                String code = valueSplit1[3];

                if (petsPlayer.isPlayer() && petsPlayer.hasPets()) {
                    for (Pet pet : pets) {
                        int level = pet.getLevel();
                        ConfigPet configPet = pet.getConfigPet();
                        PetIdentifier petIdentifier = configPet.getPetIdentifier();
                        if (petIdentifier.getCode().equalsIgnoreCase(code)) {
                            return configPet.getActions().getBooster(boosterIdentifier).getBoost(level)+"";
                        }
                    }
                }
            } else if (valueSplit1.length == 3) {
                BoosterType boosterType = UtilMethods.getBoosterType(valueSplit1[0]);
                ApplicatorType applicatorType = ApplicatorType.convert(valueSplit1[1]);
                String boosted = valueSplit1[2];
                BoosterIdentifier boosterIdentifier = new BoosterIdentifier(Config.PLUGIN_NAME, boosterType, applicatorType, boosted);
                if (petsPlayer.isPlayer() && petsPlayer.hasPets()) {
                    for (Pet pet : pets) {
                        int level = pet.getLevel();
                        ConfigPet configPet = pet.getConfigPet();
                        boost += configPet.getActions().getBooster(boosterIdentifier).getBoost(level);
                    }
                }
            }
            return boost+"";
        } else if (placeholder.startsWith("active_has_boost_")) {
            // %mopets_active_has_boost_<booster type>_<applicator type>_<boosted>_<pet code>%
            PetsPlayer petsPlayer = PetsAPI.getManager().getPlayer(uuid);
            List<Pet> pets = petsPlayer.getPets();

            String[] valueSplit1 = placeholder.replace("active_boost_", "").split("_", 4);
            if (valueSplit1.length == 4) {
                BoosterType boosterType = UtilMethods.getBoosterType(valueSplit1[0]);
                ApplicatorType applicatorType = ApplicatorType.convert(valueSplit1[1]);
                String boosted = valueSplit1[2];
                BoosterIdentifier boosterIdentifier = new BoosterIdentifier(Config.PLUGIN_NAME, boosterType, applicatorType, boosted);
                String code = valueSplit1[3];

                if (petsPlayer.isPlayer() && petsPlayer.hasPets()) {
                    for (Pet pet : pets) {
                        ConfigPet configPet = pet.getConfigPet();
                        PetIdentifier petIdentifier = configPet.getPetIdentifier();
                        if (petIdentifier.getCode().equalsIgnoreCase(code)) {
                            return configPet.getActions().hasBooster(boosterIdentifier) + "";
                        }
                    }
                }
            } else if (valueSplit1.length == 3) {
                BoosterType boosterType = UtilMethods.getBoosterType(valueSplit1[0]);
                ApplicatorType applicatorType = ApplicatorType.convert(valueSplit1[1]);
                String boosted = valueSplit1[2];
                BoosterIdentifier boosterIdentifier = new BoosterIdentifier(Config.PLUGIN_NAME, boosterType, applicatorType, boosted);

                if (petsPlayer.isPlayer() && petsPlayer.hasPets()) {
                    for (Pet pet : pets) {
                        ConfigPet configPet = pet.getConfigPet();
                        if (configPet.getActions().hasBooster(boosterIdentifier)) return "true";
                    }
                }
            }
            return "false";
        }

        return UtilString.get("&cInvalid Placeholder").hex().apply();
    }
}
