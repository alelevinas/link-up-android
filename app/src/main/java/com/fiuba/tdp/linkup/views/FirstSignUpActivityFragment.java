package com.fiuba.tdp.linkup.views;

import android.content.Intent;
import android.app.Fragment;
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
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_sign_up, container, false);

        Button buttonEditInfo = (Button) view.findViewById(R.id.button_edit_info);

        buttonEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editInfo();
            }
        });


        return view;
    }

    private void editInfo() {
        Intent main = new Intent(getActivity(), EditInfoActivity.class);
        startActivity(main);
    }
}
