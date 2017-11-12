package com.fiuba.tdp.linkup.views;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.components.InterestsFragment;
import com.fiuba.tdp.linkup.components.NewMatchFragment;
import com.fiuba.tdp.linkup.domain.LinkUpMatch;
import com.fiuba.tdp.linkup.domain.facebook.FacebookUserItem;
import com.fiuba.tdp.linkup.services.UserManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainLinkUpActivity extends AppCompatActivity implements ProfileFragment.OnFragmentInteractionListener,
        InterestsFragment.OnListFragmentInteractionListener, NewMatchFragment.OnNewMatchListFragmentInteractionListener {

    private Fragment exploreFragment = new ExploreFragment();
    private Fragment profileFragment = new FirstSignUpActivityFragment();
    private Fragment matchesFragment = new MatchsFragment();
    private Fragment comingSoon = new CommingSoonFragment();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return selectFragment(item);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_link_up);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        saveFireBaseToken();
        UserManager.getInstance().updateMyBlockedUsers(this);
        pushFragment(exploreFragment);
    }

    /**
     * Perform action when any item is selected.
     *
     * @param item Item that is selected.
     */
    protected boolean selectFragment(MenuItem item) {

        item.setChecked(true);

        switch (item.getItemId()) {
            case R.id.navigation_discover:
                pushFragment(exploreFragment);
                return true;
            case R.id.navigation_notifications:
                pushFragment(comingSoon);
                return true;
            case R.id.navigation_messages:
                pushFragment(matchesFragment);
                return true;
            case R.id.navigation_profile:
                pushFragment(profileFragment);
                return true;
        }
        return false;
    }

    /**
     * Method to push any fragment into given id.
     *
     * @param fragment An instance of Fragment to show into the given id.
     */
    protected void pushFragment(Fragment fragment) {
        if (fragment == null)
            return;

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.rootLayout, fragment);
                ft.commit();
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void onListFragmentInteraction(FacebookUserItem.Like item) {
    }

    @Override
    public void onListFragmentInteraction(LinkUpMatch item) {
        Log.e("CHATS", "CLICKED ON NEW MATCH!!");
        Intent intentBundle = new Intent(MainLinkUpActivity.this, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ChatActivity.CHAT_WITH_USER_ID, item.getId());
        bundle.putString(ChatActivity.CHAT_WITH_USER_NAME, item.getName());
        intentBundle.putExtras(bundle);
        startActivity(intentBundle);
    }

    private void showAlert(String title, String message) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(message)
                .setTitle(title);

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

    private void saveFireBaseToken() {
        // Get token
        String token = FirebaseInstanceId.getInstance().getToken();

        // Log and toast
//        String msg = getString(R.string.msg_token_fmt, token);
        Log.d("FIREBASE INSTANCE ID", token);
//        Toast.makeText(MainLinkUpActivity.this, token, Toast.LENGTH_SHORT).show();

        DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

//        HashMap<String, String> tokenObject = new HashMap<>();
//        tokenObject.put("token", token);
//        tokenObject.put("name", UserManager.getInstance().getMyUser().getName());

        if (UserManager.getInstance().getMyUser() == null) {
            return;
        }

        mFirebaseDatabaseReference.child("users" + "/" + UserManager.getInstance().getMyUser().getId() + "/token").setValue(token);
        mFirebaseDatabaseReference.child("users" + "/" + UserManager.getInstance().getMyUser().getId() + "/name").setValue(UserManager.getInstance().getMyUser().getName());
    }

}
