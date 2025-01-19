package io.github.ignorelicensescn.minimizeFactory.utils.mathutils;

import org.bukkit.Location;

import java.util.Comparator;
import java.util.Objects;

public class BlockGeometry {
    public static int ManhattanDistance(Location l1, Location l2){
        return ManhattanDistance(new BlockLocation(l1),new BlockLocation(l2));
    }
    public static int ManhattanDistance(BlockLocation l1, BlockLocation l2){
        return (int) (Math.abs(l1.x()-l2.x())
                        + Math.abs(l1.y()-l2.y())
                        + Math.abs(l1.z()-l2.z()));
    }

    /**
     * constants generated via python code
     */
    public static BlockLocation[] oneStepFartherFromBase(BlockLocation current,BlockLocation base){
        BlockLocation basePointMoved = current.subtract(base);
        BlockLocationSign extractedSign = basePointMoved.sign();

        BlockLocation[] result = new BlockLocation[extractedSign.moveDirections.length];
        for (int i=0;i< extractedSign.moveDirections.length;i+=1){
            result[i] = current.add(extractedSign.moveDirections[i]);
        }
        return result;
    }

    public static Comparator<Location> getManhattanDistanceBasedComparatorWithLocationBase(Location base){
        return (locA,locB) -> {
            int aX,aY,aZ,bX,bY,bZ;
            aX = locA.getBlockX();
            aY = locA.getBlockY();
            aZ = locA.getBlockZ();
            bX = locB.getBlockX();
            bY = locB.getBlockY();
            bZ = locB.getBlockZ();
            if ((aX==bX) && (aY==bY) && (aZ == bZ)){return 0;}

            int manhattanDistanceA = BlockGeometry.ManhattanDistance(locA,base);
            int manhattanDistanceB = BlockGeometry.ManhattanDistance(locB,base);
            if (manhattanDistanceA != manhattanDistanceB){
                return Integer.compare(manhattanDistanceA,manhattanDistanceB);
            }
            if (aX!=bX){return Integer.compare(aX,bX);}
            if (aZ!=bZ){return Integer.compare(aZ,bZ);}
            return Integer.compare(aY, bY);
        };
    }
    public static Comparator<BlockLocation> getManhattanDistanceBasedComparatorWithLocationBase(BlockLocation base){
        return (locA,locB) -> {
            long aX,aY,aZ,bX,bY,bZ;
            aX = locA.x();
            aY = locA.y();
            aZ = locA.z();
            bX = locB.x();
            bY = locB.y();
            bZ = locB.z();
            if ((aX==bX) && (aY==bY) && (aZ == bZ)){return 0;}

            int manhattanDistanceA = BlockGeometry.ManhattanDistance(locA,base);
            int manhattanDistanceB = BlockGeometry.ManhattanDistance(locB,base);
            if (manhattanDistanceA != manhattanDistanceB){
                return Long.compare(manhattanDistanceA,manhattanDistanceB);
            }
            if (aX!=bX){return Long.compare(aX,bX);}
            if (aZ!=bZ){return Long.compare(aZ,bZ);}
            return Long.compare(aY, bY);
        };
    }

    public static boolean blockLocationEquals(Location l1, Location l2){
        return Objects.equals(l1.getWorld(),l2.getWorld())
                && l1.getBlockX() == l2.getBlockX()
                && l1.getBlockY() == l2.getBlockY()
                && l1.getBlockZ() == l2.getBlockZ();
    }
}
