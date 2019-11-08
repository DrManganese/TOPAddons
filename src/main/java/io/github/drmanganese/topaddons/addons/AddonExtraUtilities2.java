package io.github.drmanganese.topaddons.addons;

import io.github.drmanganese.topaddons.api.TOPAddon;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.NoSuchElementException;
import java.util.Optional;

import static mcjty.theoneprobe.api.TextStyleClass.*;

@TOPAddon(dependency = "extrautils2")
public class AddonExtraUtilities2 extends AddonBlank {

    @GameRegistry.ObjectHolder("extrautils2:enderlilly")
    public static Block ENDER_LILY;

    private static final int MAX_GROWTH = 7;

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        if (blockState.getBlock() == ENDER_LILY) {
            try {
                final int growth = getGrowthValue(blockState);
                if (growth == MAX_GROWTH) {
                    probeInfo.text(OK + "Fully grown");
                } else {
                    probeInfo.text(LABEL + "Growth: " + WARNING + (growth * 100) / MAX_GROWTH + "%");
                }
            } catch (NoSuchElementException | ClassCastException ignored) {
            }
        }
    }

    private int getGrowthValue(IBlockState blockState) throws NoSuchElementException, ClassCastException {
        final Optional<IProperty<?>> maybeGrowthProperty = blockState.getPropertyKeys().stream().filter(p -> p.getName().equals("growth")).findFirst();
        return blockState.getValue((IProperty<Integer>) maybeGrowthProperty.get());
    }
}
