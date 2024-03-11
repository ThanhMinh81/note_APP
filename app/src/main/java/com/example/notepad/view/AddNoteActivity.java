package com.example.notepad.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.notepad.Database.DBManager;
import com.example.notepad.Model.Note;
import com.example.notepad.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText edTitle, edContent;
    TextView tvSave;
    DBManager databaseHandler;
    ImageView back;

    Boolean checkUpdateNote = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        edTitle = findViewById(R.id.edTitleNote);
        edContent = findViewById(R.id.edContentNote);
        tvSave = this.<TextView>findViewById(R.id.saveNote);
        back = this.<ImageView>findViewById(R.id.backAddNote);

        databaseHandler = new DBManager(this);
        databaseHandler.open();

        checkUpdateNote = getIntent().getExtras().getBoolean("checkUpdate", true);


        if (!checkUpdateNote) {
            eventClickAdd();
        }


    }


    private void eventClickAdd() {
        back.setOnClickListener(v -> {
            finish();
        });

        tvSave.setOnClickListener(view -> {

            Intent resultIntent = new Intent();

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String time = dateFormat.format(date);

            Note note = new Note();

            note.setTitle(edTitle.getText().toString());
            note.setContent(edContent.getText().toString());
            note.setTimeEdit(time.toString());
//            note.setBgColors("#836E4C");
            note.setBgColors("#ffff99");
            note.setStyleTextColor("#1B1A18");
            note.setStyleUnderline("false");
            note.setStyleBold("false");
            note.setStyleItalic("false");
            databaseHandler.addNote(note);
            resultIntent.putExtra("note", note);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();

        });
    }
}
