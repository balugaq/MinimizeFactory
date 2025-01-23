package io.github.ignorelicensescn.minimizefactory.items.consumptions;

import io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.DataTypeMethods;
import io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.PersistentSerializedMachineRecipeType;
import io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.DistinctiveItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

import static io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.PersistentSerializedMachineRecipeType.SERIALIZED_MACHINE_RECIPE;

public class MachineStabilizer extends UnplaceableBlock implements DistinctiveItem {
    public MachineStabilizer(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public boolean canStack(@Nonnull ItemMeta itemMetaOne, @Nonnull ItemMeta itemMetaTwo) {
        return machineStabilizerCanStack(itemMetaOne,itemMetaTwo);
    }

    public static boolean machineStabilizerCanStack(@Nonnull ItemMeta itemMetaOne, @Nonnull ItemMeta itemMetaTwo){
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

    public static class MachineStabilizerStack extends SlimefunItemStack implements DistinctiveItem {
        public final String id;
        public MachineStabilizerStack(@Nonnull String id, @Nonnull Material type, @Nullable String name, String... lore){
            super(id,type,name,lore);
            this.id = id;
        }

        /**
         * see  {@link io.github.ignorelicensescn.minimizefactory.utils.itemstackrelated.ItemStackUtil#isItemStackSimilar}
         * close to end of that method
         *
         */
        @Override
        public boolean canStack(@Nonnull ItemMeta itemMetaOne, @Nonnull ItemMeta itemMetaTwo) {
            return MachineStabilizer.machineStabilizerCanStack(itemMetaOne,itemMetaTwo);
        }

        @Nonnull
        @Override
        public String getId() {
            return id;
        }
    }
}
