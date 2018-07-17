package com.meritatech.myrewardzpos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.meritatech.myrewardzpos.controller.AdapterInventory;
import com.meritatech.myrewardzpos.controller.GlobalVariables;
import com.meritatech.myrewardzpos.controller.Inventory;
import com.meritatech.myrewardzpos.data.CustomerRecord;
import com.meritatech.myrewardzpos.data.InventotyPost;
import com.meritatech.myrewardzpos.data.InvoiceModel;
import com.meritatech.myrewardzpos.data.MyPosBase;
import com.meritatech.myrewardzpos.data.SalesInvoiceInventoryRecord;
import com.meritatech.myrewardzpos.data.SalesInvoiceRecord;
import com.meritatech.myrewardzpos.data.SalesOrderPost;
import com.meritatech.myrewardzpos.data.SalesOrderRecord;
import com.meritatech.myrewardzpos.data.SalesOrderRecordListAPI;
import com.meritatech.myrewardzpos.data.UserRecord;
import com.meritatech.myrewardzpos.dataObj.InvoiceDataObj;
import com.meritatech.myrewardzpos.enums.CustomerType;
import com.meritatech.myrewardzpos.enums.SalesOrderStatus;
import com.meritatech.myrewardzpos.global.CustomerActivityChecker;
import com.meritatech.myrewardzpos.global.CustomerVariableData;
import com.meritatech.myrewardzpos.global.InventoryVariableData;
import com.meritatech.myrewardzpos.global.LocationData;
import com.meritatech.myrewardzpos.global.SalesOrderVariableData;
import com.meritatech.myrewardzpos.global.VariableData;
import com.meritatech.myrewardzpos.global.VariableDataCopy;
import com.meritatech.myrewardzpos.model.CustomerDataModel;
import com.meritatech.myrewardzpos.model.CustomerUsabilityDataModel;
import com.meritatech.myrewardzpos.model.InvoiceDataModel;
import com.meritatech.myrewardzpos.interfaces.PosServicesInterface;
import com.meritatech.myrewardzpos.utility.FormValidation;
import com.meritatech.myrewardzpos.utility.Utilities;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.meritatech.myrewardzpos.controller.GlobalVariables.getCurrentLocale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InvoiceCreateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InvoiceCreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InvoiceCreateFragment extends Fragment
        implements View.OnClickListener, InventotyDialogFragment.EditNameDialogListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public ArrayList<Inventory> SelectedInventory;
    AdapterInventory adbInventory;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TableLayout tableLayout;
    GlobalVariables globalVars = new GlobalVariables();
    MyPosBase myPosBase = new MyPosBase();
    Double totalcost = 0.0;
    Double totaltax = 0.0;
    int NewPoints = 0;
    int userTotalPoints = 0;
    boolean isSalesOrder = false;
    boolean isFormEditable = false;
    boolean IsCustomerDetailsChange = false;
    View mainview;

    TextView totalsPoints;
    TextView newPoints;
    TextView beforePoints;

    TextView totals;
    TextView customerIdText;
    TextView subtotals;
    TextView gcttotals;
    Button saveInvBtn;

    TextView cId;
    ImageView findcId;
    ImageView editcId;
    TextView firstName;
    TextView lastName;
    TextView phone1;
    TextView phone2;
    TextView email;

    View view = null;
    private OnFragmentInteractionListener mListener;

    public InvoiceCreateFragment() {
        // Required empty public constructor
    }


    public static InvoiceCreateFragment newInstance(String param1, String param2) {
        InvoiceCreateFragment fragment = new InvoiceCreateFragment();
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
        getActivity().setTitle("Invoice");
        CustomerActivityChecker.IsInvoicePage = true;
        view = inflater.inflate(R.layout.fragment_invoice_create, container, false);


        mainview = view;
        tableLayout = (TableLayout) view.findViewById(R.id.selectedInventoryLayout);

        totalsPoints = (TextView) view.findViewById(R.id.subtotalpointsaftertxt);
        beforePoints = (TextView) view.findViewById(R.id.subtotalpointsbeforetxt);
        newPoints = (TextView) view.findViewById(R.id.subtotalpointsNewtxt);

        totals = (TextView) view.findViewById(R.id.totalstxt);
        subtotals = (TextView) view.findViewById(R.id.subtotalstxt);
        gcttotals = (TextView) view.findViewById(R.id.gcttxt);

        customerIdText = (TextView) view.findViewById(R.id.input_customer_id);

        cId = (TextView) view.findViewById(R.id.input_customer_id);
        findcId = (ImageView) view.findViewById(R.id.findcontact);
        editcId = (ImageView) view.findViewById(R.id.editcontact);
        firstName = (TextView) view.findViewById(R.id.input_fname);
        lastName = (TextView) view.findViewById(R.id.input_lname);
        phone1 = (TextView) view.findViewById(R.id.input_phone1);
        phone2 = (TextView) view.findViewById(R.id.input_phone2);
        email = (TextView) view.findViewById(R.id.input_email);


        editcId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cId.setText("");

                firstName.setText("");
                lastName.setText("");
                phone1.setText("");
                phone2.setText("");
                email.setText("");

                firstName.setEnabled(false);
                lastName.setEnabled(false);
                phone1.setEnabled(false);
                phone2.setEnabled(false);
                email.setEnabled(false);
                cId.setEnabled(true);
                firstName.setError(null);
                lastName.setError(null);
                phone1.setError(null);
                phone2.setError(null);
                email.setError(null);
                cId.setError(null);
                cId.setEnabled(true);
                cId.requestFocus();
                findcId.setVisibility(View.VISIBLE);
                editcId.setVisibility(View.INVISIBLE);
                CustomerVariableData.customerVariable = null;

            }
        });


        findcId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cidValue = cId.getText().toString();
                int ttlExp = GlobalVariables.CsRecCounter;
                if (!cidValue.equals("")) {
                    boolean isValid = Validate(cidValue);

                    cId.setEnabled(false);
                    findcId.setVisibility(View.INVISIBLE);
                    editcId.setVisibility(View.VISIBLE);

                }

            }
        });


        ImageView clearInputs = (ImageView) view.findViewById(R.id.buttonclearContact);
        clearInputs.setOnClickListener(this);

        ImageView addCustomerBtn = (ImageView) view.findViewById(R.id.buttonAddCustomer);
        addCustomerBtn.setOnClickListener(this);
        ImageView invBtn = (ImageView) view.findViewById(R.id.buttonAddInventory);
        invBtn.setEnabled(true);
        invBtn.setOnClickListener(this);
        ImageView editFormBtn = (ImageView) view.findViewById(R.id.buttonEditCustomer);
        editFormBtn.setOnClickListener(this);
        ImageView btnSv = (ImageView) mainview.findViewById(R.id.buttonsaveCustomer);
        btnSv.setOnClickListener(this);
        saveInvBtn = (Button) view.findViewById(R.id.buttonSaveInvoice);
        saveInvBtn.setOnClickListener(this);
        Button cancelInvBtn = (Button) view.findViewById(R.id.buttonCancelInvoice);
        cancelInvBtn.setOnClickListener(this);
        Switch onOffSwitch = (Switch) view.findViewById(R.id.on_off_switch);
        if (GlobalVariables.enableGPS != null) {
            if (GlobalVariables.enableGPS.equals("1")) {
                onOffSwitch.setEnabled(true);
            } else if (GlobalVariables.enableGPS.equals("0")) {
                onOffSwitch.setEnabled(false);
            }
        }
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", "" + isChecked);
                if (isChecked) {
                    GlobalVariables.IsCustomerLocation = true;
                } else {
                    GlobalVariables.IsCustomerLocation = false;
                }
            }

        });
        if (CustomerVariableData.customerVariable != null) {
            firstName.setText(CustomerVariableData.customerVariable.fName);

            lastName.setText(CustomerVariableData.customerVariable.lName);

            phone1.setText(CustomerVariableData.customerVariable.phone1);

            phone2.setText(CustomerVariableData.customerVariable.phone2);

            email.setText(CustomerVariableData.customerVariable.email);

            TextView customerId = (TextView) view.findViewById(R.id.input_customer_id);
            customerId.setText(CustomerVariableData.customerVariable.customerId);

            isSalesOrder = true;
            if (GlobalVariables.IsSalesOrder) {
                invBtn.setEnabled(true);
                addCustomerBtn.setEnabled(false);
                clearInputs.setEnabled(false);
                editFormBtn.setClickable(false);
                TextView customer = (TextView) view.findViewById(R.id.input_customer_id);
                inputStateChanger(view, true);
                TextView totalPointsLbl = (TextView) view.findViewById(R.id.subtotalpointsBeforelbl);
                totalPointsLbl.setText("Points Before: ");
                TextView totalCurrentPointsLbl = (TextView) view.findViewById(R.id.subtotalpointsNewLbl);
                totalCurrentPointsLbl.setText("New Points: ");
                TextView totalPointsAfterLbl = (TextView) view.findViewById(R.id.subtotalpointsAfterLbl);
                totalPointsAfterLbl.setText("Total Points: ");
                beforePoints.setText(CustomerVariableData.customerVariable.points);
                newPoints.setText(String.valueOf(CustomerVariableData.NewPoints));
                if (CustomerVariableData.customerVariable.points == null) {
                    CustomerVariableData.customerVariable.points = "0";
                }

                int ttpnts1 = Integer.valueOf(CustomerVariableData.customerVariable.points) + CustomerVariableData.NewPoints;
                totalsPoints.setText(String.valueOf(ttpnts1));
                populateSalesOrderData(tableLayout, VariableData.inventoryVariable);

            } else {
                TextView customer = (TextView) view.findViewById(R.id.input_customer_id);
                inputStateChanger(view, true);
                addCustomerBtn.setEnabled(false);
                clearInputs.setEnabled(false);
                CustomerVariableData.customerVariable = null;
                VariableData.inventoryVariable = null;
                SalesOrderVariableData.salesOrderVariable = null;
            }

        } else {
            addCustomerBtn.setEnabled(true);
            clearInputs.setEnabled(true);
            CustomerVariableData.customerVariable = null;
            VariableData.inventoryVariable = null;
            SalesOrderVariableData.salesOrderVariable = null;
        }
        final TextView cid = (TextView) view.findViewById(R.id.input_customer_id);
        final TextView fname = (TextView) view.findViewById(R.id.input_fname);
        final TextView lname = (TextView) view.findViewById(R.id.input_lname);


        cid.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                FormValidation.hasText(cid);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        fname.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                FormValidation.hasText(fname);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        lname.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                FormValidation.hasText(lname);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                FormValidation.isEmailAddress(email, false);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        phone1.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                FormValidation.isPhoneNumber(phone1, true);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        phone2.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                FormValidation.isPhoneNumber(phone2, false);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        //  populateInvoiceTable(tableLayout, VariableData.inventoryVariable);
        TextView totals = (TextView) view.findViewById(R.id.totalstxt);
        totals.setText(Double.toString(totalcost));
        return view;
    }

    private void inputStateChanger(View view, boolean disable) {
        TextView customer = (TextView) view.findViewById(R.id.input_customer_id);
        TextView firstName = (TextView) view.findViewById(R.id.input_fname);
        TextView lastName = (TextView) view.findViewById(R.id.input_lname);
        TextView phone1 = (TextView) view.findViewById(R.id.input_phone1);
        TextView phone2 = (TextView) view.findViewById(R.id.input_phone2);
        TextView email = (TextView) view.findViewById(R.id.input_email);
        if (disable) {
            //  customer.setEnabled(false);
            firstName.setEnabled(false);
            lastName.setEnabled(false);
            phone1.setEnabled(false);
            phone2.setEnabled(false);
            email.setEnabled(false);
        } else {
            customer.setEnabled(true);
            firstName.setEnabled(true);
            lastName.setEnabled(true);
            phone1.setEnabled(true);
            phone2.setEnabled(true);
            email.setEnabled(true);
        }
    }

    private void populateSalesOrderData(TableLayout tl, ArrayList<Inventory> inventoryVariable) {
        ArrayList<Inventory> changedItems = new ArrayList<Inventory>();
        if (inventoryVariable != null) {
            for (int l = 0; l < inventoryVariable.size(); l++) {
                if (Integer.parseInt(inventoryVariable.get(l).qty) >= 1 & inventoryVariable.get(l).priceSold != null) {
                    if (!GlobalVariables.IsSalesOrder) {
                        inventoryVariable.get(l).points = String.valueOf(globalVars.pointsPerDollar * Integer.parseInt(inventoryVariable.get(l).qty) * Double.parseDouble(inventoryVariable.get(l).priceSold));
                    }
                    changedItems.add(inventoryVariable.get(l));
                }
            }
            SalesOrderVariableData.salesOrderVariable = changedItems;
            populateInvoiceTable(tl, changedItems);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
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

    public boolean Validate(final String cusId) {
        final boolean[] returnValue = {false};
        final ProgressDialog progress1 = new ProgressDialog(getContext());
        progress1.setTitle("Validate");
        progress1.setMessage("Validating CustomerId..");
        progress1.setCancelable(false);
        progress1.show();
        String API_BASE_URL = globalVars.url;
        final ImageView btnEdt = (ImageView) mainview.findViewById(R.id.buttonEditCustomer);
        final ImageView btnSv = (ImageView) mainview.findViewById(R.id.buttonsaveCustomer);
        final ImageView btnInv = (ImageView) mainview.findViewById(R.id.buttonAddInventory);

        final TextView customer = (TextView) mainview.findViewById(R.id.input_customer_id);
        final TextView firstName = (TextView) getView().findViewById(R.id.input_fname);
        final TextView lastName = (TextView) getView().findViewById(R.id.input_lname);
        final TextView phone1 = (TextView) getView().findViewById(R.id.input_phone1);
        final TextView phone2 = (TextView) getView().findViewById(R.id.input_phone2);
        final TextView email = (TextView) getView().findViewById(R.id.input_email);

        if (globalVars.isConnected(getContext())) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            Retrofit.Builder builder =
                    new Retrofit.Builder()
                            .baseUrl(API_BASE_URL)
                            .addConverterFactory(
                                    GsonConverterFactory.create()
                            );
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Retrofit retrofit =
                    builder
                            .client(
                                    httpClient.build()
                            )
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();
            PosServicesInterface client = retrofit.create(PosServicesInterface.class);
            Call<CustomerUsabilityDataModel> call = client.checkCustomerUsability(globalVars.token, globalVars.realmId, cusId);
            call.enqueue(new Callback<CustomerUsabilityDataModel>() {
                @Override
                public void onResponse(Call<CustomerUsabilityDataModel> call, Response<CustomerUsabilityDataModel> response) {
                    try {
                        if (response.body().StatusCode.equals("10")) {
                            customer.setFocusable(false);
                            customer.setFocusableInTouchMode(true);
                            customer.setFocusable(true);
                            firstName.setText("");
                            lastName.setText("");
                            phone1.setText("");
                            phone2.setText("");
                            email.setText("");
                            firstName.setError(null);
                            lastName.setError(null);
                            phone1.setError(null);
                            phone2.setError(null);
                            email.setError(null);
                            firstName.setEnabled(false);
                            lastName.setEnabled(false);
                            phone1.setEnabled(false);
                            phone2.setEnabled(false);
                            email.setEnabled(false);
                            btnSv.setVisibility(View.INVISIBLE);
                            int duration = Toast.LENGTH_LONG;
                            Toast toast = Toast.makeText(getContext(), "Unsable CustomerID!", duration);
                            toast.show();
                            returnValue[0] = false;
                        } else if (response.body().StatusCode.equals("11")) {
                            customer.setFocusable(false);

                            firstName.setText("");
                            lastName.setText("");
                            phone1.setText("");
                            phone2.setText("");
                            email.setText("");
                            // customer.setEnabled(true);
                            firstName.setEnabled(true);
                            lastName.setEnabled(true);
                            phone1.setEnabled(true);
                            phone2.setEnabled(true);
                            email.setEnabled(true);
                            btnSv.setVisibility(View.VISIBLE);
                            btnEdt.setVisibility(View.INVISIBLE);
                            int duration = Toast.LENGTH_LONG;
                            Toast toast = Toast.makeText(getContext(), "Success!", duration);
                            toast.show();
                            customer.setFocusable(true);
                            customer.setFocusableInTouchMode(true);
                            GlobalVariables.customerType = CustomerType.NEW;
                            returnValue[0] = true;
                        } else if (response.body().StatusCode.equals("12")) {
                            try {
                                getCustomerPoints(cusId);
                                Thread.sleep(3000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            customer.setFocusable(false);
                            customer.setFocusableInTouchMode(true);
                            customer.setFocusable(true);
                            btnEdt.setVisibility(View.VISIBLE);
                            btnSv.setVisibility(View.INVISIBLE);
                            firstName.setEnabled(false);
                            lastName.setEnabled(false);
                            phone1.setEnabled(false);
                            phone2.setEnabled(false);
                            email.setEnabled(false);
                            firstName.setText(response.body().dataObj.Data.get(0).fName);
                            lastName.setText(response.body().dataObj.Data.get(0).lName);
                            phone1.setText(response.body().dataObj.Data.get(0).phone1);
                            phone2.setText(response.body().dataObj.Data.get(0).phone2);
                            email.setText(response.body().dataObj.Data.get(0).email);
                            CustomerVariableData.customerVariable = new CustomerRecord();
                            CustomerVariableData.customerVariable.customerId = customer.getText().toString();
                            CustomerVariableData.customerVariable.fName = firstName.getText().toString();
                            CustomerVariableData.customerVariable.lName = lastName.getText().toString();
                            CustomerVariableData.customerVariable.phone1 = phone1.getText().toString();
                            CustomerVariableData.customerVariable.phone2 = phone2.getText().toString();
                            CustomerVariableData.customerVariable.email = email.getText().toString();
                            btnInv.setEnabled(true);
                            GlobalVariables.customerType = CustomerType.EXISTING;
                            returnValue[0] = true;
                        }
                        progress1.dismiss();
                    } catch (Exception ex) {
                        Utilities.LogException(ex);
                        progress1.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<CustomerUsabilityDataModel> call, Throwable t) {
                    try {
                        progress1.dismiss();
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(getContext(), "Error checking CustomerID!", duration);
                        toast.show();
                        returnValue[0] = false;
                    } catch (Exception ex) {
                        Utilities.LogException(ex);
                        progress1.dismiss();
                    }

                }
            });
            return returnValue[0];
        } else {

            long totalcustomersinLocaldb = CustomerRecord.count(CustomerRecord.class);
            List<CustomerRecord> listOfCustomer = myPosBase.GetCustomers("20");
            CustomerRecord selcustomer = myPosBase.GetCustomerRecord(cusId);
            if (selcustomer != null) {
                try {
                    getCustomerPoints(cusId);
                    Thread.sleep(3000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                customer.setFocusable(false);
                customer.setFocusableInTouchMode(true);
                customer.setFocusable(true);
                btnEdt.setVisibility(View.VISIBLE);
                btnSv.setVisibility(View.INVISIBLE);
                firstName.setEnabled(false);
                lastName.setEnabled(false);
                phone1.setEnabled(false);
                phone2.setEnabled(false);
                email.setEnabled(false);
                firstName.setText(selcustomer.fName);
                lastName.setText(selcustomer.lName);
                phone1.setText(selcustomer.phone1);
                phone2.setText(selcustomer.phone2);
                email.setText(selcustomer.email);
                CustomerVariableData.customerVariable = new CustomerRecord();
                CustomerVariableData.customerVariable.customerId = customer.getText().toString();
                CustomerVariableData.customerVariable.fName = firstName.getText().toString();
                CustomerVariableData.customerVariable.lName = lastName.getText().toString();
                CustomerVariableData.customerVariable.phone1 = phone1.getText().toString();
                CustomerVariableData.customerVariable.phone2 = phone2.getText().toString();
                CustomerVariableData.customerVariable.email = email.getText().toString();
                btnInv.setEnabled(true);
                GlobalVariables.customerType = CustomerType.EXISTING;
                returnValue[0] = true;
                progress1.dismiss();
            } else {
                customer.setFocusable(false);
                customer.setFocusableInTouchMode(true);
                customer.setFocusable(true);
                firstName.setText("");
                lastName.setText("");
                phone1.setText("");
                phone2.setText("");
                email.setText("");
                firstName.setError(null);
                lastName.setError(null);
                phone1.setError(null);
                phone2.setError(null);
                email.setError(null);
                firstName.setEnabled(false);
                lastName.setEnabled(false);
                phone1.setEnabled(false);
                phone2.setEnabled(false);
                email.setEnabled(false);
                btnSv.setVisibility(View.INVISIBLE);
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(getContext(), "Unsable CustomerID!", duration);
                toast.show();
                returnValue[0] = false;
                progress1.dismiss();
            }
            return returnValue[0];
        }
    }


    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        try {


            String API_BASE_URL = globalVars.url;

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            Retrofit.Builder builder =
                    new Retrofit.Builder()
                            .baseUrl(API_BASE_URL)
                            .addConverterFactory(
                                    GsonConverterFactory.create()
                            );
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Retrofit retrofit =
                    builder
                            .client(
                                    httpClient.build()
                            )
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();

            PosServicesInterface client = retrofit.create(PosServicesInterface.class);

            FragmentManager fm = getActivity().getFragmentManager();
            TextView customer = (TextView) mainview.findViewById(R.id.input_customer_id);
            TextView firstName = (TextView) getView().findViewById(R.id.input_fname);
            TextView lastName = (TextView) getView().findViewById(R.id.input_lname);
            TextView phone1 = (TextView) getView().findViewById(R.id.input_phone1);
            TextView phone2 = (TextView) getView().findViewById(R.id.input_phone2);
            TextView email = (TextView) getView().findViewById(R.id.input_email);
            final ImageView btnEdt = (ImageView) mainview.findViewById(R.id.buttonEditCustomer);
            final ImageView btnSv = (ImageView) mainview.findViewById(R.id.buttonsaveCustomer);
            final ImageView btnInv = (ImageView) mainview.findViewById(R.id.buttonAddInventory);
            final Button btnSvInv = (Button) mainview.findViewById(R.id.buttonSaveInvoice);
            switch (v.getId()) {

                case R.id.buttonAddCustomer:
                    GlobalVariables.customerType = CustomerType.EXISTING;
                    customer.setEnabled(false);
                    firstName.setEnabled(false);
                    lastName.setEnabled(false);
                    phone1.setEnabled(false);
                    phone2.setEnabled(false);
                    email.setEnabled(false);
                    CustomerDialogFragment customerDialogFragment = new CustomerDialogFragment();
                    customerDialogFragment.setTargetFragment(InvoiceCreateFragment.this, 1);
                    customerDialogFragment.show(getFragmentManager(), "customer");
                    btnEdt.setVisibility(View.VISIBLE);
                    btnSv.setVisibility(View.INVISIBLE);
                    IsCustomerDetailsChange = false;

                    break;

                case R.id.buttonclearContact:
                    GlobalVariables.customerType = CustomerType.NEW;
                    customer.setText("");
                    firstName.setText("");
                    lastName.setText("");
                    phone1.setText("");
                    phone2.setText("");
                    email.setText("");
                    customer.setEnabled(true);
                    firstName.setEnabled(true);
                    lastName.setEnabled(true);
                    phone1.setEnabled(true);
                    phone2.setEnabled(true);
                    email.setEnabled(true);

                    CustomerVariableData.customerVariable = null;
                    IsCustomerDetailsChange = true;
                    btnSv.setVisibility(View.VISIBLE);
                    btnSvInv.setEnabled(false);
                    btnEdt.setVisibility(View.INVISIBLE);
                    btnInv.setEnabled(false);
                    break;
                case R.id.buttonAddInventory:


                    if (VariableData.inventoryVariable != null) {
                        VariableDataCopy.inventoryBeforeSaveVariable = VariableData.inventoryVariable;
                    }
                    InventotyDialogFragment bFragment = new InventotyDialogFragment();
                    bFragment.setTargetFragment(InvoiceCreateFragment.this, 1);
                    bFragment.show(getFragmentManager(), "inventory");
                    break;
                case R.id.buttonEditCustomer:
                    customer = (TextView) mainview.findViewById(R.id.input_customer_id);
                    firstName = (TextView) mainview.findViewById(R.id.input_fname);
                    lastName = (TextView) mainview.findViewById(R.id.input_lname);
                    phone1 = (TextView) mainview.findViewById(R.id.input_phone1);
                    phone2 = (TextView) mainview.findViewById(R.id.input_phone2);
                    email = (TextView) mainview.findViewById(R.id.input_email);

                    if (GlobalVariables.customerType != null) {
                        if (GlobalVariables.customerType == CustomerType.NEW) {
                            customer.setEnabled(true);
                        }
                    }

                    firstName.setEnabled(true);
                    lastName.setEnabled(true);
                    phone1.setEnabled(true);
                    phone2.setEnabled(true);
                    email.setEnabled(true);

                    btnSv.setVisibility(View.VISIBLE);
                    btnEdt.setVisibility(View.INVISIBLE);
                    break;

                case R.id.buttonsaveCustomer:
                    IsCustomerDetailsChange = true;

                    if (String.valueOf(firstName.getText()).length() > 0 && String.valueOf(lastName.getText()).length() > 0 && String.valueOf(phone1.getText()).length() > 0 && String.valueOf(customer.getText()).length() > 0) {
                        if (GlobalVariables.customerType == CustomerType.NEW) {
                            String cusId = String.valueOf(customer.getText());
                            if (cusId.length() > 0) {

                                btnEdt.setVisibility(View.VISIBLE);
                                btnSv.setVisibility(View.INVISIBLE);
                                customer.setEnabled(false);
                                firstName.setEnabled(false);
                                lastName.setEnabled(false);
                                phone1.setEnabled(false);
                                phone2.setEnabled(false);
                                email.setEnabled(false);
                                CustomerVariableData.customerVariable = new CustomerRecord();
                                CustomerVariableData.customerVariable.customerId = customer.getText().toString();
                                CustomerVariableData.customerVariable.fName = firstName.getText().toString();
                                CustomerVariableData.customerVariable.lName = lastName.getText().toString();
                                CustomerVariableData.customerVariable.phone1 = phone1.getText().toString();
                                CustomerVariableData.customerVariable.phone2 = phone2.getText().toString();
                                CustomerVariableData.customerVariable.email = email.getText().toString();
                                btnInv.setEnabled(true);
                            }
                        } else {
                            btnEdt.setVisibility(View.VISIBLE);
                            btnSv.setVisibility(View.INVISIBLE);
                            //   customer.setEnabled(false);
                            firstName.setEnabled(false);
                            lastName.setEnabled(false);
                            phone1.setEnabled(false);
                            phone2.setEnabled(false);
                            email.setEnabled(false);
                            CustomerVariableData.customerVariable = new CustomerRecord();
                            CustomerVariableData.customerVariable.customerId = customer.getText().toString();
                            CustomerVariableData.customerVariable.fName = firstName.getText().toString();
                            CustomerVariableData.customerVariable.lName = lastName.getText().toString();
                            CustomerVariableData.customerVariable.phone1 = phone1.getText().toString();
                            CustomerVariableData.customerVariable.phone2 = phone2.getText().toString();
                            CustomerVariableData.customerVariable.email = email.getText().toString();
                            btnInv.setEnabled(true);
                        }


                    } else {
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(getContext(), "Fill all required inputs!", duration);
                        toast.show();
                        btnInv.setEnabled(false);
                    }
                    break;
                case R.id.buttonCancelInvoice:
                    AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                    adb.setMessage("Do you want to cancel this Invoice?");
                    adb.setTitle("Confirmation");
                    adb.setIcon(android.R.drawable.ic_dialog_info);

                    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            VariableData.inventoryVariable = null;
                            CustomerVariableData.customerVariable = null;
                            SalesOrderVariableData.salesOrderVariable = null;
                            Fragment fragment = null;
                            fragment = new HomeFragment();
                            replaceFragment(fragment);
                        }
                    });


                    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            return;
                        }
                    });
                    adb.show();


                    break;
                case R.id.buttonSaveInvoice:

                    final ProgressDialog progress = new ProgressDialog(getContext());
                    progress.setTitle("Saving");
                    progress.setMessage("Saving Invoice");
                    progress.setCancelable(false);
                    progress.show();
                    btnSvInv.setEnabled(false);
                    final TextView pnNo = (TextView) getView().findViewById(R.id.input_phone1);
                    String phoneNo = pnNo.getText().toString();

                    final TextView customerid = (TextView) getView().findViewById(R.id.input_customer_id);
                    String cid = customerid.getText().toString();

                    final TextView fname = (TextView) getView().findViewById(R.id.input_fname);
                    String fn = fname.getText().toString();


                    if (userTotalPoints < 0) {

                        progress.dismiss();
                        AlertDialog.Builder builder1;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder1 = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder1 = new AlertDialog.Builder(getContext());
                        }
                        builder1.setTitle("Alert")
                                .setMessage("Customer does NOT qualify for a Gift!")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        return;
                                    }
                                })

                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        btnSvInv.setEnabled(true);
                        return;

                    }

                    if (!phoneNo.isEmpty() && !fn.isEmpty() && !cid.isEmpty()) {
                        try {
                            saveInvoice();
                            progress.dismiss();
                        } catch (Exception ex) {
                            Utilities.LogException(ex);
                        }

                    } else if (phoneNo.isEmpty() && fn.isEmpty() && cid.equals("999999999")) {
                        AlertDialog.Builder adb1 = new AlertDialog.Builder(getContext());
                        adb1.setMessage("Do you want to make a cash sale without Customer Details?");
                        adb1.setTitle("Confirmation");
                        adb1.setIcon(android.R.drawable.ic_dialog_info);

                        adb1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                saveInvoice();
                                progress.dismiss();
                            }
                        });


                        adb1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                progress.dismiss();
                                btnSvInv.setEnabled(true);
                                return;
                            }
                        });
                        adb1.show();


                    } else {
                        progress.dismiss();
                        btnSvInv.setEnabled(true);
                        AlertDialog.Builder builder1;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder1 = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder1 = new AlertDialog.Builder(getContext());
                        }
                        builder1.setTitle("Alert")
                                .setMessage("Phone number is a required field!")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        pnNo.requestFocus();
                                    }
                                })

                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                    break;
                default:
                    break;
            }

        } catch (Exception ex) {
            Utilities.LogException(ex);
        }
    }

    public void saveInvoice() {

        String API_BASE_URL = globalVars.url;

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

        PosServicesInterface client = retrofit.create(PosServicesInterface.class);
        Location currentLocation = LocationData.location;

        final ProgressDialog progress = new ProgressDialog(getContext());
        progress.setTitle("Saving");
        progress.setMessage("Saving Invoice");
        progress.setCancelable(false);
        progress.show();

        InvoiceModel data = new InvoiceModel();
        data.recCnt = "1";
        data.SalesOrderRecord = new SalesOrderRecord();

        data.SalesOrderRecord.salesOrderId = "25";
        data.SalesOrderRecord.salesmanId = globalVars.salesmanId;
        data.SalesOrderRecord.invoiceDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date());
        data.SalesOrderRecord.invoiceNumber = "20";
        data.SalesOrderRecord.parentStoreId = globalVars.ParentId;
        data.SalesOrderRecord.storeId = globalVars.storeId;
        data.SalesOrderRecord.SalesDetails = VariableData.inventoryVariable;


        if (currentLocation == null) {
            data.SalesOrderRecord.longitude = "0";
            data.SalesOrderRecord.latitude = "0";
        } else {
            data.SalesOrderRecord.longitude = String.valueOf(currentLocation.getLongitude());
            data.SalesOrderRecord.latitude = String.valueOf(currentLocation.getLatitude());
        }


        if (CustomerVariableData.customerVariable != null) {
            data.SalesOrderRecord.customerId = CustomerVariableData.customerVariable.customerId;
            data.SalesOrderRecord.phone1 = phone1.getText().toString();
            data.SalesOrderRecord.phone2 = phone2.getText().toString();
            data.SalesOrderRecord.email = email.getText().toString();
            data.SalesOrderRecord.fName = firstName.getText().toString();
            data.SalesOrderRecord.lName = lastName.getText().toString();
            data.SalesOrderRecord.address = CustomerVariableData.customerVariable.address;
            //  data.SalesOrderRecord.p
        } else {

            data.SalesOrderRecord.customerId = cId.getText().toString();
            data.SalesOrderRecord.phone1 = phone1.getText().toString();
            data.SalesOrderRecord.phone2 = phone2.getText().toString();
            data.SalesOrderRecord.email = email.getText().toString();
            data.SalesOrderRecord.fName = firstName.getText().toString();
            data.SalesOrderRecord.lName = lastName.getText().toString();
            CustomerRecord customerData = new CustomerRecord();

            customerData.phone1 = phone1.getText().toString();
            customerData.phone2 = phone2.getText().toString();
            customerData.email = email.getText().toString();
            customerData.fName = firstName.getText().toString();
            customerData.lName = lastName.getText().toString();
            CustomerVariableData.customerVariable = customerData;
            CustomerVariableData.customerVariable.points = "0";
        }

        final MyPosBase myPosBase = new MyPosBase();
        final SalesInvoiceRecord slR = new SalesInvoiceRecord();
        slR.customerId = data.SalesOrderRecord.customerId;
        if (!GlobalVariables.IsSalesOrder) {
            slR.salesOrderId = "";

        } else {
            slR.salesOrderId = CustomerVariableData.customerVariable.orderId;
        }
        if (GlobalVariables.customerType != null) {
            if (GlobalVariables.customerType == CustomerType.EXISTING) {
                slR.customerType = "1";
            } else if (GlobalVariables.customerType == CustomerType.NEW) {
                slR.customerType = "2";
            }
        } else {
            slR.customerType = "2";
        }
        slR.invoiceType = "1";
        if (data.SalesOrderRecord.customerId == null) {
            data.SalesOrderRecord.customerId = "999999999";
        }
        slR.fName = data.SalesOrderRecord.fName;
        slR.lName = data.SalesOrderRecord.lName;
        slR.email = data.SalesOrderRecord.email;
        slR.address = data.SalesOrderRecord.address;
        slR.salesmanId = data.SalesOrderRecord.salesmanId;
        slR.invoiceDateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        slR.parentStoreId = data.SalesOrderRecord.parentStoreId;
        slR.storeId = data.SalesOrderRecord.storeId;
        if (data.SalesOrderRecord.phone1 == null) {
            slR.phone1 = "";
            data.SalesOrderRecord.phone1 = "";
        } else {
            slR.phone1 = data.SalesOrderRecord.phone1;
        }
        if (data.SalesOrderRecord.phone2 == null) {
            slR.phone2 = "";
            data.SalesOrderRecord.phone2 = "";
        } else {
            slR.phone2 = data.SalesOrderRecord.phone2;
        }
        if (data.SalesOrderRecord.email == null) {
            slR.email = "";
            data.SalesOrderRecord.email = "";
        } else {
            slR.email = data.SalesOrderRecord.email;

        }
        slR.longitude = data.SalesOrderRecord.longitude;
        slR.latitude = data.SalesOrderRecord.latitude;
        ArrayList<Inventory> selectedInventory = new ArrayList<Inventory>();
        ArrayList<InventotyPost> inventorypostItems = new ArrayList<InventotyPost>();
        Integer counter = 0;
        if (data.SalesOrderRecord.SalesDetails != null) {
            for (int i = 0; i < data.SalesOrderRecord.SalesDetails.size(); i++) {
                if (data.SalesOrderRecord.SalesDetails.get(i).TotalItems > 0.0) {
                    counter++;
                    selectedInventory.add(data.SalesOrderRecord.SalesDetails.get(i));
                }
                InventotyPost newInv = new InventotyPost();
                newInv.itemNum = data.SalesOrderRecord.SalesDetails.get(i).itemNum;
                newInv.points = data.SalesOrderRecord.SalesDetails.get(i).points;
                newInv.priceSold = data.SalesOrderRecord.SalesDetails.get(i).priceSold;
                if (data.SalesOrderRecord.SalesDetails.get(i).taxPercentage == null) {
                    newInv.tax = "0.00";
                } else {


                    if (GlobalVariables.taxIncluded.equals("1")) {

                        double taxAmount = Double.parseDouble(data.SalesOrderRecord.SalesDetails.get(i).taxPercentage) * (Double.parseDouble(data.SalesOrderRecord.SalesDetails.get(i).sellingPrice) / (1 + Double.parseDouble(data.SalesOrderRecord.SalesDetails.get(i).taxPercentage)));
                        DecimalFormat twoDForm = new DecimalFormat("#.##");
                        taxAmount = Double.valueOf(twoDForm.format(taxAmount));
                        newInv.tax = String.valueOf(taxAmount);

                    } else if (GlobalVariables.taxIncluded.equals("0")) {

                        double tx = Double.parseDouble(data.SalesOrderRecord.SalesDetails.get(i).taxPercentage) * Double.parseDouble(data.SalesOrderRecord.SalesDetails.get(i).sellingPrice);
                        DecimalFormat twoDForm = new DecimalFormat("#.##");
                        tx = Double.valueOf(twoDForm.format(tx));
                        newInv.tax = String.valueOf(tx);
                    }
                }
                newInv.quantity = data.SalesOrderRecord.SalesDetails.get(i).qty;
                if (newInv.quantity != null) {
                    if (Double.parseDouble(newInv.quantity) > 0) {
                        inventorypostItems.add(newInv);
                    }
                }
            }

        }
        UserRecord userRecord = UserRecord.findAllRecords(UserRecord.class).get(0);
        slR.SalesDetails = selectedInventory;
        ArrayList<SalesInvoiceInventoryRecord> salesInvoiceInventoryRecords = new ArrayList<SalesInvoiceInventoryRecord>();
        if (userRecord.invCancelAgeDays != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(cal.getTime()); // sets calendar time/date
            cal.add(Calendar.DATE, Integer.parseInt(userRecord.invCancelAgeDays));
            Date dateToDeliver = cal.getTime();
            slR.invoiceCancelAgeDateTime = cal.getTime();
        }
        slR.salesOrderStatus = SalesOrderStatus.ORIGINAL;
        slR.sent = 0;
        ArrayList<SalesInvoiceRecord> sinvlist = SalesInvoiceRecord.findAllRecords(SalesInvoiceRecord.class);
     // slR.recId = slR.getId().intValue();
        ArrayList<SalesInvoiceRecord> cusHis = myPosBase.GetCustomerPointsHistory(slR.customerId);
        int customerpoints = 0;
        if (cusHis != null && cusHis.size() > 0) {

            customerpoints = cusHis.get(0).lastPoints + NewPoints;
        }

        if (customerpoints == 0) {
            slR.lastPoints = Integer.parseInt(CustomerVariableData.customerVariable.points);
        } else {
            slR.lastPoints = customerpoints;
        }

        slR.newPoints = NewPoints;
        boolean postResponse = false;

        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = (long) stat.getBlockSize() * (long) stat.getBlockCount();
        long megAvailable = bytesAvailable / 1048576;

        try {
            if (megAvailable > 20) {
                if (globalVars.directPost.equals("0") || (globalVars.directPost.equals("1") && !GlobalVariables.isConnected(getContext()))) {
                    globalVars.recBeforeCnt = (int) SalesInvoiceRecord.count(SalesInvoiceRecord.class);
                    postResponse = myPosBase.saveInvoice(slR);

                    long ttRecAfter = SalesInvoiceRecord.count(SalesInvoiceRecord.class);
                    if (ttRecAfter == globalVars.recBeforeCnt) {
                        Toast toast = Toast.makeText(getContext(), "Saving Invoice locally failed! ", Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }
                    CustomerVariableData.customerVariable.newpoints = NewPoints;
                    CustomerVariableData.customerVariable.lastpoints = slR.lastPoints;
                    for (int e = 0; e < slR.SalesDetails.size(); e++) {
                        SalesInvoiceInventoryRecord salesInvoiceInventoryRecord = new SalesInvoiceInventoryRecord();
                        salesInvoiceInventoryRecord.itemNumber = slR.SalesDetails.get(e).itemNum;
                        salesInvoiceInventoryRecord.description = slR.SalesDetails.get(e).description;
                        salesInvoiceInventoryRecord.qty = slR.SalesDetails.get(e).qty;
                        salesInvoiceInventoryRecord.points = slR.SalesDetails.get(e).points;
                        salesInvoiceInventoryRecord.priceSold = slR.SalesDetails.get(e).priceSold;
                        salesInvoiceInventoryRecord.sellingPrice = slR.SalesDetails.get(e).sellingPrice;
                        salesInvoiceInventoryRecord.salesOrderID = String.valueOf(slR.getId());
                        salesInvoiceInventoryRecords.add(salesInvoiceInventoryRecord);
                    }

                    boolean postInvResponse = myPosBase.saveInvoiceInventory(salesInvoiceInventoryRecords);
                }
            } else {
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(getContext(), "Memory low. Error Saving Invoice! ", duration);
                toast.show();
            }
        } catch (Exception ex) {
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(getContext(), "Error Saving Invoice! " + ex.getMessage(), duration);
            toast.show();
        }


        slR.invoiceNumber = String.valueOf(slR.getId());

        InvoiceDataObj dataObj = new InvoiceDataObj();
        dataObj.Data = new ArrayList<SalesOrderPost>();
        SalesOrderPost salesOrderPost = new SalesOrderPost();
        if (data.SalesOrderRecord.customerId != null && data.SalesOrderRecord.customerId.equals("0")) {
            data.SalesOrderRecord.customerId = String.valueOf(0);
        }
        salesOrderPost.customerId = data.SalesOrderRecord.customerId;
        salesOrderPost.salesmanId = data.SalesOrderRecord.salesmanId;
        if (!GlobalVariables.IsSalesOrder) {
            salesOrderPost.salesOrderId = "";

        } else {
            salesOrderPost.salesOrderId = CustomerVariableData.customerVariable.orderId;
        }
        salesOrderPost.invoiceType = "1";
        salesOrderPost.invoiceNumber = String.valueOf(slR.getId());
        salesOrderPost.invoiceDateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        salesOrderPost.parentStoreId = data.SalesOrderRecord.parentStoreId;
        salesOrderPost.storeId = data.SalesOrderRecord.storeId;
        if (GlobalVariables.IsCustomerLocation) {
            salesOrderPost.longi = data.SalesOrderRecord.longitude;
            salesOrderPost.lati = data.SalesOrderRecord.latitude;
        } else {
            salesOrderPost.longi = "0";
            salesOrderPost.lati = "0";
        }

        if (GlobalVariables.customerType != null) {
            if (GlobalVariables.customerType == CustomerType.EXISTING) {
                salesOrderPost.customerType = "1";
            } else if (GlobalVariables.customerType == CustomerType.NEW) {
                salesOrderPost.customerType = "2";
            }
        } else {
            salesOrderPost.customerType = "2";
        }
        if (IsCustomerDetailsChange) {

            salesOrderPost.fName = data.SalesOrderRecord.fName;
            salesOrderPost.lName = data.SalesOrderRecord.lName;
            salesOrderPost.phone1 = data.SalesOrderRecord.phone1;
            salesOrderPost.phone2 = data.SalesOrderRecord.phone2;
            salesOrderPost.email = data.SalesOrderRecord.email;
            //update globalV

            CustomerVariableData.customerVariable.fName = data.SalesOrderRecord.fName;
            CustomerVariableData.customerVariable.lName = data.SalesOrderRecord.lName;
            CustomerVariableData.customerVariable.phone1 = data.SalesOrderRecord.phone1;
            CustomerVariableData.customerVariable.phone2 = data.SalesOrderRecord.phone2;
            CustomerVariableData.customerVariable.email = data.SalesOrderRecord.email;
        } else {
            salesOrderPost.fName = "";
            salesOrderPost.lName = "";
            salesOrderPost.phone1 = "";
            salesOrderPost.phone2 = "";
            salesOrderPost.email = "";
        }
        salesOrderPost.slsDetailArr = inventorypostItems;

        dataObj.Data = new ArrayList<SalesOrderPost>();
        dataObj.Data.add(salesOrderPost);

        dataObj.recCnt = dataObj.Data.size();
        if (IsCustomerDetailsChange) {
            dataObj.Data.get(0).email = data.SalesOrderRecord.email;
            dataObj.Data.get(0).fName = data.SalesOrderRecord.fName;
            dataObj.Data.get(0).lName = data.SalesOrderRecord.lName;
            dataObj.Data.get(0).phone1 = data.SalesOrderRecord.phone1;
            dataObj.Data.get(0).phone2 = data.SalesOrderRecord.phone2;
        }

        if (globalVars.directPost.equals("1")) {
            if (dataObj.Data.get(0).slsDetailArr.size() > 0) {
                Gson gson1 = new Gson();
                String jsonObj = gson1.toJson(dataObj);
                Call<InvoiceDataModel> call = client.saveInvoice(globalVars.token, dataObj);
                call.enqueue(new Callback<InvoiceDataModel>() {
                    @Override
                    public void onResponse(Call<InvoiceDataModel> call, Response<InvoiceDataModel> response) {
                        try {
                            progress.dismiss();
                            if (GlobalVariables.IsSalesOrder) {
                                SalesOrderRecordListAPI salesOrderRecordListAPI = new SalesOrderRecordListAPI();
                                salesOrderRecordListAPI.SalesOrderRecordListAPICall();
                            }

                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(getContext(), "Invoice Posted Direct to API Successfully!", duration);
                            toast.show();

                            Fragment fragment = null;
                            fragment = new ReceiptFragment();
                            replaceFragment(fragment);
                        } catch (Exception e) {
                            Utilities.LogException(e);
                        }
                    }

                    @Override
                    public void onFailure(Call<InvoiceDataModel> call, Throwable t) {
                        try {

                            progress.dismiss();
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(getContext(), "Error posting Invoice!", duration);
                            toast.show();
                        } catch (Exception e) {
                            Utilities.LogException(e);
                        }
                    }
                });
            }
        }


        if (postResponse) {

            if (dataObj.Data.get(0).slsDetailArr.size() > 0) {

                progress.dismiss();
                if (GlobalVariables.IsSalesOrder) {
                    SalesOrderRecordListAPI salesOrderRecordListAPI = new SalesOrderRecordListAPI();
                    salesOrderRecordListAPI.SalesOrderRecordListAPICall();
                }

                Fragment fragment = null;
                fragment = new ReceiptFragment();
                replaceFragment(fragment);
            } else {
                AlertDialog.Builder builder1;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder1 = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder1 = new AlertDialog.Builder(getContext());
                }

                builder1.setTitle("Alert")
                        .setMessage("Select Inventory!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

            }
        }


    }


    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrame, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static final int INVENTORY_DIALOG_FRAGMENT = 1;
    public static final int CONTACT_DIALOG_FRAGMENT = 2;
    public static final int EDIT_INVENTORY_ITEM_FRAGMENT = 3;
    private ListView mListView;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case INVENTORY_DIALOG_FRAGMENT:
                //listen when closed
                try {
                    ArrayList<Inventory> savedData = VariableData.inventoryVariable;
                    if (adbInventory == null) {

                        this.getActivity();
                        adbInventory = new AdapterInventory(getActivity(), R.layout.fragment_invoice_create, savedData);

                        mListView.setAdapter(adbInventory);
                        adbInventory.notifyDataSetChanged();

                    } else {
                        ArrayList<Inventory> changedItems = new ArrayList<Inventory>();
                        for (int l = 0; l < savedData.size(); l++) {
                            if (savedData.get(l).TotalItems >= 1) {
                                changedItems.add(savedData.get(l));
                                saveInvBtn = (Button) view.findViewById(R.id.buttonSaveInvoice);
                                saveInvBtn.setEnabled(true);
                            }
                        }
                        SalesOrderVariableData.salesOrderVariable = changedItems;
                        populateInvoiceTable(tableLayout, changedItems);
                    }
                } catch
                        (Exception e) {

                    System.out.println("Error " + e.getMessage());

                }
            case CONTACT_DIALOG_FRAGMENT:
                //contact
                if (CustomerVariableData.customerVariable != null) {
                    try {
                        getCustomerPoints(CustomerVariableData.customerVariable.customerId);
                        Thread.sleep(3000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    //handles go back crash
                    if (getView() != null) {
                        populateInvoiceForm();
                    }
                }

            case EDIT_INVENTORY_ITEM_FRAGMENT:
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mainview.getWindowToken(), 0);
                populateInvoiceTable(tableLayout, SalesOrderVariableData.salesOrderVariable);

        }
    }


    @Override
    public void onFinishEditDialog(String inputText) {

    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (!visible) {
            //when fragment becomes invisible to user,do what you want...
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView totalLbl = (TextView) this.getView().findViewById(R.id.totalLbl);
        totalLbl.setText("Total:");
        TextView totals = (TextView) this.getView().findViewById(R.id.totalstxt);
        totals.setText(Double.toString(totalcost));
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

    }

    public void populateInvoiceForm() {

        ImageView btnInv = (ImageView) mainview.findViewById(R.id.buttonAddInventory);
        TextView customer = (TextView) mainview.findViewById(R.id.input_customer_id);
        if (CustomerVariableData.customerVariable.fName != null) {
            TextView firstName = (TextView) getView().findViewById(R.id.input_fname);
            firstName.setText(CustomerVariableData.customerVariable.fName);
            if (firstName.getText().length() > 0) {
                btnInv.setEnabled(true);
            }
        }
        if (CustomerVariableData.customerVariable.lName != null) {
            TextView lastName = (TextView) getView().findViewById(R.id.input_lname);
            lastName.setText(CustomerVariableData.customerVariable.lName);
        }
        if (CustomerVariableData.customerVariable.phone1 != null) {
            TextView phone1 = (TextView) getView().findViewById(R.id.input_phone1);
            phone1.setText(CustomerVariableData.customerVariable.phone1);
        }

        if (CustomerVariableData.customerVariable.phone2 != null) {
            TextView phone2 = (TextView) getView().findViewById(R.id.input_phone2);
            phone2.setText(CustomerVariableData.customerVariable.phone2);
        }
        if (CustomerVariableData.customerVariable.email != null) {
            TextView email = (TextView) getView().findViewById(R.id.input_email);
            email.setText(CustomerVariableData.customerVariable.email);
        }
        if (CustomerVariableData.customerVariable.customerId != null) {
            TextView customerId = (TextView) getView().findViewById(R.id.input_customer_id);
            customerId.setText(CustomerVariableData.customerVariable.customerId);
        }
    }

    public void populateInvoiceTable(final TableLayout tl, final ArrayList<Inventory> data) {

        totalcost = 0.0;
        totaltax = 0.0;
        int totalPoints = 0;

        tl.removeAllViewsInLayout();
        TableRow tr_head = new TableRow(getContext());
        tr_head.setId(Integer.parseInt("10"));
        tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.MATCH_PARENT));


        TextView label_edit = new TextView(getContext());
        label_edit.setId(Integer.parseInt("20"));
        label_edit.setText("Edit");
        label_edit.setTextSize(12);
        label_edit.setTextColor(Color.WHITE);
        label_edit.setGravity(Gravity.RIGHT);
        label_edit.setPadding(5, 5, 5, 5);
        tr_head.addView(label_edit);// a

        TextView label_desc = new TextView(getContext());
        label_desc.setId(Integer.parseInt("20"));
        label_desc.setText("Desc");
        label_desc.setTextSize(12);
        label_desc.setTextColor(Color.WHITE);
        label_desc.setPadding(5, 5, 5, 5);
        tr_head.addView(label_desc);

        TextView label_itemNo = new TextView(getContext());
        label_itemNo.setId(Integer.parseInt("20"));
        label_itemNo.setText("Item");
        label_itemNo.setTextSize(12);
        label_itemNo.setTextColor(Color.WHITE);
        label_itemNo.setPadding(1, 5, 1, 5);
        tr_head.addView(label_itemNo);// add the column to the table row here

        TextView label_sku = new TextView(getContext());
        label_sku.setId(Integer.parseInt("21"));
        label_sku.setText("Price");
        label_sku.setTextSize(12);
        label_sku.setTextColor(Color.WHITE);
        label_sku.setPadding(5, 5, 5, 5);
        tr_head.addView(label_sku);

        TextView label_qty = new TextView(getContext());
        label_qty.setId(Integer.parseInt("20"));
        label_qty.setText("Qty");
        label_qty.setWidth(100);
        label_qty.setTextSize(12);
        label_qty.setTextColor(Color.WHITE);
        label_qty.setPadding(1, 5, 1, 5);
        tr_head.addView(label_qty);// a


        TextView label_points = new TextView(getContext());
        label_points.setId(Integer.parseInt("20"));
        label_points.setText("Points");
        label_points.setTextSize(12);
        label_points.setTextColor(Color.WHITE);
        label_points.setGravity(Gravity.CENTER);
        label_points.setPadding(5, 5, 5, 5);
        tr_head.addView(label_points);

        TextView label_tax = new TextView(getContext());
        label_tax.setId(Integer.parseInt("20"));
        label_tax.setText(GlobalVariables.salesTaxAbbreviation);
        label_tax.setTextSize(12);
        label_tax.setTextColor(Color.WHITE);
        label_tax.setGravity(Gravity.CENTER);
        label_tax.setPadding(5, 5, 5, 5);
        tr_head.addView(label_tax);

        TextView label_price_sold = new TextView(getContext());
        label_price_sold.setId(Integer.parseInt("20"));
        label_price_sold.setText("Total");
        label_price_sold.setTextSize(12);
        label_price_sold.setTextColor(Color.WHITE);
        label_price_sold.setGravity(Gravity.CENTER);
        label_price_sold.setPadding(5, 5, 5, 5);
        tr_head.addView(label_price_sold);


        TextView label_action = new TextView(getContext());
        label_action.setId(Integer.parseInt("20"));
        label_action.setText("Del");
        label_action.setTextSize(12);
        label_action.setTextColor(Color.WHITE);
        label_action.setGravity(Gravity.RIGHT);
        label_action.setPadding(5, 5, 5, 5);
        tr_head.addView(label_action);

        tl.addView(tr_head, new TableLayout.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        Integer count = 0;

        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                Double taxItem = 0.0;
                String classId = data.get(i).itemNum.substring(0, 3);
                if (data.get(i).qty != null) {
// Create the table row
                    TableRow tr = new TableRow(getContext());
                    if (count % 2 != 0) tr.setBackgroundColor(Color.LTGRAY);
                    tr.setId(100 + count);
                    tr.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.FILL_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));

//Create  columns to add as table data
                    // Create a TextView to add date

                    if (classId.equals("017")) {
                        TextView empty = new TextView(getContext());
                        empty.setId(200 + count);
                        empty.setText(" ");
                        empty.setTextSize(10);
                        empty.setWidth(50);
                        empty.setPadding(2, 0, 5, 0);
                        empty.setTextColor(Color.BLACK);
                        tr.addView(empty);
                    } else {
                        ImageView editInv = new ImageView(getContext());
                        editInv.setImageResource(R.drawable.ic_mode_edit_black_24px);
                        editInv.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                int selectedInv = v.getId();
                                EditInventoryItemFragment bFragment = new EditInventoryItemFragment();
                                bFragment.setTargetFragment(InvoiceCreateFragment.this, 1);
                                bFragment.show(getFragmentManager(), "edit inventory");
                                Inventory inv = data.get(selectedInv);
                                inv.isEditMode = true;
                                SalesOrderVariableData.salesOrderVariable = data;
                                InventoryVariableData.inventoryVariable = inv;
                                populateInvoiceTable(tl, data);
                            }
                        });

                        editInv.setId(i);
                        tr.addView(editInv);
                    }


                    TextView description = new TextView(getContext());
                    description.setId(200 + count);
                    description.setText(data.get(i).description);
                    description.setTextSize(10);
                    description.setPadding(2, 0, 5, 0);
                    description.setTextColor(Color.BLACK);
                    tr.addView(description);

                    TextView itemNumber = new TextView(getContext());
                    itemNumber.setId(200 + count);
                    itemNumber.setText(data.get(i).itemNum);
                    itemNumber.setTextSize(10);
                    itemNumber.setPadding(2, 0, 5, 0);
                    itemNumber.setTextColor(Color.BLACK);
                    tr.addView(itemNumber);


                    TextView labelSKU = new TextView(getContext());
                    labelSKU.setId(200 + count);
                    if (data.get(i).priceSold != null) {
                        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance(getCurrentLocale(getContext()));
                        labelSKU.setText(defaultFormat.format(Double.parseDouble(data.get(i).priceSold)));
                        labelSKU.setTextSize(10);
                        labelSKU.setTextColor(Color.BLACK);
                        tr.addView(labelSKU);
                    }
                    TextView labelQNTY = new TextView(getContext());
                    labelQNTY.setId(200 + count);
                    labelQNTY.setText(data.get(i).qty);
                    label_qty.setWidth(100);
                    labelQNTY.setTextSize(10);
                    labelQNTY.setTextColor(Color.BLACK);
                    labelQNTY.setGravity(Gravity.CENTER);

                    tr.addView(labelQNTY);

                    TextView labelPOINTS = new TextView(getContext());
                    labelPOINTS.setId(200 + count);
                    if (data.get(i).sellingPrice != null) {

                        double sellingPrice = Double.parseDouble(data.get(i).sellingPrice);
                        int intsellingprice = (int) sellingPrice;
                        int pp = 0;
                        if (GlobalVariables.IsSalesOrder) {
                            if (data.get(i).points == null) {
                                data.get(i).points = "0";
                            }
                            double cl = Math.ceil(Double.parseDouble(data.get(i).points));
                            pp = (int) cl;
                            data.get(i).points = String.valueOf(pp);

                        } else {
                            double ttprice = 0.0;
                            double extendedPrice = 0.0;
                            double cl = 0.0;
                            if (GlobalVariables.taxIncluded.equals("1")) {
                                double taxAmount = Double.parseDouble(data.get(i).taxPercentage) * (Double.parseDouble(data.get(i).sellingPrice) / (1 + Double.parseDouble(data.get(i).taxPercentage)));
                                DecimalFormat twoDForm = new DecimalFormat("#.##");
                                taxAmount = Double.valueOf(twoDForm.format(taxAmount));

                                double unitPrice = Math.ceil(Double.parseDouble(data.get(i).sellingPrice) - taxAmount);
                                String cval = customerIdText.getText().toString();
                                if (cval.length() == 0) {
                                    cval = "0";
                                }
                                int cusID = Integer.parseInt(cval);
                                if (cusID == 0 || cusID > Integer.parseInt(globalVars.highCustomerLimit)) {
                                    pp = 0;
                                } else {
                                    double doublepoints = Double.parseDouble(data.get(i).qty) * Double.parseDouble(String.valueOf(globalVars.pointsPerDollar)) * unitPrice;
                                    pp = (int) Math.ceil(doublepoints);
                                }

                                data.get(i).points = String.valueOf(pp);
                            } else if (GlobalVariables.taxIncluded.equals("0")) {
                                ttprice = Double.parseDouble(data.get(i).sellingPrice) * Double.parseDouble(data.get(i).qty);
                                extendedPrice = Math.ceil(ttprice / (Math.pow(10, Integer.parseInt(GlobalVariables.roundSalesUpFactor))) * Math.pow(10, Integer.parseInt(GlobalVariables.roundSalesUpFactor)));


                                cl = extendedPrice * globalVars.pointsPerDollar;
                                pp = (int) cl;
                                data.get(i).points = String.valueOf(pp);
                            }
                        }
                        totalPoints = (int) (totalPoints + pp);

                        if (classId.equals("017")) {
                            if (data.get(i).points != null) {
                                double cl = Math.ceil(Double.parseDouble(data.get(i).points));
                                pp = (int) cl;
                                data.get(i).points = String.valueOf(pp);
                            }
                        }
                        labelPOINTS.setText(String.valueOf(pp));
                        labelPOINTS.setTextSize(10);
                        labelPOINTS.setTextColor(Color.BLACK);
                        labelPOINTS.setGravity(Gravity.CENTER);
                        tr.addView(labelPOINTS);

                    }
                    TextView tax = new TextView(getContext());
                    tax.setId(200 + count);

                    tax.setTextSize(10);
                    tax.setTextColor(Color.BLACK);
                    tax.setGravity(Gravity.CENTER);
                    if (data.get(i).taxPercentage != null) {
                        Double tx = Double.valueOf(data.get(i).taxPercentage);

                        if (GlobalVariables.taxIncluded.equals("1")) {
                            if (tx > 0) {
                                tax.setText("T");
                                double taxAmount = Double.parseDouble(data.get(i).taxPercentage) * (Double.parseDouble(data.get(i).sellingPrice) / (1 + Double.parseDouble(data.get(i).taxPercentage)));
                                taxAmount = taxAmount * Double.parseDouble(data.get(i).qty);
                                DecimalFormat twoDForm = new DecimalFormat("#.##");
                                taxAmount = Double.valueOf(twoDForm.format(taxAmount));
                                totaltax = totaltax + taxAmount;


                            } else {
                                tax.setText("");
                            }
                        } else if (GlobalVariables.taxIncluded.equals("0")) {
                            if (tx > 0) {
                                tax.setText("T");
                                taxItem = tx * Double.parseDouble(data.get(i).sellingPrice);
                                taxItem = taxItem * Double.parseDouble(data.get(i).qty);
                                totaltax = totaltax + taxItem;

                            } else {
                                tax.setText("");
                            }


                        }
                    }
                    tr.addView(tax);

                    TextView labelPRICESOLD = new TextView(getContext());
                    labelPRICESOLD.setId(200 + count);
                    if (data.get(i).sellingPrice != null) {
                        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance(getCurrentLocale(getContext()));
                        double tttx = Double.parseDouble(String.valueOf(taxItem));
                        double ttprice = Double.parseDouble(data.get(i).sellingPrice) * Double.parseDouble(data.get(i).qty);

                        double extendedPrice = Math.ceil(ttprice / (Math.pow(10, Integer.parseInt(GlobalVariables.roundSalesUpFactor))) * Math.pow(10, Integer.parseInt(GlobalVariables.roundSalesUpFactor)));
                        extendedPrice = extendedPrice + tttx;
                        String priceSold = defaultFormat.format(extendedPrice);
                        labelPRICESOLD.setText(priceSold);
                        labelPRICESOLD.setTextSize(10);
                        labelPRICESOLD.setTextColor(Color.BLACK);
                        labelPRICESOLD.setGravity(Gravity.RIGHT);
                        tr.addView(labelPRICESOLD);
                        totalcost = totalcost + extendedPrice;
                    }


                    ImageView act = new ImageView(getContext());
                    act.setImageResource(R.drawable.ic_remove_circle_black_24px);
                    act.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            int selectedInv = v.getId();
                            Inventory inv = data.get(selectedInv);
                            for (int i = 0; i < VariableData.inventoryVariable.size(); i++) {
                                if (VariableData.inventoryVariable.get(i).itemNum.equals(inv.itemNum)) {
                                    VariableData.inventoryVariable.get(i).TotalItems = 0.0;
                                    VariableData.inventoryVariable.get(i).qty = "0";
                                }
                            }

                            data.remove(selectedInv);
                            SalesOrderVariableData.salesOrderVariable = data;
                            populateInvoiceTable(tl, data);

                        }
                    });
                    act.setId(i);
                    tr.addView(act);

                    tr.setMinimumHeight(60);

// finally add this to the table row
                    tl.addView(tr, new TableLayout.LayoutParams(
                            TableRow.LayoutParams.FILL_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    count++;
                }

                SalesOrderVariableData.Totals = totalcost;
                if (view != null) {
                    if (GlobalVariables.taxIncluded.equals("1")) {
                        TextView totalLbl = (TextView) view.findViewById(R.id.totalLbl);
                        totalLbl.setText("Total(Inclusive of " + GlobalVariables.salesTaxAbbreviation + "): ");
                        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance(getCurrentLocale(getContext()));
                        Double tt = totalcost;
                        totals.setText(defaultFormat.format(Math.ceil(tt)));
                    } else if (GlobalVariables.taxIncluded.equals("0")) {
                        TextView totalLbl = (TextView) view.findViewById(R.id.totalLbl);
                        totalLbl.setText("Total: ");
                        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance(getCurrentLocale(getContext()));
                        Double tt = totalcost;
                        totals.setText(defaultFormat.format(tt));
                    }
                    if (GlobalVariables.taxIncluded.equals("0")) {
                        TextView subtotalLbl = (TextView) view.findViewById(R.id.subtotalLbl);
                        subtotalLbl.setText("SubTotal: ");
                    } else {
                        TextView subtotalLbl = (TextView) view.findViewById(R.id.subtotalLbl);
                        subtotalLbl.setVisibility(View.GONE);
                        subtotals.setVisibility(View.GONE);
                    }

                    NumberFormat defaultFormat1 = NumberFormat.getCurrencyInstance(getCurrentLocale(getContext()));
                    double subt = Math.ceil(totalcost - totaltax);
                    subtotals.setText(defaultFormat1.format(subt));

                    if (GlobalVariables.taxIncluded.equals("0")) {
                        TextView gctLbl = (TextView) view.findViewById(R.id.totalgct);
                        gctLbl.setText(GlobalVariables.salesTaxAbbreviation);
                        NumberFormat defaultFormat2 = NumberFormat.getCurrencyInstance(getCurrentLocale(getContext()));
                        gcttotals.setText(defaultFormat2.format(totaltax));
                    } else {
                        // gcttotals.setVisibility(View.INVISIBLE);
                        TextView gctLbl = (TextView) view.findViewById(R.id.totalgct);
                        gctLbl.setText(GlobalVariables.salesTaxAbbreviation);
                        NumberFormat defaultFormat2 = NumberFormat.getCurrencyInstance(getCurrentLocale(getContext()));
                        gcttotals.setText(defaultFormat2.format(totaltax));
                    }

                    TextView totalPointsLbl = (TextView) view.findViewById(R.id.subtotalpointsBeforelbl);
                    totalPointsLbl.setText("Points Before: ");

                    TextView totalCurrentPointsLbl = (TextView) view.findViewById(R.id.subtotalpointsNewLbl);
                    totalCurrentPointsLbl.setText("New Points: ");

                    TextView totalPointsAfterLbl = (TextView) view.findViewById(R.id.subtotalpointsAfterLbl);
                    totalPointsAfterLbl.setText("Total Points: ");
                    if (CustomerVariableData.customerVariable == null) {
                        CustomerVariableData.customerVariable = new CustomerRecord();
                        CustomerVariableData.customerVariable.customerId = "999999999";
                    } else {
                        if (CustomerVariableData.customerVariable.points == null) {
                            CustomerVariableData.customerVariable.points = "0";
                        }
                    }
                    int ttpnts = Integer.parseInt(CustomerVariableData.customerVariable.points) + totalPoints;
                    userTotalPoints = ttpnts;
                    if (CustomerVariableData.customerVariable.points != null) {
                        totalsPoints.setText(NumberFormat.getIntegerInstance().format(ttpnts));
                    }
                    newPoints.setText(NumberFormat.getIntegerInstance().format(totalPoints));
                    NewPoints = totalPoints;


                    ArrayList<SalesInvoiceRecord> cusHis = myPosBase.GetCustomerPointsHistory(CustomerVariableData.customerVariable.customerId);
                    int customerpoints = 0;
                    if (cusHis != null && cusHis.size() > 0) {

                        CustomerVariableData.customerVariable.points = String.valueOf(cusHis.get(0).lastPoints + cusHis.get(0).newPoints);
                    }

                    if (Integer.parseInt(CustomerVariableData.customerVariable.points) > 0) {
                        beforePoints.setText(NumberFormat.getIntegerInstance().format(Integer.parseInt(CustomerVariableData.customerVariable.points)));
                    }


                }

            }

            saveInvBtn.setEnabled(true);
        } else {
            NumberFormat defaultFormat1 = NumberFormat.getCurrencyInstance(getCurrentLocale(getContext()));
            totals.setText(defaultFormat1.format(0.0));
            subtotals.setText(defaultFormat1.format(0.0));
            gcttotals.setText(defaultFormat1.format(0.0));
            newPoints.setText(String.valueOf(0));
            if (CustomerVariableData.customerVariable != null) {
                if (CustomerVariableData.customerVariable.points == null) {
                    CustomerVariableData.customerVariable.points = "0";
                    beforePoints.setText(CustomerVariableData.customerVariable.points);
                    totalsPoints.setText(CustomerVariableData.customerVariable.points);
                }

            }
            // saveInvBtn.setEnabled(false);
        }
    }


    public void getCustomerPoints(String e) {
        if (CustomerVariableData.customerVariable == null) {
            CustomerVariableData.customerVariable = new CustomerRecord();
        }
        ArrayList<SalesInvoiceRecord> cusHis = myPosBase.GetCustomerPointsHistory(e);
        if (cusHis != null && cusHis.size() > 0) {
            if (cusHis != null && cusHis.size() > 0) {
                Collections.sort(cusHis, new Comparator<SalesInvoiceRecord>() {
                    @Override
                    public int compare(SalesInvoiceRecord lhs, SalesInvoiceRecord rhs) {

                        return Integer.valueOf( rhs.getId().intValue()).compareTo(lhs.getId().intValue());
                    }
                });
            }
            CustomerVariableData.customerVariable.points = String.valueOf((cusHis.get(0).lastPoints + cusHis.get(0).newPoints));
        } else {
            CustomerRecord rec = MyPosBase.GetCustomerRecord(e);

            if (rec != null) {

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
                            if (response.body().dataObj.Data != null && response.body().dataObj.Data.size() > 0) {
                                CustomerVariableData.customerVariable.points = response.body().dataObj.Data.get(0).points;
                                int pnts = Integer.parseInt(response.body().dataObj.Data.get(0).points);
                                beforePoints.setText(NumberFormat.getIntegerInstance().format(pnts));
                            } else {
                                CustomerVariableData.customerVariable.points = "0";
                                beforePoints.setText("0");
                            }
                        } catch (Exception ex) {
                            Utilities.LogException(ex);
                        }
                    }

                    @Override
                    public void onFailure(Call<CustomerDataModel> call, Throwable t) {
                        try {
                        } catch (Exception ex) {
                            Utilities.LogException(ex);
                            CustomerVariableData.customerVariable.points = "0";
                        }
                    }
                });
            }


        }
    }

}
