package ca.algonquin.kw2446.vocabook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import ca.algonquin.kw2446.vocabook.db.VocaDB;
import ca.algonquin.kw2446.vocabook.db.VocaRepository;
import ca.algonquin.kw2446.vocabook.model.Voca;

public class ExamActivity extends AppCompatActivity {

    ArrayList<Voca> quizlist;
    ArrayList<Voca> list;
    Button btnMark, btnReset, btnClose, btnList;
    TextView tvSTime;
    LinearLayout quiz_container;

    private final int MARK_ACTION=1;
    private final int LIST_ACTION=2;
    private final int RESET_ACTION=3;
    private final int CLOSE_ACTION=4;

    private final int WORDLIST_ACTIVITY=3;

    private String m_Text = "";
    int setId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setIcon(R.drawable.logo);
        actionBar.setTitle("  Vocabulary");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);

        setId=getIntent().getIntExtra("setId",1);
        list= VocaRepository.loadWordList(getApplication(),setId);
        quizlist=(ArrayList<Voca>) list.clone();
       Collections.shuffle(quizlist);
        //Log.d("quizlist_check",String.valueOf(quizlist.size()));


        tvSTime=findViewById(R.id.tvSTime);

        tvSTime.setText(String.format("unique_id: %d",new Random().nextInt(10000)));

        quiz_container=findViewById(R.id.quiz_container);



       makeQuiz();

    }

    private void makeQuiz(){

        for (Voca v: quizlist){
            View question=makeNewVocaBox(v);
            quiz_container.addView(question);
        }
    }

    private View makeNewVocaBox(Voca v){
        final View addView = LayoutInflater.from(ExamActivity.this.getApplicationContext()).inflate(R.layout.voca_exam_items, null);

        TextView tvMean=addView.findViewById(R.id.tvMean);
        EditText etWord=addView.findViewById(R.id.etWord);
        ImageView ivCheck=addView.findViewById(R.id.ivCheck);

        tvMean.setText(v.getMean());
        etWord.setText("");
        addView.setTag(v);
        ivCheck.setImageResource(R.drawable.checkbox);
        return addView;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exam,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mark:
                //Toast.makeText(AddActivity.this,String.valueOf(spLang.getSelectedItem()), Toast.LENGTH_SHORT).show();
                checkPwd(MARK_ACTION);
                break;
            case R.id.list:
                //Toast.makeText(AddActivity.this,String.valueOf(spLang.getSelectedItem()), Toast.LENGTH_SHORT).show();
                checkPwd(LIST_ACTION);
                break;
            case R.id.reset:
                //Toast.makeText(AddActivity.this,String.valueOf(spLang.getSelectedItem()), Toast.LENGTH_SHORT).show();
               // checkPwd(RESET_ACTION);
                tvSTime.setText(String.format("Quize unique_id: %d",new Random().nextInt(10000)));
                for (Voca voca:quizlist){
                    View qbox=quiz_container.findViewWithTag(voca);
                    EditText etWord=qbox.findViewById(R.id.etWord);
                    ImageView ivCheck=qbox.findViewById(R.id.ivCheck);
                    etWord.setText("");
                    ivCheck.setImageResource(R.drawable.checkbox);
                }
                break;
            case R.id.close:
               // checkPwd(CLOSE_ACTION);
                ExamActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }




    public void checkPwd(final int actionType){

        View promptsView = LayoutInflater.from(ExamActivity.this).inflate(R.layout.prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ExamActivity.this);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text

                                if(userInput.getText().toString().trim().equalsIgnoreCase("q1w2e3r4")){
                                    switch (actionType){
                                        case MARK_ACTION:
                                            for (Voca voca:quizlist){
                                                View qbox=quiz_container.findViewWithTag(voca);
                                                EditText etWord=qbox.findViewById(R.id.etWord);
                                                String answer=etWord.getText().toString().trim();

                                                if(voca.getWord().trim().equalsIgnoreCase(answer)){
                                                    ImageView ivCheck=qbox.findViewById(R.id.ivCheck);
                                                    ivCheck.setImageResource(R.drawable.check);
                                                }else etWord.setText(String.format("%s->[%s]",answer,voca.getWord().trim()));
                                            }
                                            break;
                                        case LIST_ACTION:
                                            Intent intent=new Intent(ExamActivity.this, WordListActivity.class);
                                            intent.putExtra("list", (Serializable)list);
                                            intent.putExtra("setId",setId);
                                            startActivityForResult(intent, WORDLIST_ACTIVITY);
                                            break;
                                        case RESET_ACTION:
                                            tvSTime.setText(String.format("Quize unique_id: %d",new Random().nextInt(10000)));
                                            for (Voca voca:quizlist){
                                                View qbox=quiz_container.findViewWithTag(voca);
                                                EditText etWord=qbox.findViewById(R.id.etWord);
                                                ImageView ivCheck=qbox.findViewById(R.id.ivCheck);
                                                etWord.setText("");
                                                ivCheck.setImageResource(R.drawable.checkbox);
                                            }
                                            break;
                                        case CLOSE_ACTION:
                                            ExamActivity.this.finish();
                                            break;
                                    }


                                }

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==WORDLIST_ACTIVITY){
                ExamActivity.this.finish();
        }
    }

}
