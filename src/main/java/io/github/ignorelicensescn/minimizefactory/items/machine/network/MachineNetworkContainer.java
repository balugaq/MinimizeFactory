package io.github.ignorelicensescn.minimizefactory.items.machine.network;

import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations.CoreLocationOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;
import io.github.ignorelicensescn.minimizefactory.utils.NameUtil;
import io.github.ignorelicensescn.minimizefactory.utils.chestmenubuilds.NodeMenuBuildUtils;
import io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.NodeType;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.List;
import java.util.Objects;

import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.properties;
import static io.github.thebusybiscuit.slimefun4.libraries.commons.lang.ArrayUtils.EMPTY_INT_ARRAY;

public class MachineNetworkContainer extends NetworkNode{
    private static final BlockBreakHandler CONTAINER_BREAK_HANDLER = new BlockBreakHandler(false,false) {
        @Override
        @ParametersAreNonnullByDefault
        public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
            Block b = e.getBlock();
            SerializeFriendlyBlockLocation key = SerializeFriendlyBlockLocation.fromLocation(b.getLocation());
            Player p = e.getPlayer();
            if (CoreLocationOperator.INSTANCE.has(key))
            {
                p.sendMessage(properties.getReplacedProperty("MachineNetworkStorage_Core_Bounded"));
                e.setCancelled(true);
                return;
            }
            removeNode(key);
            BlockStorage.getInventory(e.getBlock()).dropItems(e.getPlayer().getLocation(), STABILIZER_INPUT_SLOTS);
        }
    };
    private static final BlockPlaceHandler CONTAINER_PLACE_HANDLER = new BlockPlaceHandler(false) {
        @Override
        public void onPlayerPlace(@Nonnull BlockPlaceEvent e) {
            initNode(SerializeFriendlyBlockLocation.fromLocation(e.getBlock().getLocation()),NodeType.MACHINE_CONTAINER);
        }
    };
    public static final int[] STABILIZER_INPUT_SLOTS = new int[]{
            0,1,2,3,4,5,6,7,8,
            9,10,11,12,13,14,15,16,17,
            18,19,20,21,22,23,24,25,26,
            27,28,29,30,31,32,33,34,35,
            36,37,38,39,40,41,42,43,44,
            45,46,47,48,49,50,51,52
    };
    public static final int STATUS_BORDER = 53;
    public MachineNetworkContainer(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe, NodeType.MACHINE_CONTAINER);
        new BlockMenuPreset(getId(),getItemName()){


            @Override
            public void init() {
                this.addItem(STATUS_BORDER, new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, ""), (p, slot, item1, action) -> false);
            }

            @Override
            public void newInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
                super.newInstance(menu, b);
                showCoreLocation(b.getLocation(),STATUS_BORDER,menu);
            }

            @Override
            public boolean canOpen(@Nonnull Block b, @Nonnull Player p) {
                return p.hasPermission("slimefun.inventory.bypass")
                        || Slimefun.getProtectionManager().hasPermission(p, b.getLocation(),
                        Interaction.INTERACT_BLOCK);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return EMPTY_INT_ARRAY;
            }
        };
        addItemHandler(CONTAINER_PLACE_HANDLER,CONTAINER_BREAK_HANDLER);
    }
    public static void showCoreLocation(Location nodeLocation,int hintSlot){
        showCoreLocation(nodeLocation,hintSlot,BlockStorage.getInventory(nodeLocation));
    }
    public static void showCoreLocation(Location nodeLocation, int hintSlot, BlockMenu nodeMenu){
        SerializeFriendlyBlockLocation nodeLocationKey = SerializeFriendlyBlockLocation.fromLocation(nodeLocation);
        boolean lockFlag = isLocked(nodeLocationKey);
        NodeMenuBuildUtils.showCoreLocation(nodeLocation,hintSlot,nodeMenu);
        if (lockFlag){
            lockInputs(nodeMenu);
        }else {
            unlockInputs(nodeMenu);
        }
    }
    public static final ItemStack LOCKED_SLOT = new CustomItemStack(Material.BARRIER, properties.getReplacedProperty("MachineNetworkStorage_Locked"));
    public static void lockInputs(BlockMenu nodeMenu){
        for (int i: STABILIZER_INPUT_SLOTS){
            nodeMenu.addMenuClickHandler(i, ChestMenuUtils.getEmptyClickHandler());
            if (nodeMenu.getItemInSlot(i) == null){
                nodeMenu.replaceExistingItem(i,LOCKED_SLOT);
            }
        }
    }
    public static void unlockInputs(BlockMenu nodeMenu){
        for (int i: STABILIZER_INPUT_SLOTS){
            nodeMenu.addMenuClickHandler(i, null);
            ItemStack inSlot = nodeMenu.getItemInSlot(i);
            if (inSlot != null){
                if (Objects.equals(NameUtil.findName(inSlot), properties.getReplacedProperty("MachineNetworkStorage_Locked"))){
                    nodeMenu.replaceExistingItem(i,null);
                }
            }
        }
    }
}
