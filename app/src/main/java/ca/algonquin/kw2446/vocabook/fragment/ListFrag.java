package ca.algonquin.kw2446.vocabook.fragment;


import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import ca.algonquin.kw2446.vocabook.R;
import ca.algonquin.kw2446.vocabook.db.VocaDB;
import ca.algonquin.kw2446.vocabook.db.VocaRepository;
import ca.algonquin.kw2446.vocabook.model.WordSet;
import ca.algonquin.kw2446.vocabook.adapter.WordSetAdapter;
import ca.algonquin.kw2446.vocabook.util.Utility;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFrag extends Fragment {

    ArrayList<WordSet> list;
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
        list=new ArrayList<>();
        loadWordSets();
        recyclerView=v.findViewById(R.id.rvList);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new WordSetAdapter(this.getContext(), list);
        recyclerView.setAdapter(adapter);

    }

    public void notifyChanged(){
        if(adapter!=null) adapter.notifyDataSetChanged();
    }


    public void loadWordSets(){
        ArrayList<WordSet> data=new ArrayList<>();
        try {

            data=VocaRepository.getItemList(this.getContext(), WordSet.class );

            if(data.size()==0){
                VocaRepository.addSample_WordSet(getContext());
                data=VocaRepository.getItemList(this.getContext(), WordSet.class );
            }
        }
        catch ( SQLException e){
            Toast.makeText(this.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        list.clear();
        list.addAll(data);
        this.notifyChanged();
    }



}
