package fr.vbillard.lasertargetcompanion.repository.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Utilisateur {

    @PrimaryKey(autoGenerate = true)
    public long utilisateurId;

    public String name;
}
