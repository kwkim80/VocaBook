package ca.algonquin.kw2446.vocabook;


import android.database.SQLException;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFrag extends Fragment {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
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

        recyclerView=v.findViewById(R.id.rvList);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new WordSetAdapter(this.getContext(), loadWordSets());
        recyclerView.setAdapter(adapter);

    }

    public void notifyChanged(){
        adapter.notifyDataSetChanged();
    }

    public ArrayList<WordSet> loadWordSets(){
        ArrayList<WordSet> list=new ArrayList<>();
        try {
            VocaDB db=new VocaDB( this.getContext());
            db.open();
            list=db.getAll_WordSet();

            boolean first=false;
            if(list.size()==0){
                WordSet wordSet=ApplicationClass.list.get(0);
                long idx=db.createWordSetEntry(wordSet.getTitle(),wordSet.getCategory());
                for (Voca item: wordSet.getVocaList()) {
                    long idx2=db.createVocaEntry(idx,item.getWord(), item.getMean());
                    Log.d("idx",String.valueOf(idx2));
                }
                first=true;
            }
            db.close();

            if(first){
               MainActivity main= (MainActivity)this.getActivity();
               main.refreshPage();
            }
            //Toast.makeText(this.getContext(),"Successfully get Data!!", Toast.LENGTH_SHORT).show();

        }
        catch ( SQLException e){
            Toast.makeText(this.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        return  list;
    }



}
