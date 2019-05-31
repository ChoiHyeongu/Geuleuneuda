package songpatechnicalhighschool.motivation.geuleuneuda.Adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import songpatechnicalhighschool.motivation.geuleuneuda.R;
import songpatechnicalhighschool.motivation.geuleuneuda.SensorDevice;


public class DeviceListAdapter extends ArrayAdapter<SensorDevice> {

    private LayoutInflater layoutInfalter;
    private ArrayList<SensorDevice> devices;
    private int viewResId;

    public DeviceListAdapter(Context context, int tvResourceId, ArrayList<SensorDevice> devices) {
        super(context, tvResourceId, devices);
        this.devices = devices;
        layoutInfalter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewResId = tvResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInfalter.inflate(viewResId, null);

        SensorDevice device = devices.get(position);

        if (device != null) {
            TextView deviceName = (TextView) convertView.findViewById(R.id.list_title);
            TextView deviceSubtitle = (TextView) convertView.findViewById(R.id.list_subtitle);

            if (deviceName != null) {
                deviceName.setText(device.getDevice().getName());
            } else {
                deviceName.setText("Untitled");
            }

            if(deviceSubtitle != null){
                deviceSubtitle.setText("Subtitle");
            }
        }

        return convertView;
    }
}

