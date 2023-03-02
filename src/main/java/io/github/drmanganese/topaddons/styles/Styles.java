package io.github.drmanganese.topaddons.styles;

import io.github.drmanganese.topaddons.addons.forge.ForgeAddon;
import io.github.drmanganese.topaddons.config.Config;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.material.MaterialColor;

import com.google.common.collect.ImmutableMap;
import mcjty.theoneprobe.api.Color;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.ILayoutStyle;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Styles {

    public static final ILayoutStyle CENTERED = new LayoutStyle().alignment(ElementAlignment.ALIGN_CENTER);

    private static final ImmutableMap<DyeColor, Colors> DYE_COLORS = ImmutableMap
        .copyOf(Arrays.stream(DyeColor.values()).collect(Collectors.toMap(Function.identity(), Colors::new)));

    private static final IProgressStyle MACHINE_PROGRESS_STYLE = new ProgressStyle().suffix("%");

    public static IProgressStyle machineProgress(Player player) {
        return machineProgress(player, "topaddons:progress");
    }

    public static IProgressStyle machineProgress(Player player, String prefixWord) {
        final int borderColor = Config.getSyncedColor(player, ForgeAddon.machineProgressBorderColor);
        final int backgroundColor = Config.getSyncedColor(player, ForgeAddon.machineProgressBackgroundColor);
        return MACHINE_PROGRESS_STYLE
            .copy()
            .borderColor(borderColor)
            .backgroundColor(backgroundColor)
            .prefix(Component.translatable(prefixWord).append(": "));
    }

    public static class Colors {
        public final int color;
        public final int darkerColor;
        public final int semiTransparentColor;

        private Colors(DyeColor color) {
            this(color.getTextColor() | 0xff000000);
        }

        public Colors(int color) {
            this.color = color;
            this.darkerColor = new Color(this.color).darker().hashCode();
            this.semiTransparentColor = this.darkerColor & 0x33ffffff;
        }

        public Colors(MaterialColor color) {
            this(color.col | 0xff000000);
        }

        public static Colors fromDye(DyeColor dyeColor) {
            return DYE_COLORS.get(dyeColor);
        }
    }
}
