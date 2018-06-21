package io.github.drmanganese.topaddons.capabilities;

import java.util.Map;

public interface IClientCfgCapability {

    Map<String, String> getAll();

    void setAll(Map<String, String> newMap);

    String getString(String key);

    int getInt(String key);

    boolean getBool(String key);
}
