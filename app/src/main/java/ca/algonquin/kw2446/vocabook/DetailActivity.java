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
import ca.algonquin.kw2446.vocabook.model.Voca;
import ca.algonquin.kw2446.vocabook.model.WordSet;
import ca.algonquin.kw2446.vocabook.util.JsonUtil;


public class DetailActivity extends AppCompatActivity implements DetailFrag.VocaItemClicked {

    ArrayList<Voca> list=new ArrayList<>();
    ImageView ivLeft, ivRight, ivVoice, ivAdd;
    TextView tvBig, tvExport, tvTitle;
    int index;
    TextToSpeech tts;
    FragmentManager fragmentManager;
    DetailFrag detailFrag;
    String category;
    private VocaRepository vocaRepository;

    private final int WORDLIST_ACTIVITY=3;
    private final int EDITACTIVITY=5;
    int setId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //String name=getIntent().getStringExtra("name");

        Intent intent = getIntent();
        setId=intent.getIntExtra("setId",1);
        category=intent.getStringExtra("category");
//        setId=getIntent().getIntExtra("setId",1);
        vocaRepository=new VocaRepository(this);
        list=vocaRepository.loadWordList(setId);

        ActionBar actionBar=getSupportActionBar();
        actionBar.show();
        actionBar.setIcon(R.drawable.logo);
        actionBar.setTitle(" Vocabulary");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        //actionBar.setDisplayHomeAsUpEnabled(true);

        ivLeft=findViewById(R.id.ivLeft);
        ivRight=findViewById(R.id.ivRight);
        tvBig=findViewById(R.id.tvBigWord);
        ivVoice=findViewById(R.id.ivVoice);
        ivAdd=findViewById(R.id.ivAdd);
        tvExport=findViewById(R.id.tvExport);
        tvTitle=findViewById(R.id.tvTitle);
        tvTitle.setText(String.format("Word List",category));

        fragmentManager=getSupportFragmentManager();
        detailFrag =(DetailFrag) fragmentManager.findFragmentById(R.id.detailFrag);

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

        tts=new TextToSpeech(DetailActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    if(category.equalsIgnoreCase("french")){
                        tts.setLanguage(Locale.FRANCE);
                    }else if (category.equalsIgnoreCase("korean")){
                        tts.setLanguage(Locale.KOREA);
                    }else{
                        tts.setLanguage(Locale.US);
                    }

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

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailFrag.checkPwd(3,-1);  // EDIT_ACTION=1; DELETE_ACTION=2; ADD_ACTION=3;
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
                Toast.makeText(DetailActivity.this, "You can't access the menu",Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(DetailActivity.this, EditActivity.class);
//                intent.putExtra("list", (Serializable)list);
//                intent.putExtra("setId",setId);
//                startActivityForResult(intent,EDITACTIVITY);
                break;
            case R.id.export:
                ArrayList<Voca> list=(ArrayList<Voca>) vocaRepository.loadWordList(setId);
                JsonArray jsonArray= JsonUtil.convertArrayListToJsonArray(list);

                tvExport.setVisibility(View.VISIBLE);
                tvExport.setText(jsonArray.toString());
                break;
            case R.id.close:
                setResult(RESULT_CANCELED);
                DetailActivity.this.finish();
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
            list.addAll(vocaRepository.loadWordList(setId));
//            list.addAll((ArrayList<Voca>) data.getSerializableExtra("list"));
//
            detailFrag.notifyChanged();
        }
    }

    public int getSetId(){ return  setId; }

    @Override
    public ArrayList<Voca> onGetList() { return list; }



    public void onPause(){
        if(tts !=null){tts.stop();tts.shutdown(); }
        super.onPause();
    }


}





