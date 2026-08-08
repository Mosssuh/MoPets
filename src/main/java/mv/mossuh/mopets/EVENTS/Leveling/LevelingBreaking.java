package mv.mossuh.mopets.EVENTS.Leveling;

import mv.mossuh.mocore.VERSION.ServerVersion;
import mv.mossuh.mopets.ENUMS.ExpType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class LevelingBreaking implements Listener {

    @EventHandler
    public void blockBreakReward(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        String getType = String.valueOf(block.getType());
        short getData = !ServerVersion.isAtLeast(ServerVersion.MC1_13) ? block.getData() : -1;

        ExpType expType = ExpType.BLOCK_BREAK;
        LevelingExecutor executor = new LevelingExecutor(player, expType, getType, getData, null);
        executor.execute();
    }
}
