package com.peky.irrigation_app.ui.pair;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.peky.irrigation_app.R;
import com.peky.irrigation_app.services.BluetoothService;

import java.util.ArrayList;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class BluetoothDevicesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<String> deviceList;
    private BluetoothService bluetoothService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetooth_devices, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        bluetoothService = BluetoothService.getInstance(getContext());

        if (!bluetoothService.isBluetoothSupported()) {
            Toast.makeText(getContext(), "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            return view;
        }

        if (!bluetoothService.isBluetoothEnabled()) {
            Toast.makeText(getContext(), "Please enable Bluetooth", Toast.LENGTH_SHORT).show();
            return view;
        }

        deviceList = bluetoothService.listPairedDevices();
        BluetoothDeviceAdapter adapter = new BluetoothDeviceAdapter(deviceList, this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fabBack = view.findViewById(R.id.fabBack);
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigateUp();
            }
        });

        return view;
    }

    protected void connectToDevice(String deviceInfo) {
        bluetoothService.connectToDevice(deviceInfo);
    }
}