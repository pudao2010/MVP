package com.pudao.android.bean;

import com.pudao.android.utils.PinyinUtil;

/**
 * Created by pucheng on 2017/2/27.
 */

public class ContactsBean {

    private int avatar;

    private String userName;

    public boolean showFirstLetter = true;

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String getFirstLetter() {
        return PinyinUtil.getAlpha(userName).substring(0,1);
    }

    public String getFirstLetterString() {
        return String.valueOf(getFirstLetter()).toUpperCase();
    }

}
