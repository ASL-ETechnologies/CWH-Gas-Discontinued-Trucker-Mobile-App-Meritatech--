package com.meritatech.myrewardzpos;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.meritatech.myrewardzpos.controller.AdapterSalesInvoice;
import com.meritatech.myrewardzpos.controller.GlobalVariables;
import com.meritatech.myrewardzpos.controller.Inventory;
import com.meritatech.myrewardzpos.data.MyPosBase;
import com.meritatech.myrewardzpos.data.SalesInvoiceInventoryRecord;
import com.meritatech.myrewardzpos.data.SalesInvoiceRecord;

import java.util.ArrayList;


public class SalesInvoiceHistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListView mListView;
    public AdapterSalesInvoice adbSalesOrder;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    GlobalVariables globalVars = new GlobalVariables();
    private OnFragmentInteractionListener mListener;

    public SalesInvoiceHistoryFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SalesInvoiceHistoryFragment newInstance(String param1, String param2) {
        SalesInvoiceHistoryFragment fragment = new SalesInvoiceHistoryFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Sales Invoice History");
        final ProgressDialog progress = new ProgressDialog(getContext());
        progress.setTitle("Loading");
        progress.setMessage("loading sales orders...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        View view = inflater.inflate(R.layout.fragment_sales_order_history, container, false);

        mListView = (ListView) view.findViewById(R.id.list_view_si);

        ArrayList<SalesInvoiceRecord> listOfSalesInvoice = SalesInvoiceRecord.findAllRecords(SalesInvoiceRecord.class);
        MyPosBase myPosBase = new MyPosBase();
        ArrayList<SalesInvoiceInventoryRecord> inventoryItems = SalesInvoiceInventoryRecord.findAllRecords(SalesInvoiceInventoryRecord.class);

        if (listOfSalesInvoice != null && listOfSalesInvoice.size() > 0) {
            progress.dismiss();
            for (int i = 0; i < listOfSalesInvoice.size(); i++) {

                ArrayList<SalesInvoiceInventoryRecord> records = myPosBase.GetSalesInvoiceInventoryRecords(String.valueOf(listOfSalesInvoice.get(i).getId()));

                if (records != null && records.size() > 0) {
                    for (int e = 0; e < records.size(); e++) {
                        Inventory inventory = new Inventory();
                        inventory.description = records.get(e).description;
                        inventory.itemNum = records.get(e).itemNumber;
                        inventory.qty = records.get(e).qty;
                        inventory.sellingPrice = records.get(e).sellingPrice;
                        inventory.priceSold = records.get(e).priceSold;
                        inventory.points = records.get(e).points;
                        inventory.taxPercentage = records.get(e).taxPercentage;

                        if (listOfSalesInvoice.get(i).SalesDetails == null) {
                            listOfSalesInvoice.get(i).SalesDetails = new ArrayList<Inventory>();
                        }
                        listOfSalesInvoice.get(i).SalesDetails.add(inventory);
                        listOfSalesInvoice.get(i).salesOrderId = records.get(e).salesOrderID;
                    }
                }

            }
            adbSalesOrder = new AdapterSalesInvoice(getActivity(), R.layout.salesinvoicelistlayout, listOfSalesInvoice);
            mListView.setAdapter(adbSalesOrder);
            mListView.setTextFilterEnabled(true);
            mListView.setClickable(true);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {



                }

            });
            progress.dismiss();
        } else {
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(getContext(), "No Records Found!", duration);
            toast.show();
        }
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

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrame, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
