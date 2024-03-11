package com.example.notepad.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
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





    // add note

    public void addNote(Note note) {

        // them du lieu vao note
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dbHelper.KEY_TITLE, note.getTitle());
        values.put(dbHelper.KEY_CONTENT, note.getContent());
        values.put(dbHelper.KEY_TIME_EDIT, note.getTimeEdit());
        values.put(dbHelper.KEY_BACKGROUND,note.getBgColors());

        // tra ve -1 neu insert khong thanh cong
        // neu thành công trả về last id

       long result = db.insert(dbHelper.TABLE_NOTE, null, values);


       if (result==-1)
       {

       }
       else
       {
           // them thanh cong va add vao bang style

        try {

            SQLiteDatabase insertNoteStyle = dbHelper.getWritableDatabase();
            ContentValues noteStyle = new ContentValues();
            noteStyle.put(dbHelper.KEY_ID_NOTE, Integer.parseInt(String.valueOf(result)));
            noteStyle.put(dbHelper.KEY_ITALIC, note.getStyleItalic());
            noteStyle.put(dbHelper.KEY_BOLD, note.getStyleBold());
            noteStyle.put(dbHelper.KEY_UNDERLINE,note.getStyleUnderline());
            noteStyle.put(dbHelper.KEY_COLOR,note.getStyleTextColor());
            long result2 = insertNoteStyle.insert(dbHelper.TABLE_STYLE,null,noteStyle);
//            Log.d("kemtraduleuinsert",result2 + " " );

        }catch (Exception e)
        {


        }

       }



        // them du lieu node do vao style



        db.close();
    }

    public int updateNote(Note note)
    {

        int update = -1 ;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dbHelper.KEY_TITLE, note.getTitle());
        values.put(dbHelper.KEY_CONTENT, note.getContent());
        values.put(dbHelper.KEY_TIME_EDIT, note.getTimeEdit());
        values.put(dbHelper.KEY_BACKGROUND,note.getBgColors());

         int resultUpdateNote = db.update(dbHelper.TABLE_NOTE, values, dbHelper.KEY_ID + " = ?",
                new String[] { String.valueOf(note.getIdNote()) });

         if(resultUpdateNote != -1)
         {
             // update style

             SQLiteDatabase db2 = dbHelper.getWritableDatabase();
             ContentValues values1 = new ContentValues();

             // truyen du cac truong du leu
             // bo qua truong autoincrement tu dong tang
             values1.put(dbHelper.KEY_ID_NOTE,note.getIdNote());
             values1.put(dbHelper.KEY_COLOR, note.getStyleTextColor());
             values1.put(dbHelper.KEY_ITALIC, note.getStyleItalic());
             values1.put(dbHelper.KEY_BOLD, note.getStyleBold());
             values1.put(dbHelper.KEY_UNDERLINE,note.getStyleUnderline());

             update = db2.update(dbHelper.TABLE_STYLE, values1, dbHelper.KEY_ID_STYLE + " = ?",
                     new String[] { String.valueOf(note.getIdNoteStyle()) });


             Log.d("dfasfas",update + "" );





         }

         return update ;


    }



//    public Note getNote(int id) {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        Cursor cursor = db.query(dbHelper.TABLE_NOTE, new String[] { dbHelper.KEY_ID,
//                        dbHelper.KEY_TITLE, dbHelper.KEY_CONTENT, dbHelper.KEY_TIME_EDIT }, dbHelper.KEY_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//        Note note = new Note(Integer.parseInt(cursor.getString(0)),
//                cursor.getString(1), cursor.getString(2), cursor.getString(3));
//        cursor.close();
//        return note;
//    }

    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> noteList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + dbHelper.TABLE_NOTE + " INNER JOIN " + dbHelper.TABLE_STYLE + " ON "
                 + dbHelper.TABLE_NOTE +"." + dbHelper.KEY_ID + " = " + dbHelper.TABLE_STYLE +"." + dbHelper.KEY_ID_NOTE;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {

                Note note = new Note();
                note.setIdNote(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setTimeEdit(cursor.getString(3));
                note.setBgColors(cursor.getString(4));
                note.setIdNoteStyle(Integer.parseInt(cursor.getString(5)));
                note.setStyleTextColor(cursor.getString(7));
                note.setStyleItalic(cursor.getString(8));
                note.setStyleBold(cursor.getString(9));
                note.setStyleUnderline(cursor.getString(10));
                noteList.add(note);

                Log.d("fsfas", note.getTitle() + "  " + note.getIdNoteStyle());

            } while (cursor.moveToNext());
        }
        cursor.close();
        return noteList;
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


    public void deleteNode(ArrayList<String> strings)
    {

        String args = TextUtils.join(", ", strings);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(String.format("DELETE FROM note WHERE idNote IN (%s);", args));

    }










}
