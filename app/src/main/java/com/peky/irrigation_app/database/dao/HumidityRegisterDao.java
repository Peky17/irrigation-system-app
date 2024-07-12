package com.peky.irrigation_app.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.peky.irrigation_app.database.entities.HumidityRegister;

import java.util.List;

@Dao
public interface HumidityRegisterDao {
    @Insert
    void insert(HumidityRegister humidityRegister);

    @Update
    void update(HumidityRegister humidityRegister);

    @Delete
    void delete(HumidityRegister humidityRegister);

    @Query("SELECT * FROM HumidityRegister")
    List<HumidityRegister> getAll();

    @Query("SELECT * FROM HumidityRegister WHERE id = :id")
    HumidityRegister getById(long id);
}