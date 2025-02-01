package io.github.ignorelicensescn.minimizefactory.utils.datastructures.records;

import io.github.ignorelicensescn.minimizefactory.utils.itemstackrelated.ItemStackUtil;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

public record ItemStacksToStackRecipe(ItemStack[] input,ItemStack output) {
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ItemStacksToStackRecipe that)) {
            return false;
        }
        if (input.length != that.input.length){
            return false;
        }
        for (int i=0;i<input.length;i++){
            if (!ItemStackUtil.isItemStackSimilar(input[i],that.input[i])){
                return false;
            }
        }
        return ItemStackUtil.isItemStackSimilar(output,that.output);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(input);
        result = 31 * result + Objects.hashCode(output);
        return result;
    }
}
