package songpatechnicalhighschool.motivation.geuleuneuda;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

public class SensorDevice {

    BluetoothDevice device;
    String subTitle;

    public SensorDevice(BluetoothDevice device, String subTitle) {
        this.device = device;
        this.subTitle = subTitle;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
