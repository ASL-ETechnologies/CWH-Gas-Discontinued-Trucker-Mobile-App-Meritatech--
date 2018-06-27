package com.meritatech.myrewardzpos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;


import com.meritatech.myrewardzpos.controller.AdapterLogs;
import com.meritatech.myrewardzpos.controller.EndlessScrollListener;
import com.meritatech.myrewardzpos.data.LogRecord;
import com.meritatech.myrewardzpos.data.MyPosBase;
import com.meritatech.myrewardzpos.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;


public class AppLogFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnFragmentInteractionListener mListener;
    public AdapterLogs adbLogs;
    private ListView mListView;
    public AppLogFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static AppLogFragment newInstance(int columnCount) {
        AppLogFragment fragment = new AppLogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_applog_list, container, false);
        MyPosBase myPosBase = new MyPosBase();
        ArrayList<LogRecord> listOfLogs = myPosBase.GetLogs("0");


        adbLogs = new AdapterLogs(getActivity(), R.layout.logs_partiallayout, listOfLogs);
        mListView = (ListView) view.findViewById(R.id.list_view_logs);
        mListView.addFooterView(new ProgressBar(getContext()));
        mListView.setAdapter(adbLogs);
        mListView.setTextFilterEnabled(true);
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


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SettingsFragment.OnFragmentInteractionListener) {
            mListener = (AppLogFragment.OnFragmentInteractionListener) context;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    final boolean[] resp = {false};
    int pageItems = 20;

    public boolean[] loadItems(int pagestart) {
        int skip = (pagestart -1) * 20;
            MyPosBase myPosBase = new MyPosBase();

            List<LogRecord> listOfCustomer = myPosBase.GetLogs(String.valueOf(skip));
            for (int f = 0; f < listOfCustomer.size(); f++) {
                adbLogs.ILogRecord.add(listOfCustomer.get(f));
            }
            if (adbLogs != null) {
                adbLogs.notifyDataSetChanged();
            }
            return resp;

    }
}
