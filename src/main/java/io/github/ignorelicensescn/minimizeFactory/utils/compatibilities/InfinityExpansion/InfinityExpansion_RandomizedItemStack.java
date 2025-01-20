package io.github.ignorelicensescn.minimizeFactory.utils.compatibilities.InfinityExpansion;

import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.concurrent.ThreadLocalRandom;

public class InfinityExpansion_RandomizedItemStack extends ItemStack {

    public final ItemStack[] items;

    public InfinityExpansion_RandomizedItemStack(ItemStack... outputs) {
        super(outputs[0]);
        this.items = outputs;
    }

    @Nonnull
    @Override
    public ItemStack clone() {
        return this.items[ThreadLocalRandom.current().nextInt(this.items.length)].clone();
    }

}