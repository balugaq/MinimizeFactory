package io.github.ignorelicensescn.minimizeFactory.datastorage.machinenetwork;

import io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.NodeType;

import javax.annotation.Nullable;

public class ContainerInfo extends NodeInfo{

    public ContainerInfo() {
        super(null, NodeType.BRIDGE);
    }
}
