package io.github.ignorelicensescn.minimizefactory.utils.mathinminecraft;

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
import java.math.BigInteger;
import java.util.*;
import java.util.logging.Level;

import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.logger;
import static io.github.ignorelicensescn.minimizefactory.utils.compatibilities.InfinityExpansion.InfinityExpansionConsts.getInUnsafe;

public class RandomizedSetSolving {

    private static Field FIELD_RandomizedSet_internalSet = null;
    public static final Map<float[],IntegerRational[]> calculatedMap = new HashMap<>(128);

    @Nonnull
    public static <T> SimplePair<T[], IntegerRational[]> solveRandomizedSet(@Nonnull RandomizedSet<T> randomizedSet, Class<T> tClass){

        try {
            if (FIELD_RandomizedSet_internalSet == null){
                FIELD_RandomizedSet_internalSet = randomizedSet.getClass().getDeclaredField("internalSet");
            }
            Set<WeightedNode<T>> weightedNodes = (Set<WeightedNode<T>>) getInUnsafe(randomizedSet,FIELD_RandomizedSet_internalSet);

            List<WeightedNode<T>> weightedNodesCleared = new ArrayList<>(weightedNodes.size());
            for (WeightedNode<T> node:weightedNodes){
                if (node.getWeight() != 0){
                    weightedNodesCleared.add(node);
                }
            }

            T[] result = (T[]) Array.newInstance(tClass,weightedNodesCleared.size());
            IntegerRational[] expectations = new IntegerRational[weightedNodesCleared.size()];
            int minExp = 129;
            float[] key = new float[weightedNodesCleared.size()];
            int keyCounter = 0;
            for (WeightedNode<T> node:weightedNodesCleared){
                result[keyCounter] = node.getObject();
                int currentExp = Math.getExponent(node.getWeight());
                minExp = Math.min(minExp,currentExp);
                key[keyCounter] = node.getWeight();
                keyCounter += 1;
            }
            Arrays.sort(key);
            IntegerRational[] tryGetResult = calculatedMap.getOrDefault(key,null);
            if (tryGetResult != null){
                return new SimplePair<>(result,tryGetResult);
            }
            BigInteger gcd = BigInteger.ZERO;
            int counter = 0;
            BigInteger[] nums = new BigInteger[weightedNodesCleared.size()];
            BigInteger total = BigInteger.ZERO;

            BigInteger binaryZeroes = minExp >= 0 ?BigInteger.TWO.pow(minExp):BigInteger.TWO.pow(minExp*(-1));

            //            logger.log(Level.WARNING,"solving set"+weightedNodesCleared);
            for (WeightedNode<T> node:weightedNodesCleared){
//                logger.log(Level.WARNING, Integer.toBinaryString(Float.floatToRawIntBits(node.getWeight())));
                int intPart = (int) (node.getWeight() / Math.pow(2.,minExp));
                BigInteger current = BigInteger.valueOf(intPart);
                if(minExp >=0){
                    current = current.divide(binaryZeroes);
                }else {
                    current = current.multiply(binaryZeroes);
                }
                nums[counter] = current;
                total = total.add(current);

                counter+=1;

                if (gcd.equals(BigInteger.ONE)){
                    continue;
                }
                if (gcd.equals(BigInteger.ZERO)){
                    gcd = current;
                }else {
                    gcd = current.gcd(gcd);
                }
            }
            total = total.divide(gcd);
            for (int i=0;i<counter;i+=1){
                nums[i] = nums[i].divide(gcd);
            }
            for (int i=0;i<counter;i+=1){
                BigRational currentRational = new BigRational(nums[i],total).simplify();
                if (currentRational.canSaveConvertToIntegerRational()){
                    expectations[i] = currentRational.toIntegerRational();
                }else {
                    //i hope this won't happen
                    expectations[i] = Approximation.find(new BigDecimal(currentRational.numerator()).divide(new BigDecimal(currentRational.denominator()),100).floatValue());
                }
            }
            calculatedMap.put(key,expectations);
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
