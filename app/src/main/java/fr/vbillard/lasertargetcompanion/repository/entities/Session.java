package fr.vbillard.lasertargetcompanion.repository.entities;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Session {
    @PrimaryKey(autoGenerate = true)
    public long sessionId;

    @Embedded
    public Utilisateur utilisateur;

    public int distance;

    private long timestamp;


}
