package jherrero.architecture.example;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


/*
*  Connects our Note Entities tables & our NoteDao operations together
* */

@Database(entities = Note.class, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    // Database must be turned into a singleton
    private static NoteDatabase instance;


    // Used to access our dao  - CRUD operations
    public abstract NoteDao noteDao();

    // Create database which is a singleton
    public static synchronized NoteDatabase getInstance(Context context) {
        // check if instance exits
        if (instance == null ) {
            /*
            * instance cannot be new NoteDatabase as this is an abstract class, rather
            * We use the Room.databaseBuilder method to create our NoteDatabse instance
            */

            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()  // destroys database on new version
                    .build();
        }
        return instance;

         /*
         * Create single instance of database, which can be access. Should not be recreated
         *
         * synchronized means that only one thread at a time can gain access to method
         * which prevents multiple instances of database being created, when two different
         * threads attempt to access database at the same time.
         *
         *  changes to the database should increment version, but as we are in development
         *  we will just use fallbackToDestructiveMigration(), which just re-installs database
         *  and restarts to version = 1
         * */
    }

}


