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
import mv.mossuh.mopets.CONFIGS.Config.Config;
import mv.mossuh.mopets.CONFIGS.Pets.Pet.ConfigPet;
import mv.mossuh.mopets.PETS.Pet.Pet;
import mv.mossuh.mopets.UTILITIES.MoArgs;
import mv.mossuh.mopets.UTILITIES.UtilString;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ExecuteAction {
    private MoAction moAction = new MoAction(null, null, null, null, null, null, null);
    private Event event;
    private EventType eventType = EventType.NONE;
    private Player player;
    private Pet pet = new Pet(null, null, null, null, null, null, null);
    private MoArgs args = new MoArgs();
    private List<VariableArg> variables = new ArrayList<>();
    private ActionResult actionResult = new ActionResult(null, null, null, null, null, null, null);


    private boolean cancelEvent = false;
    private boolean cancelMessage = false;
    private boolean cancelDrops = false;
    public ExecuteAction(MoAction moAction, Event event, EventType eventType, Player player, Pet pet, MoArgs args) {
        if (moAction != null) { this.moAction = moAction; }
        this.event = event;
        if (eventType != null) { this.eventType = eventType; }
        this.player = player;
        if (pet != null) { this.pet = pet; }
        if (args != null) { this.args = args; }
    }

    public boolean cancelDrops() { return cancelDrops; }

    public boolean cancelEvent() { return cancelEvent; }
    public boolean cancelMessage() { return cancelMessage; }

    public ExecuteAction addVariables(VariableArg... variables) {
        this.variables.addAll(Arrays.asList(variables));
        return this;
    }

    public ExecuteAction addVariables(List<VariableArg> variables) {
        this.variables.addAll(variables);
        return this;
    }

    public ExecuteAction addAllDefaultVariables() {
        addDefaultPlayerVariables();
        addDefaultPetVariables();
        addDefaultItemStackVariables();
        addDefaultBlockVariables();
        addDefaultEntityVariables();
        addDefaultLivingEntityVariables();
        return this;
    }

    public ExecuteAction addDefaultPlayerVariables() {
        this.variables.addAll(DefaultVariables.player(player));
        return this;
    }

    public ExecuteAction addDefaultItemStackVariables() {
        RewardArgs rewardArgs = this.args.getRewardArgs();
        if (rewardArgs.getArgumentType().equals(RewardArgsType.ITEMSTACK)) {
            ItemStack itemStack = rewardArgs.getItemStack();
            this.variables.addAll(DefaultVariables.itemStack(itemStack));
        }
        return this;
    }

    public ExecuteAction addDefaultBlockVariables() {
        RewardArgs rewardArgs = this.args.getRewardArgs();
        if (rewardArgs.getArgumentType().equals(RewardArgsType.BLOCK)) {
            Block block = rewardArgs.getBlock();
            this.variables.addAll(DefaultVariables.block(block));
        }
        return this;
    }

    public ExecuteAction addDefaultPetVariables() {
        this.variables.addAll(DefaultVariables.pet(pet));
        return this;
    }

    public ExecuteAction addDefaultEntityVariables() {
        RewardArgs rewardArgs = this.args.getRewardArgs();

        if (rewardArgs.getArgumentType().equals(RewardArgsType.ENTITY)) {
            Entity entity = rewardArgs.getEntity();
            this.variables.addAll(DefaultVariables.entity(entity));
        }
        return this;
    }

    public ExecuteAction addDefaultLivingEntityVariables() {
        RewardArgs rewardArgs = this.args.getRewardArgs();
        if (rewardArgs.getArgumentType().equals(RewardArgsType.LIVING_ENTITY)) {
            LivingEntity entity = rewardArgs.getLivingEntity();
            this.variables.addAll(DefaultVariables.livingEntity(entity));
        }
        return this;
    }

    public ExecuteAction addCommandArgsVariables() {
        CommandArgs commandArgs = this.args.getCommandArgs();
        if (this.args.getCommandArgs().hasArgs()) {
            int size = commandArgs.getArgs().size();
            for (int i = 0; i < size; i++) {
                this.variables.add(new VariableArg("%args_" + (i+1)  + "%", commandArgs.getArg(i)));
            }
        }
        return this;
    }

    public ExecuteAction check() {
        ConfigPet configPet = pet.getConfigPet();
        UUID petUUID = pet.getPetUUID();
        UUID uuid = player.getUniqueId();

        MoRequirements requirements = moAction.getRequirements();
        List<MoRequirement> requirementList = requirements.getRequirements();
        int requirementsAmount = requirementList.size();
        int requirementsAccepted = 0;

        String actionName = moAction.getActionName();
        MoCooldown cooldown = moAction.getCooldown();

        EventType requirementEventType = moAction.getEventType();
        if (requirementEventType.equals(eventType) || requirementEventType.equals(EventType.NONE)) {

            List<VariableArg> actionVariables = new ArrayList<>();
            String cooldownCode = Config.PLUGIN_NAME+"::"+petUUID+"::"+actionName;
            long cooldownInSeconds = cooldown.getCooldown();
            actionVariables.add(new VariableArg("%cooldown%", Cooldown.showCooldownInSeconds(cooldownCode, cooldownInSeconds)));
            actionVariables.add(new VariableArg("%cooldown_formatted%", Cooldown.showCooldownFormatted(cooldownCode, cooldownInSeconds, Config.TIME_FORMAT)));

            if (!requirementList.isEmpty()) {
                for (MoRequirement moRequirement : requirementList) {
                    if (moRequirement.isRequirement(RequirementType.EVENT)) {
                        RequirementEvent requirementEvent = (RequirementEvent) moRequirement.getRequirement();
                        List<EntityRequirement> entityRequirementList = requirementEvent.getRequirements();

                        if (requirementEventType.hasEntity()) {
                            if (requirementEvent.hasRequirements()) {
                                String entity = VariableArg.getValue(variables, "%event_entity%");
                                String data = VariableArg.getValue(variables, "%event_data%");
                                if (EntityRequirement.containsEntity(entityRequirementList, entity, data)) {
                                    requirementsAccepted = requirementsAccepted + 1;
                                }
                            } else {
                                requirementsAccepted = requirementsAccepted + 1;
                            }
                        } else {
                            if (requirementEventType.equals(eventType)) {
                                requirementsAccepted = requirementsAccepted + 1;
                            }
                        }
                    } else if (moRequirement.isRequirement(RequirementType.EVAL)) {
                        RequirementEval requirement = (RequirementEval) moRequirement.getRequirement();
                        for (String eval : requirement.getRequirements()) {
                            boolean condition = UtilString.get(eval).setVariables(variables).setVariables(actionVariables)
                                    .setPlaceholders(uuid).setTimeFormatter().hex().evaluateString();
                            if (condition) {
                                requirementsAccepted = requirementsAccepted + 1;
                                break;
                            }
                        }
                    }
                }
            }

            if (cooldown.isCooldown()) {
                if (Cooldown.startAndIsOnCooldown(cooldownCode, cooldownInSeconds)) {
                    if (!cooldown.isByPass()) {
                        UtilString.get(cooldown.getMessage()).setVariables(variables).setVariables(actionVariables)
                                .setPlaceholders(uuid).setTimeFormatter().hex().sendMessage(player);
                        return this;
                    }
                }
            }

            if (requirementsAccepted == requirementsAmount) {
                MoRewards vRewards = new MoRewards(moAction.getRewards().getRewards(), null);
                vRewards.addVariables(actionVariables);
                actionResult = new ActionResult(event, eventType, player, pet, args, variables, new ArrayList<>(Collections.singletonList(vRewards)));
                Rewards rewards = new Rewards(actionResult).executeDefault().executePets().executeVariables().check();
                if (rewards.cancelEvent()) { this.cancelEvent = true; }
                if (rewards.cancelDrops()) { this.cancelDrops = true; }
                if (rewards.cancelMessage()) { this.cancelMessage = true; }
            } else {
                MoRewards vRewards = new MoRewards(moAction.getElseRewards().getRewards(), null);
                vRewards.addVariables(actionVariables);
                actionResult = new ActionResult(event, eventType, player, pet, args, variables, new ArrayList<>(Collections.singletonList(vRewards)));
                Rewards rewards = new Rewards(actionResult).executeDefault().executePets().executeVariables().check();
                if (rewards.cancelEvent()) { this.cancelEvent = true; }
                if (rewards.cancelDrops()) { this.cancelDrops = true; }
                if (rewards.cancelMessage()) { this.cancelMessage = true; }
            }
        }
        return this;
    }

    public ActionResult getActionResult() { return actionResult; }
}
