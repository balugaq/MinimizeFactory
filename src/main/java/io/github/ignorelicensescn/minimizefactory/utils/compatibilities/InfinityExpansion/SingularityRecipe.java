package io.github.ignorelicensescn.minimizefactory.utils.compatibilities.InfinityExpansion;

import org.bukkit.inventory.ItemStack;

public record SingularityRecipe(ItemStack input, ItemStack output, int amount) {
    public SingularityRecipe {
        input = input.clone();
        input.setAmount(1);
    }
}
