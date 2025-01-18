package io.github.ignorelicensescn.minimizeFactory.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class LoreGetter {
    public static final List<String> EMPTY_LORE = Collections.emptyList();
    public static List<String> tryGetLore(ItemStack stack){
        if (stack == null){return EMPTY_LORE;}
        ItemMeta meta = stack.getItemMeta();
        if (meta != null){
            List<String> lore = meta.getLore();
            if (lore != null){
                return lore;
            }
        }
        return EMPTY_LORE;
    }
}
