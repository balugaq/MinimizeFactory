package io.github.ignorelicensescn.minimizefactory.items;

import io.github.ignorelicensescn.minimizefactory.items.consumptions.MachineStabilizer;
import io.github.ignorelicensescn.minimizefactory.utils.EmptyArrays;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.properties;
import static io.github.ignorelicensescn.minimizefactory.utils.EmptyArrays.EMPTY_STRING_ARRAY;

public class SlimefunStacks {
    public static final SlimefunItemStack LENS = new SlimefunItemStack(
            "MINIMIZEFACTORY_LENS",
            Material.LIGHT_BLUE_STAINED_GLASS_PANE,
            properties.getReplacedProperty("Material_Lens"),
            properties.getReplacedProperties("Material_Lens_Description", ChatColor.GRAY).toArray(EmptyArrays.EMPTY_STRING_ARRAY)
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
    public static final ItemStack MACHINE_NETWORK_BRIDGE_OUT = MACHINE_NETWORK_BRIDGE.clone();
    static {
        SlimefunStacks.MACHINE_NETWORK_BRIDGE_OUT.setAmount(64);
    }
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
    public static final SlimefunItemStack AUTO_BAMBOO = new SlimefunItemStack(
            "MINIMIZEFACTORY_SERIALIZABLE_AUTO_BAMBOO",
            Material.OBSERVER,
            properties.getReplacedProperty("Serializable_Auto_Bamboo"),
            properties.getReplacedProperties("Serializable_Auto_Bamboo_Lore_1",ChatColor.GRAY).toArray(EmptyArrays.EMPTY_STRING_ARRAY)
    );
    public static final SlimefunItemStack SERIALIZED_WITHER = new SlimefunItemStack(
            "MINIMIZEFACTORY_SERIALIZED_WITHER",
            Material.WITHER_SKELETON_SKULL,
            properties.getReplacedProperty("Material_Serialized_Wither"),
            properties.getReplacedProperties("Material_Serialized_Wither_Description",ChatColor.GRAY).toArray(EmptyArrays.EMPTY_STRING_ARRAY)
    );
    public static final SlimefunItemStack SERIALIZED_SNOWMAN = new SlimefunItemStack(
            "MINIMIZEFACTORY_SERIALIZED_SNOWMAN",
            Material.CARVED_PUMPKIN,
            properties.getReplacedProperty("Material_Serialized_SnowMan"),
            properties.getReplacedProperties("Material_Serialized_SnowMan_Description",ChatColor.GRAY).toArray(EmptyArrays.EMPTY_STRING_ARRAY)
    );
    public static final SlimefunItemStack COBBLESTONE_GENERATOR = new SlimefunItemStack(
            "MINIMIZEFACTORY_COBBLESTONE_GENERATOR",
            Material.PISTON,
            properties.getReplacedProperty("Serializable_Cobblestone_Generator"),
            properties.getReplacedProperties("Serializable_Cobblestone_Generator_Description",ChatColor.GRAY).toArray(EMPTY_STRING_ARRAY)
    );

    public static final SlimefunItemStack SERIALIZED_VILLAGER = new SlimefunItemStack(
            "MINIMIZEFACTORY_SERIALIZED_VILLAGER",
            Material.BROWN_STAINED_GLASS,
            properties.getReplacedProperty("Material_Serialized_Villager"),
            properties.getReplacedProperties("Material_Serialized_Villager_Description",ChatColor.GRAY).toArray(EMPTY_STRING_ARRAY)
    );
    public static final SlimefunItemStack VILLAGER_SERIALIZER = new SlimefunItemStack(
            "MINIMIZEFACTORY_VILLAGER_SERIALIZER",
            Material.EMERALD_BLOCK,
            properties.getReplacedProperty("Material_Villager_Serializer"),
            properties.getReplacedProperties("Material_Villager_Serializer_Description",ChatColor.GRAY).toArray(EMPTY_STRING_ARRAY)
    );
    static {
        VILLAGER_SERIALIZER.setAmount(4);
    }
    public static final SlimefunItemStack SERIALIZABLE_IRON_GOLEM_FARM = new SlimefunItemStack(
            "MINIMIZEFACTORY_SERIALIZABLE_IRON_GOLEM_FARM",
            Material.IRON_BLOCK,
            properties.getReplacedProperty("Serializable_Iron_Golem_Farm"),
            properties.getReplacedProperties("Serializable_Iron_Golem_Farm_Description",ChatColor.GRAY).toArray(EMPTY_STRING_ARRAY)
    );
}
