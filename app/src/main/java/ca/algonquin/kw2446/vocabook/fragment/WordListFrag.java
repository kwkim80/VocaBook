package ca.algonquin.kw2446.vocabook.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;

import ca.algonquin.kw2446.vocabook.DetailActivity;
import ca.algonquin.kw2446.vocabook.R;
import ca.algonquin.kw2446.vocabook.adapter.WordEditAdapter;
import ca.algonquin.kw2446.vocabook.db.VocaRepository;
import ca.algonquin.kw2446.vocabook.model.Voca;
import ca.algonquin.kw2446.vocabook.adapter.VocaAdapter;
import ca.algonquin.kw2446.vocabook.model.WordSet;
import ca.algonquin.kw2446.vocabook.util.ApplicationClass;


/**
 * A simple {@link Fragment} subclass.
 */
public class WordListFrag extends Fragment {




    private static final String TAG = "WordListFrag";
    SwipeMenuListView smlvWords;
    View v;
    WordEditAdapter wordEditAdapter;
    int setId;

    private VocaRepository vocaRepository;
    ArrayList<WordSet> list=new ArrayList<>();
    private final int EDIT_ACTION=1;
    private final int DELETE_ACTION=2;
    private final int ADD_ACTION=3;



    public WordListFrag() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_word_list, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vocaRepository=new VocaRepository(getContext());
        //tvBig=getActivity().findViewById(R.id.tvBigWord);
        list=vocaRepository.getItemList(WordSet.class);
        smlvWords=v.findViewById(R.id.smlvWords);

        //listView = (SwipeMenuListView) findViewById(R.id.listView);
        //setId=((DetailActivity)activity).getSetId();


        wordEditAdapter=new WordEditAdapter(getContext(), list);
        smlvWords.setAdapter(wordEditAdapter);

        smlvWords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


            }
        });
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem ediItem = new SwipeMenuItem(getContext());
                ediItem.setBackground(new ColorDrawable(Color.argb(0xBB,0xEd, 0xc5, 0xEd)));
                ediItem.setWidth(170);
                ediItem.setIcon(R.drawable.edit);
                // add to menu
                menu.addMenuItem(ediItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getContext());
                deleteItem.setBackground(new ColorDrawable(Color.argb(0x44,0xEd, 0xc5, 0xEd)));
                deleteItem.setWidth(170);
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
                        checkPwd(EDIT_ACTION, position);
                        break;
                    case 1:
                        checkPwd(DELETE_ACTION, position);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });


    }

    public void notifyChanged(){
        wordEditAdapter.notifyDataSetChanged();
    }

    public void editShowDialog(final int idx){

        View addView = LayoutInflater.from(getContext()).inflate(R.layout.wordset_edit, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(addView);

        WordSet wordSet=list.get(idx);
        final EditText etTitle = (EditText) addView
                .findViewById(R.id.etTitle);
        final Spinner spLang = (Spinner) addView
                .findViewById(R.id.spLang);

        etTitle.setText(wordSet.getTitle());



        // set dialog message
        alertDialogBuilder.setTitle(String.format("Edit A WORD"))
                .setIcon(R.drawable.edit)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text

                                wordSet.setTitle(etTitle.getText().toString().trim());
                                wordSet.setCategory(String.valueOf(spLang.getSelectedItem()));

                                boolean result= vocaRepository.update_Item(wordSet);

                                Toast.makeText(getContext(),String.format("%s to %s the item!",result ?"Succeed":"Failed", "update"), Toast.LENGTH_SHORT).show();

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
                                            WordSet wordSet=list.get(idx);
                                            result= vocaRepository.delete_Item(wordSet);
                                            if(result){

                                               list.remove(idx);
                                               ArrayList vocas=vocaRepository.getItemList(Voca.class,"wordSetId",wordSet.getId());
                                               int cnt=vocaRepository.delete_List(vocas);
                                               notifyChanged();
                                            }
                                            Toast.makeText(getContext(),String.format("%s to delete the Item",result?"Succeed":"Failed"),Toast.LENGTH_SHORT).show();
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
