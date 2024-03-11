package com.example.notepad.view;

import static androidx.appcompat.widget.SearchView.*;

import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.notepad.Database.DBManager;
import com.example.notepad.MainActivity;
import com.example.notepad.Model.Note;
import com.example.notepad.Model.Selected;
import com.example.notepad.ViewModel.UpdateViewModel;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.ForwardingListener;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.example.notepad.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formattable;

import yuku.ambilwarna.AmbilWarnaDialog;

public class UpdateActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText edTitle, edContent;

    DBManager databaseHandler;

    Note noteData;

    int DefaultColor;

    LinearLayout linearLayout;

    CheckBox cbBold, cbItalic, cbUnderline, cbColorText;

    UpdateViewModel updateViewModel;

    int colorText ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update);

        Toolbar toolbar = this.<Toolbar>findViewById(R.id.toolbarUpdate); //Ignore red line errors
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);

        updateViewModel = new UpdateViewModel();

        edTitle = findViewById(R.id.edTitleNote);
        edContent = findViewById(R.id.edContentNote);
        linearLayout = this.<LinearLayout>findViewById(R.id.layoutUpdateNote);

        checkBoxSelect();

        databaseHandler = new DBManager(this);
        databaseHandler.open();

        noteData = getIntent().getExtras().getParcelable("note");

        colorText = Color.parseColor(noteData.getStyleTextColor());

        Log.d("Fsfas",noteData.getStyleTextColor() + "");

        edTitle.setText(noteData.getTitle());

        edContent.setText(noteData.getContent());

        // check mau cho bg color
        if(noteData.getBgColors().trim().length() > 0)
        {
            DefaultColor = Color.parseColor(noteData.getBgColors());
        }else {
            DefaultColor = Color.parseColor(String.valueOf(R.color.backgroundItem));
        }


        Drawable myIcon = AppCompatResources.getDrawable(this, R.drawable.layout_update_note);

        String format1 = edContent.getText().toString();
        SpannableString spannableString1 = new SpannableString(format1);

        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(colorText);

        spannableString1.setSpan(colorSpan1, 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        edContent.setText(spannableString1);

        Log.d("fsdasfd",noteData.getStyleTextColor());

        if(!noteData.getStyleTextColor().equals("#1B1A18"))
        {
            cbColorText.setChecked(true);
        }


        if(noteData.getStyleBold().equals("true"))
        {
             cbBold.setChecked(true);

            spannableString1.setSpan(new StyleSpan(Integer.parseInt("1")), 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            edContent.setText(spannableString1);

        }
        if(noteData.getStyleItalic().equals("true"))
        {

            cbItalic.setChecked(true);
            spannableString1.setSpan(new StyleSpan(Integer.parseInt("2")), 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            edContent.setText(spannableString1);

        }
        if(noteData.getStyleUnderline().equals("true"))
        {
            cbUnderline.setChecked(true);
            spannableString1.setSpan(new UnderlineSpan(), 0, format1.length(), 0);
            edContent.setText(spannableString1);

        }


//
        // xet background
        if (noteData.getBgColors().contains("#")) {
            ColorFilter filter = new LightingColorFilter(Color.parseColor(noteData.getBgColors()), Color.parseColor(noteData.getBgColors()));
            myIcon.setColorFilter(filter);
        }


        linearLayout.setBackground(myIcon);



        // 1bold
        // 2 italic

        updateViewModel.getMutableLiveData().observe(this, selected -> {

            if (selected.isCheck()) {

                if (selected.getIndex().contains("#"))
                {
                    // set Color
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(selected.getIndex()));
                    spannableString1.setSpan(colorSpan, 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    edContent.setText(spannableString1);
                }
                 else  if(selected.getIndex().equals("Under"))
                {
                    // set Underline
                    spannableString1.setSpan(new UnderlineSpan(), 0, format1.length(), 0);
                    edContent.setText(spannableString1);
                }
                 else
                {
                    spannableString1.setSpan(new StyleSpan(Integer.parseInt(selected.getIndex())), 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    edContent.setText(spannableString1);
                }
            } else {

                StyleSpan[] styleSpans = spannableString1.getSpans(0, format1.length(), StyleSpan.class);

                if(selected.getIndex().equals("Black"))
                {
                    Log.d("kiemtraduleu","sasfdf");
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(getColor(R.color.black));
                    // xet lai mau den cho text
                    colorText = getColor(R.color.black);
                    spannableString1.setSpan(colorSpan, 0, spannableString1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                for (StyleSpan styleSpan : styleSpans) {
                    if (selected.getIndex().equals("Under"))
                    {
                        spannableString1.removeSpan((new UnderlineSpan()));
                    }
                    else if(!selected.getIndex().equals("Black"))
                    {
                        if (styleSpan.getStyle() == Integer.parseInt(selected.getIndex()))
                        {
                            spannableString1.removeSpan(styleSpan);
                        }
                    }
                }

                edContent.setText(spannableString1);

            }
        });


    }

    private void checkBoxSelect() {

        cbBold = findViewById(R.id.cbBold);
        cbItalic = findViewById(R.id.cbItalic);
        cbUnderline = findViewById(R.id.cbUnderline);
        cbColorText  = findViewById(R.id.cbColorText);



        cbBold.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox) view;

                if (checkBox.isChecked()) {
                    Log.d("fsdfasfasdfasdfas",checkBox.isChecked() + "");
                    Selected selected = new Selected("1", true);
                    updateViewModel.setMutableLiveData(selected);
                } else {
//                    Log.d("fsafas", isChecked + " ");
                    Selected selected = new Selected("1", false);
                    updateViewModel.setMutableLiveData(selected);
                }

            }
        });

        cbItalic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox) view;

                if (checkBox.isChecked()) {
                    Log.d("fsdfasfasdfasdfas",checkBox.isChecked() + "");
                    Selected selected = new Selected("2", true);
                    updateViewModel.setMutableLiveData(selected);
                } else {
//                    Log.d("fsafas", isChecked + " ");
                    Selected selected = new Selected("2", false);
                    updateViewModel.setMutableLiveData(selected);
                }

            }
        });

        cbUnderline.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox) view;

                if (checkBox.isChecked()) {
                    Log.d("fsdfasfasdfasdfas",checkBox.isChecked() + "");
                    Selected selected = new Selected("Under", true);
                    updateViewModel.setMutableLiveData(selected);
                } else {
//                    Log.d("fsafas", isChecked + " ");
                    Selected selected = new Selected("Under", false);
                    updateViewModel.setMutableLiveData(selected);
                }

            }
        });

        cbColorText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox) view;

                if (checkBox.isChecked()) {
                    AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(UpdateActivity.this, colorText, true, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                   @Override
                   public void onOk(AmbilWarnaDialog ambilWarnaDialog, int color) {
                       colorText = color;
                       String hexColor = String.format("#%06X", (0xFFFFFF & colorText));
//                       Log.d("sdfsaodf", String.valueOf(colorText));
                       colorText = Color.parseColor(hexColor);
                       Selected selected = new Selected(String.valueOf(hexColor),true);
                       updateViewModel.setMutableLiveData(selected);
                   }

                   @Override
                   public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {}
               });
               ambilWarnaDialog.show();
                } else {

                    Selected selected = new Selected("Black",false);
                    updateViewModel.setMutableLiveData(selected);

                }
            }
        });

    }

    private void eventClickUpdate() {
        if (edTitle.getText().toString().length() > 0 && edContent.getText().toString().length() > 0) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String time = dateFormat.format(date);

            Note note = new Note();
            note.setIdNote(noteData.getIdNote());
            note.setContent(edContent.getText().toString());
            note.setTitle(edTitle.getText().toString());
            note.setTimeEdit(time.toString());

            // set mau cho text color
            String hexColor = String.format("#%06X", (0xFFFFFF &colorText ));
            note.setStyleTextColor(String.valueOf(hexColor));

            String bgColor = String.format("#%06X", (0xFFFFFF &DefaultColor ));
            note.setBgColors(String.valueOf(bgColor));

            Log.d("ooeee",note.getStyleTextColor());

            if(cbBold.isChecked())
            {
                Log.d("bolddagchecj","sdfda");
                note.setStyleBold("true");
            }else {
                note.setStyleBold("false");
            }

            if(cbUnderline.isChecked())
            {
                note.setStyleUnderline("true");
            }else {
                note.setStyleUnderline("false");

            }

            if(cbItalic.isChecked())
            {
                Log.d("itaalcca","sdfda");

                note.setStyleItalic("true");
            }else {
                note.setStyleItalic("false");

            }
            note.setIdNoteStyle(noteData.getIdNoteStyle());


            Intent intent = new Intent();

            Log.d("corrrrr",note.getStyleTextColor());

            intent.putExtra("note", note);

//            databaseHandler.updateNote(note);

            setResult(RESULT_OK, intent);

            Toast.makeText(this, "Cap nhat thanh cong !", Toast.LENGTH_SHORT).show();

            finish();


        } else {
            Toast.makeText(this, "Vui lòng nhập dữ liệu ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.menu_add, menu);

        getMenuInflater().inflate(R.menu.menu_save, menu);

        getMenuInflater().inflate(R.menu.searchview, menu);


        MenuItem searchItem = menu.findItem(R.id.item_search);

        SearchManager searchManager = (SearchManager) UpdateActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(UpdateActivity.this.getComponentName()));
        }

        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                highlightText(newText);

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void highlightText(String s) {
        SpannableString spannableString = new SpannableString(edContent.getText());
        BackgroundColorSpan[] backgroundColorSpan =
                spannableString.getSpans(0, spannableString.length(), BackgroundColorSpan.class);
        for (BackgroundColorSpan bgSpan : backgroundColorSpan) {
            spannableString.removeSpan(bgSpan);
        }
        int indexOfKeyWord = spannableString.toString().indexOf(s);
        while (indexOfKeyWord > 0) {
            spannableString.setSpan(new BackgroundColorSpan(Color.YELLOW), indexOfKeyWord,
                    indexOfKeyWord + s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            indexOfKeyWord = spannableString.toString().indexOf(s, indexOfKeyWord + s.length());
        }
        edContent.setText(spannableString);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        int id = item.getItemId();

        if (id == R.id.nav_saveUpdate) {
            eventClickUpdate();
        } else if (id == R.id.nav_colorize) {
            OpenColorPickerDialog(true);
        } else if (id == R.id.item_search) {
            Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);

    }


    private void OpenColorPickerDialog(boolean AlphaSupport) {

        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, DefaultColor, AlphaSupport, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int color) {

                DefaultColor = color;

                // chuyen doi ma mau  -15741650 thanh ma mau  0xFFFFFF

                String hexColor = String.format("#%06X", (0xFFFFFF & DefaultColor));
                Log.d("kemtraddulie",hexColor);

                DefaultColor = Color.parseColor(String.valueOf(hexColor)) ;

                linearLayout.setBackgroundColor(Color.parseColor(String.valueOf(hexColor)));

            }

            @Override
            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {

//                Toast.makeText(MainActivity.this, "Color Picker Closed", Toast.LENGTH_SHORT).show();
            }
        });
        ambilWarnaDialog.show();

    }


}