package com.fiuba.tdp.linkup;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.fiuba.tdp.linkup.services.LocationManager;
import com.fiuba.tdp.linkup.services.UserManager;
import com.fiuba.tdp.linkup.services.UserService;
import com.fiuba.tdp.linkup.util.UserDoesNotHaveFacebookPicture;
import com.fiuba.tdp.linkup.util.UserIsNotOldEnoughException;
import com.fiuba.tdp.linkup.views.FirstSignUpActivity;
import com.fiuba.tdp.linkup.views.MainLinkUpActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fiuba.tdp.linkup.services.LocationManager.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

public class LogInActivity extends AppCompatActivity {

    private static final String LOCATION_TAG = "login location";

    LocationManager locationManager = new LocationManager();
    
    Profile profile;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager.getLocationPermission(this);
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
        loginButton.setReadPermissions("user_birthday", "user_education_history", "user_hometown", "user_likes", "user_photos");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (!loginResult.getRecentlyDeniedPermissions().isEmpty()) {
                    showAlert("Debes aceptar todos los permisos solicitados de tu información de Facebook para usar esta app");
                    LoginManager.getInstance().logOut();
                    return;
                }
                profile = Profile.getCurrentProfile();

                // nextActivity(profile); Como se crea otra actividad para el login, luego se llama al on resume y llama a nextActivity desde ahi
            }

            @Override
            public void onCancel() {
                showAlert("Debes aceptar todos los permisos solicitados de tu información de Facebook para usar esta app");
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onError(FacebookException e) {
                showAlert("Ha habido un error al comunicarse con Facebook. Por favor intenta mas tarde");
                LoginManager.getInstance().logOut();
            }
        });
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
                    locationManager.getDeviceLocation(this);
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

            final String profilePicture = profile.getProfilePictureUri(300, 300).toString();

            new UserService().getUser(profile.getId(), new Callback<ServerResponse<LinkUpUser>>() {
                @Override
                public void onResponse(Call<ServerResponse<LinkUpUser>> call, Response<ServerResponse<LinkUpUser>> response) {
                    if (response.isSuccessful()) {
                        UserManager.getInstance().setMyUser(response.body().data);
                        if (locationManager.getLastKnownLocation() != null) {
                            Log.d(LOCATION_TAG, "set location");
                            LocationUser userLoc = new LocationUser(locationManager.getLastKnownLocation().getLatitude(), locationManager.getLastKnownLocation().getLongitude());
                            new UserService().putLocation(profile.getId(), userLoc);
                        } else {
                            Log.d(LOCATION_TAG, "set default location");
                            LocationUser userLoc = new LocationUser(-34.59,-58.41);
                            new UserService().putLocation(profile.getId(), userLoc);
                        }

                        Intent main = new Intent(getBaseContext(), MainLinkUpActivity.class);
                        startActivity(main);
                        finish();
                    } else if (response.code() == 404) {
                        new UserService().postActualFacebookUser(name, profilePicture, new Callback<ServerResponse<LinkUpUser>>() {
                            @Override
                            public void onResponse(Call<ServerResponse<LinkUpUser>> call, Response<ServerResponse<LinkUpUser>> response) {
                                Log.e("LINKUP SERVER", "POSTED USER TO LINK UP SERVERS");

                                UserManager.getInstance().setMyUser(response.body().data);
                                if (locationManager.getLastKnownLocation() != null) {
                                    Log.d(LOCATION_TAG, "set location");
                                    LocationUser userLoc = new LocationUser(locationManager.getLastKnownLocation().getLatitude(), locationManager.getLastKnownLocation().getLongitude());
                                    //profile = Profile.getCurrentProfile();
                                    new UserService().putLocation(profile.getId(), userLoc);
                                } else {
                                    Log.d(LOCATION_TAG, "set default location");
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
                                    LoginManager.getInstance().logOut();
                                    return;
                                }

                                if (t.getClass() == UserDoesNotHaveFacebookPicture.class) {
                                    //tiene menos de 18 anos
                                    Log.e("LOGIN ACTIVITY SERVER", "TIENE MENOS DE 18 ANOS");
                                    showAlert("Debes tener una foto de perfil en Facebook para usar esta app, vuelva mas tarde");
                                    LoginManager.getInstance().logOut();
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
            }
        });

        // 4. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        dialog.show();

    }



}
