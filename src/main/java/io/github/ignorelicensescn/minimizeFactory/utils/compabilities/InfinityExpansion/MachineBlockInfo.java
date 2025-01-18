package io.github.ignorelicensescn.minimizeFactory.utils.compabilities.InfinityExpansion;

import java.util.List;

public class MachineBlockInfo {
    public final List<MachineBlockRecipe> recipes;
    public final int ticksPerOutput;

    public MachineBlockInfo(int ticksPerOutput, List<MachineBlockRecipe> recipes) {
        this.ticksPerOutput = ticksPerOutput;
        this.recipes = recipes;
    }
}
