package io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.calculation;

import io.github.ignorelicensescn.minimizeFactory.utils.mathutils.BigRational;
import io.github.ignorelicensescn.minimizeFactory.utils.records.ItemStackAsKey;
import org.bukkit.inventory.ItemStack;

import java.util.Map;


public class UnmodifiableItemStackMap extends ItemStackMap{

    public static final ItemStackMap EMPTY_ITEM_STACK_MAP = new UnmodifiableItemStackMap();
    public UnmodifiableItemStackMap(){
        super();
    }

    public UnmodifiableItemStackMap(ItemStackMap map){
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
