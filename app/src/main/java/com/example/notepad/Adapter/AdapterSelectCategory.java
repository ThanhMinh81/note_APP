package com.example.notepad.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.Interface.IClickCategory;
import com.example.notepad.Model.Category;
import com.example.notepad.Model.Note;
import com.example.notepad.R;

import java.util.ArrayList;

public class AdapterSelectCategory extends RecyclerView.Adapter<AdapterSelectCategory.ViewHolder> {



    private ArrayList<Category> categoryArrayList ;

    IClickCategory iClickCategory ;


    public AdapterSelectCategory(ArrayList<Category> categoryArrayList, IClickCategory iClickCategory) {
        this.categoryArrayList = categoryArrayList;
        this.iClickCategory = iClickCategory;
    }

    @NonNull
    @Override
    public AdapterSelectCategory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;


        view = inflater.inflate(R.layout.item_select_category, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSelectCategory.ViewHolder holder, int position) {

        Category category = categoryArrayList.get(position);

        holder.textView.setText(category.getNameCategory());

        holder.checkBox.setOnClickListener(v -> {
          iClickCategory.click(category.getIdCategory());
        });

    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

         CheckBox checkBox ;
         TextView textView ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.cbName);
            textView = itemView.findViewById(R.id.tvNameCategory);

        }
    }



}
