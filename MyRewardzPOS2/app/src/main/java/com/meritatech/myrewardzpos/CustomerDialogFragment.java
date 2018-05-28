package com.meritatech.myrewardzpos;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.meritatech.myrewardzpos.controller.AdapterCustomerDialogSearch;
import com.meritatech.myrewardzpos.controller.EndlessScrollListener;
import com.meritatech.myrewardzpos.controller.GlobalVariables;
import com.meritatech.myrewardzpos.data.CustomerRecord;
import com.meritatech.myrewardzpos.data.MyPosBase;
import com.meritatech.myrewardzpos.global.CustomerActivityChecker;
import com.meritatech.myrewardzpos.global.CustomerDetailsVariableData;
import com.meritatech.myrewardzpos.interfaces.InventoryInterface;
import com.meritatech.myrewardzpos.model.CustomerDataModel;
import com.meritatech.myrewardzpos.global.CustomerVariableData;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class CustomerDialogFragment extends android.support.v4.app.DialogFragment
        implements View.OnClickListener,
        SearchView.OnQueryTextListener {
    private ListView mListView;
    GlobalVariables globalVars = new GlobalVariables();
    private Button mSearchView;
    private ImageView hideLayout;
    private ImageView showLayout;
    private LinearLayout filterLayout;
    ArrayList<CustomerRecord> data;
    String emailtxt, phonetxt, customername, customerid = "";
    String API_BASE_URL = globalVars.url;
    MyPosBase myPosBase = new MyPosBase();
    final AdapterCustomerDialogSearch[] adbCustomer = new AdapterCustomerDialogSearch[1];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_contacts, container,
                false);
        hideLayout = (ImageView) view.findViewById(R.id.hideLayout);
        showLayout = (ImageView) view.findViewById(R.id.showLayout);
        mSearchView = (Button) view.findViewById(R.id.searchContacts);
        filterLayout = (LinearLayout) view.findViewById(R.id.filterLayout);
        mListView = (ListView) view.findViewById(R.id.list_view_contacts);
        mListView.addFooterView(new ProgressBar(getContext()));


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        final Retrofit retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .build();

        hideLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (filterLayout.getVisibility() == View.VISIBLE) {
                    filterLayout.setVisibility(View.GONE);
                    showLayout.setVisibility(View.VISIBLE);
                    hideLayout.setVisibility(View.INVISIBLE);

                }
            }
        });

        showLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (filterLayout.getVisibility() == View.GONE) {
                    filterLayout.setVisibility(View.VISIBLE);
                    showLayout.setVisibility(View.INVISIBLE);
                    hideLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        mSearchView.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               //  mListView.removeFooterView(v);
                                               //  mListView.addFooterView(new ProgressBar(getContext()));
                                               InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                                               imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


                                               InventoryInterface client = retrofit.create(InventoryInterface.class);
                                               TextView searchtxt = (TextView) view.findViewById(R.id.searchtext);
                                               String nameValue = searchtxt.getText().toString();
                                               TextView contactIDtxt = (TextView) view.findViewById(R.id.clientidtext);
                                               String contactValue = contactIDtxt.getText().toString();
                                               TextView emailt = (TextView) view.findViewById(R.id.emailtxt);
                                               String emailValue = emailt.getText().toString();
                                               TextView mobilenotext = (TextView) view.findViewById(R.id.mobilenotext);
                                               String mobilenotextValue = mobilenotext.getText().toString();
                                               final TextView totaltxt = (TextView) view.findViewById(R.id.totalRecords);
                                               emailtxt = emailValue;
                                               phonetxt = mobilenotextValue;
                                               customername = nameValue;
                                               customerid = contactValue;
                                               if (contactIDtxt.getText().length() > 0 || nameValue.length() > 0 || mobilenotextValue.length() > 0 || emailValue.length() > 0) {
                                                   final ProgressDialog progress = new ProgressDialog(getContext());
                                                   progress.setTitle("Loading");
                                                   progress.setMessage("loading co...");
                                                   progress.setCancelable(false);

                                                   if (GlobalVariables.isConnected(getContext())) {

                                                       final Call<CustomerDataModel> call = client.findcustomer("1", customerid, emailValue, mobilenotextValue, "", nameValue, null, 20, globalVars.token);
                                                       // disable dismiss by tapping outside of the dialog
                                                       progress.show();
                                                       call.clone().enqueue(new Callback<CustomerDataModel>() {
                                                           @Override
                                                           public void onResponse(Call<CustomerDataModel> call, Response<CustomerDataModel> response) {
                                                               try {
                                                                   progress.hide();

                                                                   totaltxt.setVisibility(View.VISIBLE);
                                                                   if (response.body().dataObj.totRecMatch < 200) {
                                                                       totaltxt.setText(response.body().dataObj.totRecMatch + " Records Found!");
                                                                   } else {
                                                                       totaltxt.setText(response.body().dataObj.totRecMatch + " Records Found! REFINE SEARCH!");
                                                                   }


                                                                   Response<CustomerDataModel> reesponse = response;


                                                                   //populate myListItems
                                                                   data = new ArrayList<CustomerRecord>();
                                                                   data = response.body().dataObj.Data;
                                                                   adbCustomer[0] = new AdapterCustomerDialogSearch(getActivity(), R.layout.customerlistlayout, response.body().dataObj.Data);
                                                                   mListView.setAdapter(adbCustomer[0]);
                                                               } catch (Exception ex) {
                                                                   Utilities.LogError("Error loading customer");
                                                               }
                                                           }


                                                           @Override
                                                           public void onFailure(Call<CustomerDataModel> call, Throwable t) {
                                                               progress.hide();
                                                           }
                                                       });
                                                   } else {
                                                       ArrayList<CustomerRecord> selcustomerList = new ArrayList<CustomerRecord>();
                                                       if (customerid.length() > 0) {
                                                           CustomerRecord selcustomer = myPosBase.GetCustomerRecord(customerid);
                                                           if (selcustomer != null) {
                                                               selcustomerList.add(selcustomer);
                                                           }
                                                       } else {
                                                           String searchstr = "";
                                                           if (emailValue.length() > 0 || mobilenotextValue.length() > 0 || nameValue.length() > 0) {

                                                               ArrayList<CustomerRecord> selcustomerL = myPosBase.SearchCustomer(nameValue.trim(), emailValue, mobilenotextValue);
                                                               if (selcustomerL != null && selcustomerL.size() > 0) {
                                                                   selcustomerList = selcustomerL;
                                                               }
                                                           } else {
                                                               int duration = Toast.LENGTH_LONG;
                                                               Toast toast = Toast.makeText(getContext(), "Enter Search Criteria!", duration);
                                                               toast.show();
                                                           }
                                                       }


                                                       progress.hide();

                                                       totaltxt.setVisibility(View.VISIBLE);
                                                       if (selcustomerList != null && selcustomerList.size() > 0)
                                                           if (selcustomerList.size() < 200) {
                                                               totaltxt.setText(selcustomerList.size() + " Records Found!");
                                                           } else {
                                                               totaltxt.setText(selcustomerList.size() + " Records Found! REFINE SEARCH!");
                                                           }
                                                       else if (selcustomerList.size() == 0) {
                                                           totaltxt.setText(0 + " Records Found!");
                                                       }
                                                       adbCustomer[0] = new AdapterCustomerDialogSearch(getActivity(), R.layout.customerlistlayout, selcustomerList);

                                                   }

                                                   //close dialog btn
                                                   ImageView viewbtn = (ImageView) view.findViewById(R.id.imageView_close);
                                                   viewbtn.setOnClickListener(new View.OnClickListener() {

                                                       @Override
                                                       public void onClick(View arg0) {

                                                           onDestroyView();
                                                       }
                                                   });
                                                   mListView.removeFooterView(view);
                                                   mListView.setAdapter(adbCustomer[0]);
                                                   mListView.setTextFilterEnabled(true);

                                                   //on row click
                                                   mListView.setClickable(true);
                                                   mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                                       @Override
                                                       public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                                                           CustomerRecord obj = adbCustomer[0].ICustomer.get(position);
                                                           if (CustomerActivityChecker.IsInvoicePage) {
                                                               CustomerVariableData.customerVariable = obj;
                                                           } else {
                                                               CustomerDetailsVariableData.customerDetails = obj;

                                                               CustomerFullDetailsFragment customerDetailsFragment = new CustomerFullDetailsFragment();
                                                               customerDetailsFragment.setTargetFragment(CustomerDialogFragment.this, 1);
                                                               customerDetailsFragment.show(getFragmentManager(), "customer");
                                                           }
                                                           onDestroyView();

                                                       }
                                                   });
                                                   progress.dismiss();
                                               } else {
                                                   int duration = Toast.LENGTH_LONG;
                                                   Toast toast = Toast.makeText(getContext(), "Enter Search Criteria!", duration);
                                                   toast.show();
                                               }

                                           }
                                       }
        );

        mListView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                loadItems(totalItemsCount);
                mListView.removeFooterView(view);
                return true;
            }
        });
        ImageView viewbtn = (ImageView) view.findViewById(R.id.imageView_close);
        viewbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                onDestroyView();
            }
        });

        return view;
    }

    final boolean[] resp = {false};

    public boolean[] loadItems(int totalItemCount) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        final Retrofit retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .build();
        InventoryInterface client = retrofit.create(InventoryInterface.class);
        final Call<CustomerDataModel> call = client.findcustomer("1", customerid, emailtxt, phonetxt, null, customername, totalItemCount, 20, globalVars.token);
        final ProgressDialog progress = new ProgressDialog(getContext());
        if (!customername.equals("") && totalItemCount > 0) {
            call.clone().enqueue(new Callback<CustomerDataModel>() {
                @Override
                public void onResponse(Call<CustomerDataModel> call, Response<CustomerDataModel> response) {
                    try {
                        data = response.body().dataObj.Data;
                        ArrayList<CustomerRecord> newdata = response.body().dataObj.Data;
                        if (response.body().dataObj.Data != null && response.body().dataObj.Data.size() > 0) {
                            for (int f = 0; f < response.body().dataObj.Data.size(); f++) {
                                adbCustomer[0].ICustomer.add(newdata.get(f));
                            }
                        }
                        if (adbCustomer != null) {
                            adbCustomer[0].notifyDataSetChanged();
                        }
                        resp[0] = true;
                    } catch (Exception e) {
                        Utilities.LogException(e);
                    }
                }

                @Override
                public void onFailure(Call<CustomerDataModel> call, Throwable t) {
                    try {

                    } catch (Exception e) {
                        Utilities.LogException(e);
                    }
                }
            });
        }
        return resp;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            if (getTargetFragment() == null) {
                CustomerDialogFragment customerDialogFragment = new CustomerDialogFragment();
                InvoiceCreateFragment invoiceCreateFragment = new InvoiceCreateFragment();
                customerDialogFragment.setTargetFragment(invoiceCreateFragment, 1);
            }
            getTargetFragment().isResumed();
            getTargetFragment().onActivityResult(getTargetRequestCode(), 2, getActivity().getIntent());
        } catch (Exception ex) {
            Utilities.LogException(ex);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(newText);
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        setTargetFragment(null, -1);
    }
}
