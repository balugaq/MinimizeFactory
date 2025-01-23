package io.github.ignorelicensescn.minimizefactory.utils.itemstackrelated;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

/**
 * compatibilities for versions
 */
public class MaterialUtil {
    public static final Map<String, Material> materialsMap = new HashMap<>();
    static {
        for (Material m:Material.values()){
            materialsMap.put(m.name(),m);
        }
    }

    public static Material valueOf(String s){
        if (s == null){return null;}
        return materialsMap.get(s);
    }
}
