package com.meritatech.myrewardzpos.controller;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.meritatech.myrewardzpos.R;
import com.meritatech.myrewardzpos.data.LogRecord;

import java.util.ArrayList;

/**
 * Created by user on 11/27/2017.
 */

public class AdapterLogs extends ArrayAdapter<LogRecord> {
    private Activity activity;
    public ArrayList<LogRecord> ILogRecord;
    private static LayoutInflater inflater = null;

    public AdapterLogs(Activity activity, int textViewResourceId, ArrayList<LogRecord> _ILogRecord) {
        super(activity, textViewResourceId, _ILogRecord);
        try {
            this.activity = activity;
            this.ILogRecord = _ILogRecord;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return ILogRecord.size();
    }

    public LogRecord getItem(LogRecord position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView display_logDate;
        public TextView display_message;


    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final AdapterLogs.ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate( R.layout.logs_partiallayout, null);
                holder = new AdapterLogs.ViewHolder();

                holder.display_logDate = (TextView) vi.findViewById(R.id.logDate);
                holder.display_message = (TextView) vi.findViewById(R.id.message);

                vi.setTag(holder);
            } else {
                holder = (AdapterLogs.ViewHolder) vi.getTag();
            }

            holder.display_logDate.setText(ILogRecord.get(position).getEventDate().toString());
            holder.display_message.setText(ILogRecord.get(position).getErrorMessage());

        } catch (Exception e) {
            e.getMessage();
        }
        return vi;
    }

}
