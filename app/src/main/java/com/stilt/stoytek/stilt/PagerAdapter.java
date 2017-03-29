package com.stilt.stoytek.stilt;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

public class PagerAdapter extends FragmentPagerAdapter {

    private String lydbelastningsTag;
    private String ttsTag;
    private String infosideTag;

    public PagerAdapter(FragmentManager fm) {
        super(fm);

    }



    @Override
    public Fragment getItem(int arg0) {

        switch (arg0) {
            case 0:
                return new LydbelastningsFragment();
            case 1:
                return new TtsFragment();
            case 2:
                return new InfosideFragment();

            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {

        return 3;       //hvor mange views man har
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.wtf("PAgerAdapter", "instantiating item");
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        // get the tags set by FragmentPagerAdapter
        switch (position) {
            case 0:
                Log.wtf("PagerAdapter", "Enetered case 0");
                lydbelastningsTag = createdFragment.getTag();
                Log.wtf("PagerAdapter", "tag = "+lydbelastningsTag);
                break;
            case 1:
                ttsTag = createdFragment.getTag();
                break;
            case 2:
                infosideTag = createdFragment.getTag();
        }
        // ... save the tags somewhere so you can reference them later
        return createdFragment;
    }


    public String getLydbelastningsTag() {
        return lydbelastningsTag;
    }

    public String getTtsTag() {
        return ttsTag;
    }

    public String getInfosideTag() {
        return infosideTag;
    }

}