package com.example.notepad.view;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.Adapter.AdapterNote;
import com.example.notepad.Adapter.AdapterSelect;
import com.example.notepad.Database.DBManager;
import com.example.notepad.Database.DatabaseHandler;
import com.example.notepad.Interface.IClickSelect;
import com.example.notepad.Model.Note;
import com.example.notepad.R;
import com.example.notepad.ViewModel.DataViewModel;

import java.util.ArrayList;
import java.util.function.Predicate;

public class SelectAllActivity extends AppCompatActivity {

      RecyclerView rcvHome ;
      AdapterSelect adapterSelect;

      ArrayList<Note> noteArrayList ;
      IClickSelect iClickSelect ;
      DBManager databaseHandler ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_all);

        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors

        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);

        setSupportActionBar(toolbar);

        noteArrayList=new ArrayList<>();
        noteArrayList =(ArrayList) getIntent().getParcelableArrayListExtra("listData");

        iClickSelect = new IClickSelect() {
            @Override
            public void select(Note note) {

                int indexObj =  noteArrayList.indexOf(note);

              if(note.getCheckSelect())
              {

                  note.setCheckSelect(false);
                  noteArrayList.set(indexObj,note);
                  adapterSelect.notifyDataSetChanged();


              }else {

                  note.setCheckSelect(true);
                  noteArrayList.set(indexObj,note);
                  adapterSelect.notifyDataSetChanged();


              }
            }
        };




        adapterSelect = new AdapterSelect(noteArrayList,this,iClickSelect);
        rcvHome = findViewById(R.id.rcvSelectAll);


        rcvHome.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcvHome.setAdapter(adapterSelect);
        adapterSelect.notifyDataSetChanged();


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.nav_deletenote)
        {
            databaseHandler = new DBManager(this);
            databaseHandler.open();
            ArrayList<String> strings = new ArrayList<>();

            for (Note note: noteArrayList)
            {
                 if(note.getCheckSelect())
                 {
                     strings.add(String.valueOf(note.getIdNote()));
                 }
            }

            //delete note duoc pick

            databaseHandler.deleteNode(strings);

        }else if(id == R.id.nav_export_select)
        {
            Toast.makeText(this, "Export notes to text files", Toast.LENGTH_SHORT).show();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_delete,menu);
        getMenuInflater().inflate(R.menu.menu_option_select,menu);


        return super.onCreateOptionsMenu(menu);
    }


}