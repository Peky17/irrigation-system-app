package com.peky.irrigation_app.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity
public class MainActuatorLogs {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private Date date;
    private boolean status;

    public MainActuatorLogs(){}

    public MainActuatorLogs(Date date, boolean status) {
        this.date = date;
        this.status = status;
    }

    public MainActuatorLogs(long id, Date date, boolean status) {
        this.id = id;
        this.date = date;
        this.status = status;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}