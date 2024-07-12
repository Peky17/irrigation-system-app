package com.peky.irrigation_app.database.config;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.peky.irrigation_app.database.dao.AutomaticWateringDao;
import com.peky.irrigation_app.database.dao.HumidityRegisterDao;
import com.peky.irrigation_app.database.dao.MainActuatorDao;
import com.peky.irrigation_app.database.dao.SecondaryActuatorDao;
import com.peky.irrigation_app.database.entities.AutomaticWateringLogs;
import com.peky.irrigation_app.database.entities.HumidityRegister;
import com.peky.irrigation_app.database.entities.MainActuatorLogs;
import com.peky.irrigation_app.database.entities.SecondaryActuatorLogs;

@Database(entities = {
        MainActuatorLogs.class,
        SecondaryActuatorLogs.class,
        AutomaticWateringLogs.class,
        HumidityRegister.class }, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    /* DAOs */
    public abstract MainActuatorDao mainActuatorLogsDao();
    public abstract SecondaryActuatorDao secondaryActuatorLogsDao();
    public abstract AutomaticWateringDao automaticWateringDao();
    public abstract HumidityRegisterDao humidityRegisterDao();
}
