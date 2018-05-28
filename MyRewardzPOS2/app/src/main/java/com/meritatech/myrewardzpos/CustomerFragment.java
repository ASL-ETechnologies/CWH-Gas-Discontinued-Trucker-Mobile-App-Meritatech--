package com.meritatech.myrewardzpos;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.meritatech.myrewardzpos.controller.AdapterCustomerDialog;
import com.meritatech.myrewardzpos.controller.EndlessScrollListener;
import com.meritatech.myrewardzpos.controller.GlobalVariables;
import com.meritatech.myrewardzpos.data.CustomerRecord;
import com.meritatech.myrewardzpos.data.MyPosBase;
import com.meritatech.myrewardzpos.global.CustomerActivityChecker;
import com.meritatech.myrewardzpos.global.CustomerDetailsVariableData;
import com.meritatech.myrewardzpos.global.CustomerVariableData;
import com.meritatech.myrewardzpos.presenter.Presenter;
import com.meritatech.myrewardzpos.view.CustomerView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CustomerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CustomerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerFragment extends Fragment
        implements View.OnClickListener, CustomerView {
    private Presenter presenter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ListView mListView;
    GlobalVariables globalVars = new GlobalVariables();
    private SearchView mSearchView;
    ArrayList<CustomerRecord> adapterData = new ArrayList<CustomerRecord>();
    final AdapterCustomerDialog[] adbCustomer = new AdapterCustomerDialog[1];

    public CustomerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerFragment newInstance(String param1, String param2) {
        CustomerFragment fragment = new CustomerFragment();
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
        final View view = inflater.inflate(R.layout.fragment_customer, container, false);
        TextView customerCnt = (TextView) view.findViewById(R.id.customerCountTxt);
        Button addCustomerBtn = (Button) view.findViewById(R.id.buttonfiltercontact);
        addCustomerBtn.setOnClickListener(this);
        getActivity().setTitle("Customers");
        final ProgressDialog progress = new ProgressDialog(getContext());
        progress.setTitle("Loading");
        progress.setMessage("loading contacts...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        ArrayList<CustomerRecord> customerList = new ArrayList<CustomerRecord>();
        MyPosBase myPosBase = new MyPosBase();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
            }
        }, 100);

        if (!GlobalVariables.isConnected(getContext())) {

            long cnt =  CustomerRecord.count(CustomerRecord.class);

            if(cnt > 0) {
                customerCnt.setText(cnt + " Customers found!");
            }
            else {
                customerCnt.setText(0 + " Customers found!");
            }
            List<CustomerRecord> listOfCustomer = myPosBase.GetCustomers("0");
            if (listOfCustomer != null) {
                for (int i = 0; i <= listOfCustomer.size() - 1; i++) {
                    String lname = "";
                    String fname = "";
                    CustomerRecord cus = new CustomerRecord(lname);
                    cus.fName = listOfCustomer.get(i).fName;
                    cus.lName = listOfCustomer.get(i).lName;
                    cus.phone1 = listOfCustomer.get(i).phone1;
                    cus.phone2 = listOfCustomer.get(i).phone2;
                    cus.address = listOfCustomer.get(i).address;
                    cus.customerId = listOfCustomer.get(i).customerId;
                    cus.email = listOfCustomer.get(i).email;
                    cus.lati = listOfCustomer.get(i).lati;
                    cus.longi = listOfCustomer.get(i).longi;
                    cus.points = listOfCustomer.get(i).points;

                    cus.customerId = listOfCustomer.get(i).customerId;
                    customerList.add(cus);
                }
            }

            adbCustomer[0] = new AdapterCustomerDialog(getActivity(), R.layout.customerlistlayout, customerList);
            mListView = (ListView) view.findViewById(R.id.list_view_contactsmain);
            mListView.addFooterView(new ProgressBar(getContext()));
            mListView.setAdapter(adbCustomer[0]);
            mListView.setTextFilterEnabled(true);
            mListView.setClickable(true);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    CustomerRecord obj = adbCustomer[0].ICustomer.get(position);
                    CustomerVariableData.customerVariable = obj;
                    CustomerDetailsVariableData.customerDetails = obj;

                    CustomerFullDetailsFragment customerDetailsFragment = new CustomerFullDetailsFragment();
                    customerDetailsFragment.setTargetFragment(CustomerFragment.this, 1);
                    customerDetailsFragment.show(getFragmentManager(), "customer");

                }
            });
            mListView.setOnScrollListener(new EndlessScrollListener() {
                @Override
                public boolean onLoadMore(int page, int totalItemsCount) {
                    // Triggered only when new data needs to be appended to the list
                    // Add whatever code is needed to append new items to your AdapterView
                    loadItems(page);

                    // or loadNextDataFromApi(totalItemsCount);
                    return true; // ONLY if more data is actually being loaded; false otherwise.
                }
            });
        } else {
            customerCnt.setVisibility(View.GONE);
            ApiUtility interactor = new ApiUtility();
            presenter = new Presenter(interactor);
            presenter.bind(this);

            presenter.getCustomers("1", globalVars.token, null, 20);
            adbCustomer[0] = new AdapterCustomerDialog(getActivity(), R.layout.customerlistlayout, adapterData);
            mListView = (ListView) view.findViewById(R.id.list_view_contactsmain);
            mListView.addFooterView(new ProgressBar(getContext()));
            mListView.setAdapter(adbCustomer[0]);
            mListView.setTextFilterEnabled(true);
            mListView.setClickable(true);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    CustomerRecord obj = adbCustomer[0].ICustomer.get(position);
                    CustomerVariableData.customerVariable = obj;
                    CustomerDetailsVariableData.customerDetails = obj;
                    CustomerFullDetailsFragment customerDetailsFragment = new CustomerFullDetailsFragment();
                    customerDetailsFragment.setTargetFragment(CustomerFragment.this, 1);
                    customerDetailsFragment.show(getFragmentManager(), "customer");

                }
            });
            mListView.setOnScrollListener(new EndlessScrollListener() {
                @Override
                public boolean onLoadMore(int page, int totalItemsCount) {
                    loadItems(totalItemsCount);
                    return true; // ONLY if more data is actually being loaded; false otherwise.
                }
            });

        }

        // Inflate the layout for this fragment

        mSearchView = (SearchView) view.findViewById(R.id.searchContactsmain);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    mListView.clearTextFilter();
                } else {
                    mListView.setFilterText(newText);
                }
                return true;
            }
        });

        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
        progress.dismiss();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    final boolean[] resp = {false};
    int pageItems = 20;

    public boolean[] loadItems(int pagestart) {
        int skip = (pagestart -1) * 20;
        if (!GlobalVariables.isConnected(getContext())) {
            MyPosBase myPosBase = new MyPosBase();

            List<CustomerRecord> listOfCustomer = myPosBase.GetCustomers(String.valueOf(skip));
            for (int f = 0; f < listOfCustomer.size(); f++) {
                adbCustomer[0].ICustomer.add(listOfCustomer.get(f));
            }
            if (adbCustomer != null) {
                adbCustomer[0].notifyDataSetChanged();
            }
            return resp;
        } else {

            presenter.getCustomers("1", globalVars.token, skip, 20);
            if (adapterData != null && adapterData.size() > 0) {
                for (int f = 0; f < noOfRecords; f++) {
                    adbCustomer[0].ICustomer.add(adapterData.get(f));
                }

                if (adbCustomer != null) {
                    adbCustomer[0].notifyDataSetChanged();
                }
                resp[0] = true;
            }
        }
        return resp;
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
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buttonfiltercontact:
                CustomerDialogFragment customerDialogFragment = new CustomerDialogFragment();
                customerDialogFragment.setTargetFragment(CustomerFragment.this, 1);
                customerDialogFragment.show(getFragmentManager(), "customer");
                break;
            default:
                break;
        }
    }

    int noOfRecords = 0;

    @Override
    public void updateUi(ArrayList<CustomerRecord> customers) {
        if (customers.isEmpty()) {
            // if no books found, show a message

        } else {
            if (customers != null) {
                noOfRecords = customers.size();
            }
            adapterData = customers;
            if(adbCustomer[0].ICustomer.size() ==0 || adbCustomer[0].ICustomer == null) {
                adbCustomer[0].ICustomer = customers;
            }
            adbCustomer[0].notifyDataSetChanged();
        }

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
}
