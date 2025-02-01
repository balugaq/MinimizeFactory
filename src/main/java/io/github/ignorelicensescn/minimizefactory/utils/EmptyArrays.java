package io.github.ignorelicensescn.minimizefactory.utils;

import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.serializationwrappers.ItemStackSerializationWrapper;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;
import io.github.ignorelicensescn.minimizefactory.utils.datastructures.records.ItemStacksToStackRecipe;
import io.github.ignorelicensescn.minimizefactory.utils.mathutils.BigRational;
import io.github.ignorelicensescn.minimizefactory.utils.mathutils.IntegerRational;
import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.ArrayUtils;
import org.bukkit.inventory.ItemStack;

public class EmptyArrays {
    public static final BigRational[] EMPTY_BIG_RATIONAL_ARRAY =  new BigRational[0];
    public static final IntegerRational[] EMPTY_INTEGER_RATIONAL_ARRAY =  new IntegerRational[0];
    public static final ItemStack[] EMPTY_ITEM_STACK_ARRAY = new ItemStack[0];
    public static final ItemStackSerializationWrapper[] EMPTY_ITEM_STACK_WRAPPER_ARRAY = new ItemStackSerializationWrapper[0];
    public static final SerializeFriendlyBlockLocation[] EMPTY_SERIALIZE_FRIENDLY_LOCATION_ARRAY = new SerializeFriendlyBlockLocation[0];
    public static final String[] EMPTY_STRING_ARRAY = ArrayUtils.EMPTY_STRING_ARRAY;
    public static final ItemStacksToStackRecipe[] EMPTY_STACKS_TO_STACK_RECIPE = new ItemStacksToStackRecipe[0];
}
