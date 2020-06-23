package ca.algonquin.kw2446.vocabook.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import ca.algonquin.kw2446.vocabook.DetailActivity;
import ca.algonquin.kw2446.vocabook.ExamActivity;
import ca.algonquin.kw2446.vocabook.R;
import ca.algonquin.kw2446.vocabook.WordListActivity;
import ca.algonquin.kw2446.vocabook.db.VocaRepository;
import ca.algonquin.kw2446.vocabook.model.Voca;
import ca.algonquin.kw2446.vocabook.adapter.VocaAdapter;
import ca.algonquin.kw2446.vocabook.util.ApplicationClass;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFrag extends Fragment {


    public interface VocaItemClicked{
        void onVocaItemClicked(int i);
        ArrayList<Voca> onGetList();
    }
    private static final String TAG = "DetailFrag";
    SwipeMenuListView smlvWords;
    TextView tvBig;
    View v;
    VocaAdapter vocaAdapter;
    VocaItemClicked activity;
    int setId;
    private VocaRepository vocaRepository;

    private final int EDIT_ACTION=1;
    private final int DELETE_ACTION=2;
    private final int ADD_ACTION=3;



    public DetailFrag() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity=(VocaItemClicked) context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_detail, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //tvBig=getActivity().findViewById(R.id.tvBigWord);
        vocaRepository=new VocaRepository(getContext());
        smlvWords=v.findViewById(R.id.smlvWords);

        //listView = (SwipeMenuListView) findViewById(R.id.listView);
        setId=((DetailActivity)activity).getSetId();


        vocaAdapter=new VocaAdapter(getContext(), activity.onGetList());
        smlvWords.setAdapter(vocaAdapter);

        smlvWords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                activity.onVocaItemClicked(position);

            }
        });
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem ediItem = new SwipeMenuItem(
                        getContext());
                // set item background
                ediItem.setBackground(new ColorDrawable(Color.argb(0xBB,0xEd,
                        0xc5, 0xEd)));
                // set item width
                ediItem.setWidth(170);
                ediItem.setIcon(R.drawable.edit);
                // add to menu
                menu.addMenuItem(ediItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.argb(0x44,0xEd,
                        0xc5, 0xEd)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        smlvWords.setMenuCreator(creator);

        smlvWords.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                       // Log.d(TAG, "onMenuItemPosition: clicked position " + position);
                         checkPwd(EDIT_ACTION, position);
                         //editShowDialog(position);
                        break;
                    case 1:
                         checkPwd(DELETE_ACTION, position);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        activity.onVocaItemClicked(0);
    }

    public void notifyChanged(){
        vocaAdapter.notifyDataSetChanged();
    }

    public void editShowDialog(final int idx){

        View addView = LayoutInflater.from(getContext()).inflate(R.layout.voca_add, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(addView);

            Voca voca=idx!=-1?activity.onGetList().get(idx):new Voca();
            final EditText etWord = (EditText) addView
                    .findViewById(R.id.etWord);
            final EditText etMean = (EditText) addView
                    .findViewById(R.id.etMean);

            etWord.setText(voca.getWord());
            etMean.setText(voca.getMean());


        // set dialog message
        alertDialogBuilder.setTitle(String.format("%s A WORD",idx!=-1?"UPDATE":"ADD"))
                .setIcon(idx!=-1?R.drawable.edit:R.drawable.add)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                voca.setWordSetId(setId);
                                voca.setWord(etWord.getText().toString().trim());
                                voca.setMean(etMean.getText().toString().trim());

                                boolean result=idx!=-1?vocaRepository.update_Item(voca):vocaRepository.insert_Item(voca)>0;

                                Toast.makeText(getContext(),String.format("%s to %s the item!",result ?"Succeed":"Failed", idx!=-1?"update":"add"), Toast.LENGTH_SHORT).show();
                                if(result && idx==-1)activity.onGetList().add(voca);
                                notifyChanged();

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


    public void checkPwd(final int actionType, final int idx){

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

                                boolean result;
                                if(userInput.getText().toString().trim().equalsIgnoreCase(ApplicationClass.password)){
                                    switch (actionType){
                                        case EDIT_ACTION:
                                      editShowDialog(idx);
                                            break;
                                        case DELETE_ACTION:
                                            Voca voca=activity.onGetList().get(idx);
                                            result= vocaRepository.delete_Item(voca);
                                            if(result){
                                                activity.onGetList().remove(idx);
                                                notifyChanged();
                                            }
                                            Toast.makeText(getContext(),String.format("%s to delete the Item",result?"Succeed":"Failed"),Toast.LENGTH_SHORT).show();
                                            break;
                                        case ADD_ACTION:
                                            editShowDialog(-1);
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

}
