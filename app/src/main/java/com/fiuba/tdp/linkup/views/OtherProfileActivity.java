package com.fiuba.tdp.linkup.views;

import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Loader;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.components.AsyncTaskLoaders.OtherProfileActivityAsyncTaskLoader;
import com.fiuba.tdp.linkup.components.BlockDialog;
import com.fiuba.tdp.linkup.components.ReportDialog;
import com.fiuba.tdp.linkup.domain.LinkUpPicture;
import com.fiuba.tdp.linkup.domain.LinkUpUser;
import com.fiuba.tdp.linkup.domain.ServerResponse;
import com.fiuba.tdp.linkup.services.UserManager;
import com.fiuba.tdp.linkup.services.UserService;
import com.fiuba.tdp.linkup.util.GlideApp;
import com.fiuba.tdp.linkup.util.SliderPagerAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class OtherProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    public static final String ID_USER = "position";
    private static final String TAG = "OTHER PROFILE";

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

    private ImageView vp_image;
    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    private SliderPagerAdapter sliderPagerAdapter;
    private ArrayList<String> slider_image_list;
    private TextView[] dots;
    private int page_position = 0;

    private ImageButton buttonMessage;
    private ImageButton buttonShare;
    private ImageButton buttonMenu;
    private FloatingActionButton buttonNotLike;
    private FloatingActionButton buttonSuperLike;
    private FloatingActionButton buttonLike;

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

        vp_image = (ImageView) findViewById(R.id.vp_image);
        vp_slider = (ViewPager) findViewById(R.id.vp_slider);
        ll_dots = (LinearLayout) findViewById(R.id.ll_dots);

        distanceLabel = (TextView) findViewById(R.id.distanceLabel);
        studiesLabel = (TextView) findViewById(R.id.studiesLabel);

        aboutMeLabel = (TextView) findViewById(R.id.aboutMeLabel);
        userDescription = (TextView) findViewById(R.id.aboutMe);

        userInterestsLabel = (TextView) findViewById(R.id.interestsLabel);
        userInterests =  (TextView) findViewById(R.id.interests);

        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        loader = (ImageView) findViewById(R.id.loader);

        buttonMessage = (ImageButton) findViewById(R.id.buttonMessage);
        buttonShare = (ImageButton) findViewById(R.id.buttonShare);
        buttonMenu = (ImageButton) findViewById(R.id.buttonMenu);
        buttonNotLike = (FloatingActionButton) findViewById(R.id.notLikeButton);
        buttonSuperLike = (FloatingActionButton) findViewById(R.id.superlikeButton);
        buttonLike = (FloatingActionButton) findViewById(R.id.likeButton);

        startLoader();
        startMyAsyncTask();

        toolbarUsername.setTitle("");
    }

    private void addBottomDots(int currentPage) {
        dots = slider_image_list != null ? new TextView[slider_image_list.size()] : new TextView[1];

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

    private void bindUserData(final LinkUpUser otherUser) {
        int nPictures = 0;
        for(LinkUpPicture picture : otherUser.getPictures()){
            if(!picture.getUrl().equals(""))
                nPictures++;
        }
        if(nPictures>0) {
            vp_image.setVisibility(View.GONE);
            vp_slider.setVisibility(View.VISIBLE);

            slider_image_list = new ArrayList<>();

            slider_image_list.add(otherUser.getPicture());
            for (LinkUpPicture image : otherUser.getPictures()) {
                if (!image.getUrl().equals(""))
                    slider_image_list.add(image.getUrl());
            }

            sliderPagerAdapter = new SliderPagerAdapter(OtherProfileActivity.this, slider_image_list);
            vp_slider.setAdapter(sliderPagerAdapter);

            vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

            // HACK to fix viewpager bug -> https://stackoverflow.com/questions/32323570/viewpager-title-doesnt-appear-until-i-swipe-it
            vp_slider.post(new Runnable() {
                @Override
                public void run() {
                    vp_slider.setCurrentItem(1);
                    vp_slider.setCurrentItem(0);
                }
            });
        } else {
            vp_slider.setVisibility(View.GONE);
            vp_image.setVisibility(View.VISIBLE);

            GlideApp.with(this)
                    .load(otherUser.getPicture())
                    .apply(bitmapTransform(new RoundedCornersTransformation(20, 5)))
//                    .placeholder(R.drawable.ezgif_com_gif_maker)
                    .into(vp_image);

//            new DownloadImage(vp_image).execute(otherUser.getPicture());
        }
        addBottomDots(0);

        toolbarUsername.setTitle(getFirstWord(otherUser.getName()) + ", " + otherUser.getAge());
        //toolbarUsername.setBackgroundColor(Color.parseColor("#3f000000"));

        distanceLabel.setText("A 25km de distancia");
        if (otherUser.getEducation().length != 0 ) {
            studiesLabel.setText(otherUser.getEducation()[otherUser.getEducation().length - 1].getName());
        } else {
            studiesLabel.setText("");
        }

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

        buttonNotLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Eliminaste de tu listado a " + otherUser.getName(),
                        Snackbar.LENGTH_LONG).show();
                deleteFromAround(otherUser.getId());
            }
        });

        buttonSuperLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Has dado SuperLike a " + otherUser.getName(),
                        Snackbar.LENGTH_LONG).show();
            }
        });

        buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Diste Like a " + otherUser.getName(),
                        Snackbar.LENGTH_LONG).show();
            }
        });

        buttonMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "No puedes enviar mensaje a " + otherUser.getName(),
                        Snackbar.LENGTH_LONG).show();
            }
        });

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Todavia no puedes compartir a " + otherUser.getName(),
                        Snackbar.LENGTH_LONG).show();
            }
        });

        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pm = new PopupMenu(OtherProfileActivity.this, v);
                pm.getMenuInflater().inflate(R.menu.button_menu_profile, pm.getMenu());
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.block:
                                Toast.makeText(getApplicationContext(), "Bloquear a " + otherUser.getName(), Toast.LENGTH_SHORT).show();
                                new BlockDialog().setOtherUserId(otherUser.getId()).show(getFragmentManager().beginTransaction(), "bloquear");
                                break;
                            case R.id.report:
                                Toast.makeText(getApplicationContext(), "Denunciar a " + otherUser.getName(), Toast.LENGTH_SHORT).show();
                                new ReportDialog().setOtherUserId(otherUser.getId()).show(getFragmentManager().beginTransaction(), "denunciar");
                                break;

                            default:
                                break;
                        }
                        return true;
                    }
                });
                pm.show();
            }
        });
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

        buttonNotLike.setVisibility(View.GONE);
        buttonSuperLike.setVisibility(View.GONE);
        buttonLike.setVisibility(View.GONE);

        loader.setVisibility(View.VISIBLE);
        loader.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_indefinitely) );
    }

    public void startMyAsyncTask() {
        mLoader.restartLoader(0, null, this);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new OtherProfileActivityAsyncTaskLoader(getApplicationContext(), idUser);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        stopLoader();
    }

    private void stopLoader() {
        bindUserData(UserManager.getInstance().getUserSelected());

        loader.clearAnimation();
        loader.setVisibility(View.GONE);

        buttonNotLike.setVisibility(View.VISIBLE);
        buttonSuperLike.setVisibility(View.VISIBLE);
        buttonLike.setVisibility(View.VISIBLE);

        findViewById(R.id.appbar).setVisibility(View.VISIBLE);
        nestedScrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
    }

    private void deleteFromAround(String userId) {
        new UserService(getBaseContext()).deleteUserFromAround(UserManager.getInstance().getMyUser().getId(), userId, new Callback<ServerResponse<String>>() {
            @Override
            public void onResponse(Call<ServerResponse<String>> call, Response<ServerResponse<String>> response) {
                Log.e(TAG, "Deleted from arround");
                finish();
            }

            @Override
            public void onFailure(Call<ServerResponse<String>> call, Throwable t) {
                Snackbar.make(getCurrentFocus(), "Hubo un error al contactar al servidor. Por favor intenta más tarde",
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }



    private void showAlert(String s) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(s)
                .setTitle("Atención");

        // 3. Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                dialog.dismiss();
            }
        });

        // 4. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        dialog.show();

    }

}
