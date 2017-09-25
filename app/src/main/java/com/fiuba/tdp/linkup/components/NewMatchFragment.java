package com.fiuba.tdp.linkup.components;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.domain.LinkUpMatch;
import com.fiuba.tdp.linkup.domain.ServerResponse;
import com.fiuba.tdp.linkup.services.UserManager;
import com.fiuba.tdp.linkup.services.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnNewMatchListFragmentInteractionListener}
 * interface.
 */
public class NewMatchFragment extends Fragment {

    private OnNewMatchListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewMatchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newmatch_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            setUpRecyclerViewAdapter(recyclerView);
        }
        return view;
    }

    private void setUpRecyclerViewAdapter(final RecyclerView recyclerView) {
        final List<LinkUpMatch> matchs = new ArrayList<>();
        new UserService().getMatchesWithoutChat(UserManager.getInstance().getMyUser().getId(), new Callback<ServerResponse<LinkUpMatch[]>>() {
            @Override
            public void onResponse(Call<ServerResponse<LinkUpMatch[]>> call, Response<ServerResponse<LinkUpMatch[]>> response) {
                Collections.addAll(matchs, response.body().data);
                recyclerView.setAdapter(new MyNewMatchRecyclerViewAdapter(matchs, mListener));
            }

            @Override
            public void onFailure(Call<ServerResponse<LinkUpMatch[]>> call, Throwable t) {
                Log.e("GET MATCHES", "FAILURE");
            }
        });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNewMatchListFragmentInteractionListener) {
            mListener = (OnNewMatchListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNewMatchListFragmentInteractionListener");
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
    public interface OnNewMatchListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(LinkUpMatch item);
    }
}
