package android_app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;

/**
 * Created by Pascal on 08.06.2015.
 */

public class Adapter_Fragments extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private ViewPager pager;

    public Adapter_Fragments(FragmentManager fm, List<Fragment> fragments, ViewPager pager) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }


}