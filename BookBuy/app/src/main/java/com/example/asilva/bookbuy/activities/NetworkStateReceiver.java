package com.example.asilva.bookbuy.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.asilva.bookbuy.util.Util;

/**
 * Created by wildsonsantos on 28/09/2015.
 */
public class NetworkStateReceiver extends BroadcastReceiver {

    public static final String NETWORK = "network";
    @Override
    public void onReceive(final Context context, final Intent intent) {

        if (Util.isNetworkConnected(context)){
            context.sendBroadcast(new Intent(NETWORK));
        }
    }
}
