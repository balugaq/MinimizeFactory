package io.github.ignorelicensescn.minimizefactory.items.machine;

import io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static io.github.ignorelicensescn.minimizefactory.items.Registers.MACHINE_STABILIZER;
import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.properties;
import static io.github.ignorelicensescn.minimizefactory.utils.itemstackrelated.ItemStackUtil.isItemStackSimilar;
import static io.github.thebusybiscuit.slimefun4.libraries.commons.lang.ArrayUtils.EMPTY_INT_ARRAY;
import static io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils.getEmptyClickHandler;

public class MachineRecipeDeserializer extends SlimefunItem {
    public static final int[] border = new int[]{
            27,28,29,         33,34,35
    };
    public static final int button = 31;
    public static final int inputBorder = 30;
    public static final int outputBorder = 32;
    public static final int[] input = new int[]{
            0,1,2,3,4,5,6,7,8,
            9,10,11,12,13,14,15,16,17,
            18,19,20,21,22,23,24,25,26
    };
    public static final int[] outputSlots = new int[]{
            36,37,38,39,40,41,42,43,44,
            45,46,47,48,49,50,51,52
    };
    public static final ItemStack DESERIALIZER_INPUT_HINT_ITEM = new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("MachineRecipeDeserializer_Input_Serialized_Material_Machine_Stabilizer"));
    public static final ItemStack DESERIALIZER_OUTPUT_HINT_ITEM = new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("MachineRecipeDeserializer_Output"));
    public static final ItemStack DESERIALIZER_DESERIALIZE_ITEM = new CustomItemStack(Material.YELLOW_STAINED_GLASS_PANE,properties.getReplacedProperty("MachineRecipeDeserializer_Deserialize"));
    public MachineRecipeDeserializer(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        new BlockMenuPreset(item.getItemId(),item.getDisplayName()==null?"":item.getDisplayName()){
            @Override
            public void init() {
                borderInit(this);
                buttonInit(this);
            }

            @Override
            public void newInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
                super.newInstance(menu, b);
                buttonInit(menu);
            }

            @Override
            public boolean canOpen(@Nonnull Block b, @Nonnull Player p) {
                return p.hasPermission("slimefun.inventory.bypass")
                        || Slimefun.getProtectionManager().hasPermission(p, b.getLocation(),
                        Interaction.INTERACT_BLOCK);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow itemTransportFlow) {
                return EMPTY_INT_ARRAY;
            }

        };
        addItemHandler(new BlockBreakHandler(false,false) {
            @Override
            @ParametersAreNonnullByDefault
            public void onPlayerBreak(BlockBreakEvent blockBreakEvent, ItemStack itemStack, List<ItemStack> list) {
                BlockStorage.getInventory(blockBreakEvent.getBlock()).dropItems(blockBreakEvent.getBlock().getLocation(),input);
                BlockStorage.getInventory(blockBreakEvent.getBlock()).dropItems(blockBreakEvent.getBlock().getLocation(), outputSlots);
            }
        });
    }

    public static void borderInit(BlockMenuPreset preset){
        for (int i:border){
            preset.addItem(i, new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE, ""), getEmptyClickHandler());
        }
        preset.addItem(inputBorder, DESERIALIZER_INPUT_HINT_ITEM.clone(), getEmptyClickHandler());
        preset.addItem(outputBorder, DESERIALIZER_OUTPUT_HINT_ITEM.clone(), getEmptyClickHandler());
        preset.addItem(53,new ItemStack(Material.GRAY_STAINED_GLASS_PANE), getEmptyClickHandler());
    }
    public static void buttonInit(BlockMenuPreset menu){
        menu.addItem(button, DESERIALIZER_DESERIALIZE_ITEM.clone(), getEmptyClickHandler());
    }
    public static void buttonInit(BlockMenu menu){
        menu.replaceExistingItem(button, DESERIALIZER_DESERIALIZE_ITEM.clone());
        menu.addMenuClickHandler(button, (p, slot, item, action) -> {
            for (int i:input){
                ItemStack itemStack = menu.getItemInSlot(i);
                Optional<SerializedMachine_MachineRecipe> optional = SerializedMachine_MachineRecipe.retrieveFromItemStack(itemStack);
                optional.ifPresent(machineRecipe -> {
                    outputItem(menu,MACHINE_STABILIZER.clone(), outputSlots,itemStack.getAmount());
                    if (machineRecipe.sfItemStack != null){
                        ItemStack out = machineRecipe.sfItemStack.clone();
                        outputItem(menu,out, outputSlots,itemStack.getAmount() * out.getAmount());
                    }
                    if (machineRecipe.extraItems != null){
                        for (ItemStack itemStack1:machineRecipe.extraItems){
                            ItemStack out = itemStack1.clone();
                            outputItem(menu, out, outputSlots,itemStack.getAmount() * out.getAmount());
                        }
                    }
                    menu.replaceExistingItem(i,null);
                });
            }
            return false;
        });
//        menu.replaceExistingItem(53,null);
    }
    public static void outputItem(BlockMenu menu,ItemStack itemStack,int[] slots,int itemAmount){
        if (itemAmount == 0){return;}
        if (itemStack == null){return;}
        if (itemStack.getType().equals(Material.AIR)){return;}
        int counter = 0;
        for (int i:slots){
            ItemStack itemInOutputSlot = menu.getItemInSlot(i);
            if (itemInOutputSlot == null || itemInOutputSlot.getType().equals(Material.AIR)){
                if (itemAmount >= itemStack.getMaxStackSize()){
                    itemAmount -= itemStack.getMaxStackSize();
                    ItemStack out = itemStack.clone();
                    out.setAmount(itemStack.getMaxStackSize());
                    menu.replaceExistingItem(i,out);
                    outputItem(menu,itemStack, Arrays.copyOfRange(slots,counter + 1, slots.length),itemAmount);
                }else {
                    ItemStack out = itemStack.clone();
                    out.setAmount(itemAmount);
                    menu.replaceExistingItem(i,out);
                }
                return;
            }
            if (isItemStackSimilar(itemInOutputSlot,itemStack)){
                int max = itemInOutputSlot.getMaxStackSize();
                int current = itemInOutputSlot.getAmount();
                int canAdd = max-current;
                if (canAdd >= itemAmount){
                    itemInOutputSlot.setAmount(itemInOutputSlot.getAmount() + itemAmount);
                    return;
                }else if (canAdd > 0){
                    itemInOutputSlot.setAmount(itemInOutputSlot.getMaxStackSize());
                    itemAmount -= canAdd;
                    outputItem(menu,itemStack, Arrays.copyOfRange(slots,counter + 1, slots.length),itemAmount);
                    return;
                }
                else {
                    outputItem(menu,itemStack, Arrays.copyOfRange(slots,counter + 1, slots.length),itemAmount);
                    return;
                }
            }
            counter += 1;
        }
        ItemStack out = itemStack.clone();
        out.setAmount(out.getMaxStackSize());
        Location l = menu.getLocation();
        World w = l.getWorld();
        for (int i=0;i<(itemAmount / out.getMaxStackSize());i+=1)
        {
            if (w != null){
                w.dropItemNaturally(l,out.clone());
            }
        }
        itemAmount = itemAmount % out.getMaxStackSize();
        if (itemAmount == 0){return;}
        out.setAmount(itemAmount);
        if (w != null){
            w.dropItemNaturally(l,out.clone());
        }
    }
}
