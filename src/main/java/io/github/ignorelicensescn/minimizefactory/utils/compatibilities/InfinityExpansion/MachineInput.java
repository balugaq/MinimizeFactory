package io.github.ignorelicensescn.minimizefactory.utils.compatibilities.InfinityExpansion;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MachineInput {

    final List<ItemStack> items = new ArrayList<>(2);
    int amount;

    public MachineInput(ItemStack item) {
        add(item);
    }

    public MachineInput add(ItemStack item) {
        items.add(item);
        amount += item.getAmount();
        return this;
    }

}
