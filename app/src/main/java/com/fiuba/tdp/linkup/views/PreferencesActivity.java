package com.fiuba.tdp.linkup.views;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.facebook.Profile;
import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.domain.ServerResponse;
import com.fiuba.tdp.linkup.domain.UserPreferences;
import com.fiuba.tdp.linkup.services.UserManager;
import com.fiuba.tdp.linkup.services.UserService;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreferencesActivity extends AppCompatActivity {

    private ImageView loader;
    private ScrollView scrollview;
    private Button linkUpPlusButton;
    private Switch likeMenSwitch;
    private Switch likeWomenSwitch;
    private CrystalSeekbar distanceRangeSeekBar;
    private CrystalRangeSeekbar ageRangeSeekBar;
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

        userService = new UserService(getBaseContext());

        linkUpPlusButton = (Button) findViewById(R.id.txt_linkup_plus);
        linkUpPlusButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Obtener LinkUp Plus!", Snackbar.LENGTH_LONG).show();
            }
        });

        likeMenSwitch = (Switch) findViewById(R.id.switch_men);
        likeWomenSwitch = (Switch) findViewById(R.id.switch_women);

        distanceRangeSeekBar = (CrystalSeekbar) findViewById(R.id.seekbar_distance);
        // get min and max text view
//        final TextView tvMinDist = (TextView) findViewById(R.id.textMinDistance);
        final TextView tvMaxDist = (TextView) findViewById(R.id.textMaxDistance);

        tvMaxDist.setX(distanceRangeSeekBar.getLeftThumbRect().left + 20);

        // set listener
        distanceRangeSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue) {
                tvMaxDist.setText(String.valueOf(minValue));
//                tvMaxDist.setX(distanceRangeSeekBar.getLeftThumbRect().left + 20);
            }
        });

        ageRangeSeekBar = (CrystalRangeSeekbar) findViewById(R.id.rgseekbar_age);
        // get min and max text view
        final TextView tvMinAge = (TextView) findViewById(R.id.textMinAge);
        final TextView tvMaxAge = (TextView) findViewById(R.id.textMaxAge);

        ageRangeSeekBar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMinAge.setText(String.valueOf(minValue));
                tvMaxAge.setText(String.valueOf(maxValue));
            }
        });


        int userAge = Integer.parseInt(UserManager.getInstance().getMyUser().getAge());
        if(userAge <= 23)
            ageRangeSeekBar.setLeft(18);
        else
            ageRangeSeekBar.setLeft(userAge-5);
        ageRangeSeekBar.setRight(userAge+5);

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

        loader = (ImageView) findViewById(R.id.loader);
        scrollview = (ScrollView) findViewById(R.id.scrollview);

        startLoader();
    }

    private void startLoader() {
        scrollview.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
        loader.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_indefinitely) );
    }

    private void stopLoader() {
        loader.setVisibility(View.GONE);
        loader.clearAnimation();
        scrollview.setVisibility(View.VISIBLE);
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
                distanceRangeSeekBar.setMinStartValue(Integer.parseInt(userPreferences.getDistance())).apply();

//                final TextView tvMaxDist = (TextView) findViewById(R.id.textMaxDistance);
//                tvMaxDist.setX(distanceRangeSeekBar.getLeftThumbRect().left + 20);

                ageRangeSeekBar.setMinStartValue(Integer.parseInt(userPreferences.getMinAge()))
                        .setMaxStartValue(Integer.parseInt(userPreferences.getMaxAge()))
                        .apply();

                invisibleSwitch.setChecked(Objects.equals(userPreferences.getMode(),"invisible"));

                if(Objects.equals(userPreferences.getSearchMode(), "couple"))
                    radioRelationship.setChecked(true);
                else
                    radioFriendship.setChecked(true);

                stopLoader();
            }

            @Override
            public void onFailure(Call<ServerResponse<UserPreferences>> call, Throwable t) {
                Log.w("Preferences Activity", "Error: "+t.toString()+", in call: "+call.toString());
                showAlertAndFinish("Ha habido un error al comunicarse con nuestros servidores. Por favor intenta más tarde");
            }
        });
    }

    @Override
    protected void onStop() {
        startLoader();
        postPreferences();
        stopLoader();
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
        String distance = distanceRangeSeekBar.getSelectedMinValue().toString();
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

    private void showAlertAndFinish(String s) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(s)
                .setTitle("Atención");

        // 3. Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                dialog.dismiss();
                finish();
            }
        });

        // 4. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        dialog.show();
    }
}
