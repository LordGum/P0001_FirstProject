package com.example.p0001_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.badge.BadgeUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button addButton;

    private RecyclerViewAdapter adapter;
    private mainViewModel viewModel;

    private final String TAG = "where the problem?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        clickOnAddButton();

        adapter = new RecyclerViewAdapter();
        clickOnNote();
        recyclerView.setAdapter(adapter);

        viewModel = new mainViewModel(getApplication());
        viewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setNotes(notes);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Note note = adapter.getNotes().get(position);
                viewModel.remove(note);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        addButton = findViewById(R.id.MainAddButton);
    }

    private void clickOnAddButton() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });
    }

    private void clickOnNote() {
        // понадобиться для клика, чтобы просмотреть текст записки
        adapter.setOnNoteClickListener(new RecyclerViewAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(Note note) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                int  id = note.getId();
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }
}