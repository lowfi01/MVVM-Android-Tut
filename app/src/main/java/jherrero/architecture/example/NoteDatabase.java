package jherrero.architecture.example;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


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
                    .addCallback(roomCallback)  // when instance is created it will pre-populate our database
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

    // Method is used so ass to prepopulate our database with data
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {

        // Called the first time the database is created
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    // asynctask for database operations, thread should be running in a background thread
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {  // we don't pass anything to AsyncTask

        private NoteDao noteDao;

        private PopulateDbAsyncTask(NoteDatabase db) {
            noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Title 1", "Description 1", 1));
            noteDao.insert(new Note("Title 2", "Description 2", 1));
            noteDao.insert(new Note("Title 3", "Description 3", 2));
            noteDao.insert(new Note("Title 4", "Description 4", 3));
            return null;
        }
    }

}


