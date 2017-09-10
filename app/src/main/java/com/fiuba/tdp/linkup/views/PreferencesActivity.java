package com.fiuba.tdp.linkup.views;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.fiuba.tdp.linkup.R;

import org.florescu.android.rangeseekbar.RangeSeekBar;

public class PreferencesActivity extends AppCompatActivity {

    private Button linkUpPlusButton;
    private Switch likeMenSwitch;
    private Switch likeWomenSwitch;
    private RangeSeekBar distanceRangeSeekBar;
    private RangeSeekBar ageRangeSeekBar;
    private Switch invisibleSwitch;
    private RadioGroup searchingRadioGroup;
    private Button deleteAccountButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        linkUpPlusButton = (Button) findViewById(R.id.txt_linkup_plus);
        linkUpPlusButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Obtener LinkUp Plus!", Snackbar.LENGTH_LONG).show();
            }
        });

        likeMenSwitch = (Switch) findViewById(R.id.switch_men);
        likeWomenSwitch = (Switch) findViewById(R.id.switch_women);

        distanceRangeSeekBar = (RangeSeekBar) findViewById(R.id.seekbar_distance);
        distanceRangeSeekBar.setSelectedMaxValue(10);

        ageRangeSeekBar = (RangeSeekBar) findViewById(R.id.rgseekbar_age);

        invisibleSwitch = (Switch) findViewById(R.id.switch_invisible);

        searchingRadioGroup = (RadioGroup) findViewById(R.id.friend_relation_radiogroup);

        deleteAccountButton = (Button) findViewById(R.id.button_delete);

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postPreferences();
            }
        });
    }

    public void postPreferences() {
        System.out.print("Me interesan: ");
        if(likeMenSwitch.isChecked())
            System.out.print(" hombres ");
        if(likeWomenSwitch.isChecked())
            System.out.print(" mujeres ");
        System.out.println("");

        System.out.println("Distancia maxima: " + distanceRangeSeekBar.getSelectedMaxValue());

        System.out.println("Rango edad: " + ageRangeSeekBar.getSelectedMinValue() + "-" + ageRangeSeekBar.getSelectedMaxValue());

        System.out.println("Modo invisible: " + invisibleSwitch.isChecked());

        System.out.print("Estoy buscando ");
        if(searchingRadioGroup.getCheckedRadioButtonId() == R.id.relationship)
            System.out.println("una pareja");
        else
            System.out.println("amistad");
    }
}
