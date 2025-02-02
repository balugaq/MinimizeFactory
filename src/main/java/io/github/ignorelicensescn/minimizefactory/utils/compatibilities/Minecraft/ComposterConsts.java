package io.github.ignorelicensescn.minimizefactory.utils.compatibilities.Minecraft;

import io.github.ignorelicensescn.minimizefactory.utils.mathutils.BigRational;
import io.github.ignorelicensescn.minimizefactory.utils.mathutils.IntegerRational;
import io.github.ignorelicensescn.minimizefactory.utils.simpleStructure.SimplePair;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ComposterConsts {
    public static final List<SimplePair<ItemStack, BigRational>> COMPOSTER_ACCEPT_CHANCES = new ArrayList<>(108);
    static {
        Material tryToGet;
        tryToGet = Material.getMaterial("JUNGLE_LEAVES");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("OAK_LEAVES");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("SPRUCE_LEAVES");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("DARK_OAK_LEAVES");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("PALE_OAK_LEAVES");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("ACACIA_LEAVES");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("CHERRY_LEAVES");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("BIRCH_LEAVES");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("AZALEA_LEAVES");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("MANGROVE_LEAVES");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("OAK_SAPLING");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("SPRUCE_SAPLING");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("BIRCH_SAPLING");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("JUNGLE_SAPLING");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("ACACIA_SAPLING");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("CHERRY_SAPLING");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("DARK_OAK_SAPLING");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("PALE_OAK_SAPLING");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("MANGROVE_PROPAGULE");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("BEETROOT_SEEDS");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("DRIED_KELP");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("SHORT_GRASS");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("KELP");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("MELON_SEEDS");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("PUMPKIN_SEEDS");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("SEAGRASS");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("SWEET_BERRIES");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("GLOW_BERRIES");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("WHEAT_SEEDS");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("MOSS_CARPET");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("PALE_MOSS_CARPET");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("PALE_HANGING_MOSS");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("PINK_PETALS");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("SMALL_DRIPLEAF");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("HANGING_ROOTS");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("MANGROVE_ROOTS");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("TORCHFLOWER_SEEDS");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("PITCHER_POD");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.3F)));
        }
        tryToGet = Material.getMaterial("DRIED_KELP_BLOCK");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.5F)));
        }
        tryToGet = Material.getMaterial("TALL_GRASS");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.5F)));
        }
        tryToGet = Material.getMaterial("FLOWERING_AZALEA_LEAVES");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.5F)));
        }
        tryToGet = Material.getMaterial("CACTUS");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.5F)));
        }
        tryToGet = Material.getMaterial("SUGAR_CANE");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.5F)));
        }
        tryToGet = Material.getMaterial("VINE");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.5F)));
        }
        tryToGet = Material.getMaterial("NETHER_SPROUTS");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.5F)));
        }
        tryToGet = Material.getMaterial("WEEPING_VINES");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.5F)));
        }
        tryToGet = Material.getMaterial("TWISTING_VINES");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.5F)));
        }
        tryToGet = Material.getMaterial("MELON_SLICE");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.5F)));
        }
        tryToGet = Material.getMaterial("GLOW_LICHEN");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.5F)));
        }
        tryToGet = Material.getMaterial("SEA_PICKLE");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("LILY_PAD");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("PUMPKIN");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("CARVED_PUMPKIN");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("MELON");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("APPLE");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("BEETROOT");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("CARROT");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("COCOA_BEANS");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("POTATO");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("WHEAT");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("BROWN_MUSHROOM");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("RED_MUSHROOM");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("MUSHROOM_STEM");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("CRIMSON_FUNGUS");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("WARPED_FUNGUS");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("NETHER_WART");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("CRIMSON_ROOTS");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("WARPED_ROOTS");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("SHROOMLIGHT");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("DANDELION");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("POPPY");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("BLUE_ORCHID");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("ALLIUM");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("AZURE_BLUET");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("RED_TULIP");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("ORANGE_TULIP");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("WHITE_TULIP");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("PINK_TULIP");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("OXEYE_DAISY");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("CORNFLOWER");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("LILY_OF_THE_VALLEY");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("WITHER_ROSE");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("OPEN_EYEBLOSSOM");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("CLOSED_EYEBLOSSOM");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("FERN");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("SUNFLOWER");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("LILAC");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("ROSE_BUSH");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("PEONY");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("LARGE_FERN");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("SPORE_BLOSSOM");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("AZALEA");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("MOSS_BLOCK");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("PALE_MOSS_BLOCK");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("BIG_DRIPLEAF");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.65F)));
        }
        tryToGet = Material.getMaterial("HAY_BLOCK");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.85F)));
        }
        tryToGet = Material.getMaterial("BROWN_MUSHROOM_BLOCK");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.85F)));
        }
        tryToGet = Material.getMaterial("RED_MUSHROOM_BLOCK");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.85F)));
        }
        tryToGet = Material.getMaterial("NETHER_WART_BLOCK");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.85F)));
        }
        tryToGet = Material.getMaterial("WARPED_WART_BLOCK");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.85F)));
        }
        tryToGet = Material.getMaterial("FLOWERING_AZALEA");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.85F)));
        }
        tryToGet = Material.getMaterial("BREAD");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.85F)));
        }
        tryToGet = Material.getMaterial("BAKED_POTATO");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.85F)));
        }
        tryToGet = Material.getMaterial("COOKIE");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.85F)));
        }
        tryToGet = Material.getMaterial("TORCHFLOWER");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.85F)));
        }
        tryToGet = Material.getMaterial("PITCHER_PLANT");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(0.85F)));
        }
        tryToGet = Material.getMaterial("CAKE");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(1.0F)));
        }
        tryToGet = Material.getMaterial("PUMPKIN_PIE");
        if (tryToGet != null){
            COMPOSTER_ACCEPT_CHANCES.add(new SimplePair<>(new ItemStack(tryToGet),BigRational.valueOf(1.0F)));
        }

    }
}
