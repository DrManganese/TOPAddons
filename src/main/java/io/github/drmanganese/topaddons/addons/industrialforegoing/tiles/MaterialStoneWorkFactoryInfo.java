package io.github.drmanganese.topaddons.addons.industrialforegoing.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.elements.ElementSync;
import io.github.drmanganese.topaddons.elements.top.ElementItemStackBackground;

import io.github.drmanganese.topaddons.styles.Styles;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import com.buuz135.industrial.tile.world.MaterialStoneWorkFactoryTile;
import com.buuz135.industrial.tile.world.MaterialStoneWorkFactoryTile.Mode;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Contents and modes.
 */
public class MaterialStoneWorkFactoryInfo implements ITileInfo<MaterialStoneWorkFactoryTile> {

    private static final EnumDyeColor[] COLORS = new EnumDyeColor[]{EnumDyeColor.YELLOW, EnumDyeColor.BLUE, EnumDyeColor.GREEN, EnumDyeColor.ORANGE, EnumDyeColor.PURPLE};

    @Override
    public void getInfo(ProbeMode probeMode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hitData, MaterialStoneWorkFactoryTile tile) {
        LinkedHashMap<ItemStackHandler, Mode> modeList = tile.getModeList();
        IProbeInfo hori = probeInfo.horizontal(Styles.horiCentered().borderColor(0xff777777).spacing(-2));

        int i = 0;
        for (Map.Entry<ItemStackHandler, Mode> entry : modeList.entrySet()) {
            hori.vertical(probeInfo.defaultLayoutStyle().spacing(-2))
                    .element(new ElementItemStackBackground(ElementSync.getId("itemstack_background", player), entry.getKey().getStackInSlot(0), COLORS[i].getColorValue() + 0x55000000, probeInfo.defaultItemStyle().width(16).height(16)))
                    .element(new ElementItemStackBackground(ElementSync.getId("itemstack_background", player), entry.getKey().getStackInSlot(1), COLORS[i].getColorValue() + 0x55000000, probeInfo.defaultItemStyle().width(16).height(16)));

            if (i < modeList.size() - 1) {
                hori.item(entry.getValue().getItemStack(), probeInfo.defaultItemStyle());
            }
            i++;
        }
    }
}
