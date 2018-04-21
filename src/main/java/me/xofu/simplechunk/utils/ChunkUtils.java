package me.xofu.simplechunk.utils;

import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class ChunkUtils {

    public static List<Location> getChunkCorners(Chunk chunk) {
        int x = chunk.getX() << 4;
        int z = chunk.getZ() << 4;

        Location loc1 = new Location(chunk.getWorld(), x, chunk.getWorld().getHighestBlockAt(x, z).getY(), z);
        Location loc2 = new Location(chunk.getWorld(), x + 15, chunk.getWorld().getHighestBlockAt(x + 15, z + 15).getY(), z + 15);
        Location loc3 = new Location(chunk.getWorld(), x + 15, chunk.getWorld().getHighestBlockAt(x + 15, z).getY(), z);
        Location loc4 = new Location(chunk.getWorld(), x, chunk.getWorld().getHighestBlockAt(x, z + 15).getY(), z + 15);

        List<Location> corners = new ArrayList<>();

        corners.add(loc1);
        corners.add(loc2);
        corners.add(loc3);
        corners.add(loc4);
        return corners;
    }

    public static int getExactCoordinate(int coordinate) {
        return coordinate << 4;
    }
}
