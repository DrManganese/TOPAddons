package io.github.drmanganese.topaddons.addons.industrialforegoing.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;

import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.CapabilityItemHandler;

import com.buuz135.industrial.item.MobImprisonmentToolItem;
import com.buuz135.industrial.tile.mob.MobDuplicatorTile;

/**
 * Mob spawner icon and mob name.
 */
public class MobDuplicatorInfo implements ITileInfo<MobDuplicatorTile> {

    @GameRegistry.ObjectHolder("industrialforegoing:mob_imprisonment_tool")
    private static Item MOB_TOOL;

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, MobDuplicatorTile tile) {
        if (!tile.isPaused()) {
            ItemStack mobStack = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(6);
            if (!mobStack.isEmpty() && mobStack.getItem() == MOB_TOOL) {
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                        .item(mobStack)
                        .text(EntityList.getTranslationName(new ResourceLocation(((MobImprisonmentToolItem) mobStack.getItem()).getID(mobStack))));
            }
        }
    }
}
