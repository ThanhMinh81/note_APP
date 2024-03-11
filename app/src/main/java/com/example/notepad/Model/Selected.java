package com.example.notepad.Model;

public class Selected {

    private String index ;
    private boolean check ;

    public Selected(String index, boolean check) {
        this.index = index;
        this.check = check;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
