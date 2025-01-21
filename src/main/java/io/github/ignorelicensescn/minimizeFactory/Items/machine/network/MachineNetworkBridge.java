package io.github.ignorelicensescn.minimizeFactory.Items.machine.network;

import io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.NodeType;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.properties;
import static io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.NodeKeys.MINIMIZEFACTORY_CORE_LOCATION;

public class MachineNetworkBridge extends NetworkNode{
    public MachineNetworkBridge(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack out) {
        super(itemGroup, item, recipeType, recipe, NodeType.BRIDGE, out);
        addItemHandler(
                new BlockPlaceHandler(false) {
                    @Override
                    public void onPlayerPlace(@Nonnull BlockPlaceEvent e) {

                        initNode(e.getBlock().getLocation());
                    }
                },
                new BlockBreakHandler(false, false) {

                    @Override
                    @ParametersAreNonnullByDefault
                    public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
                        Block b = e.getBlock();
                        Player p = e.getPlayer();

                        if (BlockStorage.getBlockInfoAsJson(b).contains(MINIMIZEFACTORY_CORE_LOCATION)) {
                            p.sendMessage(properties.getReplacedProperty("MachineNetworkStorage_Core_Bounded"));
                            e.setCancelled(true);
                            return;
                        }
                        removeNode(e.getBlock().getLocation());
                    }
                });
    }

}
