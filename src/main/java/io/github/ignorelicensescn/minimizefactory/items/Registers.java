package io.github.ignorelicensescn.minimizefactory.items;

import io.github.ignorelicensescn.minimizefactory.items.consumptions.MachineStabilizer;
import io.github.ignorelicensescn.minimizefactory.items.machine.MachineRecipeDeserializer;
import io.github.ignorelicensescn.minimizefactory.items.machine.MachineRecipeSerializer;
import io.github.ignorelicensescn.minimizefactory.items.machine.network.*;
import io.github.ignorelicensescn.minimizefactory.items.serializable.SerializeOnly;
import io.github.ignorelicensescn.minimizefactory.MinimizeFactory;
import io.github.ignorelicensescn.minimizefactory.sfgroups.Groups;
import io.github.ignorelicensescn.minimizefactory.utils.EmptyArrays;
import io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizefactory.utils.localmachinerecipe.MachineRecipeInTicks;
import io.github.ignorelicensescn.minimizefactory.utils.simpleStructure.SimplePair;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;

import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.properties;
import static io.github.ignorelicensescn.minimizefactory.utils.EmptyArrays.EMPTY_ITEM_STACK_ARRAY;
import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.*;

/**
 * <p>SlimefunItems declared here will be registered with {@link java.lang.reflect}.So SuppressWarnings</p>
 * Learned from SlimeFrame.
 */
@SuppressWarnings("unused")
public class Registers {
    public static final SlimefunItemStack LENS = new SlimefunItemStack(
            "MINIMIZEFACTORY_LENS",
            Material.LIGHT_BLUE_STAINED_GLASS_PANE,
            properties.getReplacedProperty("Material_Lens"),
            properties.getReplacedProperties("Material_Lens_Description",ChatColor.GRAY).toArray(EmptyArrays.EMPTY_STRING_ARRAY)
    );

    public static final MachineStabilizer.MachineStabilizerStack MACHINE_STABILIZER = new MachineStabilizer.MachineStabilizerStack(
            "MINIMIZEFACTORY_MACHINE_STABILIZER",
            Material.TRAPPED_CHEST,
            properties.getReplacedProperty("Material_Machine_Stabilizer"),
            properties.getReplacedProperties("Material_Machine_Stabilizer_Lore",ChatColor.GRAY).toArray(EmptyArrays.EMPTY_STRING_ARRAY)
    );

    public static final SlimefunItemStack MACHINE_RECIPE_SERAILIZER = new SlimefunItemStack(
            "MINIMIZEFACTORY_MACHINE_RECIPE_SERAILIZER",
            Material.REDSTONE_LAMP,
            properties.getReplacedProperty("Serializer_Name"),
            properties.getReplacedProperties("Serializer_Lore_1",ChatColor.GRAY).toArray(EmptyArrays.EMPTY_STRING_ARRAY)
    );

    public static final SlimefunItemStack MACHINE_DESERIALIZER = new SlimefunItemStack(
            "MINIMIZEFACTORY_MACHINE_DESERIALIZER",
            Material.GLOWSTONE,
            properties.getReplacedProperty("Deserializer_Name"),
            properties.getReplacedProperties("Deserializer_Lore_1",ChatColor.GRAY).toArray(EmptyArrays.EMPTY_STRING_ARRAY)
    );

    public static final SlimefunItemStack MACHINE_NETWORK_STORAGE = new SlimefunItemStack(
            "MINIMIZEFACTORY_MACHINE_NETWORK_STORAGE",
            Material.END_STONE_BRICKS,
            properties.getReplacedProperty("MachineNetworkStorage_Name"),
            properties.getReplacedProperties("MachineNetworkStorage_Lore_1",ChatColor.GRAY).toArray(EmptyArrays.EMPTY_STRING_ARRAY)
    );
    public static final SlimefunItemStack MACHINE_NETWORK_CONTAINER = new SlimefunItemStack(
            "MINIMIZEFACTORY_MACHINE_NETWORK_CONTAINER",
            Material.BRICKS,
            properties.getReplacedProperty("MachineNetworkContainer_Name"),
            properties.getReplacedProperties("MachineNetworkContainer_Lore_1",ChatColor.GRAY).toArray(EmptyArrays.EMPTY_STRING_ARRAY)
    );

    public static final SlimefunItemStack MACHINE_NETWORK_BRIDGE = new SlimefunItemStack(
            "MINIMIZEFACTORY_MACHINE_NETWORK_BRIDGE",
            Material.CYAN_STAINED_GLASS,
            properties.getReplacedProperty("MachineNetworkBridge_Name"),
            properties.getReplacedProperties("MachineNetworkBridge_Lore_1",ChatColor.GRAY).toArray(EmptyArrays.EMPTY_STRING_ARRAY)
    );
    public static final SlimefunItemStack MACHINE_NETWORK_CORE = new SlimefunItemStack(
            "MINIMIZEFACTORY_MACHINE_NETWORK_CORE",
            Material.LIGHT_BLUE_CONCRETE,
            properties.getReplacedProperty("MachineNetworkCore_Name"),
            properties.getReplacedProperties("MachineNetworkCore_Lore_1",ChatColor.GRAY).toArray(EmptyArrays.EMPTY_STRING_ARRAY)
    );
    public static final SlimefunItemStack MACHINE_NETWORK_CONNECTOR = new SlimefunItemStack(
            "MINIMIZEFACTORY_MACHINE_CONNECTOR",
            Material.NETHER_BRICK,
            properties.getReplacedProperty("Connector_Name"),
            properties.getReplacedProperties("Connector_Lore_1",ChatColor.GRAY).toArray(EmptyArrays.EMPTY_STRING_ARRAY)
    );
    public static final ItemStack MACHINE_NETWORK_BRIDGE_OUT = MACHINE_NETWORK_BRIDGE.clone();
    static {
        MACHINE_NETWORK_BRIDGE_OUT.setAmount(64);
    }

    public static final UnplaceableBlock LENS_FOR_REGISTER = new UnplaceableBlock(
            Groups.MATERIALS
            , new SlimefunItemStack(LENS, 16)
            , RecipeType.ENHANCED_CRAFTING_TABLE
            , new ItemStack[]{
            null, new ItemStack(Material.GLASS),   null,
            null, null, null,
            new ItemStack(Material.GLASS), null, new ItemStack(Material.GLASS)
    }
    );
    public static final MachineStabilizer MACHINE_STABILIZER_FOR_REGISTER = new MachineStabilizer(
            Groups.MATERIALS,
            new SlimefunItemStack(MACHINE_STABILIZER, 8),
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    DUCT_TAPE,LENS,DUCT_TAPE,
                    LENS,CARBONADO,LENS,
                    CHAIN,LENS,CHAIN
            }
    );

    public static final MachineRecipeSerializer MACHINE_RECIPE_SERAILIZER_FOR_REGISTER = new MachineRecipeSerializer(
            Groups.BASIC_MACHINES,
            MACHINE_RECIPE_SERAILIZER,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    URANIUM,RAINBOW_RUNE,SYNTHETIC_DIAMOND,
                    GPS_CONTROL_PANEL,MULTIMETER,ENERGIZED_CAPACITOR,
                    SYNTHETIC_EMERALD,NUCLEAR_REACTOR,SYNTHETIC_SAPPHIRE
            }
    );

    public static final MachineRecipeDeserializer MACHINE_DESERIALIZER_FOR_REGISTER = new MachineRecipeDeserializer(
            Groups.BASIC_MACHINES,
            MACHINE_DESERIALIZER,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    SYNTHETIC_DIAMOND,new ItemStack(Material.DIAMOND_PICKAXE),SYNTHETIC_DIAMOND,
                    new ItemStack(Material.ENDER_PEARL),new ItemStack(Material.GLOWSTONE),new ItemStack(Material.ENDER_PEARL),
                    ADVANCED_CIRCUIT_BOARD,new ItemStack(Material.ANVIL),ADVANCED_CIRCUIT_BOARD
            }
    );
    public static final MachineNetworkStorage MACHINE_NETWORK_STORAGE_FOR_REGISTER = new MachineNetworkStorage(
            Groups.BASIC_MACHINES,
            MACHINE_NETWORK_STORAGE,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    GOLD_24K,new ItemStack(Material.DIAMOND_BLOCK),GOLD_24K,
                    new ItemStack(Material.DIAMOND_BLOCK),ENDER_RUNE,new ItemStack(Material.DIAMOND_BLOCK),
                    GOLD_24K,new ItemStack(Material.DIAMOND_BLOCK),GOLD_24K
            }
    );

    public static final MachineNetworkContainer MACHINE_NETWORK_CONTAINER_FOR_REGISTER = new MachineNetworkContainer(
            Groups.BASIC_MACHINES,
            MACHINE_NETWORK_CONTAINER,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    new ItemStack(Material.WATER_BUCKET),ENERGY_CONNECTOR,OUTPUT_CHEST,
                    CARGO_CONNECTOR_NODE,INFUSED_HOPPER,GPS_TRANSMITTER,
                    REACTOR_ACCESS_PORT,GPS_TELEPORTATION_MATRIX,BLOCK_PLACER,
            }
    );

    public static final MachineNetworkBridge MACHINE_NETWORK_BRIDGE_FOR_REGISTER = new MachineNetworkBridge(
            Groups.BASIC_MACHINES,
            MACHINE_NETWORK_BRIDGE,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    WITHER_PROOF_GLASS,WITHER_PROOF_GLASS,WITHER_PROOF_GLASS,
                    WITHER_PROOF_GLASS,LENS,WITHER_PROOF_GLASS,
                    WITHER_PROOF_GLASS,WITHER_PROOF_GLASS,WITHER_PROOF_GLASS
            },
            MACHINE_NETWORK_BRIDGE_OUT
    );
    public static final MachineNetworkCore MACHINE_NETWORK_CORE_FOR_REGISTER = new MachineNetworkCore(
            Groups.BASIC_MACHINES,
            MACHINE_NETWORK_CORE,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    ENERGY_REGULATOR,PROGRAMMABLE_ANDROID_3,CARGO_MANAGER,
                    GPS_CONTROL_PANEL,HOLOGRAM_PROJECTOR,ENERGIZED_CAPACITOR,
                    TRASH_CAN,new ItemStack(Material.CLOCK),INFUSED_HOPPER
            }
    );
    public static final MachineNetworkConnector MACHINE_NETWORK_CONNECTOR_FOR_REGISTER = new MachineNetworkConnector(
            Groups.BASIC_MACHINES,
            MACHINE_NETWORK_CONNECTOR,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    GPS_TELEPORTATION_MATRIX,GPS_MARKER_TOOL,GPS_TELEPORTATION_MATRIX,
                    GPS_TELEPORTATION_MATRIX,CARBONADO,GPS_TELEPORTATION_MATRIX,
                    GPS_TELEPORTATION_MATRIX,CARBONADO,GPS_TELEPORTATION_MATRIX,
            }
    );
    //these are serializables
    public static final SlimefunItemStack AUTO_SUGAR_CANE = new SlimefunItemStack(
            "MINIMIZEFACTORY_SERIALIZABLE_AUTO_SUGAR_CANE",
            Material.OBSERVER,
            properties.getReplacedProperty("Serializable_Auto_Sugar_Cane"),
            properties.getReplacedProperties("Serializable_Auto_Sugar_Cane_Lore_1",ChatColor.GRAY).toArray(EmptyArrays.EMPTY_STRING_ARRAY)
    );
    public static final SlimefunItemStack AUTO_CACTUS = new SlimefunItemStack(
            "MINIMIZEFACTORY_SERIALIZABLE_AUTO_CACTUS",
            Material.COBBLESTONE_SLAB,
            properties.getReplacedProperty("Serializable_Auto_Cactus"),
            properties.getReplacedProperties("Serializable_Auto_Cactus_Lore_1",ChatColor.GRAY).toArray(EmptyArrays.EMPTY_STRING_ARRAY)
    );

    //assume that a random tick costs 47.35 seconds in average
    public static final SerializeOnly AUTO_SUGAR_CANE_FOR_REGISTER = new SerializeOnly(
            Groups.SERIALIZABLE,
            AUTO_SUGAR_CANE,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    new ItemStack(Material.REDSTONE),new ItemStack(Material.OBSERVER),null,
                    new ItemStack(Material.COBBLESTONE),new ItemStack(Material.PISTON),null,
                    null,                               new ItemStack(Material.DIRT),new ItemStack(Material.SUGAR_CANE),
            }
    ) {
        final List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = Collections.singletonList(
                new SimplePair<>(
                        new SerializedMachine_MachineRecipe(
                                AUTO_SUGAR_CANE,
                                new MachineRecipeInTicks(
                                        15152,
                                        EMPTY_ITEM_STACK_ARRAY,
                                        new ItemStack[]{new ItemStack(Material.SUGAR_CANE)}
                                ),
                0
                        ),null));
        @Nonnull
        @Override
        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable SlimefunItem m) {
            return result;
        }
    };
    public static final SerializeOnly AUTO_CACTUS_FOR_REGISTER = new SerializeOnly(
            Groups.SERIALIZABLE,
            AUTO_CACTUS,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    null,new ItemStack(Material.COBBLESTONE_SLAB),null,
                    new ItemStack(Material.CACTUS),null,new ItemStack(Material.CACTUS),
                    new ItemStack(Material.SAND),null,new ItemStack(Material.SAND),
            }
    ) {
        final List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = Collections.singletonList(new SimplePair<>(new SerializedMachine_MachineRecipe(
                AUTO_CACTUS,
                new MachineRecipeInTicks(7576,EMPTY_ITEM_STACK_ARRAY,new ItemStack[]{new ItemStack(Material.CACTUS)}),
                0
        ),null));
        @Nonnull
        @Override
        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable SlimefunItem m) {
            return result;
        }
    };


    public static final SlimefunItemStack SERIALIZED_WITHER = new SlimefunItemStack(
            "MINIMIZEFACTORY_SERIALIZED_WITHER",
            Material.WITHER_SKELETON_SKULL,
            properties.getReplacedProperty("Material_Serialized_Wither"),
            properties.getReplacedProperties("Material_Serialized_Wither_Description",ChatColor.GRAY).toArray(EmptyArrays.EMPTY_STRING_ARRAY)
    );
    public static final UnplaceableBlock SERIALIZED_WITHER_FOR_REGISTER = new UnplaceableBlock(
            Groups.MATERIALS
            , SERIALIZED_WITHER
            , RecipeType.ENHANCED_CRAFTING_TABLE
            , new ItemStack[]{
            new ItemStack(Material.WITHER_SKELETON_SKULL), new ItemStack(Material.WITHER_SKELETON_SKULL),   new ItemStack(Material.WITHER_SKELETON_SKULL),
            new ItemStack(Material.SOUL_SAND), MACHINE_STABILIZER, new ItemStack(Material.SOUL_SAND),
            WITHER_PROOF_OBSIDIAN, new ItemStack(Material.SOUL_SAND), WITHER_PROOF_OBSIDIAN
    }
    );






























    public static void setup(MinimizeFactory plugin){
        for (Field f:Registers.class.getDeclaredFields()){
            if (Modifier.isStatic(f.getModifiers())){
                try {
                    Object o = f.get(null);
                    if (o instanceof SlimefunItem item){
                        item.register(plugin);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
