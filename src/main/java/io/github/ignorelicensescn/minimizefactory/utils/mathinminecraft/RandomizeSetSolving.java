package io.github.ignorelicensescn.minimizefactory.utils.mathinminecraft;

import io.github.ignorelicensescn.minimizefactory.utils.mathutils.Approximation;
import io.github.ignorelicensescn.minimizefactory.utils.mathutils.BigRational;
import io.github.ignorelicensescn.minimizefactory.utils.mathutils.IntegerRational;
import io.github.ignorelicensescn.minimizefactory.utils.simpleStructure.SimplePair;
import io.github.thebusybiscuit.slimefun4.libraries.dough.collections.RandomizedSet;
import io.github.thebusybiscuit.slimefun4.libraries.dough.collections.WeightedNode;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
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
            BigInteger gcd = BigInteger.ZERO;
            int counter = 0;
            BigInteger[] nums = new BigInteger[randomizedSet.size()];
            BigInteger total = BigInteger.ZERO;

            BigInteger binaryZeroes = minExp >= 0 ?BigInteger.TWO.pow(minExp):BigInteger.TWO.pow(minExp*(-1));
            for (WeightedNode<T> node:weightedNodes){
                result[counter] = node.getObject();
                int intPart = Float.floatToIntBits(node.getWeight())  & 0x7FFFFF;
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
                    expectations[i] = Approximation.find(new BigDecimal(currentRational.numerator()).divide(new BigDecimal(currentRational.denominator()),100).floatValue());
                }
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
