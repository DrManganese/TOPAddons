package io.github.drmanganese.topaddons.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import io.github.drmanganese.topaddons.AddonManager;
import io.github.drmanganese.topaddons.config.HelmetConfig;
import io.github.drmanganese.topaddons.helmets.LayerChip;
import io.github.drmanganese.topaddons.reference.EnumChip;
import io.github.drmanganese.topaddons.reference.Reference;

import static mcjty.theoneprobe.items.ModItems.PROBETAG;

@SideOnly(Side.CLIENT)
public final class ClientProxy implements IProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        Minecraft.getMinecraft().getRenderManager().getSkinMap().forEach((str, renderPlayer) -> {
            renderPlayer.addLayer(new LayerChip());
        });
    }

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {
        for (EnumChip enumChip : EnumChip.values()) {
            enumChip.setSprite(event.getMap().registerSprite(new ResourceLocation(Reference.MOD_ID, "items/" + enumChip.getTexture())));
        }
    }

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        if (event.getItemStack().getItem() instanceof ItemArmor && (AddonManager.SPECIAL_HELMETS.containsKey(((ItemArmor) event.getItemStack().getItem()).getClass()) || !HelmetConfig.helmetBlacklistSet.contains(event.getItemStack().getItem().getRegistryName()))) {
            if (event.getItemStack().hasTagCompound() && event.getItemStack().getTagCompound().getInteger(PROBETAG) == 1) {
                event.getToolTip().add(TextFormatting.AQUA + "Probing");
            }
        }
    }
}
