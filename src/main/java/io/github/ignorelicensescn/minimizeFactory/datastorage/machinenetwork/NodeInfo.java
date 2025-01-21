package io.github.ignorelicensescn.minimizeFactory.datastorage.machinenetwork;

import io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.NodeType;

import javax.annotation.Nullable;
import java.util.Objects;

public class NodeInfo {
    @Nullable
    public SerializeFriendlyBlockLocation coreLocation = null;
    public NodeType nodeType = NodeType.BRIDGE;
    public NodeInfo(@Nullable SerializeFriendlyBlockLocation coreLocation,NodeType nodeType){
        this.coreLocation = coreLocation;
        this.nodeType = nodeType;
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
