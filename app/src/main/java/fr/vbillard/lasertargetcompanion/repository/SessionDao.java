package fr.vbillard.lasertargetcompanion.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fr.vbillard.lasertargetcompanion.repository.entities.Session;

@Dao
public interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertSession (Session... sessions);

    @Update
    public void updateSession(Session... sessions);

    @Delete
    public void deleteSession(Session... sessions);

    @Query("SELECT * FROM session")
    public List<Session> getAllSessions();
}
