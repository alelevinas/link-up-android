package com.fiuba.tdp.linkup.components;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.domain.facebook.FacebookPhotoItem;
import com.fiuba.tdp.linkup.services.FacebookService;
import com.fiuba.tdp.linkup.util.GlideApp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An activity representing a single FacebookPhoto detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link FacebookAlbumListActivity}.
 */
public class FacebookPhotoGridActivity extends AppCompatActivity {

    public static String ARG_ITEM_ID = "ITEM_ID";
    public static String ARG_PHOTO_URL = "PHOTO_URL";
    private String albumId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebookphoto_grid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        albumId = getIntent().getStringExtra(ARG_ITEM_ID);


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.facebookphoto_detail_photos);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        setupRecyclerView(recyclerView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, FacebookAlbumListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        getFacebookPhotosOfAlbum(recyclerView);
    }

    public void getFacebookPhotosOfAlbum(final RecyclerView recyclerView) {
        final List<FacebookPhotoItem> photos = new ArrayList<>();
        new FacebookService(getBaseContext()).getPhotosFromAlbum(albumId, new Callback<FacebookPhotoItem[]>() {
            @Override
            public void onResponse(Call<FacebookPhotoItem[]> call, Response<FacebookPhotoItem[]> response) {
                Collections.addAll(photos, response.body());
                recyclerView.setAdapter(new FacebookPhotoGridActivity.SimpleItemRecyclerViewAdapter(photos));
            }

            @Override
            public void onFailure(Call<FacebookPhotoItem[]> call, Throwable t) {

            }
        });
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<FacebookPhotoGridActivity.SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<FacebookPhotoItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<FacebookPhotoItem> items) {
            mValues = items;
        }

        @Override
        public FacebookPhotoGridActivity.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.facebookphoto_grid_content, parent, false);
            return new FacebookPhotoGridActivity.SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final FacebookPhotoGridActivity.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
//            new DownloadImage(holder.mPhoto).execute(mValues.get(position).getPicture());

            GlideApp.with(holder.itemView.getContext())
                    .load(mValues.get(position).getPicture())
//                    .placeholder(R.drawable.ezgif_com_gif_maker)
                    .into(holder.mPhoto);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    String photoURL = String.valueOf(holder.mItem.getSource());
                    intent.putExtra(ARG_PHOTO_URL, photoURL);
                    setResult(RESULT_OK, intent);
                    Log.i("PHOTO URL", photoURL);
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mPhoto;
            public FacebookPhotoItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mPhoto = (ImageView) view.findViewById(R.id.photo);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mItem.getId() + "'";
            }
        }
    }
}