package mv.mossuh.mopets.EVENTS.Leveling;

import mv.mossuh.mopets.ENUMS.ExpType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class LevelingFishing implements Listener {
    @EventHandler
    public void fishingReward(PlayerFishEvent event) {
        Player player = event.getPlayer();
        Entity caught = event.getCaught();

        String getType = String.valueOf(((org.bukkit.entity.Item) caught).getItemStack().getType());
        short getData = ((org.bukkit.entity.Item) caught).getItemStack().getDurability();

        ExpType expType = ExpType.PLAYER_FISH;
        LevelingExecutor executor = new LevelingExecutor(player, expType, getType, getData, null);
        executor.execute();
    }
}
