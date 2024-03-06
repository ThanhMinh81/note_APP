package com.example.notepad.view;

import android.content.Intent;
import android.os.Bundle;

import com.example.notepad.Database.DBManager;
import com.example.notepad.Model.Note;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.notepad.databinding.ActivityUpdateBinding;

import com.example.notepad.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import yuku.ambilwarna.AmbilWarnaDialog;

public class UpdateActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText edTitle, edContent;

    DBManager databaseHandler;

    Note noteData;

    int DefaultColor;

    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update);

        Toolbar toolbar = this.<Toolbar>findViewById(R.id.toolbarUpdate); //Ignore red line errors
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);

        DefaultColor = ContextCompat.getColor(this, R.color.appbar);

        edTitle = findViewById(R.id.edTitleNote);
        edContent = findViewById(R.id.edContentNote);
        linearLayout = this.<LinearLayout>findViewById(R.id.layoutUpdateNote);

//        tvSave = this.<TextView>findViewById(R.id.saveNote);
//        back = this.<ImageView>findViewById(R.id.backAddNote);

        databaseHandler = new DBManager(this);
        databaseHandler.open();

        DefaultColor = ContextCompat.getColor(this, R.color.appbar);


        noteData = getIntent().getExtras().getParcelable("note");


        edTitle.setText(noteData.getTitle());
        edContent.setText(noteData.getContent());


//        back.setOnClickListener(v -> {
//          onBackPressed();
//        });
//
//        tvSave.setOnClickListener(v -> {
//            eventClickUpdate();
//        });

    }

    private void eventClickUpdate() {
        if (edTitle.getText().toString().length() > 0 && edContent.getText().toString().length() > 0) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String time = dateFormat.format(date);

            Note note = new Note();
            note.setIdNote(noteData.getIdNote());
            note.setContent(edContent.getText().toString());
            note.setTitle(edTitle.getText().toString());
            note.setTimeEdit(time.toString());

            Intent intent = new Intent();

            intent.putExtra("note", note);


            setResult(RESULT_OK, intent);
            Toast.makeText(this, "Cap nhat thanh cong !", Toast.LENGTH_SHORT).show();

            finish();


        } else {
            Toast.makeText(this, "Vui lòng nhập dữ liệu ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.menu_add, menu);

        getMenuInflater().inflate(R.menu.menu_save, menu);

        getMenuInflater().inflate(R.menu.menu_undo, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        int id = item.getItemId();

        if (id == R.id.nav_saveUpdate) {
            eventClickUpdate();
        } else if (id == R.id.nav_colorize) {
            OpenColorPickerDialog(false);
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void OpenColorPickerDialog(boolean AlphaSupport) {

        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, DefaultColor, AlphaSupport, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int color) {

                DefaultColor = color;

                linearLayout.setBackgroundColor(color);

            }

            @Override
            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {

//                Toast.makeText(MainActivity.this, "Color Picker Closed", Toast.LENGTH_SHORT).show();
            }
        });
        ambilWarnaDialog.show();

    }
}