package ca.algonquin.kw2446.vocabook;



import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import ca.algonquin.kw2446.vocabook.adapter.WordSetAdapter;
import ca.algonquin.kw2446.vocabook.db.VocaDB;
import ca.algonquin.kw2446.vocabook.db.VocaRepository;
import ca.algonquin.kw2446.vocabook.fragment.ListFrag;
import ca.algonquin.kw2446.vocabook.model.Voca;


public class MainActivity extends AppCompatActivity implements WordSetAdapter.WordSetItemClicked {
    private DrawerLayout drawerLayout;
    private ActionBar actionBar;
    private Toolbar toolbar;
    FragmentManager fragmentManager;
    ListFrag listFrag;


    private final int WORDLIST_ACTIVITY=3;
    private final int ADDACTIVITY=4;
    private final int EDITATIVITY=5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

         actionBar=getSupportActionBar();

        actionBar.setIcon(R.drawable.logo);
        actionBar.setTitle("  Vocabulary");

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);

        setContentView(R.layout.activity_main);

        //toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        initNavigationDrawer();


        fragmentManager=getSupportFragmentManager();
        listFrag= (ListFrag) fragmentManager.findFragmentById(R.id.listFrag);

    }

    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id){
                    case R.id.home:
                        Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.settings:
                        Toast.makeText(getApplicationContext(),"Settings",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.trash:
                        Toast.makeText(getApplicationContext(),"Trash",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.logout:
                        finish();

                }
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView)header.findViewById(R.id.tv_email);
        tv_email.setText("kw2446@gmail.com");
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
       // toolbar.setNavigationIcon(R.drawable.logo);
    }

    @Override
    public void onWordSetItemClicked( String category, int i) {

        Intent intent=new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("setId",i);
        intent.putExtra("category",category );
        startActivity(intent);
    }

    @Override
    public void onWordSetItemLongClicked(int i) {
        Intent intent=new Intent(MainActivity.this, ExamActivity.class);
        intent.putExtra("setId",i);
        startActivity(intent);
    }

    public void refreshPage(){
        finish();
        startActivity(getIntent());
    }

    private void downloadToJson(){
        VocaDB db=new VocaDB(getApplicationContext());
        db.open();

        JSONArray vocas=db.get_VocaList_Json();
        //JSONArray worsets=db.get_WordSetList_Json();
//        ArrayList<WordSet> wordSets2=db.getAll_WordSet();
//        Gson gson = new Gson();
//        JsonElement json = gson.toJsonTree(wordSets2);
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, "/wordSet.json");
        Gson gson = new Gson();
        try {
            //new FileWriter( "/users.json",false)
            // new FileWriter("/Users/kw244/Documents/WordSet.json",false)
            gson.toJson(vocas, new FileWriter( file));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this,"Download Failed", Toast.LENGTH_SHORT).show();
        }
        //String jsonString = gson.toJson(wordSets2);
        Toast.makeText(MainActivity.this,"Download Success", Toast.LENGTH_SHORT).show();
        Log.d("JsonArray",vocas.toString());
        db.close();

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ADDACTIVITY){
            if(resultCode==RESULT_OK){
                listFrag.loadWordSets();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                startActivityForResult(new Intent(MainActivity.this, AddActivity.class),ADDACTIVITY);
                break;
            case R.id.download:
                //
                downloadToJson();
                //  Function<Integer, Integer> inc = e -> e + 1;
                // Utility.ShowAlertDialog(MainActivity.this, 5, inc);
                break;
            case R.id.refresh:
                //Toast.makeText(MainActivity.this,"Refresh", Toast.LENGTH_SHORT).show();
                //listFrag.loadWordSets();
                Voca voca=new Voca();
                ArrayList vocas= VocaRepository.getItemList(MainActivity.this, Voca.class);
                break;
            case R.id.sample:
                VocaRepository.addSample_WordSet(MainActivity.this);
                listFrag.loadWordSets();

        }
        return super.onOptionsItemSelected(item);
    }


}
