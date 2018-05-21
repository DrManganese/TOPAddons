package io.github.drmanganese.topaddons.capabilities;

import io.github.drmanganese.topaddons.elements.ElementSync;
import io.github.drmanganese.topaddons.network.MessageElementSync;
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

import static io.github.drmanganese.topaddons.TOPAddons.SYNC_CAP;

@Mod.EventBusSubscriber
public class CapEventHandlers {

    //Create the capability when the player entity is created
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(new ResourceLocation(Reference.MOD_ID, "elementSync"), new ICapabilityProvider() {
                private IElementSyncCapability instance = SYNC_CAP.getDefaultInstance();

                @Override
                public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
                    return capability == SYNC_CAP;
                }

                @Nullable
                @Override
                public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
                    return capability == SYNC_CAP ? SYNC_CAP.<T>cast(this.instance) : null;
                }
            });
        }
    }

    //Send client's id map to the server
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getWorld().isRemote && event.getEntity() == Minecraft.getMinecraft().player) {
            PacketHandler.INSTANCE.sendToServer(new MessageElementSync(ElementSync.elementIdMap));
        }
    }

    //Copy over capability data when player respawns
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        IElementSyncCapability originalCap = event.getOriginal().getCapability(SYNC_CAP, null);
        event.getEntityPlayer().getCapability(SYNC_CAP, null).setElementIds(originalCap.getAllElementIds());
    }
}
