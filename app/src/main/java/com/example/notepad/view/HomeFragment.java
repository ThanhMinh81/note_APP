package com.example.notepad.view;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.Adapter.AdapterNote;
import com.example.notepad.Interface.IClickUpdate;
import com.example.notepad.MainActivity;
import com.example.notepad.ViewModel.DataViewModel;
import com.example.notepad.Interface.IData;
import com.example.notepad.Model.Note;
import com.example.notepad.R;

import java.util.ArrayList;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

public class HomeFragment extends Fragment {

    private   View view ;
    private RecyclerView rcvHome ;
    private ArrayList<Note> notes ;
    private AdapterNote apdaterNote ;
    IData iData ;
    IClickUpdate iClickUpdate ;

    DataViewModel dataViewModel ;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view =inflater.inflate(R.layout.fragment_home, container, false);


        iClickUpdate = new IClickUpdate() {
            @Override
            public void click(Note note) {

//                Log.d("fasdfas",note.getIdNote() + " ");
                Intent intent = new Intent(getActivity(), UpdateActivity.class);

                   intent.putExtra("note",note);
                 startActivityForResult(intent,10);


            }
        };

        notes = new ArrayList<>();
        apdaterNote = new AdapterNote(notes,getContext(),iClickUpdate);
        rcvHome = view.findViewById(R.id.rcvHome);


        rcvHome.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rcvHome.setAdapter(apdaterNote);
        apdaterNote.notifyDataSetChanged();


        // tra ve node moi

        // update node trong viewmodel


         dataViewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);

        dataViewModel.getListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Note>>() {
             @Override
             public void onChanged(ArrayList<Note> noteArrayList) {

                 notes.clear();
                 notes.addAll(noteArrayList);
                 apdaterNote.notifyDataSetChanged();
             }
         });



        return view;

    }


    public void getData(List<Note> noteArrayList)
    {
         noteArrayList.addAll(noteArrayList);
         Log.d("checkID",noteArrayList.get(0).getIdNote() + " ");
         apdaterNote.notifyDataSetChanged();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 10) {

                if(data != null)
                {
                    Note note = data.getParcelableExtra("note");
                    dataViewModel.updateNote(note);
                    Log.d("updateDataaa",note.getIdNote() + " ");

                }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
