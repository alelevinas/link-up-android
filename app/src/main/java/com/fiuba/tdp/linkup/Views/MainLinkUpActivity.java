package com.fiuba.tdp.linkup.Views;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.fiuba.tdp.linkup.R;

public class MainLinkUpActivity extends AppCompatActivity implements ProfileFragment.OnFragmentInteractionListener {

    private TextView mTextMessage;


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

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Bundle inBundle = getIntent().getExtras();
        String name = inBundle.get("name").toString();
        String surname = inBundle.get("surname").toString();
        String imageUrl = inBundle.get("imageUrl").toString();

//        TextView nameView = (TextView)findViewById(R.id.nameAndSurname);
//        nameView.setText("" + name + " " + surname);
//
//        new DownloadImage((ImageView)findViewById(R.id.profileImage)).execute(imageUrl);


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
//                mTextMessage.setText(R.string.title_home);
                pushFragment(new ExploreFragment());
                return true;
            case R.id.navigation_notifications:
//                mTextMessage.setText(R.string.title_dashboard);
                pushFragment(ProfileFragment.newInstance("hola", "mundo"));
                return true;
            case R.id.navigation_messages:
//                mTextMessage.setText(R.string.title_notifications);
                pushFragment(ProfileFragment.newInstance("hola", "mundo"));
                return true;
            case R.id.navigation_profile:
//                mTextMessage.setText(R.string.title_notifications);
                pushFragment(ProfileFragment.newInstance("hola", "mundo"));
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
        return;
    }
}
