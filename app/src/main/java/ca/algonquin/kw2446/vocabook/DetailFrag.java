package ca.algonquin.kw2446.vocabook;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.ListFragment;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFrag extends Fragment {


    public interface VocaItemClicked{
        void onVocaItemClicked(int i);
        ArrayList<Voca> onGetList();
    }

    ListView lvWords;
    TextView tvBig;
    View v;
    VocaAdapter vocaAdapter;
    VocaItemClicked activity;







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

        lvWords=v.findViewById(R.id.lvWords);

        vocaAdapter=new VocaAdapter(this.getContext(),activity.onGetList());
        lvWords.setAdapter(vocaAdapter);

        lvWords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                   activity.onVocaItemClicked(position);
            }
        });

    }






}
