package jherrero.architecture.example;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    /*
    * Member Variables
    * List is defined like this so as to allow us to modify the recycler view as needed
    * Listener is our onClickListener that allows user interaction with list items
    * */

    private List<Note> notes = new ArrayList<Note>();
    private OnItemClickListener listener; // implement member variable to allow for onClickListener

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged(); // note this is a bad option for notify
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        holder.onBind(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    // create function to return the array position of the current note
    public Note getNoteAt(int position) {
        return notes.get(position); // use this return value to delete from our database based from position
    }

    class NoteHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);

            // implement on click listener for the current item being selected
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    // conditionally check if on click listener is null and array is not -1
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClicked(notes.get(position));
                    }
                }
            });

        }

        public void onBind(Note note) {
            textViewTitle.setText(note.getTitle());
            textViewDescription.setText(note.getDescription());
            textViewPriority.setText(String.valueOf(note.getPriority())); // int value
        }
    }


    // Create interface to allow for onClickListener
    public interface OnItemClickListener {
        void onItemClicked(Note note); // you can also pass position
    }

    // implement onClickListener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
