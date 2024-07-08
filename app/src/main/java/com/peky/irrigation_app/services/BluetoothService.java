package com.peky.irrigation_app.services;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.peky.irrigation_app.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BluetoothService {

    private BluetoothAdapter bluetoothAdapter;
    private Context context;
    private AlertDialog alertDialog;

    public BluetoothService(Context context) {
        this.context = context;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public boolean isBluetoothSupported() {
        return bluetoothAdapter != null;
    }

    public boolean isBluetoothEnabled() {
        return bluetoothAdapter.isEnabled();
    }

    public ArrayList<String> listPairedDevices() {
        ArrayList<String> deviceList = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return deviceList;
        }
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                deviceList.add(device.getName() + "\n" + device.getAddress());
            }
        }
        return deviceList;
    }

    public void connectToDevice(String deviceInfo) {
        String[] deviceDetails = deviceInfo.split("\n");
        if (deviceDetails.length < 2) {
            Toast.makeText(context, "Invalid device information", Toast.LENGTH_SHORT).show();
            return;
        }
        String deviceAddress = deviceDetails[1];
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.progress, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();

        new Thread(() -> {
            try {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
                socket.connect();
                ((Activity) context).runOnUiThread(() -> {
                    alertDialog.dismiss();
                    Toast.makeText(context, "Conexión BT exitosa", Toast.LENGTH_SHORT).show();
                });
            } catch (IOException e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(() -> {
                    alertDialog.dismiss();
                    Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
}