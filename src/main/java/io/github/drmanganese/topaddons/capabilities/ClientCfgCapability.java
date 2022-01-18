package io.github.drmanganese.topaddons.capabilities;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Player capability which holds client-only configuration settings on the server. These options need to be synced
 * because probe information is created on the server-side.
 * <p>
 * All values are stored as strings.
 */
public final class ClientCfgCapability {

    private final Map<String, String> valueMap = new HashMap<>();

    public Map<String, String> getAll() {
        return this.valueMap;
    }

    public void copy(ClientCfgCapability capability) {
        fromMap(capability.getAll());
    }

    public void fromMap(Map<String, String> map) {
        this.valueMap.clear();
        this.valueMap.putAll(map);
    }

    public Optional<String> getString(@Nonnull String key) {
        return Optional.of(key).filter(valueMap::containsKey).map(valueMap::get);
    }

    public Optional<Integer> getInt(@Nonnull String key) {
        return getString(key).map(Integer::parseInt);
    }

    public Optional<Boolean> getBool(@Nonnull String key) {
        return getString(key).map(ClientCfgCapability::stringToBoolean);
    }
    
    public <E extends Enum<E>> Optional<E> getEnum(@Nonnull String key, Class<E> enumClass) {
        return getString(key).map(v -> Enum.valueOf(enumClass, v));
    }

    private static Boolean stringToBoolean(String s) {
        return s.equals("true");
    }
}
