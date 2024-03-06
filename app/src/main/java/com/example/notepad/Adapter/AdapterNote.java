package com.example.notepad.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.Interface.IClickUpdate;
import com.example.notepad.Model.Note;
import com.example.notepad.R;

import java.util.ArrayList;

public class AdapterNote extends  RecyclerView.Adapter<AdapterNote.ViewHolder> {

    private ArrayList<Note> noteArrayList ;
    private Context context ;

    private IClickUpdate iClickUpdate ;

;


    public AdapterNote(ArrayList<Note> noteArrayList, Context context, IClickUpdate iClickUpdate) {
        this.noteArrayList = noteArrayList;
        this.context = context;
        this.iClickUpdate = iClickUpdate;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view ;


            view = inflater.inflate(R.layout.item_layout_note, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

          Note note = noteArrayList.get(position);
          holder.tvTitle.setText(note.getTitle());
          holder.tvContent.setText(note.getTimeEdit());

          holder.constraintLayout.setOnClickListener(v -> {

              iClickUpdate.click(note);

          });

    }

    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

          public TextView tvTitle , tvContent ;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvTime);
            constraintLayout = itemView.findViewById(R.id.itemLayout);

        }
    }
// dùng cùng adapter truyền biến boolean để check

}
