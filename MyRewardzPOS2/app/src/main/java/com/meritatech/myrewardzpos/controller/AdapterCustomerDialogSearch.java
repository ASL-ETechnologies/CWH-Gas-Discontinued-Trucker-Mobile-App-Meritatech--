package com.meritatech.myrewardzpos.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.meritatech.myrewardzpos.R;
import com.meritatech.myrewardzpos.data.CustomerRecord;
import com.meritatech.myrewardzpos.global.VariableData;

import java.util.ArrayList;

/**
 * Created by user on 11/27/2017.
 */

public class AdapterCustomerDialogSearch extends ArrayAdapter<CustomerRecord> {
    private Activity activity;
    public ArrayList<CustomerRecord> ICustomer;
    public ArrayList<CustomerRecord> customerArrayList;

    private static LayoutInflater inflater = null;

    public AdapterCustomerDialogSearch(Activity activity, int textViewResourceId, ArrayList<CustomerRecord> _ICustomer) {
        super(activity, textViewResourceId, _ICustomer);
        try {
            this.activity = activity;
            this.ICustomer = _ICustomer;
            customerArrayList = _ICustomer;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }


    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView display_fname;
        public TextView display_lname;
        public TextView display_phone;
        public TextView display_add;
        public TextView display_id;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final AdapterCustomerDialogSearch.ViewHolder holder;
        TextView mobileNo;

        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.customerlistlayout, null);

                holder = new AdapterCustomerDialogSearch.ViewHolder();

                holder.display_fname = (TextView) vi.findViewById(R.id.cfname);
                holder.display_lname = (TextView) vi.findViewById(R.id.clname);
                holder.display_phone = (TextView) vi.findViewById(R.id.ctel);
                holder.display_id = (TextView) vi.findViewById(R.id.cusIDtxt);
                holder.display_add = (TextView) vi.findViewById(R.id.addressTxt);
                vi.setTag(holder);
            } else {
                holder = (AdapterCustomerDialogSearch.ViewHolder) vi.getTag();
            }

            holder.display_fname.setText(ICustomer.get(position).fName);
            holder.display_lname.setText(ICustomer.get(position).lName);
            holder.display_phone.setText(ICustomer.get(position).phone1);
            holder.display_id.setText(ICustomer.get(position).customerId);
            holder.display_add.setText(ICustomer.get(position).address);

        } catch (Exception e) {


        }
        return vi;
    }
}
