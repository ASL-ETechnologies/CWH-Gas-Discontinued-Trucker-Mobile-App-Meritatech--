package com.meritatech.myrewardzpos.data;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;

/**
 * Created by user on 1/2/2018.
 */

public class CustomerAPI extends AsyncQueryHandler {
    public CustomerAPI(ContentResolver cr) {
        super(cr);
    }
}
