package com.fiuba.tdp.linkup.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.facebook.Profile;
import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.components.FacebookAlbumListActivity;
import com.fiuba.tdp.linkup.util.DownloadImage;

public class EditInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        ImageButton profileImage = (ImageButton) findViewById(R.id.photo_profile);

        new DownloadImage(profileImage).execute(Profile.getCurrentProfile().getProfilePictureUri(700, 700).toString());

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromFacebook();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void pickImageFromFacebook() {
        Intent main = new Intent(this, FacebookAlbumListActivity.class);
        startActivity(main);
    }
}
