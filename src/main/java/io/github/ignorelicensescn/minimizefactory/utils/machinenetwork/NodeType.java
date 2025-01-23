package io.github.ignorelicensescn.minimizefactory.utils.machinenetwork;

import javax.annotation.Nullable;

public enum NodeType {
    INVALID,
    CONTROLLER,
    BRIDGE,
    STORAGE,
    MACHINE_CONTAINER;

    public static boolean isValid(@Nullable NodeType toVerify){
        if (toVerify == null){return false;}
        return toVerify != INVALID;
    }
}
