package io.github.drmanganese.topaddons.addons.chisel;

import com.google.common.collect.ImmutableMap;
import io.github.drmanganese.topaddons.api.IAddonBlocks;
import io.github.drmanganese.topaddons.api.IBlockInfo;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.api.TOPAddon;
import io.github.drmanganese.topaddons.styles.Styles;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.styles.IconStyle;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import team.chisel.api.block.ICarvable;
import team.chisel.common.block.BlockCarvable;
import team.chisel.common.block.TileAutoChisel;

import javax.annotation.Nonnull;

import static io.github.drmanganese.topaddons.styles.ProgressStyles.SIMPLE_PROGRESS;

@TOPAddon(dependency = "chisel")
public class AddonChisel implements IAddonBlocks {

    @Nonnull
    @Override
    public ImmutableMap<Class<? extends Block>, IBlockInfo> getBlockClasses() {
        return ImmutableMap.of(BlockCarvable.class, new CarvableInfo());
    }

    @Nonnull
    @Override
    public ImmutableMap<Class<? extends TileEntity>, ITileInfo> getTiles() {
        return ImmutableMap.of(TileAutoChisel.class, new AutoChiselInfo());
    }

    private static class CarvableInfo implements IBlockInfo {

        private static final ResourceLocation IRON_CHISEL_RL = new ResourceLocation("chisel", "textures/items/chisel_iron.png");
        private static final IIconStyle IRON_CHISEL_ICON_STYLE = new IconStyle().textureWidth(8).textureHeight(8).width(8).height(8);

        @Override
        public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData) {
            ICarvable block = (ICarvable) blockState.getBlock();
            ItemStack stack = hitData.getPickBlock();
            String unloc = stack.getTranslationKey() + "." + block.getVariationData(stack.getItemDamage()).name + ".desc.1";
            probeInfo.horizontal(Styles.horiCentered().spacing(1))
                    .icon(IRON_CHISEL_RL, 0, 0, 8, 8, IRON_CHISEL_ICON_STYLE)
                    .text(TextFormatting.GRAY + IProbeInfo.STARTLOC + unloc + IProbeInfo.ENDLOC);
        }
    }

    private static class AutoChiselInfo implements ITileInfo<TileAutoChisel> {

        @Override
        public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, TileAutoChisel tile) {
            final ItemStack target = tile.getTarget();
            if (!target.isEmpty() && tile.getProgress() > 0)
                probeInfo.horizontal(Styles.horiCentered())
                        .item(tile.getTarget())
                        .progress(100 * tile.getProgress() / tile.getMaxProgress(), 100,
                                Styles.machineProgress(player, "Chiseling")
                                        .filledColor(0xffb8b9ba)
                                        .alternateFilledColor(0xffa2a2aa)
                                        .width(81)
                        );
        }
    }
}
