package io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.calculation;

import io.github.ignorelicensescn.minimizefactory.utils.mathutils.BigRational;
import io.github.ignorelicensescn.minimizefactory.utils.records.ItemStackAsKey;
import org.bukkit.inventory.ItemStack;

import java.util.Map;


public class UnmodifiableItemStackMapForContainerCalculation extends ItemStackMapForContainerCalculation {

    public static final ItemStackMapForContainerCalculation EMPTY_ITEM_STACK_MAP = new UnmodifiableItemStackMapForContainerCalculation();
    public UnmodifiableItemStackMapForContainerCalculation(){
        super();
    }

    public UnmodifiableItemStackMapForContainerCalculation(ItemStackMapForContainerCalculation map){
        super();
        super.putAll(map);
    }

    @Override
    public void putAll(Map<? extends ItemStackAsKey, ? extends BigRational> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BigRational put(ItemStack key, BigRational value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BigRational remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }


}
