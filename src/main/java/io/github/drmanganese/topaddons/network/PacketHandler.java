package io.github.drmanganese.topaddons.network;

import io.github.drmanganese.topaddons.TopAddons;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

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
        INSTANCE.registerMessage(0, ElementSyncMessage.class, ElementSyncMessage::encode, ElementSyncMessage::decode, ElementSyncMessage::onMessage, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        INSTANCE.registerMessage(1, ClientConfigSyncMessage.class, ClientConfigSyncMessage::encode, ClientConfigSyncMessage::decode, ClientConfigSyncMessage::onMessage, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        INSTANCE.registerMessage(2, ResetColorMapsMessage.class, PacketHandler::dummyEncoder, b -> new ResetColorMapsMessage(), ResetColorMapsMessage::onMessage, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    public static void sendElementSync(Map<String, Integer> elementIdMap) {
        TopAddons.LOGGER.debug("Player {}({}) syncing element ids to server", Minecraft.getInstance().player.getDisplayName(), Minecraft.getInstance().player.getUniqueID().toString());
        INSTANCE.sendToServer(new ElementSyncMessage(elementIdMap));
    }

    public static void sendClientCfg(Map<String, String> syncMap) {
        TopAddons.LOGGER.debug("Player {}({}) syncing config to server", Minecraft.getInstance().player.getDisplayName(), Minecraft.getInstance().player.getUniqueID().toString());
        INSTANCE.sendToServer(new ClientConfigSyncMessage(syncMap));
    }

    public static void sendResetColorsMapsMessage(ServerPlayerEntity targetPlayer) {
        TopAddons.LOGGER.debug("Asking {}({}) to reset fluid color maps", Minecraft.getInstance().player.getDisplayName(), Minecraft.getInstance().player.getUniqueID().toString());
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> targetPlayer), new ResetColorMapsMessage());
    }

    private static <MSG> void dummyEncoder(MSG msg, PacketBuffer buf) {}
}
