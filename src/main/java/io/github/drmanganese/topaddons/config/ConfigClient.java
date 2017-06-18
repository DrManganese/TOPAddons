package io.github.drmanganese.topaddons.config;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import io.github.drmanganese.topaddons.config.network.MessageClientOptions;
import io.github.drmanganese.topaddons.config.network.PacketHandler;
import io.github.drmanganese.topaddons.reference.ElementSync;
import io.github.drmanganese.topaddons.reference.Reference;

import java.util.HashMap;
import java.util.Map;

/**
 * Client-side only configs, all properties are integers because of capability implementation
 */
@SideOnly(Side.CLIENT)
public class ConfigClient {

    private static Configuration config;
    private static String fluidGaugeDisplay = "Both";
    private static boolean forestryReasonCrouch = false;
    private static boolean showPitch = true;
    private static boolean ic2Progress = false;
    private static boolean smelteryInIngots = true;
    private static boolean colorDragonName = true;

    public static void init(Configuration configIn) {
        config = configIn;
        config.load();

        // Remove the old stuff
        ConfigCategory oldOptions = config.getCategory("Options");
        if (oldOptions != null) {
            config.removeCategory(oldOptions);
        }

        fluidGaugeDisplay = config.getString("fluidGaugeDisplay", "Client Options", "Both", "Which tank gauge(s) to display (The One Probe, TOP Addons, Both).", new String[]{"The One Probe", "TOP Addons", "Both"});
        forestryReasonCrouch = config.getBoolean("forestryReasonCrouch", "Client Options", false, "Only show Forestry machines' important failure reasons when crouching.");
        showPitch = config.getBoolean("showPitch", "Client Options", true, "Display pitch and instrument on Note Blocks.");
        ic2Progress = config.getBoolean("ic2Progress", "Client Options", false, "Show IC² machine progress bar when not crouching.");
        smelteryInIngots = config.getBoolean("smelteryInIngots", "Client Options", true, "Show smeltery fluid volume in ingots.");
        colorDragonName = config.getBoolean("colorDragonName", "Client Options", true, "Color the name of an Ice and Fire dragon depending on its type.");

        if (config.hasChanged()) {
            config.save();
        }
    }

    @SubscribeEvent
    public static void onConfigChangedOnConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Reference.MOD_ID)) {
            config.save();
            if (event.isWorldRunning()) {
                PacketHandler.INSTANCE.sendToServer(new MessageClientOptions(getClientValues(config), ElementSync.elementIds, Minecraft.getMinecraft().thePlayer));
            }
        }
    }

    public static Map<String, Integer> getClientValues(Configuration config) {
        Map<String, Integer> values = new HashMap<>();
        for (Map.Entry<String, Property> entry : config.getCategory("Client Options").entrySet()) {
            Property prop = entry.getValue();
            switch (entry.getValue().getType()) {
                case BOOLEAN:
                    values.put(entry.getKey(), prop.getBoolean() ? 1 : 0);
                    break;
                case INTEGER:
                    values.put(entry.getKey(), prop.getInt());
                    break;
                case STRING:
                    for (int i = 0; i < prop.getValidValues().length; i++) {
                        if (prop.getValidValues()[i].equals(prop.getString())) {
                            values.put(entry.getKey(), i);
                        }
                    }
                    break;
                default:
                    values.put(entry.getKey(), entry.getValue().getInt());
                    break;
            }
        }

        return values;
    }

}
