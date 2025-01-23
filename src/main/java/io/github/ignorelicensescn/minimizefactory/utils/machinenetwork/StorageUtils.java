package io.github.ignorelicensescn.minimizefactory.utils.machinenetwork;

import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;


import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.minimizeFactoryInstance;

public class StorageUtils {
    private static final NamespacedKey mfkey = new NamespacedKey(minimizeFactoryInstance, "mfkey");

    public static ItemStack unKeyItem(ItemStack item) {
        if (item == null){return null;}
        ItemStack clone = item.clone();
        ItemMeta meta = clone.getItemMeta();
        if (meta != null){
            meta.getPersistentDataContainer().remove(mfkey);
        }else {
            new Exception("itemMeta null during unKeyItem").printStackTrace();
        }
        clone.setItemMeta(meta);
        return clone;
    }

    public static void giveOrDropItem(Player p, ItemStack toGive) {

        for (ItemStack leftover : p.getInventory().addItem(new ItemStack[]{toGive}).values()) {
            p.getWorld().dropItemNaturally(p.getLocation(), leftover);
        }

    }
    public static void createBorder(ChestMenu menu, ItemStack backgroundItem, int[] slots) {
        for (int slot : slots) {
            menu.addItem(slot, backgroundItem, ChestMenuUtils.getEmptyClickHandler());
        }
    }

    public static ItemStack keyItem(ItemStack item) {
        ItemStack clone = item.clone();
        ItemMeta meta = clone.getItemMeta();
        if (meta == null){
            meta = new CustomItemStack(
                    clone,
                    im -> im.getPersistentDataContainer().set(mfkey, PersistentDataType.INTEGER, 1)
            ).getItemMeta();
        }else{
            meta.getPersistentDataContainer().set(mfkey, PersistentDataType.INTEGER, 1);
        }
        clone.setItemMeta(meta);
        return clone;
    }
}
