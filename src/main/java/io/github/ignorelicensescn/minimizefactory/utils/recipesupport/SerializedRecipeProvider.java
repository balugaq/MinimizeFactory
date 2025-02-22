package io.github.ignorelicensescn.minimizefactory.utils.recipesupport;

import io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizefactory.utils.simpleStructure.SimplePair;
import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.ArrayUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface SerializedRecipeProvider<Machine> {
    
    @Nonnull
    List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable Machine m, @Nullable ItemStack stack);
    @Nullable
    default SimplePair<String,List<String>> getNameAndLoreForRecipe(@Nullable Machine m,SimplePair<SerializedMachine_MachineRecipe,ItemStack> serialized,int index){
        return null;
    }
    @Nonnull
    default String[] getEnergyInfoWithCheck(@Nullable Object /*Machine*/ m){
        if (m == null){return new String[0];}
        try {
            return getEnergyInfoStrings((Machine) m);
        }catch (Exception e){
            e.printStackTrace();
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

    }
    @Nonnull
    String[] getEnergyInfoStrings(@Nonnull Machine m);

    default Material getShowingMaterial(@Nullable Machine m,SimplePair<SerializedMachine_MachineRecipe,ItemStack> serialized,int index){
        return null;
    }
}
