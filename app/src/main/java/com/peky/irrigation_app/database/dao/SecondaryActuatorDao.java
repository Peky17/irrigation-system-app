package com.peky.irrigation_app.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.peky.irrigation_app.database.entities.SecondaryActuatorLogs;

import java.util.List;

@Dao
public interface SecondaryActuatorDao {
    @Insert
    void insert(SecondaryActuatorLogs secondaryActuatorLogs);

    @Update
    void update(SecondaryActuatorLogs secondaryActuatorLogs);

    @Delete
    void delete(SecondaryActuatorLogs secondaryActuatorLogs);

    @Query("SELECT * FROM SecondaryActuatorLogs")
    List<SecondaryActuatorLogs> getAll();

    @Query("SELECT * FROM SecondaryActuatorLogs WHERE id = :id")
    SecondaryActuatorLogs getById(long id);
}
