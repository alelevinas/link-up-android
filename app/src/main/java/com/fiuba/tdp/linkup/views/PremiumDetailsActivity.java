package com.fiuba.tdp.linkup.views;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fiuba.tdp.linkup.R;

public class PremiumDetailsActivity extends AppCompatActivity implements PremiumDetailsActivityFragment.OnPremiumDetailsActivityFragmentInteractionListener,
        PremiumPaymentFragment.OnPremiumPaymentFragmentInteractionListener {

    private Fragment detailsFragment = new PremiumDetailsActivityFragment();
    private Fragment paymentFragment = new PremiumPaymentFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pushFragment(detailsFragment);
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
    public void onOnPremiumDetailsActivityFragmentInteractionListenerInteraction() {
        pushFragment(paymentFragment);
    }

    @Override
    public void onPremiumPaymentFragmentInteraction() {
        finish();
    }
}
