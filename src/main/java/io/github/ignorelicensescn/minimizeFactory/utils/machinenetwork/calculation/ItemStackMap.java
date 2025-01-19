package io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.calculation;

import io.github.ignorelicensescn.minimizeFactory.utils.mathutils.BigRational;
import io.github.ignorelicensescn.minimizeFactory.utils.mathutils.IntegerRational;
import io.github.ignorelicensescn.minimizeFactory.utils.records.ItemStackAsKey;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ItemStackMap extends HashMap<ItemStackAsKey, BigRational> {

    public BigRational get(ItemStackAsKey key) {
        BigRational result = super.get(key);
        return result==null?BigRational.ZERO:result;
    }

    public BigRational get(ItemStack key) {
        key = key.clone();
        if (key.getAmount() != 1){
            key.setAmount(1);
        }
        return get(new ItemStackAsKey(key));
    }

    public BigRational putRaw(ItemStackAsKey key, BigRational value) {
        return super.put(key, value);
    }
    @Override
    public BigRational put(ItemStackAsKey key, BigRational value) {
        BigRational base = get(key);
        BigRational putIn = base.add(value).simplify();
        if (putIn.numerator().compareTo(BigInteger.ZERO) == 0){
            remove(key);
            return base;
        }
        return super.put(key, putIn);
    }

    public BigRational put(ItemStack key, BigRational value) {
        key = key.clone();
        if (key.getAmount() != 1){
            key.setAmount(1);
        }
        return put(new ItemStackAsKey(key),value);
    }

    public void addAllItems(@Nonnull ItemStackMap another){
        for (Map.Entry<ItemStackAsKey, BigRational> fromAnother:another.entrySet()){
            this.addItem(fromAnother.getKey(),fromAnother.getValue());
        }
    }
    public void addAllItems(@Nullable ItemStack[] items, @Nullable IntegerRational[] expectations, BigRational multiplier){
        BigRational[] converted = null;
        if (expectations != null){
            converted = new BigRational[expectations.length];
            for (int i=0;i<expectations.length;i+=1){
                converted[i] = new BigRational(expectations[i]);
            }
        }
        addAllItems(items,converted,multiplier);
    }
    public void addAllItems(@Nullable ItemStack[] items, @Nullable BigRational[] expectations, BigRational multiplier){
        if (items == null){return;}
        if (expectations == null){
            for(ItemStack toAdd:items){
                addItem(toAdd,
                        multiplier
                                .multiply(toAdd.getAmount())
                );
            }
        }else {
            assert expectations.length == items.length;
            int counter = 0;
            for(ItemStack toAdd:items){
                addItem(toAdd,
                        expectations[counter]
                                .multiply(toAdd.getAmount())
                                .multiply(multiplier)
                );
                counter += 1;
            }
        }
    }

    public void addItem(@Nullable ItemStackAsKey toAdd,BigRational amount){
        if (toAdd == null){return;}
        put(toAdd,amount);
    }
    public void addItem(@Nullable ItemStack toAdd,BigRational amount){
        if (toAdd == null){return;}
        put(toAdd,amount);
    }

    /**
     * this:being consumed
     * null if this map cannot satisfy consumeMap
     * @param consumeMap map to "consume" this map
     * @return consumed
     */
    @Nullable
    public ItemStackMap tryConsume(ItemStackMap consumeMap){
        ItemStackMap resultIfSuccess = new ItemStackMap();
        ItemStackMap beingConsumed = this;
        Set<ItemStackAsKey> notConsumedKey = new HashSet<>(this.keySet());
        for (Map.Entry<ItemStackAsKey, BigRational> consumeEntry:consumeMap.entrySet()){
            BigRational ifConsumed = beingConsumed.getOrDefault(consumeEntry.getKey(),BigRational.ZERO).subtract(consumeEntry.getValue());
            if (ifConsumed.compareTo(BigRational.ZERO) < 0){
                return null;
            }
            resultIfSuccess.putRaw(consumeEntry.getKey(),ifConsumed);
            notConsumedKey.remove(consumeEntry.getKey());
        }
        for (ItemStackAsKey key:notConsumedKey){
            resultIfSuccess.putRaw(key,beingConsumed.get(key));
        }
        return resultIfSuccess;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ItemStackMap{");
        for (Map.Entry<ItemStackAsKey,BigRational> entry:this.entrySet()){
            sb.append(entry.getKey()).append("|").append(entry.getValue()).append(",\n");
        }
        sb.append("}");
        return sb.toString();
    }

}
