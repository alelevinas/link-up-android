package com.fiuba.tdp.linkup.views;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.facebook.Profile;
import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.components.AsyncTaskLoaders.ProfileFragmentAsyncTaskLoader;
import com.fiuba.tdp.linkup.domain.LinkUpPicture;
import com.fiuba.tdp.linkup.domain.LinkUpUser;
import com.fiuba.tdp.linkup.services.UserManager;
import com.fiuba.tdp.linkup.util.DownloadImage;
import com.fiuba.tdp.linkup.util.GlideApp;

import java.util.Objects;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class FirstSignUpActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>{

    private Profile profile;
    private ImageView loader;
    private ConstraintLayout mainLayout;
    private LoaderManager mLoader;
    private TextView nameView;
    private TextView ageView;
    private TextView genreView;
    private RelativeLayout aboutMeLayout;
    private TextView description;
    private TextView studiesView;
    private ImageView profile_picture;
    private ImageView premium_star;
    private ImageView secondary_pictures1;
    private ImageView secondary_pictures2;
    private ImageView secondary_pictures3;
    private ImageView secondary_pictures4;
    private ImageView secondary_pictures5;

    private View mainView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        profile = Profile.getCurrentProfile();
        mLoader = getActivity().getLoaderManager();
        if(mLoader.getLoader(0) != null) {
            mLoader.initLoader(0, null, this);// deliver the result after the screen rotation
        }

        mainView = inflater.inflate(R.layout.fragment_first_sign_up, container, false);

        startMyAsyncTask();

        mainLayout = (ConstraintLayout) mainView.findViewById(R.id.main_layout);
        loader = (ImageView) mainView.findViewById(R.id.loader);
        startLoader();

        nameView = (TextView) mainView.findViewById(R.id.label_name);
        ageView = (TextView) mainView.findViewById(R.id.label_age);
        genreView = (TextView) mainView.findViewById(R.id.label_genre);
        aboutMeLayout = (RelativeLayout) mainView.findViewById(R.id.aboutMe);
        description = (TextView) mainView.findViewById(R.id.txt_description);
        studiesView = (TextView) mainView.findViewById(R.id.label_studies);
        profile_picture = (ImageView) mainView.findViewById(R.id.profile_picture);
        premium_star = (ImageView) mainView.findViewById(R.id.premium_star);

        secondary_pictures1 = (ImageView) mainView.findViewById(R.id.secondary_picture);
        secondary_pictures2 = (ImageView) mainView.findViewById(R.id.secondary_picture2);
        secondary_pictures3 = (ImageView) mainView.findViewById(R.id.secondary_picture3);
        secondary_pictures4 = (ImageView) mainView.findViewById(R.id.secondary_picture4);
        secondary_pictures5 = (ImageView) mainView.findViewById(R.id.secondary_picture5);

        Button buttonEditInfo = (Button) mainView.findViewById(R.id.button_edit_info);
        buttonEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editInfo = new Intent(getActivity(), EditInfoActivity.class);
                startActivity(editInfo);
            }
        });

        Button btnPreferences = (Button) mainView.findViewById(R.id.button_preferences);
        btnPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent preferences = new Intent(view.getContext(), PreferencesActivity.class);
                startActivity(preferences);
            }
        });

        return mainView;
    }


    private void startLoader() {
        mainLayout.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
        loader.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rotate_indefinitely) );
    }


    @Override
    public void onResume() {
        super.onResume();

        startMyAsyncTask();
        startLoader();
    }



    private void bindUserData(View view, LinkUpUser myUser) {
//        new DownloadImage((ImageView) view.findViewById(R.id.profile_picture)).execute(profile.getProfilePictureUri(700, 700).toString());

        GlideApp.with(this)
                .load(profile.getProfilePictureUri(700, 700).toString())
                .apply(bitmapTransform(new CircleCrop()))
//                .placeholder(R.drawable.ezgif_com_gif_maker)
                .into(profile_picture);

        if (myUser.isPremium()) {
            premium_star.setVisibility(View.VISIBLE);
        } else {
            premium_star.setVisibility(View.GONE);
        }

        TextView nameView = (TextView) view.findViewById(R.id.label_name);
        nameView.setText(profile.getName());

        ImageView[] secondary_pictures = {secondary_pictures1, secondary_pictures2, secondary_pictures3,
                                                                secondary_pictures4, secondary_pictures5};
        int i = 0;
        for (LinkUpPicture p : UserManager.getInstance().getMyUser().getPictures()) {
            new DownloadImage(secondary_pictures[i]).execute(p.getUrl());
            if(!Objects.equals(p.getUrl(), ""))
                i++;
        }
        for(int j=0; j<5; j++){
            if(i == 0) {
                secondary_pictures[j].setVisibility(View.GONE);
            } else {
                secondary_pictures[j].setVisibility(View.VISIBLE);
            }
        }
        for(; i < 5; i++){
            secondary_pictures[i].setImageDrawable(null);
        }

        ageView.setText(String.format("%s %s", myUser.getAge(), getString(R.string.years)));

        genreView.setText(myUser.getGender().compareTo("male") == 0 ? "Hombre" : "Mujer");

        if (myUser.getEducation() != null && myUser.getEducation().length > 0) {
            studiesView.setText(myUser.getEducation()[myUser.getEducation().length - 1].getName());
        } else {
            studiesView.setText("No hay información de la educación");
        }

        if(Objects.equals(myUser.getDescription(), "")) {
            aboutMeLayout.setVisibility(View.GONE);
        } else {
            aboutMeLayout.setVisibility(View.VISIBLE);
            description.setText(myUser.getDescription());
        }
    }

    public void startMyAsyncTask() {
        mLoader.restartLoader(0, null, this);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new ProfileFragmentAsyncTaskLoader(this.getContext());
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        stopLoader();
    }

    private void stopLoader() {
        bindUserData(mainView, UserManager.getInstance().getMyUser());

        loader.setVisibility(View.GONE);
        loader.clearAnimation();
        mainLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
    }
}
