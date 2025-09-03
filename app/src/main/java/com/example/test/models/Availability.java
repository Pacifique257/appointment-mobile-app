package com.example.test.models;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "availabilities")
public class Availability {

    @PrimaryKey
    private int id;
    private int doctorId;
    private String date;
    private String dayOfWeek;
    private String timeSlot;

    public Availability(int id, int doctorId, String date, String dayOfWeek, String timeSlot) {
        this.id = id;
        this.doctorId = doctorId;
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.timeSlot = timeSlot;
    }

    // Getters & Setters
    public int getId() { return id; }
    public int getDoctorId() { return doctorId; }
    public String getDate() { return date; }
    public String getDayOfWeek() { return dayOfWeek; }
    public String getTimeSlot() { return timeSlot; }
}
