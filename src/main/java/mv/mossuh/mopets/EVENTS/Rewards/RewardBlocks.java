package mv.mossuh.mopets.EVENTS.Rewards;

import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import mv.mossuh.mopets.UTILITIES.MoArgs;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class RewardBlocks implements Listener {

    @EventHandler
    public void blockBreakReward(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        int times = 1;
        EventType eventType = EventType.BLOCK_BREAK;
        MoArgs args = new MoArgs();
        RewardArgs rewardArgs = new RewardArgs(RewardArgsType.BLOCK, block);
        args.setRewardArgs(rewardArgs);

        RewardExecutor executor = new RewardExecutor(player, event, eventType, args, null, times);
        executor.execute();
        if (executor.isCancelledEvent()) { event.setCancelled(true); }
        if (executor.isCancelledDrops()) { event.setDropItems(false); }
    }

    @EventHandler
    public void blockPlaceReward(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        int times = 1;
        EventType eventType = EventType.BLOCK_PLACE;
        MoArgs args = new MoArgs();
        RewardArgs rewardArgs = new RewardArgs(RewardArgsType.BLOCK, block);
        args.setRewardArgs(rewardArgs);

        RewardExecutor executor = new RewardExecutor(player, event, eventType, args, null, times);
        executor.execute();
        if (executor.isCancelledEvent()) { event.setCancelled(true); }
    }
}
