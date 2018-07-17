package com.meritatech.myrewardzpos;

import android.app.Activity;
//import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.meritatech.myrewardzpos.controller.AdapterInventoryDialog;
import com.meritatech.myrewardzpos.controller.GlobalVariables;
import com.meritatech.myrewardzpos.controller.Inventory;
import com.meritatech.myrewardzpos.data.InventoryRecord;
import com.meritatech.myrewardzpos.data.MyPosBase;
import com.meritatech.myrewardzpos.global.VariableData;
import com.meritatech.myrewardzpos.utility.Utilities;

import java.util.ArrayList;
import java.util.List;

public class InventotyDialogFragment extends android.support.v4.app.DialogFragment
        implements View.OnClickListener,
        SearchView.OnQueryTextListener {
    private SearchView mSearchView;

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }

    private EditNameDialogListener listener;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListView mListView;
    GlobalVariables globalVars = new GlobalVariables();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public InventotyDialogFragment() {
        // Required empty public constructor
    }

    public static InventotyDialogFragment newInstance(String param1, String param2) {
        InventotyDialogFragment fragment = new InventotyDialogFragment();
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


        getActivity().setTitle("Inventory");
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_inventoty_dialog, container, false);
        mSearchView = (SearchView) view.findViewById(R.id.inventorySearch);
        ArrayList<Inventory> inventoryList = new ArrayList<>();
        MyPosBase myPosBase = new MyPosBase();
        List<InventoryRecord> listOfInventory = InventoryRecord.findAllRecords(InventoryRecord.class);

        if (listOfInventory != null) {
            for (int i = 0; i <= listOfInventory.size() - 1; i++) {
                String lname = "";
                String fname = "";
                Inventory inv = new Inventory();
                inv.description = listOfInventory.get(i).description;
                inv.itemNum = listOfInventory.get(i).itemNum;
                inv.sellingPrice = listOfInventory.get(i).sellingPrice;
                inv.taxPercentage = listOfInventory.get(i).taxPercentage;

                inventoryList.add(inv);
            }
        }

        //  mSearchView = (SearchView) view.findViewById(R.id.searchSO);
        mListView = (ListView) view.findViewById(R.id.listviewinventoryItems);
        final AdapterInventoryDialog adbInventory;
        //populate myListItems
        ArrayList<Inventory> existingdata = VariableData.inventoryVariable;
        if (existingdata != null) {
            adbInventory = new AdapterInventoryDialog(getActivity(), R.layout.inventorylistlayout, existingdata);


        } else {
            adbInventory = new AdapterInventoryDialog(getActivity(), R.layout.inventorylistlayout, inventoryList);
        }

        Button saveBtn = (Button) view.findViewById(R.id.buttonSaveSelInventory);
        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                onDestroyView();
            }
        });

        Button cancelInv = (Button) view.findViewById(R.id.button_cancelselectinventory);
        cancelInv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //   VariableData.inventoryVariable = VariableData.inventoryBeforeSaveVariable;
                onDestroyView();
            }
        });
        //close dialog btn
        ImageView viewbtn = (ImageView) view.findViewById(R.id.imageView_close);
        viewbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                onDestroyView();
            }
        });

        mListView.setAdapter(adbInventory);
        mListView.setTextFilterEnabled(true);
        mListView.setClickable(false);

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
        mSearchView.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {

                                           }
                                       }
        );
        ;
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");


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
        try {
            // Instantiate the EditNameDialogListener so we can send events to the host
            listener = (EditNameDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement EditNameDialogListener");
        }
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
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        try {
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);

        }
        //listen
        Intent i = new Intent();

        getTargetFragment().isResumed();
        getTargetFragment().onActivityResult(getTargetRequestCode(), 1, getActivity().getIntent());
    } catch (Exception ex) {
        Utilities.LogException(ex);
    }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            //listen
            Intent i = new Intent();
            getTargetFragment().isResumed();
            getTargetFragment().onActivityResult(getTargetRequestCode(), 1, getActivity().getIntent());
        } catch (Exception ex) {
            Utilities.LogException(ex);
        }
    }


    @Override
    public void onSaveInstanceState(final Bundle outState) {
        setTargetFragment(null, -1);
    }

}
