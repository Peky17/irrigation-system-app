package com.peky.irrigation_app.services;

public enum DeviceCommandService {

    MAIN_ACTUATOR_ON("R"),
    MAIN_ACTUATOR_OFF("N"),
    SECONDARY_ACTUATOR_ON("P"),
    SECONDARY_ACTUATOR_OFF("Q"),
    AUTOMATIC_WATERING_ON("M"),
    AUTOMATIC_WATERING_OFF("S");

    private final String command;

    DeviceCommandService(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}