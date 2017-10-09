package com.fiuba.tdp.linkup.components.AsyncTaskLoaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.fiuba.tdp.linkup.domain.LinkUpUser;
import com.fiuba.tdp.linkup.domain.LocationUser;
import com.fiuba.tdp.linkup.domain.ServerResponse;
import com.fiuba.tdp.linkup.services.LocationManager;
import com.fiuba.tdp.linkup.services.UserManager;
import com.fiuba.tdp.linkup.services.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtherProfileActivityAsyncTaskLoader extends AsyncTaskLoader<String> {

    private long userID;

    public OtherProfileActivityAsyncTaskLoader(Context context, long user_id) {
        super(context);
        userID = user_id;
    }

    @Override
    public String loadInBackground() {
        try{
            final boolean[] userUpdated = {false};
            final LocationManager locationManager = new LocationManager();

            new UserService(getContext()).getUser(String.valueOf(userID), new Callback<ServerResponse<LinkUpUser>>() {
                @Override
                public void onResponse(Call<ServerResponse<LinkUpUser>> call, Response<ServerResponse<LinkUpUser>> response) {
                    if (response.isSuccessful()) {
                        UserManager.getInstance().setUserSelected(response.body().data);
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
