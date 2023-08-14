package net.vladislemon.mc.multifruit.mapwriter;

import ftb.utils.mod.client.gui.claims.ClaimedAreasClient;
import ftb.utils.net.MessageAreaRequest;
import ftb.utils.net.MessageClaimChunk;
import ftb.utils.world.LMPlayer;
import ftb.utils.world.LMWorldClient;
import ftb.utils.world.claims.ChunkType;
import mapwriter.api.IMwChunkOverlay;
import mapwriter.api.IMwDataProvider;
import mapwriter.map.MapView;
import mapwriter.map.mapmode.FullScreenMapMode;
import mapwriter.map.mapmode.MapMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;

public class ClaimedChunksDataProvider implements IMwDataProvider {
    private static final int MAX_AREA_SIZE = 128;
    private static final int AREA_MIN_UPDATE_INTERVAL_MILLIS = 10000;
    private static final int COLOR_TEXT_OVER_MOUSE = 0xFFFFFFFF;
    private long lastTimeAreaRequested;
    private boolean requestAreaUpdate;

    @Override
    public ArrayList<IMwChunkOverlay> getChunksOverlay(int dim, double centerX, double centerZ, double minX, double minZ, double maxX, double maxZ) {
        int minChunkX = ((int) Math.floor(minX)) >> 4;
        int minChunkZ = ((int) Math.floor(minZ)) >> 4;
        int maxChunkX = ((int) Math.ceil(maxX)) >> 4;
        int maxChunkZ = ((int) Math.ceil(maxZ)) >> 4;
        int width = maxChunkX - minChunkX;
        int height = maxChunkZ - minChunkZ;
        if (width > MAX_AREA_SIZE || height > MAX_AREA_SIZE) {
            return null;
        }
        long time = System.currentTimeMillis();
        if (requestAreaUpdate && time - lastTimeAreaRequested > AREA_MIN_UPDATE_INTERVAL_MILLIS) {
            requestAreaUpdate = false;
            lastTimeAreaRequested = time;
            new MessageAreaRequest(minChunkX, minChunkZ, width, height).sendToServer();
        }
        ArrayList<IMwChunkOverlay> result = new ArrayList<>();
        for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
            for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
                ChunkType chunkType = ClaimedAreasClient.getTypeE(chunkX, chunkZ);
                result.add(new ClaimOverlay(chunkX, chunkZ, chunkType));
            }
        }
        return result;
    }

    @Override
    public String getStatusString(int dim, int bX, int bY, int bZ) {
        ChunkType chunkType = ClaimedAreasClient.getTypeE(bX >> 4, bZ >> 4);
        String status = ", " + chunkType.getIDS();
        if (chunkType.isClaimed()) {
            //noinspection DataFlowIssue
            status += " (" + getOwner(chunkType).getPlayer().getDisplayName() + ")";
        }
        return status;
    }

    @Override
    public void onMiddleClick(int dim, int bX, int bZ, MapView mapView) {
        int chunkX = bX >> 4;
        int chunkZ = bZ >> 4;
        ChunkType chunkType = ClaimedAreasClient.getTypeE(chunkX, chunkZ);
        if (chunkType == ChunkType.WILDERNESS) {
            changeChunkType(dim, chunkX, chunkZ, MessageClaimChunk.ID_CLAIM);
        } else if (LMWorldClient.inst.clientPlayer.equalsPlayer(getOwner(chunkType))) {
            if (chunkType != ChunkType.LOADED_SELF) {
                changeChunkType(dim, chunkX, chunkZ, MessageClaimChunk.ID_LOAD);
            } else {
                changeChunkType(dim, chunkX, chunkZ, MessageClaimChunk.ID_UNCLAIM);
            }
        }
    }

    @Override
    public void onDimensionChanged(int dimension, MapView mapView) {
        requestAreaUpdate = true;
    }

    @Override
    public void onMapCenterChanged(double vX, double vZ, MapView mapView) {
        requestAreaUpdate = true;
    }

    @Override
    public void onZoomChanged(int level, MapView mapView) {
        requestAreaUpdate = true;
    }

    @Override
    public void onOverlayActivated(MapView mapView) {
        requestAreaUpdate = true;
    }

    @Override
    public void onOverlayDeactivated(MapView mapView) {
    }

    @Override
    public void onDraw(MapView mapView, MapMode mapMode) {
        if (!(mapMode instanceof FullScreenMapMode)) {
            return;
        }
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        Point blockPos = mapMode.screenXYtoBlockXZ(
                mapView,
                Mouse.getX() / 2,
                (Minecraft.getMinecraft().displayHeight - Mouse.getY()) / 2
        );
        int screenX = (Mouse.getX() * 2 - Minecraft.getMinecraft().displayWidth) / 4;
        int screenY = (Minecraft.getMinecraft().displayHeight - Mouse.getY() * 2) / 4;
        ChunkType chunkType = ClaimedAreasClient.getTypeE(blockPos.x >> 4, blockPos.y >> 4);
        LMPlayer owner = getOwner(chunkType);
        if (owner != null) {
            String ownerName = owner.getPlayer().getDisplayName();
            fontRenderer.drawStringWithShadow(ownerName, screenX + 5, screenY - 5, COLOR_TEXT_OVER_MOUSE);
        }
    }

    @Override
    public boolean onMouseInput(MapView mapView, MapMode mapMode) {
        return false;
    }

    private static LMPlayer getOwner(ChunkType chunkType) {
        if (chunkType.isClaimed()) {
            return ((ChunkType.PlayerClaimed) chunkType).chunkOwner;
        } else if (chunkType == ChunkType.LOADED_SELF) {
            return LMWorldClient.inst.clientPlayer;
        }
        return null;
    }

    private static void changeChunkType(int dimension, int x, int z, int type) {
        new MessageClaimChunk(dimension, 0, x, z, type).sendToServer();
        new MessageAreaRequest(x, z, 1, 1).sendToServer();
    }

    private static class ClaimOverlay implements IMwChunkOverlay {
        private static final int COLOR_FILL_MASK = 0x77FFFFFF;
        private final Point pos;
        private final int color;

        private ClaimOverlay(int chunkX, int chunkZ, ChunkType chunkType) {
            this.pos = new Point(chunkX, chunkZ);
            this.color = chunkType.getAreaColor(LMWorldClient.inst.clientPlayer);
        }

        @Override
        public Point getCoordinates() {
            return pos;
        }

        @Override
        public int getColor() {
            return color & COLOR_FILL_MASK;
        }

        @Override
        public float getFilling() {
            return 1.0f;
        }

        @Override
        public boolean hasBorder() {
            return true;
        }

        @Override
        public float getBorderWidth() {
            return 1;
        }

        @Override
        public int getBorderColor() {
            return color;
        }
    }
}
