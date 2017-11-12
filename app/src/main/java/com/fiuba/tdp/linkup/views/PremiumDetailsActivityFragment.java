package com.fiuba.tdp.linkup.views;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fiuba.tdp.linkup.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PremiumDetailsActivityFragment extends Fragment {

    private OnPremiumDetailsActivityFragmentInteractionListener mListener;

    public PremiumDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_premium_details, container, false);

        Button button_continue = (Button) v.findViewById(R.id.button_continue);

        button_continue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mListener.onOnPremiumDetailsActivityFragmentInteractionListenerInteraction();
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPremiumDetailsActivityFragmentInteractionListener) {
            mListener = (OnPremiumDetailsActivityFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPremiumDetailsActivityFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnPremiumDetailsActivityFragmentInteractionListener {
        void onOnPremiumDetailsActivityFragmentInteractionListenerInteraction();
    }
}
