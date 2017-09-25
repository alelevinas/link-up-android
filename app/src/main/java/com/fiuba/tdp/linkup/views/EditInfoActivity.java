package com.fiuba.tdp.linkup.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.components.PhotoPickerFragment;
import com.fiuba.tdp.linkup.domain.LinkUpPicture;
import com.fiuba.tdp.linkup.domain.LinkUpUser;
import com.fiuba.tdp.linkup.domain.ServerResponse;
import com.fiuba.tdp.linkup.services.UserManager;
import com.fiuba.tdp.linkup.services.UserService;
import com.fiuba.tdp.linkup.util.DownloadImage;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditInfoActivity extends AppCompatActivity implements PhotoPickerFragment.OnPhotoPickerFragmentInteractionListener {

    private EditText textDescription;
    private TextView labelAboutMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ImageButton profileImage = (ImageButton) findViewById(R.id.photo_profile);
        new DownloadImage(profileImage).execute(UserManager.getInstance().getMyUser().getPicture());

        labelAboutMe = (TextView) findViewById(R.id.AboutMeLabel);
        labelAboutMe.setText("Acerca de " + UserManager.getInstance().getMyUser().getName());

        textDescription = (EditText) findViewById(R.id.txt_description);
        getDescription();

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
    protected void onStop() {
        postChanges();
        super.onStop();
    }

    public void postChanges(){
        LinkUpUser user = UserManager.getInstance().getMyUser();
        user.setDescription(textDescription.getText().toString());

        new UserService(labelAboutMe.getContext()).updateUser(user.getId(), user, new Callback<ServerResponse<String>>() {
            @Override
            public void onResponse(Call<ServerResponse<String>> call, Response<ServerResponse<String>> response) {
                Log.d("EDIT INFO ACTIVITY", "USER DESCRIPTION UPDATED");
            }

            @Override
            public void onFailure(Call<ServerResponse<String>> call, Throwable t) {
                Log.e("EDIT INFO ACTIVITY", "FAILED TO UPDATE USER DESCRIPTION");
            }
        });
    }

        @Override
    public void onImageChanged(int number, String photoUrl) {
        Log.e("IMAGE CHANGED", photoUrl);
            UserManager.getInstance().updatePicture(number, photoUrl, getBaseContext());
    }

    @Override
    public void onImageRemoved(int number, String photoUrl) {
        Log.e("IMAGE REMOVED", photoUrl);
        UserManager.getInstance().updatePicture(number, "", getBaseContext());
    }

    public void getDescription() {
        new UserService(getBaseContext()).getUser(UserManager.getInstance().getMyUser().getId(), new Callback<ServerResponse<LinkUpUser>>() {
            @Override
            public void onResponse(Call<ServerResponse<LinkUpUser>> call, Response<ServerResponse<LinkUpUser>> response) {
                if (response.isSuccessful()) {
                    if(!Objects.equals(response.body().data.getDescription(), ""))
                        textDescription.setText(response.body().data.getDescription());
                } else {
                    Log.e("EDIT INFO ACTIVITY", "ERROR UPDATING DESCRIPTION, SV RESPONSE");
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<LinkUpUser>> call, Throwable t) {
                Log.e("EDIT INFO ACTIVITY", "ERROR UPDATING DESCRIPTION");
            }
        });
    }

}