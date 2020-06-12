package ca.algonquin.kw2446.vocabook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements WordSetAdapter.WordSetItemClicked {

//    TextView tvResult, tvExam, tvAnswer;
//    Button btnList, btnExam, btnAnswer;

    FragmentManager fragmentManager;
    ListFrag listFrag;
//    DetailFrag detailFrag;



    private final int WORDLIST_ACTIVITY=3;
    private final int ADDACTIVITY=4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setIcon(R.drawable.logo);
        actionBar.setTitle("  Vocabulary");

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);



        fragmentManager=getSupportFragmentManager();
        listFrag= (ListFrag) fragmentManager.findFragmentById(R.id.listFrag);

       //insertSampleData();
       // onWordSetItemClicked(0);

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
                break;
            case R.id.refresh:
                //Toast.makeText(MainActivity.this,"Refresh", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWordSetItemClicked(int i) {
        Intent intent=new Intent(MainActivity.this, ExamActivity.class);
        intent.putExtra("setId",i);
       // startActivityForResult(intent, WORDLIST_ACTIVITY);
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





}
