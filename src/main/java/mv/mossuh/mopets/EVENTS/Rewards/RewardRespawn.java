package mv.mossuh.mopets.EVENTS.Rewards;

import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import mv.mossuh.mopets.UTILITIES.MoArgs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RewardRespawn implements Listener {
    @EventHandler
    public void playerRespawnReward(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        EventType eventType = EventType.PLAYER_RESPAWN;
        int times = 1;

        RewardExecutor executor = new RewardExecutor(player, event, eventType, null, times);
        executor.execute();
    }
}
