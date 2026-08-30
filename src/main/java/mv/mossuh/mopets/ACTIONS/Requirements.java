package mv.mossuh.mopets.ACTIONS;

import mv.mossuh.mocore.ACTIONS.ActionUtil.MoAction;
import mv.mossuh.mocore.ACTIONS.OtherUtil.MoCooldown;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.MoRequirement;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.MoRequirements;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.RequirementEval;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.RequirementEvent;
import mv.mossuh.mocore.ACTIONS.RewardUtil.MoRewards;
import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.ENUMS.RequirementType;
import mv.mossuh.mocore.UTILITIES.ARGS.CommandArgs.CommandArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.Cooldown;
import mv.mossuh.mocore.UTILITIES.REQUIREMENTS.EntityRequirement;
import mv.mossuh.mopets.UTILITIES.DefaultVariables;
import mv.mossuh.mopets.DATA.Config.Config;
import mv.mossuh.mopets.MODEL.Pets.Config.ConfigPet;
import mv.mossuh.mopets.MODEL.Pets.Pet;
import mv.mossuh.mopets.MODEL.MoArgs;
import mv.mossuh.mopets.UTILITIES.UtilString;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Requirements {
    private List<MoRewards> approvedRewards = new ArrayList<>();

    private Event event;
    private EventType eventType = EventType.NONE;
    private Player player;
    private Pet pet = new Pet();
    private MoArgs args = new MoArgs();
    private ActionResult actionResult = new ActionResult();

    public Requirements(Event event, EventType eventType, Player player, Pet pet, MoArgs args) {
        this.event = event;
        if (eventType != null) { this.eventType = eventType; }
        this.player = player;
        if (pet != null) { this.pet = pet; }
        if (args != null) { this.args = args; }
    }

    public Requirements addPlayerVariables() {
        this.args.getVariableArgs().addAll(DefaultVariables.player(player));
        return this;
    }

    public Requirements addItemStackVariables() {
        RewardArgs rewardArgs = this.args.getRewardArgs();
        if (rewardArgs.getArgumentType().equals(RewardArgsType.ITEMSTACK)) {
            ItemStack itemStack = rewardArgs.getItemStack();
            this.args.getVariableArgs().addAll(DefaultVariables.itemStack(itemStack));
        }
        return this;
    }

    public Requirements addBlockVariables() {
        RewardArgs rewardArgs = this.args.getRewardArgs();
        if (rewardArgs.getArgumentType().equals(RewardArgsType.BLOCK)) {
            Block block = rewardArgs.getBlock();
            this.args.getVariableArgs().addAll(DefaultVariables.block(block));
        }
        return this;
    }

    public Requirements addPetVariables() {
        this.args.getVariableArgs().addAll(DefaultVariables.pet(pet));
        return this;
    }

    public Requirements addEntityVariables() {
        RewardArgs rewardArgs = this.args.getRewardArgs();
        if (rewardArgs.getArgumentType().equals(RewardArgsType.ENTITY)) {
            Entity entity = rewardArgs.getEntity();
            this.args.getVariableArgs().addAll(DefaultVariables.entity(entity));
        }
        return this;
    }

    public Requirements addLivingEntityVariables() {
        RewardArgs rewardArgs = this.args.getRewardArgs();
        if (rewardArgs.getArgumentType().equals(RewardArgsType.LIVING_ENTITY)) {
            LivingEntity entity = rewardArgs.getLivingEntity();
            this.args.getVariableArgs().addAll(DefaultVariables.livingEntity(entity));
        }
        return this;
    }

    public Requirements addCommandArgsVariables() {
        CommandArgs commandArgs = this.args.getCommandArgs();
        if (this.args.getCommandArgs().hasArgs()) {
            int size = commandArgs.getArgs().size();
            for (int i = 0; i < size; i++) {
                this.args.getVariableArgs().add(new VariableArg("%args_" + (i+1)  + "%", commandArgs.getArg(i)));
            }
        }
        return this;
    }

    public Requirements check() {
        ConfigPet configPet = pet.getConfigPet();
        List<MoAction> vActionList = configPet.getActions().getDefaultActions().getActions();
        List<VariableArg> variables = args.getVariableArgs();

        UUID uuid = player.getUniqueId();
        UUID petUUID = pet.getPetUUID();

        if (!vActionList.isEmpty()) {
            for (MoAction vAction : vActionList) {
                if (!vAction.isCancelled()) {
                    String actionName = vAction.getActionName();
                    MoCooldown cooldown = vAction.getCooldown();
                    EventType requirementEventType = vAction.getEventType();
                    // if (requirementEventType.equals(eventType) || requirementEventType.equals(EventType.NONE)) {
                    if (requirementEventType == eventType) {
                        MoRequirements requirements = vAction.getRequirements();
                        List<MoRequirement> requirementsList = requirements.getRequirements();

                        List<VariableArg> actionVariables = new ArrayList<>();
                        String cooldownCode = Config.PLUGIN_NAME+"::"+petUUID+"::"+actionName;
                        long cooldownInSeconds = cooldown.getCooldown();

                        if (cooldown.isCooldown()) {
                            if (Cooldown.startAndIsOnCooldown(cooldownCode, cooldownInSeconds)) {
                                if (!cooldown.isByPass()) {
                                    UtilString.get(cooldown.getMessage()).setVariables(variables).setVariables(actionVariables).setPlaceholders(uuid).setTimeFormatter().hex().sendMessage(player);
                                    continue;
                                }
                            }
                        }

                        actionVariables.add(new VariableArg("%cooldown%", Cooldown.showCooldownInSeconds(cooldownCode, cooldownInSeconds)));
                        actionVariables.add(new VariableArg("%cooldown_formatted%", Cooldown.showCooldownFormatted(cooldownCode, cooldownInSeconds, Config.TIME_FORMAT)));

                        boolean isAccepted = true;
                        if (!requirementsList.isEmpty()) {
                            for (MoRequirement moRequirement : requirementsList) {
                                boolean requirementAccepted = false;

                                if (moRequirement.isRequirement(RequirementType.EVENT)) {
                                    RequirementEvent requirementEvent = (RequirementEvent) moRequirement.getRequirement();
                                    List<EntityRequirement> entityRequirementList = requirementEvent.getRequirements();

                                    if (requirementEventType.hasEntity()) {
                                        if (requirementEvent.hasRequirements()) {
                                            String entity = VariableArg.getValue(variables, "%event_entity%");
                                            String data = VariableArg.getValue(variables, "%event_data%");
                                            requirementAccepted = EntityRequirement.containsEntity(entityRequirementList, entity, data);
                                        } else {
                                            requirementAccepted = true;
                                        }
                                    } else {
                                        requirementAccepted = requirementEventType == eventType;
                                    }
                                } else if (moRequirement.isRequirement(RequirementType.EVAL)) {
                                    RequirementEval requirement = (RequirementEval) moRequirement.getRequirement();
                                    for (String eval : requirement.getRequirements()) {
                                        boolean condition = UtilString.get(eval).setVariables(variables).setVariables(actionVariables)
                                                .setPlaceholders(uuid).setTimeFormatter().evaluateString();

                                        if (condition) {
                                            requirementAccepted = true;
                                            break;
                                        }
                                    }
                                }

                                if (!requirementAccepted) {
                                    isAccepted = false;
                                    break;
                                }
                            }
                        }

                        MoRewards vRewards;
                        if (isAccepted) {
                            vRewards = new MoRewards(vAction.getRewards().getRewards(), null);
                        } else {
                            vRewards = new MoRewards(vAction.getElseRewards().getRewards(), null);
                        }
                        vRewards.addVariables(actionVariables);
                        approvedRewards.add(vRewards);
                    }
                }
            }
        }
        actionResult = new ActionResult(event, eventType, player, pet, args, approvedRewards);
        return this;
    }

    public ActionResult getActionResult() { return actionResult; }
}
