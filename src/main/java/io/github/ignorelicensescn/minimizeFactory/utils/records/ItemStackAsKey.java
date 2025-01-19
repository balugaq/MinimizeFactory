package io.github.ignorelicensescn.minimizeFactory.utils.records;

import io.github.ignorelicensescn.minimizeFactory.utils.ItemStackUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemStackAsKey {
    private final int hash;
    private final ItemStack template;

    public int hash(){
        return hash;
    }
    public ItemStack getTemplate(){
        return template.clone();
    }
    public ItemStackAsKey(ItemStack template,Material type){
        template = template.clone();
        this.hash = 31 * (type != null ? type.hashCode() : 0) + (template.hasItemMeta()?template.getItemMeta().hashCode():0);
        this.template = template;
    }

    public ItemStackAsKey(ItemStack stack){
        //i want multi lines but it seems not allowed :(
        this(stack, stack.getType());
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemStackAsKey thatKey = (ItemStackAsKey) o;
        ItemStack thisStack = this.template.clone();
        ItemStack thatStack = thatKey.template.clone();
        return ItemStackUtil.isItemStackSimilar(thisStack,thatStack);
    }

    @Override
    public int hashCode() {
        return hash();
    }

    @Override
    public String toString() {
        return "ItemStackAsKey{" +
                "template=" + template +
                '}';
    }
}
