package io.github.drmanganese.topaddons.config;

import io.github.drmanganese.topaddons.AddonRegistry;
import io.github.drmanganese.topaddons.TopAddons;
import io.github.drmanganese.topaddons.api.IAddonConfig;
import io.github.drmanganese.topaddons.capabilities.ClientCfgCapability;
import io.github.drmanganese.topaddons.network.PacketHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = TopAddons.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class Config {

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;
    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

    static {
        AddonRegistry.getAddonConfigStream().forEachOrdered(a -> a.buildConfig(COMMON_BUILDER, ModConfig.Type.COMMON));
        COMMON_CONFIG = COMMON_BUILDER.build();

        AddonRegistry.getAddonConfigStream().forEachOrdered(a -> a.buildConfig(CLIENT_BUILDER, ModConfig.Type.CLIENT));
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    @SubscribeEvent
    public static void onModConfigReloading(ModConfigEvent.Reloading modConfigEvent) {
        if (modConfigEvent.getConfig().getType() == ModConfig.Type.CLIENT && Minecraft.getInstance().level != null)
            PacketHandler.sendClientCfg(collectClientConfigValues());
    }

    public static Map<String, String> collectClientConfigValues() {
        return AddonRegistry.getAddonConfigStream()
            .map(IAddonConfig::getClientConfigValuesToSync)
            .flatMap(List::stream)
            .collect(Collectors.toMap(Config::fullPathFromConfigValue, Config::configValueToString));
    }

    public static boolean getSyncedBoolean(Player player, ForgeConfigSpec.BooleanValue configValue) {
        return getSynced(player, configValue, ClientCfgCapability::getBool);
    }

    public static String getSyncedString(Player player, ForgeConfigSpec.ConfigValue<String> configValue) {
        return getSynced(player, configValue, ClientCfgCapability::getString);
    }

    public static int getSyncedColor(Player player, ColorValue colorValue) {
        return ColorValue.decodeString(getSyncedString(player, colorValue.configValue));
    }

    public static <E extends Enum<E>> Enum<E> getSyncedEnum(Player player, ForgeConfigSpec.EnumValue<E> configValue) {
        final BiFunction<ClientCfgCapability, String, Optional<E>> clientCfgGetter = (cap, key) -> cap.getEnum(key, configValue.get().getDeclaringClass());
        return getSynced(player, configValue, clientCfgGetter);
    }

    private static <T> T getSynced(Player player, ForgeConfigSpec.ConfigValue<T> configValue, BiFunction<ClientCfgCapability, String, Optional<T>> clientCfgGetter) {
        final String path = fullPathFromConfigValue(configValue);
        final Optional<T> syncedValue = player.getCapability(TopAddons.CLIENT_CFG_CAP).map(cap -> clientCfgGetter.apply(cap, path)).orElse(Optional.empty());

        return syncedValue.orElse(configValue.get()); // Fall back to common setting if not set on client somehow.
    }

    private static String fullPathFromConfigValue(ForgeConfigSpec.ConfigValue<?> configValue) {
        return String.join(".", configValue.getPath());
    }

    private static String configValueToString(ForgeConfigSpec.ConfigValue<?> configValue) {
        return configValue.get().toString();
    }
}
