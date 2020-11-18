package io.github.drmanganese.topaddons.addons;

import io.github.drmanganese.topaddons.TopAddons;

import mcjty.theoneprobe.api.IProbeInfoProvider;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public abstract class TopAddon implements IProbeInfoProvider {

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

    @Override
    public String getID() {
        return TopAddons.MOD_ID + ":" + this.name;
    }

    public String getFancyName() {
        return StringUtils.capitalize(name);
    }
}
