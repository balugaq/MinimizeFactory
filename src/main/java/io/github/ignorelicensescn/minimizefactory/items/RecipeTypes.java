package io.github.ignorelicensescn.minimizefactory.items;

import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.properties;
import static io.github.thebusybiscuit.slimefun4.libraries.commons.lang.ArrayUtils.EMPTY_STRING_ARRAY;

public class RecipeTypes {
    public static final RecipeType RIGHT_CLICK = new RecipeType(
            NamespacedKey.fromString("minmizefactory_right_click"),
            new CustomItemStack(Material.STONE_BUTTON,properties.getReplacedProperty("Right_Click")),
            null,
            properties.getReplacedProperties("Right_Click_Description", ChatColor.GRAY).toArray(EMPTY_STRING_ARRAY)
    );
}
