package com.fiuba.tdp.linkup.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.facebook.Profile;
import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.components.FacebookAlbumListActivity;
import com.fiuba.tdp.linkup.components.FacebookPhotoGridActivity;
import com.fiuba.tdp.linkup.util.DownloadImage;

public class EditInfoActivity extends AppCompatActivity {

    View editingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        ImageButton profileImage = (ImageButton) findViewById(R.id.photo_profile);
        ImageButton photo_1 = (ImageButton) findViewById(R.id.photo_1);
        ImageButton photo_2 = (ImageButton) findViewById(R.id.photo_2);
        ImageButton photo_3 = (ImageButton) findViewById(R.id.photo_3);
        ImageButton photo_4 = (ImageButton) findViewById(R.id.photo_4);
        ImageButton photo_5 = (ImageButton) findViewById(R.id.photo_5);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromFacebook(view);
            }
        };

        new DownloadImage(profileImage).execute(Profile.getCurrentProfile().getProfilePictureUri(700, 700).toString());

        profileImage.setOnClickListener(clickListener);
        photo_1.setOnClickListener(clickListener);
        photo_2.setOnClickListener(clickListener);
        photo_3.setOnClickListener(clickListener);
        photo_4.setOnClickListener(clickListener);
        photo_5.setOnClickListener(clickListener);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void pickImageFromFacebook(View view) {
        editingView = view;
        Intent main = new Intent(this, FacebookAlbumListActivity.class);
        startActivity(main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String photoURL = data.getStringExtra(FacebookPhotoGridActivity.ARG_PHOTO_URL);
            new DownloadImage((ImageButton) editingView).execute(photoURL);
            // TODO: actualizar en el modelo de usuario y mandar al server
        }
    }
}
