package com.test.foodpanda.anibal.foodpandatest.main;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Where;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.test.foodpanda.anibal.foodpandatest.R;
import com.test.foodpanda.anibal.foodpandatest.TestApplication;
import com.test.foodpanda.anibal.foodpandatest.adapter.VendorsAdapter;
import com.test.foodpanda.anibal.foodpandatest.dagger.module.MainActivityModule;
import com.test.foodpanda.anibal.foodpandatest.model.AppDatabase;
import com.test.foodpanda.anibal.foodpandatest.model.Vendor;
import com.test.foodpanda.anibal.foodpandatest.model.Vendor_Adapter;
import com.test.foodpanda.anibal.foodpandatest.model.Vendor_Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Subscription;
import rx.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mRefreshLayout;

    private Spinner mSpinner;

    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    private List<Vendor> mListVendors = new ArrayList<>();

    private OrderBy mOrderBy;

    private int mFirstVisibleItem;

    @Inject
    Gson mGson;

    @Inject
    PublishSubject<Map<String, Object>> mGetVendorsPublish;

    @Inject
    Subscription mGetVendorsSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((TestApplication) getApplication()).mAppComponent
                .plus(new MainActivityModule(this)).inject(this);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mRefreshLayout.setOnRefreshListener(this);

        setUpSpinner();

        setUpRecyclerView();

        if(savedInstanceState != null) {
            mFirstVisibleItem = savedInstanceState.getInt("firstVisibleItem");
        } else {
            fetchVendors();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mLayoutManager != null) {
            outState.putInt("firstVisibleItem",
                    ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition());
        }
    }

    private void setUpSpinner() {
        mSpinner = (Spinner) findViewById(R.id.spinner_nav);
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Asc Name");
        spinnerArray.add("Desc Name");
        spinnerArray.add("Asc Order");
        spinnerArray.add("Desc Order");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mOrderBy = OrderBy.fromProperty(Vendor_Table.name);
                        mOrderBy.ascending();
                        break;
                    case 1:
                        mOrderBy = OrderBy.fromProperty(Vendor_Table.name);
                        mOrderBy.descending();
                        break;
                    case 2:
                        mOrderBy = OrderBy.fromProperty(Vendor_Table.minimum_order_amount);
                        mOrderBy.ascending();
                        break;
                    case 3:
                        mOrderBy = OrderBy.fromProperty(Vendor_Table.minimum_order_amount);
                        mOrderBy.descending();
                        break;
                }
                loadVendorsDB();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setUpRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.list_vendors);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new VendorsAdapter(this, mListVendors);
        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        mListVendors.clear();
        ((VendorsAdapter) mAdapter).setLoaderActive(true);
        mAdapter.notifyDataSetChanged();
        fetchVendors();
        mRefreshLayout.setRefreshing(false);
    }

    private void fetchVendors() {
        Map<String, Object> map = new HashMap<>();
        map.put("area_id", 3122);
        map.put("language_id", 1);
        map.put("latitude", 1.33944);
        map.put("longitude", 103.8240102);

        mGetVendorsPublish.onNext(map);
    }

    public void loadVendorsDB() {
        FlowManager.getDatabase(AppDatabase.class).beginTransactionAsync(
                new QueryTransaction.Builder<>(
                        SQLite.select().from(Vendor.class)
                                //.orderBy(Vendor_Table.name, true))
                                .orderBy(mOrderBy))
                        .queryResult(new QueryTransaction.QueryResultCallback<Vendor>() {
                            @Override
                            public void onQueryResult(QueryTransaction transaction, @NonNull CursorResult<Vendor> result) {
                                List<Vendor> vendors = result.toList();
                                updateView(vendors);
                            }
                        }).build()).build().execute();
    }

    private void updateView(List<Vendor> vendors) {
        if(vendors != null && !vendors.isEmpty()) {
            mListVendors.clear();
            mListVendors.addAll(vendors);
            ((VendorsAdapter) mAdapter).setLoaderActive(false);
            mAdapter.notifyDataSetChanged();
            mLayoutManager.scrollToPosition(mFirstVisibleItem);
            mFirstVisibleItem = 0;
        }
    }

    public void onShowError() {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        loadVendorsDB();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mGetVendorsSubscription != null && !mGetVendorsSubscription.isUnsubscribed()) {
            mGetVendorsSubscription.unsubscribe();
        }
    }
}
