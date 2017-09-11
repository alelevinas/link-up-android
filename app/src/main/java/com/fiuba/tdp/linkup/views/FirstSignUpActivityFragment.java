package com.fiuba.tdp.linkup.views;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
 * A placeholder fragment containing a simple view.
 */
public class FirstSignUpActivityFragment extends Fragment {

    Profile profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        profile = Profile.getCurrentProfile();

        View view = inflater.inflate(R.layout.fragment_first_sign_up, container, false);

        getFacebookData(view);


        Button buttonEditInfo = (Button) view.findViewById(R.id.button_edit_info);

        buttonEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editInfo = new Intent(getActivity(), EditInfoActivity.class);
                startActivity(editInfo);
            }
        });

        Button btnPreferences = (Button) view.findViewById(R.id.button_preferences);
        btnPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent preferences = new Intent(view.getContext(), PreferencesActivity.class);
                startActivity(preferences);
            }
        });

        return view;
    }

    private void getFacebookData(final View view) {
        new DownloadImage((ImageView) view.findViewById(R.id.profile_picture)).execute(profile.getProfilePictureUri(200, 200).toString());

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
        TextView nameView = (TextView) view.findViewById(R.id.label_name);
        nameView.setText(profile.getName());

        TextView ageView = (TextView) view.findViewById(R.id.label_age);
        ageView.setText(String.format("%s %s", userData.getAge(), getString(R.string.years)));

        TextView genreView = (TextView) view.findViewById(R.id.label_genre);
        genreView.setText(userData.getGender());

        TextView studiesView = (TextView) view.findViewById(R.id.label_studies);
        studiesView.setText(userData.getEducation()[userData.getEducation().length - 1].getSchool().getName());
    }
}
