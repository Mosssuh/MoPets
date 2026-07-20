package mv.mossuh.mopets.EVENTS.Rewards;

import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import mv.mossuh.mopets.UTILITIES.MoArgs;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class RewardFishing implements Listener {
    @EventHandler
    public void caughtFishReward(PlayerFishEvent event) {
        Player player = event.getPlayer();
        Entity caught = event.getCaught();
        PlayerFishEvent.State state = event.getState();
        EventType eventType = EventType.NONE;
        if (state.equals(PlayerFishEvent.State.CAUGHT_FISH)) {
            eventType = EventType.PLAYER_CAUGHT_FISH;
        } else {
            eventType = EventType.PLAYER_CAUGHT_ENTITY;
        }

        int times = 1;
        MoArgs args = new MoArgs();
        RewardArgs rewardArgs = new RewardArgs(RewardArgsType.ENTITY, caught);
        args.setRewardArgs(rewardArgs);

        RewardExecutor executor = new RewardExecutor(player, event, eventType, args, null, times);
        executor.execute();
        if (executor.isCancelledEvent()) { event.setCancelled(true); }
    }
}
