package io.github.drmanganese.topaddons.addons;

import io.github.drmanganese.topaddons.api.TOPAddon;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.blakebr0.mysticalagriculture.blocks.crop.BlockMysticalCrop;
import com.blakebr0.mysticalagriculture.items.ItemSeed;
import com.blakebr0.mysticalagriculture.tileentity.reprocessor.TileEssenceReprocessor;
import com.blakebr0.mysticalagriculture.util.MystUtils;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

@TOPAddon(dependency = "mysticalagriculture")
public class AddonMysticalAgriculture extends AddonBlank {

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        if (blockState.getBlock() instanceof BlockMysticalCrop) {
            final BlockMysticalCrop cropBlock = (BlockMysticalCrop) blockState.getBlock();
            if (cropBlock.getSeed() instanceof ItemSeed) {
                final ItemSeed seed = (ItemSeed) cropBlock.getSeed();
                textPrefixed(probeInfo, "Tier", MystUtils.getColorFromMeta(seed.getTier() - 1) + String.valueOf(seed.getTier()));
            }
            return;
        }
        final TileEntity tile = world.getTileEntity(data.getPos());
        if (tile instanceof TileEssenceReprocessor) {
            final TileEssenceReprocessor seedReprocessor = (TileEssenceReprocessor) tile;
            progressBar(probeInfo, seedReprocessor.getFuel(), seedReprocessor.getFuelCapacity(), 0xff5b5b5b, 0xff303030, "Fuel: ", "");
        }
    }
}
