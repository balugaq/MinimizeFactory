package io.github.ignorelicensescn.minimizeFactory.Items.Consumptions;

import io.github.ignorelicensescn.minimizeFactory.utils.Itemmetaoperationrelated.DataTypeMethods;
import io.github.ignorelicensescn.minimizeFactory.utils.Itemmetaoperationrelated.PersistentSerializedMachineRecipeType;
import io.github.ignorelicensescn.minimizeFactory.utils.Itemmetaoperationrelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.DistinctiveItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;

import static io.github.ignorelicensescn.minimizeFactory.utils.Itemmetaoperationrelated.PersistentSerializedMachineRecipeType.SERIALIZED_MACHINE_RECIPE;

public class MachineStabilizer extends UnplaceableBlock implements DistinctiveItem {
    public MachineStabilizer(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public boolean canStack(@Nonnull ItemMeta itemMetaOne, @Nonnull ItemMeta itemMetaTwo) {
        Optional<SerializedMachine_MachineRecipe> optionalA = DataTypeMethods.getOptionalCustom(itemMetaOne, SERIALIZED_MACHINE_RECIPE, PersistentSerializedMachineRecipeType.TYPE);
        Optional<SerializedMachine_MachineRecipe> optionalB = DataTypeMethods.getOptionalCustom(itemMetaTwo, SERIALIZED_MACHINE_RECIPE, PersistentSerializedMachineRecipeType.TYPE);
        if (optionalA.isEmpty() && optionalB.isEmpty()){
            return true;
        }
        if (optionalA.isPresent() && optionalB.isPresent()){
            SerializedMachine_MachineRecipe serializedA = optionalA.get();
            SerializedMachine_MachineRecipe serializedB = optionalB.get();
            return Objects.equals(serializedA,serializedB);
        }
        return false;
    }
}
