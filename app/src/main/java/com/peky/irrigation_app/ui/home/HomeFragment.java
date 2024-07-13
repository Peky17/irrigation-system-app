package com.peky.irrigation_app.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.peky.irrigation_app.databinding.FragmentHomeBinding;
import com.peky.irrigation_app.services.BluetoothService;
import com.peky.irrigation_app.services.DeviceCommandService;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private BluetoothService bluetoothService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Obtener la instancia del servicio de Bluetooth
        bluetoothService = BluetoothService.getInstance(requireContext());

        // Observa los cambios en los estados de los switches y actualiza la UI
        observeSwitches();

        return root;
    }

    private void observeSwitches() {
        // Observa el estado del actuador principal
        homeViewModel.getMainActuatorState().observe(getViewLifecycleOwner(), isChecked -> {
            if (binding.mainActuatorSwitch.isChecked() != isChecked) {
                binding.mainActuatorSwitch.setChecked(isChecked);
            }
        });

        // Observa el estado del actuador secundario
        homeViewModel.getSecondaryActuatorState().observe(getViewLifecycleOwner(), isChecked -> {
            if (binding.secondaryActuatorSwitch.isChecked() != isChecked) {
                binding.secondaryActuatorSwitch.setChecked(isChecked);
            }
        });

        // Observa el estado del riego automático
        homeViewModel.getAutomaticWateringState().observe(getViewLifecycleOwner(), isChecked -> {
            if (binding.automaticWateringSwitch.isChecked() != isChecked) {
                binding.automaticWateringSwitch.setChecked(isChecked);
            }
        });

        // Configura listeners para cambiar el estado de los switches
        binding.mainActuatorSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Boolean.TRUE.equals(homeViewModel.getMainActuatorState().getValue()) != isChecked) {
                homeViewModel.setMainActuatorState(isChecked);
                DeviceCommandService command = isChecked ? DeviceCommandService.MAIN_ACTUATOR_ON : DeviceCommandService.MAIN_ACTUATOR_OFF;
                sendBluetoothCommand(command.getCommand(), "Actuador principal activado");
            }
        });

        binding.secondaryActuatorSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Boolean.TRUE.equals(homeViewModel.getSecondaryActuatorState().getValue()) != isChecked) {
                homeViewModel.setSecondaryActuatorState(isChecked);
                DeviceCommandService command = isChecked ? DeviceCommandService.SECONDARY_ACTUATOR_ON : DeviceCommandService.SECONDARY_ACTUATOR_OFF;
                sendBluetoothCommand(command.getCommand(), "Actuador secundario activado");
            }
        });

        binding.automaticWateringSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Boolean.TRUE.equals(homeViewModel.getAutomaticWateringState().getValue()) != isChecked) {
                homeViewModel.setAutomaticWateringState(isChecked);
                DeviceCommandService command = isChecked ? DeviceCommandService.AUTOMATIC_WATERING_ON : DeviceCommandService.AUTOMATIC_WATERING_OFF;
                sendBluetoothCommand(command.getCommand(), "Riego automático activado");
            }
        });
    }

    private void sendBluetoothCommand(String btCommand, String message) {
        if (bluetoothService.isBluetoothEnabled()) {
            if (bluetoothService.isDeviceConnected()) {
                bluetoothService.sendData(btCommand, message);
            } else {
                Toast.makeText(requireContext(), "bluetooth is not connected", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Bluetooth is not enabled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}