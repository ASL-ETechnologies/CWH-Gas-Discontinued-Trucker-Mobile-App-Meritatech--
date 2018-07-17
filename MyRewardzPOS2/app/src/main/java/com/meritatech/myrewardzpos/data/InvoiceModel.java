package com.meritatech.myrewardzpos.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 12/4/2017.
 */

public class InvoiceModel {
    @SerializedName("recCnt")
    public String recCnt;

    public String getrecCnt() {
        return recCnt;
    }

    public com.meritatech.myrewardzpos.data.SalesOrderRecord SalesOrderRecord;

    @SerializedName("mdTrxns")
    public String mdTrxns;

    public String GenerateTransactionResponse() {
        StringBuilder builder = new StringBuilder();
        builder.append(SalesOrderRecord.customerId);
        builder.append(",");
        builder.append(SalesOrderRecord.salesmanId);
        builder.append(",");
        builder.append(SalesOrderRecord.invoiceDateTime);
        builder.append(",");
        builder.append(SalesOrderRecord.invoiceNumber);
        builder.append(",");
        builder.append(SalesOrderRecord.parentStoreId);
        builder.append(",");
        builder.append(SalesOrderRecord.storeId);
        builder.append(",");
        builder.append(SalesOrderRecord.phone1);
        builder.append(",");
        builder.append(SalesOrderRecord.phone2);
        builder.append(",");
        builder.append(SalesOrderRecord.email);
        builder.append(",");
        builder.append(SalesOrderRecord.longitude);
        builder.append(",");
        builder.append(SalesOrderRecord.latitude);
        builder.append(",");
        builder.append("'");
        if (SalesOrderRecord.SalesDetails != null) {
            for (int l = 0; l < SalesOrderRecord.SalesDetails.size(); l++) {
                if (l > 0) {
                    builder.append(";");
                }
                builder.append(SalesOrderRecord.SalesDetails.get(l).classId);
                builder.append("^");
                builder.append(SalesOrderRecord.SalesDetails.get(l).sku);
                builder.append("^");
                builder.append(SalesOrderRecord.SalesDetails.get(l).points);
                builder.append("^");
                builder.append(SalesOrderRecord.SalesDetails.get(l).priceSold);
                builder.append("^");
                builder.append(SalesOrderRecord.SalesDetails.get(l).qty);
                if (l == SalesOrderRecord.SalesDetails.size())
                {
                    builder.append("'");
                }

            }
        }
        return builder.toString();
    }

    public String getmdTrxns() {

        if (mdTrxns == null) {
            mdTrxns = GenerateTransactionResponse();
        }
        return mdTrxns;
    }


}
