package mv.mossuh.mopets.EVENTS.Rewards;

import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import mv.mossuh.mopets.UTILITIES.MoArgs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

public class RewardBed implements Listener {
    @EventHandler
    public void bedEnterReward(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();

        int times = 1;
        EventType eventType = EventType.PLAYER_BED_ENTER;
        MoArgs args = new MoArgs();
        RewardArgs rewardArgs = new RewardArgs(RewardArgsType.NONE);
        args.setRewardArgs(rewardArgs);

        RewardExecutor executor = new RewardExecutor(player, event, eventType, args, null, times);
        executor.execute();
        if (executor.isCancelledEvent()) { event.setCancelled(true); }
    }

    @EventHandler
    public void bedLeaveReward(PlayerBedLeaveEvent event) {
        Player player = event.getPlayer();

        int times = 1;
        EventType eventType = EventType.PLAYER_BED_LEAVE;
        MoArgs args = new MoArgs();
        RewardArgs rewardArgs = new RewardArgs(RewardArgsType.NONE);
        args.setRewardArgs(rewardArgs);

        RewardExecutor executor = new RewardExecutor(player, event, eventType, args, null, times);
        executor.execute();
    }
}
