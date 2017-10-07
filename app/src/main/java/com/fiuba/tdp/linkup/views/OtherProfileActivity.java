package com.fiuba.tdp.linkup.views;

import android.app.LoaderManager;
import android.content.Loader;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.components.AsyncTaskLoaders.OtherProfileActivityAsyncTaskLoader;
import com.fiuba.tdp.linkup.components.AsyncTaskLoaders.ProfileFragmentAsyncTaskLoader;
import com.fiuba.tdp.linkup.domain.LinkUpUser;
import com.fiuba.tdp.linkup.services.UserManager;
import com.fiuba.tdp.linkup.util.DownloadImage;

import static com.fiuba.tdp.linkup.util.MySuperAppApplication.getContext;

public class OtherProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    public static final String ID_USER = "position";

    private LoaderManager mLoader;
    private ImageView loader;
    private NestedScrollView nestedScrollView;
    private TextView placeDetail;
    private TextView placeLocation;
    private ImageView placePicture;
    private long idUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_profile_detail);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        // collapsingToolbar.setTitle(getString(R.string.item_title));

        idUser = getIntent().getLongExtra(ID_USER, 0);

        mLoader = getLoaderManager();
        if(mLoader.getLoader(0) != null) {
            mLoader.initLoader(0, null, this);// deliver the result after the screen rotation
        }

        startMyAsyncTask();
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        loader = (ImageView) findViewById(R.id.loader);
        startLoader();

        collapsingToolbar.setTitle("title");
    }

    private void bindUserData(LinkUpUser otherUser) {
        placeDetail = (TextView) findViewById(R.id.place_detail);
        placeDetail.setText(otherUser.getName());

        placeLocation =  (TextView) findViewById(R.id.place_location);
        placeLocation.setText(otherUser.getDescription());

        placePicture = (ImageView) findViewById(R.id.image);
        new DownloadImage(placePicture).execute(otherUser.getPicture());
    }

    private void startLoader() {
        findViewById(R.id.appbar).setVisibility(View.GONE);
        nestedScrollView.setVisibility(View.GONE);

        loader.setVisibility(View.VISIBLE);
        loader.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rotate_indefinitely) );
    }

    public void startMyAsyncTask() {
        mLoader.restartLoader(0, null, this);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new OtherProfileActivityAsyncTaskLoader(getContext(), idUser);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        stopLoader();
    }

    private void stopLoader() {
        bindUserData(UserManager.getInstance().getUserSelected());

        loader.setVisibility(View.GONE);
        loader.clearAnimation();

        findViewById(R.id.appbar).setVisibility(View.VISIBLE);
        nestedScrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
    }

}
