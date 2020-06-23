package ca.algonquin.kw2446.vocabook.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import ca.algonquin.kw2446.vocabook.ExamActivity;
import ca.algonquin.kw2446.vocabook.R;
import ca.algonquin.kw2446.vocabook.WordListActivity;
import ca.algonquin.kw2446.vocabook.db.VocaDB;
import ca.algonquin.kw2446.vocabook.db.VocaRepository;
import ca.algonquin.kw2446.vocabook.model.Voca;
import ca.algonquin.kw2446.vocabook.model.WordSet;
import ca.algonquin.kw2446.vocabook.adapter.WordSetAdapter;
import ca.algonquin.kw2446.vocabook.util.ApplicationClass;
import ca.algonquin.kw2446.vocabook.util.Utility;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFrag extends Fragment {

    ArrayList<WordSet> list=null;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    private VocaRepository vocaRepository;
    View v;

    public ListFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_list, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vocaRepository=new VocaRepository(getContext());
        list=new ArrayList<>();
        loadWordSets();
        recyclerView=v.findViewById(R.id.rvList);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
       // new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);
        adapter=new WordSetAdapter(this.getContext(), list);
        recyclerView.setAdapter(adapter);

    }

    public void notifyChanged(){
        if(adapter!=null) adapter.notifyDataSetChanged();
    }


    public void loadWordSets(){
        ArrayList<WordSet> data=new ArrayList<>();
        try {

            data=vocaRepository.getItemList( WordSet.class );

            if(data.size()==0){
                vocaRepository.addSample_WordSet();
                data=vocaRepository.getItemList(WordSet.class );
            }
        }
        catch ( SQLException e){
            Toast.makeText(this.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        list.clear();
        list.addAll(data);
        this.notifyChanged();
    }

    private void deleteWordSet(WordSet wordSet){
        list.remove(wordSet);
        this.notifyChanged();
    }

    private ItemTouchHelper.SimpleCallback itemTouchHelperCallBack=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            checkPwd(list.get(viewHolder.getAdapterPosition()));
        }
    };

    public void checkPwd(final WordSet wordSet){

        View promptsView = LayoutInflater.from(getContext()).inflate(R.layout.prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());
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
                                if(userInput.getText().toString().trim().equalsIgnoreCase(ApplicationClass.password)){
                                    deleteWordSet(wordSet);
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


}
