package com.fiuba.tdp.linkup.views;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.components.NewMatchFragment;
import com.fiuba.tdp.linkup.domain.LinkUpMatch;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchsFragment extends Fragment implements NewMatchFragment.OnNewMatchListFragmentInteractionListener {


    public MatchsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_matchs, container, false);
    }

    @Override
    public void onListFragmentInteraction(LinkUpMatch item) {
        Log.e("NEW MATCH", "CLICKED ON NEW MATCH!!");
    }
}
