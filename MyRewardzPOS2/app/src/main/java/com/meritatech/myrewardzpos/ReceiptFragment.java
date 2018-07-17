package com.meritatech.myrewardzpos;

import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.meritatech.myrewardzpos.controller.GlobalVariables;
import com.meritatech.myrewardzpos.controller.ReceiptPrintDocumentAdapter;
import com.meritatech.myrewardzpos.global.CustomerVariableData;
import com.meritatech.myrewardzpos.global.SalesOrderVariableData;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import static com.meritatech.myrewardzpos.controller.GlobalVariables.getCurrentLocale;


public class ReceiptFragment extends Fragment
        implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    GlobalVariables globalVars = new GlobalVariables();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ReceiptFragment() {
        // Required empty public constructor
    }


    public static ReceiptFragment newInstance(String param1, String param2) {
        ReceiptFragment fragment = new ReceiptFragment();
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
        getActivity().setTitle("Receipt");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_receipt, container, false);
//get client points

        Button finishBtn = (Button) view.findViewById(R.id.buttonFinish);
        finishBtn.setOnClickListener(this);
        double ttPoints = 0.0;
        ImageView printBtn = (ImageView) view.findViewById(R.id.buttonPrint);
        printBtn.setOnClickListener(this);
        ImageView msgBtn = (ImageView) view.findViewById(R.id.buttonmsg);
        msgBtn.setOnClickListener(this);
        if (CustomerVariableData.customerVariable != null) {
            if ( CustomerVariableData.customerVariable.phone1 == null) {
                msgBtn.setVisibility(View.INVISIBLE);
            }
        }

        ImageView whatsappBtn = (ImageView) view.findViewById(R.id.buttonwhatsapp);
        whatsappBtn.setOnClickListener(this);

        TableLayout tl = (TableLayout) view.findViewById(R.id.receiptItems);
        TableRow tr_head = new TableRow(getContext());
        tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.MATCH_PARENT));

        TextView label_desc = new TextView(getContext());
        label_desc.setText("Desc");
        label_desc.setTextSize(11);
        label_desc.setTextColor(Color.WHITE);
        label_desc.setPadding(5, 5, 5, 5);
        tr_head.addView(label_desc);// add the column to the table row here

        TextView label_sku = new TextView(getContext());
        label_sku.setText("Item#"); // set the text for the header
        label_sku.setTextSize(11);
        label_sku.setTextColor(Color.WHITE); // set the color
        label_sku.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(label_sku); // add the column to the table row here

        TextView label_price_sold = new TextView(getContext());
        label_price_sold.setText("Price");
        label_price_sold.setTextColor(Color.WHITE);
        label_price_sold.setTextSize(11);
        label_price_sold.setPadding(5, 5, 5, 5);
        label_price_sold.setGravity(Gravity.RIGHT);
        tr_head.addView(label_price_sold);

        TextView label_qty = new TextView(getContext());
        label_qty.setText("Qnty");
        label_qty.setTextSize(11);
        label_qty.setTextColor(Color.WHITE);
        label_qty.setPadding(5, 5, 5, 5);
        tr_head.addView(label_qty);// a


        TextView label_tx = new TextView(getContext());
        label_tx.setText("Tax");
        label_tx.setTextSize(11);
        label_tx.setTextColor(Color.WHITE);
        label_tx.setPadding(5, 5, 5, 5);
        tr_head.addView(label_tx);// a


        TextView label_points = new TextView(getContext());
        label_points.setText("Points");
        label_points.setTextColor(Color.WHITE);
        label_points.setTextSize(11);
        label_points.setPadding(5, 5, 5, 5);
        tr_head.addView(label_points);

        TextView label_total = new TextView(getContext());
        label_total.setText("Total");
        label_total.setTextColor(Color.WHITE);
        label_total.setTextSize(11);
        label_total.setPadding(5, 5, 5, 5);
        label_total.setGravity(Gravity.RIGHT);
        tr_head.addView(label_total);// a

        tl.addView(tr_head, new TableLayout.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        Integer count = 0;
        double ttTax = 0.0;
        if (SalesOrderVariableData.salesOrderVariable != null && SalesOrderVariableData.salesOrderVariable.size() > 0) {
            NumberFormat defaultFormat = NumberFormat.getCurrencyInstance(getCurrentLocale(getContext()));
            for (int i = 0; i < SalesOrderVariableData.salesOrderVariable.size(); i++) {
                if (!SalesOrderVariableData.salesOrderVariable.get(i).qty.equals("null") || SalesOrderVariableData.salesOrderVariable.get(i).qty.equals("")) {
                    if (Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).qty) > 0) {

// Create the table row
                        TableRow tr = new TableRow(getContext());
                        if (count % 2 != 0) tr.setBackgroundColor(Color.LTGRAY);
                        tr.setId(100 + count);
                        tr.setLayoutParams(new TableRow.LayoutParams(
                                TableRow.LayoutParams.FILL_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));

//Create  columns to add as table data
                        // Create a TextView to add item
                        TextView labelDATE = new TextView(getContext());
                        labelDATE.setId(200 + count);
                        labelDATE.setText(SalesOrderVariableData.salesOrderVariable.get(i).description);
                        labelDATE.setPadding(2, 0, 5, 0);
                        labelDATE.setTextColor(Color.BLACK);
                        labelDATE.setTextSize(10);
                        tr.addView(labelDATE);

                        TextView labelitem = new TextView(getContext());
                        labelitem.setId(200 + count);
                        labelitem.setText(SalesOrderVariableData.salesOrderVariable.get(i).itemNum);
                        labelitem.setTextSize(10);
                        labelitem.setPadding(2, 0, 5, 0);
                        labelitem.setGravity(Gravity.LEFT);
                        labelitem.setTextColor(Color.BLACK);
                        tr.addView(labelitem);

                        if (SalesOrderVariableData.salesOrderVariable.get(i).priceSold.equals("null") || SalesOrderVariableData.salesOrderVariable.get(i).priceSold.equals("")) {
                            SalesOrderVariableData.salesOrderVariable.get(i).priceSold = "0.0";
                        }
                        TextView labelPRICESOLD = new TextView(getContext());
                        labelPRICESOLD.setId(200 + count);
                        labelPRICESOLD.setText(defaultFormat.format(Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).priceSold)));
                        labelPRICESOLD.setTextColor(Color.BLACK);
                        labelPRICESOLD.setTextSize(10);
                        labelPRICESOLD.setGravity(Gravity.RIGHT);
                        tr.addView(labelPRICESOLD);

                        TextView labelQNTY = new TextView(getContext());
                        labelQNTY.setId(200 + count);
                        labelQNTY.setText(SalesOrderVariableData.salesOrderVariable.get(i).qty);
                        labelQNTY.setTextColor(Color.BLACK);
                        label_qty.setWidth(100);
                        labelQNTY.setTextSize(10);
                        labelQNTY.setGravity(Gravity.CENTER);
                        tr.addView(labelQNTY);


                        TextView labeltx = new TextView(getContext());
                        labeltx.setId(200 + count);

                        if(SalesOrderVariableData.salesOrderVariable != null) {
                            if (SalesOrderVariableData.salesOrderVariable.get(i).taxPercentage != null && !SalesOrderVariableData.salesOrderVariable.get(i).taxPercentage.isEmpty()) {
                                if (Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).taxPercentage) > 0) {
                                    double taxAmount = 0;
                                    if (GlobalVariables.taxIncluded.equals("1")) {
                                        taxAmount = Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).taxPercentage) * (Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).priceSold) / (1 + Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).taxPercentage)));
                                        taxAmount = taxAmount * Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).qty);
                                        DecimalFormat twoDForm = new DecimalFormat("#.##");
                                        taxAmount = Double.valueOf(twoDForm.format(taxAmount));
                                    } else if (GlobalVariables.taxIncluded.equals("0")) {

                                        taxAmount = Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).taxPercentage) * Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).priceSold);
                                        taxAmount = taxAmount * Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).qty);
                                        DecimalFormat twoDForm = new DecimalFormat("#.##");
                                        taxAmount = Double.valueOf(twoDForm.format(taxAmount));

                                    }


                                    ttTax = ttTax + taxAmount;
                                    labeltx.setText("T");
                                } else {
                                    labeltx.setText("");
                                }
                            }
                        }
                        labeltx.setTextColor(Color.BLACK);
                        label_qty.setWidth(100);
                        labeltx.setTextSize(10);
                        labeltx.setGravity(Gravity.CENTER);
                        tr.addView(labeltx);


                        TextView labelPOINTS = new TextView(getContext());
                        labelPOINTS.setId(200 + count);
                        labelPOINTS.setText(SalesOrderVariableData.salesOrderVariable.get(i).points);
                        labelPOINTS.setTextColor(Color.BLACK);
                        label_qty.setWidth(100);
                        labelPOINTS.setTextSize(10);
                        labelPOINTS.setGravity(Gravity.LEFT);
                        tr.addView(labelPOINTS);
                        if (SalesOrderVariableData.salesOrderVariable.get(i).points != null) {
                            ttPoints = ttPoints + Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).points);
                        }
                        TextView labeltotal = new TextView(getContext());
                        labeltotal.setId(200 + count);

                        double ttprice = Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).priceSold) * Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).qty);
                        double extendedPrice = Math.ceil(ttprice / (Math.pow(10, Integer.parseInt(GlobalVariables.roundSalesUpFactor))) * Math.pow(10, Integer.parseInt(GlobalVariables.roundSalesUpFactor)));
                        String priceSold = defaultFormat.format(extendedPrice);

                        labeltotal.setText(priceSold);
                        labeltotal.setTextColor(Color.BLACK);
                        labeltotal.setTextSize(10);
                        labeltotal.setGravity(Gravity.RIGHT);
                        tr.addView(labeltotal);

                        // finally add this to the table row
                        tl.addView(tr, new TableLayout.LayoutParams(
                                TableRow.LayoutParams.FILL_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        count++;
                    }
                }
            }
        }
        TextView fname = (TextView) view.findViewById(R.id.fNameR);
        fname.setText(CustomerVariableData.customerVariable.fName);
        TextView lname = (TextView) view.findViewById(R.id.lNameR);
        lname.setText(CustomerVariableData.customerVariable.lName);
        TextView phone1R = (TextView) view.findViewById(R.id.phone1R);
        phone1R.setText(CustomerVariableData.customerVariable.phone1);
        TextView phone2R = (TextView) view.findViewById(R.id.phone2R);
        phone2R.setText(CustomerVariableData.customerVariable.phone2);
        TextView emailr = (TextView) view.findViewById(R.id.emailR);
        emailr.setText(CustomerVariableData.customerVariable.email);
        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance(getCurrentLocale(getContext()));
        TextView totals = (TextView) view.findViewById(R.id.totaltxt);
        totals.setText(defaultFormat.format(SalesOrderVariableData.Totals));
        TextView totalstx = (TextView) view.findViewById(R.id.totaltaxtxt);
        totalstx.setText(defaultFormat.format(ttTax));


        TextView txLbl = (TextView) view.findViewById(R.id.txLbl);
        txLbl.setText("Totals (" + GlobalVariables.salesTaxAbbreviation + " Inc.)");

        TextView tcpoints = (TextView) view.findViewById(R.id.pntstxt);
        int s = (int) ttPoints;
        String scomma = NumberFormat.getIntegerInstance().format(s);
        tcpoints.setText(scomma);

        TextView totalsPoints = (TextView) view.findViewById(R.id.totalpntstxt);
        TextView beforePoints = (TextView) view.findViewById(R.id.pntsbeforetxt);
        if (CustomerVariableData.customerVariable != null) {
            if (CustomerVariableData.customerVariable.points == null) {
                CustomerVariableData.customerVariable.points = "0";
            }
        }
        double ttp = ttPoints + Double.parseDouble(CustomerVariableData.customerVariable.points);
        int stringPoints =CustomerVariableData.customerVariable.newpoints + CustomerVariableData.customerVariable.lastpoints;
        String ttpcomma = NumberFormat.getIntegerInstance().format(stringPoints);
        totalsPoints.setText(ttpcomma);
        beforePoints.setText(String.valueOf(CustomerVariableData.customerVariable.lastpoints));
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
    public void onClick(final View v) {
        FragmentManager fm = getActivity().getFragmentManager();
        if (CustomerVariableData.customerVariable.points == null) {
            CustomerVariableData.customerVariable.points = "0";
        }
        switch (v.getId()) {

            case R.id.buttonPrint:

                PrintManager printManager = (PrintManager) getActivity()
                        .getSystemService(Context.PRINT_SERVICE);

                // Set job name, which will be displayed in the print queue
                String jobName = getActivity().getString(R.string.app_name) + " Receipt";
                // Start a print job, passing in a PrintDocumentAdapter implementation
                // to handle the generation of a print document
                printManager.print(jobName, new ReceiptPrintDocumentAdapter(getContext()),
                        null);
                break;
            case R.id.buttonwhatsapp:
                NumberFormat defaultFormat = NumberFormat.getCurrencyInstance(getCurrentLocale(getContext()));
                int total = 0;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String text = "Name: " + CustomerVariableData.customerVariable.fName + " " + CustomerVariableData.customerVariable.lName + "  Items{";
                for (int i = 0; i < SalesOrderVariableData.salesOrderVariable.size(); i++) {
                    double tot = Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).qty) * Integer.parseInt(SalesOrderVariableData.salesOrderVariable.get(i).priceSold);
                    total = (int) (total + tot);
                    text = text + SalesOrderVariableData.salesOrderVariable.get(i).qty + "x" + SalesOrderVariableData.salesOrderVariable.get(i).description + "(@" + defaultFormat.format(Integer.parseInt(SalesOrderVariableData.salesOrderVariable.get(i).priceSold)) + ")=" + defaultFormat.format(tot);

                    if (i + 1 < SalesOrderVariableData.salesOrderVariable.size()) {
                        text = text + ", ";
                    }
                }
                text = text + "} TOTAL:" + defaultFormat.format(total);
                int ponts = total * GlobalVariables.pointsPerDollar;
                text = text + " POINTS: " + ponts;
                // change with required  application package

                intent.setPackage("com.whatsapp");
                if (intent != null) {
                    intent.putExtra(Intent.EXTRA_TEXT, text);//
                    startActivity(Intent.createChooser(intent, text));
                } else {

                    Toast.makeText(getContext(), "App not found", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.buttonmsg:
                NumberFormat defaultFormat1 = NumberFormat.getCurrencyInstance(getCurrentLocale(getContext()));
                int totalpoints = 0;
                double totMoney = 0;
                double totTax = 0;
                String phoneNumber = CustomerVariableData.customerVariable.phone1;
                String textMSG = "Name: " + CustomerVariableData.customerVariable.fName + " " + CustomerVariableData.customerVariable.lName + " Store Name: " + GlobalVariables.StoreName + "  Items{";
                for (int i = 0; i < SalesOrderVariableData.salesOrderVariable.size(); i++) {

                    double ttprice = Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).priceSold) * Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).qty);
                    double totalperItem = Math.ceil(ttprice / (Math.pow(10, Integer.parseInt(GlobalVariables.roundSalesUpFactor))) * Math.pow(10, Integer.parseInt(GlobalVariables.roundSalesUpFactor)));
                    double taxAmount = 0.0;

                    totMoney = totMoney + totalperItem;
                    if (GlobalVariables.IsSalesOrder) {

                        totalpoints = totalpoints + Integer.parseInt(SalesOrderVariableData.salesOrderVariable.get(i).points);
                    } else {

                        if (GlobalVariables.taxIncluded.equals("1")) {
                            if(SalesOrderVariableData.salesOrderVariable.get(i).taxPercentage == null)
                            {
                                SalesOrderVariableData.salesOrderVariable.get(i).taxPercentage = "0";
                            }
                            if (Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).taxPercentage) > 0) {
                                taxAmount = Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).taxPercentage) * (Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).priceSold) / (1 + Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).taxPercentage)));
                                DecimalFormat twoDForm = new DecimalFormat("#.##");
                                taxAmount = taxAmount * Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).qty);
                                taxAmount = Double.valueOf(twoDForm.format(taxAmount));
                                totTax = totTax + taxAmount;
                                double unitPrice = Math.ceil(Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).priceSold) - (taxAmount / Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).qty)));
                                double doublepoints = Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).qty) * unitPrice * Double.parseDouble(String.valueOf(globalVars.pointsPerDollar));
                                double pp = (int) Math.ceil(doublepoints);
                                int thispoints = (int) pp;
                                totalpoints = totalpoints + thispoints;
                            } else {
                                double cl = Math.ceil(Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).priceSold) * Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).qty) * GlobalVariables.pointsPerDollar);
                                int thispoints = (int) cl;
                                totalpoints = totalpoints + thispoints;
                            }
                        } else if (GlobalVariables.taxIncluded.equals("0")) {

                            if (Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).taxPercentage) > 0) {
                                taxAmount = Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).taxPercentage) * (Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).priceSold));
                                taxAmount = taxAmount * Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).qty);
                                totTax = totTax + taxAmount;
                            }
                            double cl = Math.ceil(Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).priceSold) * Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).qty)) * GlobalVariables.pointsPerDollar;
                            int thispoints = (int) cl;
                            totalpoints = totalpoints + thispoints;
                        }


                    }
                    if(SalesOrderVariableData.salesOrderVariable.get(i).taxPercentage != null) {
                        if (Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).taxPercentage) > 0.00) {
                            textMSG = textMSG + " (T) ";
                        }
                    }
                    if (taxAmount > 0) {
                        if (GlobalVariables.taxIncluded.equals("1")) {
                            textMSG = textMSG + SalesOrderVariableData.salesOrderVariable.get(i).qty + "x" + SalesOrderVariableData.salesOrderVariable.get(i).description + "(@" + defaultFormat1.format(Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).priceSold)) + ")= Inc. Tax " + defaultFormat1.format(totMoney) + " Tax " + taxAmount;
                        } else if (GlobalVariables.taxIncluded.equals("0")) {
                            textMSG = textMSG + SalesOrderVariableData.salesOrderVariable.get(i).qty + "x" + SalesOrderVariableData.salesOrderVariable.get(i).description + "(@" + defaultFormat1.format(Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).priceSold)) + ")=" + defaultFormat1.format(totMoney) + " + Tax " + taxAmount + "= " + defaultFormat1.format(totMoney + taxAmount);
                        }

                    } else {
                        textMSG = textMSG + SalesOrderVariableData.salesOrderVariable.get(i).qty + "x" + SalesOrderVariableData.salesOrderVariable.get(i).description + "(@" + defaultFormat1.format(Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).priceSold)) + ")=" + defaultFormat1.format(totMoney);
                    }

                    if (i + 1 < SalesOrderVariableData.salesOrderVariable.size()) {
                        textMSG = textMSG + ", ";
                    }


                }
                int points = 0;
                points = totalpoints;
                int pointsafter = totalpoints + Integer.parseInt(CustomerVariableData.customerVariable.points);
                if (GlobalVariables.taxIncluded.equals("1")) {
                    if (totTax > 0) {
                        textMSG = textMSG + "}" + " TAX:" + totTax + " TOTAL (Inc." + GlobalVariables.salesTaxAbbreviation + "): " + defaultFormat1.format(totMoney);
                    } else {
                        textMSG = textMSG + "} TOTAL: " + defaultFormat1.format(totMoney);
                    }

                } else if (GlobalVariables.taxIncluded.equals("0")) {
                    if (totTax > 0) {
                        textMSG = textMSG + "}" + " TAX:" + totTax + " SUB TOTAL: " + defaultFormat1.format(totMoney) + " TOTAL:" + defaultFormat1.format(totMoney + totTax);
                    } else {
                        textMSG = textMSG + "} TOTAL: " + defaultFormat1.format(totMoney);
                    }
                }
                textMSG = textMSG + " POINTS: " + points + " POINTS BEFORE: " + CustomerVariableData.customerVariable.lastpoints + " TOTAL POINTS: " + (CustomerVariableData.customerVariable.lastpoints + CustomerVariableData.customerVariable.newpoints );

                if (CustomerVariableData.customerVariable.phone1 != null) {
                    if (CustomerVariableData.customerVariable.phone1 != null && CustomerVariableData.customerVariable.phone2 != null) {
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
                        } else {
                            builder = new AlertDialog.Builder(getContext());
                        }
                        final String finalTextMSG = textMSG;
                        builder.setTitle("Select Phone Number")
                                .setMessage("Select Number to send SMS")
                                .setPositiveButton(CustomerVariableData.customerVariable.phone1, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String phoneNumber = CustomerVariableData.customerVariable.phone1;

                                        SmsManager smsManager = SmsManager.getDefault();
                                        ArrayList<String> parts = smsManager.divideMessage(finalTextMSG);
                                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 1);

                                        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                                        smsIntent.setType("vnd.android-dir/mms-sms");
                                        smsIntent.putExtra("address", phoneNumber);
                                        smsIntent.putExtra("sms_body", finalTextMSG);

                                        v.getContext().startActivity(smsIntent);
                                    }
                                })
                                .setNegativeButton(CustomerVariableData.customerVariable.phone2, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String phoneNumber = CustomerVariableData.customerVariable.phone2;

                                        SmsManager smsManager = SmsManager.getDefault();
                                        ArrayList<String> parts = smsManager.divideMessage(finalTextMSG);
                                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 1);

                                        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                                        smsIntent.setType("vnd.android-dir/mms-sms");
                                        smsIntent.putExtra("address", phoneNumber);
                                        smsIntent.putExtra("sms_body", finalTextMSG);

                                        v.getContext().startActivity(smsIntent);
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    } else {

                        SmsManager smsManager = SmsManager.getDefault();
                        ArrayList<String> parts = smsManager.divideMessage(textMSG);
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 1);

                        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                        smsIntent.setType("vnd.android-dir/mms-sms");
                        smsIntent.putExtra("address", phoneNumber);
                        smsIntent.putExtra("sms_body", textMSG);

                        v.getContext().startActivity(smsIntent);
                    }
                }

                break;

            case R.id.buttonFinish:
                SalesOrderVariableData.salesOrderVariable = null;
                CustomerVariableData.customerVariable = null;
                Fragment fragment = null;
                fragment = new HomeFragment();
                replaceFragment(fragment);
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

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrame, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
