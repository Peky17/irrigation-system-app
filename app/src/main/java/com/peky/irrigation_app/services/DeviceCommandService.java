package com.peky.irrigation_app.services;

public enum DeviceCommandService {

    MAIN_ACTUATOR_ON("A"),
    MAIN_ACTUATOR_OFF("B"),
    SECONDARY_ACTUATOR_ON("C"),
    SECONDARY_ACTUATOR_OFF("D"),
    AUTOMATIC_WATERING_ON("Y"),
    AUTOMATIC_WATERING_OFF("Z"),
    DEVICE_STATUS("E"),
    HUMIDITY_SENSOR_ONE_INFO("F"),
    HUMIDITY_SENSOR_TWO_INFO("G"),
    HUMIDITY_INFO("H");

    private final String command;

    DeviceCommandService(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}