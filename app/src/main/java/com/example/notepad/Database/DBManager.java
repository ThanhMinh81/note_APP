package com.example.notepad.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.notepad.Model.Note;

import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private DatabaseHandler dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHandler(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }


    public void close() {
        dbHelper.close();
    }


    public ArrayList<Note> search(String keyWord){

        ArrayList<Note> noteList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT " + dbHelper.TABLE_NOTE +"." +dbHelper.KEY_ID +","
                + dbHelper.TABLE_NOTE +"." + dbHelper.KEY_TITLE +","+
                dbHelper.TABLE_NOTE + "." + dbHelper.KEY_CONTENT + " FROM " + dbHelper.TABLE_NOTE +
                " WHERE " + dbHelper.TABLE_NOTE + "." + dbHelper.KEY_TITLE + " LIKE ? OR " +
                dbHelper.TABLE_NOTE + "." + dbHelper.KEY_CONTENT + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + keyWord + "%", "%" + keyWord + "%"};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setIdNote(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setTimeEdit(cursor.getString(3));
                noteList.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return noteList;

    }




    public void addNote(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dbHelper.KEY_TITLE, note.getTitle());
        values.put(dbHelper.KEY_CONTENT, note.getContent());
        values.put(dbHelper.KEY_TIME_EDIT, note.getTimeEdit());
        db.insert(dbHelper.TABLE_NOTE, null, values);
        db.close();
    }


    public Note getNote(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(dbHelper.TABLE_NOTE, new String[] { dbHelper.KEY_ID,
                        dbHelper.KEY_TITLE, dbHelper.KEY_CONTENT, dbHelper.KEY_TIME_EDIT }, dbHelper.KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Note note = new Note(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));
        cursor.close();
        return note;
    }

    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> noteList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + dbHelper.TABLE_NOTE;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setIdNote(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setTimeEdit(cursor.getString(3));
                noteList.add(note);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return noteList;
    }


    public int updateNote(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dbHelper.KEY_TITLE, note.getTitle());
        values.put(dbHelper.KEY_CONTENT, note.getContent());
        values.put(dbHelper.KEY_TIME_EDIT, note.getTimeEdit());
        return db.update(dbHelper.TABLE_NOTE, values, dbHelper.KEY_ID + " = ?",
                new String[] { String.valueOf(note.getIdNote()) });
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(dbHelper.TABLE_NOTE, dbHelper.KEY_ID + " = ?",
                new String[] { String.valueOf(note.getIdNote()) });
        db.close();
    }

    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + dbHelper.TABLE_NOTE;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }



}
