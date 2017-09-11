package com.fiuba.tdp.linkup.views;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.facebook.Profile;
import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.domain.ServerResponse;
import com.fiuba.tdp.linkup.domain.UserPreferences;
import com.fiuba.tdp.linkup.services.UserService;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreferencesActivity extends AppCompatActivity {

    private Button linkUpPlusButton;
    private Switch likeMenSwitch;
    private Switch likeWomenSwitch;
    private RangeSeekBar<Integer> distanceRangeSeekBar;
    private RangeSeekBar<Integer> ageRangeSeekBar;
    private Switch invisibleSwitch;
    private RadioButton radioRelationship;
    private RadioButton radioFriendship;
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

        distanceRangeSeekBar = (RangeSeekBar<Integer>) findViewById(R.id.seekbar_distance);
        distanceRangeSeekBar.setSelectedMaxValue(10);

        ageRangeSeekBar = (RangeSeekBar<Integer>) findViewById(R.id.rgseekbar_age);

        invisibleSwitch = (Switch) findViewById(R.id.switch_invisible);

        radioFriendship = (RadioButton) findViewById(R.id.radio_friendship);
        radioRelationship = (RadioButton) findViewById(R.id.radio_relationship);

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
    protected void onResume() {
        super.onResume();
        Log.d("Preferences view", "Refreshing current values for user "+Profile.getCurrentProfile().getId());
        userService.getUserPreferences(Profile.getCurrentProfile().getId(), new Callback<ServerResponse<UserPreferences>>() {
            @Override
            public void onResponse(Call<ServerResponse<UserPreferences>> call, Response<ServerResponse<UserPreferences>> response) {
                UserPreferences userPreferences = response.body().data;
                if(Objects.equals(userPreferences.getGender(), "both")){
                    likeMenSwitch.setChecked(true);
                    likeWomenSwitch.setChecked(true);
                }
                if(Objects.equals(userPreferences.getGender(), "male")){
                    likeMenSwitch.setChecked(true);
                    likeWomenSwitch.setChecked(false);
                }
                if(Objects.equals(userPreferences.getGender(), "female")){
                    likeMenSwitch.setChecked(false);
                    likeWomenSwitch.setChecked(true);
                }
                distanceRangeSeekBar.setSelectedMaxValue(Integer.parseInt(userPreferences.getDistance()));

                ageRangeSeekBar.setSelectedMinValue(Integer.parseInt(userPreferences.getMinAge()));
                ageRangeSeekBar.setSelectedMaxValue(Integer.parseInt(userPreferences.getMaxAge()));

                invisibleSwitch.setChecked(Objects.equals(userPreferences.getMode(),"invisible"));

                if(Objects.equals(userPreferences.getSearchMode(), "couple"))
                    radioRelationship.setChecked(true);
                else
                    radioFriendship.setChecked(true);
            }

            @Override
            public void onFailure(Call<ServerResponse<UserPreferences>> call, Throwable t) {
                Log.w("Preferences Activity", "Error: "+t.toString()+", in call: "+call.toString());
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
        if(radioRelationship.isChecked())
            searchMode = "couple";
        else
            searchMode = "friendship";

        userService.postPreferences(userId, gender, distance, minAge, maxAge, mode, searchMode);
    }
}
