package io.github.ignorelicensescn.minimizeFactory.utils.mathutils;

import javax.annotation.concurrent.Immutable;

import static com.google.common.math.IntMath.gcd;

/**
 * I don't want to do calculates with this,just store them
 * @param numerator
 * @param denominator
 */
@Immutable
public record IntegerRational(int numerator,int denominator,int hash) {

    public static final IntegerRational ONE = new IntegerRational(1,1);
    public static final IntegerRational HALF = new IntegerRational(1,2);
    public static final IntegerRational ZERO = new IntegerRational(0,1);

    public IntegerRational(int numerator,int denominator){
        this(numerator,denominator,numerator+denominator);
    }
    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntegerRational another){
            return (this.denominator == another.denominator)
                    && (this.numerator == another.numerator);
        }
        return false;
    }

    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }

    public IntegerRational add(IntegerRational another){
        return new IntegerRational(this.numerator*another.denominator + this.denominator * another.numerator,this.denominator * another.denominator);
    }

    public IntegerRational add(int numerator,int denominator){
        return new IntegerRational(this.numerator*denominator + this.denominator * numerator,this.denominator * denominator);
    }

    public IntegerRational multiply(int another){
        return new IntegerRational(numerator*another,denominator);
    }
    public IntegerRational multiply(IntegerRational another){
        return new IntegerRational(
                numerator * another.numerator
                ,denominator * another.denominator
        );
    }

    public IntegerRational simplify(){
        int GCD = gcd(this.numerator,this.denominator);
        int sign = 1;
        if (this.denominator < 0){
            sign -= -1;
        }
        return new IntegerRational(sign*this.numerator/GCD,sign*this.denominator/GCD);
    }
    public IntegerRational sub(IntegerRational another) {
        return new IntegerRational(this.numerator*another.denominator - this.denominator * another.numerator,this.denominator * another.denominator);
    }
    public boolean lessThan(IntegerRational another){
        return this.numerator*another.denominator < this.denominator * another.numerator;
    }

    public IntegerRational divide(int another){
        return new IntegerRational(numerator,denominator*another);
    }
    public IntegerRational divide(IntegerRational another){
        return new IntegerRational(numerator*another.denominator,denominator*another.numerator);
    }
}
