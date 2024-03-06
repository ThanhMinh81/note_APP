package com.example.notepad.ViewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.notepad.Database.DBManager;
import com.example.notepad.Database.DatabaseHandler;
import com.example.notepad.Model.Note;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataViewModel extends ViewModel {

    private  MutableLiveData<ArrayList<Note>> listMutableLiveData = new MutableLiveData<>();
    DBManager databaseHandler ;

    public DataViewModel(DBManager databaseHandler ) {
        this.databaseHandler = databaseHandler;
    }

    public void getData()
    {
        listMutableLiveData.setValue(databaseHandler.getAllNotes());
    }

    public MutableLiveData<ArrayList<Note>> getListMutableLiveData() {
        return listMutableLiveData;
    }

    public void setListMutableLiveData(ArrayList<Note> listMutableLiveData) {
        this.listMutableLiveData.setValue(listMutableLiveData);
    }


    public void clearAll(){
        ArrayList<Note> noteArrayList = new ArrayList<>();
        this.listMutableLiveData.setValue(noteArrayList);
    }

    public ArrayList<Note> getValueArr()
    {
        return this.listMutableLiveData.getValue();
    }


    public void updateNote(Note note)
    {

        ArrayList<Note> notes = new ArrayList<>();
        notes.addAll(listMutableLiveData.getValue());

        for (int i = 0 ; i < notes.size() ; i++ ) {
            if(notes.get(i).getIdNote() == note.getIdNote())
            {
                notes.set(i,note);
            }
        }

        ArrayList<Note> noteArrayList = new ArrayList<>();
        this.listMutableLiveData.setValue(noteArrayList);
        this.listMutableLiveData.setValue(notes);

        databaseHandler.updateNote(note);

    }



}
