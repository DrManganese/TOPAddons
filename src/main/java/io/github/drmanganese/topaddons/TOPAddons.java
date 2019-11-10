package io.github.drmanganese.topaddons;

import io.github.drmanganese.topaddons.capabilities.DefaultClientCfgCapability;
import io.github.drmanganese.topaddons.capabilities.DefaultElementSyncCapability;
import io.github.drmanganese.topaddons.capabilities.IClientCfgCapability;
import io.github.drmanganese.topaddons.capabilities.IElementSyncCapability;
import io.github.drmanganese.topaddons.config.ModConfig;
import io.github.drmanganese.topaddons.helmets.RecipeHelmetDeprobify;
import io.github.drmanganese.topaddons.network.PacketHandler;
import io.github.drmanganese.topaddons.proxy.IProxy;
import io.github.drmanganese.topaddons.reference.Reference;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.logging.log4j.Logger;

import static mcjty.theoneprobe.items.ModItems.PROBETAG;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, guiFactory = Reference.GUI_FACTORY, dependencies = "required-after:theoneprobe;")
public class TOPAddons {

    @CapabilityInject(IElementSyncCapability.class)
    public static Capability<IElementSyncCapability> ELT_SYNC_CAP = null;
    @CapabilityInject(IClientCfgCapability.class)
    public static Capability<IClientCfgCapability> CLIENT_CFG_CAP = null;

    @SidedProxy(modId = Reference.MOD_ID, clientSide = Reference.CLIENT_PROXY, serverSide = Reference.COMMON_PROXY)
    public static IProxy PROXY = null;

    public static Configuration CONFIG;
    public static Logger LOGGER;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();
        CONFIG = new Configuration(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(ModConfig.class);
        MinecraftForge.EVENT_BUS.register(this);

        CapabilityManager.INSTANCE.register(IElementSyncCapability.class, new DefaultElementSyncCapability.Storage(), DefaultElementSyncCapability::new);
        CapabilityManager.INSTANCE.register(IClientCfgCapability.class, new DefaultClientCfgCapability.Storage(), DefaultClientCfgCapability::new);

        PacketHandler.init();

        AddonLoader.loadAddons(event.getAsmData());
        PROXY.updateConfigs(CONFIG, AddonLoader.CFG_ADDONS);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        TOPRegistrar.register();
    }


    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.getItem() instanceof ItemArmor
                && ((ItemArmor) stack.getItem()).armorType == EntityEquipmentSlot.HEAD
                && !ModConfig.NEVER_PROBIFY.contains(stack.getItem().getRegistryName().toString())
                && stack.hasTagCompound() && stack.getTagCompound().hasKey(PROBETAG)) {

            event.getToolTip().add(TextFormatting.AQUA + "Probing");
        }
    }

    @SubscribeEvent
    public void onRegistry(RegistryEvent.Register<IRecipe> event) {
        event.getRegistry().register(new RecipeHelmetDeprobify().setRegistryName("deprobify"));
    }
}
