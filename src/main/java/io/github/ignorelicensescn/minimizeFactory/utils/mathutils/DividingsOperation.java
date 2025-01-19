package io.github.ignorelicensescn.minimizeFactory.utils.mathutils;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static com.google.common.math.IntMath.gcd;

@Deprecated
public class DividingsOperation {
    public static final BigInteger[] DIVIDING1_BIGINTEGER = new BigInteger[]{BigInteger.ONE,BigInteger.ONE};
    //Integer[].length == 2
    public static Integer[] sumOfDividings(List<Integer[]> dividings){
        if (dividings.size() > 2){
            return sumOfDividings(sumOfDividings(dividings.subList(0,dividings.size()-2)), dividings.get(dividings.size()-1));
        }else if (dividings.size() == 2){
            return sumOfDividings(dividings.get(0), dividings.get(1));
        }else if (dividings.size() == 1){
            return dividings.get(0);
        }else {
            return new Integer[]{0,0};
        }
    }

    public static Integer[] sumOfDividings(Integer[] aDividing, Integer[] anotherDividing){
        int numerator = aDividing[0] * anotherDividing[1] + anotherDividing[0] * aDividing[1];
        int denominator = aDividing[1] * anotherDividing[1];
        int GCD = gcd(numerator, denominator);
        numerator /= GCD;
        denominator /= GCD;
        return new Integer[]{numerator,denominator};
    }
    public static IntegerRational sumOfDividings(IntegerRational[] dividings){
        IntegerRational result = dividings[0];
        for (int i=1;i<dividings.length;i+=1){
            result.add(dividings[i]);
        }
        return result;
    }

    public static int[] sumOfDividings(int[][] dividings){
        if (dividings.length > 2){
            return sumOfDividings(sumOfDividings(Arrays.copyOfRange(dividings,0,dividings.length-2)), dividings[dividings.length-1]);
        }else if (dividings.length == 2){
            return sumOfDividings(dividings[0], dividings[1]);
        }else if (dividings.length == 1){
            return dividings[0];
        }else {
            return new int[]{0,0};
        }
    }

    public static int[] sumOfDividings(int[] aDividing, int[] anotherDividing){
        int numerator = aDividing[0] * anotherDividing[1] + anotherDividing[0] * aDividing[1];
        int denominator = aDividing[1] * anotherDividing[1];
        int GCD = gcd(numerator, denominator);
        numerator /= GCD;
        denominator /= GCD;
        return new int[]{numerator,denominator};
    }


    public static BigInteger[] sumOfDividings(BigInteger[][] dividings){
        if (dividings.length > 2){
            return sumOfDividings(sumOfDividings(Arrays.copyOfRange(dividings,0,dividings.length-2)), dividings[dividings.length-1]);
        }else if (dividings.length == 2){
            return sumOfDividings(dividings[0], dividings[1]);
        }else if (dividings.length == 1){
            return dividings[0];
        }else {
            return new BigInteger[]{BigInteger.ZERO,BigInteger.ZERO};
        }
    }

    public static BigInteger[] sumOfDividings(BigInteger[] aDividing, BigInteger[] anotherDividing){
        BigInteger numerator = aDividing[0].multiply(anotherDividing[1]).add(anotherDividing[0].multiply(aDividing[1]));
        BigInteger denominator = aDividing[1].multiply(anotherDividing[1]);
        BigInteger GCD = numerator.gcd(denominator);
        numerator = numerator.divide(GCD);
        denominator = denominator.divide(GCD);
        return new BigInteger[]{numerator,denominator};
    }

    /**
     * a < b
     */
    public static boolean less(int[] a,int[] b){
        return a[0]*b[1] < a[1]*b[0];
    }
    public static int[] min(int[] a,int[] b){
        if (less(a,b)) {
            return a;
        }
        else {
            return b;
        }
    }
    public static boolean less(BigInteger[] a,BigInteger[] b){
        return a[0].multiply(b[1]).compareTo(a[1].multiply(b[0])) < 0;
    }
    public static BigInteger[] min(BigInteger[] a,BigInteger[] b){
        if (less(a,b)) {
            return a;
        }
        else {
            return b;
        }
    }

    public static int[] sub(int[] a,int[] b){
        return new int[]{a[0]*b[1] - a[1]*b[0],a[1]*b[1]};
    }

    /**
     * a/b
     */
    public static int[] divide(int[] a,int[] b){
        return new int[]{a[0]*b[1],a[1]*b[0]};
    }
    public static int[] multiply(int[] a,int[] b){
        return new int[]{a[0]*b[0],a[1]*b[1]};
    }

    public static BigInteger[] sub(BigInteger[] a,BigInteger[] b){
        return new BigInteger[]{a[0].multiply(b[1]).subtract(a[1].multiply(b[0])),a[1].multiply(b[1])};
    }

    /**
     * a/b
     */
    public static BigInteger[] divide(BigInteger[] a,BigInteger[] b){
        return new BigInteger[]{a[0].multiply(b[1]),a[1].multiply(b[0])};
    }
    public static BigInteger[] multiply(BigInteger[] a,BigInteger[] b){
        return new BigInteger[]{a[0].multiply(b[0]),a[1].multiply(b[1])};
    }

    public static BigInteger[][] dividingsToBigInteger(int[][] dividings){
        BigInteger[][] result = new BigInteger[dividings.length][2];
        for (int i=0;i<dividings.length;i+=1){
            result[i] = new BigInteger[]{BigInteger.valueOf(dividings[i][0]),BigInteger.valueOf(dividings[i][1])};
        }
        return result;
    }
    public static BigInteger[][] dividingsToBigInteger(IntegerRational[] dividings){
        BigInteger[][] result = new BigInteger[dividings.length][2];
        for (int i=0;i<dividings.length;i+=1){
            result[i] = new BigInteger[]{BigInteger.valueOf(dividings[i].numerator()),BigInteger.valueOf(dividings[i].denominator())};
        }
        return result;
    }
    public static BigInteger[][] dividingsToBigInteger(LongRational[] dividings){
        BigInteger[][] result = new BigInteger[dividings.length][2];
        for (int i=0;i<dividings.length;i+=1){
            result[i] = new BigInteger[]{BigInteger.valueOf(dividings[i].numerator()),BigInteger.valueOf(dividings[i].denominator())};
        }
        return result;
    }
    public static BigInteger[][] simplify (BigInteger[][] toSimplify){
        for (int i = 0; i < toSimplify.length; i+=1) {
            toSimplify[i] = simplify(toSimplify[i]);
        }
        return toSimplify;
    }
    public static BigInteger[] simplify (BigInteger[] toSimplify){
        BigInteger GCD = toSimplify[0].gcd(toSimplify[1]);
        toSimplify[0] = toSimplify[0].divide(GCD);
        toSimplify[1] = toSimplify[1].divide(GCD);
        return toSimplify;
    }
}
