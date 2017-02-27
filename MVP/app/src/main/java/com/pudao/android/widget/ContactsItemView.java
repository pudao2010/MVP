package com.pudao.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pudao.android.R;
import com.pudao.android.bean.ContactsBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pucheng on 2017/2/27.
 * 联系人自定义组合View , 包含了首字母的Text
 */

public class ContactsItemView extends LinearLayout {

    public static final String TAG = "ContactItemView";
    @BindView(R.id.section)
    TextView mSection;
    @BindView(R.id.user_name)
    TextView mUserName;

    public ContactsItemView(Context context) {
        this(context, null);
    }

    public ContactsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_contacts_item, this);
        ButterKnife.bind(this, this);
    }

    public void bindView(ContactsBean contactsBean) {
        mUserName.setText(contactsBean.getUserName());
        if (contactsBean.showFirstLetter) {
            mSection.setVisibility(VISIBLE);
            mSection.setText(contactsBean.getFirstLetterString());
        } else {
            mSection.setVisibility(GONE);
        }
    }
}
