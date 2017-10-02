package com.fiuba.tdp.linkup.views;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.components.ExploreUserViewHolder;
import com.fiuba.tdp.linkup.domain.ServerResponse;
import com.fiuba.tdp.linkup.domain.UserAround;
import com.fiuba.tdp.linkup.domain.UsersAround;
import com.fiuba.tdp.linkup.services.UserService;
import com.fiuba.tdp.linkup.util.DownloadImage;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExploreFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView emptyView;
    private ImageView loader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
//                R.layout.fragment_explore_list, container, false);
        View view = inflater.inflate(R.layout.fragment_explore_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.explore_list);
        emptyView = (TextView) view.findViewById(R.id.empty_view);
        loader = (ImageView) view.findViewById(R.id.loader);

        ExploreUserContentAdapter adapter = new ExploreUserContentAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        loader.setVisibility(View.GONE);

        return view;
    }

    private void setEmptyView() {
        loader.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView() {
        loader.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }

    private void startLoader() {
        loader.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
    }

    public static class ExploreUserContentAdapter extends RecyclerView.Adapter<ExploreUserViewHolder> {
        private static int LENGTH = 0;
        private static UsersAround usersAround;
        private UserService userService;

        public ExploreUserContentAdapter(final ExploreFragment exploreFragment) {
            exploreFragment.startLoader();
            userService = new UserService(exploreFragment.getContext());

            userService.getUsersCompatible(Profile.getCurrentProfile().getId(), new Callback<ServerResponse<ArrayList<UserAround>>>() {
                @Override
                public void onResponse(Call<ServerResponse<ArrayList<UserAround>>> call, Response<ServerResponse<ArrayList<UserAround>>> response) {
                    usersAround = new UsersAround(response.body().data);
                    LENGTH = usersAround.getSize();
                    notifyDataSetChanged();

                    if (LENGTH == 0) {
                        exploreFragment.setEmptyView();
                    } else {
                        exploreFragment.hideEmptyView();
                    }
                }

                @Override
                public void onFailure(Call<ServerResponse<ArrayList<UserAround>>> call, Throwable t) {
                    Log.w("ERROR", "Error getting users around!");
                    exploreFragment.setEmptyView();
                }
            });
        }

        @Override
        public ExploreUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ExploreUserViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ExploreUserViewHolder holder, int position) {
            new DownloadImage(holder.picture).execute(usersAround.getPictures().get(position));
            holder.name.setText(usersAround.getNames().get(position));
            holder.description.setText(usersAround.getDescriptions().get(position));
            holder.userId = usersAround.getIds().get(position);
            holder.favoriteImageButtonChecked = usersAround.getUser(position).getLike().compareTo("true") == 0;
            holder.updateLikeStatus();
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }

}

