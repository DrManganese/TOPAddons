package io.github.drmanganese.topaddons.styles;

import io.github.drmanganese.topaddons.addons.forge.ForgeAddon;
import io.github.drmanganese.topaddons.config.Config;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.util.text.TranslationTextComponent;

import com.google.common.collect.ImmutableMap;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.ILayoutStyle;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;

import java.awt.*;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Styles {

    public static final ILayoutStyle CENTERED = new LayoutStyle().alignment(ElementAlignment.ALIGN_CENTER);

    private static final ImmutableMap<DyeColor, Colors> DYE_COLORS = ImmutableMap
        .copyOf(Arrays.stream(DyeColor.values()).collect(Collectors.toMap(Function.identity(), Colors::new)));

    private static final IProgressStyle MACHINE_PROGRESS_STYLE = new ProgressStyle().suffix("%");

    public static IProgressStyle machineProgress(PlayerEntity player) {
        return machineProgress(player, "topaddons:progress");
    }

    public static IProgressStyle machineProgress(PlayerEntity player, String prefixWord) {
        final int borderColor = Config.getSyncedColor(player, ForgeAddon.machineProgressBorderColor);
        final int backgroundColor = Config.getSyncedColor(player, ForgeAddon.machineProgressBackgroundColor);
        return MACHINE_PROGRESS_STYLE
            .copy()
            .borderColor(borderColor)
            .backgroundColor(backgroundColor)
            .prefix(new TranslationTextComponent(prefixWord).appendString(": "));
    }

    public static class Colors {
        public final DyeColor dye;
        public final int dyeColor;
        public final int darkerColor;
        public final int semiTransparentColor;

        private Colors(DyeColor dyeColor) {
            this.dye = dyeColor;
            this.dyeColor = dyeColor.getColorValue() | 0xff000000;
            this.darkerColor = new Color(this.dyeColor).darker().hashCode();
            this.semiTransparentColor = this.darkerColor & 0x33ffffff;
        }

        public static Colors fromDye(DyeColor dyeColor) {
            return DYE_COLORS.get(dyeColor);
        }
    }
}
