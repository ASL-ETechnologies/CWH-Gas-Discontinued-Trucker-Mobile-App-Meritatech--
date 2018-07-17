package com.meritatech.myrewardzpos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meritatech.myrewardzpos.global.CustomerDetailsVariableData;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CustomerFullDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CustomerFullDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerFullDetailsFragment extends android.support.v4.app.DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CustomerFullDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerFullDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerFullDetailsFragment newInstance(String param1, String param2) {
        CustomerFullDetailsFragment fragment = new CustomerFullDetailsFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_full_details, container, false);
        TextView fname = (TextView) view.findViewById(R.id.cfname);
        fname.setText(CustomerDetailsVariableData.customerDetails.fName + " " + CustomerDetailsVariableData.customerDetails.fName);
        TextView cid = (TextView) view.findViewById(R.id.cusIDtxt);
        cid.setText(CustomerDetailsVariableData.customerDetails.customerId);
        TextView tel1 = (TextView) view.findViewById(R.id.ctel);
        tel1.setText(CustomerDetailsVariableData.customerDetails.phone1);
        TextView tel2 = (TextView) view.findViewById(R.id.ctel2);
        tel2.setText(CustomerDetailsVariableData.customerDetails.phone2);
        TextView eml = (TextView) view.findViewById(R.id.emailtxt);
        eml.setText(CustomerDetailsVariableData.customerDetails.email);
        TextView add = (TextView) view.findViewById(R.id.addtxt);
        add.setText(CustomerDetailsVariableData.customerDetails.address);

        TextView loc = (TextView) view.findViewById(R.id.loctxt);
        if (CustomerDetailsVariableData.customerDetails.lati == null) {
            loc.setText(0 + ", " + 0);
        } else {
            loc.setText(CustomerDetailsVariableData.customerDetails.lati + ", " + CustomerDetailsVariableData.customerDetails.longi);
        }

        ImageView viewbtn = (ImageView) view.findViewById(R.id.imageView_close);
        viewbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                onDestroyView();
            }
        });

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
}
