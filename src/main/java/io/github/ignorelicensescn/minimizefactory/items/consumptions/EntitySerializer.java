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
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EntitySerializer extends SlimefunItem {

    private final ItemStack dropItem;
    private final Class<? extends Creature> targetCreature;
    public EntitySerializer(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack dropItem, Class<? extends Creature> targetCreature) {
        super(itemGroup, item, recipeType, recipe);
        this.dropItem = dropItem;
        this.targetCreature = targetCreature;
        addItemHandler((EntityInteractHandler) (e,usingItem,offhandFlag) -> {

            Entity interactedEntity = e.getRightClicked();
            Player p = e.getPlayer();
            if (!Slimefun.getProtectionManager().hasPermission(p,interactedEntity.getLocation(),Interaction.INTERACT_ENTITY)){
                //TODO:Write a message
                return;
            }

            if (EntitySerializer.this.targetCreature.isAssignableFrom(interactedEntity.getClass())){
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_HURT,3.0f,0.5f);
                interactedEntity.getWorld().dropItem(interactedEntity.getLocation(),EntitySerializer.this.dropItem.clone());
                interactedEntity.remove();
                ItemUtils.consumeItem(usingItem,1,false);
            }
        });
    }
}
