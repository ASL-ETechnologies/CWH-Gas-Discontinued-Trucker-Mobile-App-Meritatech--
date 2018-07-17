package com.meritatech.myrewardzpos.controller;

import android.app.Activity;
import android.content.Context;
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

public class AdapterCustomerDialog extends ArrayAdapter<CustomerRecord>
        implements Filterable {
    private Activity activity;
    public ArrayList<CustomerRecord> ICustomer;
    public ArrayList<CustomerRecord> customerArrayList;

    private static LayoutInflater inflater = null;

    public AdapterCustomerDialog(Activity activity, int textViewResourceId, ArrayList<CustomerRecord> _ICustomer) {
        super(activity, textViewResourceId, _ICustomer);
        try {
            this.activity = activity;
            this.ICustomer = _ICustomer;
            customerArrayList = _ICustomer;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Customer> results = new ArrayList<Customer>();
                if(constraint.length() == 0)
                {
                    ICustomer = customerArrayList;
                }
                if (ICustomer == null)
                {
                    ICustomer = customerArrayList;
                }

                if (constraint != null) {
                    if (ICustomer != null && ICustomer.size() > 0) {
                        for (final CustomerRecord g : ICustomer) {
//                            String y = g.getName();
//                            if (g.getName().toLowerCase()
//                                    .contains(constraint.toString()))
//                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            public void notifyDataSetChanged() {
                AdapterCustomerDialog.super.notifyDataSetChanged();
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                ICustomer = (ArrayList<CustomerRecord>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public int getCount() {
        return ICustomer.size();
    }

    public Customer getItem(Customer position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView display_fname;
        public TextView display_lname;
        public TextView display_phone;
        public TextView display_address;

        public TextView display_id;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final AdapterCustomerDialog.ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.customerlistlayout, null);
                holder = new AdapterCustomerDialog.ViewHolder();

                holder.display_fname = (TextView) vi.findViewById(R.id.cfname);
                holder.display_lname = (TextView) vi.findViewById(R.id.clname);
               holder.display_phone = (TextView) vi.findViewById(R.id.ctel);
                holder.display_id = (TextView) vi.findViewById(R.id.cusIDtxt);
                holder.display_address = (TextView) vi.findViewById(R.id.addressTxt);

                vi.setTag(holder);
            } else {
                holder = (AdapterCustomerDialog.ViewHolder) vi.getTag();
            }

            holder.display_fname.setText(ICustomer.get(position).fName);
            holder.display_lname.setText(ICustomer.get(position).lName);
            holder.display_phone.setText(ICustomer.get(position).phone1);
            holder.display_id.setText(ICustomer.get(position).customerId);
            holder.display_address.setText(ICustomer.get(position).address);

        } catch (Exception e) {


        }
        return vi;
    }
}
