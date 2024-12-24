package fr.vbillard.lasertargetcompanion.repository.entities;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Tir {
    @PrimaryKey(autoGenerate = true)
    long tirId;

    @Embedded
    public Session session;
     int ordre;
     int score;

     int x;
     int y;

}
