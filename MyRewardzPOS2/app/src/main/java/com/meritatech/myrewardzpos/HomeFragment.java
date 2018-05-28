package com.meritatech.myrewardzpos;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.meritatech.myrewardzpos.controller.AdapterSalesOrder;
import com.meritatech.myrewardzpos.controller.GlobalVariables;
import com.meritatech.myrewardzpos.controller.Inventory;
import com.meritatech.myrewardzpos.controller.SalesOrder;
import com.meritatech.myrewardzpos.data.CustomerDataInfoRecord;
import com.meritatech.myrewardzpos.data.CustomerRecord;
import com.meritatech.myrewardzpos.data.InventoryListAPI;
import com.meritatech.myrewardzpos.data.InventoryRecord;
import com.meritatech.myrewardzpos.data.MyPosBase;
import com.meritatech.myrewardzpos.data.SalesOrderInventoryRecord;
import com.meritatech.myrewardzpos.data.SalesOrderRecord;
import com.meritatech.myrewardzpos.global.CustomerVariableData;
import com.meritatech.myrewardzpos.global.LocationData;
import com.meritatech.myrewardzpos.global.VariableData;
import com.meritatech.myrewardzpos.interfaces.SalesOrderInterface;
import com.meritatech.myrewardzpos.model.CustomerDataModel;
import com.meritatech.myrewardzpos.model.PosServicesInterface;
import com.meritatech.myrewardzpos.model.SalesOrderDataModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;
import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Location currentLocation = LocationData.location;
    private int clickNo = 0;
    private Handler mHandler;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AdapterSalesOrder adbSalesOrder;
    MapView mMapView;
    public GoogleMap mGoogleMap;
    private LocationCallback mLocationCallback;
    LatLng myPosition;
    private OnFragmentInteractionListener mListener;
    private ListView mListView;
    private static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST = 1337;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;
    GlobalVariables globalVars = new GlobalVariables();
    TextView input_bgprocess;
    Handler handler = new Handler();

    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mHandler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Home");
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        input_bgprocess = (TextView) view.findViewById(R.id.input_bgprocess);
        LinearLayout mapl = (LinearLayout) view.findViewById(R.id.mapviewlayout);
        if (GlobalVariables.enableGPS != null) {
            if (GlobalVariables.enableGPS.equals("0")) {

                mapl.setVisibility(View.GONE);
            }
        }


        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                populateBGstatus();

            }
        }, 10000, 10000);


        mMapView = (MapView) view.findViewById(R.id.mapview);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this); //this is important

        mListView = (ListView) view.findViewById(R.id.list_view_main_salesorder);

        ImageView addInventotyBtn = (ImageView) view.findViewById(R.id.buttonAddInventory);
        addInventotyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = null;
                fragment = new InvoiceCreateFragment();
                replaceFragment(fragment);
            }
        });


        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                final ArrayList<SalesOrderRecord> dt = SalesOrder.findAllRecords(SalesOrderRecord.class);
                MyPosBase myPosBase = new MyPosBase();
                if (dt != null && dt.size() > 0) {
                    for (int i = 0; i < dt.size(); i++) {
                        if (dt.get(i).salesOrderId != null) {
                            ArrayList<SalesOrderInventoryRecord> records = myPosBase.GetSalesOrderInventoryRecords(dt.get(i).salesOrderId);
                            if (records != null && records.size() > 0) {
                                for (int e = 0; e < records.size(); e++) {
                                    Inventory inventory = new Inventory();
                                    inventory.description = records.get(e).description;
                                    inventory.itemNum = records.get(e).itemNumber;
                                    inventory.qty = records.get(e).qty;
                                    inventory.priceValue = records.get(e).dollarValue;
                                    inventory.points = records.get(e).pointsValue;
                                    if (dt.get(i).SalesDetails == null) {
                                        dt.get(i).SalesDetails = new ArrayList<Inventory>();
                                    }
                                    dt.get(i).SalesDetails.add(inventory);
                                }

                            }
                        }
                    }
                    adbSalesOrder = new AdapterSalesOrder(getActivity(), android.R.layout.simple_list_item_1, dt);
                    mListView.setAdapter(adbSalesOrder);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (int l = 0; l < dt.size(); l++) {
                                if (dt.get(l).lati != null && dt.get(l).longi != null) {
                                    mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(dt.get(l).lati), Double.parseDouble(dt.get(l).longi))));
                                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(dt.get(l).lati), Double.parseDouble(dt.get(l).longi)), 10));
                                }
                            }
                        }
                    }, 5000);

                } else {
                    String API_BASE_URL = globalVars.url;
                    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                    Retrofit.Builder builder =
                            new Retrofit.Builder()
                                    .baseUrl(API_BASE_URL)
                                    .addConverterFactory(
                                            GsonConverterFactory.create()
                                    );

                    Retrofit retrofit =
                            builder
                                    .client(
                                            httpClient.build()
                                    )
                                    .build();
                    ArrayList<Double> locations = new ArrayList<Double>();
                    PosServicesInterface client = retrofit.create(PosServicesInterface.class);
                    Call<SalesOrderDataModel> call = client.getSalesOders(globalVars.salesmanId, globalVars.token);

                    call.enqueue(new Callback<SalesOrderDataModel>() {
                        @Override
                        public void onResponse(Call<SalesOrderDataModel> call, Response<SalesOrderDataModel> response) {
                            try {
                                adbSalesOrder = new AdapterSalesOrder(getActivity(), android.R.layout.simple_list_item_1, response.body().salesOrderDataObj.Data);
                                if (response.body().salesOrderDataObj.Data != null && response.body().salesOrderDataObj.Data.size() > 0) {
                                    for (int l = 0; l < response.body().salesOrderDataObj.Data.size(); l++) {
                                        if (response.body().salesOrderDataObj.Data.get(l).lati != null && response.body().salesOrderDataObj.Data.get(l).longi != null) {
                                            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(response.body().salesOrderDataObj.Data.get(l).lati), Double.parseDouble(response.body().salesOrderDataObj.Data.get(l).longi))));
                                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(response.body().salesOrderDataObj.Data.get(l).lati), Double.parseDouble(response.body().salesOrderDataObj.Data.get(l).longi)), 10));
                                        }
                                    }

                                    mListView.setAdapter(adbSalesOrder);
                                    mListView.setTextFilterEnabled(true);
                                    mListView.setClickable(true);
                                    mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                        @Override
                                        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                                       int position, long id) {
                                            boolean returnV = false;
                                            VariableData.inventoryVariable = null;
                                            try {
                                                SalesOrderRecord o = (SalesOrderRecord) mListView.getItemAtPosition(position);
                                                CustomerRecord cusRec = new CustomerRecord();

                                                cusRec.customerId = o.customerId;
                                                cusRec.fName = o.fName;
                                                cusRec.lName = o.lName;
                                                cusRec.phone1 = o.phone;
                                                cusRec.orderId = o.salesOrderId;
                                                CustomerVariableData.customerVariable = cusRec;
                                                getCustomerPoints(o.customerId);
                                                Thread.sleep(3000);
                                                ArrayList<Inventory> invList = new ArrayList<Inventory>();
                                                List<InventoryRecord> listOfInventory = InventoryRecord.findAllRecords(InventoryRecord.class);

                                                if (listOfInventory.size() > 0) {
                                                    for (int i = 0; i < listOfInventory.size(); i++) {

                                                        Inventory inv = new Inventory();
                                                        inv.description = listOfInventory.get(i).description;
                                                        inv.itemNum = listOfInventory.get(i).itemNum;
                                                        inv.priceSold = listOfInventory.get(i).sellingPrice;
                                                        inv.sellingPrice = listOfInventory.get(i).sellingPrice;
                                                        inv.taxPercentage = listOfInventory.get(i).taxPercentage;
                                                        inv.points = "0";
                                                        inv.qty = String.valueOf(0);
                                                        inv.sellingPrice = listOfInventory.get(i).sellingPrice;
                                                        inv.TotalItems = 0;
                                                        invList.add(inv);

                                                    }
                                                }

                                                if (o.SalesDetails.size() > 0) {
                                                    for (int r = 0; r < o.SalesDetails.size(); r++) {
                                                        String classId = o.SalesDetails.get(r).itemNum.substring(0, 3);
                                                        if (classId.equals("017")) {
                                                            Inventory inv = new Inventory();
                                                            inv.description = o.SalesDetails.get(r).description;
                                                            inv.itemNum = o.SalesDetails.get(r).itemNum;

                                                            inv.taxPercentage = o.SalesDetails.get(r).taxPercentage;
                                                            inv.qty = "1";
                                                            if (o.SalesDetails.get(r).priceValue == null) {
                                                                o.SalesDetails.get(r).priceValue = String.valueOf(0);
                                                            }
                                                            inv.sellingPrice = o.SalesDetails.get(r).priceValue;
                                                            inv.priceSold = o.SalesDetails.get(r).priceValue;
                                                            inv.points = o.SalesDetails.get(r).points;
                                                            inv.TotalItems = 1;
                                                            invList.add(inv);

                                                        }

                                                        int totapnts = 0;
                                                        if (invList.size() > 0) {
                                                            for (int i = 0; i < invList.size(); i++) {
                                                                if (o.SalesDetails.get(r).itemNum.equals(invList.get(i).itemNum)) {
                                                                    invList.get(i).TotalItems = Integer.parseInt(o.SalesDetails.get(r).qty);
                                                                    invList.get(i).qty = o.SalesDetails.get(r).qty;
                                                                    invList.get(i).description = o.SalesDetails.get(r).description;
                                                                    invList.get(i).taxPercentage = o.SalesDetails.get(r).taxPercentage;
                                                                    invList.get(i).points = o.SalesDetails.get(r).pointsValue;
                                                                    totapnts = totapnts + Integer.valueOf(o.SalesDetails.get(r).pointsValue);
                                                                }
                                                            }
                                                            CustomerVariableData.NewPoints = totapnts;
                                                        }
                                                    }
                                                }
                                                GlobalVariables.IsSalesOrder = true;
                                                VariableData.inventoryVariable = invList;
                                                Fragment fragment = null;
                                                fragment = new InvoiceCreateFragment();
                                                replaceFragment(fragment);
                                                returnV = true;

                                            } catch (Exception ex) {
                                                returnV = false;
                                                return returnV;
                                            }
                                            return returnV;
                                        }
                                    });
                                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                        @Override
                                        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                                            SalesOrderRecord o = (SalesOrderRecord) mListView.getItemAtPosition(position);
                                            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
                                            if (o.lati != null && o.longi != null) {
                                                currentLocation = LocationData.location;
                                                MarkerOptions mo = new MarkerOptions().position(new LatLng(Double.parseDouble(o.lati), Double.parseDouble(o.longi))).title(o.address).visible(true);
                                                Marker marker = mGoogleMap.addMarker(mo);
                                                mo.anchor(0f, 0.5f);
                                                marker.showInfoWindow();

                                                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(o.lati), Double.parseDouble(o.longi)), 10));
                                            } else {
                                                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(43.673731, -79.337363)));
                                                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.673731, -79.337363), 10));
                                            }

                                        }
                                    });
                                }
                            } catch (Exception e) {
                                Utilities.LogException(e);
                            }
                        }

                        @Override
                        public void onFailure(Call<SalesOrderDataModel> call, Throwable t) {

                        }
                    });

                }

            }
        }, 1000);


        new Thread(new Runnable() {
            public void run() {
                try {
                    getLoc();
                } catch (Exception e) {
                    Utilities.LogException(e);
                }

            }
        }).start();


        mListView.setAdapter(adbSalesOrder);
        mListView.setTextFilterEnabled(true);
        mListView.setClickable(true);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long id) {
                boolean returnV = false;
                VariableData.inventoryVariable = null;
                try {
                    SalesOrderRecord o = (SalesOrderRecord) mListView.getItemAtPosition(position);
                    CustomerRecord cusRec = new CustomerRecord();

                    cusRec.customerId = o.customerId;
                    cusRec.fName = o.fName;
                    cusRec.lName = o.lName;
                    cusRec.phone1 = o.phone;
                    cusRec.orderId = o.salesOrderId;
                    cusRec.address = o.address;
                    CustomerVariableData.customerVariable = null;
                    CustomerVariableData.customerVariable = cusRec;
                    getCustomerPoints(o.customerId);
                    Thread.sleep(3000);
                    ArrayList<Inventory> invList = new ArrayList<Inventory>();
                    List<InventoryRecord> listOfInventory = InventoryRecord.findAllRecords(InventoryRecord.class);

                    for (int i = 0; i < listOfInventory.size(); i++) {

                        Inventory inv = new Inventory();
                        inv.description = listOfInventory.get(i).description;
                        inv.itemNum = listOfInventory.get(i).itemNum;
                        inv.priceSold = listOfInventory.get(i).sellingPrice;
                        inv.sellingPrice = listOfInventory.get(i).sellingPrice;
                        inv.taxPercentage = listOfInventory.get(i).taxPercentage;
                        inv.points = "0";
                        inv.qty = String.valueOf(0);
                        inv.sellingPrice = listOfInventory.get(i).sellingPrice;
                        inv.TotalItems = 0;
                        invList.add(inv);

                    }

                    for (int r = 0; r < o.SalesDetails.size(); r++) {
                        String classId = o.SalesDetails.get(r).itemNum.substring(0, 3);
                        if (classId.equals("017")) {
                            Inventory inv = new Inventory();
                            inv.description = o.SalesDetails.get(r).description;
                            inv.itemNum = o.SalesDetails.get(r).itemNum;

                            inv.taxPercentage = o.SalesDetails.get(r).taxPercentage;
                            inv.qty = "1";
                            if (o.SalesDetails.get(r).priceValue == null) {
                                o.SalesDetails.get(r).priceValue = String.valueOf(0);
                            }
                            inv.sellingPrice = o.SalesDetails.get(r).priceValue;
                            inv.priceSold = o.SalesDetails.get(r).priceValue;
                            inv.points = o.SalesDetails.get(r).points;
                            inv.TotalItems = 1;
                            invList.add(inv);

                        }
                        int totapnts = 0;
                        for (int i = 0; i < invList.size(); i++) {
                            if (o.SalesDetails.get(r).itemNum.equals(invList.get(i).itemNum)) {
                                invList.get(i).TotalItems = Integer.parseInt(o.SalesDetails.get(r).qty);
                                invList.get(i).qty = o.SalesDetails.get(r).qty;
                                invList.get(i).description = o.SalesDetails.get(r).description;
                                invList.get(i).taxPercentage = o.SalesDetails.get(r).taxPercentage;
                                invList.get(i).points = o.SalesDetails.get(r).points;
                                totapnts = totapnts + Integer.valueOf(o.SalesDetails.get(r).points);
                            }
                        }
                        CustomerVariableData.NewPoints = totapnts;
                    }

                    GlobalVariables.IsSalesOrder = true;
                    VariableData.inventoryVariable = invList;
                    Fragment fragment = null;
                    fragment = new InvoiceCreateFragment();
                    replaceFragment(fragment);
                    returnV = true;
                } catch (Exception ex) {
                    returnV = false;
                    return returnV;
                }
                return returnV;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                SalesOrderRecord o = (SalesOrderRecord) mListView.getItemAtPosition(position);
                mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
                if (o.lati != null && o.longi != null) {
                    currentLocation = LocationData.location;
                    MarkerOptions mo = new MarkerOptions().position(new LatLng(Double.parseDouble(o.lati), Double.parseDouble(o.longi))).title(o.address).visible(true);
                    Marker marker = mGoogleMap.addMarker(mo);
                    mo.anchor(0f, 0.5f);
                    marker.showInfoWindow();

                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(o.lati), Double.parseDouble(o.longi)), 10));
                } else {
                    mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(43.673731, -79.337363)));
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.673731, -79.337363), 10));
                }

            }
        });

        return view;

    }


    public void populateBGstatus() {


        mHandler.post(new Runnable() {
            @Override
            public void run() {
                String recordinfo = "";
                ArrayList<CustomerDataInfoRecord> cdi = CustomerDataInfoRecord.findAllRecords(CustomerDataInfoRecord.class);
                long totalcustomersinLocaldb = CustomerRecord.count(CustomerRecord.class);
                int totalLocRec = 0;
                if (cdi != null && cdi.size() > 0) {
                    if (cdi.size() > 1) {
                        totalLocRec = cdi.get(cdi.size() - 1).expectedtotalRecords;
                    } else {
                        totalLocRec = cdi.get(0).expectedtotalRecords;
                    }
                }
                if (totalLocRec > 0 && totalcustomersinLocaldb >= totalLocRec) {
                    recordinfo = "Finished Syncing";
                } else if (totalcustomersinLocaldb == 0) {
                    recordinfo = "Initializing Sync...";
                } else {
                    recordinfo = "Please Wait. Syncing Customers ... " + totalcustomersinLocaldb + " of " + totalLocRec;
                }


                input_bgprocess.setText(recordinfo);
            }
        });


    }

    public void getCustomerPoints(String e) {
        CustomerRecord rec = MyPosBase.GetCustomerRecord(e);

        if (rec != null) {
            if (CustomerVariableData.customerVariable == null) {
                CustomerVariableData.customerVariable = new CustomerRecord();
            }
            CustomerVariableData.customerVariable.points = rec.points;
        } else {
            String API_BASE_URL = globalVars.url;
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            Retrofit.Builder builder =
                    new Retrofit.Builder()
                            .baseUrl(API_BASE_URL)
                            .addConverterFactory(
                                    GsonConverterFactory.create()
                            );

            Retrofit retrofit =
                    builder
                            .client(
                                    httpClient.build()
                            )
                            .build();

            PosServicesInterface client = retrofit.create(PosServicesInterface.class);
            Call<CustomerDataModel> call = client.getCustomer(globalVars.realmId, globalVars.token, e);
            call.enqueue(new Callback<CustomerDataModel>() {
                @Override
                public void onResponse(Call<CustomerDataModel> call, Response<CustomerDataModel> response) {
                    try {
                        if (CustomerVariableData.customerVariable == null) {
                            CustomerVariableData.customerVariable = new CustomerRecord();
                        }
                        CustomerVariableData.customerVariable.points = response.body().dataObj.Data.get(0).points;
                    } catch (Exception ex) {
                        Utilities.LogException(ex);
                    }
                }

                @Override
                public void onFailure(Call<CustomerDataModel> call, Throwable t) {
                    try {
                    } catch (Exception ex) {
                        Utilities.LogException(ex);
                    }
                }
            });
        }


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void getLoc() {
        Criteria criteria = new Criteria();
        LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, true);
        requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        boolean gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gps_enabled) {
            buildAlertMessageNoGps();
        } else {
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                myPosition = new LatLng(latitude, longitude);
            } else {
                myPosition = new LatLng(0, 0);
            }

        }


    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

        if (LocationData.location != null) {
            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 10));
        } else if (LocationData.location == null && myPosition != null) {
            mGoogleMap.setMyLocationEnabled(true);

            mGoogleMap.setMinZoomPreference(6.0f);
            mGoogleMap.setMaxZoomPreference(14.0f);
            LocationManager locationManager = (LocationManager)
                    getContext().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            double latitude = 0;
            double longitude = 0;
            Location location = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(criteria, false));
            if (location == null) {
                latitude = 18.015312;
                longitude = -76.805637;
            } else {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 10));
            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
        }

    }

    private void buildAlertMessageNoGps() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(" Your GPS seems to be disabled, do you want to enable it?")
                        .setCancelable(false)
                        .setPositiveButton("ّyes", new DialogInterface.OnClickListener() {
                            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                try {
                                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                } catch (Exception ex) {
                                    Utilities.LogException(ex);
                                }

                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        if (mGoogleMap != null) {
            currentLocation = LocationData.location;
            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

            if (LocationData.location != null) {
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(LocationData.location.getLatitude(), LocationData.location.getLongitude())));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LocationData.location.getLatitude(), LocationData.location.getLongitude()), 10));
            } else {

                mGoogleMap.setMinZoomPreference(6.0f);
                mGoogleMap.setMaxZoomPreference(14.0f);
                LocationManager locationManager = (LocationManager)
                        getContext().getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();


                final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (locationManager
                            .getBestProvider(criteria, false) != null) {
                        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(locationManager
                                .getBestProvider(criteria, false));
                        double latitude = 0;
                        double longitude = 0;
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        } else {
                            latitude = 0;
                            longitude = 0;
                        }

                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 10));
                        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
                    }
                } else {
                    int y = 9;
                }
            }

        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrame, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
