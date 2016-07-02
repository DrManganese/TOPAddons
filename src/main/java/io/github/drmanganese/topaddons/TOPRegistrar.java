package io.github.drmanganese.topaddons;

import net.minecraftforge.fml.common.event.FMLInterModComms;
import io.github.drmanganese.topaddons.addons.AddonManager;

import com.google.common.base.Function;

import javax.annotation.Nullable;

import mcjty.theoneprobe.api.ITheOneProbe;

public class TOPRegistrar {

    private static boolean registered;

    public static void register() {
        if (registered)
            return;
        registered = true;

        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "io.github.drmanganese.topaddons.TOPRegistrar$GetTheOneProbe");
    }

    public static class GetTheOneProbe implements Function<ITheOneProbe, Void> {

        public static ITheOneProbe probe;

        @Nullable
        @Override
        public Void apply(ITheOneProbe theOneProbe) {
            probe = theOneProbe;
            AddonManager.addons.forEach(addon -> {
                probe.registerProvider(addon);
                probe.registerProbeConfigProvider(addon);
                probe.registerEntityProvider(addon);
                addon.registerElements();
            });

            return null;
        }
    }
}
