package io.github.ignorelicensescn.minimizeFactory.utils.ItemMetaRelated.machineWithRecipe;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.Objects;

public abstract class SerializedMachineWithRecipe {
    public ItemStack sfItemStack = null;
    @Nullable
    public SlimefunItem sfItem = null;


    public SerializedMachineWithRecipe(ItemStack sfItemStack){
        this.sfItemStack = sfItemStack;
        this.sfItem = SlimefunItem.getByItem(sfItemStack);
    }
    public SerializedMachineWithRecipe(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SerializedMachineWithRecipe that = (SerializedMachineWithRecipe) o;

        if (!Objects.equals(sfItemStack, that.sfItemStack)) return false;
        return Objects.equals(sfItem, that.sfItem);
    }

    @Override
    public int hashCode() {
        int result = sfItemStack != null ? sfItemStack.hashCode() : 0;
        result = 31 * result + (sfItem != null ? sfItem.hashCode() : 0);
        return result;
    }
}
