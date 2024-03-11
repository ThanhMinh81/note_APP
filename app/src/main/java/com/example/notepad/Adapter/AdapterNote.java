package com.example.notepad.Adapter;

import static com.example.notepad.R.*;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.Interface.IClickUpdate;
import com.example.notepad.Model.Note;
import com.example.notepad.R;

import java.util.ArrayList;

public class AdapterNote extends  RecyclerView.Adapter<AdapterNote.ViewHolder> {

    private ArrayList<Note> noteArrayList ;

    private ArrayList<Note> searchArrayList ;
    private Context context ;

    private IClickUpdate iClickUpdate ;

    private String themeNote  = "default";
    private  String colorTheme ="#ffff99";

    private String searchText = new String() ;

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



          if(searchText.length() > 0)
          {

              Note note = noteArrayList.get(position);

              holder.tvContent.setVisibility(View.VISIBLE);
            int index = note.getTitle().toString().indexOf(searchText);
            Log.d("searchjhh",note.getTitle());
            int index2 = note.getContent().indexOf(searchText);

            holder.tvTitle.setText(Html.fromHtml(note.getTitle()));
            holder.tvContent.setText(Html.fromHtml(note.getContent()));
              // số với chữ thì nó chỉ nhận số thôi


            while(index>0)
            {

                SpannableStringBuilder sb = new SpannableStringBuilder(String.valueOf(note.getTitle()));

                ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(191, 255, 0)); //specify color here
                sb.setSpan(fcs, index, index+searchText.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                index = note.getTitle().indexOf(searchText,index+1);
                holder.tvTitle.setText(sb);
            }

            if(index2>0)
            {
                SpannableStringBuilder sb = new SpannableStringBuilder(note.getContent());
                ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(191, 255, 0)); //specify color here
                sb.setSpan(fcs, index2, index2+searchText.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                index2 = note.getContent().indexOf(searchText,index2+1);
                holder.tvContent.setText(sb);
            }

              holder.constraintLayout.setOnClickListener(v -> {
                  iClickUpdate.click(note);
              });

          }
          else
          {
              Note note = noteArrayList.get(position);
              holder.tvTitle.setText(note.getTitle());
              holder.tvTime.setText(note.getTimeEdit());
              holder.tvContent.setVisibility(View.GONE);
              holder.constraintLayout.setOnClickListener(v -> {
                  iClickUpdate.click(note);
              });



              Drawable myIcon = AppCompatResources.getDrawable(context, drawable.border_radius_corners);

//              if(note.getBgColors().contains("#"))
//              {
//                  ColorFilter filter = new LightingColorFilter(Color.parseColor(note.getBgColors()),Color.parseColor(note.getBgColors()));
//                  myIcon.setColorFilter(filter);

                  // Chuyển đổi Drawable thành GradientDrawable
                  GradientDrawable gradientDrawable = (GradientDrawable) myIcon;

            if(themeNote.equals("default"))
            {
                // Thiết lập màu sắc cho viền
                gradientDrawable.setStroke(5, Color.parseColor("#836E4C"));
//                Log.d("colorrr",note.getBgColors());
                gradientDrawable.setColor(Color.parseColor(note.getBgColors()));
//              }
//              else if(note.getBgColors().contains("-"))
//              {
//                  ColorFilter filter = new LightingColorFilter(Integer.parseInt(note.getBgColors()),Integer.parseInt(note.getBgColors()));

                // Chuyển đổi Drawable thành GradientDrawable
//                  GradientDrawable gradientDrawable = (GradientDrawable) myIcon;

                // Thiết lập màu sắc cho viền
//                  gradientDrawable.setStroke(10, Color.parseColor("#f9ca24"));
//                  gradientDrawable.setColor(Color.parseColor(note.getBgColors()));


//                  myIcon.setColorFilter(filter);
                holder.constraintLayout.setBackground(myIcon);
            }else {
//                gradientDrawable.setStroke(5, Color.parseColor(this.themeNote));
//                gradientDrawable.setColor(Color.parseColor(note.getBgColors()));
//                holder.constraintLayout.setBackground(myIcon);

                if(note.getBgColors().equals("#ffff99"))
                {
                    gradientDrawable.setStroke(5, Color.parseColor(colorTheme));
//                Log.d("colorrr",note.getBgColors());
                    gradientDrawable.setColor(Color.parseColor(colorTheme));
                    holder.constraintLayout.setBackground(myIcon);
                }else {
                    gradientDrawable.setStroke(5, Color.parseColor("#836E4C"));
//                Log.d("colorrr",note.getBgColors());
                    gradientDrawable.setColor(Color.parseColor(note.getBgColors()));
                    holder.constraintLayout.setBackground(myIcon);
                }


            }

//              }

          }

    }

    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

          public TextView tvTitle , tvTime , tvContent ;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

          if(searchText.length() > 0)
          {
              tvTitle = itemView.findViewById(R.id.tvTitle);
              tvTime = itemView.findViewById(R.id.tvTime);
              tvContent = itemView.findViewById(R.id.tvContent);
              constraintLayout = itemView.findViewById(R.id.itemLayout);
          }else {
              tvTitle = itemView.findViewById(R.id.tvTitle);
              tvTime = itemView.findViewById(R.id.tvTime);
              tvContent = itemView.findViewById(R.id.tvContent);
              constraintLayout = itemView.findViewById(R.id.itemLayout);
          }
        }
    }

    public void setFilter(ArrayList<Note> myNotes,String searchText) {
        searchArrayList = new ArrayList<>();
        searchArrayList.addAll(myNotes);
        this.searchText = searchText;
        notifyDataSetChanged();
    }
    public void setLayoutNote(String textTheme , String bgColor)
    {
        this.themeNote = textTheme;
        this.colorTheme = bgColor ;
        notifyDataSetChanged();
    }



}
