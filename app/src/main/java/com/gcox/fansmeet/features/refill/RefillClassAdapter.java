package com.gcox.fansmeet.features.refill;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gcox.fansmeet.AppsterApplication;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.features.mvpbase.RecyclerItemCallBack;
import com.gcox.fansmeet.util.CountryCode;
import com.gcox.fansmeet.util.ImageLoaderUtil;

import java.util.List;

public class RefillClassAdapter extends BaseAdapter {

    private Activity activity;
    private List<RefillListItem> arrObjects;
    private RecyclerItemCallBack<RefillListItem> listener;

    public RefillClassAdapter(Activity activity, List<RefillListItem> objects, RecyclerItemCallBack<RefillListItem> listener) {
        this.activity = activity;
        this.arrObjects = objects;
        this.listener = listener;
    }

    @Override
    public int getCount() {

        if (arrObjects != null) {
            return arrObjects.size();
        }

        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (arrObjects != null) {
            return arrObjects.get(position);
        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AdapterHolder holder;
        final RefillListItem item = arrObjects.get(position);

        if (convertView == null) {
            LayoutInflater mLayoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mLayoutInflater.inflate(
                    R.layout.grid_view_topup, parent, false);

            holder = new AdapterHolder();
            holder.rlItemPurchased = convertView.findViewById(R.id.item_purchased);
            holder.coin_image = convertView.findViewById(R.id.coin_image);
            holder.usd_price = convertView.findViewById(R.id.usd_price);
            holder.percentage = convertView.findViewById(R.id.percentage);
            holder.txt_usd = convertView.findViewById(R.id.txt_usd);
            holder.name_icon = convertView.findViewById(R.id.name_icon);

            convertView.setTag(holder);

        } else {
            holder = (AdapterHolder) convertView.getTag();
        }

        ImageLoaderUtil.displayMediaImage(activity, item.getImage(), holder.coin_image);
//        holder.percentage.setText("+ " + item.getPercentage() + " " + activity.getString(R.string.refill_percen));
        holder.txt_usd.setText(String.format(activity.getString(R.string.refill_usd), item.getPrice_usd()));
        holder.usd_price.setText(item.getBean() + "");
        holder.name_icon.setText(item.getName());

        holder.rlItemPurchased.setOnClickListener(v -> {
            if (listener != null) listener.onItemClicked(item, position);
        });

        return convertView;
    }


    private class AdapterHolder {
        private RelativeLayout rlItemPurchased;
        private ImageView coin_image;
        private TextView usd_price;
        private TextView percentage;
        private TextView txt_usd;
        private TextView name_icon;
    }
}
