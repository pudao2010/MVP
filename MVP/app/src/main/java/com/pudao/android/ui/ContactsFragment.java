package com.pudao.android.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pudao.android.R;
import com.pudao.android.base.BaseFragment;
import com.pudao.android.bean.ContactsBean;
import com.pudao.android.widget.SlideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by pucheng on 2017/2/27.
 * 通讯录
 */

public class ContactsFragment extends BaseFragment {

    private static final int POSITION_NOT_FOUND = -1;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.slidebar)
    SlideBar mSlidebar;
    @BindView(R.id.section)
    TextView mSection;

    private BaseQuickAdapter<ContactsBean, BaseViewHolder> mAdapter;
    private List<ContactsBean> mList = new ArrayList<>();
    private SlideBar.OnSlideBarChangeListener mOnSlideBarChangeListener = new SlideBar.OnSlideBarChangeListener() {
        @Override
        public void onSectionChange(int index, String section) {
            mSection.setVisibility(View.VISIBLE);
            mSection.setText(section);
            scrollToSection(section);
        }

        @Override
        public void onSlidingFinish() {
            mSection.setVisibility(View.GONE);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contacts;
    }

    public static final String BUNDLE_TITLE = "title";

    public static ContactsFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        ContactsFragment fragment = new ContactsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initview() {
        super.initview();
        mSlidebar.setOnSlidingBarChangeListener(mOnSlideBarChangeListener);
    }

    @Override
    protected void initdata() {
        super.initdata();
        String[] name = {"张", "周", "刘", "王", "陈"};
        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            ContactsBean bean = new ContactsBean();
            if (i % 5 == 0) {
                bean.setUserName(name[0] + random.nextInt(100));
            } else if (i % 5 == 1) {
                bean.setUserName(name[1] + random.nextInt(100));
            } else if (i % 5 == 2) {
                bean.setUserName(name[2] + random.nextInt(100));
            } else if (i % 5 == 3) {
                bean.setUserName(name[3] + random.nextInt(100));
            } else {
                bean.setUserName(name[4] + random.nextInt(100));
            }
            mList.add(bean);
        }

        Collections.sort(mList, new Comparator<ContactsBean>() {
            @Override
            public int compare(ContactsBean o1, ContactsBean o2) {
                return o1.getUserName().charAt(0) - o2.getUserName().charAt(0);
            }
        });

        mAdapter = new BaseQuickAdapter<ContactsBean, BaseViewHolder>(R.layout.view_contacts_item, mList) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, ContactsBean contactsBean) {
                TextView section = baseViewHolder.getView(R.id.section);
                if (contactsBean.showFirstLetter) {
                    section.setVisibility(VISIBLE);
                    section.setText(contactsBean.getFirstLetterString());
                } else {
                    section.setVisibility(GONE);
                }

                baseViewHolder.setText(R.id.user_name, contactsBean.getUserName());
//                        .setText(R.id.iv_avatar, contactsBean.getAvatar());

            }
        };
        View header = View.inflate(this.getContext(), R.layout.header_contacts, null);
        mAdapter.addHeaderView(header);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * RecyclerView滚动直到界面出现对应section的联系人
     *
     * @param section 首字符
     */
    private void scrollToSection(String section) {
        int sectionPosition = getSectionPosition(section);
        if (sectionPosition != POSITION_NOT_FOUND) {
            mRecyclerView.smoothScrollToPosition(sectionPosition);
        }
    }

    /**
     * @param section 首字符
     * @return 在联系人列表中首字符是section的第一个联系人在联系人列表中的位置
     */
    private int getSectionPosition(String section) {

        for (int i = 0; i < mList.size(); i++) {
            if (section.equals(mList.get(i).getFirstLetterString())) {
                return i;
            }
        }
        return POSITION_NOT_FOUND;
    }
}
