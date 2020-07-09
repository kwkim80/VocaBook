package ca.algonquin.kw2446.vocabook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import ca.algonquin.kw2446.vocabook.adapter.WordSetAdapter;
import ca.algonquin.kw2446.vocabook.db.VocaDB;
import ca.algonquin.kw2446.vocabook.db.VocaRepository;
import ca.algonquin.kw2446.vocabook.model.Voca;
import ca.algonquin.kw2446.vocabook.util.ApplicationClass;
import ca.algonquin.kw2446.vocabook.util.PasswordFragment;

public class ExamActivity extends AppCompatActivity {

    ArrayList<Voca> quizlist=null;
    ArrayList<Voca> list;
    Button btnMark, btnReset, btnClose, btnList;
    TextView tvSTime;
    LinearLayout quiz_container;
    private VocaRepository vocaRepository;
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

        vocaRepository=new VocaRepository(this);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setIcon(R.drawable.logo);
        actionBar.setTitle("  Vocabulary");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);

        setId=getIntent().getIntExtra("setId",1);
        list= vocaRepository.loadWordList(setId);


        if(savedInstanceState != null && savedInstanceState.getSerializable("answerList") != null){
            quizlist = ((ArrayList<Voca>) savedInstanceState.getSerializable("answerList"));

        }else{
            quizlist=(ArrayList<Voca>) list.clone();
            Collections.shuffle(quizlist);
        }
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
        etWord.setText(v.getChangedWord());
        addView.setTag(v.getId());
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
                checkPwd(MARK_ACTION);
                break;
            case R.id.list:
                checkPwd(LIST_ACTION);
                break;
            case R.id.reset:
                tvSTime.setText(String.format("Quize unique_id: %d",new Random().nextInt(10000)));
                for (Voca voca:quizlist){
                    View qbox=quiz_container.findViewWithTag(voca.getId());
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

        PasswordFragment.newInstance(new PasswordFragment.OnOkClickListener() {
            @Override
            public void onOkClicked() {
                switch (actionType){
                    case MARK_ACTION:
                        for (Voca voca:quizlist){
                            View qbox=quiz_container.findViewWithTag(voca.getId());
                            EditText etWord=qbox.findViewById(R.id.etWord);
                            String answer=etWord.getText().toString().trim();

                            if(voca.getWord().trim().equalsIgnoreCase(answer)){
                                ImageView ivCheck=qbox.findViewById(R.id.ivCheck);
                                ivCheck.setImageResource(R.drawable.check);
                            }else {
                                voca.setChangedWord(String.format("%s->[%s]",answer,voca.getWord().trim()));
                                etWord.setText(voca.getChangedWord());
                            }
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
                            View qbox=quiz_container.findViewWithTag(voca.getId());
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


        }).show(getSupportFragmentManager(), "dialog");;
//        View promptsView = LayoutInflater.from(ExamActivity.this).inflate(R.layout.prompt, null);
//
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                ExamActivity.this);
//        // set prompts.xml to alertdialog builder
//        alertDialogBuilder.setView(promptsView);
//
//        final EditText userInput = (EditText) promptsView
//                .findViewById(R.id.editTextDialogUserInput);
//        // set dialog message
//        alertDialogBuilder
//                .setCancelable(false)
//                .setPositiveButton("OK",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                // get user input and set it to result
//                                // edit text
//
//                                if(userInput.getText().toString().trim().equalsIgnoreCase(ApplicationClass.password)){
//                                    switch (actionType){
//                                        case MARK_ACTION:
//                                            for (Voca voca:quizlist){
//                                                View qbox=quiz_container.findViewWithTag(voca.getId());
//                                                EditText etWord=qbox.findViewById(R.id.etWord);
//                                                String answer=etWord.getText().toString().trim();
//
//                                                if(voca.getWord().trim().equalsIgnoreCase(answer)){
//                                                    ImageView ivCheck=qbox.findViewById(R.id.ivCheck);
//                                                    ivCheck.setImageResource(R.drawable.check);
//                                                }else {
//                                                    voca.setChangedWord(String.format("%s->[%s]",answer,voca.getWord().trim()));
//                                                    etWord.setText(voca.getChangedWord());
//                                                }
//                                            }
//                                            break;
//                                        case LIST_ACTION:
//                                            Intent intent=new Intent(ExamActivity.this, WordListActivity.class);
//                                            intent.putExtra("list", (Serializable)list);
//                                            intent.putExtra("setId",setId);
//                                            startActivityForResult(intent, WORDLIST_ACTIVITY);
//                                            break;
//                                        case RESET_ACTION:
//                                            tvSTime.setText(String.format("Quize unique_id: %d",new Random().nextInt(10000)));
//                                            for (Voca voca:quizlist){
//                                                View qbox=quiz_container.findViewWithTag(voca.getId());
//                                                EditText etWord=qbox.findViewById(R.id.etWord);
//                                                ImageView ivCheck=qbox.findViewById(R.id.ivCheck);
//                                                etWord.setText("");
//                                                ivCheck.setImageResource(R.drawable.checkbox);
//                                            }
//                                            break;
//                                        case CLOSE_ACTION:
//                                            ExamActivity.this.finish();
//                                            break;
//                                    }
//                                }else
//                                    Toast.makeText(ExamActivity.this, "The password is not matched", Toast.LENGTH_SHORT).show();
//
//                            }
//                        })
//                .setNegativeButton("Cancel",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,int id) {
//                                dialog.cancel();
//                            }
//                        });
//        // create alert dialog
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        // show it
//        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==WORDLIST_ACTIVITY){
                ExamActivity.this.finish();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        for (Voca voca:quizlist){
            View qbox=quiz_container.findViewWithTag(voca.getId());
            EditText etWord=qbox.findViewById(R.id.etWord);
            String answer=etWord.getText().toString().trim();
            voca.setChangedWord(answer);

        }
      outState.putSerializable("answerList", quizlist);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if ( savedInstanceState.getSerializable("answerList") != null) {
            quizlist = ((ArrayList<Voca>) savedInstanceState.getSerializable("answerList"));

            for (Voca voca:quizlist){
                View qbox=quiz_container.findViewWithTag(voca.getId());
                EditText etWord=qbox.findViewById(R.id.etWord);
                etWord.setText(voca.getChangedWord());
            }

        }

    }

}
