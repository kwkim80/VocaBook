package ca.algonquin.kw2446.vocabook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class WordListActivity extends AppCompatActivity implements  DetailFrag.VocaItemClicked{

    ArrayList<Voca> list=new ArrayList<>();


//    ListView lvWords;
//    VocaAdapter vocaAdapter;

    ImageView ivLeft, ivRight, ivVoice;
    TextView tvBig;
    int index;
    TextToSpeech tts;
//    FragmentManager fragmentManager;
//    DetailFrag detailFrag;

    private final int EDITACTIVITY=5;
    int setId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        //String name=getIntent().getStringExtra("name");


        Intent intent = getIntent();
        list= (ArrayList<Voca>) intent.getSerializableExtra("list");
//        setId=getIntent().getIntExtra("setId",1);
//        loadWordList(setId);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setIcon(R.drawable.logo);
        actionBar.setTitle("  Vocabulary");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);



        ivLeft=findViewById(R.id.ivLeft);
        ivRight=findViewById(R.id.ivRight);
        tvBig=findViewById(R.id.tvBigWord);
        ivVoice=findViewById(R.id.ivVoice);



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
                startActivityForResult(intent,EDITACTIVITY);
                break;
            case R.id.close:
                WordListActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onVocaItemClicked(int i) {
        index=i;
        Voca voca=list.get(i);
        tvBig.setText(voca.getWord());

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
