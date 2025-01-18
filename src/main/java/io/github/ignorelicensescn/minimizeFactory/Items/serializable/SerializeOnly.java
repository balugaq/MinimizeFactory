package io.github.ignorelicensescn.minimizeFactory.Items.serializable;

import io.github.ignorelicensescn.minimizeFactory.utils.SerializedRecipeProvider;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.properties;

public abstract class SerializeOnly extends UnplaceableBlock implements SerializedRecipeProvider<SlimefunItem> {
    public SerializeOnly(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Nonnull
    @Override
    public String[] getEnergyInfoStrings(@Nonnull SlimefunItem m) {
        return new String[]{properties.getReplacedProperty("Serializable_No_Energy_Needed")};
    }
}
