package com.fiuba.tdp.linkup.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiuba.tdp.linkup.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PremiumDetailsActivityFragment extends Fragment {

    public PremiumDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_premium_details, container, false);
    }
}
