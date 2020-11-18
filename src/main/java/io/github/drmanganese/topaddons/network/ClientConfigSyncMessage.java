package io.github.drmanganese.topaddons.network;

import io.github.drmanganese.topaddons.TopAddons;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Network message which is sent to the server when a client joins the server or updates their client-only configuration
 * settings.
 * <p>
 * Each synced setting is turned into a string and stored as a player capability.
 */
public class ClientConfigSyncMessage {

    private final Map<String, String> syncMap;

    ClientConfigSyncMessage(Map<String, String> syncMap) {
        this.syncMap = new HashMap<>(syncMap.size());
        syncMap.forEach(this.syncMap::put);
    }

    static ClientConfigSyncMessage decode(PacketBuffer buf) {
        final int size = buf.readInt();
        final HashMap<String, String> syncMap = new HashMap<>(size);
        for (int i = 0; i < size; i++) syncMap.put(buf.readString(32767), buf.readString(32767));

        return new ClientConfigSyncMessage(syncMap);
    }

    static void onMessage(ClientConfigSyncMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
            Objects.requireNonNull(ctx.get().getSender())
                .getCapability(TopAddons.CLIENT_CFG_CAP)
                .ifPresent(cap -> cap.fromMap(message.syncMap)));
    }

    void encode(PacketBuffer buf) {
        buf.writeInt(syncMap.size());
        syncMap.forEach((key, value) -> {
            buf.writeString(key);
            buf.writeString(value);
        });
    }
}
