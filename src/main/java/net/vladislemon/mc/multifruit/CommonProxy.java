package net.vladislemon.mc.multifruit;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.vladislemon.mc.multifruit.command.CommandEnchantability;
import net.vladislemon.mc.multifruit.integration.Integrations;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items,
    // etc, and register them with the GameRegistry."
    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes."
    public void init(FMLInitializationEvent event) {
        Integrations.initCommon();
        if (Loader.isModLoaded("IC2")) {
            ItemStack stickyResin = new ItemStack((Item) Item.itemRegistry.getObject("IC2:itemHarz"), 1, 1);
            OreDictionary.registerOre("itemRawRubber", stickyResin);
            MinecraftForge.EVENT_BUS.register(new IC2AchievementFixer());
        }
        if (Loader.isModLoaded("EnderIO")) {
            ItemStack basicGear = new ItemStack((Item) Item.itemRegistry.getObject("EnderIO:itemMachinePart"), 1, 1);
            OreDictionary.registerOre("gearIron", basicGear);
        }
        if (Loader.isModLoaded("ImmersiveEngineering")) {
            ItemStack netherQuartzDust = new ItemStack((Item) Item.itemRegistry.getObject("ImmersiveEngineering:metal"), 1, 18);
            OreDictionary.registerOre("dustNetherQuartz", netherQuartzDust);
        }
        if (Loader.isModLoaded("Magneticraft")) {
            ItemStack netherQuartzDust = new ItemStack((Item) Item.itemRegistry.getObject("Magneticraft:item.dustQuartz"), 1, 1);
            OreDictionary.registerOre("dustNetherQuartz", netherQuartzDust);
        }
    }

    // postInit "Handle interaction with other mods, complete your setup based on this."
    public void postInit(FMLPostInitializationEvent event) {

    }

    public void serverAboutToStart(FMLServerAboutToStartEvent event) {

    }

    // register server commands in this event handler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandEnchantability());
    }

    public void serverStarted(FMLServerStartedEvent event) {

    }

    public void serverStopping(FMLServerStoppingEvent event) {

    }

    public void serverStopped(FMLServerStoppedEvent event) {

    }
}
