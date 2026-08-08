package mv.mossuh.mopets.ACTIONS;

import mv.mossuh.mocore.ACTIONS.ActionUtil.MoAction;
import mv.mossuh.mocore.ACTIONS.RewardUtil.AvailableRewards;
import mv.mossuh.mocore.ACTIONS.RewardUtil.MoReward;
import mv.mossuh.mocore.ACTIONS.RewardUtil.MoRewards;
import mv.mossuh.mocore.ACTIONS.RewardUtil.SelectedReward;
import mv.mossuh.mocore.ENUMS.ChanceType;
import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.ENUMS.RewardReceiverType;
import mv.mossuh.mocore.ENUMS.RewardType;
import mv.mossuh.mocore.MoCore;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.REWARDS.RewardReceiver;
import mv.mossuh.mocore.UTILITIES.UsefulMethods;
import mv.mossuh.mopets.ACTIONS.RewardsUtils.RewardMethods;
import mv.mossuh.mopets.ACTIONS.RewardsUtils.VariableSeparator;
import mv.mossuh.mopets.CONFIGS.Pets.Actions.Actions;
import mv.mossuh.mopets.PETS.Pet.Pet;
import mv.mossuh.mopets.UTILITIES.UtilMethods;
import mv.mossuh.mopets.UTILITIES.UtilString;
import mv.mossuh.mopets.UTILITIES.MoArgs;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Rewards {
    private final MoCore instance = MoCore.getInstance();
    private boolean executeDefaultRewards = false;
    private boolean executePetRewards = false;
    private boolean executeVariableRewards = false;
    private boolean cancelEvent = false;
    private boolean cancelDrops = false;
    private boolean cancelMessage = false;

    private ActionResult actionResult = new ActionResult(null, null, null, null, null, null, null);
    private int times = 1;


    public Rewards(ActionResult actionResult) {
        if (actionResult != null) { this.actionResult = actionResult; }
    }

    public Rewards(ActionResult actionResult, Integer times) {
        if (actionResult != null) { this.actionResult = actionResult; }
        if (times != null) { this.times = times; }
    }

    public boolean cancelDrops() { return cancelDrops; }

    public boolean cancelEvent() { return cancelEvent; }
    public boolean cancelMessage() { return cancelMessage; }


    public Rewards executeDefault() {
        this.executeDefaultRewards = true;
        return this;
    }

    public Rewards executePets() {
        this.executePetRewards = true;
        return this;
    }

    public Rewards executeVariables() {
        this.executeVariableRewards = true;
        return this;
    }

    public Rewards check() {
        if(!Bukkit.isPrimaryThread()){
            new BukkitRunnable(){
                @Override
                public void run() {
                    for (int i = 0; i < times; i++) {
                        rewards();
                    }
                }
            }.runTask(instance);
        } else{
            for (int i = 0; i < times; i++) {
                rewards();
            }
        }
        return this;
    }

    private void rewards() {
        Event event = actionResult.getEvent();
        EventType eventType = actionResult.getEventType();
        Player player = actionResult.getPlayer();
        Pet pet = actionResult.getPet();
        Actions actions = pet.getConfigPet().getActions();
        List<MoRewards> approvedRewards = actionResult.getApprovedRewards();

        MoArgs args = actionResult.getArgs();
        List<VariableArg> variables = actionResult.getVariables();

        RewardArgs rewardArgs = args.getRewardArgs();
        RewardArgsType rewardArgsType = rewardArgs.getArgumentType();

        int level = pet.getLevel();

        if (actionResult.hasApprovedRewards()) {
            for (MoRewards moRewards : approvedRewards) {
                List<VariableArg> actionVariables = moRewards.getVariables();
                List<MoReward> rewardList = moRewards.getRewards();
                for (MoReward moReward : rewardList) {

                    double rewardChance = Double.parseDouble(moReward.getChance());
                    ChanceType chanceType = moReward.getChanceType();
                    double chance = UtilMethods.transformChance(chanceType, rewardChance, level);
                    double random = UsefulMethods.randomNumber();

                    if (random >= chance) {
                        List<AvailableRewards> availableRewardsList = moReward.getAvailableRewards();
                        if (!availableRewardsList.isEmpty()) {
                            for (AvailableRewards availableRewards : availableRewardsList) {
                                if (availableRewards.hasReward()) {
                                    LivingEntity entityReceiver = null;
                                    Location location = null;
                                    SelectedReward randomSelectedReward = availableRewards.getRandomReward();
                                    RewardReceiver rewardReceiver = randomSelectedReward.getRewardReceiver();
                                    RewardReceiverType rewardReceiverType = rewardReceiver.getRewardReceiverType();
                                    String reward = null;
                                    if (rewardReceiverType.equals(RewardReceiverType.TARGET)) {
                                        if (rewardArgsType.equals(RewardArgsType.LIVING_ENTITY)) {
                                            entityReceiver = rewardArgs.getLivingEntity();
                                        }
                                        location = rewardArgs.getLocation();
                                    } else if (rewardReceiverType.equals(RewardReceiverType.PLACEHOLDER)) {
                                        if (rewardReceiver.hasRewardReceiver()) {
                                            String rewardReceiverString = UtilString.get(rewardReceiver.getRewardReceiver()).setDefaultNumberRandomVariable().setVariables(variables)
                                                    .setVariables(actionVariables).setPlaceholders(player).setChangeOutputPlaceholder().setMathPlaceholder().setTimeFormatter().apply();
                                            Player rewardPlayer = Bukkit.getPlayer(rewardReceiverString);
                                            if (rewardPlayer != null && rewardPlayer.isOnline()) {
                                                entityReceiver = rewardPlayer;
                                                location = entityReceiver.getLocation();
                                            }
                                        }
                                    } else {
                                        entityReceiver = player;
                                        location = player.getLocation();
                                    }
                                    reward = UtilString.get(randomSelectedReward.getReward()).hex().setDefaultNumberRandomVariable()
                                            .setVariables(variables).setVariables(actionVariables).setPlaceholders(player).setChangeOutputPlaceholder().setMathPlaceholder()
                                            .setTimeFormatter().apply();

                                    EntityType entityReceiverType = EntityType.UNKNOWN;
                                    if (entityReceiver != null) {
                                        entityReceiverType = entityReceiver.getType();
                                    }

                                    RewardType rewardType = randomSelectedReward.getRewardType();
                                    if (reward == null) {
                                        continue;
                                    }

                                    if (executeDefaultRewards) {
                                        if (rewardType.equals(RewardType.CONSOLE_COMMAND)) {
                                            RewardMethods.consoleCommand(reward);
                                        } else if (rewardType.equals(RewardType.PLAYER_COMMAND)) {
                                            RewardMethods.playerCommand(entityReceiver, reward);
                                        } else if (rewardType.equals(RewardType.PLAYER_COMMAND_AS_OP)) {
                                            RewardMethods.playerCommandAsOP(entityReceiver, reward);
                                        } else if (rewardType.equals(RewardType.MESSAGE)) {
                                            RewardMethods.playerMessage(entityReceiver, reward);
                                        } else if (rewardType.equals(RewardType.TITLE)) {
                                            RewardMethods.playerTitle(entityReceiver, reward);
                                        } else if (rewardType.equals(RewardType.SOUND)) {
                                            RewardMethods.playerSound(entityReceiver, reward);
                                        } else if (rewardType.equals(RewardType.BROADCAST_MESSAGE)) {
                                            Bukkit.broadcastMessage(reward);
                                        } else if (rewardType.equals(RewardType.BROADCAST_TITLE)) {
                                            RewardMethods.broadcastTitle(reward);
                                        } else if (rewardType.equals(RewardType.JSON)) {
                                            RewardMethods.json(entityReceiver, reward);
                                        } else if (rewardType.equals(RewardType.JSON_BROADCAST)) {
                                            RewardMethods.jsonBroadcast(reward);
                                        } else if (rewardType.equals(RewardType.EFFECT)) {
                                            RewardMethods.effect(entityReceiver, reward);
                                        } else if (rewardType.equals(RewardType.WORLD_DROP)) {
                                            RewardMethods.worldDrop(entityReceiver, location, reward);
                                        } else if (rewardType.equals(RewardType.GIVE_ITEM)) {
                                            RewardMethods.giveItem(entityReceiver, reward);
                                        } else if (rewardType.equals(RewardType.CANCEL_DROPS)) {
                                            cancelDrops = true;
                                        } else if (rewardType.equals(RewardType.CANCEL_EVENT)) {
                                            cancelEvent = true;
                                        } else if (rewardType.equals(RewardType.EXECUTE_ACTION)) {
                                            if (entityReceiverType.equals(EntityType.PLAYER)) {
                                                MoAction rewardAction = actions.getDefaultActions().getAction(reward);
                                                ExecuteAction executeAction = new ExecuteAction(rewardAction, event, eventType, (Player) entityReceiver, pet, args)
                                                        .addVariables(variables).check();
                                                if (executeAction.cancelEvent()) {
                                                    this.cancelEvent = true;
                                                }
                                                if (executeAction.cancelDrops()) {
                                                    this.cancelDrops = true;
                                                }
                                                if (executeAction.cancelMessage()) {
                                                    this.cancelMessage = true;
                                                }
                                            }
                                        } else if (rewardType.equals(RewardType.CANCEL_MESSAGE)) {
                                            cancelMessage = true;
                                        }
                                    }

                                    if (executePetRewards) {
                                        Pet petReceiver = new Pet(null, null, null, null, null, null, null);
                                        if (rewardReceiverType.equals(RewardReceiverType.TARGET)) {
                                            if (rewardArgsType.equals(RewardArgsType.ITEMSTACK)) {
                                                ItemStack itemStackReceiver = rewardArgs.getItemStack();
                                                Pet petReceiverFromArg = Pet.getPet(itemStackReceiver);
                                                if (petReceiverFromArg.isPet()) {
                                                    petReceiver = petReceiverFromArg;
                                                }
                                            }
                                        } else {
                                            petReceiver = pet;
                                        }

                                        if (petReceiver.isPet()) {
                                            if (rewardType.equals(RewardType.ADD_EXP)) {
                                                if (UtilString.get(reward).isNumeric()) {
                                                    double exp = Double.parseDouble(reward);
                                                    RewardMethods.addExp(player, petReceiver, exp);
                                                }
                                            } else if (rewardType.equals(RewardType.SET_EXP)) {
                                                if (UtilString.get(reward).isNumeric()) {
                                                    double exp = Double.parseDouble(reward);
                                                    RewardMethods.setExp(player, petReceiver, exp);
                                                }
                                            } else if (rewardType.equals(RewardType.ADD_LEVEL)) {
                                                if (UtilString.get(reward).isNumeric()) {
                                                    int levelReward = Integer.parseInt(reward);
                                                    RewardMethods.addLevel(player, petReceiver, levelReward);
                                                }
                                            } else if (rewardType.equals(RewardType.SET_LEVEL)) {
                                                if (UtilString.get(reward).isNumeric()) {
                                                    int levelReward = Integer.parseInt(reward);
                                                    RewardMethods.setLevel(player, petReceiver, levelReward);
                                                }
                                            } else if (rewardType.equals(RewardType.REMOVE_EXP)) {
                                                if (UtilString.get(reward).isNumeric()) {
                                                    double exp = Double.parseDouble(reward);
                                                    RewardMethods.removeExp(player, petReceiver, exp);
                                                }
                                            } else if (rewardType.equals(RewardType.REMOVE_LEVEL)) {
                                                if (UtilString.get(reward).isNumeric()) {
                                                    int levelReward = Integer.parseInt(reward);
                                                    RewardMethods.removeLevel(player, petReceiver, levelReward);
                                                }
                                            }
                                        }
                                    }

                                    if (executeVariableRewards) {
                                        Pet petReceiver = new Pet(null, null, null, null, null, null, null);
                                        if (rewardReceiverType.equals(RewardReceiverType.TARGET)) {
                                            if (rewardArgsType.equals(RewardArgsType.ITEMSTACK)) {
                                                ItemStack itemStackReceiver = rewardArgs.getItemStack();
                                                Pet petReceiverFromArg = Pet.getPet(itemStackReceiver);
                                                if (petReceiverFromArg.isPet()) {
                                                    petReceiver = petReceiverFromArg;
                                                }
                                            }
                                        } else {
                                            petReceiver = pet;
                                        }

                                        if (petReceiver.isPet()) {
                                            if (rewardType.equals(RewardType.SET_VARIABLE)) {
                                                VariableSeparator vs = new VariableSeparator(reward, true);
                                                VariableArg variable = vs.getVariable();
                                                if (variable.isVariable() && variable.isValue()) {
                                                    RewardMethods.setVariable(player, petReceiver, variable);
                                                }
                                            } else if (rewardType.equals(RewardType.REMOVE_VARIABLE)) {
                                                VariableSeparator vs = new VariableSeparator(reward, false);
                                                VariableArg variable = vs.getVariable();
                                                if (variable.isVariable()) {
                                                    RewardMethods.removeVariable(player, petReceiver, variable.getVariable());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
