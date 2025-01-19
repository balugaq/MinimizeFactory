package io.github.ignorelicensescn.minimizeFactory.utils.serialization;

import io.github.ignorelicensescn.minimizeFactory.utils.mathutils.BigRational;

import java.math.BigInteger;

import static io.github.ignorelicensescn.minimizeFactory.utils.ItemStackUtil.emptyDividingsArray_BigInteger;
import static io.github.ignorelicensescn.minimizeFactory.utils.compabilities.InfinityExpansion.InfinityExpansionConsts.emptyItemStackArray;
import static io.github.ignorelicensescn.minimizeFactory.utils.serialization.Serializer.*;
import static io.github.ignorelicensescn.minimizeFactory.utils.serialization.UnifiedSerializer.ITEM_STACK_ARRAY_SERIALIZER;

public class Serializations {
    public static String EMPTY_ITEM_STACK_ARRAY_BASE64 = ITEM_STACK_ARRAY_SERIALIZER.SerializableToString(emptyItemStackArray);
    public static String EMPTY_BIG_RATIONAL_BASE64 = BIG_RATIONAL_ARRAY_SERIALIZER.SerializableToString(emptyDividingsArray_BigInteger);
    public static String BIGINTEGER_ZERO_BASE64 = BIG_INTEGER_SERIALIZER.SerializableToString(BigInteger.ZERO);
}
