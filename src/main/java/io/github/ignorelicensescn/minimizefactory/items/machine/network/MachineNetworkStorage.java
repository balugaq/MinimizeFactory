package io.github.ignorelicensescn.minimizefactory.items.machine.network;

import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.implementations.StorageInfoSerializer;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations.CoreLocationOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations.DataRemover;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.StorageInfo;
import io.github.ignorelicensescn.minimizefactory.utils.namemateriallore.NameUtil;
import io.github.ignorelicensescn.minimizefactory.utils.chestmenubuilds.NodeMenuBuildUtils;
import io.github.ignorelicensescn.minimizefactory.utils.itemstackrelated.ItemStackUtil;
import io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.NodeType;
import io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.StorageUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.properties;
import static io.github.thebusybiscuit.slimefun4.libraries.commons.lang.ArrayUtils.EMPTY_INT_ARRAY;

/**
 * a chest from FluffyMachines
 * <p>Tweaked to fit in network</p>
 */
public class MachineNetworkStorage extends NetworkNode{
    private static final BlockPlaceHandler STORAGE_PLACE_HANDLER = new BlockPlaceHandler(false) {
        @Override
        public void onPlayerPlace(@Nonnull BlockPlaceEvent e) {
            initNode(SerializeFriendlyBlockLocation.fromLocation(e.getBlock().getLocation()),NodeType.STORAGE);
        }
    };
    private static final BlockBreakHandler STORAGE_BREAK_HANDLER = new BlockBreakHandler(false, false) {
        @Override
        public void onPlayerBreak(@Nonnull BlockBreakEvent e, @Nonnull ItemStack item, @Nonnull List<ItemStack> drops) {
            try (StorageInfoSerializer storageInfoSerializer = StorageInfoSerializer.getInstance()){
                Block b = e.getBlock();
                Player p = e.getPlayer();

                SerializeFriendlyBlockLocation storageLocationKey = SerializeFriendlyBlockLocation.fromLocation(b.getLocation());
                StorageInfo storageInfo = storageInfoSerializer.getOrDefault(storageLocationKey);

                BlockMenu inv = BlockStorage.getInventory(b);

                BigInteger stored = storageInfo.storeAmount;


                boolean hasCore = CoreLocationOperator.INSTANCE.has(SerializeFriendlyBlockLocation.fromLocation(b.getLocation()));
                if (hasCore) {
                    p.sendMessage(properties.getReplacedProperty("MachineNetworkStorage_Core_Bounded"));
                    e.setCancelled(true);
                    return;
                }

                if (inv != null) {

                    BigInteger itemCount = BigInteger.ZERO;
                    int storedCompareToZero = stored.compareTo(BigInteger.ZERO);

                    for (Entity en : p.getNearbyEntities(5, 5, 5)) {
                        if (en instanceof Item) {
                            itemCount = itemCount.add(BigInteger.ONE);
                        }
                    }

                    if (itemCount.compareTo(BIGINTEGER_FIVE) > 0) {
                        p.sendMessage(properties.getReplacedProperty("MachineNetworkStorage_FluffyMachine_Item_Nearby"));
                        e.setCancelled(true);
                        return;
                    }

                    inv.dropItems(b.getLocation(), INPUT_SLOTS);
                    inv.dropItems(b.getLocation(), OUTPUT_SLOTS);

                    if (storedCompareToZero > 0) {
                        int stackSize = storageInfo.storeItem.getMaxStackSize();
                        ItemStack unKeyed = getStoredItem(storageInfo);

                        if (ItemStackUtil.isItemStackSimilar(unKeyed, EMPTY_ITEM)) {
                            storageInfo.storeAmount = BigInteger.ZERO;
//                            setStored(b, BigInteger.ZERO);
                            updateMenu(inv, true,storageInfo,storageLocationKey,storageInfoSerializer);
                            return;
                        }

                        if (stored.compareTo(OVERFLOW_AMOUNT) > 0) {
                            p.sendMessage(properties.getReplacedProperty("MachineNetworkStorage_FluffyMachine_Dropping_Item_Part1")
                                    + OVERFLOW_AMOUNT + properties.getReplacedProperty("MachineNetworkStorage_FluffyMachine_Dropping_Item_Part2"));
                            int toRemove = 3240;
                            while (toRemove >= stackSize) {
                                unKeyed = unKeyed.clone();
                                unKeyed.setAmount(stackSize);
                                b.getWorld().dropItemNaturally(b.getLocation(), unKeyed.clone());
                                toRemove = toRemove - stackSize;
                            }
                            if (toRemove > 0) {
                                unKeyed = unKeyed.clone();
                                unKeyed.setAmount(toRemove);
                                b.getWorld().dropItemNaturally(b.getLocation(), unKeyed.clone());
                            }
                            storageInfo.storeAmount = stored.subtract(OVERFLOW_AMOUNT);
//                            setStored(b, stored.subtract(OVERFLOW_AMOUNT));
                            updateMenu(inv, false,storageInfo,storageLocationKey,storageInfoSerializer);
                            e.setCancelled(true);
                        } else {
                            BigInteger stackSizeBigInteger = BigInteger.valueOf(stackSize);
                            // Everything greater than 1 stack
                            while (stored.compareTo(stackSizeBigInteger) > 0) {
                                unKeyed = unKeyed.clone();
                                unKeyed.setAmount(stackSize);
                                b.getWorld().dropItemNaturally(b.getLocation(), unKeyed.clone());

                                stored = stored.subtract(stackSizeBigInteger);
                            }

                            // Drop remaining, if there is any
                            if (stored.compareTo(BigInteger.ZERO) > 0) {
                                unKeyed = unKeyed.clone();
                                unKeyed.setAmount(stored.intValue());
                                b.getWorld().dropItemNaturally(b.getLocation(), unKeyed.clone());
                            }

                            // In case they use an explosive pickaxe
                            storageInfo.storeAmount = BigInteger.ZERO;
//                            setStored(b, BigInteger.ZERO);
                            updateMenu(inv, true,storageInfo,storageLocationKey,storageInfoSerializer);
                            {
                                DataRemover.INSTANCE.remove(storageLocationKey);
                            }
                        }
                    }
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
    };
    
    public static final ItemStack EMPTY_ITEM = new CustomItemStack(Material.BARRIER, properties.getReplacedProperty("MachineNetworkStorage_Empty"));
    public static final BigInteger BIGINTEGER_FIVE = BigInteger.valueOf(5);
    public static final int[] inputBorder = {9, 10, 11, 12, 18, 21, 27, 28, 29, 30};
    public static final int[] outputBorder = {14, 15, 16, 17, 23, 26, 32, 33, 34, 35, 36};
    public static final int[] plainBorder = {0, 1, 2, 3, 4, 5, 6, 7, 8, 13, 36, 37, 38, 39, 40, 41, 42, 43, 44};

    public static final int[] INPUT_SLOTS = {19, 20};
    public static final int[] OUTPUT_SLOTS = {24, 25};
    public static final int STATUS_SLOT = 22;
    public static final int DISPLAY_SLOT = 31;
    public static final int HINT_SLOT = 0;
    public static final int CHECK_SLIMEFUN_ITEM_SLOT = 4;

    public static final int OVERFLOW_AMOUNT_INTEGER = 3240;
    public static final BigInteger OVERFLOW_AMOUNT = BigInteger.valueOf(OVERFLOW_AMOUNT_INTEGER);
    public static final DecimalFormat STORAGE_INDICATOR_FORMAT = new DecimalFormat("###,###.####",
            DecimalFormatSymbols.getInstance(Locale.ROOT));

    public MachineNetworkStorage(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe, NodeType.STORAGE);

        //auto-put
        new BlockMenuPreset(getId(), getItemName()) {

            @Override
            public void init() {
                constructMenu(this);
            }

            @Override
            public void newInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
                try (StorageInfoSerializer storageInfoSerializer = StorageInfoSerializer.getInstance()){
                    buildMenu(menu, b, storageInfoSerializer);
                }catch (Exception e){
                    e.printStackTrace();
                }
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

            @Override
            public int[] getSlotsAccessedByItemTransport(DirtyChestMenu menu, ItemTransportFlow flow, ItemStack item) {
                if (flow == ItemTransportFlow.INSERT) {
                    return INPUT_SLOTS;
                }
                else if (flow == ItemTransportFlow.WITHDRAW) {
                    return OUTPUT_SLOTS;
                } else {
                    return EMPTY_INT_ARRAY;
                }
            }
        };

        addItemHandler(STORAGE_BREAK_HANDLER, STORAGE_PLACE_HANDLER);
    }

    private void constructMenu(BlockMenuPreset preset) {
        StorageUtils.createBorder(preset, ChestMenuUtils.getOutputSlotTexture(), outputBorder);
//        StorageUtils.createBorder(preset, ChestMenuUtils.getInputSlotTexture(), inputBorder);
        StorageUtils.createBorder(preset, ChestMenuUtils.getBackground(), inputBorder);
        ChestMenuUtils.drawBackground(preset, plainBorder);
    }

    protected void buildMenu(BlockMenu menu, Block b, StorageInfoSerializer storageInfoSerializer) {

        // Initialize an empty barrel
        SerializeFriendlyBlockLocation storageLocationKey = SerializeFriendlyBlockLocation.fromLocation(b.getLocation());
        StorageInfo storageInfo = storageInfoSerializer.getOrDefault(storageLocationKey);

        BigInteger stored = storageInfo.storeAmount;

        updateStoredStacks(menu,storageInfo.storeItem,stored);
//        menu.replaceExistingItem(DISPLAY_SLOT, StorageUtils.keyItem(storageInfo.storeItem));

        // Every time setup
        menu.addMenuClickHandler(STATUS_SLOT, ChestMenuUtils.getEmptyClickHandler());
        menu.addMenuClickHandler(DISPLAY_SLOT, ChestMenuUtils.getEmptyClickHandler());

        // Insert all
        int INSERT_ALL_SLOT = 43;
        menu.replaceExistingItem(INSERT_ALL_SLOT,
                new CustomItemStack(Material.LIME_STAINED_GLASS_PANE, properties.getReplacedProperty("MachineNetworkStorage_Insert_All"),
                        properties.getReplacedProperties("MachineNetworkStorage_Insert_All_Lore_1",ChatColor.GRAY)));
        menu.addMenuClickHandler(INSERT_ALL_SLOT, (pl, slot, item, action) -> {
            if(!isLocked(SerializeFriendlyBlockLocation.fromLocation(b.getLocation()))){insertAll(pl, menu, b);}
            return false;
        });

        // Extract all
        int EXTRACT_SLOT = 44;
        menu.replaceExistingItem(EXTRACT_SLOT,
                new CustomItemStack(Material.RED_STAINED_GLASS_PANE, properties.getReplacedProperty("MachineNetworkStorage_Extract_All"),
                        properties.getReplacedProperties("MachineNetworkStorage_Extract_All_Lore_1",ChatColor.GRAY)
                ));
        menu.addMenuClickHandler(EXTRACT_SLOT, (pl, slot, item, action) -> {
            if(!isLocked(SerializeFriendlyBlockLocation.fromLocation(b.getLocation()))){extract(pl, menu, b, action);}
            return false;
        });


        showCoreLocation(b.getLocation(),HINT_SLOT,menu);
    }

    @Override
    public void preRegister() {
        addItemHandler(ticker);
    }

    private static final BlockTicker ticker = new BlockTicker() {

        @Override
        public void tick(Block b, SlimefunItem sf, Config data) {
            
            if(isLocked(SerializeFriendlyBlockLocation.fromLocation(b.getLocation()))){return;}
            BlockMenu inv = BlockStorage.getInventory(b);


            for (int ignored : OUTPUT_SLOTS) {
                pushOutput(inv, b);
            }
        }

        @Override
        public boolean isSynchronized() {
            return true;
        }
    };

    static void pushOutput(BlockMenu inv, Block b) {
        SerializeFriendlyBlockLocation storageLocationKey = SerializeFriendlyBlockLocation.fromLocation(b.getLocation());
        try (StorageInfoSerializer storageInfoSerializer = StorageInfoSerializer.getInstance()){
            StorageInfo storageInfo = storageInfoSerializer.getOrDefault(storageLocationKey);
            ItemStack displayItem = storageInfo.storeItem;
            if (displayItem != null && displayItem.getType() != Material.BARRIER) {
                BigInteger stored = storageInfo.storeAmount;
                // Output stack
                if (stored.compareTo(BigInteger.valueOf(displayItem.getMaxStackSize())) > 0) {
                    ItemStack clone = StorageUtils.unKeyItem(displayItem);
                    clone.setAmount(clone.getMaxStackSize());
                    if (inv.fits(clone, OUTPUT_SLOTS)) {
                        int amount = clone.getMaxStackSize();

//                        setStored(b, stored.subtract(BigInteger.valueOf(amount)));
                        storageInfo.storeAmount = storageInfo.storeAmount.subtract(BigInteger.valueOf(amount));
                        inv.pushItem(clone, OUTPUT_SLOTS);
                        updateMenu(inv, false, storageInfo, storageLocationKey,storageInfoSerializer);
                    }
                } else if (stored.compareTo(BigInteger.ZERO) == 0) {   // Output remaining

                    ItemStack clone = StorageUtils.unKeyItem(displayItem);
                    clone.setAmount(stored.intValue());

                    if (inv.fits(clone, OUTPUT_SLOTS)) {
//                        setStored(b, BigInteger.ZERO);
                        storageInfo.storeAmount = BigInteger.ZERO;
                        storageInfo.storeItem = MachineNetworkStorage.EMPTY_ITEM;
                        inv.pushItem(clone, OUTPUT_SLOTS);
                        updateMenu(inv, false, storageInfo, storageLocationKey,storageInfoSerializer);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void updateStoredStacks(BlockMenu inv, ItemStack storedItem, BigInteger stored){
        if (BigInteger.ZERO.compareTo(stored) < 0){
            BigDecimal storedBigDecimal = new BigDecimal(stored);
            BigDecimal maxStackSizeBigDecimal = BigDecimal.valueOf(storedItem.getMaxStackSize());
            String storedStacks = doubleRoundAndFade(storedBigDecimal.divide(maxStackSizeBigDecimal, 4, RoundingMode.DOWN));

            inv.replaceExistingItem(STATUS_SLOT, new CustomItemStack(
                    Material.LIME_STAINED_GLASS_PANE, NameUtil.findName(storedItem),
                    properties.getReplacedProperty("MachineNetworkStorage_Items_Stored") + stored,
                    ChatColor.GRAY + storedStacks + properties.getReplacedProperty("MachineNetworkStorage_Stacks")));
            inv.replaceExistingItem(DISPLAY_SLOT, StorageUtils.keyItem(storedItem));
        }else {
            inv.replaceExistingItem(STATUS_SLOT, new CustomItemStack(
                    Material.LIME_STAINED_GLASS_PANE, NameUtil.findName(EMPTY_ITEM),
                    properties.getReplacedProperty("MachineNetworkStorage_Items_Stored_0")));
            inv.replaceExistingItem(DISPLAY_SLOT, StorageUtils.keyItem(EMPTY_ITEM));
        }
    }

    /**
     * This method updates the barrel's menu and hologram displays
     * (modified:will save storageInfo)
     * @param b   is the barrel block
     * @param inv is the barrel's inventory
     */
    public static void updateMenu(BlockMenu inv, boolean force, StorageInfo storageInfo,
                                  SerializeFriendlyBlockLocation storageLocationKey,
                                  @Nullable StorageInfoSerializer serializer) {

        try{
            ItemStack storedItem = storageInfo.storeItem;
            BigInteger stored = storageInfo.storeAmount;

            // This helps a bit with lag, but may have visual impacts
            if (inv.hasViewer() || force) {
                updateStoredStacks(inv,storedItem,stored);
            }

            checkEmpty(inv,storageInfo);

            if (serializer == null){
                try (StorageInfoSerializer storageInfoSerializer = StorageInfoSerializer.getInstance()){
                    storageInfoSerializer.saveToLocationNoThrow(storageInfo,storageLocationKey);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                serializer.saveToLocationNoThrow(storageInfo,storageLocationKey);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static boolean checkEmpty(BlockMenu inv,StorageInfo storageInfo){
        BigInteger stored = storageInfo.storeAmount;
        try {
            if (stored.compareTo(BigInteger.ZERO) == 0) {
                inv.replaceExistingItem(DISPLAY_SLOT, EMPTY_ITEM);
                if (!ItemStackUtil.isItemStackSimilar(storageInfo.storeItem,EMPTY_ITEM)){
                    storageInfo.storeItem = EMPTY_ITEM;
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void insertAll(Player p, BlockMenu menu, Block b) {

        SerializeFriendlyBlockLocation storageLocationKey = SerializeFriendlyBlockLocation.fromLocation(b.getLocation());
        try (StorageInfoSerializer storageInfoSerializer = StorageInfoSerializer.getInstance()) {
            StorageInfo storageInfo = storageInfoSerializer.getOrDefault(storageLocationKey);
            ItemStack storedItem = storageInfo.storeItem;

            PlayerInventory inv = p.getInventory();

            BigInteger stored = storageInfo.storeAmount;

            for (int i = 0; i < inv.getContents().length; i += 1) {
                ItemStack item = inv.getItem(i);
                if (item == null) {
                    continue;
                }
                int amount = item.getAmount();
                if (ItemStackUtil.isItemStackSimilar(item, storedItem)) {
                    inv.setItem(i, null);
                    stored = stored.add(BigInteger.valueOf(amount));
                }
            }
            updateMenu(menu, false,storageInfo,storageLocationKey,storageInfoSerializer);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void extract(Player p, BlockMenu menu, Block b, ClickAction action) {
        boolean infoChangedFlag = false;
        SerializeFriendlyBlockLocation storageLocationKey = SerializeFriendlyBlockLocation.fromLocation(b.getLocation());
        StorageInfo storageInfo = null;
        try (StorageInfoSerializer storageInfoSerializer = StorageInfoSerializer.getInstance()){
            storageInfo = storageInfoSerializer.getOrDefault(storageLocationKey);
            ItemStack storedItem = getStoredItem(storageInfo);
            if (storedItem == null) {
                return;
            }
            ItemStack storedClone = storedItem.clone();

            PlayerInventory inv = p.getInventory();
            BigInteger stored = storageInfo.storeAmount;

            // Extract single
            if (action.isRightClicked()) {
                if (stored.compareTo(BigInteger.ZERO) > 0) { // Extract from stored
                    StorageUtils.giveOrDropItem(p, storedClone);
                    stored = stored.subtract(BigInteger.ONE);
                    storageInfo.storeAmount = stored;
                    infoChangedFlag = true;
                    return;
                } else {
                    for (int slot : OUTPUT_SLOTS) { // Extract from slot
                        ItemStack inSlot = menu.getItemInSlot(slot);
                        if (inSlot != null) {
                            StorageUtils.giveOrDropItem(p, inSlot);
                            menu.consumeItem(slot);
                            return;
                        }
                    }
                }
                return;
            }

//            if (storedItem.getType() == Material.BARRIER) {
//                return;
//            }

            // Extract all
            ItemStack[] contents = inv.getStorageContents().clone();
            int maxStackSize = storedItem.getMaxStackSize();
            BigInteger maxStackSizeBigInteger = BigInteger.valueOf(maxStackSize);
            int outI = 0;

            for (int i = 0; i < contents.length; i += 1) {

                if (contents[i] == null) {
                    if (stored.compareTo(maxStackSizeBigInteger) > 0) {
                        storedItem = storedItem.clone();
                        storedItem.setAmount(maxStackSize);
                        inv.setItem(i, storedItem.clone());
                        stored = stored.subtract(maxStackSizeBigInteger);
                    } else if (stored.compareTo(BigInteger.ZERO) > 0) {
                        storedItem = storedItem.clone();
                        storedItem.setAmount(stored.intValue());
                        inv.setItem(i, storedItem.clone());
                        stored = BigInteger.ZERO;
                    } else {
                        if (outI > 1) {
                            break;
                        }

                        ItemStack item = menu.getItemInSlot(OUTPUT_SLOTS[outI]);

                        if (item == null) {
                            continue;
                        }

                        inv.setItem(i, item.clone());
                        menu.replaceExistingItem(OUTPUT_SLOTS[outI], null);

                        outI += 1;
                    }
                }
            }
            storageInfo.storeAmount = stored;
            infoChangedFlag = true;
//            setStored(b, stored);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (infoChangedFlag){
                updateMenu(menu, false,storageInfo,storageLocationKey,null);
            }
        }
    }

    //I remember I stole this from FluffyMachines.
    public static String doubleRoundAndFade(BigDecimal num) {
        // Using same format that is used on lore power
        String formattedString = STORAGE_INDICATOR_FORMAT.format(num);
        if (formattedString.indexOf('.') != -1) {
            return formattedString.substring(0, formattedString.indexOf('.')) + ChatColor.DARK_GRAY
                    + formattedString.substring(formattedString.indexOf('.')) + ChatColor.GRAY;
        } else {
            return formattedString;
        }
    }

    public static ItemStack getStoredItem(StorageInfo storageInfo){
        ItemStack result = storageInfo.storeItem;
        if (ItemStackUtil.isItemStackSimilar(result,EMPTY_ITEM)){return null;}
        return result;
    }

    public static void showCoreLocation(Location nodeLocation,int hintSlot){
        showCoreLocation(nodeLocation,hintSlot,BlockStorage.getInventory(nodeLocation));
    }
    public static void showCoreLocation(Location nodeLocation, int hintSlot, BlockMenu nodeMenu){
        NodeMenuBuildUtils.showCoreLocation(nodeLocation,hintSlot,nodeMenu);
    }

    public static boolean isValidStorage(Location l){
        return BlockStorage.check(l) instanceof MachineNetworkStorage;
    }
}
