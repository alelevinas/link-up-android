package com.fiuba.tdp.linkup;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fiuba.tdp.linkup.domain.LinkUpUser;
import com.fiuba.tdp.linkup.domain.LocationUser;
import com.fiuba.tdp.linkup.domain.ServerResponse;
import com.fiuba.tdp.linkup.services.UserManager;
import com.fiuba.tdp.linkup.services.UserService;
import com.fiuba.tdp.linkup.util.UserDoesNotHaveFacebookPicture;
import com.fiuba.tdp.linkup.util.UserIsNotOldEnoughException;
import com.fiuba.tdp.linkup.views.FirstSignUpActivity;
import com.fiuba.tdp.linkup.views.MainLinkUpActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {

    private static final String TAG = "login location";
    Profile profile;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLocationPermission();
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
//                nextActivity(newProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        //  si ya tiene user en LinkUp! y esta logueado en facebook ir directo a la MainLinkUpActivity

        profile = Profile.getCurrentProfile();

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends", "user_birthday", "user_education_history", "user_hometown", "user_likes", "user_photos");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                profile = Profile.getCurrentProfile();
                nextActivity(profile);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
            }
        });
    }


    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    public void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
                // Construct a FusedLocationProviderClient.
                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                mLastKnownLocation = location;
                                Log.d(TAG+" lat", String.valueOf(location.getLatitude()));
                                Log.d(TAG+" long", String.valueOf(location.getLongitude()));

                                // ...
                            }
                        }
                    });
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            System.out.println("PERMISSIONS GRANTED");
            getDeviceLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        System.out.println("PERMISSIONS ??? ");
        System.out.println(requestCode);


        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    System.out.println("PERMISSIONS GRANTED 2");
                    getDeviceLocation();
                }
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //Facebook login
        profile = Profile.getCurrentProfile();
        nextActivity(profile);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
        //Facebook login
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        //Facebook login
        callbackManager.onActivityResult(requestCode, responseCode, intent);
    }

    private void nextActivity(final Profile profile) {
        if (profile != null) {
            // si ya tiene usuario en LinkUp! ir directo a la MainLinkUpActivity

            final String name = profile.getName();

            new UserService().getUser(profile.getId(), new Callback<ServerResponse<LinkUpUser>>() {
                @Override
                public void onResponse(Call<ServerResponse<LinkUpUser>> call, Response<ServerResponse<LinkUpUser>> response) {
                    if (response.isSuccessful()) {
                        UserManager.getInstance().setMyUser(response.body().data);
                        if (mLastKnownLocation != null) {
                            Log.d(TAG, "set location");
                            LocationUser userLoc = new LocationUser(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                            new UserService().putLocation(profile.getId(), userLoc);
                        } else {
                            Log.d(TAG, "set default location");
                            LocationUser userLoc = new LocationUser(-34.59,-58.41);
                            new UserService().putLocation(profile.getId(), userLoc);
                        }

                        Intent main = new Intent(getBaseContext(), MainLinkUpActivity.class);
                        startActivity(main);
                        finish();
                    } else if (response.code() == 404) {
                        new UserService().postActualFacebookUser(name, new Callback<ServerResponse<LinkUpUser>>() {
                            @Override
                            public void onResponse(Call<ServerResponse<LinkUpUser>> call, Response<ServerResponse<LinkUpUser>> response) {
                                Log.e("LINKUP SERVER", "POSTED USER TO LINK UP SERVERS");

                                UserManager.getInstance().setMyUser(response.body().data);
                                if (mLastKnownLocation != null) {
                                    Log.d(TAG, "set location");
                                    LocationUser userLoc = new LocationUser(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                                    //profile = Profile.getCurrentProfile();
                                    new UserService().putLocation(profile.getId(), userLoc);
                                } else {
                                    Log.d(TAG, "set default location");
                                    LocationUser userLoc = new LocationUser(-34.59,-58.41);
                                    //profile = Profile.getCurrentProfile();
                                    new UserService().putLocation(profile.getId(), userLoc);
                                }

                                Intent main = new Intent(getBaseContext(), FirstSignUpActivity.class);
                                startActivity(main);
                                finish();
                            }

                            @Override
                            public void onFailure(Call<ServerResponse<LinkUpUser>> call, Throwable t) {
                                if (t.getClass() == UserIsNotOldEnoughException.class) {
                                    //tiene menos de 18 anos
                                    Log.e("LOGIN ACTIVITY SERVER", "TIENE MENOS DE 18 ANOS");
                                    showAlert("El usuario es menor a 18 años, vuelva mas tarde");
                                    return;
                                }

                                if (t.getClass() == UserDoesNotHaveFacebookPicture.class) {
                                    //tiene menos de 18 anos
                                    Log.e("LOGIN ACTIVITY SERVER", "TIENE MENOS DE 18 ANOS");
                                    showAlert("Debes tener una foto de perfil en Facebook para usar esta app, vuelva mas tarde");
                                    return;
                                }
                                Log.e("LOGIN ACTIVITY SERVER", "ERROR POSTING USER TO LINK UP SERVERS");
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<ServerResponse<LinkUpUser>> call, Throwable t) {
                    Log.e("LOGIN ACTIVITY SERVER", "ERROR GETING USER FROM LINK UP SERVERS");
                }
            });
        }
    }

    private void showAlert(String s) {
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
                LoginManager.getInstance().logOut();
            }
        });

        // 4. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        dialog.show();

    }



}
