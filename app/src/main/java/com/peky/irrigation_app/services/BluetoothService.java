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

    private static BluetoothService instance;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private Context context;
    private AlertDialog alertDialog;

    private Thread receiveThread;
    private boolean receiving = false;

    // Private constructor to prevent instantiation
    private BluetoothService(Context context) {
        this.context = context;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    // Method to get the single instance of BluetoothService
    public static synchronized BluetoothService getInstance(Context context) {
        if (instance == null) {
            instance = new BluetoothService(context);
        }
        return instance;
    }

    public boolean isBluetoothSupported() {
        return bluetoothAdapter != null;
    }

    public boolean isBluetoothEnabled() {
        return bluetoothAdapter.isEnabled();
    }

    public boolean isDevicePaired(String deviceAddress) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            if (device.getAddress().equals(deviceAddress)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDeviceConnected() {
        return bluetoothSocket != null && bluetoothSocket.isConnected();
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
            Toast.makeText(context, "Device information is invalid", Toast.LENGTH_SHORT).show();
            return;
        }
        String deviceAddress = deviceDetails[1];
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        // Use getActivity() to get the current activity context
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
                bluetoothSocket = socket;
                ((Activity) context).runOnUiThread(() -> {
                    alertDialog.dismiss();
                    Toast.makeText(context, "Bluetooth connection is successful", Toast.LENGTH_SHORT).show();
                });
            } catch (IOException e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(() -> {
                    alertDialog.dismiss();
                    Toast.makeText(context, "Failed to connect to Bluetooth device", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    public void sendData(String data, String message) {
        if (bluetoothSocket != null && bluetoothSocket.isConnected()) {
            try {
                bluetoothSocket.getOutputStream().write(data.getBytes());
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Error al enviar el comando", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Bluetooth no está conectado", Toast.LENGTH_SHORT).show();
        }
    }

    public String receiveData() {
        if (bluetoothSocket != null && bluetoothSocket.isConnected()) {
            try {
                byte[] buffer = new byte[1024]; // Buffer de tamaño adecuado
                int bytes;
                bytes = bluetoothSocket.getInputStream().read(buffer);
                String receivedData = new String(buffer, 0, bytes);
                Toast.makeText(context, "Datos recibidos exitosamente", Toast.LENGTH_SHORT).show();
                return receivedData;
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Error al recibir datos", Toast.LENGTH_SHORT).show();
                return null;
            }
        } else {
            Toast.makeText(context, "Bluetooth no está conectado", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public void closeConnection() {
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
                bluetoothSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Failed to connect to close bluetooth connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void startReceivingData() {
        receiving = true;
        receiveThread = new Thread(() -> {
            while (receiving) {
                try {
                    byte[] buffer = new byte[1024]; // Buffer de tamaño adecuado
                    int bytes = bluetoothSocket.getInputStream().read(buffer);
                    if (bytes != -1) {
                        String receivedData = new String(buffer, 0, bytes);
                        // Manejar los datos recibidos aquí (por ejemplo, actualizar la UI)
                        ((Activity) context).runOnUiThread(() -> {
                            Toast.makeText(context, "Datos recibidos: " + receivedData, Toast.LENGTH_SHORT).show();
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        receiveThread.start();
    }

    public void stopReceivingData() {
        receiving = false;
        if (receiveThread != null && receiveThread.isAlive()) {
            try {
                receiveThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}