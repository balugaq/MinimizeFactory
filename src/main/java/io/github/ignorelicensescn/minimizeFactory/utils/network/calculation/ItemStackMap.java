package io.github.ignorelicensescn.minimizeFactory.utils.network.calculation;

import io.github.ignorelicensescn.minimizeFactory.utils.mathUtils.BigRational;
import io.github.ignorelicensescn.minimizeFactory.utils.mathUtils.IntegerRational;
import io.github.ignorelicensescn.minimizeFactory.utils.network.records.ItemStackAsKey;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ItemStackMap extends HashMap<ItemStackAsKey, BigRational> {

    public static final ItemStackMap EMPTY_ITEM_STACK_MAP = new ItemStackMap();

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


    @Override
    public BigRational put(ItemStackAsKey key, BigRational value) {
        BigRational base = get(key);
        return super.put(key, base.add(value).simplify());
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
            for (int i=0;i<expectations.length;i++){
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
     * null if cannot satisfy consumeMap
     *
     * @param consumeMap map to "consume" this map
     * @return consumed
     */
    @Nullable
    public ItemStackMap tryConsume(ItemStackMap consumeMap){
        ItemStackMap resultIfSuccess = new ItemStackMap();
        ItemStackMap beingConsumed = this;
        for (Map.Entry<ItemStackAsKey, BigRational> consumeEntry:consumeMap.entrySet()){
            BigRational ifConsumed = beingConsumed.getOrDefault(consumeEntry.getKey(),BigRational.ZERO).subtract(consumeEntry.getValue());
            if (ifConsumed.compareTo(BigRational.ZERO) < 0){
                return null;
            }
            resultIfSuccess.put(consumeEntry.getKey(),ifConsumed);
        }
        return resultIfSuccess;
    }
}
