package com.fiuba.tdp.linkup.components;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.domain.facebook.FacebookAlbumItem;
import com.fiuba.tdp.linkup.services.FacebookService;
import com.fiuba.tdp.linkup.util.GlideApp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;

/**
 * An activity representing a grid of FacebookPhotos. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link FacebookPhotoGridActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class FacebookAlbumListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebookphoto_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        View recyclerView = findViewById(R.id.facebookphoto_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String photoURL = data.getStringExtra(FacebookPhotoGridActivity.ARG_PHOTO_URL);
            Log.i("PHOTO URL", photoURL);
            setResult(RESULT_OK, data);  // passing data to stacked activity
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        getFacebookAlbums(recyclerView);
    }

    public void getFacebookAlbums(final RecyclerView recyclerView) {
        final List<FacebookAlbumItem> albums = new ArrayList<>();
        new FacebookService(recyclerView.getContext()).getAlbums(new Callback<FacebookAlbumItem[]>() {
            @Override
            public void onResponse(Call<FacebookAlbumItem[]> call, Response<FacebookAlbumItem[]> response) {
                Collections.addAll(albums, response.body());
                recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(albums));
            }

            @Override
            public void onFailure(Call<FacebookAlbumItem[]> call, Throwable t) {

            }
        });
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<FacebookAlbumItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<FacebookAlbumItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.facebookphoto_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mAlbumName.setText(mValues.get(position).getName());
//            new DownloadImage(holder.mAlbumCover).execute(mValues.get(position).getCoverPhoto().getPicture());

            GlideApp.with(holder.itemView.getContext())
                    .load(mValues.get(position).getCoverPhoto().getPicture())
//                    .placeholder(R.drawable.ezgif_com_gif_maker)
                    .into(holder.mAlbumCover);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Context context = v.getContext();
                    Intent intent = new Intent(context, FacebookPhotoGridActivity.class);
                    intent.putExtra(FacebookPhotoGridActivity.ARG_ITEM_ID, holder.mItem.id);

                    startActivityForResult(intent, 10);

                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mAlbumCover;
            public final TextView mAlbumName;
            public FacebookAlbumItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mAlbumCover = (ImageView) view.findViewById(R.id.album_cover);
                mAlbumName = (TextView) view.findViewById(R.id.album_name);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mAlbumName.getText() + "'";
            }
        }
    }
}
