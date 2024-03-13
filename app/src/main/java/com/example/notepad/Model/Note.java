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

    String bgColors ;

    Boolean checkSelect = true ;

    int idNoteStyle ;
    String styleItalic ;
    String styleBold ;
    public String styleTextColor ;
    String styleUnderline ;
    String idCategory ;



    public Note() {
    }

    public Note(int idNote, String title, String content, String timeEdit, String bgColors, Boolean checkSelect, int idNoteStyle, String styleItalic, String styleBold, String styleTextColor, String styleUnderline, String idCategory) {
        this.idNote = idNote;
        this.title = title;
        this.content = content;
        this.timeEdit = timeEdit;
        this.bgColors = bgColors;
        this.checkSelect = checkSelect;
        this.idNoteStyle = idNoteStyle;
        this.styleItalic = styleItalic;
        this.styleBold = styleBold;
        this.styleTextColor = styleTextColor;
        this.styleUnderline = styleUnderline;
        this.idCategory = idCategory;
    }


    protected Note(Parcel in) {
        idNote = in.readInt();
        title = in.readString();
        content = in.readString();
        timeEdit = in.readString();
        bgColors = in.readString();
        byte tmpCheckSelect = in.readByte();
        checkSelect = tmpCheckSelect == 0 ? null : tmpCheckSelect == 1;
        idNoteStyle = in.readInt();
        styleItalic = in.readString();
        styleBold = in.readString();
        styleTextColor = in.readString();
        styleUnderline = in.readString();
        idCategory = in.readString();
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

    public String getStyleItalic() {
        return styleItalic;
    }

    public void setStyleItalic(String styleItalic) {
        this.styleItalic = styleItalic;
    }

    public String getStyleBold() {
        return styleBold;
    }

    public void setStyleBold(String styleBold) {
        this.styleBold = styleBold;
    }

    public String getStyleTextColor() {
        return styleTextColor;
    }

    public void setStyleTextColor(String styleTextColor) {
        this.styleTextColor = styleTextColor;
    }

    public String getStyleUnderline() {
        return styleUnderline;
    }

    public void setStyleUnderline(String styleUnderline) {
        this.styleUnderline = styleUnderline;
    }

    public int getIdNote() {
        return idNote;
    }

    public void setIdNote(int idNote) {
        this.idNote = idNote;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
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

    public String getBgColors() {
        return bgColors;
    }



    public void setBgColors(String bgColors) {
        this.bgColors = bgColors;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {

        parcel.writeInt(idNote);
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeString(timeEdit);
        parcel.writeString(bgColors);
        parcel.writeByte((byte) (checkSelect == null ? 0 : checkSelect ? 1 : 2));
        parcel.writeInt(idNoteStyle);
        parcel.writeString(styleItalic);
        parcel.writeString(styleBold);
        parcel.writeString(styleTextColor);
        parcel.writeString(styleUnderline);
        parcel.writeString(idCategory);
    }

    public int getIdNoteStyle() {
        return idNoteStyle;
    }

    public void setIdNoteStyle(int idNoteStyle) {
        this.idNoteStyle = idNoteStyle;
    }
}
