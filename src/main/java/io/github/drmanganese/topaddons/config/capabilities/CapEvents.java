package io.github.drmanganese.topaddons.config.capabilities;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import io.github.drmanganese.topaddons.config.ConfigClient;
import io.github.drmanganese.topaddons.network.MessageClientOptions;
import io.github.drmanganese.topaddons.network.PacketHandler;
import io.github.drmanganese.topaddons.reference.Reference;

import javax.annotation.Nullable;

import static io.github.drmanganese.topaddons.TOPAddons.OPTS_CAP;

public class CapEvents {

    @SubscribeEvent
    public void onAttachCapabilityEntity(AttachCapabilitiesEvent.Entity event) {
        if (event.getEntity() instanceof EntityPlayer) {
            event.addCapability(new ResourceLocation(Reference.MOD_ID, "options"), new ICapabilityProvider() {
                private IClientOptsCapability instance = OPTS_CAP.getDefaultInstance();

                @Override
                public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
                    return capability == OPTS_CAP;
                }

                @Override
                public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
                    return capability == OPTS_CAP ? OPTS_CAP.<T>cast(this.instance) : null;
                }
            });
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getWorld().isRemote && event.getEntity() == Minecraft.getMinecraft().player) {
            //noinspection VariableUseSideOnly
            PacketHandler.INSTANCE.sendToServer(new MessageClientOptions(ConfigClient.VALUES, (EntityPlayer) event.getEntity()));
        }

    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        IClientOptsCapability originalCap = event.getOriginal().getCapability(OPTS_CAP, null);
        event.getEntityPlayer().getCapability(OPTS_CAP, null).setAll(originalCap.getAll());
    }
}
