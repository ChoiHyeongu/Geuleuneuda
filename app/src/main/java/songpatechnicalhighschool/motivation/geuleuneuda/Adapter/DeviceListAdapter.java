package songpatechnicalhighschool.motivation.geuleuneuda.Adapter;

import android.content.Context;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import songpatechnicalhighschool.motivation.geuleuneuda.Module.PairingRequest;
import songpatechnicalhighschool.motivation.geuleuneuda.R;
import songpatechnicalhighschool.motivation.geuleuneuda.Module.SensorDevice;


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
            LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.list_layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentFilter pairingFilter = new IntentFilter("android.bluetooth.device.action.PARING_REQUEST");
                    getContext().registerReceiver(new PairingRequest(), pairingFilter);
                }
            });
            TextView deviceName = (TextView) convertView.findViewById(R.id.list_title);
            TextView deviceSubtitle = (TextView) convertView.findViewById(R.id.list_subtitle);

            if (deviceName != null) {
                String name = device.getDevice().getName().substring(4);
                deviceName.setText(name);
            } else {
                deviceName.setText("Untitled");
            }

            if (deviceSubtitle != null) {
                deviceSubtitle.setText("Subtitle");
            }
        }

        return convertView;
    }
}

