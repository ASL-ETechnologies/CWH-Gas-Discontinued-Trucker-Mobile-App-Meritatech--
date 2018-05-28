package com.meritatech.myrewardzpos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.meritatech.myrewardzpos.controller.GlobalVariables;
import com.meritatech.myrewardzpos.data.MyPosBase;
import com.meritatech.myrewardzpos.data.SalesInvoiceRecord;
import com.meritatech.myrewardzpos.database.SchemaGenerator;
import com.meritatech.myrewardzpos.database.SugarContext;
import com.meritatech.myrewardzpos.database.SugarDb;
import com.meritatech.myrewardzpos.global.CustomerVariableData;
import com.meritatech.myrewardzpos.global.SalesOrderVariableData;
import com.meritatech.myrewardzpos.global.VariableData;

import java.util.ArrayList;

import static com.meritatech.myrewardzpos.MainActivity.appContext;

public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        getActivity().setTitle("Settings");
        final BackgroundActivity backgroundActivity = new BackgroundActivity();
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        ImageView buttonReset = (ImageView) view.findViewById(R.id.buttonReset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MyPosBase myPosBase = new MyPosBase();
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                adb.setMessage("Do you want to Reset the App?");
                adb.setTitle("Confirmation");
                adb.setIcon(android.R.drawable.ic_dialog_info);

                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        AlertDialog.Builder adb1 = new AlertDialog.Builder(getContext());
                        adb1.setMessage("Resetting the application will result to LOSS of data. Do you wish to proceed?");
                        adb1.setTitle("Confirmation");
                        adb1.setIcon(android.R.drawable.ic_dialog_alert);

                        adb1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                ArrayList<SalesInvoiceRecord> allsalestransactions = myPosBase.GetUnSentTransactions("0");

                                if (allsalestransactions.size() == 0) {

                                    appContext.stopService(new Intent(appContext,
                                            UploadBackGroundService.class));
                                    SugarContext.terminate();
                                    SchemaGenerator schemaGenerator = new SchemaGenerator(getContext());

                                    schemaGenerator.deleteTables(new SugarDb(getContext()).getDB());
                                    SugarContext.init(getContext());
                                    schemaGenerator.createDatabase(new SugarDb(getContext()).getDB());

                                    new AlertDialog.Builder(getContext())
                                            .setMessage("Reset Complete!")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Toast toast = Toast.makeText(getContext(), "Resetting the app in the background..", Toast.LENGTH_SHORT);
                                                    toast.show();
                                                    getActivity().finish();
                                                    System.exit(0);

                                                }
                                            })

                                            .show();
                                    return;

                                } else {
                                    new AlertDialog.Builder(getContext())
                                            .setMessage("Reset Failed! Unsent Sales Transaction exist in local storage. Sync Invoices to proceed.")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    return;
                                                }
                                            })

                                            .show();
                                }
                            }
                        });

                        adb1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                return;
                            }
                        });
                        adb1.show();

                    }
                });

                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        return;
                    }
                });
                adb.show();

            }
        });


        ImageView buttonSyncInvoices = (ImageView) view.findViewById(R.id.buttonSyncInvoices);
        buttonSyncInvoices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalVariables.isConnected(getContext())) {

                    AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                    adb.setMessage("Do you want to sync Invoices?");
                    adb.setTitle("Confirmation");
                    adb.setIcon(android.R.drawable.ic_dialog_info);

                    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            backgroundActivity.pollSalesTransactions();
                            int duration = Toast.LENGTH_LONG;
                            String recordinfo = "Syncing Invoices in the background";
                            Toast toast = Toast.makeText(getContext(), recordinfo, duration);
                            toast.show();
                        }
                    });

                    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            return;
                        }
                    });
                    adb.show();

                }

            }
        });

        ImageView buttonSyncSalesOrder = (ImageView) view.findViewById(R.id.buttonSyncSalesOrder);
        buttonSyncSalesOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalVariables.isConnected(getContext())) {

                    AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                    adb.setMessage("Do you want to sync Sales Orders?");
                    adb.setTitle("Confirmation");
                    adb.setIcon(android.R.drawable.ic_dialog_info);

                    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            backgroundActivity.syncSalesOrder();
                            int duration = Toast.LENGTH_LONG;
                            String recordinfo = "Syncing Sales Orders in the background";
                            Toast toast = Toast.makeText(getContext(), recordinfo, duration);
                            toast.show();
                        }
                    });

                    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            return;
                        }
                    });
                    adb.show();
                }


            }
        });

        ImageView buttonsyncContacts = (ImageView) view.findViewById(R.id.buttonsyncContacts);
        buttonsyncContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalVariables.isConnected(getContext())) {

                    AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                    adb.setMessage("Do you want to sync Contacts afresh?");
                    adb.setTitle("Confirmation");
                    adb.setIcon(android.R.drawable.ic_dialog_info);

                    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            int duration = Toast.LENGTH_LONG;
                            String recordinfo = "Syncing Customer Contacts in the background";
                            Toast toast = Toast.makeText(getContext(), recordinfo, duration);
                            toast.show();
                            Fragment fragment = null;
                            fragment = new HomeFragment();
                            replaceFragment(fragment);
                            backgroundActivity.syncContacts();
                        }
                    });

                    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            return;
                        }
                    });
                    adb.show();
                }
                else {
                    if(!GlobalVariables.isConnected(getContext()))
                    {
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(getContext(), "Turn On Data Connection!", duration);
                        toast.show();
                    }
                }

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
