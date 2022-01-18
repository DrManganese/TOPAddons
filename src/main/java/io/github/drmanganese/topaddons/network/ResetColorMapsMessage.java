package io.github.drmanganese.topaddons.network;

import io.github.drmanganese.topaddons.TopAddons;
import io.github.drmanganese.topaddons.client.FluidColors;

import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ResetColorMapsMessage {

    static void onMessage(ResetColorMapsMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(FluidColors::resetMaps).thenRun(() -> TopAddons.LOGGER.debug("Reset fluid color maps"));
        ctx.get().setPacketHandled(true);
    }
}
