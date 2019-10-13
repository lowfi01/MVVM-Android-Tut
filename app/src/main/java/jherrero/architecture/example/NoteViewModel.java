package jherrero.architecture.example;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;


public class NoteViewModel extends AndroidViewModel {

    private Repository repository;
    private LiveData<List<Note>> allNotes;

    /*
     * Do not store the context of an activity or a view that references an activity here,
     * as the viewModel outlives the activity, and by holding a reference to an already destroyed
     * activity we have a memory leak.
     *
     * The NoteViewModel is passed a context as we need this to instantiate our database, this is
     * why we extend AndroidViewModel
     *
     *
     */

    public NoteViewModel(@NonNull Application application) {
        super(application);

        repository = new Repository(application);
        allNotes = repository.getAllNotes();
    }

    // Getter method for all notes
    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }


    //###
    // Wrapper method for our database methods
    //###


    public void insert(Note note) {
        repository.insert(note);
    }

    // Wrapper method for our database method
    public void delete(Note note) {
        repository.delete(note);
    }

    // Wrapper method for our database method
    public void update(Note note) {
        repository.update(note);
    }

    // Wrapper method for our database method
    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

}
