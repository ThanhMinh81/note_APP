package com.example.notepad.Model;

public class Formatted {

    private String textStyle ;
    private Boolean check ;



    public Formatted(String textStyle, Boolean check) {
        this.textStyle = textStyle;
        this.check = check;


    }

    public String getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(String textStyle) {
        this.textStyle = textStyle;
    }


    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }
}
