package mv.mossuh.mopets.EVENTS.Rewards.Default;

import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.EVENTS.ArmorEquipEvent.ArmorEquipEvent;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mopets.MODEL.MoArgs;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class RewardEquipUnEquipArmor implements Listener {
    @EventHandler
    public void playerEquipUnEquipPieceReward(ArmorEquipEvent event) {
        Player player = event.getPlayer();
        ItemStack newPiece = event.getNewArmorPiece();
        EventType eventType = EventType.PLAYER_EQUIP_PIECE;
        String equipType = "EQUIP";
        ItemStack newItem = event.getNewArmorPiece();
        ItemStack oldItem = event.getOldArmorPiece();

        int times = 1;
        ItemStack selectedItem = null;
        if(newItem == null || newItem.getType().name().equals("AIR")) {
            eventType = EventType.PLAYER_UNEQUIP_PIECE;
            equipType = "UNEQUIP";
            selectedItem = oldItem;
        } else {
            selectedItem = newItem;
        }

        String armorType = "";
        if (event.getType() != null) {
            armorType = event.getType().name();
        }


        if (newPiece != null && !newPiece.getType().equals(Material.AIR)) {
            MoArgs args = new MoArgs();
            RewardArgs rewardArgs = new RewardArgs(RewardArgsType.ITEMSTACK, selectedItem);
            args.setRewardArgs(rewardArgs);

            args.addVariableArg(
                    new VariableArg("%piece_type%", armorType),
                    new VariableArg("%equip_type%", equipType)
            );

            RewardExecutor executor = new RewardExecutor(player, event, eventType, args, times);
            executor.execute();
            if (executor.isCancelledEvent()) { event.setCancelled(true); }
        }
    }
}
