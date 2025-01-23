package io.github.ignorelicensescn.minimizefactory.sfgroups;

import io.github.ignorelicensescn.minimizefactory.MinimizeFactory;
import io.github.mooy1.infinitylib.groups.MultiGroup;
import io.github.mooy1.infinitylib.groups.SubGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Material;

import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.properties;

public final class Groups {

    public static final ItemGroup BASIC_MACHINES = new SubGroup(
            "minimizefactory_basic_machines",
            new CustomItemStack(Material.CRAFTING_TABLE, properties.getReplacedProperty("MINIMIZEFACTORY_BASIC_MACHINES"))
    );
    public static final ItemGroup MATERIALS = new SubGroup(
            "minimizefactory_materials",
            new CustomItemStack(Material.GLASS, properties.getReplacedProperty("MINIMIZEFACTORY_MATERIAL")));
    public static final ItemGroup SERIALIZABLE = new SubGroup(
            "minimizefactory_serializable",
            new CustomItemStack(Material.OBSERVER, properties.getReplacedProperty("MINIMIZEFACTORY_SERIALIZABLE")));


    public static final ItemGroup MAIN_CATEGORY = new MultiGroup(
            "minimizefactory_main",
            new CustomItemStack(
                    Material.BLAST_FURNACE
                    , properties.getReplacedProperty("MINIMIZEFACTORY_MAIN_GROUP")
            )
            , 3
            , BASIC_MACHINES
            , MATERIALS
            , SERIALIZABLE
    );
    public static void setup(MinimizeFactory inst) {
        MAIN_CATEGORY.register(inst);
    }

}