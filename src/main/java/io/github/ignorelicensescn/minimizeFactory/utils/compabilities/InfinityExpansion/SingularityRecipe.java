package io.github.ignorelicensescn.minimizeFactory.utils.compabilities.InfinityExpansion;

import org.bukkit.inventory.ItemStack;

public class SingularityRecipe {
    public ItemStack input;
    public ItemStack output;
    public int amount;

    public SingularityRecipe(ItemStack input, ItemStack output, int amount){
        input = input.clone();
        input.setAmount(1);
        this.input = input;
        this.output = output;
        this.amount = amount;
    }
}
