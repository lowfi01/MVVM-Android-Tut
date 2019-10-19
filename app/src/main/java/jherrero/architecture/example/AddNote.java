package jherrero.architecture.example;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddNote extends AppCompatActivity {
    public static final String EXTRA_TITLE =
            "jherrero.architecture.example.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "jherrero.architecture.example.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY =
            "jherrero.architecture.example.EXTRA_PRIORITY";


    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);


        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerPriority = findViewById(R.id.number_picker_priority);

        // setup number picker
        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);

        // setup close icon to our action bar
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        // Set title to action bar for our add notes activity
        setTitle("Add Note");
    }


    // Logic for submitting form, should actually communicate with the database
    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int priority = numberPickerPriority.getValue();

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Title and Description cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // note - viewmodel should handle the logic here,
        // we should create a second smaller ViewModel for this interaction
        // but rather we cheat and just send everything to the main activity

        // use intent - but we shoudlnt' normlly

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);

        setResult(RESULT_OK, data);
        finish();

    }


    // setup save icon to confirm inputs
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Assign add_note_menu as the menu for this activity
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;  // true will show menu
    }


    // Add click handler for out menu item
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
