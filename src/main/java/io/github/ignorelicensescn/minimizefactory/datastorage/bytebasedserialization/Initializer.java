package io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization;

import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;

public interface Initializer<T> {
    void initializeAtLocation(SerializeFriendlyBlockLocation location);

    T getOrDefault(SerializeFriendlyBlockLocation location);
}
