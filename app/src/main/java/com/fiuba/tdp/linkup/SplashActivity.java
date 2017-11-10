package com.fiuba.tdp.linkup;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

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
import com.fiuba.tdp.linkup.views.ChatActivity;
import com.fiuba.tdp.linkup.views.LogInActivity;
import com.fiuba.tdp.linkup.views.MainLinkUpActivity;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 150;
    LocationManager locationManager = new LocationManager();

    Profile profile;
    private CallbackManager callbackManager;

    //Firebase
    private FirebaseAuth mAuth;
    private String otherUserId;
    private String otherUserName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileAds.initialize(this, "ca-app-pub-6059841271500739~5358722483");

        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();
        if (extrasBundle != null && !extrasBundle.isEmpty()) {
            otherUserId = extrasBundle.getString(ChatActivity.CHAT_WITH_USER_ID, "");
            otherUserName = extrasBundle.getString(ChatActivity.CHAT_WITH_USER_NAME, "");
        } else {
            otherUserId = "";
            otherUserName = "";
        }

        setContentView(R.layout.activity_splash);

        // Location
        locationManager.getDeviceLocation(this);

        // Firebase
        mAuth = FirebaseAuth.getInstance();


        // Facebook
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


    private void updateWithToken(final AccessToken currentAccessToken) {
        if (currentAccessToken != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    profile = Profile.getCurrentProfile();
                    firebaseAuthenticate(currentAccessToken);
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
        new UserService(getBaseContext()).getUser(profile.getId(), new Callback<ServerResponse<LinkUpUser>>() {
            @Override
            public void onResponse(Call<ServerResponse<LinkUpUser>> call, Response<ServerResponse<LinkUpUser>> response) {
                if (response.isSuccessful()) {
                    UserManager.getInstance().setMyUser(response.body().data);
                    UserManager.getInstance().updateMyBlockedUsers(getBaseContext());

                    if (locationManager.getLastKnownLocation() != null) {
                        LocationUser userLoc = new LocationUser(locationManager.getLastKnownLocation().getLatitude(), locationManager.getLastKnownLocation().getLongitude());
                        new UserService(getBaseContext()).putLocation(profile.getId(), userLoc);
                    } else {
                        LocationUser userLoc = new LocationUser(-34.59, -58.41);
                        new UserService(getBaseContext()).putLocation(profile.getId(), userLoc);
                    }


                    LinkUpUser me = UserManager.getInstance().getMyUser();
                    if (me.isDisable()) {
                        showAlertAndExit("Has sido bloqueado por el administrador. Consultas a preguntas@linkup.com");
                        return;
                    }

                    if (otherUserId.compareTo("") != 0) {
                        Intent intent = new Intent(SplashActivity.this, ChatActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(ChatActivity.CHAT_WITH_USER_ID, otherUserId);
                        bundle.putString(ChatActivity.CHAT_WITH_USER_NAME, otherUserName);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent main = new Intent(getBaseContext(), MainLinkUpActivity.class);
                        startActivity(main);
                        finish();
                    }
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

    private void firebaseAuthenticate(AccessToken token) {
        final String TAG = "FIREBASE AUTH";
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
//        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SplashActivity.this, "Firebase Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // [START_EXCLUDE]
//                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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

        LoginManager.getInstance().logOut();
    }
}