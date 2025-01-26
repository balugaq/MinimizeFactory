package io.github.ignorelicensescn.minimizefactory;

import io.github.ignorelicensescn.minimizefactory.utils.mathutils.BigRational;

public class QuickTest {

    public static void main(String[] args) {
        System.out.println(BigRational.valueOf(4.0f));
        System.out.println(Float.floatToRawIntBits(4.0f));
        System.out.println(Integer.toBinaryString(Float.floatToRawIntBits(4.0f)));
        System.out.println(Float.floatToRawIntBits(4.0f) & 0x7FFFFF);
    }
}
