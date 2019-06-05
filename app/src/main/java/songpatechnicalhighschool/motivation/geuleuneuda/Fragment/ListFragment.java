package songpatechnicalhighschool.motivation.geuleuneuda.Fragment;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import songpatechnicalhighschool.motivation.geuleuneuda.Adapter.DeviceListAdapter;
import songpatechnicalhighschool.motivation.geuleuneuda.InduceActivity;
import songpatechnicalhighschool.motivation.geuleuneuda.MainActivity;
import songpatechnicalhighschool.motivation.geuleuneuda.R;
import songpatechnicalhighschool.motivation.geuleuneuda.Module.SensorDevice;

import static android.bluetooth.BluetoothDevice.BOND_BONDED;

public class ListFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String TAG = "ListFragment";

    private ProgressDialog progressDialog;
    private ArrayList<SensorDevice> sensorDevices;
    private RecyclerView deviceRecyclerView;
    private MediaPlayer mediaPlayer;
    public DeviceListAdapter deviceListAdapter;
    ListView deviceListView;
    private BluetoothAdapter bluetoothAdapter;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);

        deviceListView = root.findViewById(R.id.list_listview);
        deviceListView.setOnItemClickListener(this);

        sensorDevices = new ArrayList<SensorDevice>();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        activity = getActivity();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        activity.registerReceiver(mBroadcastReceiver4, filter);

        checkBluetoothPermissions();
        //doDiscoverable();
        Toast.makeText(activity, "doDiscover", Toast.LENGTH_SHORT).show();
        doDiscover();

        return root;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: called.");
        super.onDestroy();
        //activity.unregisterReceiver(mBroadcastReceiver1);
        //activity.unregisterReceiver(mBroadcastReceiver2);
        activity.unregisterReceiver(mBroadcastReceiver3);
        activity.unregisterReceiver(mBroadcastReceiver4);
        mediaPlayer.stop();
        mediaPlayer.release();
        bluetoothAdapter.cancelDiscovery();
    }

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(bluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, bluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    /**
     * Broadcast Receiver for changes made to bluetooth states such as:
     * 1) Discoverability mode on/off or expire.
     */
    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {

                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    //Device is in Discoverable Mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Enabled.");
                        break;
                    //Device not in doDiscoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "mBroadcastReceiver2: Connecting....");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "mBroadcastReceiver2: Connected.");
                        break;
                }

            }
        }
    };


    /**
     * Broadcast Receiver for listing devices that are not yet paired
     * -Executed by doDiscover() method.
     */
    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                if (isOurDevice(device.getName())) {
                    SensorDevice sensorDevice = new SensorDevice(device, "subtitle");
                    sensorDevices.add(sensorDevice);
                    Log.d(TAG, "Sensor : " + sensorDevice.getDevice().getName());
                    deviceListAdapter = new DeviceListAdapter(context, R.layout.item_device, sensorDevices);
                    deviceListView.setAdapter(deviceListAdapter);
                }
            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases:
                //case1: bonded already
                if (mDevice.getBondState() == BOND_BONDED) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDED.");
                    hideProgress();
                    startActivity(new Intent(getContext(), InduceActivity.class));
                }
                //case2: creating a bone
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDING.");
                    hideProgress();
                    startActivity(new Intent(getContext(), InduceActivity.class));
                }
                //case3: breaking a bond
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d(TAG, "BroadcastReceiver: BOND_NONE.");
                    Toast.makeText(context, "연결을 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    boolean isOurDevice(String device) {
        if (device != null && device.length() >= 3) {
            device = device.substring(0, 3);
            Log.d("Device Name", device);
            if (device.equals("STH"))
                return true;
            else
                return false;
        } else
            return false;
    }

    public void enableDisableBluetooth() {
        if (bluetoothAdapter == null) {
            Log.d(TAG, "enableDisableBluetooth: Does not have BT capabilities.");
        }
        if (!bluetoothAdapter.isEnabled()) {
            Log.d(TAG, "enableDisableBluetooth: enabling BT.");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            activity.registerReceiver(mBroadcastReceiver1, BTIntent);
        }
        if (bluetoothAdapter.isEnabled()) {
            Log.d(TAG, "enableDisableBluetooth: disabling BT.");
            bluetoothAdapter.disable();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            activity.registerReceiver(mBroadcastReceiver1, BTIntent);
        }

    }


    public void doDiscoverable() {
        Log.d(TAG, "doDiscoverable: Making device doDiscoverable for 300 seconds.");

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        activity.startActivity(discoverableIntent);

        IntentFilter intentFilter = new IntentFilter(bluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        activity.registerReceiver(mBroadcastReceiver2, intentFilter);
    }

    public void doDiscover() {
        Log.d(TAG, "doDiscover: Looking for unpaired devices.");

        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "doDiscover: Canceling discovery.");

            //check BT permissions in manifest
            checkBluetoothPermissions();

            bluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            Log.d(TAG, "doDiscover : receiver on");
            activity.registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
        if (!bluetoothAdapter.isDiscovering()) {

            //check BT permissions in manifest
            checkBluetoothPermissions();

            bluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            activity.registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
    }

    /**
     * This method is required for all devices running API23+
     * Android must programmatically check the permissions for bluetooth. Putting the proper permissions
     * in the manifest is not enough.
     * <p>
     * NOTE: This will only execute on versions > LOLLIPOP because it is not needed otherwise.
     */
    private void checkBluetoothPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = activity.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += activity.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                ((Activity) activity).requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        1001); //Any number
            }
        } else {
            Log.d(TAG, "checkBluetoothPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //first cancel discovery because its very memory intensive.
        bluetoothAdapter.cancelDiscovery();

        Log.d(TAG, "onItemClick: You Clicked on a device.");
        String deviceName = sensorDevices.get(i).getDevice().getName();
        String deviceAddress = sensorDevices.get(i).getDevice().getAddress();

        Log.d(TAG, "onItemClick: deviceName = " + deviceName);
        Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);

        int isBonded = sensorDevices.get(i).getDevice().getBondState();
        if (isBonded == BOND_BONDED) {
            Toast.makeText(activity, "연결 되었습니다.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), InduceActivity.class));
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            showProgress(deviceName.substring(4) + "와 연결 중 입니다.");
            Log.d(TAG, "Trying to pair with " + deviceName);
            sensorDevices.get(i).getDevice().createBond();
        }
    }

    public void showProgress(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setCancelable(false);
        }
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
