package io.github.drmanganese.topplugins;

import net.minecraftforge.fml.common.event.FMLInterModComms;
import io.github.drmanganese.topplugins.plugins.PluginManager;

import com.google.common.base.Function;

import javax.annotation.Nullable;

import mcjty.theoneprobe.api.IProbeConfigProvider;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.ITheOneProbe;

public class Plugins {

    private static boolean registered;

    public static void register() {
        if (registered)
            return;
        registered = true;

        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "io.github.drmanganese.topplugins.Plugins$GetTheOneProbe");
    }

    public static class GetTheOneProbe implements Function<ITheOneProbe, Void> {

        public static ITheOneProbe probe;

        @Nullable
        @Override
        public Void apply(ITheOneProbe theOneProbe) {
            probe = theOneProbe;
            PluginManager.plugins.forEach(plugin -> {
                probe.registerProvider(plugin);
                probe.registerProbeConfigProvider((IProbeConfigProvider) plugin);
                probe.registerEntityProvider((IProbeInfoEntityProvider) plugin);

            });

            return null;
        }
    }
}
