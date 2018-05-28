package com.meritatech.myrewardzpos.view;

import com.meritatech.myrewardzpos.data.CustomerRecord;
import com.meritatech.myrewardzpos.data.CustomerRecordListAPI;

import java.util.ArrayList;

/**
 * Created by Dennis Njagi on 1/26/2018.
 */

public interface CustomerView {
    void updateUi(ArrayList<CustomerRecord> customers);
}

