package io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization;

import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;

import java.io.IOException;

public interface LocationBasedInfoProvider<T> {
    T getFromLocation(SerializeFriendlyBlockLocation location);
    void saveToLocation(T info,SerializeFriendlyBlockLocation toLocation) throws IOException;
    void saveToLocationNoThrow(T info,SerializeFriendlyBlockLocation toLocation);
}
