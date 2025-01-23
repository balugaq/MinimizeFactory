package io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated;

public class ConnectorSettings {
    public String coreLocation;
    public boolean connectMode = true;//true:connect;false:disconnect
    public ConnectorSettings(String coreLocation){
        this.coreLocation = coreLocation;
    }
}
