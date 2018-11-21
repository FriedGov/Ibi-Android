package com.example.lexar.projecthbo_ictbv.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.lexar.projecthbo_ictbv.service.LocationService;

/**
 * BootReceiver.
 *
 * Method to receive the boot status of the phone. Used to start the location service on startup.
 *
 * Created by wesselperik on 19/06/2018.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // phone booted up, start the location service
        Intent startServiceIntent = new Intent(context, LocationService.class);
        context.startService(startServiceIntent);
    }
}
