package com.peky.irrigation_app.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<Boolean> mainActuatorState = new MutableLiveData<>();
    private MutableLiveData<Boolean> secondaryActuatorState = new MutableLiveData<>();
    private MutableLiveData<Boolean> automaticWateringState = new MutableLiveData<>();

    // Constructor y métodos existentes

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

    // Métodos para cambiar el estado de los switches
    public void setMainActuatorState(boolean state) {
        if (!automaticWateringState.getValue()) { // Solo permite cambios si el riego automático no está activado
            mainActuatorState.setValue(state);
        }
    }

    public void setSecondaryActuatorState(boolean state) {
        if (!automaticWateringState.getValue()) { // Solo permite cambios si el riego automático no está activado
            secondaryActuatorState.setValue(state);
        }
    }

    public void setAutomaticWateringState(boolean state) {
        automaticWateringState.setValue(state);
        // Deshabilitar los otros switches cuando se activa el riego automático
        if (state) {
            mainActuatorState.setValue(false);
            secondaryActuatorState.setValue(false);
        }
    }
}