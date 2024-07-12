package com.peky.irrigation_app.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.peky.irrigation_app.database.entities.AutomaticWateringLogs;

import java.util.List;

@Dao
public interface AutomaticWateringDao {
    @Insert
    void insert(AutomaticWateringLogs automaticWateringLogs);

    @Update
    void update(AutomaticWateringLogs automaticWateringLogs);

    @Delete
    void delete(AutomaticWateringLogs automaticWateringLogs);

    @Query("SELECT * FROM AutomaticWateringLogs")
    List<AutomaticWateringLogs> getAll();

    @Query("SELECT * FROM AutomaticWateringLogs WHERE id = :id")
    AutomaticWateringLogs getById(long id);
}