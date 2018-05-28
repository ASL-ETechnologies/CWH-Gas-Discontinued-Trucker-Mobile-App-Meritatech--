package com.meritatech.myrewardzpos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.meritatech.myrewardzpos.controller.GlobalVariables;
import com.meritatech.myrewardzpos.global.InventoryVariableData;
import com.meritatech.myrewardzpos.global.SalesOrderVariableData;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditInventoryItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditInventoryItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditInventoryItemFragment extends android.support.v4.app.DialogFragment
        implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EditInventoryItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditInventoryItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditInventoryItemFragment newInstance(String param1, String param2) {
        EditInventoryItemFragment fragment = new EditInventoryItemFragment();
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

        View view = inflater.inflate(R.layout.fragment_edit_inventory_item, container, false);
        if (InventoryVariableData.inventoryVariable != null) {
            TextView input_description = (TextView) view.findViewById(R.id.input_description);
            input_description.setText(InventoryVariableData.inventoryVariable.description);
            TextView input_itemNumber = (TextView) view.findViewById(R.id.input_itemNumber);
            input_itemNumber.setText(InventoryVariableData.inventoryVariable.itemNum);
            TextView input_price = (TextView) view.findViewById(R.id.input_price);
            input_price.setText(InventoryVariableData.inventoryVariable.priceSold);
            TextView input_qty = (TextView) view.findViewById(R.id.input_qty);
            input_qty.setText(InventoryVariableData.inventoryVariable.qty);
            if (GlobalVariables.allowPriceChange.equals("1")) {
                input_price.setEnabled(true);
            }
        }
        Button btnCancel = (Button) view.findViewById(R.id.buttoncancel);
        btnCancel.setOnClickListener(this);
        Button btnSave = (Button) view.findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(this);


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
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buttoncancel:
                onDestroyView();
                break;
            case R.id.buttonSave:
                if (SalesOrderVariableData.salesOrderVariable != null) {
                    for (int i = 0; i < SalesOrderVariableData.salesOrderVariable.size(); i++) {
                        if (SalesOrderVariableData.salesOrderVariable.get(i).isEditMode != null) {
                            if (SalesOrderVariableData.salesOrderVariable.get(i).isEditMode) {
                                TextView qty = (TextView) this.getView().findViewById(R.id.input_qty);
                                SalesOrderVariableData.salesOrderVariable.get(i).qty = String.valueOf(qty.getText());
                                TextView price = (TextView) this.getView().findViewById(R.id.input_price);
                                SalesOrderVariableData.salesOrderVariable.get(i).sellingPrice = String.valueOf(price.getText());
                                SalesOrderVariableData.salesOrderVariable.get(i).priceSold = String.valueOf(price.getText());
                                SalesOrderVariableData.salesOrderVariable.get(i).isEditMode = false;
                            }
                        }
                    }
                }
                //hide keyboard
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                onDestroyView();
                break;
            default:
                break;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //   getTargetFragment().isResumed();
        getTargetFragment().onActivityResult(getTargetRequestCode(), 3, getActivity().getIntent());
    }

}
