package com.meritatech.myrewardzpos.controller;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.meritatech.myrewardzpos.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.meritatech.myrewardzpos.controller.GlobalVariables.getCurrentLocale;

/**
 * Created by user on 11/27/2017.
 */

public class AdapterInventory extends ArrayAdapter<Inventory> {
    private Activity activity;
    public ArrayList<Inventory> IInventory;
    private static LayoutInflater inflater = null;

    public AdapterInventory(Activity activity, int textViewResourceId, ArrayList<Inventory> _IInventory) {
        super(activity, textViewResourceId, _IInventory);
        try {
            this.activity = activity;
            this.IInventory = _IInventory;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return IInventory.size();
    }

    public Inventory getItem(Inventory position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView display_description;
        public TextView display_sku;
        public TextView display_sellingPrice;
        public TextView display_class1;
        public TextView display_totalitems;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final AdapterInventory.ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate( R.layout.inventorymainlayout, null);
                holder = new AdapterInventory.ViewHolder();

                holder.display_description = (TextView) vi.findViewById(R.id.inventoryDescription);
                holder.display_sellingPrice = (TextView) vi.findViewById(R.id.sellingPrice);
                holder.display_totalitems = (TextView) vi.findViewById(R.id.itemnumber);
                vi.setTag(holder);
            } else {
                holder = (AdapterInventory.ViewHolder) vi.getTag();
            }

            holder.display_description.setText(IInventory.get(position).getdescription());
            holder.display_description.setText(IInventory.get(position).description);
            NumberFormat defaultFormat = NumberFormat.getCurrencyInstance(getCurrentLocale(getContext()));
            holder.display_sellingPrice.setText(defaultFormat.format(Double.parseDouble(IInventory.get(position).sellingPrice)));
            holder.display_totalitems.setText(IInventory.get(position).itemNum);
        } catch (Exception e) {
            e.getMessage();
        }
        return vi;
    }

}
