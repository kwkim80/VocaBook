package ca.algonquin.kw2446.vocabook;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.fragment.app.FragmentManager;
import com.google.gson.JsonArray;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Locale;

import ca.algonquin.kw2446.vocabook.db.VocaRepository;
import ca.algonquin.kw2446.vocabook.fragment.DetailFrag;
import ca.algonquin.kw2446.vocabook.fragment.WordListFrag;
import ca.algonquin.kw2446.vocabook.model.Voca;
import ca.algonquin.kw2446.vocabook.model.WordSet;
import ca.algonquin.kw2446.vocabook.util.JsonUtil;


public class WordListActivity extends AppCompatActivity {



    int index;

    FragmentManager fragmentManager;
    WordListFrag wordListFrag;




    int setId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        //String name=getIntent().getStringExtra("name");

//        setId=getIntent().getIntExtra("setId",1);



        ActionBar actionBar=getSupportActionBar();
        actionBar.setIcon(R.drawable.logo);
        actionBar.setTitle("  Voca Book");
//        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);



        fragmentManager=getSupportFragmentManager();
        wordListFrag =(WordListFrag) fragmentManager.findFragmentById(R.id.wordListFrag);




    }

//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.detail,menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                WordListActivity.this.finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }







}





