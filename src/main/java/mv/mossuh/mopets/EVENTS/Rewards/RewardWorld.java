package mv.mossuh.mopets.EVENTS.Rewards;

import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mopets.UTILITIES.MoArgs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RewardWorld implements Listener {
    @EventHandler
    public void playerChangedWorldReward(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        String fromWorld = event.getFrom().getName();
        String toWorld = event.getPlayer().getWorld().getName();

        List<VariableArg> variables = new ArrayList<>();
        variables.add(new VariableArg("%to_world%", toWorld));
        variables.add(new VariableArg("%from_world%", fromWorld));

        EventType eventType = EventType.PLAYER_CHANGE_WORLD;
        int times = 1;
        MoArgs args = new MoArgs();
        RewardArgs rewardArgs = new RewardArgs(RewardArgsType.STRING, toWorld);
        args.setRewardArgs(rewardArgs);

        RewardExecutor executor = new RewardExecutor(player, event, eventType, args, variables, times);
        executor.execute();
    }
}
