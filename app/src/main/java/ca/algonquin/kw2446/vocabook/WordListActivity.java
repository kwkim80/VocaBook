package ca.algonquin.kw2446.vocabook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

import ca.algonquin.kw2446.vocabook.db.VocaDB;
import ca.algonquin.kw2446.vocabook.db.VocaRepository;
import ca.algonquin.kw2446.vocabook.fragment.DetailFrag;
import ca.algonquin.kw2446.vocabook.model.Voca;
import ca.algonquin.kw2446.vocabook.util.Utility;

public class WordListActivity extends AppCompatActivity implements  DetailFrag.VocaItemClicked{

    ArrayList<Voca> list=new ArrayList<>();


//    ListView lvWords;
//    VocaAdapter vocaAdapter;

    ImageView ivLeft, ivRight, ivVoice;
    TextView tvBig;
    int index;
    TextToSpeech tts;
    FragmentManager fragmentManager;
    DetailFrag detailFrag;
    EditText etExport;

    private final int WORDLIST_ACTIVITY=3;
    private final int EDITACTIVITY=5;
    int setId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        //String name=getIntent().getStringExtra("name");


        Intent intent = getIntent();
        setId=intent.getIntExtra("setId",1);

//        setId=getIntent().getIntExtra("setId",1);
        list=VocaRepository.loadWordList(getApplicationContext(),setId);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setIcon(R.drawable.logo);
        actionBar.setTitle("  Vocabulary");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);



        ivLeft=findViewById(R.id.ivLeft);
        ivRight=findViewById(R.id.ivRight);
        tvBig=findViewById(R.id.tvBigWord);
        ivVoice=findViewById(R.id.ivVoice);
        etExport=findViewById(R.id.etExport);

        fragmentManager=getSupportFragmentManager();
        detailFrag=(DetailFrag) fragmentManager.findFragmentById(R.id.detailFrag);

        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index=index-1<0?0:index-1;
                Voca voca=list.get(index);
                tvBig.setText(voca.getWord());
            }
        });

        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index=index+1>=list.size()?list.size()-1:index+1;
                Voca voca=list.get(index);
                tvBig.setText(voca.getWord());
            }
        });

        tts=new TextToSpeech(WordListActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }
        });


        ivVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = tvBig.getText().toString().trim();
                //Toast.makeText(getContext(), toSpeak,Toast.LENGTH_SHORT).show();
                tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });


        onVocaItemClicked(0);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit:
                //Toast.makeText(AddActivity.this,String.valueOf(spLang.getSelectedItem()), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(WordListActivity.this, EditActivity.class);
                intent.putExtra("list", (Serializable)list);
                intent.putExtra("setId",setId);
                startActivityForResult(intent,EDITACTIVITY);
                break;
            case R.id.export:
                ArrayList<Voca> list=(ArrayList<Voca>) VocaRepository.loadWordList(getApplicationContext(),setId);
                JsonArray jsonArray=Utility.convertArrayListToJson(list);
                etExport.setVisibility(View.VISIBLE);
                etExport.setText(jsonArray.toString());
                break;
            case R.id.close:
                setResult(RESULT_CANCELED);
                WordListActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onVocaItemClicked(int i) {
        index=i;
        if(list.size()>i){
            Voca voca=list.get(i);
            tvBig.setText(voca.getWord());
        }


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==EDITACTIVITY){
          //if(resultCode==RESULT_OK){}
            setId=data.getIntExtra("setId",setId);

            list.clear();
            list.addAll(VocaRepository.loadWordList(WordListActivity.this,setId));
//            list.addAll((ArrayList<Voca>) data.getSerializableExtra("list"));
//
            detailFrag.notifyChanged();


        }
    }
    @Override
    public ArrayList<Voca> onGetList() {
            return list;
    }

    public void onPause(){
        if(tts !=null){tts.stop();tts.shutdown(); }
        super.onPause();
    }



}
