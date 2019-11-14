package com.gcox.fansmeet.features.home;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.util.ImageLoaderUtil;

import java.util.List;

public class HomeSliderAdapter extends PagerAdapter {
    private Context context;
    private List<String> userImages;

    public HomeSliderAdapter(Context context, List<String> userImage) {
        this.context = context;
        this.userImages = userImage;
    }

    @Override
    public int getCount() {
        if (userImages == null) return 0;
        return userImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);
        ImageView userImage = view.findViewById(R.id.userImage);
        ImageLoaderUtil.displayUserImage(context.getApplicationContext(), userImages.get(position),
                userImage);

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
