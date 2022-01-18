package io.github.drmanganese.topaddons.addons;

import io.github.drmanganese.topaddons.TopAddons;
import io.github.drmanganese.topaddons.api.IAddonBlocks;
import io.github.drmanganese.topaddons.api.IAddonEntities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import mcjty.theoneprobe.api.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.regex.Pattern;

public abstract class TopAddon {

    private static final Pattern VALID_NAME = Pattern.compile("^[a-z][_a-z0-9]+$");
    protected final String name;

    protected TopAddon(final String name) {
        if (VALID_NAME.matcher(name).matches()) {
            this.name = name;
        } else {
            throw new IllegalArgumentException(
                String.format("%s is not a valid addon name. Expected pattern = %s", name, VALID_NAME.pattern())
            );
        }
    }

    public ResourceLocation getID() {
        return new ResourceLocation(TopAddons.MOD_ID, this.name);
    }

    public String getFancyName() {
        return StringUtils.capitalize(name);
    }

    public Optional<BlockInfoProviderWrapper> asBlockInfoProvider() {
        if (this instanceof IAddonBlocks)
            return Optional.of(new BlockInfoProviderWrapper((IAddonBlocks) this, getID()));
        else
            return Optional.empty();
    }

    public Optional<EntityInfoProviderWrapper> asEntityInfoProvider() {
        if (this instanceof IAddonEntities)
            return Optional.of(new EntityInfoProviderWrapper((IAddonEntities) this, getID()));
        else
            return Optional.empty();
    }

    /**
     * Wrapper around {@link IAddonBlocks} to register this addon as an {@link IProbeInfoProvider}.
     * {@link IProbeInfoProvider} can't be implemented directly because its `getID` returns a {@link ResourceLocation}
     * while {@link IProbeInfoEntityProvider} returns a {@link String}. With this wrapper an addon can provide both block and
     * entity info with the same ID.
     */
    final public static class BlockInfoProviderWrapper implements IProbeInfoProvider {

        private final IAddonBlocks provider;
        private final ResourceLocation id;

        public BlockInfoProviderWrapper(IAddonBlocks provider, ResourceLocation id) {
            this.provider = provider;
            this.id = id;
        }

        @Override
        public ResourceLocation getID() {
            return this.id;
        }

        @Override
        public void addProbeInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, Player player, Level level, BlockState blockState, IProbeHitData iProbeHitData) {
            provider.addProbeInfo(probeMode, iProbeInfo, player, level, blockState, iProbeHitData);
        }
    }

    /**
     * Wrapper around {@link IAddonEntities} to register this addon as an {@link IProbeInfoEntityProvider}.
     * {@link IProbeInfoEntityProvider} can't be implemented directly because its `getID` returns a {@link String}
     * while {@link IProbeInfoProvider} returns a {@link ResourceLocation}. With this wrapper an addon can provide both
     * block and entity info with the same ID.
     */
    final public static class EntityInfoProviderWrapper implements IProbeInfoEntityProvider {

        private final IAddonEntities provider;
        private final String id;

        public EntityInfoProviderWrapper(IAddonEntities provider, ResourceLocation id) {
            this.provider = provider;
            this.id = id.toString();
        }

        @Override
        public String getID() {
            return this.id;
        }

        @Override
        public void addProbeEntityInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, Player player, Level level, Entity entity, IProbeHitEntityData iProbeHitEntityData) {
            provider.addProbeEntityInfo(probeMode, iProbeInfo, player, level, entity, iProbeHitEntityData);
        }
    }
}
