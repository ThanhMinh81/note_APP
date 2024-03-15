package com.example.notepad.view;

import static android.app.PendingIntent.getActivity;
import static androidx.appcompat.widget.SearchView.*;
import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.notepad.Adapter.AdapterSelect;
import com.example.notepad.Adapter.AdapterSelectCategory;
import com.example.notepad.Database.DBManager;
import com.example.notepad.Interface.IClickCategory;
import com.example.notepad.MainActivity;
import com.example.notepad.Model.Category;
import com.example.notepad.Model.Note;
import com.example.notepad.Model.Selected;
import com.example.notepad.ViewModel.DataViewModel;
import com.example.notepad.ViewModel.MyViewModelFactory;
import com.example.notepad.ViewModel.UpdateViewModel;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
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
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.ForwardingListener;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


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

    // cai nay color cho background
    int DefaultColor;

    // cai nay la mau cua background color
    int bgColorText  =   Color.parseColor("#FFFFFF") ;

    LinearLayout linearLayout;

    CheckBox cbBold, cbItalic, cbUnderline, cbColorText, cbBgColor, cbStrike;

    UpdateViewModel updateViewModel;

    DataViewModel dataViewModel;

    // mau cua hinh anhh ne
    int colorText;

    String themeStyle = "Default";

    Dialog dialog;

    SharedPreferences sharedPreferences;

    Drawable overflowIcon;

    ArrayList<Category> categoryArrayList;

    String idCategory;

    IClickCategory iClickCategory;

    StrikethroughSpan strikethroughSpan;


    AdapterSelectCategory adapterSelectCategory;

    Boolean checkUpdate = false;
    String format1;

    String colorTextAddNote = "";

    String format2;
    SpannableString spannableString1;

    SpannableStringBuilder spannableStringBuilder;


    SpannableString spannableString2;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update);

        toolbar = this.<Toolbar>findViewById(R.id.toolbarUpdate);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });

        // icon de xo ra thanh menu bar
        overflowIcon = toolbar.getNavigationIcon();

        Intent intent = getIntent();
        checkUpdate = intent.getBooleanExtra("checkUpdate", false);
        Log.d("fsffasf", checkUpdate + " ");

        databaseHandler = new DBManager(this);

        databaseHandler.open();

        edTitle = findViewById(R.id.edTitleNote);
        edContent = findViewById(R.id.edContentNote);
        linearLayout = this.<LinearLayout>findViewById(R.id.layoutUpdateNote);

        MyViewModelFactory factory = new MyViewModelFactory(databaseHandler);
        dataViewModel = new ViewModelProvider(this, factory).get(DataViewModel.class);

        categoryArrayList = new ArrayList<>();

        adapterSelectCategory = new AdapterSelectCategory(categoryArrayList, iClickCategory);

        dataViewModel.getAllListCategory();

        dataViewModel.getListCategory().observe(UpdateActivity.this, new Observer<ArrayList<Category>>() {
            @Override
            public void onChanged(ArrayList<Category> categories) {
                categoryArrayList.clear();
                categoryArrayList.addAll(categories);
                adapterSelectCategory.notifyDataSetChanged();
            }
        });

        sharedPreferences = UpdateActivity.this.getSharedPreferences(" ", Context.MODE_PRIVATE);

        themeStyle = sharedPreferences.getString("theme_system", "Default");

        // lay dai mau mo do cho add
        DefaultColor = R.color.backgroundItem;

        updateViewModel = new UpdateViewModel();

        colorText = getColor(R.color.black);

        dialog = new Dialog(UpdateActivity.this);

        // ban dau la color mau trong suot
        bgColorText = Color.parseColor("#FFFFFF");

        checkBoxSelect();
        strikethroughSpan = new StrikethroughSpan();

        updateViewModel.getMutableLiveData().observe(this, selected -> {

            if (selected.isCheck()) {
                if (selected.getIndex().contains("#")) {
                    // set Color
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(selected.getIndex()));
                    spannableStringBuilder.setSpan(colorSpan, 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    edContent.setText(spannableStringBuilder);
                } else if (selected.getIndex().equals("Under")) {
                    // set Underline
                    spannableStringBuilder.setSpan(new UnderlineSpan(), 0, format1.length(), 0);
                    edContent.setText(spannableStringBuilder);
                } else if (selected.getIndex().equals("Strike")) {

                    spannableStringBuilder.setSpan(strikethroughSpan, 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    edContent.setText(spannableStringBuilder);
                } else if (selected.getIndex().equals("BgColorText")) {
                    // Tạo một BackgroundColorSpan với màu nền mong muốn (ví dụ: màu vàng)
                    String hexColor = String.format("#%06X", (0xFFFFFF & Integer.parseInt(selected.getValue())));
                    BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.parseColor(hexColor));
                    spannableStringBuilder.setSpan(backgroundColorSpan, 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    edContent.setText(spannableStringBuilder);
                } else {
                    // cai nay xet text
                    spannableStringBuilder.setSpan(new StyleSpan(Integer.parseInt(selected.getIndex())), 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    edContent.setText(spannableStringBuilder);
                }
            }
            else
            {
                StyleSpan[] styleSpans = spannableStringBuilder.getSpans(0, format1.length(), StyleSpan.class);

                if (selected.getIndex().equals("Black")) {
                    Log.d("kiemtraduleu", "sasfdf");
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(getColor(R.color.black));
                    colorText = getColor(R.color.black);
                    spannableStringBuilder.setSpan(colorSpan, 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                if (selected.getIndex().equals("BgColorText")) {
                    BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.parseColor("#00000000"));
                    spannableStringBuilder.setSpan(backgroundColorSpan, 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                if (selected.getIndex().equals("Strike")) {
                    spannableStringBuilder.removeSpan(strikethroughSpan);
                }

                for (StyleSpan styleSpan : styleSpans)
                {
                    if (selected.getIndex().equals("Under")) {
                        spannableStringBuilder.removeSpan((new UnderlineSpan()));
                    } else if (!selected.getIndex().equals("Black")
                            && !selected.getIndex().equals("BgColorText")
                            && !selected.getIndex().equals("Strike")) {
                        if (styleSpan.getStyle() == Integer.parseInt(selected.getIndex())) {
                            Log.d(" xoaaa ", styleSpan.getStyle() + " ");
                            spannableStringBuilder.removeSpan(styleSpan);
                        }
                    }
                }

                edContent.setText(spannableStringBuilder);
            }

        });

        if (checkUpdate) {

            noteData = getIntent().getExtras().getParcelable("note");

            if (noteData.getStyleTextColor() != null) {
                if (noteData.getStyleTextColor().equals("#1B1A18")) {
                    colorText = getResources().getColor(R.color.black);
                } else {
                    colorText = Color.parseColor(noteData.getStyleTextColor());
                }
                colorText = Color.parseColor(noteData.getStyleTextColor());
            }

            edTitle.setText(noteData.getTitle());

            edContent.setText(noteData.getContent());

            format1 = edContent.getText().toString();
            spannableString1 = new SpannableString(format1);
            Log.d("kiemtradulieu", format1.toString());

            ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(colorText);

            spannableString1.setSpan(colorSpan1, 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            spannableStringBuilder = new SpannableStringBuilder(format1);

            spannableStringBuilder.setSpan(colorSpan1, 0, format1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            // dinh dang mau cho text ne

            // ontextchang


            iClickCategory = new IClickCategory() {
                @Override
                public void click(String id) {
                    idCategory = id;
                }
            };

            // check mau cho bg color
            if (noteData.getBgColors().trim().length() > 0) {
                DefaultColor = Color.parseColor(noteData.getBgColors());
            } else {
                DefaultColor = Color.parseColor(String.valueOf(R.color.backgroundItem));
            }

            Drawable myIcon = AppCompatResources.getDrawable(this, R.drawable.layout_update_note);

            // khac mau mac dinh thi check
            if (!noteData.getStyleTextColor().equals("#1B1A18")) {
                cbColorText.setChecked(true);
            }

            if (noteData.getStyleBold().equals("true")) {

                cbBold.setBackground(getDrawable(R.drawable.checkbox_formmated));
                cbBold.setChecked(true);
                spannableStringBuilder.setSpan(new StyleSpan(Integer.parseInt("1")), 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                edContent.setText(spannableStringBuilder);

            } else {
                cbBold.setBackground(getDrawable(R.drawable.checkbox_formmated));
                cbBold.setChecked(false);
            }


            if (noteData.getStyleItalic().equals("true")) {
                Log.d("lieiee", "idfsadfa");
                cbItalic.setBackground(getDrawable(R.drawable.checkbox_formmated));
                cbItalic.setChecked(true);
                spannableStringBuilder.setSpan(new StyleSpan(Integer.parseInt("2")), 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                edContent.setText(spannableStringBuilder);
            } else {
                cbItalic.setBackground(getDrawable(R.drawable.checkbox_formmated));
                cbItalic.setChecked(false);
            }

            if (noteData.getStyleUnderline().equals("true")) {
                cbUnderline.setBackground(getDrawable(R.drawable.checkbox_formmated));
                cbUnderline.setChecked(true);
                spannableStringBuilder.setSpan(new UnderlineSpan(), 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                edContent.setText(spannableStringBuilder);
            }

            if (!noteData.getStyleTextColor().equals("#1B1A18")) {

                cbColorText.setBackground(getDrawable(R.drawable.checkbox_formmated));
                cbColorText.setChecked(true);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor(noteData.getStyleTextColor())), 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                edContent.setText(spannableStringBuilder);
            }


               if(noteData.getStrike().equals("true"))
               {
                   // khong hieu sao nhung bat buoc phai co dong nay moi duoc
                   cbStrike.setBackground(getDrawable(R.drawable.checkbox_formmated));
                   cbStrike.setChecked(true);
                   spannableStringBuilder.setSpan(strikethroughSpan, 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                   edContent.setText(spannableStringBuilder);
               }




               if(!noteData.getBackgroundColorText().equals("-1"))
               {

                   Log.d("656565",Integer.parseInt(noteData.getBackgroundColorText()) + " ");


//                   String hexColor = String.format("#%06X", (0xFFFFFF & Integer.parseInt(noteData.getBackgroundColorText())));

                   // ma mau no la -32423 nen chuyen no ve #3232FS
//                   cbBgColor.setBackground(getDrawable(R.drawable.checkbox_formmated));
//                   cbBgColor.setChecked(true);
//                   BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.parseColor(hexColor));

//                   Log.d("656565",noteData.getBackgroundColorText());
//                   edContent.setText(spannableStringBuilder);

               }

               if( noteData.getBgColors() != null)
               {
                   linearLayout.setBackgroundColor(Color.parseColor(noteData.getBgColors()));
               }



           checkSelect();


        } else {

            format1 = edContent.getText().toString();
            spannableStringBuilder = new SpannableStringBuilder(format1);
            Log.d("kiemtradulieu", format1.toString());
            ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(colorText);
            spannableStringBuilder.setSpan(colorSpan1, 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

             checkSelect();


        }

    }

    private void checkSelect() {

        if (cbBold.isChecked()) {
            spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (cbItalic.isChecked()) {
            spannableStringBuilder.setSpan(new StyleSpan(Typeface.ITALIC), 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        if (cbUnderline.isChecked()) {
            spannableStringBuilder.setSpan(new UnderlineSpan(), 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }

        //
        //lay mau cho text

        if (cbColorText.isChecked()) {
            // Chuyển đổi mã màu từ dạng hex sang giá trị integer và xét màu cho span
            // xet mau kieu Int cho span ne
            spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor(noteData.getStyleTextColor())), 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }

        if (cbStrike.isChecked()) {
            spannableStringBuilder.setSpan(strikethroughSpan, 0, format1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        edContent.addTextChangedListener(new TextWatcher() {

            private boolean isBold = false;


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(colorText);
                spannableStringBuilder.replace(0, format1.length(), s.toString());
                format1 = s.toString();

            }

            @Override
            public void afterTextChanged(Editable s) {
                // ban dau la  false

                if (!isBold) {
                    isBold = true;
                    edContent.setText(spannableStringBuilder);
                    // chuyen con tro ve cuoi doan editext
                    edContent.setSelection(format1.length());
                } else {
                    isBold = false;
                }
            }
        });
    }

    private void applyTextStyle(String text) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);

        // Áp dụng kiểu chữ in đậm nếu checkbox Bold được chọn
        if (cbBold.isChecked()) {
            spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // Áp dụng kiểu chữ nghiêng nếu checkbox Italic được chọn
        if (cbItalic.isChecked()) {
            spannableStringBuilder.setSpan(new StyleSpan(Typeface.ITALIC), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // Cập nhật văn bản của EditText
        edContent.setText(spannableStringBuilder);
    }

    private void checkBoxSelect() {
        cbBold = findViewById(R.id.cbBold);
        cbItalic = findViewById(R.id.cbItalic);
        cbUnderline = findViewById(R.id.cbUnderline);
        cbColorText = findViewById(R.id.cbColorText);
        cbBgColor = findViewById(R.id.cbBgColor);
        cbStrike = findViewById(R.id.cbStrike);
        cbBold.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox) view;

                if (checkBox.isChecked()) {
                    cbBold.setBackground(getDrawable(R.drawable.checkbox_formmated));
                    cbBold.setChecked(true);
                    Selected selected = new Selected("1", true);
                    updateViewModel.setMutableLiveData(selected);
                } else {
                    cbBold.setBackground(getDrawable(R.drawable.checkbox_formmated));
                    cbBold.setChecked(false);
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
                    cbItalic.setBackground(getDrawable(R.drawable.checkbox_formmated));
                    cbItalic.setChecked(true);
                    Selected selected = new Selected("2", true);
                    updateViewModel.setMutableLiveData(selected);
                } else {
                    cbItalic.setChecked(false);
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
                    cbUnderline.setBackground(getDrawable(R.drawable.checkbox_formmated));
                    cbUnderline.setChecked(true);
                    Selected selected = new Selected("Under", true);
                    updateViewModel.setMutableLiveData(selected);
                } else {
                    cbUnderline.setChecked(false);
                    Selected selected = new Selected("Under", false);
                    updateViewModel.setMutableLiveData(selected);
                }

            }
        });

        cbStrike.setOnClickListener(view -> {

            CheckBox checkBox = (CheckBox) view;
            if (checkBox.isChecked()) {
                cbStrike.setBackground(getDrawable(R.drawable.checkbox_formmated));
                cbStrike.setChecked(true);
                Selected selected = new Selected("Strike", true);
                updateViewModel.setMutableLiveData(selected);
            } else {
                cbStrike.setBackground(getDrawable(R.drawable.checkbox_formmated));
                cbStrike.setChecked(false);
                Selected selected = new Selected("Strike", false);
                updateViewModel.setMutableLiveData(selected);
            }
        });

        cbBgColor.setOnClickListener(view -> {

            CheckBox checkBox = (CheckBox) view;

            if (checkBox.isChecked()) {

                Log.d("fsfdafaaaa", "saa");
                AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(UpdateActivity.this, colorText, true, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog ambilWarnaDialog, int color) {
                        String hexColor = String.format("#%06X", (0xFFFFFF & color));

                        // Color.parseColor de chuyen tu color string sang color int
                        bgColorText = Color.parseColor(hexColor);
                        cbBgColor.setBackground(getDrawable(R.drawable.checkbox_formmated));
                        cbBgColor.setChecked(true);
                        Log.d("fsdfasfafs", String.valueOf(bgColorText) + " ");
                        Selected selected = new Selected("BgColorText", String.valueOf(bgColorText), true);
                        updateViewModel.setMutableLiveData(selected);
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
                    }
                });
                ambilWarnaDialog.show();
            } else {
                cbBgColor.setBackground(getDrawable(R.drawable.checkbox_formmated));
                cbBgColor.setChecked(false);
                Selected selected = new Selected("BgColorText", false);
                updateViewModel.setMutableLiveData(selected);

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

                            String hexColor = String.format("#%06X", (0xFFFFFF & color));
                            colorText = Color.parseColor(hexColor);
                            colorTextAddNote = hexColor;
                            Selected selected = new Selected(String.valueOf(hexColor), true);
                            Log.d("sdfsaodf", String.valueOf(hexColor));

                            updateViewModel.setMutableLiveData(selected);
                            cbColorText.setBackground(getDrawable(R.drawable.checkbox_formmated));
                            cbColorText.setChecked(true);
                        }

                        @Override
                        public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
                        }
                    });
                    ambilWarnaDialog.show();
                } else {

                    Selected selected = new Selected("Black", false);
                    updateViewModel.setMutableLiveData(selected);

                }
            }
        });

    }


    private void showDialogCategory(Context context) {

        TextView tvCancelCategory, tvOkCategory;


        RecyclerView rcvCategory;

        dataViewModel.getAllListCategory();

        dialog.setContentView(R.layout.selected_category);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        tvCancelCategory = dialog.findViewById(R.id.tvCancleCategory);
        tvOkCategory = dialog.findViewById(R.id.tvOkCategory);


        LinearLayoutManager layoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        rcvCategory = dialog.findViewById(R.id.rcvSelectCategory);
        rcvCategory.setLayoutManager(layoutManager);
        rcvCategory.setAdapter(adapterSelectCategory);

        tvOkCategory.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dataViewModel.updateCategoryNote(noteData, idCategory);
                dialog.dismiss();
            }
        });

        tvCancelCategory.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_add, menu);

        getMenuInflater().inflate(R.menu.menu_save, menu);

        if (checkUpdate) {
            // Inflate menu resource file.
            getMenuInflater().inflate(R.menu.searchview, menu);

            MenuItem menuItemSave = menu.findItem(R.id.nav_saveUpdate);

            if (themeStyle.equals("Solarized")) {
                // thay doi mau cho menu toolbar
                SpannableString spannable = new SpannableString(menuItemSave.getTitle().toString()

                );
                // Thay doi mau cua menu Sort
                spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorTextSolari)), 0, spannable.length(), 0);
                menuItemSave.setTitle(spannable);

                //   Thay đổi màu của OverflowIcon
                if (overflowIcon != null) {
                    overflowIcon.setColorFilter(ContextCompat.getColor(UpdateActivity.this, R.color.colorTextSolari), PorterDuff.Mode.SRC_IN);
                    toolbar.setOverflowIcon(overflowIcon);
                }

                toolbar.setBackgroundColor(getResources().getColor(R.color.themeSolari));
                toolbar.setTitleTextColor(getResources().getColor(R.color.colorTextSolari));

            } else {
                // thay doi mau cho menu toolbar
                SpannableString spannable = new SpannableString(menuItemSave.getTitle().toString());
                // Thay doi mau cua menu Sort
                spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, spannable.length(), 0);
                menuItemSave.setTitle(spannable);

                toolbar.setTitleTextColor(getResources().getColor(R.color.white));

                if (overflowIcon != null) {
                    overflowIcon.setColorFilter(ContextCompat.getColor(UpdateActivity.this, R.color.white), PorterDuff.Mode.SRC_IN);
                    toolbar.setOverflowIcon(overflowIcon);
                }

            }

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


        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        int id = item.getItemId();

        if (id == R.id.nav_saveUpdate) {
            if (checkUpdate) {
                // update note

                eventClickUpdate();
            } else {

                // add note

                addNote();

            }
        } else if (id == R.id.nav_colorize) {

            OpenColorPickerDialog(true);

        } else if (id == R.id.item_search) {
            Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_category) {
            showDialogCategory(UpdateActivity.this);
        }

        return super.onOptionsItemSelected(item);

    }

    private void addNote() {

        if (edTitle.getText().toString().length() > 0 && edContent.getText().toString().length() > 0) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String time = dateFormat.format(date);

            Note note = new Note();

            note.setContent(edContent.getText().toString());
            note.setTitle(edTitle.getText().toString());
            note.setTimeEdit(time.toString());

            // set mau cho text color
            String hexColor = String.format("#%06X", (0xFFFFFF & colorText));
            note.setStyleTextColor(String.valueOf(hexColor));

            String bgColor = String.format("#%06X", (0xFFFFFF & DefaultColor));
            note.setBgColors(String.valueOf(bgColor));



            if (cbBold.isChecked()) {
                Log.d("boldUpdateActivity", "sdfda");
                note.setStyleBold("true");
            } else {
                note.setStyleBold("false");
            }

            if (cbUnderline.isChecked()) {
                note.setStyleUnderline("true");
            } else {
                note.setStyleUnderline("false");

            }

            if (cbItalic.isChecked()) {
                Log.d("itaalcca", "sdfda");

                note.setStyleItalic("true");
            } else {
                note.setStyleItalic("false");

            }

            if(cbStrike.isChecked())
            {
                note.setStrike("true");
            }else {
                note.setStrike("false");

            }


            note.setBgColors(String.valueOf(DefaultColor));

            Intent intent = new Intent();

            Log.d("corrrrr",note.toString() + " ");

            intent.putExtra("note", note);

//            databaseHandler.updateNote(note);

            setResult(RESULT_OK, intent);

            Toast.makeText(this, "Cap nhat thanh cong !", Toast.LENGTH_SHORT).show();

            finish();


        } else {
            Toast.makeText(this, "Vui lòng nhập dữ liệu ", Toast.LENGTH_SHORT).show();
        }


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
            String hexColor = String.format("#%06X", (0xFFFFFF & colorText));
            note.setStyleTextColor(String.valueOf(hexColor));

            String bgColor = String.format("#%06X", (0xFFFFFF & DefaultColor));

            note.setBgColors(String.valueOf(bgColor));

            if (cbBold.isChecked()) {
                note.setStyleBold("true");
            } else {
                note.setStyleBold("false");
            }

            if (cbUnderline.isChecked()) {
                note.setStyleUnderline("true");
            } else {
                note.setStyleUnderline("false");

            }

            if (cbItalic.isChecked()) {
                Log.d("itaalcca", "sdfda");

                note.setStyleItalic("true");
            } else {
                note.setStyleItalic("false");
            }

            if(cbStrike.isChecked())
            {
                note.setStrike("true");
            }else {
                note.setStrike("false");
            }

            note.setBackgroundColorText(String.valueOf(bgColorText));
            note.setIdNoteStyle(noteData.getIdNoteStyle());
            Log.d("checkNotess", note.getIdNoteStyle() + " ");


            Intent intent = new Intent();

            Log.d("corrrrr", note.toString());

            intent.putExtra("note", note);

//            databaseHandler.updateNote(note);

            setResult(RESULT_OK, intent);

//            Toast.makeText(this, "Cap nhat thanh cong !", Toast.LENGTH_SHORT).show();

            finish();


        } else {
            Toast.makeText(this, "Vui lòng nhập dữ liệu ", Toast.LENGTH_SHORT).show();
        }
    }

    private void OpenColorPickerDialog(boolean AlphaSupport) {

        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, DefaultColor, AlphaSupport, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int color) {

                DefaultColor = color;

                // chuyen doi ma mau  -15741650 thanh ma mau  0xFFFFFF

                String hexColor = String.format("#%06X", (0xFFFFFF & DefaultColor));


                DefaultColor = Color.parseColor(String.valueOf(hexColor));

                Log.d("kemtraddulie", String.valueOf(DefaultColor) + " ");

                linearLayout.setBackgroundColor(Color.parseColor(String.valueOf(hexColor)));

            }

            @Override
            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {

//                Toast.makeText(MainActivity.this, "Color Picker Closed", Toast.LENGTH_SHORT).show();
            }
        });
        ambilWarnaDialog.show();

    }

    private void highlightText(String s) {
        SpannableString spannableString = new SpannableString(edContent.getText());
        BackgroundColorSpan[] backgroundColorSpan = spannableString.getSpans(0, spannableString.length(), BackgroundColorSpan.class);
        for (BackgroundColorSpan bgSpan : backgroundColorSpan) {
            spannableString.removeSpan(bgSpan);
        }
        int indexOfKeyWord = spannableString.toString().indexOf(s);
        while (indexOfKeyWord > 0) {
            spannableString.setSpan(new BackgroundColorSpan(Color.YELLOW), indexOfKeyWord, indexOfKeyWord + s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            indexOfKeyWord = spannableString.toString().indexOf(s, indexOfKeyWord + s.length());
        }
        edContent.setText(spannableString);
    }

}