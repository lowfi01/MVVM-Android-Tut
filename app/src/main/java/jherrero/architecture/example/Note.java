package jherrero.architecture.example;


/*
*  Entity - part of the ROOM implementation
*  Note represents a single Note object & the database
*  table which will contain all notes
*
*  @Entity - ROOM Annotation, compile time it will an SQL table
*  for us automatically for this object.
*
*  Annotations are how ROOM knows to write boilerplate code for us.
*
*  As ID is auto generated and we do not pass it through the constructor
*  we need to assign a setter
* */

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true) // assign primary key
    private int id;

    // member variables become table columns
    private String title;
    private String description;
    @ColumnInfo(name = "priority_column") // changes name of column
    private int priority;

    public Note(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    // Ignore fields with @Ignore

    // Assign id with a setter as we do not define it in the constructor, to prevent errors
    public void setId(int id) {
        this.id = id;
    }

    // Create getter methods, persist to the database
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}
