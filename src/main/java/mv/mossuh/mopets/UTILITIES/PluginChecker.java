package mv.mossuh.mopets.UTILITIES;

import mv.mossuh.mocore.ENUMS.PluginType;
import mv.mossuh.mocore.EVENTS.PluginCheckerEvent;
import mv.mossuh.mopets.CONFIGS.Config.Config;
import mv.mossuh.mopets.EVENTS.MoBoosters.MoBoostersReward;
import mv.mossuh.mopets.MoPets;
import mv.mossuh.mopets.PAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PluginChecker implements Listener {
    private MoPets instance = MoPets.getInstance();

    @EventHandler
    public void onPluginChecker(PluginCheckerEvent event) {
        PluginType pluginType = event.getPluginType();

        switch (pluginType) {
            case PlaceholderAPI:
                new PAPI().register();
                UtilString.get("&8[" + Config.PREFIX + "&8] &aDetected PlaceholderAPI, used as soft-depend.").hex().sendMessageInConsole();
                break;
            case MoBoosters:
                if (Config.ACTIONS) {
                    instance.getServer().getPluginManager().registerEvents(new MoBoostersReward(), instance);
                }
                UtilString.get("&8[" + Config.PREFIX + "&8] &aDetected MoBoosters, used as soft-depend. Enabling classes.").hex().sendMessageInConsole();
                break;
        }
    }
}
