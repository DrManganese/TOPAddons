package io.github.drmanganese.topaddons;

import io.github.drmanganese.topaddons.api.IAddonBlocks;
import io.github.drmanganese.topaddons.api.IAddonElements;
import io.github.drmanganese.topaddons.api.IAddonEntities;
import io.github.drmanganese.topaddons.elements.ElementSync;
import io.github.drmanganese.topaddons.elements.top.ElementItemStackBackground;
import io.github.drmanganese.topaddons.elements.top.ElementSimpleProgressCentered;

import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import net.minecraftforge.fml.common.event.FMLInterModComms;

import com.google.common.base.Function;

import javax.annotation.Nullable;

public class TOPRegistrar {

    private static boolean registered;

    static void register() {
        if (registered)
            return;
        registered = true;

        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "io.github.drmanganese.topaddons.TOPRegistrar$GetTheOneProbe");
    }

    public static class GetTheOneProbe implements Function<ITheOneProbe, Void> {

        static ITheOneProbe probe;

        @Nullable
        @Override
        public Void apply(ITheOneProbe theOneProbe) {
            probe = theOneProbe;
            for (Object addon : AddonLoader.ADDONS) {
                if (addon instanceof IAddonBlocks) {
                    probe.registerProvider((IProbeInfoProvider) addon);
                }

                if (addon instanceof IAddonEntities) {
                    probe.registerEntityProvider((IProbeInfoEntityProvider) addon);
                }

                if (addon instanceof IAddonElements) {
                    ((IAddonElements) addon).registerElements(probe);
                }
            }

            ElementSync.registerElement(probe, "centered_progress", ElementSimpleProgressCentered::new);
            ElementSync.registerElement(probe, "itemstack_background", ElementItemStackBackground::new);
            return null;
        }
    }
}
