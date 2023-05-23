package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Models.Notes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesTakerActivity extends AppCompatActivity {

    EditText editTextTitle, editTextNote;
    ImageView imgSave;
    Notes notes;
    boolean isOldNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_taker);

        imgSave = findViewById(R.id.imgSave);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextNote = findViewById(R.id.editTextNote);

        notes = new Notes();
        try {
            notes = (Notes) getIntent().getSerializableExtra("note");
            editTextTitle.setText(notes.getTitle());
            editTextNote.setText(notes.getNotes());
            isOldNote = true;
        } catch (Exception e){
            e.printStackTrace();
        }
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString();
                String noteText = editTextNote.getText().toString();

                if(noteText.isEmpty()){
                    Toast.makeText(NotesTakerActivity.this, "Please enter your note", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss");
                Date date = new Date();

                if(!isOldNote) {
                    notes = new Notes();
                }

                notes.setTitle(title);
                notes.setNotes(noteText);
                notes.setDate(format.format(date));

                Intent intent = new Intent();
                intent.putExtra("notes", notes);
                setResult(Activity.RESULT_OK, intent);

                finish();
            }
        });

    }
}