package io.github.drmanganese.topaddons.network;

import io.github.drmanganese.topaddons.reference.Reference;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

public class PacketHandler {

    private static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

    public static void init() {
        INSTANCE.registerMessage(MessageElementSync.class, MessageElementSync.class, 0, Side.SERVER);
        INSTANCE.registerMessage(MessageClientCfgSync.class, MessageClientCfgSync.class, 1, Side.SERVER);
    }

    public static void sendElementSync(Map<String, Integer> elementIdMap) {
        INSTANCE.sendToServer(new MessageElementSync(elementIdMap));
    }

    public static void sendClientCfg(Map<String, Object> syncMap) {
        INSTANCE.sendToServer(new MessageClientCfgSync(syncMap));
    }
}
