package mv.mossuh.mopets.EVENTS.Rewards.Default;

import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mopets.MODEL.MoArgs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class RewardWorld implements Listener {
    @EventHandler
    public void playerChangedWorldReward(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        String fromWorld = event.getFrom().getName();
        String toWorld = event.getPlayer().getWorld().getName();

        EventType eventType = EventType.PLAYER_CHANGE_WORLD;
        int times = 1;
        MoArgs args = new MoArgs();
        args.addVariableArg(
                new VariableArg("%to_world%", toWorld),
                new VariableArg("%from_world%", fromWorld)
        );

        RewardExecutor executor = new RewardExecutor(player, event, eventType, args, times);
        executor.execute();
    }
}
