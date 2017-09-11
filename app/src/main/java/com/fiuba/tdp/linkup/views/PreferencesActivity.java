package com.fiuba.tdp.linkup.views;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.facebook.Profile;
import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.domain.ServerResponse;
import com.fiuba.tdp.linkup.domain.UserAround;
import com.fiuba.tdp.linkup.domain.UsersAround;
import com.fiuba.tdp.linkup.services.UserService;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.util.ArrayList;
import java.util.Collection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreferencesActivity extends AppCompatActivity {

    private Button linkUpPlusButton;
    private Switch likeMenSwitch;
    private Switch likeWomenSwitch;
    private RangeSeekBar distanceRangeSeekBar;
    private RangeSeekBar ageRangeSeekBar;
    private Switch invisibleSwitch;
    private RadioGroup searchingRadioGroup;
    private Button deleteAccountButton;
    private UserService userService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userService = new UserService();

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
                Snackbar.make(view, "Eliminar cuenta!!!",
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStop() {
        postPreferences();
        super.onStop();
    }

    public void postPreferences() {
        String userId = Profile.getCurrentProfile().getId();
        String gender;
        if(likeMenSwitch.isChecked() && likeWomenSwitch.isChecked())
            gender = "both";
        else
            if(likeWomenSwitch.isChecked())
                gender = "female";
            else
                gender = "male";
        String distance = distanceRangeSeekBar.getSelectedMaxValue().toString();
        String minAge = ageRangeSeekBar.getSelectedMinValue().toString();
        String maxAge = ageRangeSeekBar.getSelectedMaxValue().toString();
        String mode = invisibleSwitch.isChecked() ? "invisible" : "visible";
        String searchMode;
        if(searchingRadioGroup.getCheckedRadioButtonId() == R.id.relationship)
            searchMode = "couple";
        else
            searchMode = "friendship";

        userService.postPreferences(userId, gender, distance, minAge, maxAge, mode, searchMode);
    }
}
