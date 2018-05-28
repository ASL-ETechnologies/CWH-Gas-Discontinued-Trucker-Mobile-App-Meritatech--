package com.meritatech.myrewardzpos;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;


import android.widget.SearchView;
import android.widget.Toast;

import com.meritatech.myrewardzpos.controller.AdapterSalesOrder;
import com.meritatech.myrewardzpos.controller.GlobalVariables;
import com.meritatech.myrewardzpos.controller.Inventory;
import com.meritatech.myrewardzpos.controller.SalesOrder;
import com.meritatech.myrewardzpos.data.CustomerRecord;
import com.meritatech.myrewardzpos.data.InventoryRecord;
import com.meritatech.myrewardzpos.data.MyPosBase;
import com.meritatech.myrewardzpos.data.SalesOrderInventoryRecord;
import com.meritatech.myrewardzpos.data.SalesOrderRecord;
import com.meritatech.myrewardzpos.data.SalesOrderRecordListAPI;
import com.meritatech.myrewardzpos.data.SalesRecords;
import com.meritatech.myrewardzpos.global.CustomerVariableData;
import com.meritatech.myrewardzpos.global.VariableData;
import com.meritatech.myrewardzpos.interfaces.SalesOrderInterface;
import com.meritatech.myrewardzpos.model.CustomerDataModel;
import com.meritatech.myrewardzpos.model.PosServicesInterface;
import com.meritatech.myrewardzpos.model.SalesOrderDataModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SalesOrderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SalesOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SalesOrderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int clickNo = 0;
    public AdapterSalesOrder adbSalesOrder;
    private OnFragmentInteractionListener mListener;

    public SalesOrderFragment() {
        // Required empty public constructor
    }

    public static SalesOrderFragment newInstance(String param1, String param2) {
        SalesOrderFragment fragment = new SalesOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    private SearchView mSearchView;
    private ListView mListView;
    GlobalVariables globalVars = new GlobalVariables();
    String output = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Sales Order");
        final ProgressDialog progress = new ProgressDialog(getContext());

        progress.setTitle("Loading");
        progress.setMessage("loading sales orders...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        final View view = inflater.inflate(R.layout.fragment_service_order, container, false);
        mSearchView = (SearchView) view.findViewById(R.id.searchSO);
        mListView = (ListView) view.findViewById(R.id.list_view_soss);
        ImageView refreshView = view.findViewById(R.id.refreshSO);
        refreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SalesOrderRecordListAPI salesOrderRecordListAPI = new SalesOrderRecordListAPI();
                salesOrderRecordListAPI.SalesOrderRecordListAPICall();
                getFragmentManager().beginTransaction().detach(SalesOrderFragment.this).attach(SalesOrderFragment.this).commit();
            }
        });
        ArrayList<SalesOrder> salesOrderList = new ArrayList<>();
        List<SalesRecords> listOfSalesOrder = SalesRecords.findAllRecords(SalesRecords.class);
        MyPosBase myPosBase = new MyPosBase();
        ArrayList<SalesOrderRecord> dt = SalesOrder.findAllRecords(SalesOrderRecord.class);

        if (dt != null && dt.size() > 0) {
            progress.dismiss();
            for (int i = 0; i < dt.size(); i++) {
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
            adbSalesOrder = new AdapterSalesOrder(getActivity(), R.layout.salesorderlistlayout, dt);
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
                        cusRec.phone2 = o.phone2;
                        cusRec.address=o.address;
                        cusRec.orderId = o.salesOrderId;

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
                            CustomerVariableData.NewPoints =  totapnts;
                        }

                        GlobalVariables.IsSalesOrder = true;
                        VariableData.inventoryVariable = invList;
                        Fragment fragment = null;
                        fragment = new InvoiceCreateFragment();
                        replaceFragment(fragment);
                        returnV = true;
                    } catch (Exception ex) {
                        Utilities.LogException(ex);
                    }

                    return returnV;
                }


            });
            progress.dismiss();
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
            Call<SalesOrderDataModel> call = client.getSalesOders(globalVars.salesmanId, globalVars.token);
            progress.show();
            call.enqueue(new Callback<SalesOrderDataModel>() {
                @Override
                public void onResponse(Call<SalesOrderDataModel> call, Response<SalesOrderDataModel> response) {
                    try {

                        if (response.body().salesOrderDataObj.Data.size() == 0 || response.body().salesOrderDataObj.Data == null) {

                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(getContext(), "No Records Found!", duration);
                            toast.show();

                        }
                        adbSalesOrder = new AdapterSalesOrder(getActivity(), R.layout.salesorderlistlayout, response.body().salesOrderDataObj.Data);
                        mListView.setAdapter(adbSalesOrder);
                        mListView.setTextFilterEnabled(true);
                        mListView.setClickable(true);

                        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                           int position, long id) {
                                boolean returnV = false;
                                try {


                                    VariableData.inventoryVariable = null;
                                    SalesOrderRecord o = (SalesOrderRecord) mListView.getItemAtPosition(position);
                                    CustomerRecord cusRec = new CustomerRecord();
                                    cusRec.customerId = o.customerId;
                                    getCustomerPoints(o.customerId);
                                    Thread.sleep(3000);
                                    cusRec.fName = o.fName;
                                    cusRec.lName = o.lName;
                                    cusRec.phone1 = o.phone;
                                    cusRec.phone2 =o.phone2;
                                    cusRec.address =o.address;
                                    cusRec.orderId = o.salesOrderId;
                                    CustomerVariableData.customerVariable = cusRec;
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
                                                invList.get(i).points = o.SalesDetails.get(r).pointsValue;
                                                totapnts = totapnts + Integer.valueOf(o.SalesDetails.get(r).pointsValue);
                                            }
                                        }
                                        CustomerVariableData.NewPoints =  totapnts;
                                    }


                                    VariableData.inventoryVariable = invList;
                                    GlobalVariables.IsSalesOrder = true;
                                    Fragment fragment = null;
                                    fragment = new InvoiceCreateFragment();
                                    replaceFragment(fragment);
                                    returnV = true;
                                    return returnV;
                                } catch (Exception e) {

                                }
                                return returnV;
                            }

                        });
                        progress.dismiss();
                    } catch (Exception ex) {
                        Utilities.LogException(ex);

                    }
                }

                @Override
                public void onFailure(Call<SalesOrderDataModel> call, Throwable t) {
                    try {
                        progress.dismiss();
                    } catch (Exception ex) {
                        Utilities.LogException(ex);
                    }
                }
            });
        }

        setupSearchView();
        return view;
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrame, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
    }

    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(newText.toString());
        }
        return true;
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
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
}
