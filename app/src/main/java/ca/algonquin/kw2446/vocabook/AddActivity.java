package ca.algonquin.kw2446.vocabook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    ArrayList<Voca> newList;

    Button btnList, btnJson, btnText;

    Spinner spLang;
    RadioGroup rgLang;
    RadioButton selectedRb;
    EditText etTitle, etVocaCnt, etRawData;
    ImageView ivVocaAdd;

    LinearLayout container;
    int vocaCnt;
    int inputType=1;  //1:list, 2:json, 3:text;
    private final int LIST_INPUT=1;
    private final int TEXT_INPUT=2;
    private final int JSON_INPUT=3;

    FragmentManager fragmentManager;
    Input_List_Fragment inputListFragment;
    Input_Text_Fragment inputTextFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setIcon(R.drawable.logo);
        actionBar.setTitle("  Vocabulary");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        newList=new ArrayList<>();

        spLang=findViewById(R.id.spLang);

//        int selectedId=rgLang.getCheckedRadioButtonId();
//        selectedRb=(RadioButton)findViewById(selectedId);
        etTitle=findViewById(R.id.etTitle);
        etVocaCnt=findViewById(R.id.etVocaCnt);
        etRawData=findViewById(R.id.etRawData);
        ivVocaAdd=findViewById(R.id.ivVocaAdd);

        btnList=findViewById(R.id.btnList);
        btnJson=findViewById(R.id.btnJson);
        btnText=findViewById(R.id.btnText);

        container=findViewById(R.id.container);

        fragmentManager=getSupportFragmentManager();
        inputListFragment=(Input_List_Fragment) fragmentManager.findFragmentById(R.id.input_List_Frag);
        inputTextFragment=(Input_Text_Fragment) fragmentManager.findFragmentById(R.id.input_Text_Frag);

        ivVocaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeVocaBox();
            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputType=LIST_INPUT;
                fragmentManager.beginTransaction().show(inputListFragment).hide(inputTextFragment).commit();
            }
        });
        fragmentManager.beginTransaction().show(inputListFragment).hide(inputTextFragment).commit();
        btnJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputType=JSON_INPUT;
                fragmentManager.beginTransaction().show(inputTextFragment).hide(inputListFragment).commit();
            }
        });

        btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputType=TEXT_INPUT;
                fragmentManager.beginTransaction().show(inputTextFragment).hide(inputListFragment).commit();
            }
        });
        fragmentManager.beginTransaction().show(inputListFragment).hide(inputTextFragment).commit();

    }

    private  void  changeVocaBox(){
        vocaCnt=Integer.parseInt(etVocaCnt.getText().toString());

        for (int i=0;i<vocaCnt;i++){
            Voca newVoca=new Voca(); // make new voca object
            View newVocaBox=makeNewVocaBox(newVoca); //make new inputbox
            container.addView(newVocaBox);
            newList.add(newVoca);
        }
        Log.d("item_Cnt", String.valueOf(newList.size()));
    }

    private View makeNewVocaBox(Voca voca){
        final View addView = LayoutInflater.from(AddActivity.this.getApplicationContext()).inflate(R.layout.voca_new_item, null);
        Button ibRemove = addView.findViewById(R.id.ibRemove);
        EditText etMean=addView.findViewById(R.id.etMean);
        EditText etWord=addView.findViewById(R.id.etWord);


        final View.OnClickListener thisListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((LinearLayout)addView.getParent()).removeView(addView);
            }
        };
        ibRemove.setOnClickListener(thisListener);
        addView.setTag(voca);
        return addView;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                //Toast.makeText(AddActivity.this,String.valueOf(spLang.getSelectedItem()), Toast.LENGTH_SHORT).show();
                boolean result= vocalist_save();
                Toast.makeText(AddActivity.this,result?"Successed Save!":"Failed Save!", Toast.LENGTH_SHORT).show();
                if(result){
                    Intent intent=new Intent();
                    setResult(RESULT_OK,intent);
                    AddActivity.this.finish();
                }
                break;
            case R.id.cancel:
                setResult(RESULT_CANCELED);
                AddActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean vocalist_save(){
        String title=etTitle.getText().toString().trim();
        String category=String.valueOf(spLang.getSelectedItem());
        boolean result=false;

        if(title.isEmpty() || category.isEmpty()){
            Toast.makeText(AddActivity.this,"Please fill all fields", Toast.LENGTH_SHORT).show();
          //  Log.d("List_Check", "List_cnt"+newList.size());
        }
        else{
            switch (inputType){
                case LIST_INPUT:
                    fillVocaListByList();
                    break;
                case TEXT_INPUT:
                    fillVocaListByText();
                    break;
                case JSON_INPUT:
                    break;
            }

            VocaDB db=new VocaDB(getApplicationContext());
            db.open();

            long idx=db.createWordSetEntry(title,category);
            result=db.createVocaListEntry(idx,newList);
            db.close();
        }

        return result;
    }

    // input value to set value to new voca object.
    private void fillVocaListByList(){
        for (Voca item:newList) {
            View v=container.findViewWithTag(item);
            EditText etWord=v.findViewById(R.id.etWord);
            EditText etMean=v.findViewById(R.id.etMean);

            String mean=etMean.getText().toString().trim();
            String word=etWord.getText().toString().trim();

            item.setMean(mean);
            item.setWord(word);
        }

    }

    private void fillVocaListByText(){

        String rawData=etRawData.getText().toString().trim();
        String vocas[]=rawData.split(",");
        for (String item:vocas) {
            newList.add(new Voca(item.split(":")[0].trim(),item.split(":")[1].trim()));

        }

    }



}
