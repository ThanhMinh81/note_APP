package com.example.notepad.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UpdateSpanViewModel extends ViewModel {

    private MutableLiveData<String> typeSetSpan = new MutableLiveData<>();

    public UpdateSpanViewModel() {
    }

    public MutableLiveData<String> getTypeSetSpan() {
        return typeSetSpan;
    }

    public void setTypeSetSpan(String typeSetSpan) {
        this.typeSetSpan.setValue(typeSetSpan);
    }



}
