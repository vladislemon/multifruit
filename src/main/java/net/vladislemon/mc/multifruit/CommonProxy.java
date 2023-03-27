package net.vladislemon.mc.multifruit;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items,
    // etc, and register them with the GameRegistry."
    public void preInit(FMLPreInitializationEvent event) 	{
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes."
    public void init(FMLInitializationEvent event) {
        if (Loader.isModLoaded("IC2")) {
            ItemStack stickyResin = new ItemStack((Item) Item.itemRegistry.getObject("IC2:itemHarz"), 1, 1);
            OreDictionary.registerOre("itemRawRubber", stickyResin);
        }
        if (Loader.isModLoaded("EnderIO")) {
            ItemStack basicGear = new ItemStack((Item) Item.itemRegistry.getObject("EnderIO:itemMachinePart"), 1, 1);
            OreDictionary.registerOre("gearIron", basicGear);
        }
    }

    // postInit "Handle interaction with other mods, complete your setup based on this."
    public void postInit(FMLPostInitializationEvent event) {

    }

    public void serverAboutToStart(FMLServerAboutToStartEvent event) {

    }

    // register server commands in this event handler
    public void serverStarting(FMLServerStartingEvent event) {

    }

    public void serverStarted(FMLServerStartedEvent event) {

    }

    public void serverStopping(FMLServerStoppingEvent event) {

    }

    public void serverStopped(FMLServerStoppedEvent event) {

    }
}
