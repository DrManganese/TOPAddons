package io.github.drmanganese.topaddons.config.capabilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import io.github.drmanganese.topaddons.config.ConfigClient;
import io.github.drmanganese.topaddons.config.network.MessageClientOptions;
import io.github.drmanganese.topaddons.config.network.PacketHandler;
import io.github.drmanganese.topaddons.reference.Reference;

import javax.annotation.Nullable;

public class ModCapabilities {

    @CapabilityInject(IClientOptsCapability.class)
    public static final Capability<IClientOptsCapability> OPTIONS = null;


    @SubscribeEvent
    public void onAttachCapabilityEntity(AttachCapabilitiesEvent.Entity event) {
        if (event.getEntity() instanceof EntityPlayer) {
            event.addCapability(new ResourceLocation(Reference.MOD_ID, "options"), new ICapabilityProvider() {
                @Override
                public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
                    return capability == OPTIONS;
                }

                @Override
                public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
                    return capability == OPTIONS ?  OPTIONS.cast(OPTIONS.getDefaultInstance()) : null;
                }
            });
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            if (event.getWorld().isRemote) {
                //noinspection VariableUseSideOnly
                PacketHandler.INSTANCE.sendToServer(new MessageClientOptions(ConfigClient.VALUES, (EntityPlayer) event.getEntity()));
            }
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        IClientOptsCapability originalCap = event.getOriginal().getCapability(OPTIONS, null);
        event.getEntityPlayer().getCapability(OPTIONS, null).setAll(originalCap.getAll());
    }
}
