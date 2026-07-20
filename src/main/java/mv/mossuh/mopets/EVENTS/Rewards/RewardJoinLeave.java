package mv.mossuh.mopets.EVENTS.Rewards;

import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import mv.mossuh.mopets.UTILITIES.MoArgs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class RewardJoinLeave implements Listener {
    @EventHandler
    public void playerJoinReward(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        EventType eventType = EventType.PLAYER_JOIN;
        int times = 1;
        MoArgs args = new MoArgs();
        RewardArgs rewardArgs = new RewardArgs(RewardArgsType.NONE);
        args.setRewardArgs(rewardArgs);

        RewardExecutor executor = new RewardExecutor(player, event, eventType, args, null, times);
        executor.execute();
    }

    @EventHandler
    public void playerLeaveReward(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        EventType eventType = EventType.PLAYER_LEAVE;
        int times = 1;
        MoArgs args = new MoArgs();
        RewardArgs rewardArgs = new RewardArgs(RewardArgsType.NONE);
        args.setRewardArgs(rewardArgs);

        RewardExecutor executor = new RewardExecutor(player, event, eventType, args, null, times);
        executor.execute();
    }
}
