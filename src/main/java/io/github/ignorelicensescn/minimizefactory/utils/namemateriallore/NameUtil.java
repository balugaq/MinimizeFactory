package io.github.ignorelicensescn.minimizefactory.utils.namemateriallore;

import io.github.ignorelicensescn.minimizefactory.utils.itemstackrelated.ItemStackUtil;

import net.guizhanss.minecraft.guizhanlib.gugu.minecraft.helpers.MaterialHelper;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.properties;
import static io.github.ignorelicensescn.minimizefactory.PluginEnabledFlags.GuizhanLibPluginFlag;
import static io.github.ignorelicensescn.minimizefactory.utils.namemateriallore.MaterialColorMaps.legacyMaterialColorMap;
import static io.github.ignorelicensescn.minimizefactory.utils.namemateriallore.MaterialColorMaps.materialColorMap;

public class NameUtil {
    public static final String NULL_STRING = "NULL";
    public static String findName(ItemStack itemStack){
        if (!ItemStackUtil.isItemStackValid(itemStack)){return NULL_STRING;}
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()){return itemStack.getItemMeta().getDisplayName();}
        return NameUtil.nameForMaterial(itemStack.getType());
    }
    public static String findNameWithAmount(ItemStack itemStack){
        String itemName = findName(itemStack);
        if (!itemName.equals(NULL_STRING)) {
            itemName += properties.getReplacedProperty("Stabilizer_Input_Unit") + itemStack.getAmount();
        }
        return itemName;
    }

    public static String nameForMaterial(@Nullable Material m){
        return colorForMaterial(m) + nameForMaterialNoColor(m);
    }
    public static String nameForMaterialNoColor(@Nullable Material m){
        if (m == null){return NULL_STRING;}
        if (GuizhanLibPluginFlag){
            return MaterialHelper.getName(m);
        }
        return m.name();
    }

    @Nonnull
    public static net.md_5.bungee.api.ChatColor colorForMaterial(@Nullable Material m){
        if (m == null){return net.md_5.bungee.api.ChatColor.GRAY;}
        String materialName = m.name();
        if (materialName.startsWith("LEGACY_")){
            materialName = materialName.substring(7);
        }
        net.md_5.bungee.api.ChatColor result = materialColorMap.get(materialName);
        if (result == null){
            result = legacyMaterialColorMap.getOrDefault(materialName,net.md_5.bungee.api.ChatColor.GRAY);
        }
        return result;
    }

    public static String nameForBiome(@Nullable Biome biome) {
        return biome == null?NULL_STRING:biome.name();
    }
}
