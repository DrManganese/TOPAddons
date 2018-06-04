package io.github.drmanganese.topaddons.api;

import javax.annotation.Nonnull;

public interface IAddonBlocksAndEntities extends IAddonBlocks, IAddonEntities {

    @Nonnull
    @Override
    default String getID() {
        return IAddonBlocks.super.getID();
    }
}
