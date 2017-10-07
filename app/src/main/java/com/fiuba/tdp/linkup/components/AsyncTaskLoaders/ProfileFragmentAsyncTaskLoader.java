package com.fiuba.tdp.linkup.components.AsyncTaskLoaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;

import com.facebook.login.LoginManager;
import com.fiuba.tdp.linkup.domain.LinkUpUser;
import com.fiuba.tdp.linkup.domain.LocationUser;
import com.fiuba.tdp.linkup.domain.ServerResponse;
import com.fiuba.tdp.linkup.services.LocationManager;
import com.fiuba.tdp.linkup.services.UserManager;
import com.fiuba.tdp.linkup.services.UserService;
import com.fiuba.tdp.linkup.views.LogInActivity;
import com.fiuba.tdp.linkup.views.MainLinkUpActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragmentAsyncTaskLoader extends AsyncTaskLoader<String> {
    public ProfileFragmentAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    public String loadInBackground() {
        try{
            final boolean[] userUpdated = {false};
            final LocationManager locationManager = new LocationManager();

            new UserService(getContext()).getUser(UserManager.getInstance().getMyUser().getId(), new Callback<ServerResponse<LinkUpUser>>() {
                @Override
                public void onResponse(Call<ServerResponse<LinkUpUser>> call, Response<ServerResponse<LinkUpUser>> response) {
                    if (response.isSuccessful()) {
                        UserManager.getInstance().setMyUser(response.body().data);
                        if (locationManager.getLastKnownLocation() != null) {
                            LocationUser userLoc = new LocationUser(locationManager.getLastKnownLocation().getLatitude(), locationManager.getLastKnownLocation().getLongitude());
                            new UserService(getContext()).putLocation(UserManager.getInstance().getMyUser().getId(), userLoc);
                        } else {
                            LocationUser userLoc = new LocationUser(-34.59, -58.41);
                            new UserService(getContext()).putLocation(UserManager.getInstance().getMyUser().getId(), userLoc);
                        }
                        userUpdated[0] = true;

                    } else {
                        userUpdated[0] = true;
                    }
                }

                @Override
                public void onFailure(Call<ServerResponse<LinkUpUser>> call, Throwable t) {
                    userUpdated[0] = true;
                }
            });

            while(!userUpdated[0]){
                Thread.sleep(100);
            }
            return "user updated";
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "user not updated";
        }
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
