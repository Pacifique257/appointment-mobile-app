package com.example.test.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.test.models.Availability;

import java.util.List;

@Dao
public interface AvailabilityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Availability> availabilities);

    @Query("SELECT * FROM availabilities ORDER BY date ASC")
    List<Availability> getAll();
}
