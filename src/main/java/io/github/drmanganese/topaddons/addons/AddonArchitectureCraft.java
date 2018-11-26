package io.github.drmanganese.topaddons.addons;

import io.github.drmanganese.topaddons.TOPAddons;
import io.github.drmanganese.topaddons.api.TOPAddon;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.elytradev.architecture.common.shape.Shape;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IBlockDisplayOverride;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.config.Config;

import java.util.Collections;
import java.util.List;

@TOPAddon(dependency = "architecturecraft")
public class AddonArchitectureCraft extends AddonBlank {

    @GameRegistry.ObjectHolder("architecturecraft:shape")
    public static Block SHAPE;

    private static String getShapeBlockName(ItemStack pickBlock) {
        final NBTTagCompound tag = pickBlock.getTagCompound();
        final Block block = Block.getBlockFromName(tag.getString("BaseName"));

        if (block == null) {
            return pickBlock.getDisplayName();
        } else {
            final Item item = Item.getItemFromBlock(block);
            final ItemStack stack = new ItemStack(item, 1, tag.getInteger("BaseData"));
            return stack.getDisplayName();
        }
    }

    private static String getShapeName(ItemStack pickBlock) {
        return Shape.forId(pickBlock.getTagCompound().getInteger("Shape")).title;
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        if (blockState.getBlock() == SHAPE) {
            boolean swap = player.getCapability(TOPAddons.OPTS_CAP, null).getBoolean("swapArchitectureShapeAndMaterial");
            final String description = !swap ? getShapeBlockName(data.getPickBlock()) : getShapeName(data.getPickBlock());
            probeInfo.text(TextStyleClass.LABEL + description);
        }
    }

    @Override
    public List<IBlockDisplayOverride> getBlockDisplayOverrides() {
        return Collections.singletonList(new IBlockDisplayOverride() {
            @Override
            public boolean overrideStandardInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
                if (blockState.getBlock() == SHAPE) {
                    boolean swap = player.getCapability(TOPAddons.OPTS_CAP, null).getBoolean("swapArchitectureShapeAndMaterial");
                    final String name = swap ? getShapeBlockName(data.getPickBlock()) : getShapeName(data.getPickBlock());

                    if (Tools.show(mode, Config.getRealConfig().getShowModName())) {
                        probeInfo.horizontal()
                                .item(data.getPickBlock())
                                .vertical()
                                .text(name)
                                .text(TextStyleClass.MODNAME + Tools.getModName(((ItemBlock) data.getPickBlock().getItem()).getBlock()));
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
}
