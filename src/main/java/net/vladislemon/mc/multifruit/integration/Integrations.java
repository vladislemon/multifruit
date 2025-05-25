package net.vladislemon.mc.multifruit.integration;

import net.vladislemon.mc.multifruit.MultiFruit;
import net.vladislemon.mc.multifruit.integration.mapwriter.MapWriterIntegration;

public class Integrations {

    public static void initCommon() {}

    public static void initClient() {
        try {
            MapWriterIntegration.init();
        } catch (Throwable e) {
            MultiFruit.warn("Unable to load MapWriter integration: " + e);
        }
    }
}
