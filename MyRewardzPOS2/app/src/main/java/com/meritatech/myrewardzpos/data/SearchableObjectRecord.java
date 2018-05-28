package com.meritatech.myrewardzpos.data;

import com.meritatech.myrewardzpos.model.DatabaseObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Waithera on 12/12/2017.
 */

public class SearchableObjectRecord extends SearchableObjectModel{

    public static ArrayList<InventoryRecord> findAll() {
        ArrayList<InventoryRecord> result = new ArrayList<>();
        for (Iterator<InventoryRecord> it = DatabaseObject.findAll(InventoryRecord.class);
             it.hasNext(); ) {
            result.add(it.next());
        }
        return result;
    }

    static boolean isStringMatch(String field, String search) {
        return field != null && field.toLowerCase().contains(search.toLowerCase());
    }

    public boolean isMatch(String search) {
        return isStringMatch(this.description, search)
                || isStringMatch(this.sellingPrice, search);

    }

}
