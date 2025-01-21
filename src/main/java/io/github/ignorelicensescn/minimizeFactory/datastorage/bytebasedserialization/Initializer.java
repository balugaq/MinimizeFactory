package io.github.ignorelicensescn.minimizeFactory.datastorage.bytebasedserialization;

import io.github.ignorelicensescn.minimizeFactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;

public interface Initializer<T> {
    void initializeAtLocation(SerializeFriendlyBlockLocation location);
}
