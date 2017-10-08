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

import com.bumptech.glide.Glide;
import com.facebook.Profile;
import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.domain.LinkUpUser;
import com.fiuba.tdp.linkup.services.UserManager;

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

        bindUserData(view, UserManager.getInstance().getMyUser());

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

    private void bindUserData(View view, LinkUpUser myUser) {
//        new DownloadImage((ImageView) view.findViewById(R.id.profile_picture)).execute(profile.getProfilePictureUri(700, 700).toString());

        Glide.with(this)
                .load(profile.getProfilePictureUri(700, 700).toString())
                .into((ImageView) view.findViewById(R.id.profile_picture));

        TextView nameView = (TextView) view.findViewById(R.id.label_name);
        nameView.setText(profile.getName());

        TextView ageView = (TextView) view.findViewById(R.id.label_age);
        ageView.setText(String.format("%s %s", myUser.getAge(), getString(R.string.years)));

        TextView genreView = (TextView) view.findViewById(R.id.label_genre);
        genreView.setText(myUser.getGender().compareTo("male") == 0 ? "Hombre" : "Mujer");


        TextView studiesView = (TextView) view.findViewById(R.id.label_studies);

        if (myUser.getEducation() != null && myUser.getEducation().length > 0) {
            studiesView.setText(myUser.getEducation()[myUser.getEducation().length - 1].getName());
        } else {
            studiesView.setText("No hay información de la educación");
        }
    }
}
