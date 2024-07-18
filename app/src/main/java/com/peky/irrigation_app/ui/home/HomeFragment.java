package com.peky.irrigation_app.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.peky.irrigation_app.databinding.FragmentHomeBinding;
import com.peky.irrigation_app.services.BluetoothService;
import com.peky.irrigation_app.services.DeviceCommandService;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel.loadSwitchStates(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializa BluetoothService en el ViewModel
        homeViewModel.initBluetoothService(requireContext());

        // Observa los cambios en los estados de los switches y actualiza la UI
        observeSwitches();

        // Carga el estado de los switches
        homeViewModel.loadSwitchStates(requireContext());

        return root;
    }

    private void observeSwitches() {
        // Observa el estado del actuador principal
        homeViewModel.getMainActuatorState().observe(getViewLifecycleOwner(), isChecked -> {
            if (binding.mainActuatorSwitch.isChecked() != isChecked) {
                binding.mainActuatorSwitch.setChecked(isChecked);
            }
            // Habilita o deshabilita el switch según el estado de riego automático
            binding.mainActuatorSwitch.setEnabled(!homeViewModel.getAutomaticWateringState().getValue());
        });

        // Observa el estado del actuador secundario
        homeViewModel.getSecondaryActuatorState().observe(getViewLifecycleOwner(), isChecked -> {
            if (binding.secondaryActuatorSwitch.isChecked() != isChecked) {
                binding.secondaryActuatorSwitch.setChecked(isChecked);
            }
            // Habilita o deshabilita el switch según el estado de riego automático
            binding.secondaryActuatorSwitch.setEnabled(!homeViewModel.getAutomaticWateringState().getValue());
        });

        // Observa el estado del riego automático
        homeViewModel.getAutomaticWateringState().observe(getViewLifecycleOwner(), isChecked -> {
            if (binding.automaticWateringSwitch.isChecked() != isChecked) {
                binding.automaticWateringSwitch.setChecked(isChecked);
            }
            // Habilita o deshabilita los otros switches cuando se activa el riego automático
            binding.mainActuatorSwitch.setEnabled(!isChecked);
            binding.secondaryActuatorSwitch.setEnabled(!isChecked);
        });

        // Observa los datos recibidos
        homeViewModel.getReceivedData().observe(getViewLifecycleOwner(), data -> {
            if (data != null) {
                homeViewModel.processReceivedData(data);
            }
        });

        // Configura listeners para cambiar el estado de los switches
        binding.mainActuatorSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Boolean.TRUE.equals(homeViewModel.getMainActuatorState().getValue()) != isChecked) {
                homeViewModel.setMainActuatorState(isChecked);
                DeviceCommandService command = isChecked ? DeviceCommandService.MAIN_ACTUATOR_ON : DeviceCommandService.MAIN_ACTUATOR_OFF;
                sendBluetoothCommand(command.getCommand(), "Main actuator switched");
            }
        });

        binding.secondaryActuatorSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Boolean.TRUE.equals(homeViewModel.getSecondaryActuatorState().getValue()) != isChecked) {
                homeViewModel.setSecondaryActuatorState(isChecked);
                DeviceCommandService command = isChecked ? DeviceCommandService.SECONDARY_ACTUATOR_ON : DeviceCommandService.SECONDARY_ACTUATOR_OFF;
                sendBluetoothCommand(command.getCommand(), "Secondary actuator switched");
            }
        });

        binding.automaticWateringSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Boolean.TRUE.equals(homeViewModel.getAutomaticWateringState().getValue()) != isChecked) {
                // Cambia el estado de riego automático
                homeViewModel.setAutomaticWateringState(isChecked);
                DeviceCommandService command = isChecked ? DeviceCommandService.AUTOMATIC_WATERING_ON : DeviceCommandService.AUTOMATIC_WATERING_OFF;
                sendBluetoothCommand(command.getCommand(), "Automatic watering switched");
            }
        });
    }

    private void sendBluetoothCommand(String btCommand, String message) {
        BluetoothService bluetoothService = BluetoothService.getInstance(requireContext());
        if (bluetoothService.isBluetoothEnabled()) {
            if (bluetoothService.isDeviceConnected()) {
                bluetoothService.sendData(btCommand, message);
            } else {
                Toast.makeText(requireContext(), "Bluetooth is not connected", Toast.LENGTH_SHORT).show();
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