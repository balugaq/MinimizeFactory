package io.github.ignorelicensescn.minimizeFactory.utils.searchregistries;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

public class SearchRegistries {
    public static final Multimap<Class<? extends SlimefunItem>,OnScannedSlimefunItemInstanceListener> listeners = Multimaps.synchronizedSetMultimap(HashMultimap.create());
    public static void registerOnScannedSlimefunItemInstanceListener(Class<? extends SlimefunItem> scanClass,OnScannedSlimefunItemInstanceListener onScannedSlimefunItemInstanceListener){
        listeners.put(scanClass,onScannedSlimefunItemInstanceListener);
    }

    public static void unregisterOnScannedSlimefunItemInstanceListener(Class<? extends SlimefunItem> scanClass,OnScannedSlimefunItemInstanceListener onScannedSlimefunItemInstanceListener){
        listeners.remove(scanClass,onScannedSlimefunItemInstanceListener);
    }
    public static void scan(){
        for (SlimefunItem sfItem: Slimefun.getRegistry().getEnabledSlimefunItems()){
            Class<? extends SlimefunItem> sfItemClass = sfItem.getClass();
            for (OnScannedSlimefunItemInstanceListener listener:listeners.get(sfItemClass)){
                listener.onScanned(sfItem);
            }
        }
    }
}
