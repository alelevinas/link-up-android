package com.fiuba.tdp.linkup.views;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiuba.tdp.linkup.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class FirstSignUpActivityFragment extends Fragment {

    public FirstSignUpActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first_sign_up, container, false);
    }
}
