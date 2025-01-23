package io.github.ignorelicensescn.minimizefactory.utils.serialization;

import java.math.BigInteger;

import static io.github.ignorelicensescn.minimizefactory.utils.EmptyArrays.EMPTY_BIG_RATIONAL_ARRAY;
import static io.github.ignorelicensescn.minimizefactory.utils.EmptyArrays.EMPTY_ITEM_STACK_ARRAY;
import static io.github.ignorelicensescn.minimizefactory.utils.serialization.Serializer.*;

public class Serializations {
    public static final String EMPTY_ITEM_STACK_ARRAY_BASE64 = ITEM_STACK_ARRAY_SERIALIZER.SerializableToString(EMPTY_ITEM_STACK_ARRAY);
    public static final String EMPTY_BIG_RATIONAL_BASE64 = BIG_RATIONAL_ARRAY_SERIALIZER.SerializableToString(EMPTY_BIG_RATIONAL_ARRAY);
    public static final String BIGINTEGER_ZERO_BASE64 = BIG_INTEGER_SERIALIZER.SerializableToString(BigInteger.ZERO);
}
