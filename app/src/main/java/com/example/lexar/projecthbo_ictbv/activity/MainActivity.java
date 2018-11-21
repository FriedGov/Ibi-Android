package com.example.lexar.projecthbo_ictbv.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.os.Bundle;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lexar.projecthbo_ictbv.adapter.CampaignAdapter;
import com.example.lexar.projecthbo_ictbv.R;
import com.example.lexar.projecthbo_ictbv.helper.NotificationManager;
import com.example.lexar.projecthbo_ictbv.model.Campaign;
import com.example.lexar.projecthbo_ictbv.model.User;
import com.example.lexar.projecthbo_ictbv.helper.Constants;
import com.example.lexar.projecthbo_ictbv.service.LocationService;
import com.example.lexar.projecthbo_ictbv.task.ActiveCampaignsTask;
import com.example.lexar.projecthbo_ictbv.task.DeleteAccountTask;
import com.example.lexar.projecthbo_ictbv.task.LocationQuestionTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;

/**
 * The main activity of the app.
 */
public class MainActivity extends AppCompatActivity
        implements LocationService.LocationListener, ActiveCampaignsTask.Callback,
        LocationQuestionTask.Callback, DeleteAccountTask.DeleteAccountCallbacks {

    @BindView(R.id.lv_campaigns)
    ListView lvCampaigns;

    @BindView(R.id.btn_removeAccount)
    Button removeAccount;

    @BindView(R.id.appbar)
    AppBarLayout appBar;

    @BindView(R.id.fadeLayout)
    LinearLayout fadeLayout;

    public LocationService locationService;
    private ArrayList<Campaign> campaigns;
    private CampaignAdapter adapter;

    /**
     * Binding Butterknife and getting the user location (asking for permissions if needed).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        campaigns = new ArrayList<>();
        if (!User.getInstance().init(getApplicationContext())) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
        if (checkPermissions()) {
            String[] string = new String[]{ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, string,
                    Constants.REQUEST_LOCATION_PERMISSIONS);
            ActivityCompat.requestPermissions(this,
                    new String[]{INTERNET}, Constants.REQUEST_INTERNET_PERMISSIONS);
        }
        adapter = new CampaignAdapter(getApplicationContext(), R.layout.campaign_item, campaigns);
        lvCampaigns.setAdapter(adapter);

        setTranslucentStatusBar(getWindow());

        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                fadeLayout.setAlpha(1.0f - Math.abs(verticalOffset /
                        (float) appBarLayout.getTotalScrollRange()));
            }
        });

        initialiseLocationManager();
        ActiveCampaignsTask task = new ActiveCampaignsTask(this);
        task.execute();
    }

    /*
     * Check if user has to give permission to access his/her location
     */
    private boolean checkPermissions() {
        return hasAccessFinePermission()
                && hasAccessCoarseLocationPermission()
                && hasAccessInternet();
    }

    private boolean hasAccessFinePermission() {
        return ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED;
    }

    private boolean hasAccessCoarseLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED;
    }

    private boolean hasAccessInternet() {
        return ActivityCompat.checkSelfPermission(this, INTERNET)
                != PackageManager.PERMISSION_DENIED;
    }


    /**
     * Initialize Location mangager to ask for location updates every x seconds.
     */
    private void initialiseLocationManager() {
        final Intent serviceStart = new Intent(this.getApplication(), LocationService.class);
        this.getApplication().startService(serviceStart);
        this.getApplication().bindService
                (serviceStart, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Initialize a service connection in this activity.
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            String name = className.getClassName();

            if (name.endsWith("LocationService")) {
                locationService = ((LocationService.LocationServiceBinder) service).getService();
                locationService.initializeService(MainActivity.this);
                locationService.startUpdatingLocation();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            if (className.getClassName().equals("LocationService")) {
                locationService = null;
            }
        }
    };

    /**
     * Set the statusbar to a translucent color.
     *
     * @param window the current window
     */
    private static void setTranslucentStatusBar(Window window) {
        if (window == null) {
            return;
        }
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatusBarLollipop(window);
        } else if (sdkInt >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatusBarKitKat(window);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setTranslucentStatusBarLollipop(Window window) {
        window.setStatusBarColor(
                window.getContext()
                        .getResources()
                        .getColor(R.color.statusBar));
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void setTranslucentStatusBarKitKat(Window window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    public void onLocationUpdated(Location newLocation) {
        LocationQuestionTask task = new LocationQuestionTask(this);
        task.execute(User.getInstance().getLanguageId(),
                User.getInstance().getUserID(), newLocation.getLatitude(),
                newLocation.getLongitude());
    }

    @Override
    public void onGpsStatusChanged(int event) {
        Log.d("MainActivity GPS", "Status changed: " + event);
    }

    @Override
    public void onGpsAvailabilityChanged(boolean available) {
        Log.d("MainActivity GPS", "Availability changed: " + available);
    }

    @Override
    public void onStartedLocationUpdates() {
        Log.d("MainActivity GPS", "Started updating location");
    }

    @Override
    public void onStoppedLocationUpdates() {
        Log.d("MainActivity GPS", "Stopped updating location");
    }

    @Override
    public void onPermissionsCheckError() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                Constants.REQUEST_LOCATION_PERMISSIONS);
    }

    /**
     * Method to delete the user's account.
     */
    @OnClick(R.id.btn_removeAccount)
    void delete() {
        DeleteAccountTask deleteAccountTask = new DeleteAccountTask(this);
        deleteAccountTask.execute();
    }

    /**
     * Callback which is called when the campaigns are loaded.
     *
     * @param campaigns the campaigns
     */
    @Override
    public void onCampaignsLoaded(ArrayList<Campaign> campaigns) {
        this.campaigns.addAll(campaigns);
        adapter.notifyDataSetChanged();
    }

    /**
     * Callback when a call was successfully made to the server to get locations in a user's area.
     *
     * @param response a JSONObject with questions and the place
     */
    @Override
    public void onResponse(JSONObject response) {
        Log.d("LocationTask response", response.toString());
        try {
            if (response.getJSONArray("questions").length() > 0) {
                // there are questions available on this location!
                NotificationManager.sendNotification(this,
                        new com.example.lexar.projecthbo_ictbv.model.Location(
                                response.getJSONArray("place").getJSONObject
                                        (0)), response.getJSONArray("questions"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Callback which is called when an error occured whilst loading campaigns.
     */
    @Override
    public void onLoadingError() {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.error_loading_campaigns), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Callback which is called when the user's account is successfully deleted.
     */
    @Override
    public void deleteSuccessful() {
        User.getInstance().delete();
        Toast.makeText(this, R.string.remove_account_success, Toast.LENGTH_LONG).show();
        finish();

    }

    /**
     * Callback which is called when the user's account deletion failed.
     */
    @Override
    public void deleteFailed() {
        Toast.makeText(this, R.string.remove_account_failed, Toast.LENGTH_LONG).show();
    }
}
