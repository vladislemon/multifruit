package net.vladislemon.mc.multifruit.integration.mapwriter;

import mapwriter.api.MwAPI;

public class MapWriterIntegration {

    public static void init() {
        MwAPI.registerDataProvider("Claimed chunks", new ClaimedChunksDataProvider());
    }
}
