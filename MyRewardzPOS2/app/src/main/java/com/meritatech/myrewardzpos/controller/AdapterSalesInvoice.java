package com.meritatech.myrewardzpos.controller;

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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.meritatech.myrewardzpos.R;
import com.meritatech.myrewardzpos.ReceiptFragment;
import com.meritatech.myrewardzpos.SalesInvoiceHistoryFragment;
import com.meritatech.myrewardzpos.data.CustomerRecord;
import com.meritatech.myrewardzpos.data.InventoryListAPI;
import com.meritatech.myrewardzpos.data.InventoryRecord;
import com.meritatech.myrewardzpos.data.InventotyPost;
import com.meritatech.myrewardzpos.data.MyPosBase;
import com.meritatech.myrewardzpos.data.SalesInvoiceInventoryRecord;
import com.meritatech.myrewardzpos.data.SalesInvoiceRecord;
import com.meritatech.myrewardzpos.data.SalesOrderPost;
import com.meritatech.myrewardzpos.data.SalesOrderRecord;
import com.meritatech.myrewardzpos.data.UserRecord;
import com.meritatech.myrewardzpos.dataObj.InvoiceDataObj;
import com.meritatech.myrewardzpos.enums.CustomerType;
import com.meritatech.myrewardzpos.enums.SalesOrderStatus;
import com.meritatech.myrewardzpos.global.CustomerVariableData;
import com.meritatech.myrewardzpos.global.LocationData;
import com.meritatech.myrewardzpos.global.SalesOrderVariableData;
import com.meritatech.myrewardzpos.global.VariableData;
import com.meritatech.myrewardzpos.model.InvoiceDataModel;
import com.meritatech.myrewardzpos.model.PosServicesInterface;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by user on 11/24/2017.
 */


public class AdapterSalesInvoice extends ArrayAdapter<SalesInvoiceRecord> {
    private Activity activity;
    private ArrayList<SalesInvoiceRecord> ISalesInvoiceRecord;
    private static LayoutInflater inflater = null;
    GlobalVariables globalVars = new GlobalVariables();

    public AdapterSalesInvoice(Activity activity, int textViewResourceId, ArrayList<SalesInvoiceRecord> _ISalesInvoiceRecord) {
        super(activity, textViewResourceId, _ISalesInvoiceRecord);
        try {
            this.activity = activity;
            this.ISalesInvoiceRecord = _ISalesInvoiceRecord;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return ISalesInvoiceRecord.size();
    }

    public SalesOrder getItem(SalesOrder position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView display_name;
        public TextView display_number;
        public TextView display_address;
        public TextView display_cid;
        public TextView display_salesItems;
        public TextView display_status;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        UserRecord userRecord = UserRecord.findAllRecords(UserRecord.class).get(0);
        TextView mobileNo;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.salesinvoicelistlayout, null);
                holder = new ViewHolder();

                holder.display_name = (TextView) vi.findViewById(R.id.fName);
                holder.display_address = (TextView) vi.findViewById(R.id.address);
                holder.display_number = (TextView) vi.findViewById(R.id.phone);
                holder.display_cid = (TextView) vi.findViewById(R.id.cid);
                holder.display_salesItems = (TextView) vi.findViewById(R.id.salesorderItems);
                holder.display_status = (TextView) vi.findViewById(R.id.status);
                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            StringBuilder builder = new StringBuilder();
            if (ISalesInvoiceRecord.get(position).SalesDetails != null) {
                for (int i = 0; i < ISalesInvoiceRecord.get(position).SalesDetails.size(); i++) {
                    builder.append(ISalesInvoiceRecord.get(position).SalesDetails.get(i).qty);
                    builder.append(" x ");
                    builder.append(ISalesInvoiceRecord.get(position).SalesDetails.get(i).description);
                    builder.append("; ");
                }
            }

            holder.display_name.setText(ISalesInvoiceRecord.get(position).fName + " " + ISalesInvoiceRecord.get(position).lName);
            holder.display_number.setText(ISalesInvoiceRecord.get(position).phone1);
            holder.display_address.setText(ISalesInvoiceRecord.get(position).address);
            holder.display_cid.setText(ISalesInvoiceRecord.get(position).customerId);
            holder.display_salesItems.setText(builder);
            ImageView buttonPrint = (ImageView) vi.findViewById(R.id.buttonPrintItem);
            buttonPrint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VariableData.inventoryVariable = null;
                    SalesInvoiceRecord o = ISalesInvoiceRecord.get(position);
                    CustomerRecord cusRec = new CustomerRecord();
                    cusRec.customerId = o.customerId;
                    cusRec.fName = o.fName;
                    cusRec.lName = o.lName;
                    cusRec.phone1 = o.phone1;
                    cusRec.phone2 = o.phone2;
                    cusRec.orderId = o.salesOrderId;
                    cusRec.email = o.email;
                    cusRec.newpoints = o.newPoints;
                    cusRec.lastpoints = o.lastPoints;
                    CustomerVariableData.customerVariable = cusRec;
                    ArrayList<Inventory> invList = new ArrayList<Inventory>();
                    double total = 0;
                    double subTotal = 0;
                    InventoryListAPI inventoryListAPI = new InventoryListAPI();
                    ArrayList<SalesInvoiceInventoryRecord> invItems = inventoryListAPI.getInventoryByOrderId(o.salesOrderId);
                    for (int i = 0; i < invItems.size(); i++) {

                        Inventory inv = new Inventory();
                        inv.itemNum = invItems.get(i).itemNumber;
                        inv.points = invItems.get(i).points;
                        inv.priceSold = invItems.get(i).sellingPrice;
                        inv.sellingPrice = invItems.get(i).sellingPrice;
                        inv.description = invItems.get(i).description;
                        inv.taxPercentage = invItems.get(i).taxPercentage;
                        inv.qty = invItems.get(i).qty;
                        subTotal = Double.parseDouble(invItems.get(i).qty) * Double.parseDouble(invItems.get(i).sellingPrice);
                        total = total + subTotal;
                        invList.add(inv);
                    }
                    VariableData.inventoryVariable = invList;
                    SalesOrderVariableData.salesOrderVariable = invList;
                    SalesOrderVariableData.Totals = total;
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment myFragment = new ReceiptFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, myFragment).addToBackStack(null).commit();

                }
            });

            ImageView buttonCancel = (ImageView) vi.findViewById(R.id.buttoncancelInvoice);
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder1;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder1 = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder1 = new AlertDialog.Builder(getContext());
                    }
                    builder1.setTitle("Confirm Cancel")
                            .setMessage("Are you sure you want to cancel this sale order?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                    final ProgressDialog progress = new ProgressDialog(getContext());
                                    progress.setTitle("Cancel Invoice");
                                    progress.setMessage("Processing");
                                    progress.setCancelable(false);
                                    progress.show();
                                    VariableData.inventoryVariable = null;
                                    SalesInvoiceRecord o = ISalesInvoiceRecord.get(position);
                                    CustomerRecord cusRec = new CustomerRecord();
                                    cusRec.customerId = o.customerId;
                                    cusRec.fName = o.fName;
                                    cusRec.lName = o.lName;
                                    cusRec.phone1 = o.phone;
                                    cusRec.orderId = o.salesOrderId;

                                    CustomerVariableData.customerVariable = cusRec;
                                    ArrayList<Inventory> invList = new ArrayList<Inventory>();

                                    for (int i = 0; i < o.SalesDetails.size(); i++) {
                                        InventoryListAPI inventoryListAPI = new InventoryListAPI();
                                        InventoryRecord ir = inventoryListAPI.getInventoryByItemNumber(o.SalesDetails.get(i).itemNum);
                                        Inventory inv = new Inventory();
                                        inv.itemNum = o.SalesDetails.get(i).itemNum;
                                        inv.points = o.SalesDetails.get(i).points;
                                        inv.priceSold = ir.sellingPrice;
                                        inv.description = ir.description;
                                        inv.qty = o.SalesDetails.get(i).qty;
                                        invList.add(inv);
                                    }
                                    VariableData.inventoryVariable = invList;

                                    MyPosBase myPosBase = new MyPosBase();

                                    SalesInvoiceRecord slR = new SalesInvoiceRecord();
                                    slR.customerId = o.customerId;
                                    slR.fName = o.fName;
                                    slR.lName = o.lName;
                                    slR.email = o.email;
                                    slR.phone1 = o.phone1;
                                    slR.phone2 = o.phone2;
                                    slR.address = o.address;
                                    slR.salesmanId = globalVars.salesmanId;
                                    slR.invoiceDateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                                    slR.parentStoreId = globalVars.ParentId;
                                    slR.storeId = globalVars.storeId;
                                    slR.recId = (int) (SalesInvoiceRecord.count(SalesInvoiceRecord.class) + 1);
                                    slR.salesOrderId = "";
                                    slR.longitude = o.longitude;
                                    slR.latitude = o.latitude;
                                    slR.newPoints = o.newPoints - o.newPoints;
                                    ArrayList<SalesInvoiceRecord> cusHis = myPosBase.GetCustomerPointsHistory(slR.customerId);
                                    if (cusHis != null && cusHis.size() > 0) {
                                        slR.lastPoints = cusHis.get(0).lastPoints - o.newPoints;

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

                                    slR.SalesDetails = invList;
                                    ArrayList<SalesInvoiceInventoryRecord> salesInvoiceInventoryRecords = new ArrayList<SalesInvoiceInventoryRecord>();
                                    UserRecord userRecord = UserRecord.findAllRecords(UserRecord.class).get(0);
                                    if (userRecord.invCancelAgeDays != null) {
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(cal.getTime()); // sets calendar time/date
                                        cal.add(Calendar.DATE, Integer.parseInt(userRecord.invCancelAgeDays));
                                        Date dateToDeliver = cal.getTime();
                                        slR.invoiceCancelAgeDateTime = cal.getTime();
                                    }
                                    slR.salesOrderStatus = SalesOrderStatus.CANCELLED_ORIGINAL;
                                    slR.sent = 0;
                                    boolean postResponse = myPosBase.saveInvoice(slR);

                                    SalesInvoiceRecord sir = SalesInvoiceRecord.findById(SalesInvoiceRecord.class, o.getId());
                                    sir.salesOrderStatus = SalesOrderStatus.CANCELLED;
                                    sir.save();

                                    for (int e = 0; e < slR.SalesDetails.size(); e++) {
                                        SalesInvoiceInventoryRecord salesInvoiceInventoryRecord = new SalesInvoiceInventoryRecord();
                                        salesInvoiceInventoryRecord.itemNumber = slR.SalesDetails.get(e).itemNum;
                                        salesInvoiceInventoryRecord.description = slR.SalesDetails.get(e).description;
                                        salesInvoiceInventoryRecord.qty = "-" + slR.SalesDetails.get(e).qty;
                                        salesInvoiceInventoryRecord.priceSold = slR.SalesDetails.get(e).priceSold;
                                        salesInvoiceInventoryRecord.points = String.valueOf(Integer.parseInt(slR.SalesDetails.get(e).points) * -1);
                                        salesInvoiceInventoryRecord.sellingPrice = slR.SalesDetails.get(e).sellingPrice;
                                        salesInvoiceInventoryRecord.salesOrderID = String.valueOf(slR.getId());
                                        salesInvoiceInventoryRecords.add(salesInvoiceInventoryRecord);
                                    }

                                    boolean postInvResponse = myPosBase.saveInvoiceInventory(salesInvoiceInventoryRecords);

                                    progress.dismiss();
                                    notifyDataSetChanged();
                                    Fragment fragment = null;
                                    fragment = new SalesInvoiceHistoryFragment();
                                    ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.mainFrame, fragment)
                                            .commit();

                                    if (postResponse) {

                                    }

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });
            if (ISalesInvoiceRecord.get(position).salesOrderStatus == SalesOrderStatus.ORIGINAL) {
                if (userRecord.invCancelAgeDays != null) {

                    if (!ISalesInvoiceRecord.get(position).invoiceCancelAgeDateTime.after(new Date())) {
                        buttonCancel.setClickable(true);
                    }
                }
                holder.display_status.setText(ISalesInvoiceRecord.get(position).salesOrderStatus.toString());
                buttonCancel.setVisibility(View.VISIBLE);
            } else {
                holder.display_status.setText(ISalesInvoiceRecord.get(position).salesOrderStatus.toString());
                buttonCancel.setVisibility(View.INVISIBLE);
            }
            mobileNo = (TextView) vi.findViewById(R.id.phone);
            mobileNo.setOnClickListener(new View.OnClickListener() {


                public void onClick(final View v) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Contact Customer");
                    builder.setNegativeButton("Call",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (ISalesInvoiceRecord.get(position).customerId != null) {
                                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + ISalesInvoiceRecord.get(position).phone));
                                        v.getContext().startActivity(intent);
                                    }
                                }
                            });

                    builder.setPositiveButton("SMS",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (ISalesInvoiceRecord.get(position).customerId != null) {
                                        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                                        smsIntent.setType("vnd.android-dir/mms-sms");
                                        smsIntent.putExtra("address", ISalesInvoiceRecord.get(position).phone);

                                        v.getContext().startActivity(smsIntent);
                                    }
                                }
                            });
                    builder.setNeutralButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    //dialog.cancel();
                                }
                            });
                    builder.create().show();


                }
            });


        } catch (Exception e) {


        }
        return vi;
    }


}
