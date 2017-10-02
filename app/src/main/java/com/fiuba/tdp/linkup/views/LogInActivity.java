package com.fiuba.tdp.linkup.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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
import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.components.AsyncTaskLoaders.LoginAsyncTaskLoader;
import com.fiuba.tdp.linkup.domain.LinkUpUser;
import com.fiuba.tdp.linkup.domain.LocationUser;
import com.fiuba.tdp.linkup.domain.ServerResponse;
import com.fiuba.tdp.linkup.services.LocationManager;
import com.fiuba.tdp.linkup.services.UserManager;
import com.fiuba.tdp.linkup.services.UserService;
import com.fiuba.tdp.linkup.util.UserDoesNotHaveFacebookPicture;
import com.fiuba.tdp.linkup.util.UserIsNotOldEnoughException;

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
    private View loginView;
    private ImageView loader;

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

        accessTokenTracker.startTracking();
        profile = Profile.getCurrentProfile();

        loginView = (View) findViewById(R.id.include);
        loader = (ImageView) findViewById(R.id.loader);
        loader.setVisibility(View.GONE);

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile", "user_birthday", "user_education_history", "user_hometown", "user_likes", "user_photos");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoader();
            }
        });
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            private ProfileTracker profileTracker;

            @Override
            public void onSuccess(final LoginResult loginResult) {
                if(Profile.getCurrentProfile() == null) {
                    profileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                            if (!loginResult.getRecentlyDeniedPermissions().isEmpty()) {
                                showAlert("Debes aceptar todos los permisos solicitados de tu información de Facebook para usar esta app");
                                LoginManager.getInstance().logOut();
                                return;
                            }
                            profileTracker.stopTracking();
                            nextActivity(newProfile);
                        }
                    };
                } else {
                    if (!loginResult.getRecentlyDeniedPermissions().isEmpty()) {
                        showAlert("Debes aceptar todos los permisos solicitados de tu información de Facebook para usar esta app");
                        LoginManager.getInstance().logOut();
                        return;
                    }
                    profile = Profile.getCurrentProfile();
                    nextActivity(profile);
                }
            }

            @Override
            public void onCancel() {
                showAlert("Debes aceptar todos los permisos solicitados de tu información de Facebook para usar esta app");
                LoginManager.getInstance().logOut();
                stopLoader();
            }

            @Override
            public void onError(FacebookException e) {
                showAlert("Ha habido un error al comunicarse con Facebook. Por favor intenta mas tarde");
                LoginManager.getInstance().logOut();
                stopLoader();
            }
        });
    }

    private void startLoader() {
        loginView.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
    }

    private void stopLoader() {
        loader.setVisibility(View.GONE);
        loginView.setVisibility(View.VISIBLE);
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
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
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
        //Facebook login
        accessTokenTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);

        // if you don't add following block,
        // your registered `FacebookCallback` won't be called
        if (callbackManager.onActivityResult(requestCode, responseCode, intent)) {
            return;
        }
    }

    private void nextActivity(final Profile profile) {
        if (profile != null) {
            // si ya tiene usuario en LinkUp! ir directo a la MainLinkUpActivity

            final String name = profile.getName();

            final String profilePicture = profile.getProfilePictureUri(500, 500).toString();

            new UserService(getBaseContext()).getUser(profile.getId(), new Callback<ServerResponse<LinkUpUser>>() {
                @Override
                public void onResponse(Call<ServerResponse<LinkUpUser>> call, Response<ServerResponse<LinkUpUser>> response) {
                    if (response.isSuccessful()) {
                        UserManager.getInstance().setMyUser(response.body().data);
                        if (locationManager.getLastKnownLocation() != null) {
                            Log.d(LOCATION_TAG, "set location");
                            LocationUser userLoc = new LocationUser(locationManager.getLastKnownLocation().getLatitude(), locationManager.getLastKnownLocation().getLongitude());
                            new UserService(getBaseContext()).putLocation(profile.getId(), userLoc);
                        } else {
                            Log.d(LOCATION_TAG, "set default location");
                            LocationUser userLoc = new LocationUser(-34.59,-58.41);
                            new UserService(getBaseContext()).putLocation(profile.getId(), userLoc);
                        }

                        Intent main = new Intent(getBaseContext(), MainLinkUpActivity.class);
                        startActivity(main);
                        finish();
                    } else if (response.code() == 404) {
                        new UserService(getBaseContext()).postActualFacebookUser(name, profilePicture, new Callback<ServerResponse<LinkUpUser>>() {
                            @Override
                            public void onResponse(Call<ServerResponse<LinkUpUser>> call, Response<ServerResponse<LinkUpUser>> response) {
                                Log.e("LINKUP SERVER", "POSTED USER TO LINK UP SERVERS");

                                UserManager.getInstance().setMyUser(response.body().data);
                                if (locationManager.getLastKnownLocation() != null) {
                                    Log.d(LOCATION_TAG, "set location");
                                    LocationUser userLoc = new LocationUser(locationManager.getLastKnownLocation().getLatitude(), locationManager.getLastKnownLocation().getLongitude());
                                    new UserService(getBaseContext()).putLocation(profile.getId(), userLoc);
                                } else {
                                    Log.d(LOCATION_TAG, "set default location");
                                    LocationUser userLoc = new LocationUser(-34.59,-58.41);
                                    new UserService(getBaseContext()).putLocation(profile.getId(), userLoc);
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
                                    stopLoader();
                                    return;
                                }

                                if (t.getClass() == UserDoesNotHaveFacebookPicture.class) {
                                    //tiene menos de 18 anos
                                    Log.e("LOGIN ACTIVITY SERVER", "TIENE MENOS DE 18 ANOS");
                                    showAlert("Debes tener una foto de perfil en Facebook para usar esta app, vuelva mas tarde");
                                    LoginManager.getInstance().logOut();
                                    stopLoader();
                                    return;
                                }

                                stopLoader();
                                Log.e("LOGIN ACTIVITY SERVER", "ERROR POSTING USER TO LINK UP SERVERS");
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<ServerResponse<LinkUpUser>> call, Throwable t) {
                    Log.e("LOGIN ACTIVITY SERVER", "ERROR GETING USER FROM LINK UP SERVERS");
                    showAlertAndExit("Ha habido un error al comunicarse con nuestros servidores. Por favor intenta mas tarde");
                    stopLoader();
                }
            });
        }
    }

    private void showAlertAndExit(String message) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(message)
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
