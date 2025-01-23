package io.github.ignorelicensescn.minimizefactory.utils.mathutils;

import javax.annotation.concurrent.Immutable;
import java.util.Objects;

/**
 * I don't want to do calculates with this,just store them
 * @param numerator
 * @param denominator
 */
@Immutable
public record LongRational(long numerator,long denominator,int hash) {
    public static final LongRational ONE = new LongRational(1,1);
    public static final LongRational HALF = new LongRational(1,2);
    public static final LongRational ZERO = new LongRational(0,1);

    public LongRational(long numerator,long denominator){
        this(numerator,denominator,Long.hashCode(numerator) + Long.hashCode(denominator));
    }
    @Override
    public int hashCode() {
        return Objects.hash(numerator, denominator);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LongRational another){
            return (this.denominator == another.denominator)
                    && (this.numerator == another.numerator);
        }
        return false;
    }
    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }
    public LongRational multiply(long l){
        return new LongRational(numerator()*l,denominator);
    }
}
