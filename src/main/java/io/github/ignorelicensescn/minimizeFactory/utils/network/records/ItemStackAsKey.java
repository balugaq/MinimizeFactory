package io.github.ignorelicensescn.minimizeFactory.utils.network.records;

import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public record ItemStackAsKey(Material type,ItemMetaAsKey meta,int hash) {

    public ItemStackAsKey(ItemStack stack){
        //i want multi lines but it seems not allowed :(
        this(stack==null?null:stack.getType(),
                stack==null
                        ?null
                        :stack.hasItemMeta()
                            ?new ItemMetaAsKey(stack.getItemMeta(), stack.getType())
                            :null);

    }
    public ItemStackAsKey(Material type,ItemMetaAsKey meta){
        this(type,
                meta,
                31 * (type != null ? type.hashCode() : 0)
                        + (meta != null ? meta.hashCode() : 0)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemStackAsKey that = (ItemStackAsKey) o;
        ItemStack thisStack = new ItemStack(type(),1);
        thisStack.setItemMeta(meta());

        ItemStack thatStack = new ItemStack(
                this.type(),//yes,material different happens when some updates like InfinityExpansion changes its material.
                1
        );
        thatStack.setItemMeta(that.meta());
        if (Objects.equals(meta, that.meta)){return true;}
        if (SlimefunUtils.isItemSimilar(thisStack,thatStack,false,false,true)){
            return true;
        }
        return Objects.equals(this.type(),that.type());
    }

    @Override
    public int hashCode() {
        return hash();
    }
}
