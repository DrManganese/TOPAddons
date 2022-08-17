package io.github.drmanganese.topaddons.addons.industrialforegoing.tiles;

import io.github.drmanganese.topaddons.addons.industrialforegoing.IndustrialForegoingAddon;
import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.config.Config;
import io.github.drmanganese.topaddons.styles.Styles;
import io.github.drmanganese.topaddons.styles.Styles.Colors;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import com.buuz135.industrial.block.generator.tile.MycelialGeneratorTile;
import com.google.common.collect.LinkedHashMultimap;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.block.tile.GeneratorTile;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import mcjty.theoneprobe.api.*;
import org.apache.commons.lang3.ClassUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class IndustrialActiveTileInfo implements ITileInfo<ActiveTile<?>> {

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData hitData, @Nonnull ActiveTile<?> tile) {
        ProgressBars.getForTile(tile)
            // We don't want to see the additional (unused?) bar from Mycelial Generators
            .filter(progressBarHolder -> !(tile instanceof MycelialGeneratorTile && progressBarHolder.fieldName.equals("bar")))
            .forEach(progressBarHolder -> {
                    final ProgressBarComponent<?> progressBar = progressBarHolder.progressBar;

                    if (tile instanceof GeneratorTile<?>) {
                        final GeneratorTile<?> generatorTile = (GeneratorTile<?>) tile;
                        if (progressBar.getProgress() > 0)
                            probeInfo.text(CompoundText.createLabelInfo("{*topaddons:generating*}: ", generatorTile.getEnergyProducedEveryTick()).label(" FE/t"));
                        tinyIndustrialProgressBar(probeInfo, player, probeMode, progressBar);
                        return;
                    }

                    switch (progressBarHolder.fieldName) {
                        case "work":
                        case "workingBar":
                        case "red":   // Mixing
                        case "green": // Mixing
                        case "blue":  // Mixing
                        case "bar":   // Bioreactor
                        case "blaze": // Potion brewer
                            tinyIndustrialProgressBar(probeInfo, player, probeMode, progressBar);
                            break;
                        case "progressBar":
                            industrialProgressBar(probeInfo, player, progressBar);
                            break;
                        case "etherBuffer": // Hydroponics
                            tinyIndustrialProgressBar(probeInfo, player, progressBar, -5385004, -8809324, -8809324 & 0x33ffffff);
                            break;
                        default:
                            break;
                    }
                }
            );
    }

    @SuppressWarnings("unchecked")
    private static void industrialProgressBar(IProbeInfo probeInfo, Player player, ProgressBarComponent<?> progressBar) {
        final Predicate<ActiveTile<?>> canIncrease = (Predicate<ActiveTile<?>>) progressBar.getCanIncrease();
        if (!canIncrease.test((ActiveTile<?>) progressBar.getComponentHarness()) && progressBar.getProgress() == 0) return;
        final Colors colors = Colors.fromDye(progressBar.getColor() == DyeColor.WHITE ? DyeColor.LIGHT_GRAY : progressBar.getColor());
        final IProgressStyle style = Styles.machineProgress(player)
            .filledColor(colors.dyeColor)
            .alternateFilledColor(colors.darkerColor)
            .alignment(ElementAlignment.ALIGN_CENTER);
        final int progressScaled = 100 * progressBar.getProgress() / progressBar.getMaxProgress();
        probeInfo.progress(progressScaled, 100, style);
    }

    private static void tinyIndustrialProgressBar(IProbeInfo probeInfo, Player player, ProbeMode probeMode, ProgressBarComponent<?> progressBar) {
        if (progressBar.getProgress() == 0 && probeMode == ProbeMode.NORMAL) return;
        final Colors colors = Colors.fromDye(progressBar.getColor());
        tinyIndustrialProgressBar(probeInfo, player, progressBar, colors.dyeColor, colors.darkerColor, colors.semiTransparentColor);
    }

    private static void tinyIndustrialProgressBar(IProbeInfo probeInfo, Player player, ProgressBarComponent<?> progressBar, int filledColor, int alternateFilledColor, int backgroundColor) {
        final IProgressStyle style = Styles.machineProgress(player)
            .showText(false)
            .height(probeInfo.defaultProgressStyle().getHeight() / 2)
            .filledColor(filledColor)
            .alternateFilledColor(alternateFilledColor);

        if (Config.getSyncedBoolean(player, IndustrialForegoingAddon.colorTinyProgressBackground))
            style.backgroundColor(backgroundColor);

        probeInfo.progress(progressBar.getProgress(), progressBar.getMaxProgress(), style);
    }

    /**
     * Industrial Foregoing's machines' progress is tracked and updated in private fields. Reflectively get field
     * accessors on demand.
     */
    public static class ProgressBars {

        private static final LinkedHashMultimap<Class<?>, Field> FIELDS = LinkedHashMultimap.create();
        private static final Set<Field> EMPTY_SET = new HashSet<>();
        private static final List<Class> IGNORE = new ArrayList<>();

        public static Stream<ProgressBarHolder> getForTile(BlockEntity tile) {
            return getFieldsForClass(tile.getClass()).stream()
                .map(field -> getProgressBarHolder(field, tile))
                .filter(Optional::isPresent)
                .map(Optional::get);
        }

        private static Optional<ProgressBarHolder> getProgressBarHolder(Field field, BlockEntity tile) {
            try {
                return Optional.ofNullable(field.get(tile))
                    .map(progressBarComponent -> new ProgressBarHolder(field.getName(), (ProgressBarComponent<?>) progressBarComponent));
            } catch (final IllegalAccessException e) {
                return Optional.empty();
            }
        }

        private static Set<Field> getFieldsForClass(Class<?> clazz) {
            if (FIELDS.containsKey(clazz)) {
                return FIELDS.get(clazz);
            } else {
                if (IGNORE.contains(clazz))
                    return EMPTY_SET;
                else {
                    findProgressBarsForClass(clazz);
                    return getFieldsForClass(clazz);
                }
            }
        }

        private static void findProgressBarsForClass(Class<?> clazz) {
            final Predicate<Field> fieldPredicate = field -> field.getType() == ProgressBarComponent.class;
            final List<Field> fields = Stream.concat(
                    ClassUtils.getAllSuperclasses(clazz).stream().map(Class::getDeclaredFields).flatMap(Arrays::stream),
                    Arrays.stream(clazz.getDeclaredFields())
                ).filter(fieldPredicate)
                // Always put "progressBar"s last
                .sorted((f1, f2) ->
                    f1.getName().equals("progressBar") ? 1 : (f2.getName().equals("progressBar") ? -1 : f1.getName().compareTo(f2.getName()))
                ).toList();

            if (fields.isEmpty())
                IGNORE.add(clazz);
            else
                fields.forEach(field -> {
                        field.setAccessible(true);
                        FIELDS.put(clazz, field);
                    }
                );
        }

        private static class ProgressBarHolder {

            final String fieldName;
            final ProgressBarComponent<?> progressBar;

            private ProgressBarHolder(String fieldName, ProgressBarComponent<?> progressBar) {
                this.fieldName = fieldName;
                this.progressBar = progressBar;
            }
        }
    }
}
