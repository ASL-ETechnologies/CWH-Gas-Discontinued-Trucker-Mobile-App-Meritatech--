package com.meritatech.myrewardzpos;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.meritatech.myrewardzpos.controller.GlobalVariables;
import com.meritatech.myrewardzpos.data.LogRecord;
import com.meritatech.myrewardzpos.data.MyPosBase;
import com.meritatech.myrewardzpos.data.SalesInvoiceRecord;
import com.meritatech.myrewardzpos.database.SchemaGenerator;
import com.meritatech.myrewardzpos.database.SugarContext;
import com.meritatech.myrewardzpos.database.SugarDb;
import com.meritatech.myrewardzpos.global.CustomerVariableData;
import com.meritatech.myrewardzpos.global.SalesOrderVariableData;
import com.meritatech.myrewardzpos.global.VariableData;
import com.meritatech.myrewardzpos.utility.Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import myrewardzpos.com.LocationService;

public class MainActivity extends AppCompatActivity
        implements
        HomeFragment.OnFragmentInteractionListener,
        SalesOrderFragment.OnFragmentInteractionListener,
        SalesOrderDetailsFragment.OnFragmentInteractionListener,
        InventoryFragment.OnFragmentInteractionListener,
        InvoiceFragment.OnFragmentInteractionListener,
        InvoiceCreateFragment.OnFragmentInteractionListener,
        InventotyDialogFragment.OnFragmentInteractionListener,
        InventotyDialogFragment.EditNameDialogListener,
        NavigationView.OnNavigationItemSelectedListener,
        CustomerFragment.OnFragmentInteractionListener,
        ReceiptFragment.OnFragmentInteractionListener,
        SalesInvoiceHistoryFragment.OnFragmentInteractionListener,
        EditInventoryItemFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener,
        AppLogFragment.OnFragmentInteractionListener,
        CustomerFullDetailsFragment.OnFragmentInteractionListener,

        OnMapReadyCallback {

    public static Context appContext;
    // private FusedLocationProviderClient mFusedLocationClient;


    LocationManager locationManager = null;
    String provider;
    private LocationCallback mLocationCallback;
    private static final int INITIAL_REQUEST = 1337;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;
    private static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        appContext = getApplicationContext();
        SugarContext.init(appContext);

        SchemaGenerator sg = new SchemaGenerator(getApplicationContext());
        boolean isTableCreated = sg.tableExists(LogRecord.class, new SugarDb(getApplicationContext()).getDB());
        if (!isTableCreated) {
            sg.createDatabase(new SugarDb(getApplicationContext()).getDB());
        }

// here set the pattern as you date in string was containing like date/month/year

        if (GlobalVariables.killApplication == 1) {
            appContext.stopService(new Intent(appContext,
                    UploadBackGroundService.class));
            SugarContext.terminate();
            SchemaGenerator schemaGenerator = new SchemaGenerator(getApplicationContext());

            schemaGenerator.deleteTables(new SugarDb(getApplicationContext()).getDB());
            SugarContext.init(getApplicationContext());

            schemaGenerator.createDatabase(new SugarDb(getApplicationContext()).getDB());

            new AlertDialog.Builder(this)
                    .setMessage("Contact the Administrator")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            System.exit(0);

                        }
                    })

                    .show();
            return;

        }


        if (isStoragePermissionGranted()) {
            LogSession();
        }

        //  @SuppressLint("MissingPermission") String IMEI = telephonyManager.getDeviceId();;


        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                startService(new Intent(appContext, UploadBackGroundService.class));

                startService(new Intent(appContext, LocationService.class));
            }

        }, 15000);


        //NOTE:  Open fragment1 initially.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new HomeFragment());
        ft.commit();

        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            // only for gingerbread and newer versions
            //  requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
            buildAlertMessageNoGps();
        } else {
            int hasLocPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasLocPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
                buildAlertMessageNoGps();
                return;
            }
        }



        try {
            NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);
            Menu menu = navigationView1.getMenu();
            MenuItem nav_camara = menu.findItem(R.id.nav_info);

            PackageInfo pinfo = null;

            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);

            String versionName = pinfo.versionName;
            nav_camara.setTitle(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            Utilities.LogException(e);
        }

    }

    private void LogSession() {
        try {

            int itemCountBefore = (int) LogRecord.count(LogRecord.class);
            LogRecord lr = new LogRecord();
            lr.ErrorMessage = "Session started at " + new Date();
            lr.save();
            int itemCountAfter = (int) LogRecord.count(LogRecord.class);
            if (itemCountBefore == itemCountAfter) {
                Toast toast = Toast.makeText(appContext, "Error saving Records in DB contact Admin! ", Toast.LENGTH_LONG);
                toast.show();
            }
        } catch (Exception ex) {
            Utilities.LogException(ex);
        }
    }


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LogSession();
            }
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void startBackGroundThread() {
        startService(new Intent(getBaseContext(), UploadBackGroundService.class));

        startService(new Intent(this, LocationService.class));


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {


            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                new AlertDialog.Builder(this)
                        .setMessage("Do you want to exit MyRewardzPOS?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.super.onBackPressed();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //NOTE: creating fragment object
        Fragment fragment = null;

        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
            VariableData.inventoryVariable = null;
        } else if (id == R.id.nav_settings) {
            VariableData.inventoryVariable = null;
            fragment = new SettingsFragment();
        } else if (id == R.id.nav_service_order) {
            VariableData.inventoryVariable = null;
            fragment = new SalesOrderFragment();
        } else if (id == R.id.nav_inventory) {
            VariableData.inventoryVariable = null;
            fragment = new InventoryFragment();
        } else if (id == R.id.nav_customer) {
            VariableData.inventoryVariable = null;
            fragment = new CustomerFragment();
        } else if (id == R.id.nav_create_invoice) {
            GlobalVariables.IsSalesOrder = false;
            CustomerVariableData.customerVariable = null;
            SalesOrderVariableData.IsSalesOrder = false;
            fragment = new InvoiceCreateFragment();
            VariableData.inventoryVariable = null;
        } else if (id == R.id.nav_customer) {
            fragment = new CustomerFragment();
        } else if (id == R.id.nav_service_order_history) {
            fragment = new SalesInvoiceHistoryFragment();
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }


        final Fragment finalFragment = fragment;
        new Thread(new Runnable() {
            public void run() {
                //NOTE: Fragment changing code
                if (finalFragment != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.mainFrame, finalFragment);
                    ft.commit();
                }

                //NOTE:  Closing the drawer after selecting

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout); //Ya you can also globalize this variable :P
                        drawer.closeDrawer(GravityCompat.START);

                    }
                });
            }
        }).start();
        return true;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }


    @Override
    public void onFinishEditDialog(String inputText) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

    }

    private void buildAlertMessageNoGps() {
        boolean gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gps_enabled) {
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

    }


    public void openInvoice(View v) {
        Fragment fragment = new InvoiceCreateFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, fragment);
        ft.commit();
    }

}

