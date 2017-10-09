package com.fiuba.tdp.linkup.components;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.domain.ServerResponse;
import com.fiuba.tdp.linkup.services.UserManager;
import com.fiuba.tdp.linkup.services.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alejandro on 10/9/17.
 */

public class BlockDialog extends DialogFragment {

    final static String TAG = "REPORT DIALOG";

    String otherUserId;

    String[] options;

    public BlockDialog() {
    }

    public BlockDialog setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        options = getResources().getStringArray(R.array.report_options);

        final int otherReason = options.length - 1;

        builder.setTitle("¿Seguro quieres bloquear a este usuario?");

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                sendBlock();
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });

        return builder.create();

    }

    private void sendBlock() {

        final Context context = getContext();

        new UserService(context).postBlockUser(UserManager.getInstance().getMyUser().getId(), otherUserId, new Callback<ServerResponse<String>>() {
            @Override
            public void onResponse(Call<ServerResponse<String>> call, Response<ServerResponse<String>> response) {
                Toast.makeText(context, "El usuario ha sido bloqueado", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "todo bien");
            }

            @Override
            public void onFailure(Call<ServerResponse<String>> call, Throwable t) {
                Toast.makeText(context, "Nos hemos encontrado con un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "todo mal");
            }
        });
    }
}
