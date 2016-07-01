package io.github.drmanganese.topplugins.api;

import java.util.List;

import mcjty.theoneprobe.api.IProbeConfigProvider;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.IProbeInfoProvider;

public interface ITOPPlugin extends IProbeInfoProvider, IProbeInfoEntityProvider, IProbeConfigProvider {

    boolean hasHelmets();

    List<Class<? extends ItemArmorProbed>> getHelmets();
}
