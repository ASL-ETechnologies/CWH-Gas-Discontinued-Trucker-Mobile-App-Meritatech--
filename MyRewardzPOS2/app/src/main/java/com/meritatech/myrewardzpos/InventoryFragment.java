package com.meritatech.myrewardzpos;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.meritatech.myrewardzpos.AutoSyncServices.ApiClient;
import com.meritatech.myrewardzpos.controller.AdapterInventory;
import com.meritatech.myrewardzpos.controller.GlobalVariables;
import com.meritatech.myrewardzpos.controller.Inventory;
import com.meritatech.myrewardzpos.data.InventoryListAPI;
import com.meritatech.myrewardzpos.data.InventoryRecord;
import com.meritatech.myrewardzpos.data.MyPosBase;
import com.meritatech.myrewardzpos.database.SugarContext;
import com.meritatech.myrewardzpos.interfaces.PosServicesInterface;
import com.meritatech.myrewardzpos.presenter.Presenter;
import com.meritatech.myrewardzpos.utility.Utilities;
import com.meritatech.myrewardzpos.view.InventoryView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventoryFragment extends Fragment
        implements InventoryView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Presenter presenter;
    final AdapterInventory[] adbInventory = new AdapterInventory[1];
    ArrayList<Inventory> adapterData = new ArrayList<Inventory>();
    private ListView mListView;
    GlobalVariables globalVars = new GlobalVariables();

    private OnFragmentInteractionListener mListener;

    public InventoryFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static InventoryFragment newInstance(String param1, String param2) {
        InventoryFragment fragment = new InventoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SugarContext.init(MainActivity.appContext);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Inventory");

        final ProgressDialog progress = new ProgressDialog(getContext());
        progress.setTitle("Loading");
        progress.setMessage("loading Inventory...");
        progress.setCancelable(false);
        ApiUtility interactor = new ApiUtility();
        presenter = new Presenter(interactor);
        presenter.bind(this);

        PosServicesInterface inventoryInterface =
                ApiClient.getClient().create(PosServicesInterface.class);
        Call<InventoryListAPI> call = inventoryInterface.inventoryList(GlobalVariables.ParentId, GlobalVariables.storeId, GlobalVariables.token, GlobalVariables.salesmanId);

        call.enqueue(new Callback<InventoryListAPI>() {
            @Override
            public void onResponse(Call<InventoryListAPI> call, Response<InventoryListAPI> response) {
                try {
                    InventoryListAPI reesponse = response.body();
                    final ArrayList<InventoryRecord> records = InventoryRecord.findAllRecords(InventoryRecord.class);
                    final MyPosBase myPosBase = new MyPosBase();
                    if (records != null && records.size() < reesponse.DataObj.Data.size()) {
                        if (response.body().StatusCode == 0) {
                            InventoryRecord.deleteAll(InventoryRecord.class);
                            myPosBase.saveInventory(reesponse.DataObj.Data);
                        }
                    } else {
                        if (response.body().StatusCode == 0) {
                            InventoryRecord.deleteAll(InventoryRecord.class);
                            myPosBase.saveInventory(reesponse.DataObj.Data);
                        }
                    }
                } catch (Exception e) {
                    Utilities.LogException(e);
                }
            }

            @Override
            public void onFailure(Call<InventoryListAPI> call, Throwable t) {
                try {
                } catch (Exception e) {
                    Utilities.LogException(e);
                }

            }
        });



        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        ImageView refreshView = view.findViewById(R.id.refreshInv);
        refreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.show();
                InventoryListAPI inventoryListAPI = new InventoryListAPI();
                inventoryListAPI.InventoryListAPICall();
                getFragmentManager().beginTransaction().detach(InventoryFragment.this).attach(InventoryFragment.this).commit();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progress.dismiss();
            }
        });

        mListView = (ListView) view.findViewById(R.id.list_view_inventory);

        //populate myListItems
        adbInventory[0] = new AdapterInventory(getActivity(),  R.layout.inventorymainlayout, adapterData);
        presenter.getInventory();
        mListView.setAdapter(adbInventory[0]);
        mListView.setTextFilterEnabled(true);
        progress.dismiss();
        return view;
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

    @Override
    public void updateUi(ArrayList<Inventory> inventory) {

        if (inventory.isEmpty()) {
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(getContext(), "No Records Found!", duration);
            toast.show();

        } else {

            adapterData = inventory;
            if (adbInventory[0].IInventory.size() == 0 || adbInventory[0].IInventory == null) {
                adbInventory[0].IInventory = inventory;
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(getContext(), adbInventory[0].IInventory.size() + " Records Found!", duration);
                toast.show();
            } else {
                adbInventory[0].IInventory = inventory;
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(getContext(), adbInventory[0].IInventory.size() + " Records Found!", duration);
                toast.show();
            }
            adbInventory[0].notifyDataSetChanged();
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
