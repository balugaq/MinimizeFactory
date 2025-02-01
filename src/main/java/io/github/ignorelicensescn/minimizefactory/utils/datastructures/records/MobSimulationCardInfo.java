package io.github.ignorelicensescn.minimizefactory.utils.datastructures.records;

import io.github.ignorelicensescn.minimizefactory.utils.EmptyArrays;
import io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizefactory.utils.localmachinerecipe.MachineRecipeInTicks;
import io.github.ignorelicensescn.minimizefactory.utils.mathutils.IntegerRational;
import org.bukkit.inventory.ItemStack;

import static io.github.ignorelicensescn.minimizefactory.utils.compatibilities.InfinityExpansion.InfinityExpansionConsts.CHAMBER_INTERVAL;

public record MobSimulationCardInfo(ItemStack[] outputs,IntegerRational[] outputExpectations,long energyConsumption) {


    public SerializedMachine_MachineRecipe toSerializedRecipe(ItemStack returnStack){
        return new SerializedMachine_MachineRecipe(
                returnStack,
                new MachineRecipeInTicks(CHAMBER_INTERVAL, EmptyArrays.EMPTY_ITEM_STACK_ARRAY, outputs),
                energyConsumption
                , 1
                , outputExpectations);
    }
}
