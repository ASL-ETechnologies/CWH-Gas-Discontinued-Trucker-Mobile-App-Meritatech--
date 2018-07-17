package com.meritatech.myrewardzpos.view;

import com.meritatech.myrewardzpos.controller.Inventory;
import com.meritatech.myrewardzpos.data.CustomerRecord;
import com.meritatech.myrewardzpos.data.InventoryRecord;

import java.util.ArrayList;

/**
 * Created by Dennis Njagi on 1/26/2018.
 */

public interface InventoryView {
    void updateUi( ArrayList<Inventory> inventory);
}

