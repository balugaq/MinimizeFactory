package io.github.ignorelicensescn.minimizefactory.utils.mathinminecraft;

import io.github.ignorelicensescn.minimizefactory.utils.mathutils.Approximation;
import io.github.ignorelicensescn.minimizefactory.utils.mathutils.IntegerRational;
import io.github.ignorelicensescn.minimizefactory.utils.simpleStructure.SimplePair;
import io.github.thebusybiscuit.slimefun4.libraries.dough.collections.RandomizedSet;
import io.github.thebusybiscuit.slimefun4.libraries.dough.collections.WeightedNode;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

import static io.github.ignorelicensescn.minimizefactory.utils.compatibilities.InfinityExpansion.InfinityExpansionConsts.getInUnsafe;

public class RandomizeSetSolving {

    private static Field FIELD_RandomizedSet_internalSet = null;

    @Nonnull
    public static <T> SimplePair<T[], IntegerRational[]> solveRandomizeSet(@Nonnull RandomizedSet<T> randomizedSet){
        T[] result = (T[]) new Object[randomizedSet.size()];
        IntegerRational[] expectations = new IntegerRational[randomizedSet.size()];
        try {
            if (FIELD_RandomizedSet_internalSet == null){
                FIELD_RandomizedSet_internalSet = randomizedSet.getClass().getDeclaredField("internalSet");
            }
            Set<WeightedNode<T>> weightedNodes = (Set<WeightedNode<T>>) getInUnsafe(randomizedSet,FIELD_RandomizedSet_internalSet);

            int minExp = 129;
            for (WeightedNode<T> node:weightedNodes){
                int currentExp = Math.getExponent(node.getWeight());
                minExp = Math.min(minExp,currentExp);
            }


        }catch (Exception e){
            e.printStackTrace();
            int counter = 0;
            for (Map.Entry<T,Float> weightedItem:randomizedSet.toMap().entrySet()){
                result[counter] = weightedItem.getKey();
                expectations[counter] = Approximation.find(weightedItem.getValue());
                counter += 1;
            }
        }
        return new SimplePair<>(result,expectations);
    }
}
