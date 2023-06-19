package com.example.p0001_notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class NoteActivity extends AppCompatActivity {

    private TextView textView;
    private TextView textView2;

    private mainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        init();
    }

    private void init() {
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);

        viewModel = new mainViewModel(getApplication());
        viewModel.getNote(id).observe(this, new Observer<Note>() {
            @Override
            public void onChanged(Note note) {
                textView.setText(note.getName());
                textView2.setText(note.getText());
            }
        });
    }
}