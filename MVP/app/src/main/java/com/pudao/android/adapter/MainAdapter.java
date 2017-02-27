package com.pudao.android.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.pudao.android.ui.ContactsFragment;
import com.pudao.android.ui.FindFragment;
import com.pudao.android.ui.MineFragment;
import com.pudao.android.ui.WechatFragment;
import com.pudao.android.widget.AlphaTabsIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pucheng on 2017/2/27.
 * 主页面的Fragment适配
 */

public class MainAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener{

    private AlphaTabsIndicator alphaTabsIndicator;

    private List<Fragment> fragments = new ArrayList<>();
    private String[] titles = {"微信", "通讯录", "发现", "我"};

    public MainAdapter(FragmentManager fm, AlphaTabsIndicator alphaTabsIndicator) {
        super(fm);
        this.alphaTabsIndicator = alphaTabsIndicator;
        fragments.add(WechatFragment.newInstance(titles[0]));
        fragments.add(ContactsFragment.newInstance(titles[1]));
        fragments.add(FindFragment.newInstance(titles[2]));
        fragments.add(MineFragment.newInstance(titles[3]));
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (0 == position) {
            alphaTabsIndicator.getTabView(0).showNumber(alphaTabsIndicator.getTabView(0).getBadgeNumber() - 1);
        } else if (2 == position) {
            alphaTabsIndicator.getCurrentItemView().removeShow();
        } else if (3 == position) {
            alphaTabsIndicator.removeAllBadge();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
