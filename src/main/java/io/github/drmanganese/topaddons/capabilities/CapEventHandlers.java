package io.github.drmanganese.topaddons.capabilities;

import io.github.drmanganese.topaddons.TopAddons;
import io.github.drmanganese.topaddons.config.Config;
import io.github.drmanganese.topaddons.network.PacketHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static io.github.drmanganese.topaddons.TopAddons.CLIENT_CFG_CAP;
import static io.github.drmanganese.topaddons.TopAddons.ELT_SYNC_CAP;

@Mod.EventBusSubscriber
public class CapEventHandlers {

    // Create the capability when the player entity is created
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(TopAddons.MOD_ID, "element_sync"), new ElementSyncCapabilityProvider());
            event.addCapability(new ResourceLocation(TopAddons.MOD_ID, "client_cfg_sync"), new ClientCfgCapabilityProvider());
        }
    }

    //Send client's id map and config options to the server
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getWorld().isRemote && event.getEntity() == Minecraft.getInstance().player) {
            PacketHandler.sendElementSync(ElementSync.elementIdMap);
            PacketHandler.sendClientCfg(Config.collectClientConfigValues());
        }
    }

    //Copy over capability data when player respawns
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(ELT_SYNC_CAP).ifPresent((oldCap) ->
                    event.getPlayer().getCapability(ELT_SYNC_CAP).ifPresent(cap -> cap.setElementIds(oldCap.getAllElementIds()))
            );

            event.getOriginal().getCapability(CLIENT_CFG_CAP).ifPresent((oldCap) ->
                    event.getPlayer().getCapability(CLIENT_CFG_CAP).ifPresent(cap -> cap.copy(oldCap))
            );
        }
    }
}
