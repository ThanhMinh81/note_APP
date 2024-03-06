package com.example.notepad.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.notepad.Database.DBManager;

public class MyViewModelFactory implements ViewModelProvider.Factory {

    private DBManager databaseHandler ;

    public MyViewModelFactory(DBManager databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DataViewModel.class)) {
            return (T) new DataViewModel(databaseHandler);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }




}
