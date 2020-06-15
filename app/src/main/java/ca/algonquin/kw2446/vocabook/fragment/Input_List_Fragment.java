package ca.algonquin.kw2446.vocabook.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.algonquin.kw2446.vocabook.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Input_List_Fragment extends Fragment {


    public Input_List_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input__list, container, false);
    }

}