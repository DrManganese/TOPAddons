package io.github.drmanganese.topaddons.addons.vanilla.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.styles.Styles;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.styles.IconStyle;

import javax.annotation.Nonnull;
import java.util.Objects;

public class BeaconInfo implements ITileInfo<BeaconBlockEntity> {

    private static final IIconStyle SMALL_ICON_STYLE = new IconStyle().bounds(8, 8).textureBounds(8, 8);
    private static final IIconStyle FULL_ICON_STYLE =  new IconStyle().bounds(18, 18).textureBounds(18, 18);
    private static final ILayoutStyle FULL_LAYOUT_STYLE = Styles.CENTERED.copy().spacing(2);

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData hitData, @Nonnull BeaconBlockEntity tile) {
        final int level = tile.dataAccess.get(0);
        if (level == 0) return;
        probeInfo.text(CompoundText.createLabelInfo("Level: ", level));

        final MobEffect primaryEffect = MobEffect.byId(tile.dataAccess.get(1));
        final MobEffect secondaryEffect = MobEffect.byId(tile.dataAccess.get(2));

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

    private void drawEffectIcon(IProbeInfo probeInfo, MobEffect effect, String level, ProbeMode mode) {
        if (mode == ProbeMode.EXTENDED)
            drawEffectIconAndName(probeInfo, effect, level);
        else
            drawEffectIcon(probeInfo, effect, SMALL_ICON_STYLE);
    }

    private void drawEffectIconAndName(IProbeInfo probeInfo, MobEffect effect, String level) {
        final IProbeInfo horizontal = probeInfo.horizontal(FULL_LAYOUT_STYLE);
        drawEffectIcon(horizontal, effect, FULL_ICON_STYLE)
            .text(CompoundText.create().text(effect.getDisplayName()).text(" " + level));
    }

    private IProbeInfo drawEffectIcon(IProbeInfo probeInfo, MobEffect effect, IIconStyle style) {
        return probeInfo.icon(getEffectIconRL(effect), 0, 0, style.getTextureWidth(), style.getTextureHeight(), style);
    }

    private ResourceLocation getEffectIconRL(MobEffect effect) {
        final String effectName = Objects.requireNonNull(effect.getRegistryName()).getPath();
        return new ResourceLocation("minecraft:textures/mob_effect/" + effectName + ".png");
    }
}
