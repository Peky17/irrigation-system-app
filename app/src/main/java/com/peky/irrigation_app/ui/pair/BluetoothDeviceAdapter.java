package com.peky.irrigation_app.ui.pair;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.peky.irrigation_app.R;

import java.util.ArrayList;

public class BluetoothDeviceAdapter extends RecyclerView.Adapter<BluetoothDeviceAdapter.ViewHolder> {

    private BluetoothDevicesFragment bluetoothDevicesFragment;
    private ArrayList<String> deviceList;

    public BluetoothDeviceAdapter(ArrayList<String> deviceList, BluetoothDevicesFragment bluetoothDevicesFragment) {
        this.deviceList = deviceList;
        this.bluetoothDevicesFragment = bluetoothDevicesFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bt_device_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String device = deviceList.get(position);
        holder.deviceInfo.setText(device);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothDevicesFragment.connectToDevice(deviceList.get(holder.getBindingAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView deviceInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceInfo = itemView.findViewById(R.id.deviceInfo);
        }
    }
}