package io.github.ignorelicensescn.minimizefactory.utils.mathutils;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;
@Immutable
public record BigRational(@Nonnull BigInteger numerator,@Nonnull BigInteger denominator,int sign,int hash)
        implements Comparable<BigRational>, Serializable
{
    public static final BigRational ONE = new BigRational(BigInteger.ONE,BigInteger.ONE);
    public static final BigRational ZERO = new BigRational(BigInteger.ZERO,BigInteger.ONE);

    public BigRational(IntegerRational integerRational){
        this(BigInteger.valueOf(integerRational.numerator()),BigInteger.valueOf(integerRational.denominator()));
    }
    public BigRational(@Nonnull BigInteger numerator,@Nonnull BigInteger denominator){
        this(numerator,denominator,numerator.signum()*denominator.signum());
    }
    public BigRational(long numerator, long denominator){
        this(BigInteger.valueOf(numerator),BigInteger.valueOf(denominator));
    }
    private BigRational(@Nonnull BigInteger numerator,@Nonnull BigInteger denominator,int sign){
        this(numerator,denominator,sign,Objects.hash(numerator,denominator));
    }
    public BigRational(long value){
        this(BigInteger.valueOf(value),BigInteger.ONE);
    }

    public BigRational pow(int x){
        BigRational result = BigRational.ONE;
        BigRational base = this.simplify();
        for (int i = 0;i<Math.abs(x);i++){
            if (x > 0){
                result = result.multiply(base);
            }else {
                result = result.divide(base);
            }
        }
        return result;
    }

    public BigRational add(BigRational another){
        return new BigRational(
                this.numerator.multiply(another.denominator())
                        .add(this.denominator.multiply(another.numerator())),
                this.denominator.multiply(another.denominator()));
    }

    public BigRational add(IntegerRational another){
        return new BigRational(
                this.numerator.multiply(BigInteger.valueOf(another.denominator()))
                        .add(this.denominator.multiply(BigInteger.valueOf(another.numerator()))),
                this.denominator.multiply(BigInteger.valueOf(another.denominator())));
    }

    public BigRational add(LongRational another){
        return new BigRational(
                this.numerator.multiply(BigInteger.valueOf(another.denominator()))
                        .add(this.denominator.multiply(BigInteger.valueOf(another.numerator()))),
                this.denominator.multiply(BigInteger.valueOf(another.denominator())));
    }

    public BigRational add(BigInteger toAdd){
        return new BigRational(
                this.numerator.add(this.denominator.multiply(toAdd)),
                this.denominator);
    }
    public BigRational add(long toAdd){
        return new BigRational(
                this.numerator.add(this.denominator.multiply(BigInteger.valueOf(toAdd))),
                this.denominator);
    }
    public BigRational subtract(BigRational another){
        return new BigRational(
                this.numerator.multiply(another.denominator())
                        .subtract(this.denominator.multiply(another.numerator())),
                this.denominator.multiply(another.denominator()));
    }
    public BigRational subtract(IntegerRational another){
        return new BigRational(
                this.numerator.multiply(BigInteger.valueOf(another.denominator()))
                        .subtract(this.denominator.multiply(BigInteger.valueOf(another.numerator()))),
                this.denominator.multiply(BigInteger.valueOf(another.denominator())));
    }
    public BigRational subtract(LongRational another){
        return new BigRational(
                this.numerator.multiply(BigInteger.valueOf(another.denominator()))
                        .subtract(this.denominator.multiply(BigInteger.valueOf(another.numerator()))),
                this.denominator.multiply(BigInteger.valueOf(another.denominator())));
    }
    public BigRational subtract(long toSubtract){
        return new BigRational(
                this.numerator.subtract(this.denominator.multiply(BigInteger.valueOf(toSubtract))),
                this.denominator);
    }
    public BigRational multiply(BigRational another){
        return new BigRational(
                this.numerator.multiply(another.numerator()),
                this.denominator.multiply(another.denominator()));
    }
    public BigRational multiply(IntegerRational another){
        return new BigRational(
                this.numerator.multiply(BigInteger.valueOf(another.numerator())),
                this.denominator.multiply(BigInteger.valueOf(another.denominator())));
    }
    public BigRational multiply(LongRational another){
        return new BigRational(
                this.numerator.multiply(BigInteger.valueOf(another.numerator())),
                this.denominator.multiply(BigInteger.valueOf(another.denominator())));
    }
    public BigRational multiply(long toMultiply){
        return new BigRational(
                this.numerator.multiply(BigInteger.valueOf(toMultiply)),
                this.denominator);
    }
    public BigRational divide(BigRational another){
        return new BigRational(
                this.numerator.multiply(another.denominator()),
                this.denominator.multiply(another.numerator()));
    }
    public BigRational divide(IntegerRational another){
        return new BigRational(
                this.numerator.multiply(BigInteger.valueOf(another.denominator())),
                this.denominator.multiply(BigInteger.valueOf(another.numerator())));
    }
    public BigRational divide(LongRational another){
        return new BigRational(
                this.numerator.multiply(BigInteger.valueOf(another.denominator())),
                this.denominator.multiply(BigInteger.valueOf(another.numerator())));
    }
    public BigRational divide(long toDivide){
        return new BigRational(
                this.numerator,
                this.denominator.multiply(BigInteger.valueOf(toDivide))
        );
    }
    public BigRational unifySign(){
        if (this.denominator.signum() == -1){
            return new BigRational(this.numerator.multiply(BigInteger.valueOf(-1)),this.denominator.multiply(BigInteger.valueOf(-1)));
        }
        return this;
    }
    public BigRational simplify(){
        if (this.numerator.equals(BigInteger.ZERO)){return BigRational.ZERO;}
        if (this.denominator.equals(BigInteger.ZERO)){new ArithmeticException("division by zero").printStackTrace();}
        BigInteger gcd = this.numerator.gcd(this.denominator);
        return new BigRational(this.numerator.divide(gcd),this.denominator.divide(gcd)).unifySign();
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BigRational another){
            BigRational here = this.simplify();
            another = another.simplify();
            return Objects.equals(here.numerator(),another.numerator()) && Objects.equals(here.denominator,another.denominator());
        }
        return false;
    }
    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }
    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public int compareTo(@Nonnull BigRational there) {
        BigRational here = this.simplify();
        there = there.simplify();
        if (there.sign()==0){
            return sign();
        }
        if (sign() == 0){
            return there.sign();
        }
        if (this.sign() > there.sign()){
            return 1;
        }
        if (this.sign() < there.sign()){
            return -1;
        }
        if (Objects.equals(here.numerator(),there.numerator())){
            return (-1) * here.denominator.compareTo(there.denominator);
        }
        if (Objects.equals(here.denominator,there.denominator)){
            return here.numerator().compareTo(there.numerator());
        }
        return here.numerator().multiply(there.denominator).compareTo(here.denominator.multiply(there.numerator));
    }
}
