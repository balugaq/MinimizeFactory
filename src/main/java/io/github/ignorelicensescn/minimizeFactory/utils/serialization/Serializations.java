package io.github.ignorelicensescn.minimizeFactory.utils.serialization;

import java.math.BigInteger;

import static io.github.ignorelicensescn.minimizeFactory.utils.itemstackrelated.ItemStackUtil.emptyDividingsArray_BigInteger;
import static io.github.ignorelicensescn.minimizeFactory.utils.compatibilities.InfinityExpansion.InfinityExpansionConsts.emptyItemStackArray;
import static io.github.ignorelicensescn.minimizeFactory.utils.serialization.Serializer.*;

public class Serializations {
    public static final String EMPTY_ITEM_STACK_ARRAY_BASE64 = ITEM_STACK_ARRAY_SERIALIZER.SerializableToString(emptyItemStackArray);
    public static final String EMPTY_BIG_RATIONAL_BASE64 = BIG_RATIONAL_ARRAY_SERIALIZER.SerializableToString(emptyDividingsArray_BigInteger);
    public static final String BIGINTEGER_ZERO_BASE64 = BIG_INTEGER_SERIALIZER.SerializableToString(BigInteger.ZERO);
}
