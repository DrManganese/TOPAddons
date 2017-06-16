package io.github.drmanganese.topaddons.addons;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.World;

import io.github.drmanganese.topaddons.api.TOPAddon;

import java.util.Collections;
import java.util.List;

import gcewing.architecture.ArchitectureCraft;
import gcewing.architecture.Shape;
import gcewing.architecture.ShapeTE;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IBlockDisplayOverride;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;

import static mcjty.theoneprobe.api.TextStyleClass.MODNAME;

@TOPAddon(dependency = "architecturecraft")
public class AddonArchitectureCraft extends AddonBlank {

    @Override
    public List<IBlockDisplayOverride> getBlockDisplayOverrides() {
        return Collections.singletonList(new IBlockDisplayOverride() {
            @Override
            public boolean overrideStandardInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
                if (blockState.getBlock() == ArchitectureCraft.blockShape && world.getTileEntity(data.getPos()) instanceof ShapeTE) {
                    int id = data.getPickBlock().getTagCompound().getInteger("Shape");
                    String name = Shape.forId(id).title;

                    if (Tools.show(mode, mcjty.theoneprobe.config.Config.getRealConfig().getShowModName())) {
                        probeInfo.horizontal()
                                .item(data.getPickBlock())
                                .vertical()
                                .text(name)
                                .text(MODNAME + Tools.getModName(((ItemBlock) data.getPickBlock().getItem()).getBlock()));
                    } else {
                        probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                                .item(data.getPickBlock())
                                .text(name);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        if ( world.getTileEntity(data.getPos()) instanceof ShapeTE) {
            ShapeTE tile = (ShapeTE) world.getTileEntity(data.getPos());
            probeInfo.text(TextStyleClass.INFO + tile.baseBlockState.getBlock().getLocalizedName());
        }
    }
}
