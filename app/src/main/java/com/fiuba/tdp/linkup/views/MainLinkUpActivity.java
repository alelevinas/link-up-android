package com.fiuba.tdp.linkup.views;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.components.InterestsFragment;
import com.fiuba.tdp.linkup.domain.facebook.FacebookUserItem;

public class MainLinkUpActivity extends AppCompatActivity implements ProfileFragment.OnFragmentInteractionListener,
        InterestsFragment.OnListFragmentInteractionListener{

    private Fragment exploreFragment = new ExploreFragment();
    private Fragment profileFragment = new FirstSignUpActivityFragment();

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
                pushFragment(profileFragment);
                return true;
            case R.id.navigation_messages:
                pushFragment(profileFragment);
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
}
