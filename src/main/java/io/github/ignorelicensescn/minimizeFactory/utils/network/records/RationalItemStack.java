package io.github.ignorelicensescn.minimizeFactory.utils.network.records;

import io.github.ignorelicensescn.minimizeFactory.utils.mathUtils.BigRational;
import io.github.ignorelicensescn.minimizeFactory.utils.mathUtils.IntegerRational;
import io.github.ignorelicensescn.minimizeFactory.utils.mathUtils.LongRational;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.math.BigInteger;

public record RationalItemStack(@Nonnull ItemStack stack,@Nonnull BigRational amount) {
    public RationalItemStack(ItemStack stack){
        this(stack,new BigRational(BigInteger.valueOf(stack.getAmount()),BigInteger.ONE));
    }
    public RationalItemStack(ItemStack stack, BigRational amount){
        this.stack = stack.clone();
        this.stack.setAmount(1);
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RationalItemStack that = (RationalItemStack) o;

        if (!stack.equals(that.stack)) return false;
        return amount.equals(that.amount);
    }

    @Override
    public int hashCode() {
        int result = stack.hashCode();
        result = 31 * result + amount.hashCode();
        return result;
    }
    
    public RationalItemStack add(BigRational toCalculate){
        return new RationalItemStack(this.stack,this.amount.add(toCalculate));
    }
    public RationalItemStack subtract(BigRational toCalculate){
        return new RationalItemStack(this.stack,this.amount.subtract(amount));
    }
    public RationalItemStack multiply(BigRational toCalculate){
        return new RationalItemStack(this.stack,this.amount.multiply(toCalculate));
    }
    public RationalItemStack divide(BigRational toCalculate){
        return new RationalItemStack(this.stack,this.amount.divide(toCalculate));
    }
    public RationalItemStack add(int toCalculate){
        return new RationalItemStack(this.stack,this.amount.add(toCalculate));
    }
    public RationalItemStack subtract(int toCalculate){
        return new RationalItemStack(this.stack,this.amount.subtract(toCalculate));
    }
    public RationalItemStack multiply(int toCalculate){
        return new RationalItemStack(this.stack,this.amount.multiply(toCalculate));
    }
    public RationalItemStack divide(int toCalculate){
        return new RationalItemStack(this.stack,this.amount.divide(toCalculate));
    }
    public RationalItemStack add(long toCalculate){
        return new RationalItemStack(this.stack,this.amount.add(toCalculate));
    }
    public RationalItemStack subtract(long toCalculate){
        return new RationalItemStack(this.stack,this.amount.subtract(toCalculate));
    }
    public RationalItemStack multiply(long toCalculate){
        return new RationalItemStack(this.stack,this.amount.multiply(toCalculate));
    }
    public RationalItemStack divide(long toCalculate){
        return new RationalItemStack(this.stack,this.amount.divide(toCalculate));
    }
    public RationalItemStack add(IntegerRational toCalculate){
        return new RationalItemStack(this.stack,this.amount.add(toCalculate));
    }
    public RationalItemStack subtract(IntegerRational toCalculate){
        return new RationalItemStack(this.stack,this.amount.subtract(toCalculate));
    }
    public RationalItemStack multiply(IntegerRational toCalculate){
        return new RationalItemStack(this.stack,this.amount.multiply(toCalculate));
    }
    public RationalItemStack divide(IntegerRational toCalculate){
        return new RationalItemStack(this.stack,this.amount.divide(toCalculate));
    }
    public RationalItemStack add(LongRational toCalculate){
        return new RationalItemStack(this.stack,this.amount.add(toCalculate));
    }
    public RationalItemStack subtract(LongRational toCalculate){
        return new RationalItemStack(this.stack,this.amount.subtract(toCalculate));
    }
    public RationalItemStack multiply(LongRational toCalculate){
        return new RationalItemStack(this.stack,this.amount.multiply(toCalculate));
    }
    public RationalItemStack divide(LongRational toCalculate){
        return new RationalItemStack(this.stack,this.amount.divide(toCalculate));
    }
}
