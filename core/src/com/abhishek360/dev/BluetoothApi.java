package com.abhishek360.dev;



import java.util.Set;

public interface BluetoothApi
{
    public boolean isBluetoothAvailable();

    public void turnBluetoothOn(String data);

    //public void enableBluetoothDiscovering(MultiplayerSettingsWindow multiplayerSettingsWindow);

    public boolean startDiscoverBluetooth();

    public String isMyDevicePaired(String deviceMac);


}
