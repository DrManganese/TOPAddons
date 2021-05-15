package io.github.drmanganese.topaddons.addons.vanilla.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.styles.Styles;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import mcjty.theoneprobe.api.*;

import javax.annotation.Nonnull;
import java.util.Objects;

public class BeaconInfo implements ITileInfo<BeaconTileEntity> {

    private static final IIconStyle SMALL_ICON_STYLE = IIconStyle.createBounds(8, 8).textureBounds(8, 8);
    private static final IIconStyle FULL_ICON_STYLE = IIconStyle.createBounds(18, 18).textureBounds(18, 18);
    private static final ILayoutStyle FULL_LAYOUT_STYLE = Styles.CENTERED.copy().spacing(2);

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData hitData, @Nonnull BeaconTileEntity tile) {
        final int level = tile.getLevels();
        if (level == 0) return;
        probeInfo.text(CompoundText.createLabelInfo("Level: ", level));

        final Effect primaryEffect = Effect.get(tile.beaconData.get(1));
        final Effect secondaryEffect = Effect.get(tile.beaconData.get(2));

        if (primaryEffect != null) {
            final IProbeInfo layout = probeMode == ProbeMode.EXTENDED ? probeInfo : probeInfo.horizontal();
            if (primaryEffect == secondaryEffect)
                drawEffectIcon(layout, primaryEffect, "II", probeMode);
            else {
                drawEffectIcon(layout, primaryEffect, "I", probeMode);
                if (secondaryEffect != null)
                    drawEffectIcon(layout, secondaryEffect, "I", probeMode);
            }
        }
    }

    private void drawEffectIcon(IProbeInfo probeInfo, Effect effect, String level, ProbeMode mode) {
        if (mode == ProbeMode.EXTENDED)
            drawEffectIconAndName(probeInfo, effect, level);
        else
            drawEffectIcon(probeInfo, effect, SMALL_ICON_STYLE);
    }

    private void drawEffectIconAndName(IProbeInfo probeInfo, Effect effect, String level) {
        final IProbeInfo horizontal = probeInfo.horizontal(FULL_LAYOUT_STYLE);
        drawEffectIcon(horizontal, effect, FULL_ICON_STYLE)
            .text(CompoundText.create().text(effect.getDisplayName()).text(" " + level));
    }

    private IProbeInfo drawEffectIcon(IProbeInfo probeInfo, Effect effect, IIconStyle style) {
        return probeInfo.icon(getEffectIconRL(effect), 0, 0, style.getTextureWidth(), style.getTextureHeight(), style);
    }

    private ResourceLocation getEffectIconRL(Effect effect) {
        final String effectName = Objects.requireNonNull(effect.getRegistryName()).getPath();
        return new ResourceLocation("minecraft:textures/mob_effect/" + effectName + ".png");
    }
}
