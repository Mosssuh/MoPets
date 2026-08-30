package mv.mossuh.mopets.EVENTS.Leveling;

import mv.mossuh.mocore.VERSION.ServerVersion;
import mv.mossuh.mopets.UTILITIES.Enums.ExpType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class LevelingFishing implements Listener {
    @EventHandler
    public void fishingReward(PlayerFishEvent event) {
        Player player = event.getPlayer();
        Entity caught = event.getCaught();

        if (!(caught instanceof Item)) return;
        Item item = (Item) caught;

        String getType = String.valueOf(item.getItemStack().getType());
        short getData = !ServerVersion.isAtLeast(ServerVersion.MC1_13) ? item.getItemStack().getData().getData() : -1;

        ExpType expType = ExpType.PLAYER_FISH;
        LevelingExecutor executor = new LevelingExecutor(player, expType, getType, getData, null);
        executor.execute();
    }
}
