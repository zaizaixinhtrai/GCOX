package com.gcox.fansmeet.features.home;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.util.ImageLoaderUtil;
import timber.log.Timber;

import java.util.List;

/**
 * Created by Ngoc on 9/27/2016.
 */

public class EventPagerAdapter extends PagerAdapter {

    List<HomeCurrentEventModel> eventModels;
    private Context context;
    private LayoutInflater inflater;
    private EventClick eventClick;

    public void setEventClick(EventClick eventClick) {
        this.eventClick = eventClick;
    }

    public EventPagerAdapter(Context context, List<HomeCurrentEventModel> eventModels) {
        this.context = context;
        this.eventModels = eventModels;
    }

    @Override
    public int getCount() {
        if (eventModels == null)
            return 0;
        return eventModels.size();
    }

    public void setEventModels(List<HomeCurrentEventModel> eventModels) {
        this.eventModels = eventModels;
        Timber.e("update %s",this.eventModels);
    }

    public HomeCurrentEventModel getModelAt(int position){
        return (eventModels!=null && !eventModels.isEmpty())? eventModels.get(position) : null;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((FrameLayout) object);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final ImageView imgDisplay;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.event_viewpagee_layout, container,
                false);
        imgDisplay = (ImageView) viewLayout.findViewById(R.id.imv_banner);

        imgDisplay.setOnClickListener(v -> {
            if (eventClick != null) {
                // event tracking
                int eventId = eventModels.get(position).getEventId();
                eventClick.goToLick(position);
            }
        });


        ImageLoaderUtil.displayMediaImage(context, eventModels.get(position).getImage(), imgDisplay);
        container.addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);
    }

    public interface EventClick {
        void goToLick(int position);
    }
}
