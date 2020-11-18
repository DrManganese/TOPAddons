package io.github.drmanganese.topaddons.network;

import io.github.drmanganese.topaddons.TopAddons;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Network message that is sent to the server when the client joins. It stores the client's element discriminators in a
 * player capability.
 */
class ElementSyncMessage {

    private final Map<String, Integer> elementIdMap;

    ElementSyncMessage(Map<String, Integer> elementIdMap) {
        this.elementIdMap = elementIdMap;
    }

    static ElementSyncMessage decode(PacketBuffer buf) {
        final int size = buf.readInt();
        final Map<String, Integer> elementIdMap = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            elementIdMap.put(buf.readString(32767), buf.readInt());
        }
        return new ElementSyncMessage(elementIdMap);
    }

    static void onMessage(ElementSyncMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                Objects.requireNonNull(ctx.get().getSender())
                        .getCapability(TopAddons.ELT_SYNC_CAP)
                        .ifPresent(cap -> cap.setElementIds(msg.elementIdMap))
        );
    }

    void encode(PacketBuffer buf) {
        buf.writeInt(this.elementIdMap.size());
        elementIdMap.forEach((key, value) -> {
            buf.writeString(key);
            buf.writeInt(value);
        });
    }
}
