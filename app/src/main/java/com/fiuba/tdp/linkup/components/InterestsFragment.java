package com.fiuba.tdp.linkup.components;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.domain.LinkUpUser;
import com.fiuba.tdp.linkup.domain.facebook.FacebookUserItem;
import com.fiuba.tdp.linkup.services.UserManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class InterestsFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public InterestsFragment() {
    }

    @SuppressWarnings("unused")
    public static InterestsFragment newInstance(int columnCount) {
        InterestsFragment fragment = new InterestsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interests_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            LinkUpUser.LinkUpLike[] linkUpLikes = UserManager.getInstance().getMyUser().getLikes();

            List<LinkUpUser.LinkUpLike> likes = new ArrayList<>();
            Collections.addAll(likes, linkUpLikes);

            recyclerView.setAdapter(new MyInterestsRecyclerViewAdapter(likes, mListener));

        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(FacebookUserItem.Like item);
    }
}


/*get likes from faceboook
* new FacebookService().getUserLikes(new Callback<FacebookUserItem.FacebookLikesItem>() {
                @Override
                public void onResponse(Call<FacebookUserItem.FacebookLikesItem> call, Response<FacebookUserItem.FacebookLikesItem> response) {
                    FacebookUserItem.FacebookLikesItem likesItem = response.body();
                    FacebookUserItem.Like[] arrayLikes = likesItem.getData();

                    List<FacebookUserItem.Like> likes = new ArrayList<FacebookUserItem.Like>();
                    Collections.addAll(likes, arrayLikes);

                    recyclerView.setAdapter(new MyInterestsRecyclerViewAdapter(likes, mListener));
                }

                @Override
                public void onFailure(Call<FacebookUserItem.FacebookLikesItem> call, Throwable t) {

                }
            });*/