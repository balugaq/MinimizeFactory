package io.github.ignorelicensescn.minimizefactory.utils.mathutils.mathinminecraft;

import io.github.ignorelicensescn.minimizefactory.utils.mathutils.Approximation;
import io.github.ignorelicensescn.minimizefactory.utils.mathutils.BigRational;
import io.github.ignorelicensescn.minimizefactory.utils.mathutils.IntegerRational;
import io.github.ignorelicensescn.minimizefactory.utils.simpleStructure.SimplePair;
import io.github.thebusybiscuit.slimefun4.libraries.dough.collections.RandomizedSet;
import io.github.thebusybiscuit.slimefun4.libraries.dough.collections.WeightedNode;

import javax.annotation.Nonnull;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static io.github.ignorelicensescn.minimizefactory.utils.EmptyArrays.EMPTY_INTEGER_RATIONAL_ARRAY;
import static io.github.ignorelicensescn.minimizefactory.utils.compatibilities.InfinityExpansion.InfinityExpansionConsts.getInUnsafe;

public class RandomizedSetSolving {

    private static Field FIELD_RandomizedSet_internalSet = null;
    @Nonnull
    public static <T> SimplePair<T[], IntegerRational[]> solveRandomizedSet(@Nonnull RandomizedSet<T> randomizedSet, Class<T> tClass){
        if (randomizedSet.isEmpty()){
            return new SimplePair<>((T[])Array.newInstance(tClass,0),EMPTY_INTEGER_RATIONAL_ARRAY);
        }
        try {
            if (FIELD_RandomizedSet_internalSet == null){
                FIELD_RandomizedSet_internalSet = randomizedSet.getClass().getDeclaredField("internalSet");
            }
            List<WeightedNode<T>> weightedNodes = new ArrayList<>(randomizedSet.size());
            weightedNodes.addAll((Set<WeightedNode<T>>) getInUnsafe(randomizedSet,FIELD_RandomizedSet_internalSet));

            T[] result = (T[]) Array.newInstance(tClass,weightedNodes.size());
            IntegerRational[] expectations = new IntegerRational[weightedNodes.size()];
            BigRational total = BigRational.ZERO;
            BigRational[] rationals = new BigRational[weightedNodes.size()];
            for (int i=0;i<weightedNodes.size();i+=1){
                WeightedNode<T> node = weightedNodes.get(i);
                BigRational currentRational = BigRational.valueOf(node.getWeight());
                rationals[i] = currentRational;
                total = total.add(currentRational);
            }
            for (int i=0;i<weightedNodes.size();i+=1){
                WeightedNode<T> node = weightedNodes.get(i);
                BigRational currentRational = rationals[i].divide(total).simplify();
                if (currentRational.canSaveConvertToIntegerRational()){
                    expectations[i] = currentRational.toIntegerRational();
                }else {
                    //i hope this won't happen
                    expectations[i] = Approximation.find(new BigDecimal(currentRational.numerator()).divide(new BigDecimal(currentRational.denominator()), RoundingMode.HALF_UP).floatValue());
                }
                result[i] = node.getObject();
            }
            return new SimplePair<>(result,expectations);
        }catch (Exception e){
            e.printStackTrace();
            T[] result = (T[]) Array.newInstance(tClass,randomizedSet.size());
            IntegerRational[] expectations = new IntegerRational[randomizedSet.size()];
            int counter = 0;
            for (Map.Entry<T,Float> weightedItem:randomizedSet.toMap().entrySet()){
                result[counter] = weightedItem.getKey();
                expectations[counter] = Approximation.find(weightedItem.getValue());
                counter += 1;
            }
            return new SimplePair<>(result,expectations);
        }
    }
}
