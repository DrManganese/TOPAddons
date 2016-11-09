package io.github.drmanganese.topaddons.config;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import io.github.drmanganese.topaddons.TOPAddons;
import io.github.drmanganese.topaddons.config.network.MessageClientOptions;
import io.github.drmanganese.topaddons.config.network.PacketHandler;
import io.github.drmanganese.topaddons.reference.Names;
import io.github.drmanganese.topaddons.reference.Reference;

import java.util.HashMap;
import java.util.Map;

/**
 * Client-side only configs, all properties are integers because of capability implementation
 */
@SideOnly(Side.CLIENT)
public class ConfigClient {

    public static final Map<String, Integer> VALUES = new HashMap<>();
    private static final Map<String, Tuple<Integer, String>> DEFAULTS = new HashMap<>();

    static {
        DEFAULTS.put("fluidGauge", new Tuple<>(1, "Display the TOP Addons fluid gauge for internal tanks on tiles (0 to disable)."));
        DEFAULTS.put("hideTOPTank", new Tuple<>(0, "Hide the vanilla TOP fluid gauge (1 to hide)."));
        DEFAULTS.put("forestryReasonCrouch", new Tuple<>(0, "Only show Forestry machines' important failure reasons when crouching (1 to enable)."));
        DEFAULTS.put("showPitch", new Tuple<>(1, "Display pitch and instrument on Note Blocks (0 to disable)."));
        DEFAULTS.put("ic2Progress", new Tuple<>(0, "Show IC² machine progress bar (0: Extended mode, 1: Normal mode)."));
    }

    public static void init(Configuration config) {
        config.load();
        DEFAULTS.forEach((s, t) -> VALUES.put(s, config.getInt(s, "Options", t.getFirst(), 0, 1, t.getSecond())));
        if (config.hasChanged()) {
            config.save();
        }
    }

    @SubscribeEvent
    public static void onConfigChangedOnConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Reference.MOD_ID)) {
            DEFAULTS.forEach((s, t) -> VALUES.put(s, TOPAddons.configClient.getInt(s, "Options", t.getFirst(), 0, 1, t.getSecond())));
            if (Minecraft.getMinecraft().theWorld != null)
                PacketHandler.INSTANCE.sendToServer(new MessageClientOptions(ConfigClient.VALUES, (EntityPlayer) Minecraft.getMinecraft().thePlayer));
        }
    }

    public static void set(String key, int value) {
        TOPAddons.configClient.get("Options",
                key,
                DEFAULTS.get(key).getFirst(),
                DEFAULTS.get(key).getSecond(),
                0,
                Names.clientConfigOptions.get(key) == Boolean.TYPE ? 1 : Integer.MAX_VALUE)
            .set(value);
        VALUES.put(key, value);
        TOPAddons.configClient.save();
    }

}
