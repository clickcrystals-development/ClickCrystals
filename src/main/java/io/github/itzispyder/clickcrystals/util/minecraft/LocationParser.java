package io.github.itzispyder.clickcrystals.util.minecraft;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LocationParser {

    private final double x, y, z;

    public LocationParser(String input) {
        String[] secs = input.replaceAll("[^0-9 -]", "").trim().split(" ");
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;

        for (int i = 0; i < secs.length; i++) {
            if (secs[i].isEmpty()) {
                continue;
            }

            switch (i) {
                case 0 -> x = Double.parseDouble(secs[i]);
                case 1 -> y = Double.parseDouble(secs[i]);
                case 2 -> z = Double.parseDouble(secs[i]);
            }
            if (i >= 3) {
                break;
            }
        }

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public LocationParser(String input, Vec3d relativeTo) {
        String[] secs = input.replaceAll("[^0-9 ~-]", "").trim().split(" ");
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;

        for (int i = 0; i < secs.length; i++) {
            switch (i) {
                case 0 -> {
                    String parsing = secs[i].replaceAll("[^0-9-]", "");
                    if (!parsing.isEmpty()) {
                        x = Double.parseDouble(parsing);
                    }
                    x = secs[i].contains("~") ? relativeTo.getX() + x : x;
                }
                case 1 -> {
                    String parsing = secs[i].replaceAll("[^0-9-]", "");
                    if (!parsing.isEmpty()) {
                        y = Double.parseDouble(parsing);
                    }
                    y = secs[i].contains("~") ? relativeTo.getY() + y : y;
                }
                case 2 -> {
                    String parsing = secs[i].replaceAll("[^0-9-]", "");
                    if (!parsing.isEmpty()) {
                        z = Double.parseDouble(parsing);
                    }
                    z = secs[i].contains("~") ? relativeTo.getZ() + z : z;
                }
            }
            if (i >= 3) {
                break;
            }
        }

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public LocationParser(String x, String y, String z) {
        this(x + " " + y + " " + z);
    }

    public LocationParser(String x, String y, String z, Vec3d relativeTo) {
        this(x + " " + y + " " + z, relativeTo);
    }

    public Vec3d getLocation() {
        return new Vec3d(x, y, z);
    }

    public BlockState getBlock(World world) {
        return world.getBlockState(getBlockLocation());
    }

    public BlockPos getBlockLocation() {
        Vec3d v = getLocation();
        return new BlockPos((int)v.x, (int)v.y, (int)v.z);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}