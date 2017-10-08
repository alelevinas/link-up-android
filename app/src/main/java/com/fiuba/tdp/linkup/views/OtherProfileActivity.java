package com.fiuba.tdp.linkup.views;

import android.app.LoaderManager;
import android.content.Loader;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.components.AsyncTaskLoaders.OtherProfileActivityAsyncTaskLoader;
import com.fiuba.tdp.linkup.components.AsyncTaskLoaders.ProfileFragmentAsyncTaskLoader;
import com.fiuba.tdp.linkup.domain.LinkUpPicture;
import com.fiuba.tdp.linkup.domain.LinkUpUser;
import com.fiuba.tdp.linkup.services.UserManager;
import com.fiuba.tdp.linkup.util.DownloadImage;
import com.fiuba.tdp.linkup.util.SliderPagerAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.fiuba.tdp.linkup.util.MySuperAppApplication.getContext;

public class OtherProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    public static final String ID_USER = "position";

    private LoaderManager mLoader;
    private ImageView loader;
    private NestedScrollView nestedScrollView;
    private CollapsingToolbarLayout toolbarUsername;
    private TextView distanceLabel;
    private TextView studiesLabel;
    private TextView aboutMeLabel;
    private TextView userDescription;
    private TextView userInterestsLabel;
    private TextView userInterests;

    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    SliderPagerAdapter sliderPagerAdapter;
    ArrayList<String> slider_image_list;
    private TextView[] dots;
    int page_position = 0;

    private long idUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_profile_detail);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarUsername = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        // collapsingToolbar.setTitle(getString(R.string.item_title));

        idUser = getIntent().getLongExtra(ID_USER, 0);

        mLoader = getLoaderManager();
        if(mLoader.getLoader(0) != null) {
            mLoader.initLoader(0, null, this);// deliver the result after the screen rotation
        }

        distanceLabel = (TextView) findViewById(R.id.distanceLabel);
        studiesLabel = (TextView) findViewById(R.id.studiesLabel);
        aboutMeLabel = (TextView) findViewById(R.id.aboutMeLabel);
        userDescription = (TextView) findViewById(R.id.aboutMe);
        userInterestsLabel = (TextView) findViewById(R.id.interestsLabel);
        userInterests =  (TextView) findViewById(R.id.interests);

        startMyAsyncTask();
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        loader = (ImageView) findViewById(R.id.loader);
        startLoader();

        toolbarUsername.setTitle("");
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_image_list.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#000000"));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#FFFFFF"));
    }

    private void bindUserData(LinkUpUser otherUser) {
        vp_slider = (ViewPager) findViewById(R.id.vp_slider);
        ll_dots = (LinearLayout) findViewById(R.id.ll_dots);

        slider_image_list = new ArrayList<>();

        slider_image_list.add(otherUser.getPicture());
        for(LinkUpPicture image : otherUser.getPictures()) {
            if(!image.getUrl().equals(""))
                slider_image_list.add(image.getUrl());
        }

        sliderPagerAdapter = new SliderPagerAdapter(OtherProfileActivity.this, slider_image_list);
        vp_slider.setAdapter(sliderPagerAdapter);

        vp_slider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        addBottomDots(0);
        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if (page_position == slider_image_list.size()) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                vp_slider.setCurrentItem(page_position, true);
            }
        };

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 5000, 5000);
        vp_slider.setCurrentItem(0, true);

        toolbarUsername.setTitle(getFirstWord(otherUser.getName()) + ", " + otherUser.getAge());

        distanceLabel.setText("A 25km de distancia");
        studiesLabel.setText(otherUser.getEducation()[otherUser.getEducation().length - 1].getName());

        if(otherUser.getDescription().equals("")) {
            aboutMeLabel.setVisibility(View.GONE);
            userDescription.setVisibility(View.GONE);
        } else {
            aboutMeLabel.setText("Acerca de " + getFirstWord(otherUser.getName()));
            userDescription.setText(otherUser.getDescription());
            aboutMeLabel.setVisibility(View.VISIBLE);
            userDescription.setVisibility(View.VISIBLE);
        }

        if(otherUser.getLikes().length == 0){
            userInterestsLabel.setVisibility(View.GONE);
            userInterests.setVisibility(View.GONE);
        } else {
            String likesUser = "";
            for(LinkUpUser.LinkUpLike like : otherUser.getLikes()) {
                if(likesUser.equals(""))
                    likesUser = like.getName();
                else
                    likesUser = likesUser + "\n " + like.getName();
            }
            userInterests.setText(likesUser);
            userInterestsLabel.setVisibility(View.VISIBLE);
            userInterests.setVisibility(View.VISIBLE);
        }
    }

    private String getFirstWord(String text) {
        if (text.indexOf(' ') > -1) { // Check if there is more than one word.
            return text.substring(0, text.indexOf(' ')); // Extract first word.
        } else {
            return text; // Text is the first word itself.
        }
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
