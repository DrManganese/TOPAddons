package io.github.drmanganese.topaddons.network;

import io.github.drmanganese.topaddons.TopAddons;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Map;
import java.util.Optional;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    private static SimpleChannel INSTANCE;

    public static void init() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(TopAddons.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
        );
        INSTANCE.registerMessage(0, ClientConfigSyncMessage.class, ClientConfigSyncMessage::encode, ClientConfigSyncMessage::decode, ClientConfigSyncMessage::onMessage, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        INSTANCE.registerMessage(1, ResetColorMapsMessage.class, PacketHandler::dummyEncoder, b -> new ResetColorMapsMessage(), ResetColorMapsMessage::onMessage, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    public static void sendClientCfg(Map<String, String> syncMap) {
        TopAddons.LOGGER.debug("Player {}({}) syncing config to server", Minecraft.getInstance().player.getDisplayName(), Minecraft.getInstance().player.getUUID().toString());
        INSTANCE.sendToServer(new ClientConfigSyncMessage(syncMap));
    }

    public static void sendResetColorsMapsMessage(ServerPlayer targetPlayer) {
        TopAddons.LOGGER.debug("Asking {}({}) to reset fluid color maps", Minecraft.getInstance().player.getDisplayName(), Minecraft.getInstance().player.getUUID().toString());
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> targetPlayer), new ResetColorMapsMessage());
    }

    private static <MSG> void dummyEncoder(MSG msg, FriendlyByteBuf buf) {}
}
