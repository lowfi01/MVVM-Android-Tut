package jherrero.architecture.example;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


// General rule it's better to create one dao per entity.

@Dao
public interface NoteDao {

    // Crud operations are defined by @

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    // Build a query to define deleting all notes as there is no automated annotation
    @Query("DELETE FROM note_table")
    void deleteAllNotes();

    // Return all notes query
    @Query("SELECT * FROM note_table ORDER BY priority_column DESC")
    LiveData<List<Note>> getAllNotes();  // returned note is passed as Live data and is observable
}
