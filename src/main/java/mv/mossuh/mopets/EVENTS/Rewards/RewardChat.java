package mv.mossuh.mopets.EVENTS.Rewards;

import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mopets.UTILITIES.MoArgs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.List;

public class RewardChat implements Listener {
    @EventHandler
    public void playerCommandReward(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage();

        int times = 1;
        EventType eventType = EventType.PLAYER_COMMAND;
        MoArgs args = new MoArgs();
        args.addVariableArg(
                new VariableArg("%command%", command)
        );

        RewardExecutor executor = new RewardExecutor(player, event, eventType, args, times);
        executor.execute();
        if (executor.isCancelledEvent()) { event.setCancelled(true); }
    }

    @EventHandler
    public void playerChatReward(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        int times = 1;
        EventType eventType = EventType.PLAYER_CHAT;
        MoArgs args = new MoArgs();
        args.addVariableArg(
                new VariableArg("%message%", message)
        );

        RewardExecutor executor = new RewardExecutor(player, event, eventType, args, times);
        executor.execute();
        if (executor.isCancelledEvent()) { event.setCancelled(true); }
    }
}
