package com.example.distancemeasure;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private String TAG = "蓝牙";
    private List<BluetoothDevice> deviceList;
    private BluetoothAdapter ba;
    private Set<BluetoothDevice> devices;
    private RecyclerView recyclerView;
    private DevicesAdapter adapter;
    private BroadcastReceiver mReceiver;
    private Button scan;
    private Button refresh;
    private TextView rssi;
    private TextView distance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ba = BluetoothAdapter.getDefaultAdapter();
        deviceList = new ArrayList<>();
        devices = new HashSet<>();
        scan = findViewById(R.id.scan);
        distance = findViewById(R.id.distance);
        rssi = findViewById(R.id.rssi);
        refresh = findViewById(R.id.refresh);
        recyclerView = findViewById(R.id.DevicesList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DevicesAdapter(deviceList);

        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                    Log.d(TAG, "开始扫描...");
                }

                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device != null) {
                        if(device.getBondState() == 12){
                            deviceList.add(device);
                            int rssi_val = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                            double distance_val = RssiUtil.getDistance(rssi_val);
                            rssi.setText(String.valueOf(rssi_val));
                            distance.setText(String.valueOf(distance_val));
                            Log.d(TAG, "onReceive: RSSI" + rssi);
                            Log.d(TAG, "onReceive: Name:" + device.getName());
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

                if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    Log.d(TAG, "扫描结束.");
                }
            }
        };

        IntentFilter filterFound = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filterFound);

        IntentFilter filterStart = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(mReceiver, filterStart);

        IntentFilter filterFinish = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filterFinish);

        adapter = new DevicesAdapter(deviceList);
        recyclerView.setAdapter(adapter);



//        ba.startDiscovery();
        final BluetoothAdapter.LeScanCallback callback = new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice bd, int i, byte[] bytes) {
                devices.add(bd);
                Log.d(TAG, "onLeScan: " + bd.getName());
                adapter.notifyDataSetChanged();
            }
        };

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]
                            {Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

                }else{
                    clearRecyclerView();
                    ba.startDiscovery();
                }
            }

        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ba.isDiscovering()){
                    clearRecyclerView();
                    ba.cancelDiscovery();
                    Toast.makeText(MainActivity.this, "正在尝试重新搜索...", Toast.LENGTH_SHORT).show();
                    ba.startDiscovery();
                }
            }
        });

    }

    private void clearRecyclerView(){
        deviceList.clear();
        rssi.setText("Loading...");
        distance.setText("Loading...");
        adapter.notifyDataSetChanged();
    }


}
