package com.fiuba.tdp.linkup.views;

import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Loader;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.components.AsyncTaskLoaders.OtherProfileActivityAsyncTaskLoader;
import com.fiuba.tdp.linkup.components.BlockDialog;
import com.fiuba.tdp.linkup.components.ReportDialog;
import com.fiuba.tdp.linkup.domain.LinkUpPicture;
import com.fiuba.tdp.linkup.domain.LinkUpUser;
import com.fiuba.tdp.linkup.domain.Match;
import com.fiuba.tdp.linkup.domain.ServerResponse;
import com.fiuba.tdp.linkup.services.LocationManager;
import com.fiuba.tdp.linkup.services.UserManager;
import com.fiuba.tdp.linkup.services.UserService;
import com.fiuba.tdp.linkup.util.GlideApp;
import com.fiuba.tdp.linkup.util.SliderPagerAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.color.holo_blue_bright;
import static android.R.color.holo_green_light;
import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class OtherProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, BlockDialog.OnBlockDialogFragmentInteractionListener, OnMapReadyCallback {

    public static final String ID_USER = "position";
    public static final String IS_LIKED = "IS_LIKED";
    public static final String IS_SUPERLIKED = "IS_SUPERLIKED";
    private static final String TAG = "OTHER PROFILE";
    private static final double ASSUMED_INIT_LATLNG_DIFF = 1.0;
    private static final float ACCURACY = 0.01f;
    public boolean likeChecked = false;
    public boolean superlikeChecked = false;
    LocationManager locationManager = new LocationManager();
    private Menu menu;
    private GoogleMap mMap;
    private Circle mCircle;
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
    private LinkUpUser otherUser;

    public static LatLngBounds boundsWithCenterAndLatLngDistance(LatLng center, double latDistanceInMeters, double lngDistanceInMeters) {
        latDistanceInMeters /= 2;
        lngDistanceInMeters /= 2;
        LatLngBounds.Builder builder = LatLngBounds.builder();
        float[] distance = new float[1];
        {
            boolean foundMax = false;
            double foundMinLngDiff = 0;
            double assumedLngDiff = ASSUMED_INIT_LATLNG_DIFF;
            do {
                Location.distanceBetween(center.latitude, center.longitude, center.latitude, center.longitude + assumedLngDiff, distance);
                double distanceDiff = distance[0] - lngDistanceInMeters;
                if (distanceDiff < 0) {
                    if (!foundMax) {
                        foundMinLngDiff = assumedLngDiff;
                        assumedLngDiff *= 2;
                    } else {
                        double tmp = assumedLngDiff;
                        assumedLngDiff += (assumedLngDiff - foundMinLngDiff) / 2;
                        foundMinLngDiff = tmp;
                    }
                } else {
                    assumedLngDiff -= (assumedLngDiff - foundMinLngDiff) / 2;
                    foundMax = true;
                }
            } while (Math.abs(distance[0] - lngDistanceInMeters) > lngDistanceInMeters * ACCURACY);
            LatLng east = new LatLng(center.latitude, center.longitude + assumedLngDiff);
            builder.include(east);
            LatLng west = new LatLng(center.latitude, center.longitude - assumedLngDiff);
            builder.include(west);
        }
        {
            boolean foundMax = false;
            double foundMinLatDiff = 0;
            double assumedLatDiffNorth = ASSUMED_INIT_LATLNG_DIFF;
            do {
                Location.distanceBetween(center.latitude, center.longitude, center.latitude + assumedLatDiffNorth, center.longitude, distance);
                double distanceDiff = distance[0] - latDistanceInMeters;
                if (distanceDiff < 0) {
                    if (!foundMax) {
                        foundMinLatDiff = assumedLatDiffNorth;
                        assumedLatDiffNorth *= 2;
                    } else {
                        double tmp = assumedLatDiffNorth;
                        assumedLatDiffNorth += (assumedLatDiffNorth - foundMinLatDiff) / 2;
                        foundMinLatDiff = tmp;
                    }
                } else {
                    assumedLatDiffNorth -= (assumedLatDiffNorth - foundMinLatDiff) / 2;
                    foundMax = true;
                }
            } while (Math.abs(distance[0] - latDistanceInMeters) > latDistanceInMeters * ACCURACY);
            LatLng north = new LatLng(center.latitude + assumedLatDiffNorth, center.longitude);
            builder.include(north);
        }
        {
            boolean foundMax = false;
            double foundMinLatDiff = 0;
            double assumedLatDiffSouth = ASSUMED_INIT_LATLNG_DIFF;
            do {
                Location.distanceBetween(center.latitude, center.longitude, center.latitude - assumedLatDiffSouth, center.longitude, distance);
                double distanceDiff = distance[0] - latDistanceInMeters;
                if (distanceDiff < 0) {
                    if (!foundMax) {
                        foundMinLatDiff = assumedLatDiffSouth;
                        assumedLatDiffSouth *= 2;
                    } else {
                        double tmp = assumedLatDiffSouth;
                        assumedLatDiffSouth += (assumedLatDiffSouth - foundMinLatDiff) / 2;
                        foundMinLatDiff = tmp;
                    }
                } else {
                    assumedLatDiffSouth -= (assumedLatDiffSouth - foundMinLatDiff) / 2;
                    foundMax = true;
                }
            } while (Math.abs(distance[0] - latDistanceInMeters) > latDistanceInMeters * ACCURACY);
            LatLng south = new LatLng(center.latitude - assumedLatDiffSouth, center.longitude);
            builder.include(south);
        }
        return builder.build();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_profile_detail);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Location
        locationManager.getDeviceLocation(this);

        toolbarUsername = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        // collapsingToolbar.setTitle(getString(R.string.item_title));

        idUser = getIntent().getLongExtra(ID_USER, 0);

        likeChecked = getIntent().getBooleanExtra(IS_LIKED, false);
        superlikeChecked = getIntent().getBooleanExtra(IS_SUPERLIKED, false);

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

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        toolbarUsername.setTitle("");

        if (!getIntent().hasExtra(IS_LIKED)) {
            buttonLike.setVisibility(View.GONE);
            buttonSuperLike.setVisibility(View.GONE);

            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) buttonNotLike.getLayoutParams();
            lp.anchorGravity = Gravity.BOTTOM | Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
            buttonNotLike.setLayoutParams(lp); //bottom|center_vertical|center_horizontal"
        }
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

        this.otherUser = otherUser;

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
                    likesUser = likesUser + "\n" + like.getName();
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
                pressSuperLikeButton(v);
            }
        });

        buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pressLikeButton(v);
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

        final BlockDialog.OnBlockDialogFragmentInteractionListener self = this;

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
                                if (item.getTitle().equals("Bloquear"))
                                    new BlockDialog().setOtherUserId(otherUser.getId()).attach(self).show(getFragmentManager().beginTransaction(), "bloquear");
                                else
                                    new BlockDialog().setOtherUserId(otherUser.getId()).unblock().attach(self).show(getFragmentManager().beginTransaction(), "desbloquear");
                                break;
                            case R.id.report:
                                new ReportDialog().setOtherUserId(otherUser.getId()).show(getFragmentManager().beginTransaction(), "denunciar");
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
                menu = pm.getMenu();
                onBlockDialogFragmentInteraction(UserManager.getInstance().isBlocked(otherUser.getId()));
                pm.show();
            }
        });

        // MAP

        LatLng otherPos = new LatLng(otherUser.getLocation().getLat(), otherUser.getLocation().getLon());
        LatLng myPos;
        if (locationManager.getLastKnownLocation() != null) {
            myPos = new LatLng(locationManager.getLastKnownLocation().getLatitude(), locationManager.getLastKnownLocation().getLongitude());
            distanceLabel.setText(String.format("A %.0f km de distancia", Math.ceil(getDistanceFromPositions(myPos, otherPos) / 1000)));
        } else {
            distanceLabel.setText("");
        }

        //TODO: sacar markers!!!!!
//        mMap.addMarker(new MarkerOptions().position(myPos).title(UserManager.getInstance().getMyUser().getName()));
        mMap.addMarker(new MarkerOptions().position(otherPos).title(otherUser.getName()));

//        LatLng center = getCenterFromPositions(myPos, otherPos);
//        float radius = getDistanceFromPositions(myPos, otherPos);


        LatLng center = getRandomPositionFrom(otherPos);
        float radius = 300;
        drawMarkerWithCircle(center, radius);


        updateLikeStatus();
        updateSuperLikeStatus();
    }

    private void pressSuperLikeButton(View v) {
        if (superlikeChecked) {
            Snackbar.make(v, "No puedes quitarle el superlike!",
                    Snackbar.LENGTH_LONG).show();
            return;
        }

        superlikeChecked = !superlikeChecked;
        updateSuperLikeStatus();
        if (superlikeChecked) {
            sendSuperLikeToServer(v);
        } else {
            sendDeleteSuperLikeToServer(v);
        }
    }

    private void sendDeleteSuperLikeToServer(final View v) {
        new UserService(v.getContext()).deleteSuperLikeToUser(UserManager.getInstance().getMyUser().getId(), otherUser.getId(), new Callback<ServerResponse<String>>() {
            String LOG_LIKE = "DELETE SUPER LIKE FROM USER";

            @Override
            public void onResponse(Call<ServerResponse<String>> call, Response<ServerResponse<String>> response) {
                Log.d(LOG_LIKE, "message = " + response.message());
                if (response.isSuccessful()) {
                    Log.d(LOG_LIKE, "-----isSuccess----");
                    Log.d(LOG_LIKE, response.body().data);
                } else {
                    Log.d(LOG_LIKE, "-----isFalse-----");
                    this.onFailure(call, null);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<String>> call, Throwable t) {
                superlikeChecked = !superlikeChecked;
                updateSuperLikeStatus();
                Snackbar.make(v, "Hubo un error al contactar al servidor. Por favor intenta luego más tarde",
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void sendSuperLikeToServer(final View v) {
        new UserService(v.getContext()).postSuperLikeToUser(UserManager.getInstance().getMyUser().getId(), otherUser.getId(), new Callback<ServerResponse<Match>>() {
            String LOG_LIKE = "SUPER LIKE USER";

            @Override
            public void onResponse(Call<ServerResponse<Match>> call, Response<ServerResponse<Match>> response) {
                Log.d(LOG_LIKE, "message = " + response.message());
                if (response.isSuccessful()) {
                    Log.d(LOG_LIKE, "-----isSuccess----");
                    Log.d(LOG_LIKE, response.body().data.getLink().toString());

                    if (response.body().data.getLink()) {
                        showAlert("Felicitaciones!", "Hay match!");
                    }

                } else {
                    Log.d(LOG_LIKE, "-----isFalse-----");
                    this.onFailure(call, null);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<Match>> call, Throwable t) {
                superlikeChecked = !superlikeChecked;
                updateSuperLikeStatus();
                Snackbar.make(v, "Hubo un error al contactar al servidor. Por favor intenta luego más tarde",
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void updateSuperLikeStatus() {
        if (superlikeChecked) {
            buttonSuperLike.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorAccent));
            buttonSuperLike.setImageTintList(ContextCompat.getColorStateList(this, R.color.cardview_light_background));
        } else {
            buttonSuperLike.setImageTintList(ContextCompat.getColorStateList(this, holo_blue_bright));
            buttonSuperLike.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.cardview_light_background));
        }
    }

    private LatLng getRandomPositionFrom(LatLng pos) {
        return new LatLng(pos.latitude + ThreadLocalRandom.current().nextDouble(-0.002, 0.002), pos.longitude + ThreadLocalRandom.current().nextDouble(-0.002, 0.002));
    }

    private float getDistanceFromPositions(LatLng myPos, LatLng otherPos) {

        float[] distance = new float[1];
        Location.distanceBetween(myPos.latitude, myPos.longitude, otherPos.latitude, otherPos.longitude, distance);

        return distance[0];
    }

    private LatLng getCenterFromPositions(LatLng myPos, LatLng otherPos) {
        return new LatLng((myPos.latitude + otherPos.latitude) / 2, (myPos.longitude + otherPos.longitude) / 2);
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

        if (getIntent().hasExtra(IS_LIKED)) {
            buttonSuperLike.setVisibility(View.VISIBLE);
            buttonLike.setVisibility(View.VISIBLE);
        }


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

    @Override
    public void onBlockDialogFragmentInteraction(Boolean isBlocked) {
        MenuItem blockMenuItem = menu.findItem(R.id.block);
        if (isBlocked) {
            // cambiar el menu a Desbloquear
            blockMenuItem.setTitle("Desbloquear");
        } else {
            // pooner el menu en Bloquear
            blockMenuItem.setTitle("Bloquear");
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        Log.e(TAG, otherUser.getLocation().getLat() + "   " + otherUser.getLocation().getLon());


        // Add a marker in Sydney and move the camera
//        LatLng ba = new LatLng(-34.6156625, -58.503338);
//        drawMarkerWithCircle(ba);
//        mMap.addMarker(new MarkerOptions().position(ba).title("Marker in BA"));

//        mMap.moveCamera(CameraUpdateFactory.newLatLng(ba));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds.Builder().include(ba).build(), 10));
    }

    private void drawMarkerWithCircle(LatLng position, double radiusInMeters){
        if (mMap == null)
            return;
        Log.e(TAG, String.format("Other user Lat: %f  | Long: %f\n",otherUser.getLocation().getLat(), otherUser.getLocation().getLon()));

//        double radiusInMeters = 60000.0;
        int strokeColor = 0xff33b5e5; //blue outline
        int shadeColor = 0x33ff0000; //opaque red fill

        CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
        mCircle = mMap.addCircle(circleOptions);

        LatLngBounds bounds = boundsWithCenterAndLatLngDistance(position,radiusInMeters/2,radiusInMeters/2);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = 200;
//        int padding = (int) (width * 0.12); // offset from edges of the map 12% of screen

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, 0));
//        mMap.addMarker(new MarkerOptions().position(position));
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

    private void showAlert(String title, String message) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(message)
                .setTitle(title);

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


    /***********************LIKE*************************************/

    private void pressLikeButton(View v) {
        likeChecked = !likeChecked;
        updateLikeStatus();
        if (likeChecked) {
            sendLikeToServer(v);
        } else {
            sendDeleteLikeToServer(v);
        }
    }

    private void sendDeleteLikeToServer(final View v) {
        new UserService(v.getContext()).deleteLikeToUser(UserManager.getInstance().getMyUser().getId(), otherUser.getId(), new Callback<ServerResponse<String>>() {
            String LOG_LIKE = "DELETE LIKE FROM USER";

            @Override
            public void onResponse(Call<ServerResponse<String>> call, Response<ServerResponse<String>> response) {
                Log.d(LOG_LIKE, "message = " + response.message());
                if (response.isSuccessful()) {
                    Log.d(LOG_LIKE, "-----isSuccess----");
                    Log.d(LOG_LIKE, response.body().data);
                } else {
                    Log.d(LOG_LIKE, "-----isFalse-----");
                    this.onFailure(call, null);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<String>> call, Throwable t) {
                likeChecked = !likeChecked;
                updateLikeStatus();
                Snackbar.make(v, "Hubo un error al contactar al servidor. Por favor intenta luego más tarde",
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void sendLikeToServer(final View v) {
        new UserService(v.getContext()).postLikeToUser(UserManager.getInstance().getMyUser().getId(), otherUser.getId(), new Callback<ServerResponse<Match>>() {
            String LOG_LIKE = "LIKE USER";

            @Override
            public void onResponse(Call<ServerResponse<Match>> call, Response<ServerResponse<Match>> response) {
                Log.d(LOG_LIKE, "message = " + response.message());
                if (response.isSuccessful()) {
                    Log.d(LOG_LIKE, "-----isSuccess----");
                    Log.d(LOG_LIKE, response.body().data.getLink().toString());

                    if (response.body().data.getLink()) {
                        showAlert("Felicitaciones!", "Hay match!");
                    }

                } else {
                    Log.d(LOG_LIKE, "-----isFalse-----");
                    this.onFailure(call, null);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<Match>> call, Throwable t) {
                likeChecked = !likeChecked;
                updateLikeStatus();
                Snackbar.make(v, "Hubo un error al contactar al servidor. Por favor intenta luego más tarde",
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void updateLikeStatus() {
        if (likeChecked) {
            buttonLike.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorAccent));
            buttonLike.setImageTintList(ContextCompat.getColorStateList(this, R.color.cardview_light_background));
        } else {
            buttonLike.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.cardview_light_background));
            buttonLike.setImageTintList(ContextCompat.getColorStateList(this, holo_green_light));
        }
    }

}
