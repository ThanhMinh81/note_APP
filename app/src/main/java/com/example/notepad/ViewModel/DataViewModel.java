package com.example.notepad.ViewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.notepad.Database.DBManager;
import com.example.notepad.Database.DatabaseHandler;
import com.example.notepad.Model.Category;
import com.example.notepad.Model.Note;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class DataViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Note>> listMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> stringMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<String> themeString = new MutableLiveData<>();

    private MutableLiveData<Note> mutableLiveDataNote = new MutableLiveData<>();

    private MutableLiveData<String> onSelectedSort = new MutableLiveData<>();


    private MutableLiveData<ArrayList<Category>> listCategory = new MutableLiveData<>();

    private MutableLiveData<ArrayList<Note>> listNoteCategory = new MutableLiveData<>();


    DBManager databaseHandler;

    public DataViewModel(DBManager databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    public void getData() {
//        listMutableLiveData.setValue(databaseHandler.getAllNotes());
        listMutableLiveData.setValue(databaseHandler.getAllNoteCategory());
    }

    public MutableLiveData<ArrayList<Note>> getListMutableLiveData() {
        return listMutableLiveData;
    }


    public MutableLiveData<String> getStringMutableLiveData() {
        return stringMutableLiveData;
    }

    public void setStringMutableLiveData(String stringMutableLiveData) {
        this.stringMutableLiveData.setValue(stringMutableLiveData);
    }

    public void setListMutableLiveData(ArrayList<Note> listMutableLiveData) {
        this.listMutableLiveData.setValue(listMutableLiveData);
    }

    public MutableLiveData<ArrayList<Note>> getListNoteCategory() {
        return listNoteCategory;
    }

    public void setListNoteCategory(ArrayList<Note> listNoteCategory) {
        this.listNoteCategory.setValue(listNoteCategory);
    }

    public MutableLiveData<String> getOnSelectedSort() {
        return onSelectedSort;
    }

    public void setOnSelectedSort(String onSelectedSort) {
        this.onSelectedSort.setValue(onSelectedSort);
    }

    public MutableLiveData<String> getThemeString() {
        return themeString;
    }

    public void setThemeString(String themeString) {
        this.themeString.setValue(themeString);
    }

    public MutableLiveData<Note> getMutableLiveDataNote() {
        return mutableLiveDataNote;
    }

    public void setMutableLiveDataNote(Note note) {
        this.mutableLiveDataNote.setValue(note);
    }

    public MutableLiveData<ArrayList<Category>> getListCategory() {
        return listCategory;
    }

    public void setListCategory(ArrayList<Category> listCategory) {
        this.listCategory.setValue(listCategory);
    }

    public void clearAll() {
        ArrayList<Note> noteArrayList = new ArrayList<>();
        this.listMutableLiveData.setValue(noteArrayList);
    }

    public ArrayList<Note> getValueArr() {
        return this.listMutableLiveData.getValue();
    }




    public void updateNote(Note note) {

        ArrayList<Note> notes = new ArrayList<>();
        notes.addAll(listMutableLiveData.getValue());

        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getIdNote() == note.getIdNote()) {
                notes.set(i, note);
            }
        }

        ArrayList<Note> noteArrayList = new ArrayList<>();
        this.listMutableLiveData.setValue(noteArrayList);
        this.listMutableLiveData.setValue(notes);

        databaseHandler.updateNote(note);

    }


    public void getAllListCategory(){
        this.listCategory.setValue(this.databaseHandler.getAllCategory());
    }

    public  void addCategory(Category category)
    {
        databaseHandler.addCategory(category);
        getAllListCategory();
    }


    public void removeCategory(Category category)
    {
        databaseHandler.deleteCategory(category);
        getAllListCategory();
    }

    public void updateCategory(Category category)
    {
        databaseHandler.updateCategory(category);
        this.getAllListCategory();
    }


    public void updateCategoryNote(Note note,String id)
    {
        databaseHandler.updateCategoryNote(note,id);
    }

    public  void getNoteIDCategory(String id)
    {
        ArrayList<Note> notes =  databaseHandler.getListNote(id);
        this.listNoteCategory.setValue(notes);
    }


    public void deleteNoteCategory(Note note,String idCategory)
    {
        databaseHandler.deleteCategoryNote(note,idCategory);
        getNoteIDCategory(idCategory);
    }







}
