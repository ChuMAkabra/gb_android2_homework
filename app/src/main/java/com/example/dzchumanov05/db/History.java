package com.example.dzchumanov05.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity (indices = {@Index({"date", "time"})})
public class History {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo (typeAffinity = ColumnInfo.INTEGER)
    long id;
    String date;
    String time;
    String city;
    String temp;
}
