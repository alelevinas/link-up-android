package com.fiuba.tdp.linkup.views;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.domain.FacebookUserItem;
import com.fiuba.tdp.linkup.services.FacebookService;
import com.fiuba.tdp.linkup.util.DownloadImage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Profile profile;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profile = Profile.getCurrentProfile();

        getFacebookData(view);

        return view;
    }

    private void getFacebookData(final View view) {
        new DownloadImage((ImageView) view.findViewById(R.id.profileImage)).execute(profile.getProfilePictureUri(200, 200).toString());

        new FacebookService().getUserData(new Callback<FacebookUserItem>() {
            @Override
            public void onResponse(Call<FacebookUserItem> call, Response<FacebookUserItem> response) {
                FacebookUserItem userData = response.body();
                attachUserDataToView(userData, view);
            }

            @Override
            public void onFailure(Call<FacebookUserItem> call, Throwable t) {

            }
        });
    }

    private void attachUserDataToView(FacebookUserItem userData, View view) {
        TextView nameView = (TextView) view.findViewById(R.id.nameAndSurname);
        nameView.setText(profile.getName());

        TextView ageView = (TextView) view.findViewById(R.id.label_age);
        ageView.setText(userData.getBirthday());

        TextView genreView = (TextView) view.findViewById(R.id.label_genre);
        genreView.setText(userData.getGender());

        TextView studiesView = (TextView) view.findViewById(R.id.label_studies);
        studiesView.setText(userData.getEducation()[userData.getEducation().length - 1].getSchool().getName());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
