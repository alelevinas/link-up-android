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
import com.fiuba.tdp.linkup.services.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alejandro on 10/9/17.
 */

public class ReportDialog extends DialogFragment {

    final static String TAG = "REPORT DIALOG";

    String otherUserId;

    String[] options;

    public ReportDialog() {
    }

    public ReportDialog setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        options = getResources().getStringArray(R.array.report_options);

        final int otherReason = options.length - 1;

        builder.setTitle("¿Por qué motivo quieres denunciar a este usuario?");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == otherReason) {
                    Log.e(TAG, "OTHER REASON");
                    new TextInputDialog().setOtherUserId(otherUserId).show(getFragmentManager(), "textinput");
//                    sendReport(options[i]);
                } else {
                    Log.e(TAG, String.format("Se eligio el %d", i));
                    sendReport(options[i]);
                }
            }
        });

        return builder.create();

    }

    private void sendReport(String reportReason) {

        final Context context = getContext();

        new UserService(context).postReportUser(otherUserId, reportReason, new Callback<ServerResponse<String>>() {
            @Override
            public void onResponse(Call<ServerResponse<String>> call, Response<ServerResponse<String>> response) {
                Toast.makeText(context, "Gracias, nuestros moderadores ya están analizando el caso", Toast.LENGTH_SHORT).show();
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
