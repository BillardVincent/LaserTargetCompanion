package fr.vbillard.lasertargetcompanion.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import fr.vbillard.lasertargetcompanion.repository.entities.Session;
import fr.vbillard.lasertargetcompanion.repository.entities.Tir;


@Database(entities = {Session.class, Tir.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {


    // --- SINGLETON ---
    private static volatile AppDatabase INSTANCE;

    // --- DAO ---
    public abstract SessionDao sessionDao();

    // --- INSTANCE ---
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "LaserTargetCompanion.db")
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

// ---

    private static Callback prepopulateDatabase() {
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
/*
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", 1);
        contentValues.put("username", "Philippe");
        contentValues.put("urlPicture", "https://oc-user.imgix.net/users/avatars/15175844164713_frame_523.jpg?auto=compress,format&q=80&h=100&dpr=2");

        db.insert("User", OnConflictStrategy.IGNORE, contentValues);

 */
            }
        };
    }
}

