package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.myapplication.Adapter.Adapter;
import com.example.myapplication.DataBase.RoomDb;
import com.example.myapplication.Models.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    Adapter adapter;
    RoomDb dataBase;
    List<Notes> notesList = new ArrayList<>();
    SearchView search_bar;
    Notes selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search_bar = findViewById(R.id.search_bar);

        recyclerView = findViewById(R.id.recycler_home);
        floatingActionButton = findViewById(R.id.fab_add);

        dataBase = RoomDb.getInstance(this);

        notesList = dataBase.mainData().getAll();

        UpdateRecycler(notesList);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

    }

    private void filter(String newText) {
        List<Notes> filteredNotes = new ArrayList<>();
        for (Notes singleNote : notesList) {
            if (singleNote.getTitle().toLowerCase().contains(newText.toLowerCase()) || singleNote.getNotes().toLowerCase().contains(newText.toLowerCase())) {
                filteredNotes.add(singleNote);
            }
        }
        adapter.filterList(filteredNotes);
    }

    private void UpdateRecycler(List<Notes> notesList) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        adapter = new Adapter(MainActivity.this, notesList, notesClickListener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                Notes newNotes = (Notes) data.getSerializableExtra("notes");
                dataBase.mainData().insert(newNotes);
                notesList.clear();
                notesList.addAll(dataBase.mainData().getAll());
                adapter.notifyDataSetChanged();
            }
        }

        if (requestCode == 202) {
            if (resultCode == Activity.RESULT_OK) {
                Notes newNotes = (Notes) data.getSerializableExtra("notes");
                dataBase.mainData().update(newNotes.getID(), newNotes.getTitle(), newNotes.getNotes());
                notesList.clear();
                notesList.addAll(dataBase.mainData().getAll());
                adapter.notifyDataSetChanged();
            }
        }
    }


    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @Override
        public void onClick(Notes notes) {
            Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
            intent.putExtra("note", notes);
            startActivityForResult(intent, 202);
        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {

            selectedNote = new Notes();
            selectedNote = notes;
            showPopUp(cardView);

        }
    };

    private void showPopUp(CardView cardView) {

        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.pin) {
            if (selectedNote.isPinned()) {
                dataBase.mainData().pin(selectedNote.getID(), false);
                Toast.makeText(MainActivity.this, "Unpinned", Toast.LENGTH_SHORT).show();
            } else {
                dataBase.mainData().pin(selectedNote.getID(), true);
                Toast.makeText(MainActivity.this, "Pinned", Toast.LENGTH_SHORT).show();
            }
            notesList.clear();
            notesList.addAll(dataBase.mainData().getAll());
            adapter.notifyDataSetChanged();
            return true;
        } else if (item.getItemId() == R.id.delete) {
            dataBase.mainData().delete(selectedNote);
            notesList.remove(selectedNote);
            adapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
