package com.fiuba.tdp.linkup.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.components.InterestsFragment;
import com.fiuba.tdp.linkup.domain.FacebookUserItem;

public class FirstSignUpActivity extends AppCompatActivity implements InterestsFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button button = (Button) findViewById(R.id.done);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfile();
            }
        });
    }


    @Override
    public void onListFragmentInteraction(FacebookUserItem.Like item) {
        return;
    }

    private void saveProfile() {
        Intent main = new Intent(this, MainLinkUpActivity.class);
        startActivity(main);
        finish();
    }
}
