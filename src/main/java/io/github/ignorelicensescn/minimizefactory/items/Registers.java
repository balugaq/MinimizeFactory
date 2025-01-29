package io.github.ignorelicensescn.minimizefactory.items;

import io.github.ignorelicensescn.minimizefactory.items.consumptions.MachineStabilizer;
import io.github.ignorelicensescn.minimizefactory.items.consumptions.EntitySerializer;
import io.github.ignorelicensescn.minimizefactory.items.machine.MachineRecipeDeserializer;
import io.github.ignorelicensescn.minimizefactory.items.machine.MachineRecipeSerializer;
import io.github.ignorelicensescn.minimizefactory.items.machine.network.*;
import io.github.ignorelicensescn.minimizefactory.items.serializable.SerializeOnly;
import io.github.ignorelicensescn.minimizefactory.MinimizeFactory;
import io.github.ignorelicensescn.minimizefactory.sfgroups.Groups;
import io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizefactory.utils.localmachinerecipe.MachineRecipeInTicks;
import io.github.ignorelicensescn.minimizefactory.utils.simpleStructure.SimplePair;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;

import static io.github.ignorelicensescn.minimizefactory.items.SlimefunStacks.MACHINE_STABILIZER;
import static io.github.ignorelicensescn.minimizefactory.utils.EmptyArrays.EMPTY_ITEM_STACK_ARRAY;
import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.*;

/**
 * <p>SlimefunItems declared here will be registered with {@link java.lang.reflect}.So SuppressWarnings</p>
 * Learned from SlimeFrame.
 */
@SuppressWarnings("unused")
public class Registers {

    public static final UnplaceableBlock LENS_FOR_REGISTER = new UnplaceableBlock(
            Groups.MATERIALS
            , new SlimefunItemStack(SlimefunStacks.LENS, 16)
            , RecipeType.ENHANCED_CRAFTING_TABLE
            , new ItemStack[]{
            null, new ItemStack(Material.GLASS),   null,
            null, null, null,
            new ItemStack(Material.GLASS), null, new ItemStack(Material.GLASS)
    }
    );
    public static final MachineStabilizer MACHINE_STABILIZER_FOR_REGISTER = new MachineStabilizer(
            Groups.MATERIALS,
            new SlimefunItemStack(SlimefunStacks.MACHINE_STABILIZER, 8),
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    DUCT_TAPE, SlimefunStacks.LENS,DUCT_TAPE,
                    SlimefunStacks.LENS,CARBONADO, SlimefunStacks.LENS,
                    CHAIN, SlimefunStacks.LENS,CHAIN
            }
    );

    public static final MachineRecipeSerializer MACHINE_RECIPE_SERAILIZER_FOR_REGISTER = new MachineRecipeSerializer(
            Groups.BASIC_MACHINES,
            SlimefunStacks.MACHINE_RECIPE_SERAILIZER,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    URANIUM,RAINBOW_RUNE,SYNTHETIC_DIAMOND,
                    GPS_CONTROL_PANEL,MULTIMETER,ENERGIZED_CAPACITOR,
                    SYNTHETIC_EMERALD,NUCLEAR_REACTOR,SYNTHETIC_SAPPHIRE
            }
    );

    public static final MachineRecipeDeserializer MACHINE_DESERIALIZER_FOR_REGISTER = new MachineRecipeDeserializer(
            Groups.BASIC_MACHINES,
            SlimefunStacks.MACHINE_DESERIALIZER,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    SYNTHETIC_DIAMOND,new ItemStack(Material.DIAMOND_PICKAXE),SYNTHETIC_DIAMOND,
                    new ItemStack(Material.ENDER_PEARL),new ItemStack(Material.GLOWSTONE),new ItemStack(Material.ENDER_PEARL),
                    ADVANCED_CIRCUIT_BOARD,new ItemStack(Material.ANVIL),ADVANCED_CIRCUIT_BOARD
            }
    );
    public static final MachineNetworkStorage MACHINE_NETWORK_STORAGE_FOR_REGISTER = new MachineNetworkStorage(
            Groups.BASIC_MACHINES,
            SlimefunStacks.MACHINE_NETWORK_STORAGE,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    GOLD_24K,new ItemStack(Material.DIAMOND_BLOCK),GOLD_24K,
                    new ItemStack(Material.DIAMOND_BLOCK),ENDER_RUNE,new ItemStack(Material.DIAMOND_BLOCK),
                    GOLD_24K,new ItemStack(Material.DIAMOND_BLOCK),GOLD_24K
            }
    );

    public static final MachineNetworkContainer MACHINE_NETWORK_CONTAINER_FOR_REGISTER = new MachineNetworkContainer(
            Groups.BASIC_MACHINES,
            SlimefunStacks.MACHINE_NETWORK_CONTAINER,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    new ItemStack(Material.WATER_BUCKET),ENERGY_CONNECTOR,OUTPUT_CHEST,
                    CARGO_CONNECTOR_NODE,INFUSED_HOPPER,GPS_TRANSMITTER,
                    REACTOR_ACCESS_PORT,GPS_TELEPORTATION_MATRIX,BLOCK_PLACER,
            }
    );

    public static final MachineNetworkBridge MACHINE_NETWORK_BRIDGE_FOR_REGISTER = new MachineNetworkBridge(
            Groups.BASIC_MACHINES,
            SlimefunStacks.MACHINE_NETWORK_BRIDGE,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    WITHER_PROOF_GLASS,WITHER_PROOF_GLASS,WITHER_PROOF_GLASS,
                    WITHER_PROOF_GLASS, SlimefunStacks.LENS,WITHER_PROOF_GLASS,
                    WITHER_PROOF_GLASS,WITHER_PROOF_GLASS,WITHER_PROOF_GLASS
            },
            SlimefunStacks.MACHINE_NETWORK_BRIDGE_OUT
    );
    public static final MachineNetworkCore MACHINE_NETWORK_CORE_FOR_REGISTER = new MachineNetworkCore(
            Groups.BASIC_MACHINES,
            SlimefunStacks.MACHINE_NETWORK_CORE,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    ENERGY_REGULATOR,PROGRAMMABLE_ANDROID_3,CARGO_MANAGER,
                    GPS_CONTROL_PANEL,HOLOGRAM_PROJECTOR,ENERGIZED_CAPACITOR,
                    TRASH_CAN,new ItemStack(Material.CLOCK),INFUSED_HOPPER
            }
    );
    public static final MachineNetworkConnector MACHINE_NETWORK_CONNECTOR_FOR_REGISTER = new MachineNetworkConnector(
            Groups.BASIC_MACHINES,
            SlimefunStacks.MACHINE_NETWORK_CONNECTOR,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    GPS_TELEPORTATION_MATRIX,GPS_MARKER_TOOL,GPS_TELEPORTATION_MATRIX,
                    GPS_TELEPORTATION_MATRIX,CARBONADO,GPS_TELEPORTATION_MATRIX,
                    GPS_TELEPORTATION_MATRIX,CARBONADO,GPS_TELEPORTATION_MATRIX,
            }
    );
    //ticks are slimefun ticks(sft,1sft=10gt)
    public static final SerializeOnly AUTO_SUGAR_CANE_FOR_REGISTER = new SerializeOnly(
            Groups.SERIALIZABLE,
            SlimefunStacks.AUTO_SUGAR_CANE,
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
                                SlimefunStacks.AUTO_SUGAR_CANE,
                                new MachineRecipeInTicks(
                                        2048,//based on mcp 1.12 random tick
                                        EMPTY_ITEM_STACK_ARRAY,
                                        new ItemStack[]{new ItemStack(Material.SUGAR_CANE)}
                                ),
                0
                        ),null));
        @Nonnull
        @Override
        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable SlimefunItem m ,@Nullable ItemStack stack) {
            return result;
        }
    };
    public static final SerializeOnly AUTO_CACTUS_FOR_REGISTER = new SerializeOnly(
            Groups.SERIALIZABLE,
            SlimefunStacks.AUTO_CACTUS,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    null,new ItemStack(Material.COBBLESTONE_SLAB),null,
                    new ItemStack(Material.CACTUS),null,new ItemStack(Material.CACTUS),
                    new ItemStack(Material.SAND),null,new ItemStack(Material.SAND),
            }
    ) {
        final List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = Collections.singletonList(new SimplePair<>(new SerializedMachine_MachineRecipe(
                SlimefunStacks.AUTO_CACTUS,
                new MachineRecipeInTicks(
                        2048,//based on mcp 1.12 random tick
                        EMPTY_ITEM_STACK_ARRAY,new ItemStack[]{new ItemStack(Material.CACTUS)}),
                0
        ),null));
        @Nonnull
        @Override
        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable SlimefunItem m ,@Nullable ItemStack stack) {
            return result;
        }
    };

    public static final SerializeOnly AUTO_BAMBOO_FOR_REGISTER = new SerializeOnly(
            Groups.SERIALIZABLE,
            SlimefunStacks.AUTO_BAMBOO,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    new ItemStack(Material.REDSTONE),new ItemStack(Material.OBSERVER),null,
                    new ItemStack(Material.COBBLESTONE),new ItemStack(Material.PISTON),null,
                    null,                               new ItemStack(Material.DIRT),new ItemStack(Material.BAMBOO),
            }
    ) {
        final List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = Collections.singletonList(
                new SimplePair<>(
                        new SerializedMachine_MachineRecipe(
                                SlimefunStacks.AUTO_BAMBOO,
                                new MachineRecipeInTicks(
                                        401,//4096 game-ticks,but 1 slimefun-tick == 10game-ticks
                                        EMPTY_ITEM_STACK_ARRAY,
                                        new ItemStack[]{new ItemStack(Material.BAMBOO)}
                                ),
                                0
                        ),null));
        @Nonnull
        @Override
        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable SlimefunItem m ,@Nullable ItemStack stack) {
            return result;
        }
    };


    public static final UnplaceableBlock SERIALIZED_WITHER_FOR_REGISTER = new UnplaceableBlock(
            Groups.MATERIALS
            , SlimefunStacks.SERIALIZED_WITHER
            , RecipeType.ENHANCED_CRAFTING_TABLE
            , new ItemStack[]{
            new ItemStack(Material.WITHER_SKELETON_SKULL), new ItemStack(Material.WITHER_SKELETON_SKULL),   new ItemStack(Material.WITHER_SKELETON_SKULL),
            new ItemStack(Material.SOUL_SAND), SlimefunStacks.MACHINE_STABILIZER, new ItemStack(Material.SOUL_SAND),
            WITHER_PROOF_OBSIDIAN, new ItemStack(Material.SOUL_SAND), WITHER_PROOF_OBSIDIAN
    }
    );
    public static final UnplaceableBlock SERIALIZED_SNOWMAN_FOR_REGISTER = new UnplaceableBlock(
            Groups.MATERIALS
            , SlimefunStacks.SERIALIZED_SNOWMAN
            , RecipeType.ENHANCED_CRAFTING_TABLE
            , new ItemStack[]{
            null, new ItemStack(Material.CARVED_PUMPKIN),   null,
            null, SlimefunStacks.MACHINE_STABILIZER, null,
            null, new ItemStack(Material.SNOW_BLOCK), null
    }
    );
    public static final SerializeOnly COBBLESTONE_GENERATOR_FOR_REGISTER = new SerializeOnly(
            Groups.SERIALIZABLE,
            SlimefunStacks.COBBLESTONE_GENERATOR,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    new ItemStack(Material.LAVA_BUCKET),WITHER_PROOF_OBSIDIAN,new ItemStack(Material.WATER_BUCKET),
                    new ItemStack(Material.LAVA_BUCKET), SlimefunStacks.SERIALIZED_WITHER,new ItemStack(Material.WATER_BUCKET),
                    new ItemStack(Material.LAVA_BUCKET), SlimefunStacks.SERIALIZED_SNOWMAN,new ItemStack(Material.WATER_BUCKET),
            }
    ) {
        final List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = Collections.singletonList(new SimplePair<>(new SerializedMachine_MachineRecipe(
                SlimefunStacks.COBBLESTONE_GENERATOR,
                new MachineRecipeInTicks(
                        1,
                        EMPTY_ITEM_STACK_ARRAY,
                        new ItemStack[]{new ItemStack(Material.COBBLESTONE,3)}),
                0
        ),null));
        @Nonnull
        @Override
        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable SlimefunItem m ,@Nullable ItemStack stack) {
            return result;
        }
    };
    public static final EntitySerializer VILLAGER_SERIALIZER_FOR_REGISTER = new EntitySerializer(
            Groups.MATERIALS,
            SlimefunStacks.VILLAGER_SERIALIZER,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    new ItemStack(Material.WHITE_BED),       MACHINE_STABILIZER,            new ItemStack(Material.WHITE_BED),
                    MACHINE_STABILIZER,             new ItemStack(Material.EMERALD_BLOCK),  MACHINE_STABILIZER,
                    new ItemStack(Material.WHITE_BED),       MACHINE_STABILIZER,            new ItemStack(Material.WHITE_BED)
            },
            SlimefunStacks.SERIALIZED_VILLAGER,
            Villager.class
    );
    public static final UnplaceableBlock SERIALIZED_VILLAGER_FOR_REGISTER = new UnplaceableBlock(
            Groups.MATERIALS,
            SlimefunStacks.SERIALIZED_VILLAGER,
            RecipeTypes.RIGHT_CLICK,
            new ItemStack[]{
                    null,                               null,                               null,
                    new ItemStack(Material.PLAYER_HEAD),SlimefunStacks.VILLAGER_SERIALIZER_SINGLE,new ItemStack(Material.VILLAGER_SPAWN_EGG),
                    null,                               null,                               null
            }
    );
    public static final SerializeOnly SERIALIZABLE_IRON_GOLEM_FOR_REGISTER = new SerializeOnly(
            Groups.SERIALIZABLE,
            SlimefunStacks.SERIALIZABLE_IRON_GOLEM_FARM,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[]{
                    SlimefunStacks.SERIALIZED_VILLAGER,SlimefunStacks.SERIALIZED_VILLAGER,SlimefunStacks.SERIALIZED_VILLAGER,
                    new ItemStack(Material.COBBLESTONE_WALL), new ItemStack(Material.COBBLESTONE_SLAB),new ItemStack(Material.COBBLESTONE_WALL),
                    new ItemStack(Material.LAVA_BUCKET), new ItemStack(Material.ZOMBIE_HEAD),new ItemStack(Material.WATER_BUCKET),
            }
    ) {
        final List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = Collections.singletonList(new SimplePair<>(new SerializedMachine_MachineRecipe(
                SlimefunStacks.SERIALIZABLE_IRON_GOLEM_FARM,
                new MachineRecipeInTicks(
                        60,
                        EMPTY_ITEM_STACK_ARRAY,
                        new ItemStack[]{
                                new ItemStack(Material.IRON_INGOT,4),//average
                                new ItemStack(Material.POPPY,1),//average
                                BASIC_CIRCUIT_BOARD,
                        }
                ),
                0
        ),null));
        @Nonnull
        @Override
        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable SlimefunItem m ,@Nullable ItemStack stack) {
            return result;
        }
    };


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
