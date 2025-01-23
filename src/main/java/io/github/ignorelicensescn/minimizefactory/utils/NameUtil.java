package io.github.ignorelicensescn.minimizefactory.utils;

import io.github.ignorelicensescn.minimizefactory.utils.itemstackrelated.ItemStackUtil;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.properties;

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
        return m == null?NULL_STRING:m.name();
    }

    //i hope it has least complexity(O(1))
    public static ChatColor colorForMaterial(Material m){
        try {
            MinecraftVersion current = Slimefun.getMinecraftVersion();
            if (current.isBefore(MinecraftVersion.MINECRAFT_1_16)) {
                return switch (m){
                    case COBBLESTONE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Cobblestone"));
                    case NETHERRACK,LEGACY_NETHERRACK -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Netherrack"));
                    case DIAMOND,DIAMOND_ORE,DIAMOND_BLOCK -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Diamond"));
                    case COAL,COAL_BLOCK,COAL_ORE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Coal"));
                    case LAPIS_LAZULI,LAPIS_BLOCK,LAPIS_ORE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Lapis"));
                    case EMERALD ,EMERALD_BLOCK,EMERALD_ORE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Emerald"));
                    case IRON_INGOT ,IRON_BLOCK,IRON_ORE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Iron"));
                    case GOLD_INGOT ,GOLD_BLOCK,GOLD_ORE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Gold"));
                    case REDSTONE ,REDSTONE_BLOCK,REDSTONE_ORE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Redstone"));
                    case QUARTZ ,QUARTZ_BLOCK,NETHER_QUARTZ_ORE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Quartz"));
                    default -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Default"));
                };
            }else if (current.isBefore(MinecraftVersion.MINECRAFT_1_17)){
                return switch (m){
                    case COBBLESTONE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Cobblestone"));
                    case NETHERRACK,LEGACY_NETHERRACK -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Netherrack"));
                    case DIAMOND,DIAMOND_ORE,DIAMOND_BLOCK -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Diamond"));
                    case COAL,COAL_BLOCK,COAL_ORE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Coal"));
                    case LAPIS_LAZULI,LAPIS_BLOCK,LAPIS_ORE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Lapis"));
                    case EMERALD ,EMERALD_BLOCK,EMERALD_ORE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Emerald"));
                    case IRON_INGOT ,IRON_BLOCK,IRON_ORE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Iron"));
                    case GOLD_INGOT ,GOLD_BLOCK,GOLD_ORE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Gold"));
                    case REDSTONE ,REDSTONE_BLOCK,REDSTONE_ORE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Redstone"));
                    case QUARTZ ,QUARTZ_BLOCK,NETHER_QUARTZ_ORE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Quartz"));
                    case NETHERITE_SCRAP ,ANCIENT_DEBRIS ,NETHERITE_BLOCK -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Netherite"));
                    default -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Default"));
                };
            }else /*if (current.isBefore(MinecraftVersion.MINECRAFT_1_19))*/{
                return switch (m){
                    case COBBLESTONE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Cobblestone"));
                    case NETHERRACK,LEGACY_NETHERRACK -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Netherrack"));
                    case COPPER_BLOCK,COPPER_INGOT,COPPER_ORE,RAW_COPPER,RAW_COPPER_BLOCK -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Copper"));
                    case DIAMOND,DIAMOND_ORE,DIAMOND_BLOCK -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Diamond"));
                    case COAL,COAL_BLOCK,COAL_ORE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Coal"));
                    case LAPIS_LAZULI,LAPIS_BLOCK,LAPIS_ORE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Lapis"));
                    case EMERALD ,EMERALD_BLOCK,EMERALD_ORE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Emerald"));
                    case IRON_INGOT ,IRON_BLOCK,IRON_ORE,RAW_IRON,RAW_IRON_BLOCK -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Iron"));
                    case GOLD_INGOT ,GOLD_BLOCK,GOLD_ORE,RAW_GOLD,RAW_GOLD_BLOCK -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Gold"));
                    case REDSTONE ,REDSTONE_BLOCK,REDSTONE_ORE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Redstone"));
                    case QUARTZ ,QUARTZ_BLOCK,NETHER_QUARTZ_ORE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Quartz"));
                    case NETHERITE_SCRAP ,ANCIENT_DEBRIS ,NETHERITE_BLOCK -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Netherite"));
                    case AMETHYST_BLOCK ,AMETHYST_SHARD -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Amethyst"));
                    default -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Default"));
                };
            }
        }catch (Exception e){
            e.printStackTrace();
            return switch (m){
                case COBBLESTONE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Cobblestone"));
                case NETHERRACK,LEGACY_NETHERRACK -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Netherrack"));
                case DIAMOND,DIAMOND_ORE,DIAMOND_BLOCK -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Diamond"));
                case COAL,COAL_BLOCK,COAL_ORE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Coal"));
                case LAPIS_LAZULI,LAPIS_BLOCK,LAPIS_ORE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Lapis"));
                case EMERALD ,EMERALD_BLOCK,EMERALD_ORE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Emerald"));
                case IRON_INGOT ,IRON_BLOCK,IRON_ORE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Iron"));
                case GOLD_INGOT ,GOLD_BLOCK,GOLD_ORE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Gold"));
                case REDSTONE ,REDSTONE_BLOCK,REDSTONE_ORE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Redstone"));
                case QUARTZ ,QUARTZ_BLOCK,NETHER_QUARTZ_ORE -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Quartz"));
                default -> ChatColor.getByChar(properties.getReplacedProperty("ColorForItem_Default"));
            };
        }
    }

    public static String nameForBiome(@Nullable Biome biome) {
        return biome == null?NULL_STRING:biome.name();
    }
}
