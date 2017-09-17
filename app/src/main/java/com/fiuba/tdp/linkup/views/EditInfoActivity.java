package com.fiuba.tdp.linkup.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.components.PhotoPickerFragment;
import com.fiuba.tdp.linkup.domain.LinkUpPicture;
import com.fiuba.tdp.linkup.services.UserManager;
import com.fiuba.tdp.linkup.util.DownloadImage;

public class EditInfoActivity extends AppCompatActivity implements PhotoPickerFragment.OnPhotoPickerFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        ImageButton profileImage = (ImageButton) findViewById(R.id.photo_profile);
        new DownloadImage(profileImage).execute(UserManager.getInstance().getMyUser().getPicture());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PhotoPickerFragment f1 = (PhotoPickerFragment) getSupportFragmentManager().findFragmentById(R.id.photo_1);
        PhotoPickerFragment f2 = (PhotoPickerFragment) getSupportFragmentManager().findFragmentById(R.id.photo_2);
        PhotoPickerFragment f3 = (PhotoPickerFragment) getSupportFragmentManager().findFragmentById(R.id.photo_3);
        PhotoPickerFragment f4 = (PhotoPickerFragment) getSupportFragmentManager().findFragmentById(R.id.photo_4);
        PhotoPickerFragment f5 = (PhotoPickerFragment) getSupportFragmentManager().findFragmentById(R.id.photo_5);

        PhotoPickerFragment[] fragments = {f1, f2, f3, f4, f5};

        int n = 0;
        for (PhotoPickerFragment f : fragments) {
            f.setNumber(n);
            n++;
        }

        int i = 0;
        for (LinkUpPicture p : UserManager.getInstance().getMyUser().getPictures()) {
            fragments[i].setImage(p.getUrl());
            i++;
        }
    }

    @Override
    public void onImageChanged(int number, String photoUrl) {
        Log.e("IMAGE CHANGED", photoUrl);
        UserManager.getInstance().updatePicture(number, photoUrl);
    }

    @Override
    public void onImageRemoved(int number, String photoUrl) {
        Log.e("IMAGE REMOVED", photoUrl);
        UserManager.getInstance().updatePicture(number, "");
    }
}
