package mv.mossuh.mopets.EVENTS.Rewards.Default;

import mv.mossuh.mocore.ENUMS.EventType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class RewardJoinLeave implements Listener {
    @EventHandler
    public void playerJoinReward(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        EventType eventType = EventType.PLAYER_JOIN;
        int times = 1;

        RewardExecutor executor = new RewardExecutor(player, event, eventType, null, times);
        executor.execute();
    }

    @EventHandler
    public void playerLeaveReward(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        EventType eventType = EventType.PLAYER_LEAVE;
        int times = 1;

        RewardExecutor executor = new RewardExecutor(player, event, eventType, null, times);
        executor.execute();
    }
}
