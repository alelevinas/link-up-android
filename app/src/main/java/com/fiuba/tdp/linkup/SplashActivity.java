package com.fiuba.tdp.linkup;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.fiuba.tdp.linkup.domain.LinkUpUser;
import com.fiuba.tdp.linkup.domain.LocationUser;
import com.fiuba.tdp.linkup.domain.ServerResponse;
import com.fiuba.tdp.linkup.services.LocationManager;
import com.fiuba.tdp.linkup.services.UserManager;
import com.fiuba.tdp.linkup.services.UserService;
import com.fiuba.tdp.linkup.views.LogInActivity;
import com.fiuba.tdp.linkup.views.MainLinkUpActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 1500;
    LocationManager locationManager = new LocationManager();

    Profile profile;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();

        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
//                updateWithToken(newAccessToken);
            }
        };
        ProfileTracker profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
//                nextActivity(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        updateWithToken(AccessToken.getCurrentAccessToken());

    }


    private void updateWithToken(AccessToken currentAccessToken) {
        if (currentAccessToken != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    profile = Profile.getCurrentProfile();
                    handleFacebookAlreadyLoggedIn();
                }
            }, SPLASH_TIME_OUT);
        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent i = new Intent(SplashActivity.this, LogInActivity.class);
                    startActivity(i);

                    finish();

                }
            }, SPLASH_TIME_OUT);
        }
    }

    private void handleFacebookAlreadyLoggedIn() {
        new UserService().getUser(profile.getId(), new Callback<ServerResponse<LinkUpUser>>() {
            @Override
            public void onResponse(Call<ServerResponse<LinkUpUser>> call, Response<ServerResponse<LinkUpUser>> response) {
                if (response.isSuccessful()) {
                    UserManager.getInstance().setMyUser(response.body().data);
                    if (locationManager.getLastKnownLocation() != null) {
                        LocationUser userLoc = new LocationUser(locationManager.getLastKnownLocation().getLatitude(), locationManager.getLastKnownLocation().getLongitude());
                        new UserService().putLocation(profile.getId(), userLoc);
                    } else {
                        LocationUser userLoc = new LocationUser(-34.59, -58.41);
                        new UserService().putLocation(profile.getId(), userLoc);
                    }

                    Intent main = new Intent(getBaseContext(), MainLinkUpActivity.class);
                    startActivity(main);
                    finish();
                } else if (response.code() == 404) {
                    LoginManager.getInstance().logOut();
                    Intent main = new Intent(getBaseContext(), LogInActivity.class);
                    startActivity(main);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<LinkUpUser>> call, Throwable t) {
                LoginManager.getInstance().logOut();
                Intent main = new Intent(getBaseContext(), LogInActivity.class);
                startActivity(main);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}