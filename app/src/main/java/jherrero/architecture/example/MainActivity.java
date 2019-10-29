package jherrero.architecture.example;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST = 1;


    private NoteViewModel noteViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*
        *  Setup floating add action button which should move activities to
        *  form and allow input, intent will carry form data.
        *
        *
        *  Note - We are using Activities and intents to pass data & this is not
        *  good practice, rather we should have a view model which will allow us to pass
        *  data dynamically and not require the use of intents
        *
        * */
        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            // User has clicked add note button
            @Override
            public void onClick(View view) {
                // do not use intents when setting up normally
                Intent intent = new Intent(MainActivity.this, AddNote.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST); // send intent & wait

            }
        });


        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true); // only set when you know the size will not change, performance improvement

        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        // Implement interaction with data and observe
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);  // pass fragment here instead of this, use getActivity(), watch video 7
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {  // pass fragment with this, as the observered method for the life cycle data
                // When data is added to LiveData observer will fire this method
                // used to update recycler view to new data


                // get live data and push to our adapter
                adapter.setNotes(notes);  // adapter becomes a final variable
            }
        });


        /*
        * Implement Swipe on delete
        *  - Use helper class to implement feature
        *
        * */

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0, // drag and drop
              ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT  // support both swipe directions
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;

                // drag and drop functionality
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int notePosition = viewHolder.getAdapterPosition(); // get position of swiped note from recycler view
                Note note = adapter.getNoteAt(notePosition); // fetch note from recycler view using position
                noteViewModel.delete(note); // delete note from database

                Toast.makeText(MainActivity.this, "Note deleted!", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView); // attach everything to our recycleer view



    }

    // Recieving intents sent cia startActivityForResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // form data sent successfully
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddNote.EXTRA_TITLE);
            String description = data.getStringExtra(AddNote.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddNote.EXTRA_PRIORITY, 1); // default value is to prevent null


            // create new note with intent data from form submission
            Note note = new Note(title,description, priority);


            // insert note to database
            noteViewModel.insert(note);

            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
        } else {
            // User has not saved the form, rather they have hit back button
            // Note - this will not normally happen if you are using view model correctly
            // rather we will need to find anther implementation


            // note - RESULT_CANCELED is the resultCode send back


            Toast.makeText(this, "Note Note Saved", Toast.LENGTH_SHORT).show();


        }


    }


    // Implement options menu for all delete notes action


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // generate custom menu we created
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return true; // allows for custom menu
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // setup on on delete all notes menu item
        switch(item.getItemId()){
            case R.id.delete_all_notes:
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}
