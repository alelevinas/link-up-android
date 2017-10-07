package com.fiuba.tdp.linkup.views;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiuba.tdp.linkup.R;

public class OtherProfileActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "position";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_profile_detail);
        //setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Set Collapsing Toolbar layout to the screen
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        // Set title of Detail page
        // collapsingToolbar.setTitle(getString(R.string.item_title));

        int postion = getIntent().getIntExtra(EXTRA_POSITION, 0);
        Resources resources = getResources();
        collapsingToolbar.setTitle("title");

        TextView placeDetail = (TextView) findViewById(R.id.place_detail);
        placeDetail.setText("holas");

        TextView placeLocation =  (TextView) findViewById(R.id.place_location);
        placeLocation.setText("chau");

        ImageView placePicutre = (ImageView) findViewById(R.id.image);
        placePicutre.setImageDrawable(null);
    }
}
