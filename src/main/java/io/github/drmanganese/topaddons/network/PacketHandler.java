package io.github.drmanganese.topaddons.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import io.github.drmanganese.topaddons.reference.Reference;

public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

    public static void init() {
        INSTANCE.registerMessage(MessageClientOptions.class, MessageClientOptions.class, 0, Side.SERVER);
        INSTANCE.registerMessage(MessageClientOption.class, MessageClientOption.class, 1, Side.CLIENT);

        INSTANCE.registerMessage(MessageSendTranslation.class, MessageSendTranslation.class, 2, Side.SERVER);
        INSTANCE.registerMessage(MessageAskTranslation.class, MessageAskTranslation.class, 3, Side.CLIENT);
    }



}
