package com.peky.irrigation_app.ui.home;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.peky.irrigation_app.services.BluetoothService;
import com.peky.irrigation_app.services.DeviceCommandService;
import com.peky.irrigation_app.utils.JsonUtils;

import org.json.JSONObject;

public class HomeViewModel extends ViewModel {

    private BluetoothService bluetoothService;
    private MutableLiveData<Boolean> mainActuatorState = new MutableLiveData<>();
    private MutableLiveData<Boolean> secondaryActuatorState = new MutableLiveData<>();
    private MutableLiveData<Boolean> automaticWateringState = new MutableLiveData<>();
    private MutableLiveData<String> receivedData = new MutableLiveData<>();

    public HomeViewModel() {
        // Inicializa el estado de los switches
        mainActuatorState.setValue(false);
        secondaryActuatorState.setValue(false);
        automaticWateringState.setValue(false);
    }

    // Métodos para obtener los LiveData de los estados de los switches
    public LiveData<Boolean> getMainActuatorState() {
        return mainActuatorState;
    }

    public LiveData<Boolean> getSecondaryActuatorState() {
        return secondaryActuatorState;
    }

    public LiveData<Boolean> getAutomaticWateringState() {
        return automaticWateringState;
    }

    public LiveData<String> getReceivedData() {
        return receivedData;
    }

    // Métodos para cambiar el estado de los switches
    public void setMainActuatorState(boolean state) {
        mainActuatorState.setValue(state);
    }

    public void setSecondaryActuatorState(boolean state) {
        secondaryActuatorState.setValue(state);
    }

    public void setAutomaticWateringState(boolean state) {
        automaticWateringState.setValue(state);
    }

    // Inicializa BluetoothService
    public void initBluetoothService(Context context) {
        bluetoothService = BluetoothService.getInstance(context);
    }

    // Método para cargar el estado de los switches
    public void loadSwitchStates(Context context) {
        if (bluetoothService != null && bluetoothService.isBluetoothEnabled() && bluetoothService.isDeviceConnected()) {
            DeviceCommandService command = DeviceCommandService.DEVICE_STATUS;
            bluetoothService.sendData(command.getCommand(), "Loading device status...");

            // Recibe los datos
            new Thread(() -> {
                String data = bluetoothService.receiveData();
                if (data != null) {
                    receivedData.postValue(data);
                }
            }).start();
        } else {
            Toast.makeText(context, "Bluetooth is not enabled or not connected", Toast.LENGTH_SHORT).show();
        }
    }

    public void processReceivedData(String data) {
        try {
            JSONObject jsonObject = JsonUtils.stringToJsonObject(data);
            if (jsonObject != null) {
                int mainActuator = JsonUtils.getIntFromJsonObject(jsonObject, "MainActuator");
                int secondaryActuator = JsonUtils.getIntFromJsonObject(jsonObject, "SecondaryActuator");

                setMainActuatorState(mainActuator == 1); // true si mainActuator es 1, false si es 0
                setSecondaryActuatorState(secondaryActuator == 1); // true si secondaryActuator es 1, false si es 0
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}