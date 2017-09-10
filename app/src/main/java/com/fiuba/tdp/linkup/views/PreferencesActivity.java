package com.fiuba.tdp.linkup.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.fiuba.tdp.linkup.R;

import org.florescu.android.rangeseekbar.RangeSeekBar;

public class PreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RangeSeekBar rangeSeekBarDistance = (RangeSeekBar) findViewById(R.id.seekbar_distance);
        rangeSeekBarDistance.setSelectedMaxValue(10);


        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });*/
    }
}
