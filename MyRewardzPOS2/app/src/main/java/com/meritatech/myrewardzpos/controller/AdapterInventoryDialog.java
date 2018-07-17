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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meritatech.myrewardzpos.R;
import com.meritatech.myrewardzpos.utility.Utilities;
import com.meritatech.myrewardzpos.global.VariableData;

import java.text.NumberFormat;
import java.util.ArrayList;

import static com.meritatech.myrewardzpos.controller.GlobalVariables.getCurrentLocale;

/**
 * Created by user on 11/27/2017.
 */

public class AdapterInventoryDialog extends ArrayAdapter<Inventory> implements Filterable {
    private Activity activity;
    public ArrayList<Inventory> IInventory;
    private ArrayList<Inventory> inventoryArrayList;
    private static LayoutInflater inflater = null;

    public AdapterInventoryDialog(Activity activity, int textViewResourceId, ArrayList<Inventory> _IInventory) {
        super(activity, textViewResourceId, _IInventory);
        try {
            this.activity = activity;
            this.IInventory = _IInventory;
            inventoryArrayList = _IInventory;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {
            Utilities.LogException(e);
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
        public TextView display_sellingPrice;
        public TextView display_amount;
        public TextView display_item;
        public LinearLayout GasInputLayout;
        public LinearLayout InvInputLayout;
        public TextView itemQty;
    }
    String contactId =  null;
    ArrayList<String> list = new ArrayList<String>();

    public View getView(final int position, final View convertView, ViewGroup parent) {
        View vi = convertView;
        final AdapterInventoryDialog.ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.inventorylistlayout, null);
                holder = new AdapterInventoryDialog.ViewHolder();

                holder.display_description = (TextView) vi.findViewById(R.id.inventoryDescription);
                holder.display_sellingPrice = (TextView) vi.findViewById(R.id.sellingPrice);
                holder.display_amount = (TextView) vi.findViewById(R.id.amount);
                holder.display_item = (TextView) vi.findViewById(R.id.itemtxt);
                holder.GasInputLayout = vi.findViewById(R.id.GasInputLayout);
                holder.InvInputLayout = vi.findViewById(R.id.InvInputLayout);
                holder.itemQty =  vi.findViewById(R.id.input_price);
                 contactId = holder.itemQty.getText().toString();
                vi.setTag(holder);
            } else {
                holder = (AdapterInventoryDialog.ViewHolder) vi.getTag();
            }

            if (GlobalVariables.quantityType.equals("I") || GlobalVariables.quantityType == null) {
                holder.GasInputLayout.setVisibility(View.GONE);
                ImageView decreaseBtn = (ImageView) vi.findViewById(R.id.decrease);
                ImageView increaseBtn = (ImageView) vi.findViewById(R.id.increase);

                decreaseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //do something
                        Inventory i = IInventory.get(position);
                        double amount = i.TotalItems;
                        if (i.TotalItems > 0) {
                            amount = amount - 1;
                            i.TotalItems = amount;

                            i.TotalItems = amount;
                            i.qty = String.valueOf(amount);
                            i.priceSold = i.sellingPrice;
                            i.points = i.sellingPrice;

                            holder.display_amount.setText("" + amount);
                            VariableData.inventoryVariable = IInventory;
                        } else {
                            return;
                        }
                        notifyDataSetChanged();

                    }
                });
                increaseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = "test";

                        list.add(text);
                        Inventory i = IInventory.get(position);
                        double amount = i.TotalItems;
                        amount = amount + 1;

                        //
                        i.TotalItems = amount;
                        i.qty = String.valueOf(amount);
                        i.priceSold = i.sellingPrice;
                        i.points = String.valueOf(Integer.parseInt(i.sellingPrice) * GlobalVariables.pointsPerDollar);

                        holder.display_amount.setText("" + amount);
                        notifyDataSetChanged();
                        VariableData.inventoryVariable = IInventory;
                        Log.i("mytag", VariableData.inventoryVariable.toString());
                        //  notifyDataSetChanged();
                    }
                });
            } else if (GlobalVariables.quantityType.equals("D")) {


                holder.InvInputLayout.setVisibility(View.GONE);
                holder.GasInputLayout.setVisibility(View.VISIBLE);

                ImageView saveqntyBtn = (ImageView) vi.findViewById(R.id.svinv);
                ImageView editqntyBtn = (ImageView) vi.findViewById(R.id.edit);

                holder.itemQty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        holder.itemQty.setEnabled(true);
                    }
                });
                editqntyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.itemQty.setEnabled(true);
                    }
                });
                saveqntyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            //do something
                            Inventory i = IInventory.get(position);
                            double amount = i.TotalItems;

                            String quantity = holder.itemQty.getText().toString();
                            holder.itemQty.setEnabled(false);
                            if (quantity == null || quantity == "") {
                                quantity = "0";
                            }

                            i.TotalItems = Double.parseDouble(quantity);
                            i.qty = String.valueOf(quantity);
                            i.priceSold = i.sellingPrice;
                            i.points = i.sellingPrice;
                            holder.itemQty.setText("" + quantity);
                            notifyDataSetChanged();
                            VariableData.inventoryVariable = IInventory;
                        }
                        catch (Exception ex)
                        {
                            Utilities.LogException(ex);
                        }
                    }
                });
                if(IInventory.get(position).TotalItems == 0.0) {
                    holder.itemQty.setText(" ");
                }
                else {
                    holder.itemQty.setText("" + IInventory.get(position).TotalItems);
                }
            }


            holder.display_description.setText(IInventory.get(position).description);
            NumberFormat defaultFormat = NumberFormat.getCurrencyInstance(getCurrentLocale(getContext()));
            holder.display_sellingPrice.setText(defaultFormat.format(Double.parseDouble(IInventory.get(position).sellingPrice)));
            if(IInventory.get(position).TotalItems == 0.0) {
                holder.display_amount.setText("" + IInventory.get(position).TotalItems);
            }
            else
            {
                holder.display_amount.setText("" + IInventory.get(position).TotalItems);
            }
            String fullText = IInventory.get(position).classId + "-" + IInventory.get(position).sku;
            holder.display_item.setText(IInventory.get(position).itemNum);
            //   holder.display_class.setText(IInventory.get(position).classId);
        } catch (Exception e) {

            Utilities.LogException(e);
        }
        return vi;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Inventory> results = new ArrayList<Inventory>();
                if (constraint.length() == 0) {
                    IInventory = inventoryArrayList;
                }
                if (IInventory == null) {
                    IInventory = inventoryArrayList;
                }

                if (constraint != null) {
                    if (IInventory != null && IInventory.size() > 0) {
                        for (final Inventory g : IInventory) {
                            if (g.getdescription().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            public void notifyDataSetChanged() {
                AdapterInventoryDialog.super.notifyDataSetChanged();
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                IInventory = (ArrayList<Inventory>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public String wordStripper(String word) {
        if (word.length() == 4) {
            return word;
        } else if (word.length() > 4) {
            return word.substring(word.length() - 4);
        }
        return word;
    }
}
