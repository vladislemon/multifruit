package net.vladislemon.mc.multifruit;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import ic2.core.IC2;

public class IC2AchievementFixer {

    private final Collection<String> ic2Ores = new ArrayList<>();

    public IC2AchievementFixer() {
        ic2Ores.add("oreCopper");
        ic2Ores.add("oreTin");
        ic2Ores.add("oreUranium");
    }

    @SubscribeEvent
    public void onItemPickup(EntityItemPickupEvent event) {
        for (int oreID : OreDictionary.getOreIDs(event.item.getEntityItem())) {
            if (ic2Ores.contains(OreDictionary.getOreName(oreID))) {
                IC2.achievements.issueAchievement(event.entityPlayer, "mineOre");
            }
        }
    }
}
