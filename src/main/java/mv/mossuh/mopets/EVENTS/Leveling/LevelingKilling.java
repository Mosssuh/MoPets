package mv.mossuh.mopets.EVENTS.Leveling;

import mv.mossuh.mopets.ENUMS.ExpType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class LevelingKilling implements Listener {
    @EventHandler
    public void playerKillsReward(EntityDeathEvent event) {
        Player player = event.getEntity().getKiller();
        LivingEntity dead = event.getEntity();

        if (player != null) {
            String getType = String.valueOf(dead.getType());
            short getData = -1;

            ExpType expType = ExpType.PLAYER_KILLS;
            LevelingExecutor executor = new LevelingExecutor(player, expType, getType, getData, null);
            executor.execute();
        }
    }
}
