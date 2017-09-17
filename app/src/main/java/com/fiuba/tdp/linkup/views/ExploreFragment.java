package com.fiuba.tdp.linkup.views;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        ExploreUserContentAdapter adapter = new ExploreUserContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }


    public static class ExploreUserContentAdapter extends RecyclerView.Adapter<ExploreUserViewHolder> {
        private static int LENGTH = 0;
        private static UsersAround usersAround;
        private UserService userService;

        public ExploreUserContentAdapter(Context context) {
            userService = new UserService();

            userService.getUsersCompatible(Profile.getCurrentProfile().getId(), new Callback<ServerResponse<ArrayList<UserAround>>>() {
                @Override
                public void onResponse(Call<ServerResponse<ArrayList<UserAround>>> call, Response<ServerResponse<ArrayList<UserAround>>> response) {
                    usersAround = new UsersAround(response.body().data);
                    LENGTH = usersAround.getSize();
                    notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<ServerResponse<ArrayList<UserAround>>> call, Throwable t) {
                    Log.w("ERROR", "Error getting users around!");
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
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}

