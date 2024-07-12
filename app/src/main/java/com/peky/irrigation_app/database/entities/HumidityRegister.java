package com.peky.irrigation_app.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;
import java.sql.Time;

@Entity
public class HumidityRegister {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private Date date;
    private Time time;
    private float humidity;
    private String sensorName;

    public HumidityRegister(){}

    public HumidityRegister(Date date, Time time, float humidity, String sensorName) {
        this.date = date;
        this.time = time;
        this.humidity = humidity;
        this.sensorName = sensorName;
    }

    public HumidityRegister(long id, Date date, Time time, float humidity, String sensorName) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.humidity = humidity;
        this.sensorName = sensorName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }
}
