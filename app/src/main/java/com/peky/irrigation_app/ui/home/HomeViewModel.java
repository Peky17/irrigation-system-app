package com.peky.irrigation_app.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<Boolean> mainActuatorState = new MutableLiveData<>();
    private MutableLiveData<Boolean> secondaryActuatorState = new MutableLiveData<>();
    private MutableLiveData<Boolean> automaticWateringState = new MutableLiveData<>();

    public HomeViewModel() {
        // Inicializa el estado de los switches (Cargar el valor almacenado en base de datos)
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
}