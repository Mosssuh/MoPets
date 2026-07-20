package mv.mossuh.mopets.EVENTS.Leveling;

import mv.mossuh.mopets.ENUMS.ExpType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class LevelingPlacing implements Listener {
    @EventHandler
    public void blockPlaceReward(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        String getType = String.valueOf(block.getType());
        short getData = block.getData();

        ExpType expType = ExpType.BLOCK_PLACE;
        LevelingExecutor executor = new LevelingExecutor(player, expType, getType, getData, null);
        executor.execute();
    }
}
