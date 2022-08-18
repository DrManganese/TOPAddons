package io.github.drmanganese.topaddons.addons.forge;

import io.github.drmanganese.topaddons.TopAddons;
import io.github.drmanganese.topaddons.addons.TopAddon;
import io.github.drmanganese.topaddons.addons.forge.tiles.FluidHandlerTileInfo;
import io.github.drmanganese.topaddons.api.*;
import io.github.drmanganese.topaddons.config.ColorValue;
import io.github.drmanganese.topaddons.config.Config;
import io.github.drmanganese.topaddons.elements.forge.FluidGaugeElement;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import mcjty.theoneprobe.api.IElement;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class ForgeAddon extends TopAddon implements IAddonBlocks, IAddonElements, IAddonConfig, IAddonConfigProviders {

    public static final ResourceLocation GAUGE_ELEMENT_ID = new ResourceLocation(TopAddons.MOD_ID, "forge.fluid_gauge");

    // Client
    public static ForgeConfigSpec.EnumValue<FluidTextureAlignment> gaugeFluidTextureAlignment;
    public static ForgeConfigSpec.EnumValue<FluidColorAlgorithm> gaugeFluidColorAlgorithm;
    public static ForgeConfigSpec.IntValue gaugeFluidColorTransparency;
    public static ForgeConfigSpec.BooleanValue gaugeRenderFluidTexture;
    public static ForgeConfigSpec.BooleanValue gaugeShowCapacity;
    public static ForgeConfigSpec.BooleanValue gaugeRounded;
    public static ColorValue gaugeBackgroundColor;
    public static ColorValue gaugeBorderColor;

    // Synced
    public static ForgeConfigSpec.EnumValue<FluidGaugeChoice> fluidGaugeChoice;
    public static ForgeConfigSpec.BooleanValue gaugeShowCompactTop;
    public static ForgeConfigSpec.BooleanValue gaugeUseCustomTankNames;
    public static ForgeConfigSpec.BooleanValue gaugeHideEmptyTanks;
    public static ColorValue machineProgressBackgroundColor;
    public static ColorValue machineProgressBorderColor;

    // Common
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> gaugeModBlacklist;
    private static final List<? extends String> DEFAULT_BLACKLIST = Arrays.asList("mekanism", "mekanismgenerators", "fluidtank");


    public ForgeAddon() {
        super("forge");
    }

    @Override
    public List<Pair<ResourceLocation, Function<FriendlyByteBuf, IElement>>> getElements() {
        return Collections.singletonList(Pair.of(GAUGE_ELEMENT_ID, FluidGaugeElement::new));
    }

    @Nonnull
    @Override
    public ImmutableMultimap<Class<? extends BlockEntity>, ITileInfo> getTileInfos() {
        return ImmutableMultimap.of(BlockEntity.class, FluidHandlerTileInfo.INSTANCE);
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder, ModConfig.Type type) {
        builder.push(name);
        if (type == ModConfig.Type.CLIENT) {
            builder.push("fluidGauge");
            gaugeRounded = builder.comment("Show a rounded tank fluid gauge").define("gaugeRounded", true);
            gaugeBackgroundColor = new ColorValue(builder.comment("Fluid gauge background color (try #557F0000 for BC red)").define("gaugeBackgroundColor", "#55666666", ColorValue::test));
            gaugeBorderColor = new ColorValue(builder.comment("Fluid gauge border color (try #FF7F0000 for BC red)").define("gaugeBorderColor", "#ff666666", ColorValue::test));
            gaugeUseCustomTankNames = builder.comment("Allow certain tiles to show custom tank names").define("gaugeUseCustomTankNames", true);
            gaugeFluidColorAlgorithm = builder.comment("Which \"algorithm\" should be used to pick fluid colors (TOP_LEFT is sometimes lighter)").defineEnum("gaugeFluidColorAlgorithm", FluidColorAlgorithm.AVERAGE_COLOR);
            fluidGaugeChoice = builder.comment("Which fluid gauges to show, BOTH and THE_ONE_PROBE_ONLY options also depend on the The One Probe \"showTankSetting\" configuration").defineEnum("fluidGaugeChoice", FluidGaugeChoice.TOP_ADDONS_ONLY);
            gaugeShowCapacity = builder.comment("Show the tank's total capacity in the fluid gauge").define("gaugeShowCapacity", true);
            gaugeFluidColorTransparency = builder.comment("Fluid color transparency.").defineInRange("gaugeFluidColorTransparency", 255, 0, 255);
            gaugeRenderFluidTexture = builder.comment("Use the fluid's texture in the fluid gauge instead of the TOP lines.").define("gaugeRenderFluidTexture", true);
            gaugeFluidTextureAlignment = builder.comment("Alignment of the fluid's texture when gaugeRenderFluidTexture is enabled.").defineEnum("fluidTextureAlignment", FluidTextureAlignment.MIDDLE);
            gaugeShowCompactTop = builder.comment("When 'fluidGaugeChoice' is set to THE_ONE_PROBE_ONLY show a compact version of it's tank gauge.").define("gaugeShowCompactTop", true);
            gaugeHideEmptyTanks = builder.comment("Hide empty thanks when displaying the TOP Addons fluid gauge (same behaviour as vanilla TOP).").define("gaugeHideEmptyTanks", false);
            builder.pop();
            machineProgressBackgroundColor = new ColorValue(builder.comment("Machine progress bar background color").define("machineProgressBackgroundColor", "#55666666", ColorValue::test));
            machineProgressBorderColor = new ColorValue(builder.comment("Machine progress bar border color").define("machineProgressBorderColor", "#ff666666", ColorValue::test));
        }

        if (type == ModConfig.Type.COMMON)
            gaugeModBlacklist = builder.comment("List of mod IDs for which no TOP Addons fluid gauge should be shown").defineList("gaugeModBlacklist", DEFAULT_BLACKLIST, o -> true);
        builder.pop();
    }

    @Override
    public List<ForgeConfigSpec.ConfigValue<?>> getClientConfigValuesToSync() {
        return Lists.newArrayList(
            fluidGaugeChoice,
            machineProgressBackgroundColor.configValue,
            machineProgressBorderColor.configValue,
            gaugeUseCustomTankNames,
            gaugeShowCompactTop,
            gaugeHideEmptyTanks
        );
    }

    @Override
    @Nonnull
    public ImmutableMap<Object, ITileConfigProvider> getBlockConfigProviders() {
        return ImmutableMap.of(BlockEntity.class, FluidHandlerTileInfo.INSTANCE);
    }

    public enum FluidGaugeChoice {
        BOTH(false, false),
        THE_ONE_PROBE_ONLY(false, true),
        TOP_ADDONS_ONLY(true, false);

        public final boolean hideOriginal;
        public final boolean hideTopAddonsGauge;

        FluidGaugeChoice(boolean hideOriginal, boolean hideTopAddonsGauge) {
            this.hideOriginal = hideOriginal;
            this.hideTopAddonsGauge = hideTopAddonsGauge;
        }

        public static FluidGaugeChoice getSyncedValueFor(Player player) {
            return (FluidGaugeChoice) Config.getSyncedEnum(player, ForgeAddon.fluidGaugeChoice);
        }
    }

    public enum FluidColorAlgorithm {
        TOP_LEFT_COLOR,
        AVERAGE_COLOR;
    }

    /**
     * TOP: Align the texture at the top and cut off the overflow
     * MIDDLE: Align the texture in the middle of the gauge and cut off the overflow
     * SQUEEZE: Vertically squeeze in the full texture in the gauge
     */
    public enum FluidTextureAlignment {
        TOP((minV, maxV, vSpace) -> minV, (minV, maxV, vSpace) -> minV + vSpace),
        MIDDLE(
            (minV, maxV, vSpace) -> minV + (maxV - minV - vSpace) / 2,
            (minV, maxV, vSpace) -> maxV - (maxV - minV - vSpace) / 2
        ),
        SQUEEZE((minV, maxV, vSpace) -> minV, (minV, maxV, vSpace) -> maxV);

        /**
         * Functions for calculating the UV ordinate to use when rendering the fluid gauge with a texture.
         */
        public TriFunction<Float, Float, Float, Float> fv1, fv2;

        FluidTextureAlignment(TriFunction<Float, Float, Float, Float> fv1, TriFunction<Float, Float, Float, Float> fv2) {
            this.fv1 = fv1;
            this.fv2 = fv2;
        }

        @FunctionalInterface
        public interface TriFunction<A, B, C, R> {
            R apply(A a, B b, C c);
        }
    }
}
