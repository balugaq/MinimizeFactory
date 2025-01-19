package io.github.ignorelicensescn.minimizeFactory.utils.mathutils;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>VERY FAST</p>
 * <p/>
 * <a href="https://people.ksp.sk//~misof/publications/2007approx.pdf">FROM HERE(8 Implementation)(copied with source code)</a>
 * <p>Approximating Rational Numbers by Fractions</p>
 * <p>Michal Foriˇsek</p>
 * <p>forisek@dcs.fmph.uniba.sk,</p>
 * <p>Department of Informatics,</p>
 * <p>Faculty of Mathematics, Physics and Informatics,</p>
 * <p>Comenius University,</p>
 * <p>Mlynsk´a dolina, 842 48 Bratislava, Slovakia</p>
 */

public class Approximation {
    public static final Map<Float, IntegerRational> cacheMapForFloat = new HashMap<>();
    public static final Map<Double, LongRational> cacheMapForDouble = new HashMap<>();
    static {
        cacheMapForFloat.put(0.5F,IntegerRational.HALF);
        cacheMapForFloat.put(0F,IntegerRational.ZERO);
        cacheMapForFloat.put(1F,IntegerRational.ONE);
        cacheMapForDouble.put(0.5, LongRational.HALF);
        cacheMapForDouble.put(0., LongRational.ZERO);
        cacheMapForDouble.put(1., LongRational.ONE);
    }
            // fraction comparison: compare (a/b) and (c/d)
    public static boolean less(int a,int b,int c,int d) { return (a*d < b*c); }
    public static boolean less(long a,long b,long c,long d) { return (a*d < b*c); }
    public static boolean less_or_equal(int a,int b,int c,int d) { return (a*d <= b*c); }
    public static boolean less_or_equal(long a,long b,long c,long d) { return (a*d <= b*c); }
// check whether a/b is a valid approximation
    public static boolean matches(int a,int b, int alpha_num, int d_num, int denum) {
        if (less_or_equal(a,b,alpha_num-d_num,denum)) return false;
        return less(a, b, alpha_num + d_num, denum);
    }
    // check whether a/b is a valid approximation
    public static boolean matches(long a,long b, long alpha_num, long d_num, long denum) {
        if (less_or_equal(a,b,alpha_num-d_num,denum)) return false;
        return less(a, b, alpha_num + d_num, denum);
    }

    public static int find_exact_solution_left(int p_a,int q_a,int p_b,int q_b, int alpha_num, int d_num, int denum) {
        double k_num = denum * p_b - (alpha_num + d_num) * q_b;
        double k_denum = (alpha_num + d_num) * q_a - denum * p_a;
        return (int) (Math.floor((k_num / k_denum)) + 1);
        //print (p_b + k*p_a)," ",(q_b + k*q_a),"\n";
    }
    public static int find_exact_solution_right(int p_a,int q_a,int p_b,int q_b, int alpha_num, int d_num, int denum) {
        double k_num = - denum * p_b + (alpha_num - d_num) * q_b;
        double k_denum = - (alpha_num - d_num) * q_a + denum * p_a;
        return (int) (Math.floor(k_num / k_denum) + 1);
        //print (p_b + k*p_a)," ",(q_b + k*q_a),"\n";
    }

    public static long find_exact_solution_left(long p_a,long q_a,long p_b,long q_b, long alpha_num, long d_num, long denum) {
        double k_num = denum * p_b - (alpha_num + d_num) * q_b;
        double k_denum = (alpha_num + d_num) * q_a - denum * p_a;
        return (long) (Math.floor((k_num / k_denum)) + 1);
        //print (p_b + k*p_a)," ",(q_b + k*q_a),"\n";
    }
    public static long find_exact_solution_right(long p_a,long q_a,long p_b,long q_b, long alpha_num, long d_num, long denum) {
        double k_num = - denum * p_b + (alpha_num - d_num) * q_b;
        double k_denum = - (alpha_num - d_num) * q_a + denum * p_a;
        return (long) (Math.floor(k_num / k_denum) + 1);
        //print (p_b + k*p_a)," ",(q_b + k*q_a),"\n";
    }

    /**
     *      input variables: alpha_num(given real number in [0,1)), d_num(tolerance), denum
     *     <p>α = alpha_num/denum</p>
     *     <p>d = d_num/denum</p>
     *     <p>0 < d_num < alpha_num < denum</p>
     *     <p>d_num + alpha_num < denum</p>
     *
     *          <p>we seek the first fraction that falls into the interval</p>
     *              <p>(α-d, α+d) =</p>
     *              <p>= ( (alpha_num-d_num)/denum, (alpha_num+d_num)/denum )</p>
     */
    public static IntegerRational find(int alpha_num,int d_num,int denum){
        // set initial bounds for the search:
        int p_a = 0 ;
        int q_a = 1 ;
        int p_b = 1 ;
        int q_b = 1 ;
        while (true) {
// compute the number of steps to the left
            int x_num = denum * p_b - alpha_num * q_b;
            int x_denum = - denum * p_a + alpha_num * q_a;
            int x = (int) Math.ceil((double) x_num / (double)x_denum);// = ceil(x_num / x_denum) //(x_num + x_denum - 1) / x_denum
// check whether we have a valid approximation
            boolean aa = matches(p_b + x*p_a, q_b + x*q_a,alpha_num,d_num,denum);
            boolean bb = matches( p_b + (x-1)*p_a, q_b + (x-1)*q_a,alpha_num,d_num,denum);
            if (aa || bb) {
                int cc = find_exact_solution_left(p_a,q_a,p_b,q_b,alpha_num,d_num,denum);
                return new IntegerRational((p_b + cc*p_a),(q_b + cc*q_a));
            }
// update the interval
            int new_p_a = p_b + (x-1)*p_a;
            int new_q_a = q_b + (x-1)*q_a;
            int new_p_b = p_b + x*p_a;
            int new_q_b = q_b + x*q_a;
            p_a = new_p_a;
            q_a = new_q_a;
            p_b = new_p_b;
            q_b = new_q_b;
// compute the number of steps to the right
            x_num = alpha_num * q_b - denum * p_b;
            x_denum = - alpha_num * q_a + denum * p_a;
            x = (x_num + x_denum - 1) / x_denum; // = ceil(x_num / x_denum)
// check whether we have a valid approximation
            aa = matches( p_b + x*p_a, q_b + x*q_a ,alpha_num,d_num,denum);
            bb = matches( p_b + (x-1)*p_a, q_b + (x-1)*q_a ,alpha_num,d_num,denum);
            if (aa || bb) {
                int cc = find_exact_solution_right(p_a, q_a, p_b, q_b, alpha_num, d_num, denum);
                return new IntegerRational((p_b + cc * p_a), (q_b + cc * q_a));
            }
// update the interval
            new_p_a = p_b + (x-1)*p_a ; new_q_a = q_b + (x-1)*q_a;
            new_p_b = p_b + x*p_a ; new_q_b = q_b + x*q_a;
            p_a = new_p_a ; q_a = new_q_a;
            p_b = new_p_b ; q_b = new_q_b;
        }
    }
    public static IntegerRational find(float a){
        LongRational longResult = find((double) a);
        if (Integer.MIN_VALUE <= longResult.numerator() && longResult.numerator() <= Integer.MAX_VALUE
                && Integer.MIN_VALUE <= longResult.denominator() && longResult.denominator() <= Integer.MAX_VALUE){
            return new IntegerRational((int) longResult.numerator(), (int) longResult.denominator());
        }
        if (cacheMapForFloat.containsKey(a)){return cacheMapForFloat.get(a);}
        int sign = (int) Math.signum(a);
        a = Math.abs(a);
        if (0 < a && a < 1){
            IntegerRational result = find((int) (a * (1000000000)),1,1000000000);
            result = result.multiply(sign);
            cacheMapForFloat.put(a * sign,result);
            return result;
        }
        else {
            int floorPart = (int) Math.floor(a);
            a -= floorPart;
            IntegerRational dividingPart = find(a);
            dividingPart = new IntegerRational(dividingPart.denominator()*floorPart*sign,dividingPart.numerator());
            cacheMapForFloat.put(a * sign,dividingPart);
            return dividingPart;
        }
    }

    public static LongRational find(long alpha_num,long d_num,long denum){
        // set initial bounds for the search:
        long p_a = 0 ;
        long q_a = 1 ;
        long p_b = 1 ;
        long q_b = 1 ;
        while (true) {
// compute the number of steps to the left
            long x_num = denum * p_b - alpha_num * q_b;
            long x_denum = - denum * p_a + alpha_num * q_a;
            long x = (long) Math.ceil((double) x_num / (double)x_denum);// = ceil(x_num / x_denum) //(x_num + x_denum - 1) / x_denum
// check whether we have a valid approximation
            boolean aa = matches(p_b + x*p_a, q_b + x*q_a,alpha_num,d_num,denum);
            boolean bb = matches( p_b + (x-1)*p_a, q_b + (x-1)*q_a,alpha_num,d_num,denum);
            if (aa || bb) {
                long cc = find_exact_solution_left(p_a,q_a,p_b,q_b,alpha_num,d_num,denum);
                return new LongRational((p_b + cc*p_a),(q_b + cc*q_a));
            }
// update the interval
            long new_p_a = p_b + (x-1)*p_a;
            long new_q_a = q_b + (x-1)*q_a;
            long new_p_b = p_b + x*p_a;
            long new_q_b = q_b + x*q_a;
            p_a = new_p_a;
            q_a = new_q_a;
            p_b = new_p_b;
            q_b = new_q_b;
// compute the number of steps to the right
            x_num = alpha_num * q_b - denum * p_b;
            x_denum = - alpha_num * q_a + denum * p_a;
            x = (x_num + x_denum - 1) / x_denum; // = ceil(x_num / x_denum)
// check whether we have a valid approximation
            aa = matches( p_b + x*p_a, q_b + x*q_a ,alpha_num,d_num,denum);
            bb = matches( p_b + (x-1)*p_a, q_b + (x-1)*q_a ,alpha_num,d_num,denum);
            if (aa || bb) {
                long cc = find_exact_solution_right(p_a, q_a, p_b, q_b, alpha_num, d_num, denum);
                return new LongRational((p_b + cc * p_a), (q_b + cc * q_a));
            }
// update the interval
            new_p_a = p_b + (x-1)*p_a ; new_q_a = q_b + (x-1)*q_a;
            new_p_b = p_b + x*p_a ; new_q_b = q_b + x*q_a;
            p_a = new_p_a ; q_a = new_q_a;
            p_b = new_p_b ; q_b = new_q_b;
        }
    }

    public static LongRational find(double a){
        if (cacheMapForDouble.containsKey(a)){return cacheMapForDouble.get(a);}
        int sign = (int) Math.signum(a);
        a = Math.abs(a);
        if (0 < a && a < 1){
            LongRational result = find((long) (a * 1000000000000000000L),1,1000000000000000000L);
            result = result.multiply(sign);
            cacheMapForDouble.put(a * sign,result);
            return result;
        }
        else {
            long floorPart = (long) Math.floor(a);
            a -= floorPart;
            LongRational dividingPart = find(a);
            dividingPart = new LongRational(
                    dividingPart.denominator() * floorPart + dividingPart.numerator(),
                    dividingPart.denominator()
            );
            cacheMapForDouble.put(a * sign,dividingPart);
            return dividingPart;
        }
    }
}
