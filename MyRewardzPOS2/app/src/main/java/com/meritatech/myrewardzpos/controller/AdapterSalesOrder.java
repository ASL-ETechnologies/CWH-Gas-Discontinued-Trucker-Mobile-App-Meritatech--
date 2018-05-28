package com.meritatech.myrewardzpos.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.meritatech.myrewardzpos.R;
import com.meritatech.myrewardzpos.data.SalesOrderRecord;
import com.meritatech.myrewardzpos.data.UserRecord;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by user on 11/24/2017.
 */


public class AdapterSalesOrder extends ArrayAdapter<SalesOrderRecord> {
    private Activity activity;
    public ArrayList<SalesOrderRecord> ISalesOrder;
    private static LayoutInflater inflater = null;

    public AdapterSalesOrder(Activity activity, int textViewResourceId, ArrayList<SalesOrderRecord> _ISalesOrder) {
        super(activity, textViewResourceId, _ISalesOrder);
        try {
            this.activity = activity;
            this.ISalesOrder = _ISalesOrder;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return ISalesOrder.size();
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
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        ArrayList<UserRecord>   userRecordOj = UserRecord.findAllRecords(UserRecord.class);
        UserRecord userRecord = new UserRecord();
        if(userRecordOj!= null && userRecordOj.size()>0)
        {
            userRecord =   userRecordOj.get(0);
        }
        TextView mobileNo;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.salesorderlistlayout, null);
                holder = new ViewHolder();

                holder.display_name = (TextView) vi.findViewById(R.id.fName);
                holder.display_address = (TextView) vi.findViewById(R.id.address);
                holder.display_number = (TextView) vi.findViewById(R.id.phone);
                holder.display_cid = (TextView) vi.findViewById(R.id.cid);
                holder.display_salesItems = (TextView) vi.findViewById(R.id.salesorderItems);
                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            StringBuilder builder = new StringBuilder();
            if (ISalesOrder.get(position).SalesDetails != null) {
                for (int i = 0; i < ISalesOrder.get(position).SalesDetails.size(); i++) {
                    builder.append(ISalesOrder.get(position).SalesDetails.get(i).qty);
                    builder.append(" x ");
                    builder.append(ISalesOrder.get(position).SalesDetails.get(i).description);
                    builder.append("; ");
                }
            }

            holder.display_name.setText(ISalesOrder.get(position).fName + " " + ISalesOrder.get(position).lName);
            holder.display_number.setText(ISalesOrder.get(position).phone);
            holder.display_address.setText(ISalesOrder.get(position).address);
            holder.display_cid.setText(ISalesOrder.get(position).customerId);
            holder.display_salesItems.setText(builder);

            if(userRecord.soAgingLimitHours != null) {
                DateFormat originalFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date2 = null;
                try {

                    date2 = originalFormat.parse(ISalesOrder.get(position).orderDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar cal = Calendar.getInstance(); // creates calendar
                cal.setTime(date2); // sets calendar time/date
                cal.add(Calendar.HOUR_OF_DAY, Integer.parseInt(userRecord.soAgingLimitHours));
                Date dateToDeliver = cal.getTime();

                if (dateToDeliver.after(new Date())) {
                    holder.display_address.setTextColor(Color.RED);
                }
            }
            mobileNo = (TextView) vi.findViewById(R.id.phone);
            mobileNo.setOnClickListener(new View.OnClickListener() {


                public void onClick(final View v) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Contact Customer");
                    builder.setNegativeButton("Call",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (ISalesOrder.get(position).customerId != null) {
                                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + ISalesOrder.get(position).phone));
                                        v.getContext().startActivity(intent);
                                    }
                                }
                            });

                    builder.setPositiveButton("SMS",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (ISalesOrder.get(position).customerId != null) {
                                        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                                        smsIntent.setType("vnd.android-dir/mms-sms");
                                        smsIntent.putExtra("address", ISalesOrder.get(position).phone);
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
