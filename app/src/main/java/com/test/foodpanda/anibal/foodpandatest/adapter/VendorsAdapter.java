package com.test.foodpanda.anibal.foodpandatest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.foodpanda.anibal.foodpandatest.R;
import com.test.foodpanda.anibal.foodpandatest.utils.Utility;
import com.test.foodpanda.anibal.foodpandatest.model.Vendor;

import java.util.List;

/**
 * Created by Anibal on 13/5/16.
 */
public class VendorsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;

    private final int VIEW_PROG = 0;

    private List<Vendor> mListVendors;

    public boolean mLoaderActive = true;

    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mName;

        public ImageView mAvatar;

        public TextView mMinOrders;

        public ViewHolder(View v) {
            super(v);
            mAvatar = (ImageView) v.findViewById(R.id.logo);
            mName = (TextView) v.findViewById(R.id.name);
            mMinOrders = (TextView) v.findViewById(R.id.min_orders);
        }
    }

    public static class LoaderViewHolder extends RecyclerView.ViewHolder {

        public View mLoader;

        public LoaderViewHolder(View v) {
            super(v);
            mLoader = v.findViewById(R.id.loader);
        }
    }

    public VendorsAdapter(Context context, List<Vendor> listVendors) {
        mContext = context;
        mListVendors = listVendors;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v;
        switch (viewType) {
            case VIEW_ITEM:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_row, parent, false);
                vh = new ViewHolder(v);
                break;
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.loader_row, parent, false);
                vh = new LoaderViewHolder(v);
                break;
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ViewHolder) {

            final Vendor vendor = mListVendors.get(position);

            int size = (int) Utility.convertDpToPixel(150, mContext);
            vendor.logo = vendor.logo.replaceAll("%s", String.valueOf(size));

            Glide.with(mContext)
                    .load(vendor.logo)
                    .centerCrop()
                    .crossFade()
                    .into(((ViewHolder) holder).mAvatar);

            ((ViewHolder) holder).mName.setText(vendor.name);

            ((ViewHolder) holder).mMinOrders
                    .setText(String.valueOf(vendor.minimum_order_amount)
                            + " " + mContext.getString(R.string.min_orders));
        }
    }

    @Override
    public int getItemCount() {
        if(mLoaderActive) {
            return 1;
        }
        return mListVendors.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mLoaderActive) {
            return VIEW_PROG;
        }
        return VIEW_ITEM;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setLoaderActive(boolean loaderActive) {
        this.mLoaderActive = loaderActive;
    }
}
