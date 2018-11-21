package com.example.lexar.projecthbo_ictbv.service;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.lexar.projecthbo_ictbv.helper.NotificationManager;
import com.example.lexar.projecthbo_ictbv.model.LocationStatus;
import com.example.lexar.projecthbo_ictbv.helper.Constants;

/**
 * LocationService
 *
 * Service to track the users location and send them notifications with questions accordingly.
 *
 * Created by wesselperik on 12/06/2018.
 */

public class LocationService extends Service implements LocationListener, GpsStatus.Listener {

    private boolean isLocationManagerUpdatingLocation;
    private final LocationServiceBinder binder = new LocationServiceBinder();
    private LocationService.LocationListener listener = null;
    private static final int NOTIFICATION_FOREGROUND_ID = 100;

    @Override
    public void onCreate() {
        isLocationManagerUpdatingLocation = false;
    }

    /**
     * Method to bind a listener to the service.
     * @param listener LocationListener
     */
    public void initializeService(LocationService.LocationListener listener) {
        this.listener = listener;
    }

    /**
     * Start updating the location of the user if GPS/location services are enabled.
     */
    public void startUpdatingLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        criteria.setBearingRequired(false);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            if (listener != null) {
                listener.onPermissionsCheckError();
            }
            return;
        }

        locationManager.addGpsStatusListener(this);
        locationManager.requestLocationUpdates(
                Constants.MIN_UPDATE_TIME_MILLIS,
                Constants.MIN_UPDATE_DISTANCE_METERS,
                criteria, this, null);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            NotificationManager.sendNotification(this, LocationStatus.GPS_DISABLED);
        }

        moveToForeground();

        if (listener != null) {
            listener.onStartedLocationUpdates();
        }
    }

    /**
     * Stop updating the location of the user.
     */
    public void stopUpdatingLocation(){
        if (this.isLocationManagerUpdatingLocation && listener != null){
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.removeUpdates(this);
            isLocationManagerUpdatingLocation = false;
            this.listener.onStoppedLocationUpdates();
        }
    }

    /**
     * Move the service to the foreground to keep updating the location.
     */
    private void moveToForeground() {
        Notification notification = new NotificationCompat.Builder(this).build();
        super.startForeground(NOTIFICATION_FOREGROUND_ID, notification);
    }

    @Override
    public int onStartCommand(Intent i, int flags, int startId) {
        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            listener.onGpsAvailabilityChanged(false);
        }
        NotificationManager.sendNotification(this, LocationStatus.GPS_DISABLED);
    }

    @Override
    public void onProviderEnabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            listener.onGpsAvailabilityChanged(true);
        }
    }

    /**
     * Called when the GPS status of the provider has changed.
     * @param provider the provider
     * @param status status
     * @param extras extras
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            if (status == LocationProvider.OUT_OF_SERVICE) {
                listener.onGpsAvailabilityChanged(false);
                NotificationManager.sendNotification(this, LocationStatus.GPS_FAILED);
            } else {
                listener.onGpsAvailabilityChanged(true);
                NotificationManager.cancelNotification(this,
                        Constants.NOTIFICATION_ID_LOCATION_STATUS);
            }
        }
    }

    /**
     * Called when the location of the user has changed.
     * @param newLocation the new location of the user
     */
    @Override
    public void onLocationChanged(final Location newLocation) {
        Log.d("LocationService",
                "(" + newLocation.getLatitude() +
                        "," + newLocation.getLongitude() + ")");

        Intent intent = new Intent("LocationUpdated");
        intent.putExtra("location", newLocation);

        this.listener.onLocationUpdated(newLocation);
        LocalBroadcastManager.getInstance(this.getApplication()).sendBroadcast(intent);
    }

    /**
     * Called when the GPS status of the user's phone has changed.
     * @param event the event status code
     */
    @Override
    public void onGpsStatusChanged(int event) {
        Log.i("LocationService", "GPS status changed: " + event);
        if (event == GpsStatus.GPS_EVENT_STOPPED) {
            NotificationManager.sendNotification(this, LocationStatus.GPS_DISCONNECTED);
        } else if (event == GpsStatus.GPS_EVENT_STARTED ||
                event == GpsStatus.GPS_EVENT_FIRST_FIX ||
                event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
            NotificationManager.cancelNotification(this, Constants.NOTIFICATION_ID_LOCATION_STATUS);
        }
        this.listener.onGpsStatusChanged(event);
    }

    /**
     * Return the location service.
     */
    public class LocationServiceBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }

    /**
     * Listener for location changes.
     */
    public interface LocationListener {
        void onLocationUpdated(Location newLocation);
        void onGpsStatusChanged(int event);
        void onGpsAvailabilityChanged(boolean available);
        void onStartedLocationUpdates();
        void onStoppedLocationUpdates();
        void onPermissionsCheckError();
    }
}
