package mv.mossuh.mopets.UTILITIES.Nbt;

import mv.mossuh.mocore.NBT.NBTMethods;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mopets.DATA.Config.Config;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NBTPet {
    public static final String UUIDKey = Config.PLUGIN_NAME+"UUID";
    public static final String CODEKey = Config.PLUGIN_NAME+"Code";
    public static final String LEVELKey = "MoItemLevel";
    public static final String EXPKey = "MoItemExp";
    public static final String COSTKey = "MoItemCost";
    public static final String VARIABLESKey = "MoItemVariables";
    public static final String ARGSKey = "MoItemArgs";
    public static final String UNIQUEKey = "MoItemUniqueID";
    public static final String TAGSKey = "MoItemTags";

    public static void setHeadTexture(ItemStack itemStack, String value) {
        NBTMethods.setHeadTexture(itemStack, value);
    }
    public static void setUUID(ItemStack itemStack) {
        NBTMethods.setString(itemStack, UUIDKey, UUID.randomUUID().toString());
    }
    public static void setCode(ItemStack itemStack, String code) {
        NBTMethods.setString(itemStack, CODEKey, code);
    }
    public static void setLevel(ItemStack itemStack, Integer level) {
        NBTMethods.setInteger(itemStack, LEVELKey, level);
    }
    public static void setExp(ItemStack itemStack, Double exp) {
        NBTMethods.setDouble(itemStack, EXPKey, exp);
    }
    public static void setCost(ItemStack itemStack, Double cost) {
        NBTMethods.setDouble(itemStack, COSTKey, cost);
    }

    public static void setVariables(ItemStack itemStack, List<VariableArg> variables) {
        NBTMethods.setString(itemStack, VARIABLESKey, VariableArg.toString(variables));
    }
    public static String getArgs(ItemStack itemStack) {
        return NBTMethods.getString(itemStack, ARGSKey);
    }
    public static void setArgs(ItemStack itemStack, String args) {
        NBTMethods.setString(itemStack, ARGSKey, args);
    }
    public static void setUnique(ItemStack itemStack) {
        NBTMethods.setString(itemStack, UNIQUEKey, UUID.randomUUID().toString());
    }
    public static void setTags(ItemStack itemStack, String tags) {
        NBTMethods.setString(itemStack, TAGSKey, tags);
    }


    public static UUID getUUID(ItemStack itemStack) {
        String uuidString = NBTMethods.getString(itemStack, UUIDKey);
        if (uuidString == null) { return null; }
        return UUID.fromString(uuidString);
    }
    public static String getCode(ItemStack itemStack) {
        return NBTMethods.getString(itemStack, CODEKey);
    }
    public static Integer getLevel(ItemStack itemStack) {
        return NBTMethods.getInteger(itemStack, LEVELKey);
    }
    public static Double getExp(ItemStack itemStack) {
        return NBTMethods.getDouble(itemStack, EXPKey);
    }
    public static Double getCost(ItemStack itemStack) {
        return NBTMethods.getDouble(itemStack, COSTKey);
    }
    public static List<VariableArg> getVariables(ItemStack itemStack) {
        String variablesAsString = NBTMethods.getString(itemStack, VARIABLESKey);
        if (variablesAsString != null) { return VariableArg.fromString(variablesAsString); }
        return new ArrayList<>();
    }
    public static Boolean isUnique(ItemStack itemStack) {
        String id = NBTMethods.getString(itemStack, UNIQUEKey);
        return id != null;
    }
    public static String getTags(ItemStack itemStack) {
        return NBTMethods.getString(itemStack, TAGSKey);
    }
}
