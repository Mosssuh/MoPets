package mv.mossuh.mopets.ACTIONS;

import mv.mossuh.mocore.ACTIONS.RewardUtil.MoRewards;
import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mopets.PETS.Pet.Pet;
import mv.mossuh.mopets.UTILITIES.MoArgs;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public class ActionResult {
    private Event event;
    private EventType eventType = EventType.NONE;
    private Player player;
    private Pet pet = new Pet(null, null, null, null, null, null, null);
    private MoArgs args = new MoArgs();
    private List<VariableArg> variables = new ArrayList<>();
    private List<MoRewards> approvedRewards = new ArrayList<>();
    private boolean hasRewards = false;
    public ActionResult(Event event, EventType eventType, Player player, Pet pet, MoArgs args, List<VariableArg> variables, List<MoRewards> approvedRewards) {
        this.event = event;
        if (eventType != null) { this.eventType = eventType; }
        this.player = player;
        if (pet != null) { this.pet = pet; }
        if (args != null) { this.args = args; }
        if (variables != null) { this.variables = variables; }
        if (approvedRewards != null) { this.approvedRewards = approvedRewards; }
        if (approvedRewards != null && !approvedRewards.isEmpty()) { this.hasRewards = true; }
    }

    public Event getEvent() { return event; }
    public EventType getEventType() { return eventType; }
    public Player getPlayer() { return player; }
    public Pet getPet() { return pet; }
    public MoArgs getArgs() { return args; }
    public List<VariableArg> getVariables() { return variables; }
    public List<MoRewards> getApprovedRewards() { return approvedRewards; }
    public boolean hasApprovedRewards() { return hasRewards; }
}
