package io.github.ignorelicensescn.minimizefactory.utils.compatibilities.FNAmp;

import ne.fnfal113.fnamplifications.materialgenerators.implementations.CustomMaterialGenerator;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static io.github.ignorelicensescn.minimizefactory.utils.compatibilities.InfinityExpansion.InfinityExpansionConsts.getInUnsafe;

public class FNAmpConsts {
    public static final Map<String,ItemStack> materialGeneratorsOutputs = new HashMap<>();
    public static ItemStack getMaterialGeneratorsOutput(CustomMaterialGenerator sfItem) throws NoSuchFieldException {
        if (materialGeneratorsOutputs.containsKey(sfItem.getId())){
            return materialGeneratorsOutputs.get(sfItem.getId());
        }
        Field f = sfItem.getClass().getDeclaredField("item");
        f.setAccessible(true);
        ItemStack out = (ItemStack) getInUnsafe(sfItem,f);
        out = out.clone();
        materialGeneratorsOutputs.put(sfItem.getId(),out);
        f.setAccessible(false);
        return out;
    }
}
