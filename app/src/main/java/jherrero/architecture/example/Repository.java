package jherrero.architecture.example;




import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;


/*
 * API, abstracts our different data sources from the view
 *
 * Connects our DAO
 * Gives access to LiveData
 * */


public class Repository {

    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    // Constructor
    public Repository(Application application) {

        // create instance of database
        NoteDatabase database = NoteDatabase.getInstance(application); // application is a subclass of context

        noteDao = database.noteDao(); // though abstract class, ROOM implements body for us.
        allNotes = noteDao.getAllNotes(); // fetch all notes
    }

    /*
    * API actions - which is given by noteDao
    *
    * Room does not allow database operations on the main thread so we must use ASYNCTASK
    * to push our opearations to the background thread. As database operations will freeze
    * our application
    *
    * */

    public void insert(Note note) {
        new InsertNoteAsyncTask(noteDao).execute(note);
    }

    public void update(Note note) {
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }

    public void delete(Note note) {
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }

    public void deleteAllNotes() {
        new DeleteAllNoteAsyncTask(noteDao).execute();
    }

    // Room implements this for us automatically - ref the NoteDao interface
    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }


    // AsyncTask must be static so that it does not have a reference to the class itself or
    // this will cause a memory leak
    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        // constructor is required as class is private
        private InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]); // we are only passing 1 note to our varargs
            return null;
        }
    }



    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        // constructor is required as class is private
        private UpdateNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]); // we are only passing 1 note to our varargs
            return null;
        }
    }


    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        // constructor is required as class is private
        private DeleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]); // we are only passing 1 note to our varargs
            return null;
        }
    }

    private static class DeleteAllNoteAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;

        // constructor is required as class is private
        private DeleteAllNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }
}
