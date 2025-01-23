package io.github.ignorelicensescn.minimizefactory.items.machine.network;

import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations.CoreLocationOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;
import io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.NodeType;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.properties;

public class MachineNetworkBridge extends NetworkNode{
    private static final BlockBreakHandler BRIDGE_BREAK_HANDLER = new BlockBreakHandler(false, false) {

        @Override
        @ParametersAreNonnullByDefault
        public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
            Block b = e.getBlock();
            Player p = e.getPlayer();
            SerializeFriendlyBlockLocation key = SerializeFriendlyBlockLocation.fromLocation(b.getLocation());
            if (
                    CoreLocationOperator.INSTANCE.get(key) != null
            ) {
                p.sendMessage(properties.getReplacedProperty("MachineNetworkStorage_Core_Bounded"));
                e.setCancelled(true);
                return;
            }
            removeNode(key);
        }
    };
    private static final BlockPlaceHandler BRIDGE_PLACE_HANDLER =
            new BlockPlaceHandler(false) {
                @Override
                public void onPlayerPlace(@Nonnull BlockPlaceEvent e) {

                    initNode(SerializeFriendlyBlockLocation.fromLocation(e.getBlock().getLocation()),NodeType.BRIDGE);
                }
            };

    public MachineNetworkBridge(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack out) {
        super(itemGroup, item, recipeType, recipe, NodeType.BRIDGE, out);
        addItemHandler(BRIDGE_PLACE_HANDLER, BRIDGE_BREAK_HANDLER);
    }

}
