package com.example.p0001_notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNoteActivity extends AppCompatActivity {
    private EditText noteName;
    private String noteNameString;
    private EditText text;
    private String textString;
    private Button button;

    private mainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        init();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteNameString = noteName.getText().toString().trim();
                textString = text.getText().toString().trim();

                if(noteNameString .equals("")) { Toast.makeText(
                        getApplicationContext(),
                        "Add the name",
                        Toast.LENGTH_LONG
                ).show();
                } else if(textString.equals("")) {
                    Toast.makeText(
                            getApplicationContext(),
                            "write the note",
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    Note note = new Note(0, noteNameString, textString);
                    viewModel = new mainViewModel(getApplication());
                    viewModel.add(note);
                    Intent intent = new Intent(AddNoteActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void init() {
        noteName = findViewById(R.id.noteName);
        text = findViewById(R.id.EditeTextNote);
        button = findViewById(R.id.button);
    }

}