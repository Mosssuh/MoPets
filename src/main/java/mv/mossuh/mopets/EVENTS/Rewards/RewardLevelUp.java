package mv.mossuh.mopets.EVENTS.Rewards;

import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mopets.UTILITIES.MoArgs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

import java.util.ArrayList;
import java.util.List;

public class RewardLevelUp implements Listener {

    @EventHandler
    public void levelUpReward(PlayerLevelChangeEvent event) {
        Player player = event.getPlayer();
        int newLevel = event.getNewLevel();
        int oldLevel = event.getOldLevel();

        List<VariableArg> variables = new ArrayList<>();
        variables.add(new VariableArg("%old_level%", oldLevel+""));
        variables.add(new VariableArg("%new_level%", newLevel+""));

        EventType eventType = EventType.PLAYER_LEVELUP;
        int times = 1;
        MoArgs args = new MoArgs();
        RewardArgs rewardArgs = new RewardArgs(RewardArgsType.NONE);
        args.setRewardArgs(rewardArgs);

        RewardExecutor executor = new RewardExecutor(player, event, eventType, args, variables, times);
        executor.execute();
    }
}
