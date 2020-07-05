package ca.algonquin.kw2446.vocabook;



import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

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
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import ca.algonquin.kw2446.vocabook.adapter.WordSetAdapter;
import ca.algonquin.kw2446.vocabook.db.VocaRepository;
import ca.algonquin.kw2446.vocabook.fragment.ListFrag;
import ca.algonquin.kw2446.vocabook.model.Voca;
import ca.algonquin.kw2446.vocabook.model.WordSet;
import ca.algonquin.kw2446.vocabook.util.PreferenceManager;


public class MainActivity extends AppCompatActivity implements WordSetAdapter.WordSetItemClicked {
    private DrawerLayout drawerLayout;
    private ActionBar actionBar;
    private Toolbar toolbar;
    FragmentManager fragmentManager;
    ListFrag listFrag;
    FloatingActionButton fab;
    private VocaRepository vocaRepository;
    private final int WORDLIST_ACTIVITY=3;
    private final int ADDACTIVITY=4;
    private final int EDITATIVITY=5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

         actionBar=getSupportActionBar();

       // actionBar.setIcon(R.drawable.logo);
        actionBar.setTitle("Voca Book");

        // actionBar.setDisplayShowHomeEnabled(true);
        // actionBar.setDisplayUseLogoEnabled(true);

        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_main);
        vocaRepository=new VocaRepository(this);
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        initNavigationDrawer();


        fragmentManager=getSupportFragmentManager();
        listFrag= (ListFrag) fragmentManager.findFragmentById(R.id.listFrag);
        fab=findViewById(R.id.fab);
        String pwd= PreferenceManager.getString(this,"pwd");
        //Toast.makeText(this, "pwd:"+pwd, Toast.LENGTH_SHORT).show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, AddActivity.class),ADDACTIVITY);
            }
        });

    }

    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();
                Intent intent;
                switch (id){
                    case R.id.home:
                        //Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();

                        drawerLayout.closeDrawers();
                        break;
                    case R.id.settings:
                       // Toast.makeText(getApplicationContext(),"Settings",Toast.LENGTH_SHORT).show();
                        intent=new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.arrange:
                        //Toast.makeText(getApplicationContext(),"Trash",Toast.LENGTH_SHORT).show();
                        intent=new Intent(MainActivity.this, WordListActivity.class);
                        startActivity(intent);
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
    public void onWordSetItemClicked(WordSet wordSet) {

        Intent intent=new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("setId",wordSet.getId());
        intent.putExtra("category",wordSet.getCategory() );
        startActivity(intent);
    }

    @Override
    public void onWordSetItemLongClicked(WordSet wordSet) {

        Intent intent=new Intent(MainActivity.this, ExamActivity.class);
        intent.putExtra("setId",wordSet.getId());
        startActivity(intent);
    }

    public void refreshPage(){
        finish();
        startActivity(getIntent());
    }

    private void downloadToJson(){

        //JSONArray worsets=db.get_WordSetList_Json();
//        ArrayList<WordSet> wordSets2=db.getAll_WordSet();

        JSONArray vocas=vocaRepository.getJsonList(Voca.class);
        Gson gson = new Gson();
        JsonElement json = gson.toJsonTree(vocas);
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, "/wordSet.json");

        try {
            //new FileWriter( "/users.json",false)
            // new FileWriter("/Users/kw244/Documents/WordSet.json",false)
            gson.toJson(json, new FileWriter( file));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this,"Download Failed", Toast.LENGTH_SHORT).show();
        }
        //String jsonString = gson.toJson(wordSets2);
        Toast.makeText(MainActivity.this,"Download Success", Toast.LENGTH_SHORT).show();
        Log.d("JsonArray",null);
       // db.close();

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
//                Voca voca=new Voca();
//                ArrayList vocas= vocaRepository.getItemList(Voca.class);
                break;
            case R.id.sample:
                vocaRepository.addSample_WordSet();
                listFrag.loadWordSets();
                break;
            case android.R.id.home:

                if (!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.openDrawer(Gravity.LEFT) ;
                }else{
                    drawerLayout.closeDrawer(Gravity.LEFT); ;
                }

                break;

        }
        return super.onOptionsItemSelected(item);
    }


}
