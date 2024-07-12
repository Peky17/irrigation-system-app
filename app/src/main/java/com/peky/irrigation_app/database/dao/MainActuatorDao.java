package com.peky.irrigation_app.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.peky.irrigation_app.database.entities.MainActuatorLogs;

import java.util.List;

@Dao
public interface MainActuatorDao {

    @Insert
    void insert(MainActuatorLogs mainActuatorLogs);

    @Update
    void update(MainActuatorLogs mainActuatorLogs);

    @Delete
    void delete(MainActuatorLogs mainActuatorLogs);

    @Query("SELECT * FROM MainActuatorLogs")
    List<MainActuatorLogs> getAll();

    @Query("SELECT * FROM MainActuatorLogs WHERE id = :id")
    MainActuatorLogs getById(long id);
}