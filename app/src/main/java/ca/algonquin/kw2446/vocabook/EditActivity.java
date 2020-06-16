package ca.algonquin.kw2446.vocabook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
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
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import ca.algonquin.kw2446.vocabook.db.VocaDB;
import ca.algonquin.kw2446.vocabook.model.Voca;

public class EditActivity extends AppCompatActivity {

    ImageView ivAdd, ivEdit, ivRemove;
    LinearLayout edit_container;
    ArrayList<Voca> list;

    EditText etTitle;
    Spinner spLang;
    int changedItem_cnt=0;
    int setId;
    private final int EDIT_ACTION=1;
    private final int DEL_ACTION=2;
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
        setId=getIntent().getIntExtra("setId",1);

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
        final View addView = LayoutInflater.from(EditActivity.this.getApplicationContext()).inflate(R.layout.voca_edit_items, null);

        EditText etWord=addView.findViewById(R.id.etWord);
        EditText etMean=addView.findViewById(R.id.etMean);
        ImageView ivRemove=addView.findViewById(R.id.ivRemove);
        ImageView ivEdit=addView.findViewById(R.id.ivEdit);

        ivRemove.setImageResource(R.drawable.closex);
        ivEdit.setImageResource(R.drawable.edit);
        etWord.setText(v.getWord());
        etMean.setText(v.getMean());
        addView.setTag(v);
       // ibRemove.setImageResource(R.drawable.closex);

        final View.OnClickListener removeListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               // ((LinearLayout)addView.getParent()).removeView(addView);
                //edit_container.removeView(addView);
                 ShowAlertDialog(DEL_ACTION,addView);
            }
        };

        final View.OnClickListener editListener=new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ShowAlertDialog(EDIT_ACTION, addView);
            }
        };

        ivRemove.setOnClickListener(removeListener);
        ivEdit.setOnClickListener(editListener);

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
                break;
            case R.id.cancel:
                break;
        }
        boolean result=changedItem_cnt>0;//update_WordSet();   ///vocalist_edit();
        Toast.makeText(EditActivity.this,result?"Successed Save!":"Failed Save!", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent();
        //intent.putExtra("list",(Serializable) list);
        intent.putExtra("setId",setId);
        setResult(RESULT_OK, intent);
        EditActivity.this.finish();
        return super.onOptionsItemSelected(item);
    }


    private boolean vocalist_edit(int i){
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


            //result=db.update_Item()  //db.update_Voca(list.get(i));
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

    public  void ShowAlertDialog(final int actionType, View v){


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditActivity.this);
        // set title

        // set dialog message
        alertDialogBuilder.setTitle((actionType==1?"Edit?":"Delete")+" a word")
                .setIcon(actionType==1?R.drawable.edit:R.drawable.delete)
                .setMessage("Do you want to "+(actionType==1?"edit?":"delete?"))
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        boolean result;
                        Voca voca=(Voca) v.getTag();
                        VocaDB db=new VocaDB(getApplicationContext());
                        db.open();
                        switch (actionType){
                            case EDIT_ACTION:
                                voca.setWord(((EditText)v.findViewById(R.id.etWord)).getText().toString().trim());
                                voca.setMean(((EditText)v.findViewById(R.id.etMean)).getText().toString().trim());
                                result=db.update_Item(voca);
                                Toast.makeText(EditActivity.this,String.format("%s to update the item!", result?"Succeed":"Failed"), Toast.LENGTH_SHORT).show();
                                break;
                            case DEL_ACTION:

                                result=db.delete_Item(voca);
                                list.remove(voca);
                                edit_container.removeView(v);
                                Toast.makeText(EditActivity.this,String.format("%s to delete the item!", result?"Succeed":"Failed"), Toast.LENGTH_SHORT).show();
                                break;

                        }
                        db.close();
                        changedItem_cnt++;
                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


}
