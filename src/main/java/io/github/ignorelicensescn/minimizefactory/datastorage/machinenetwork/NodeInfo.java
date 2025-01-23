package io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork;

import io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.NodeType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class NodeInfo {
    @Nullable
    public SerializeFriendlyBlockLocation coreLocation = null;
    @Nonnull
    public NodeType nodeType = NodeType.BRIDGE;
    public NodeInfo(@Nullable SerializeFriendlyBlockLocation coreLocation,@Nullable NodeType nodeType){
        this.coreLocation = coreLocation;
        this.nodeType = nodeType == null?NodeType.BRIDGE:nodeType;
    }
    public NodeInfo(){}

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof NodeInfo info)) return false;

        return Objects.equals(coreLocation, info.coreLocation) && nodeType == info.nodeType;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(coreLocation);
        result = 31 * result + Objects.hashCode(nodeType);
        return result;
    }
}
