package com.example.notepad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notepad.Database.DBManager;
import com.example.notepad.Interface.IData;
import com.example.notepad.Model.Category;
import com.example.notepad.Model.Note;
import com.example.notepad.ViewModel.DataViewModel;
import com.example.notepad.ViewModel.MyViewModelFactory;
import com.example.notepad.view.AboutFragment;
import com.example.notepad.view.AddNoteActivity;
import com.example.notepad.view.CategoryFragment;
import com.example.notepad.view.HomeFragment;
import com.example.notepad.view.SelectAllActivity;
import com.example.notepad.view.SettingFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    DBManager databaseHandler;

    FloatingActionButton floatingActionButton;
    DataViewModel dataViewModel;
    ArrayList<Note> noteArrayList;

    Dialog dialog;


    String selector = "";
    IData iData;

    Toolbar toolbar;

    MenuItem searchItem;

    ConstraintLayout constraintLayout;

    // icon open menu selecter
    Drawable overflowIcon;

    NavigationView navigationView;

    String valueTheme;

    ActionBarDrawerToggle toggle;
    int itemId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        setSupportActionBar(toolbar);

        overflowIcon = toolbar.getOverflowIcon();


        drawerLayout = findViewById(R.id.drawer_layout);
        floatingActionButton = findViewById(R.id.fab);

        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);


        dialog = new Dialog(MainActivity.this);

        // tham so truyen vao
        databaseHandler = new DBManager(this);
        databaseHandler.open();
//        int abc = databaseHandler.getAllNotes().size();
//        databaseHandler.getAllNoteCategory() ;
        databaseHandler.getAllCategory();


        MyViewModelFactory factory = new MyViewModelFactory(databaseHandler);

        dataViewModel = new ViewModelProvider(this, factory).get(DataViewModel.class);


        dataViewModel.getData();

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        dataViewModel.getAllListCategory();


        dataViewModel.getListCategory().observe(this, new Observer<ArrayList<Category>>() {
            @Override
            public void onChanged(ArrayList<Category> categories) {
                itemId = 0;
                MenuItem item = navigationView.getMenu().getItem(1);
                SubMenu subMenu = item.getSubMenu();
                int size = subMenu.size();
                for (int i = size - 1; i > 0; i--) {
                    subMenu.removeItem(i);
                }
                for (Category category : categories) {
                    itemId++;
                    subMenu.add(Menu.NONE, Integer.parseInt(category.getIdCategory()), Menu.NONE, category.getNameCategory()).setIcon(R.drawable.icons8tag40);
                }

            }
        });


        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        noteArrayList = new ArrayList<>();

        // backup data note
        noteArrayList.addAll(dataViewModel.getValueArr());

        dataViewModel.getMutableLiveDataNote().observe(this, new Observer<Note>() {
            @Override
            public void onChanged(Note note) {
                Log.d("safdfa", note.getTitle());
                noteArrayList.add(note);
            }
        });


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.fragmentHome);
        }


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
//                Bundle extras = new Bundle();
//                extras.putBoolean("checkUpdate",false);
//                intent.putExtras(extras);
                intent.putExtra("checkUpdate", false);

                startActivityForResult(intent, 10);

            }
        });


        dataViewModel.getThemeString().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                switch (s) {
                    case "Solarized":
                        // icon de mo menu ben trai ra
                        overflowIcon = toolbar.getOverflowIcon();

                        //   Thay đổi màu của OverflowIcon
                        if (overflowIcon != null) {
                            overflowIcon.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorTextSolari), PorterDuff.Mode.SRC_IN);
                            toolbar.setOverflowIcon(overflowIcon);
                        }


                        // thay doi mau tooolbar
                        toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.themeSolari));

                        // thay doi background
                        navigationView.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.themeSolari));

                        View headerView = navigationView.getHeaderView(0);
                        LinearLayout linearLayout = headerView.findViewById(R.id.idHeaderNav);
                        linearLayout.setBackgroundColor(getResources().getColor(R.color.themeSolari));

                        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorTextSolari));
                        // thay doi mau sac cua toolbar
                        toolbar.setTitleTextColor(getResources().getColor(R.color.colorTextSolari));

                        if (searchItem != null) {
                            Drawable icon = searchItem.getIcon();

                            // Thay doi mau icon
                            if (icon != null) {
                                icon.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorTextSolari), PorterDuff.Mode.SRC_IN);
                                searchItem.setIcon(icon);
                            }
                        }
                        break;

                    case "Default":

                        //    reference đến OverflowIcon của Toolbar
                        overflowIcon = toolbar.getOverflowIcon();

                        //   Thay đổi màu của OverflowIcon
                        if (overflowIcon != null) {
                            overflowIcon.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.white), PorterDuff.Mode.SRC_IN);
                            toolbar.setOverflowIcon(overflowIcon);
                        }
                        // thay doi mau tooolbar
                        toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.appbar));

                        // thay doi background
                        navigationView.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.themeSolari));

                        View headerViewDefault = navigationView.getHeaderView(0);
                        LinearLayout linearLayoutDefault = headerViewDefault.findViewById(R.id.idHeaderNav);
                        linearLayoutDefault.setBackgroundColor(getResources().getColor(R.color.themeSolari));
                        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
                        // thay doi mau sac cua toolbar
                        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

                        if (searchItem != null) {
                            Drawable icon = searchItem.getIcon();

                            // Thay doi mau icon
                            if (icon != null) {
                                icon.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.white), PorterDuff.Mode.SRC_IN);
                                searchItem.setIcon(icon);
                            }
                        }
                        break;

                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Lấy giá trị từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyTheme", Context.MODE_PRIVATE);
        valueTheme = sharedPreferences.getString("theme_system", "default");

        Log.d("kiemtratheme", valueTheme.toString());

        if (!valueTheme.equals("default")) {
            dataViewModel.setThemeString(valueTheme);
        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_select) {

            Intent intent = new Intent(MainActivity.this, SelectAllActivity.class);
            intent.putParcelableArrayListExtra("listData", noteArrayList);

            startActivityForResult(intent, 20);

            Toast.makeText(this, "itemImportText", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_import) {
            Toast.makeText(this, "itemSelectAll", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_export) {
        } else if (id == R.id.sort_menu) {
            showDialogSort();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.searchview, menu);

        searchItem = menu.findItem(R.id.item_search);

        Drawable icon = searchItem.getIcon();

        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            if (valueTheme.equals("Default")) {
                searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
                Drawable icon1 = searchItem.getIcon();
                icon1.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.white), PorterDuff.Mode.SRC_IN);
                searchItem.setIcon(icon);
            } else {
                searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
                Drawable icon1 = searchItem.getIcon();
                icon1.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorTextSolari), PorterDuff.Mode.SRC_IN);
                searchItem.setIcon(icon);
            }
        }

//         Listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query submission

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                dataViewModel.setStringMutableLiveData(newText.trim());
                // list result search
                ArrayList<Note> noteNewArrayList = new ArrayList<>();
                // xét cho nó list mới là xong
                if (newText.trim().length() > 0) {
                    int count = 0;
                    // check xem co tim ra hay khong
                    Boolean check = false;
                    for (Note note : noteArrayList) {
                        count++;
                        if (note.getTitle().contains(newText.trim().toLowerCase()) || note.getTitle().contains(newText.trim().toUpperCase())) {
                            check = true;
                            noteNewArrayList.add(note);
                            dataViewModel.setListMutableLiveData(noteNewArrayList);
                        }
                    }
                    if (count == noteArrayList.size() && !check) {
                        // neu khong tim thay set mang ve empty
                        ArrayList<Note> notes = new ArrayList<>();
                        dataViewModel.setListMutableLiveData(notes);
                    }
                } else if (newText.trim().length() == 0) {
                    Log.d("fasfa", noteArrayList.size() + "");
                    dataViewModel.setListMutableLiveData(noteArrayList);
                }
                // Handle search query text change
                return false;
            }
        });

        


//         tao menu cho selector
        getMenuInflater().inflate(R.menu.menu_option, menu);

        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (requestCode == 10) {

            if (data != null) {

                dataViewModel.getData();

                Note note = (Note) data.getParcelableExtra("note");

                Log.d("fasfasf", note.getStyleBold());

                dataViewModel.setMutableLiveDataNote(note);

            }

        } else if (requestCode == 20) {
            dataViewModel.getData();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    
    // menu drawer selector
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id > 0 && id <= itemId) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment(String.valueOf(id))).commit();

        } else {
            if (id == R.id.fragmentHome) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            } else if (id == R.id.nav_setting_theme) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingFragment()).commit();

            } else if (id == R.id.nav_editCategory) {
                Toast.makeText(this, "HHHH", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoryFragment()).commit();
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    // dialog sap xep

    private void showDialogSort() {

        TextView tvCancle, tvSort;

        dialog.setContentView(R.layout.sort_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        tvCancle = dialog.findViewById(R.id.tvCancle);
        tvSort = dialog.findViewById(R.id.tvSort);
        RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);


        // xap xep theo tieu de
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.title_AtoZ) {
                selector = "TitleAtoZ";

            } else if (checkedId == R.id.title_newest) {
                selector = "title_newest";

            } else if (checkedId == R.id.title_ZtoA) {
                selector = "TitleZtoA";
            } else if (checkedId == R.id.title_oldest) {
                selector = "title_oldest";
            }

        });

        tvSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dataViewModel.setOnSelectedSort(selector);

                dialog.dismiss();

            }
        });

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                Toast.makeText(MainActivity.this, "Cancel clicked", Toast.LENGTH_SHORT).show();

            }
        });

        dialog.show();

    }


}