package mv.mossuh.mopets.EVENTS.Rewards.Default;

import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import mv.mossuh.mopets.ACTIONS.ActionResult;
import mv.mossuh.mopets.ACTIONS.Requirements;
import mv.mossuh.mopets.ACTIONS.Rewards;
import mv.mossuh.mopets.API.PetsAPI;
import mv.mossuh.mopets.MODEL.Pets.Actions.DefaultActions;
import mv.mossuh.mopets.MODEL.Pets.Pet;
import mv.mossuh.mopets.MODEL.Player.PetsPlayer;
import mv.mossuh.mopets.MODEL.MoArgs;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.*;

public class RewardExecutor {
    private boolean cancelEvent = false;
    private boolean cancelDrops = false;
    private boolean cancelMessage = false;



    private Player player = null;
    private Event event = null;
    private EventType eventType = EventType.INVALID;
    private MoArgs args = new MoArgs();
    private int times = 1;
    public RewardExecutor(Player player, Event event, EventType eventType, MoArgs args, Integer times) {
        this.player = player;
        this.event = event;
        if (eventType != null) { this.eventType = eventType; }
        if (args != null) { this.args = args; }
        if (times != null) { this.times = times; }
    }

    public void execute() {
        UUID uuid = player.getUniqueId();
        RewardArgs rewardArgs = args.getRewardArgs();
        RewardArgsType rewardArgsType = rewardArgs.getArgumentType();

        PetsPlayer petsPlayer = PetsAPI.getManager().getPlayer(uuid);
        if (petsPlayer.isPlayer() && petsPlayer.hasPets()) {
            List<Pet> pets = petsPlayer.getPets();

            for (Pet pet : pets) {
                DefaultActions defaultActions = pet.getConfigPet().getActions().getDefaultActions();
                if (pet.isPet() && defaultActions.hasEvent(eventType)) {
                    Requirements requirements = new Requirements(event, eventType, player, pet, args)
                            .addPlayerVariables().addPetVariables();

                    switch (rewardArgsType) {
                        case ITEMSTACK:
                            requirements.addItemStackVariables();
                            break;
                        case BLOCK:
                            requirements.addBlockVariables();
                            break;
                        case ENTITY:
                            requirements.addEntityVariables();
                            break;
                        case LIVING_ENTITY:
                            requirements.addLivingEntityVariables();
                            break;
                    }

                    requirements.check();
                    ActionResult result = requirements.getActionResult();
                    if (result.hasApprovedRewards()) {
                        Rewards rewards = new Rewards(result, times).executeDefault().executePets().executeVariables().check();
                        if (!this.cancelDrops && rewards.cancelDrops()) {
                            this.cancelDrops = true;
                        }
                        if (!this.cancelEvent && rewards.cancelEvent()) {
                            this.cancelEvent = true;
                        }
                        if (!this.cancelMessage && rewards.cancelMessage()) {
                            this.cancelMessage = true;
                        }
                    }
                }
            }
        }
    }


    public boolean isCancelledDrops() { return cancelDrops; }
    public boolean isCancelledEvent() { return cancelEvent; }
    public boolean isCancelledMessage() { return cancelMessage; }
}
