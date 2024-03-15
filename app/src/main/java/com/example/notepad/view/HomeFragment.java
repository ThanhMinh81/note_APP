package com.example.notepad.view;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import yuku.ambilwarna.AmbilWarnaDialog;

public class HomeFragment extends Fragment {

    private View view;
    private RecyclerView rcvHome;
    private ArrayList<Note> notes;
    private AdapterNote apdaterNote;
    IData iData;
    IClickUpdate iClickUpdate;
    DataViewModel dataViewModel;

    ConstraintLayout constraintLayout ;



    SeekBar seekBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_home, container, false);


        iClickUpdate = note -> {
//            Intent intent = new Intent(getActivity(), UpdateActivity.class);
//            intent.putExtra("note", note);
//            startActivityForResult(intent, 10);


            Intent intent = new Intent(getActivity(), UpdateActivity.class);
            intent.putExtra("checkUpdate", true);
            intent.putExtra("note", note);
            startActivityForResult(intent, 10);




        };

        /// xetttt mauuuuu cho bg fragment
        constraintLayout = view.findViewById(R.id.fragmentHomeConstrain);
        constraintLayout.setBackgroundColor(getResources().getColor(R.color.backgroundItem));

        /////===

        notes = new ArrayList<>();
        apdaterNote = new AdapterNote(notes, getContext(), iClickUpdate);
        rcvHome = view.findViewById(R.id.rcvHome);
        rcvHome.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rcvHome.setAdapter(apdaterNote);
        apdaterNote.notifyDataSetChanged();

        dataViewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);

        dataViewModel.getListMutableLiveData().observe(getViewLifecycleOwner(), noteArrayList -> {
            notes.clear();
            notes.addAll(noteArrayList);
            apdaterNote.notifyDataSetChanged();
        });


        dataViewModel.getThemeString().observe(getViewLifecycleOwner(), s -> {
            if(s.equals("Solarized")) {
                apdaterNote.setLayoutNote("Solarized","#FCF6E0");
            }else if(s.equals("Default"))
            {
                apdaterNote.setLayoutNote("Default","#FCF6E0");
            }
        });


        dataViewModel.getOnSelectedSort().observe(getViewLifecycleOwner(), s -> {

            switch (s) {
                case "TitleAtoZ":
                    Collections.sort(notes, Comparator.comparing(Note::getTitle));
                    apdaterNote.notifyDataSetChanged();
                    break;
                case "TitleZtoA":
                    Collections.sort(notes, Comparator.comparing(Note::getTitle).reversed());
                    apdaterNote.notifyDataSetChanged();

                    break;
                case "title_newest":
                    Collections.sort(notes, Comparator.comparing(Note::getTimeEdit).reversed());
                    apdaterNote.notifyDataSetChanged();

                    break;
                case "title_oldest":
                    Collections.sort(notes, Comparator.comparing(Note::getTimeEdit));
                    apdaterNote.notifyDataSetChanged();
                    break;

                default:
                    return;
            }
        });

        rcvHome.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public void onLongPress(MotionEvent e) {
                    View childView = rcvHome.findChildViewUnder(e.getX(), e.getY());
                    if (childView != null) {
                        // Thay đổi background của item khi nhấn giữ lâu
                        childView.setBackgroundResource(R.drawable.border_selected);

                    }
                }
            });

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                gestureDetector.onTouchEvent(e);
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });



        dataViewModel.getThemeString().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                switch (s)
                {
                    case "Solarized":
                        Log.d("Fasfsa","thaydoi");
                         constraintLayout.setBackgroundColor(getResources().getColor(R.color.themeSolari));
                        break;
                }

            }
        });

        dataViewModel.getStringMutableLiveData().observe(getViewLifecycleOwner(), s -> apdaterNote.setFilter(notes, s));

        return view;

    }

    public void getData(List<Note> noteArrayList) {
        noteArrayList.addAll(noteArrayList);
        apdaterNote.notifyDataSetChanged();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 10) {

            if (data != null) {
                Note note = (Note) data.getParcelableExtra("note");
                Log.d("strikeHomeFragment",note.getBackgroundColorText());
                dataViewModel.updateNote(note);
                dataViewModel.setMutableLiveDataNote(note);
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
