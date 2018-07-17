package com.meritatech.myrewardzpos.controller;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;

import com.meritatech.myrewardzpos.data.CustomerRecord;
import com.meritatech.myrewardzpos.data.MyPosBase;
import com.meritatech.myrewardzpos.global.CustomerVariableData;
import com.meritatech.myrewardzpos.global.SalesOrderVariableData;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import static com.meritatech.myrewardzpos.controller.GlobalVariables.getCurrentLocale;

/**
 * Created by user on 12/14/2017.
 */

public class ReceiptPrintDocumentAdapter extends PrintDocumentAdapter {

    Context context;
    private int pageHeight;
    private int pageWidth;
    public PdfDocument myPdfDocument;
    public int totalpages = 1;

    public ReceiptPrintDocumentAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes,
                         PrintAttributes newAttributes,
                         CancellationSignal cancellationSignal,
                         LayoutResultCallback callback,
                         Bundle metadata) {

        myPdfDocument = new PrintedPdfDocument(context, newAttributes);

        pageHeight =
                newAttributes.getMediaSize().getHeightMils() / 1000 * 72;
        pageWidth =
                newAttributes.getMediaSize().getWidthMils() / 1000 * 72;

        if (cancellationSignal.isCanceled()) {
            callback.onLayoutCancelled();
            return;
        }

        if (totalpages > 0) {
            PrintDocumentInfo.Builder builder = new PrintDocumentInfo
                    .Builder("print_output.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(totalpages);

            PrintDocumentInfo info = builder.build();
            callback.onLayoutFinished(info, true);
        } else {
            callback.onLayoutFailed("Page count is zero.");
        }
    }


    @Override
    public void onWrite(final PageRange[] pageRanges,
                        final ParcelFileDescriptor destination,
                        final CancellationSignal cancellationSignal,
                        final WriteResultCallback callback) {

        for (int i = 0; i < totalpages; i++) {
            if (pageInRange(pageRanges, i)) {
                PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pageWidth,
                        pageHeight, i).create();

                PdfDocument.Page page =
                        myPdfDocument.startPage(newPage);

                if (cancellationSignal.isCanceled()) {
                    callback.onWriteCancelled();
                    myPdfDocument.close();
                    myPdfDocument = null;
                    return;
                }
                drawPage(page, i);
                myPdfDocument.finishPage(page);
            }
        }

        try {
            myPdfDocument.writeTo(new FileOutputStream(
                    destination.getFileDescriptor()));
        } catch (IOException e) {
            callback.onWriteFailed(e.toString());
            return;
        } finally {
            myPdfDocument.close();
            myPdfDocument = null;
        }

        callback.onWriteFinished(pageRanges);
    }

    private boolean pageInRange(PageRange[] pageRanges, int page) {
        for (int i = 0; i < pageRanges.length; i++) {
            if ((page >= pageRanges[i].getStart()) &&
                    (page <= pageRanges[i].getEnd()))
                return true;
        }
        return false;
    }

    private void drawPage(PdfDocument.Page page,
                          int pagenumber) {

        Canvas canvas = page.getCanvas();

        pagenumber++; // Make sure page numbers start at 1

        int titleBaseLine = 72;
        int leftMargin = 54;

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);
        canvas.drawText(
                "MyRewardzPOS Invoice",
                leftMargin,
                titleBaseLine,
                paint);
        paint.setTextSize(14);
        canvas.drawText("Name: " + CustomerVariableData.customerVariable.fName + " " + CustomerVariableData.customerVariable.lName, leftMargin + 1, titleBaseLine + 35, paint);

        paint.setTextSize(14);
        canvas.drawText("Phone#: " + CustomerVariableData.customerVariable.phone1, leftMargin + 1, titleBaseLine + 55, paint);

        paint.setTextSize(14);
        canvas.drawText("Email: " + CustomerVariableData.customerVariable.email, leftMargin + 1, titleBaseLine + 75, paint);

        paint.setTextSize(14);
        canvas.drawText("Store Name: " + GlobalVariables.StoreName, leftMargin + 1, titleBaseLine + 95, paint);


        paint.setTextSize(15);
        canvas.drawText("Description", leftMargin + 1, titleBaseLine + 110, paint);
        paint.setTextSize(15);
        canvas.drawText("Qty", leftMargin + 150, titleBaseLine + 110, paint);
        paint.setTextSize(15);
        canvas.drawText("Price", leftMargin + 180, titleBaseLine + 110, paint);
        paint.setTextSize(15);
        canvas.drawText("Points", leftMargin + 280, titleBaseLine + 110, paint);

        paint.setTextSize(15);
        canvas.drawText("Tax", leftMargin + 330, titleBaseLine + 110, paint);

        canvas.drawText("Total", leftMargin + 380, titleBaseLine + 110, paint);

        int rowNu = 110;
        double total = 0.0;
        int points = 0;
        int ttpoints = 0;
        double ttTax = 0.0;
        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance(getCurrentLocale(context));
        int skip = 0;
        if (SalesOrderVariableData.salesOrderVariable != null) {
            for (int i = 0; i < SalesOrderVariableData.salesOrderVariable.size(); i++) {
                double taxAmount = 0.0;
                rowNu = rowNu + 20;
                skip = (i + 1) * 20;
                skip = skip + 105;
                paint.setTextSize(14);
                canvas.drawText(SalesOrderVariableData.salesOrderVariable.get(i).description, leftMargin + 1, titleBaseLine + skip, paint);

                paint.setTextSize(14);
                canvas.drawText(SalesOrderVariableData.salesOrderVariable.get(i).qty, leftMargin + 150, titleBaseLine + skip, paint);

                paint.setTextSize(14);
                canvas.drawText(defaultFormat.format(Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).priceSold)), leftMargin + 180, titleBaseLine + skip, paint);

                if (SalesOrderVariableData.salesOrderVariable.get(i).sellingPrice == null) {
                    SalesOrderVariableData.salesOrderVariable.get(i).sellingPrice = "0";
                }
                if (SalesOrderVariableData.salesOrderVariable.get(i).qty == null) {
                    SalesOrderVariableData.salesOrderVariable.get(i).qty = "0";
                }
                double ttprice = Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).sellingPrice) * Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).qty);
                double totalperItem = Math.ceil(ttprice / (Math.pow(10, Integer.parseInt(GlobalVariables.roundSalesUpFactor))) * Math.pow(10, Integer.parseInt(GlobalVariables.roundSalesUpFactor)));

                total = total + totalperItem;

                if (GlobalVariables.IsSalesOrder) {
                    points = Integer.parseInt(SalesOrderVariableData.salesOrderVariable.get(i).points);
                } else {

                    double ttprice1 = Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).priceSold) * Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).qty);
                    double extendedPrice = Math.ceil(ttprice1 / (Math.pow(10, Integer.parseInt(GlobalVariables.roundSalesUpFactor))) * Math.pow(10, Integer.parseInt(GlobalVariables.roundSalesUpFactor)));
                    if (GlobalVariables.taxIncluded.equals("1")) {

                        if (SalesOrderVariableData.salesOrderVariable != null && SalesOrderVariableData.salesOrderVariable.size() > 0) {
                            if (SalesOrderVariableData.salesOrderVariable.get(i).taxPercentage == null) {
                                SalesOrderVariableData.salesOrderVariable.get(i).taxPercentage = "0";
                            }
                            if (SalesOrderVariableData.salesOrderVariable.get(i).priceSold == null) {
                                SalesOrderVariableData.salesOrderVariable.get(i).priceSold = "0";
                            }

                            taxAmount = Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).taxPercentage) * (Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).priceSold) / (1 + Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).taxPercentage)));
                            taxAmount = taxAmount * Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).qty);
                            DecimalFormat twoDForm = new DecimalFormat("#.##");
                            taxAmount = Double.valueOf(twoDForm.format(taxAmount));
                            ttTax = ttTax + taxAmount;
                            double unitPrice = Math.ceil(Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).priceSold) - (taxAmount / Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).qty)));
                            double doublepoints = 0.0;
                            String cval = CustomerVariableData.customerVariable.customerId;
                            if (cval == null) {
                                cval = "0";
                            }
                            int cusID = Integer.parseInt(cval);
                            if (cusID == 0 || cusID > Integer.parseInt(GlobalVariables.highCustomerLimit)) {
                                doublepoints = 0.0;
                            } else {
                                doublepoints = Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).qty) * Double.parseDouble(String.valueOf(GlobalVariables.pointsPerDollar)) * unitPrice;

                            }

                            double pp = (int) Math.ceil(doublepoints);
                            int thispoints = (int) pp;
                            points = (int) thispoints;
                        }
                    } else if (GlobalVariables.taxIncluded.equals("0")) {
                        if (Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).taxPercentage) > 0) {
                            ttTax = ttTax + (Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).qty) * (Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).taxPercentage) * Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).priceSold)));
                        }
                        double cl = extendedPrice * GlobalVariables.pointsPerDollar;
                        points = (int) cl;
                    }
                }
                ttpoints = ttpoints + points;
                paint.setTextSize(14);
                canvas.drawText(String.valueOf(points), leftMargin + 280, titleBaseLine + skip, paint);

                if(SalesOrderVariableData.salesOrderVariable.get(i).taxPercentage == null)
                {
                    SalesOrderVariableData.salesOrderVariable.get(i).taxPercentage = "0";
                }

                if (Double.parseDouble(SalesOrderVariableData.salesOrderVariable.get(i).taxPercentage) > 0.00) {
                    paint.setTextSize(14);
                    canvas.drawText("T", leftMargin + 335, titleBaseLine + skip, paint);
                }

                paint.setTextSize(14);
                canvas.drawText(defaultFormat.format(totalperItem), leftMargin + 380, titleBaseLine + skip, paint);
            }
        }

        canvas.drawLine(leftMargin, rowNu + titleBaseLine + 20, canvas.getWidth(), rowNu + titleBaseLine + 20, paint);

        if (GlobalVariables.taxIncluded.equals("1")) {
            paint.setTextSize(15);
            canvas.drawText("Total(Inc." + GlobalVariables.salesTaxAbbreviation + "):", leftMargin + 280, rowNu + titleBaseLine + 40, paint);

            paint.setTextSize(15);
            canvas.drawText(defaultFormat.format(total), leftMargin + 380, rowNu + titleBaseLine + 40, paint);

            paint.setTextSize(15);
            canvas.drawText(GlobalVariables.salesTaxAbbreviation, leftMargin + 280, rowNu + titleBaseLine + 60, paint);

            paint.setTextSize(15);
            canvas.drawText(defaultFormat.format(ttTax), leftMargin + 380, rowNu + titleBaseLine + 60, paint);


        } else if (GlobalVariables.taxIncluded.equals("0")) {
            if (ttTax > 0) {
                paint.setTextSize(15);
                canvas.drawText("Sub Total:", leftMargin + 280, rowNu + titleBaseLine + 40, paint);

                paint.setTextSize(15);
                canvas.drawText(defaultFormat.format(total), leftMargin + 380, rowNu + titleBaseLine + 40, paint);

                paint.setTextSize(15);
                canvas.drawText(GlobalVariables.salesTaxAbbreviation, leftMargin + 280, rowNu + titleBaseLine + 60, paint);

                paint.setTextSize(15);
                canvas.drawText(defaultFormat.format(ttTax), leftMargin + 380, rowNu + titleBaseLine + 60, paint);

                paint.setTextSize(15);
                canvas.drawText("Total:", leftMargin + 280, rowNu + titleBaseLine + 80, paint);

                paint.setTextSize(15);
                canvas.drawText(defaultFormat.format(total + ttTax), leftMargin + 380, rowNu + titleBaseLine + 80, paint);
            } else {
                paint.setTextSize(15);
                canvas.drawText("Total:", leftMargin + 280, rowNu + titleBaseLine + 40, paint);

                paint.setTextSize(15);
                canvas.drawText(defaultFormat.format(total), leftMargin + 380, rowNu + titleBaseLine + 40, paint);
            }
        }


        paint.setTextSize(15);
        canvas.drawText("New Points: " + CustomerVariableData.customerVariable.newpoints, leftMargin + 1, rowNu + titleBaseLine + 100, paint);

        String beforeTrans = "Before Transaction: " + (CustomerVariableData.customerVariable.lastpoints );
        paint.setTextSize(15);
        canvas.drawText(beforeTrans, leftMargin + 1, rowNu + titleBaseLine + 120, paint);

        if (CustomerVariableData.customerVariable.points == null) {
            CustomerVariableData.customerVariable.points = "0";
        }
        int totalPoints = Integer.parseInt(CustomerVariableData.customerVariable.points) + ttpoints;
        String afterTrans = "After Transaction: " + (CustomerVariableData.customerVariable.lastpoints + CustomerVariableData.customerVariable.newpoints);
        paint.setTextSize(15);
        canvas.drawText(afterTrans, leftMargin + 1, rowNu + titleBaseLine + 140, paint);

        paint.setTextSize(9);
        canvas.drawText(GlobalVariables.invoiceFooterMessage, leftMargin + 1, rowNu + titleBaseLine + 180, paint);


        if (pagenumber % 2 == 0)
            paint.setColor(Color.RED);
        else
            paint.setColor(Color.GREEN);

        PdfDocument.PageInfo pageInfo = page.getInfo();


        /*canvas.drawCircle(pageInfo.getPageWidth()/2,
                pageInfo.getPageHeight()/2,
                150,
                paint);*/
    }

}
