package com.fiuba.tdp.linkup.views;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiuba.tdp.linkup.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommingSoonFragment extends Fragment {


    public CommingSoonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comming_soon, container, false);
    }

}
