package me.twodee.friendlyneighbor;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class NetworkUtilsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!NetworkUtils.isConnectedToInternet(context)) {

            new MaterialAlertDialogBuilder(context)
                    .setTitle("Error")
                    .setMessage("Check your internet connection and try again.")
                    .setCancelable(false)
                    .setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            onReceive(context, intent);
                        }
                    })
        .show();
        }

    }
}
