package io.github.drmanganese.topaddons.capabilities;

import io.github.drmanganese.topaddons.TOPAddons;
import io.github.drmanganese.topaddons.config.ModConfig;
import io.github.drmanganese.topaddons.elements.ElementSync;
import io.github.drmanganese.topaddons.network.PacketHandler;
import io.github.drmanganese.topaddons.reference.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static io.github.drmanganese.topaddons.TOPAddons.CLIENT_CFG_CAP;
import static io.github.drmanganese.topaddons.TOPAddons.ELT_SYNC_CAP;

@Mod.EventBusSubscriber
public class CapEventHandlers {

    //Create the capability when the player entity is created
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(new ResourceLocation(Reference.MOD_ID, "elementSync"), new ICapabilityProvider() {
                private final IElementSyncCapability instance = ELT_SYNC_CAP.getDefaultInstance();

                @Override
                public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
                    return capability == ELT_SYNC_CAP;
                }

                @Nullable
                @Override
                public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
                    return capability == ELT_SYNC_CAP ? ELT_SYNC_CAP.<T>cast(this.instance) : null;
                }
            });

            event.addCapability(new ResourceLocation(Reference.MOD_ID, "clientCfgSync"), new ICapabilityProvider() {
                private final IClientCfgCapability instance = CLIENT_CFG_CAP.getDefaultInstance();

                @Override
                public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
                    return capability == CLIENT_CFG_CAP;
                }

                @Nullable
                @Override
                public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
                    return capability == CLIENT_CFG_CAP ? CLIENT_CFG_CAP.<T>cast(this.instance) : null;
                }
            });
        }
    }

    //Send client's id map and config options to the server
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getWorld().isRemote && event.getEntity() == Minecraft.getMinecraft().player) {
            PacketHandler.sendElementSync(ElementSync.elementIdMap);
            PacketHandler.sendClientCfg(ModConfig.updateSync(TOPAddons.CONFIG));
        }
    }

    //Copy over capability data when player respawns
    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        IElementSyncCapability originalSyncCap = event.getOriginal().getCapability(ELT_SYNC_CAP, null);
        IClientCfgCapability originalCfgCap = event.getOriginal().getCapability(CLIENT_CFG_CAP, null);
        event.getEntityPlayer().getCapability(ELT_SYNC_CAP, null).setElementIds(originalSyncCap.getAllElementIds());
        event.getEntityPlayer().getCapability(CLIENT_CFG_CAP, null).setAll(originalCfgCap.getAll());
    }
}
