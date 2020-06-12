package ca.algonquin.kw2446.vocabook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {

    ImageView ivAdd;
    LinearLayout edit_container;
    ArrayList<Voca> list;
    EditText etTitle;
    Spinner spLang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setIcon(R.drawable.logo);
        actionBar.setTitle("  Vocabulary");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);

        ivAdd=findViewById(R.id.ivAdd);
        etTitle=findViewById(R.id.etTitle);
        spLang=findViewById(R.id.spLang);
        edit_container=findViewById(R.id.edit_container);


        Intent intent = getIntent();
        list= (ArrayList<Voca>) intent.getSerializableExtra("list");
//        setId=getIntent().getIntExtra("setId",1);

        makeEditList();

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Voca newVoca=new Voca();
                View editBox=makeNewVocaBox(newVoca);
                edit_container.addView(editBox);
            }
        });
    }

    private void makeEditList(){
        for (Voca v: list){
            View editBox=makeNewVocaBox(v);
            edit_container.addView(editBox);
        }
    }

    private View makeNewVocaBox(Voca v){
        final View addView = LayoutInflater.from(EditActivity.this.getApplicationContext()).inflate(R.layout.voca_new_item, null);

        EditText etWord=addView.findViewById(R.id.etWord);
        EditText etMean=addView.findViewById(R.id.etMean);
        Button ibRemove=addView.findViewById(R.id.ibRemove);

        etWord.setText(v.getWord());
        etMean.setText(v.getMean());
        addView.setTag(v);
       // ibRemove.setImageResource(R.drawable.closex);

        final View.OnClickListener thisListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               // ((LinearLayout)addView.getParent()).removeView(addView);
                edit_container.removeView(addView);
            }
        };

        ibRemove.setOnClickListener(thisListener);

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

                Intent intent=new Intent();
                setResult(RESULT_OK,intent);
                EditActivity.this.finish();
                break;
            case R.id.cancel:
                setResult(RESULT_CANCELED);
                EditActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean vocalist_edit(){
        String title=etTitle.getText().toString().trim();
        String category=String.valueOf(spLang.getSelectedItem());
        boolean result=false;

        if(title.isEmpty() || category.isEmpty()){
            Toast.makeText(EditActivity.this,"Please fill all fields", Toast.LENGTH_SHORT).show();
            //  Log.d("List_Check", "List_cnt"+newList.size());
        }
        else{
            fillVocaListByList();

            VocaDB db=new VocaDB(getApplicationContext());
            db.open();

            long idx=db.createWordSetEntry(title,category);
            result=db.createVocaListEntry(idx,list);
            db.close();
        }
        return result;
    }

    private void fillVocaListByList(){
        for (Voca item:list) {
            View v=edit_container.findViewWithTag(item);
            EditText etWord=v.findViewById(R.id.etWord);
            EditText etMean=v.findViewById(R.id.etMean);

            String mean=etMean.getText().toString().trim();
            String word=etWord.getText().toString().trim();

            item.setMean(mean);
            item.setWord(word);
        }

    }

}
