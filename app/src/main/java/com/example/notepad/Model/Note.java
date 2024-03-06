package com.example.notepad.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Note implements Parcelable {


    int idNote ;
    String title ;
    String content ;
    String timeEdit ;

    Boolean checkSelect = true ;



    public Note() {
    }

    public Note(int idNote, String title, String content, String timeEdit) {
        this.idNote = idNote;
        this.title = title;
        this.content = content;
        this.timeEdit = timeEdit;
    }

    protected Note(Parcel in) {
        idNote = in.readInt();
        title = in.readString();
        content = in.readString();
        timeEdit = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public int getIdNote() {
        return idNote;
    }

    public void setIdNote(int idNote) {
        this.idNote = idNote;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeEdit() {
        return timeEdit;
    }

    public void setTimeEdit(String timeEdit) {
        this.timeEdit = timeEdit;
    }

    public Boolean getCheckSelect() {
        return checkSelect;
    }

    public void setCheckSelect(Boolean checkSelect) {
        this.checkSelect = checkSelect;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(idNote);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(timeEdit);
    }
}
