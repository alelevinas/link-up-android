package com.fiuba.tdp.linkup.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.facebook.Profile;
import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.components.PhotoPickerFragment;
import com.fiuba.tdp.linkup.util.DownloadImage;

public class EditInfoActivity extends AppCompatActivity implements PhotoPickerFragment.OnPhotoPickerFragmentInteractionListener {

    View editingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        ImageButton profileImage = (ImageButton) findViewById(R.id.photo_profile);
        new DownloadImage(profileImage).execute(Profile.getCurrentProfile().getProfilePictureUri(700, 700).toString());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onImageChanged(String photoUrl) {
        Log.e("IMAGE CHANGED", photoUrl);
    }

    @Override
    public void onImageRemoved(String photoUrl) {
        Log.e("IMAGE REMOVED", photoUrl);
    }
}
