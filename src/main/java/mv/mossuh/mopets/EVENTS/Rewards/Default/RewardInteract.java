package mv.mossuh.mopets.EVENTS.Rewards.Default;

import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mopets.MODEL.MoArgs;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class RewardInteract implements Listener {
    @EventHandler
    public void playerInteractBlockReward(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Block block = event.getClickedBlock();
        Action action = event.getAction();

        if (block != null && block.getType() != Material.AIR) {
            String clickType = "NONE";
            if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
                clickType = "RIGHT";
            } else if (action.equals(Action.LEFT_CLICK_BLOCK)) {
                clickType = "LEFT";
            }

            EventType eventType = EventType.BLOCK_INTERACT;
            int times = 1;

            MoArgs args = new MoArgs();
            RewardArgs rewardArgs = new RewardArgs(RewardArgsType.BLOCK, block);
            args.setRewardArgs(rewardArgs);
            args.addVariableArg(
                    new VariableArg("%click_type%", clickType)
            );

            RewardExecutor executor = new RewardExecutor(player, event, eventType, args, times);
            executor.execute();
            if (executor.isCancelledEvent()) { event.setCancelled(true); }
        }
    }

    @EventHandler
    public void playerInteractItemReward(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        ItemStack itemStack = event.getItem();
        Action action = event.getAction();

        if (itemStack != null && itemStack.getType() != Material.AIR) {
            String clickType = "NONE";
            if (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR)) {
                clickType = "RIGHT";
            } else if (action.equals(Action.LEFT_CLICK_BLOCK) || action.equals(Action.LEFT_CLICK_AIR)) {
                clickType = "LEFT";
            }

            EventType eventType = EventType.ITEM_INTERACT;
            int times = 1;
            MoArgs args = new MoArgs();
            RewardArgs rewardArgs = new RewardArgs(RewardArgsType.ITEMSTACK, itemStack);
            args.setRewardArgs(rewardArgs);
            args.addVariableArg(
                    new VariableArg("%click_type%", clickType)
            );

            RewardExecutor executor = new RewardExecutor(player, event, eventType, args, times);
            executor.execute();
            if (executor.isCancelledEvent()) { event.setCancelled(true); }
        }
    }



    @EventHandler
    public void playerInteractEntityReward(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if(!Bukkit.getVersion().contains("1.8") && !event.getHand().equals(EquipmentSlot.HAND)) {
            return;
        }

        if (entity == null) {
            return;
        }

        EventType eventType = EventType.ENTITY_INTERACT;
        int times = 1;
        MoArgs args = new MoArgs();
        RewardArgs rewardArgs = new RewardArgs(RewardArgsType.ENTITY, entity);
        args.setRewardArgs(rewardArgs);

        RewardExecutor executor = new RewardExecutor(player, event, eventType, args, times);
        executor.execute();
        if (executor.isCancelledEvent()) { event.setCancelled(true); }
    }
}
