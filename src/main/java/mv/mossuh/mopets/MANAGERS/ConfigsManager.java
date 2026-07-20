package mv.mossuh.mopets.MANAGERS;

import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.mocore.ACTIONS.ActionUtil.MoAction;
import mv.mossuh.mocore.ACTIONS.OtherUtil.MoCooldown;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.MoRequirement;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.MoRequirements;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.RequirementEval;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.RequirementEvent;
import mv.mossuh.mocore.ACTIONS.RewardUtil.AvailableRewards;
import mv.mossuh.mocore.ACTIONS.RewardUtil.MoReward;
import mv.mossuh.mocore.ACTIONS.RewardUtil.MoRewards;
import mv.mossuh.mocore.ACTIONS.RewardUtil.SelectedReward;
import mv.mossuh.mocore.CONFIG.FolderConfigs;
import mv.mossuh.mocore.CONFIG.MoConfig;
import mv.mossuh.mocore.ENUMS.ChanceType;
import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.ENUMS.PluginType;
import mv.mossuh.mocore.ENUMS.RequirementType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.PluginsChecker;
import mv.mossuh.mopets.CONFIGS.Config.Config;
import mv.mossuh.mopets.CONFIGS.Messages;
import mv.mossuh.mopets.CONFIGS.Pets.Actions.Actions;
import mv.mossuh.mopets.CONFIGS.Pets.Actions.DefaultActions;
import mv.mossuh.mopets.ENUMS.MultiplierType;
import mv.mossuh.mopets.CONFIGS.Pets.Actions.MoBoosters.LocalBooster;
import mv.mossuh.mopets.ENUMS.ExpType;
import mv.mossuh.mopets.CONFIGS.Pets.Pet.ConfigPet;
import mv.mossuh.mopets.CONFIGS.Pets.Pet.ConfigPets;
import mv.mossuh.mopets.CONFIGS.Pets.PetIdentifier;
import mv.mossuh.mopets.CONFIGS.Pets.ItemInfo;
import mv.mossuh.mopets.CONFIGS.Pets.Upgrades;
import mv.mossuh.mopets.CONFIGS.Pets.ItemInfoUtil.Enchantments;
import mv.mossuh.mopets.CONFIGS.Pets.ItemInfoUtil.EntityExp;
import mv.mossuh.mopets.CONFIGS.Pets.ItemInfoUtil.Exp;
import mv.mossuh.mopets.MoPets;
import mv.mossuh.mopets.UTILITIES.UtilMethods;
import mv.mossuh.mopets.UTILITIES.UtilString;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.ENUMS.BoosterType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigsManager {
    private static FolderConfigs petsConfigs;
    private static MoConfig mainConfig;
    private static MoConfig informationConfig;
    private static MoConfig messagesConfig;

    public ConfigsManager(MoPets main) {
        petsConfigs = new FolderConfigs(true, main, "pets");
        mainConfig = new MoConfig("config.yml", main, null);
        informationConfig = new MoConfig("information.yml", main, null);
        messagesConfig = new MoConfig("messages.yml", main, null);

    }

    public void configure() {
        mainConfig.registerConfig(true);
        informationConfig.registerConfig(true);
        messagesConfig.registerConfig(true);
        petsConfigs.configure("Example1.yml");
        Config.load();
        configurePets();
    }

    public MoConfig getMainConfig() {
        return mainConfig;
    }
    public MoConfig getMessagesConfig() { return messagesConfig; }

    public List<MoConfig> getConfigs() {
        List<MoConfig> configs = new ArrayList<>();

        configs.add(mainConfig);
        configs.addAll(petsConfigs.getConfigs());

        return configs;
    }

    private static final List<String> codes = new ArrayList<>();
    public static List<String> getCodes() {
        return codes;
    }

    public void reload(){
        mainConfig.reloadConfig();
        petsConfigs.reloadConfigs();
        messagesConfig.reloadConfig();

        ConfigPets.clearConfigPets();
        Config.load();
        Messages.load();

        configurePets();
    }

    private void configurePets() {
        int savedPets = 0;
        List<MoConfig> configs = getConfigs();

        for (MoConfig configClass : configs) {
            FileConfiguration config = configClass.getConfig();
            String fileName = configClass.getFileName();
            if (config.contains("Pets") && !config.getConfigurationSection("Pets").getKeys(false).isEmpty()) {
                for (String code : config.getConfigurationSection("Pets").getKeys(false)) {
                    codes.add(code);
                    if (!config.contains("Pets." + code + ".item-info.material")) {
                        UtilString.get("&8[" + Config.PREFIX + "&8] &cThe item couldn't be loaded in " + code + ", due to possible lack of material.").hex().sendMessageInConsole();
                        continue;
                    }
                    String stringTags = config.getString("Pets." + code + ".tags");
                    List<String> tags = UtilMethods.separateString(stringTags);
                    List<String> defaultVariableList = config.getStringList("Pets." + code + ".variables");
                    List<VariableArg> defaultVariables = VariableArg.fromConfig(defaultVariableList);
                    String boosterAvailable = config.getString("Pets." + code + ".booster-identifier");

                    Integer maxLevel = config.getInt("Pets." + code + ".upgrades.max-level");
                    Double costPerLevel = config.getDouble("Pets." + code + ".upgrades.cost-per-level");
                    List<String> progress = config.getStringList("Pets." + code + ".upgrades.message.progress");
                    List<String> maxedProgress = config.getStringList("Pets." + code + ".upgrades.message.maxed-progress");

                    String itemMaterial = config.getString("Pets." + code + ".item-info.material");
                    Byte itemData = (byte) config.getLong("Pets." + code + ".item-info.data");
                    String itemName = config.getString("Pets." + code + ".item-info.name");
                    List<String> itemLore = config.getStringList("Pets." + code + ".item-info.lore");
                    Boolean itemUnbreakable = config.getBoolean("Pets." + code + ".item-info.unbreakable");
                    Boolean itemUnique = config.getBoolean("Pets." + code + ".item-info.unique");
                    List<String> itemEnchantments = config.getStringList("Pets." + code + ".item-info.enchantments");
                    List<String> itemFlags = config.getStringList("Pets." + code + ".item-info.flags");

                    PetIdentifier itemIdentifier = new PetIdentifier(code, tags, defaultVariables, boosterAvailable);
                    Upgrades upgrades = new Upgrades(maxLevel, costPerLevel, progress, maxedProgress);
                    ItemInfo itemInfo = new ItemInfo(itemMaterial, itemData, itemName, itemLore, new Enchantments(itemEnchantments), itemFlags, itemUnbreakable, itemUnique);
                    List<Exp> exps = transformToExps(code, config);
                    DefaultActions defaultActions = transformToDefaultActions(code, config);

                    List<LocalBooster> boosters = new ArrayList<>();
                    if (PluginsChecker.isPluginEnabled(PluginType.MoBoosters)) {
                        if (config.contains("Pets." + code + ".actions.boosters")) {
                            for (String boosterSection : config.getConfigurationSection("Pets." + code + ".actions.boosters").getKeys(false)) {
                                MultiplierType multiplierType = UtilMethods.getMultiplierType(config.getString("Pets." + code + ".actions.boosters." + boosterSection + ".multiplier"));
                                BoosterType boosterType = mv.mossuh.moboosters.UTILITIES.UtilMethods.getBoosterType(config.getString("Pets." + code + ".actions.boosters." + boosterSection + ".type"));
                                ApplicatorType applicatorType = mv.mossuh.moboosters.UTILITIES.UtilMethods.getApplicatorType(config.getString("Pets." + code + ".actions.boosters." + boosterSection + ".applicator"));
                                String boosted = config.getString("Pets." + code + ".actions.boosters." + boosterSection + ".boosted");
                                Double boost = config.getDouble("Pets." + code + ".actions.boosters." + boosterSection + ".boost");

                                LocalBooster booster = new LocalBooster(new BoosterIdentifier(Config.PLUGIN_NAME, boosterType, applicatorType, boosted), multiplierType, boost);
                                if (!booster.isValid()) {
                                    continue;
                                }

                                boosters.add(booster);
                            }
                        }
                    }

                    Actions actions = new Actions(defaultActions, boosters);

                    ConfigPet configItem = new ConfigPet(itemIdentifier, upgrades, itemInfo, exps, actions);
                    ConfigPets.addPet(configItem);
                    savedPets = savedPets + 1;
                }
            }
        }

        UtilString.get("&8[" + Config.PREFIX + "&8] &aLoaded &2" + savedPets + " &apets of the configurations!").hex().sendMessageInConsole();
    }

    private static List<Exp> transformToExps(String code, FileConfiguration config) {
        List<Exp> exps = new ArrayList<>();
        if (config.contains("Pets." + code + ".exp")) {
            for (String expTypeString : config.getConfigurationSection("Pets." + code + ".exp").getKeys(false)) {
                ExpType expType = ExpType.valueOf(expTypeString);
                List<String> entitiesAsString = config.getStringList("Pets." + code + ".exp." + expTypeString);
                List<EntityExp> entityExps = EntityExp.stringToEntitiesExp(entitiesAsString);
                Exp exp = new Exp(expType, entitiesAsString, entityExps);
                exps.add(exp);
            }
        }
        return exps;
    }

    private static DefaultActions transformToDefaultActions(String code, FileConfiguration config) {
        List<MoAction> actionList = new ArrayList<>();
        Set<EventType> usedEvents = new HashSet<>();
        if (config.contains("Pets." + code + ".actions.default") && !config.getString("Pets." + code + ".actions.default").isEmpty()) {
            for (String actionName : config.getConfigurationSection("Pets." + code + ".actions.default").getKeys(false)) {

                List<MoRequirement> requirementList = new ArrayList<>();
                boolean cancelAction = config.getBoolean("Pets." + code + ".actions.default." + actionName + ".cancel_action");
                long cooldown = config.getLong("Pets." + code + ".actions.default." + actionName + ".cooldown");
                boolean cooldownByPass = config.getBoolean("Pets." + code + ".actions.default." + actionName + ".cooldown_bypass");
                String cooldownMessage = config.getString("Pets." + code + ".actions.default." + actionName + ".cooldown_message");
                MoCooldown moCooldown = new MoCooldown(cooldown, cooldownByPass, cooldownMessage);

                EventType eventType = UtilMethods.getEventType(config.getString("Pets." + code + ".actions.default." + actionName + ".event"));

                if (config.contains("Pets." + code + ".actions.default." + actionName + ".requirements")) {
                    List<String> requirementsAsString = config.getStringList("Pets." + code + ".actions.default." + actionName + ".requirements");
                    for (String requirementString : requirementsAsString) {
                        String[] requirementSplit = requirementString.split(" ", 2);
                        String requirementTypeAsString = requirementSplit[0].replace("]", "").replace("[", "").toUpperCase();
                        RequirementType requirementType = RequirementType.valueOf(requirementTypeAsString.toUpperCase());
                        String requirement = requirementSplit[1];
                        MoRequirement requirementClass = new MoRequirement(null, null);
                        if (requirementType.equals(RequirementType.EVENT)) {
                            requirementClass = new MoRequirement(requirementType, RequirementEvent.getRequirementEvent(requirement));
                        } else if (requirementType.equals(RequirementType.EVAL)){
                            requirementClass = new MoRequirement(requirementType, RequirementEval.separateEvals(requirement));
                        }
                        requirementList.add(requirementClass);
                    }
                }

                List<MoReward> rewardList = new ArrayList<>();

                if (config.contains("Pets." + code + ".actions.default." + actionName + ".rewards")) {
                    List<String> rewardsAsString = config.getStringList("Pets." + code + ".actions.default." + actionName + ".rewards");
                    for (String rewardString : rewardsAsString) {
                        String[] rewardSplit1 = rewardString.split("] ", 2);

                        String[] rewardSplit2 = rewardSplit1[0].replaceAll(" ", "").replace("]", "").replace("[", "").split("->", 2);
                        ChanceType chanceType = ChanceType.valueOf(rewardSplit2[0].toUpperCase());
                        String chance = "100.0";
                        if (rewardSplit2.length == 2) {
                            chance = rewardSplit2[1];
                        }

                        List<AvailableRewards> availableRewards = transformRandomRewards(rewardSplit1[1]);

                        MoReward reward = new MoReward(chanceType, chance, availableRewards);
                        rewardList.add(reward);
                    }
                }

                List<MoReward> elseRewardList = new ArrayList<>();

                if (config.contains("Pets." + code + ".actions.default." + actionName + ".else")) {
                    List<String> rewardsAsString = config.getStringList("Pets." + code + ".actions.default." + actionName + ".else");
                    for (String rewardString : rewardsAsString) {
                        String[] rewardSplit1 = rewardString.split("] ", 2);

                        String[] rewardSplit2 = rewardSplit1[0].replaceAll(" ", "").replace("]", "").replace("[", "").split("->", 2);
                        ChanceType chanceType = ChanceType.valueOf(rewardSplit2[0].toUpperCase());
                        String chance = "100.0";
                        if (rewardSplit2.length == 2) {
                            chance = rewardSplit2[1];
                        }

                        List<AvailableRewards> availableRewards = transformRandomRewards(rewardSplit1[1]);

                        MoReward reward = new MoReward(chanceType, chance, availableRewards);
                        elseRewardList.add(reward);
                    }
                }

                MoRequirements requirements = new MoRequirements(requirementList, null);
                MoRewards rewards = new MoRewards(rewardList, null);
                MoRewards elseRewards = new MoRewards(elseRewardList, null);
                MoAction action = new MoAction(actionName, cancelAction, moCooldown, eventType, requirements, rewards, elseRewards);
                actionList.add(action);
                if (eventType != EventType.INVALID) {
                    usedEvents.add(eventType);
                }
            }
        }
        return new DefaultActions(actionList, usedEvents);
    }

    @NotNull
    private static List<AvailableRewards> transformRandomRewards(String reward) {
        List<AvailableRewards> availableRewards = new ArrayList<>();

        String[] rewardSplitY = reward.split(" && ");

        for (String rewardOptions : rewardSplitY) {
            String[] separatedRandomReward = rewardOptions.split(" \\|\\| ");
            AvailableRewards randomRewards = new AvailableRewards(SelectedReward.transformToRewards(separatedRandomReward));
            availableRewards.add(randomRewards);
        }
        return availableRewards;
    }

}
