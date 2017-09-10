package com.fiuba.tdp.linkup.views;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

        View view = inflater.inflate(R.layout.fragment_first_sign_up, container, false);

        Button btn_preferences = (Button) view.findViewById(R.id.button_preferences);
        btn_preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent preferences = new Intent(view.getContext(), PreferencesActivity.class);
                startActivity(preferences);
            }
        });

        return view;
    }
}
