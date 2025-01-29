package io.github.ignorelicensescn.minimizefactory.items.consumptions;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.EntityInteractHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.ItemUtils;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

import static io.github.ignorelicensescn.minimizefactory.items.SlimefunStacks.SERIALIZED_VILLAGER;

public class VillagerSerializer extends SlimefunItem {

    public VillagerSerializer(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        addItemHandler((EntityInteractHandler) (e,usingItem,offhandFlag) -> {

            Entity interactedEntity = e.getRightClicked();
            Player p = e.getPlayer();
            if (!Slimefun.getProtectionManager().hasPermission(p,interactedEntity.getLocation(),Interaction.INTERACT_ENTITY)){
                //TODO:Write a message
                return;
            }
            if (interactedEntity instanceof Villager v){
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_HURT,3.0f,0.5f);
                v.getWorld().dropItem(v.getLocation(),SERIALIZED_VILLAGER.clone());
                v.remove();
                ItemUtils.consumeItem(usingItem,1,false);
            }
        });
    }
}
