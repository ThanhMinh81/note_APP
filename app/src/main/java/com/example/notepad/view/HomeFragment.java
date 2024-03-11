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
import android.view.LayoutInflater;
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
            Intent intent = new Intent(getActivity(), UpdateActivity.class);
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

        TextView textView = view.findViewById(R.id.tvTest);


        final Integer[] fontNames = {20,  25 ,30, 35 , 40, 45 , 50 };

        seekBar = view.findViewById(R.id.seebarFont);


        seekBar.setMax(fontNames.length - 1);
        textView.setText(fontNames[0].toString());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Hiển thị tên font chữ tương ứng với giá trị của SeekBar
                textView.setText(fontNames[progress].toString());
//                textView.setTypeface(Typeface.create(fontNames[progress], Typeface.NORMAL));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,fontNames[progress] );


                // Áp dụng font chữ tương ứng vào TextView hoặc EditText, nếu cần
                // Ví dụ: fontTextView.setTypeface(Typeface.create(fontNames[progress], Typeface.NORMAL));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Không cần xử lý ở đây
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Không cần xử lý ở đây
            }
        });



        dataViewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);

        dataViewModel.getListMutableLiveData().observe(getViewLifecycleOwner(), noteArrayList -> {
            notes.clear();
            notes.addAll(noteArrayList);
            apdaterNote.notifyDataSetChanged();
        });


        dataViewModel.getThemeString().observe(getViewLifecycleOwner(), s -> {

            if(s.equals("Solarized"))
            {
                apdaterNote.setLayoutNote("Solarized",String.valueOf(getResources().getColor(R.color.themeSolari)));
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


        dataViewModel.getThemeString().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                switch (s)
                {
                    case "Solarized":
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
                dataViewModel.updateNote(note);
                dataViewModel.setMutableLiveDataNote(note);
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
