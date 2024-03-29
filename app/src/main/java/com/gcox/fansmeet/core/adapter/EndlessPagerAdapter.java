package com.gcox.fansmeet.core.adapter;

import android.os.Parcelable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by User on 9/29/2016.
 */

public class EndlessPagerAdapter extends PagerAdapter {

    private static final String TAG = "EndlessPagerAdapter";

    private final PagerAdapter mPagerAdapter;

    public EndlessPagerAdapter(PagerAdapter pagerAdapter) {
        if (pagerAdapter == null) {
            throw new IllegalArgumentException("Did you forget initialize PagerAdapter?");
        }
        if ((pagerAdapter instanceof FragmentPagerAdapter || pagerAdapter instanceof FragmentStatePagerAdapter) && pagerAdapter.getCount() < 3) {
            throw new IllegalArgumentException("When you use FragmentPagerAdapter or FragmentStatePagerAdapter, it only supports >= 3 pages.");
        }
        mPagerAdapter = pagerAdapter;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mPagerAdapter.destroyItem(container, getVirtualPosition(position), object);

        if (mPagerAdapter.getCount() < 4) {
            mPagerAdapter.instantiateItem(container, getVirtualPosition(position));
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        mPagerAdapter.finishUpdate(container);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE; // this is the magic that we can scroll infinitely.
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mPagerAdapter.getPageTitle(getVirtualPosition(position));
    }

    @Override
    public float getPageWidth(int position) {
        return mPagerAdapter.getPageWidth(getVirtualPosition(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return mPagerAdapter.isViewFromObject(view, o);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return mPagerAdapter.instantiateItem(container, getVirtualPosition(position));
    }

    @Override
    public Parcelable saveState() {
        return mPagerAdapter.saveState();
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        mPagerAdapter.restoreState(state, loader);
    }

    @Override
    public void startUpdate(ViewGroup container) {
        mPagerAdapter.startUpdate(container);
    }

    int getVirtualPosition(int realPosition) {
        return realPosition % mPagerAdapter.getCount();
    }
}