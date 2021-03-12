package io.github.drmanganese.topaddons.addons.industrialforegoing.tiles;

import io.github.drmanganese.topaddons.api.ITileInfo;
import io.github.drmanganese.topaddons.styles.Styles;
import io.github.drmanganese.topaddons.styles.Styles.Colors;
import io.github.drmanganese.topaddons.util.InfoHelper;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.buuz135.industrial.block.generator.tile.MycelialGeneratorTile;
import com.google.common.collect.LinkedHashMultimap;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.api.ProbeMode;
import org.apache.commons.lang3.ClassUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IndustrialActiveTileInfo implements ITileInfo<ActiveTile<?>> {

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData hitData, @Nonnull ActiveTile<?> tile) {
        ProgressBars.getForTile(tile)
            // We don't want to see the additional (unused?) bar from Mycelial Generators
            .filter(progressBarHolder -> !(tile instanceof MycelialGeneratorTile && progressBarHolder.fieldName.equals("bar")))
            .forEach(progressBarHolder -> {
                    final ProgressBarComponent<?> progressBar = progressBarHolder.progressBar;

                    switch (progressBarHolder.fieldName) {
                        case "work":
                        case "workingBar":
                        case "red":   // Mixing
                        case "green": // Mixing
                        case "blue":  // Mixing
                        case "bar":   // Bioreactor
                        case "blaze": // Potion brewer
                            tinyIndustrialProgressBar(probeInfo, player, progressBar);
                            break;
                        case "progressBar":
                            industrialProgressBar(probeInfo, player, progressBar, "Progress: ");
                            break;
                        case "etherBuffer": // Hydroponics
                            tinyIndustrialProgressBar(probeInfo, player, progressBar, -5385004, -8809324);
                            break;
                        default:
                            break;
                    }
                }
            );
    }

    private static void industrialProgressBar(IProbeInfo probeInfo, PlayerEntity player, ProgressBarComponent<?> progressBar, String prefix) {
        final Colors colors = Colors.fromDye(progressBar.getColor() == DyeColor.WHITE ? DyeColor.LIGHT_GRAY : progressBar.getColor());
        final IProgressStyle style = Styles.machineProgress(player)
            .filledColor(colors.dyeColor)
            .alternateFilledColor(colors.darkerColor)
            .prefix(prefix);
        InfoHelper.progressCenteredScaled(probeInfo, player, progressBar.getProgress(), progressBar.getMaxProgress(), 100, style, null);
    }

    private static void tinyIndustrialProgressBar(IProbeInfo probeInfo, PlayerEntity player, ProgressBarComponent<?> progressBar) {
        final Colors colors = Colors.fromDye(progressBar.getColor());
        tinyIndustrialProgressBar(probeInfo, player, progressBar, colors.dyeColor, colors.darkerColor);
    }

    private static void tinyIndustrialProgressBar(IProbeInfo probeInfo, PlayerEntity player, ProgressBarComponent<?> progressBar, int filledColor, int alternateFilledColor) {
        final IProgressStyle style = Styles.machineProgress(player)
            .showText(false)
            .height(probeInfo.defaultProgressStyle().getHeight() / 2)
            .filledColor(filledColor)
            .alternateFilledColor(alternateFilledColor);
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

        public static Stream<ProgressBarHolder> getForTile(TileEntity tile) {
            return getFieldsForClass(tile.getClass()).stream()
                .map(field -> getProgressBarHolder(field, tile))
                .filter(Optional::isPresent)
                .map(Optional::get);
        }

        private static Optional<ProgressBarHolder> getProgressBarHolder(Field field, TileEntity tile) {
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
                )
                .collect(Collectors.toList());

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
