package com.example.distancemeasure;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.ViewHolder>  {
    private List<BluetoothDevice> deviceList = new ArrayList<>();
    static class ViewHolder extends RecyclerView.ViewHolder {

        View deviceView;
        ImageView deviceImage;
        TextView deviceName;
        TextView deviceAddress;

        public ViewHolder(View view) {
            super(view);
            deviceView = view;
            deviceImage = view.findViewById(R.id.Blueteethimage);
            deviceName = view.findViewById(R.id.device_name);
            deviceAddress = view.findViewById(R.id.device_address);
        }
    }

    public DevicesAdapter(List<BluetoothDevice> deviceList) { this.deviceList = deviceList; }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item,
                parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.deviceView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                BluetoothDevice bd = deviceList.get(position);
                String detail = "Name: " + bd.getName() + " Address:" + bd.getAddress()
                        + " Type:" + bd.getType() + " UUid:" + bd.getUuids() +
                        " State: " + bd.getBondState() + " Class: " + bd.getBluetoothClass();
                Toast.makeText(view.getContext(), detail,
                        Toast.LENGTH_SHORT).show();
            }
        });
        holder.deviceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                BluetoothDevice bd = deviceList.get(position);
                String detail = "Name: " + bd.getName() + " Address:" + bd.getAddress()
                        + " Type:" + bd.getType() + " UUid:" + bd.getUuids() +
                        " State: " + bd.getBondState() + " Class: " + bd.getBluetoothClass();
                Toast.makeText(view.getContext(), detail,
                        Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        BluetoothDevice device = deviceList.get(i);
        viewHolder.deviceName.setText(device.getName());
        viewHolder.deviceAddress.setText((device.getAddress()));
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }
}
