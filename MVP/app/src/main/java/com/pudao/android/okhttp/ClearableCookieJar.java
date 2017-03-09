package com.pudao.android.okhttp;

import okhttp3.CookieJar;

/**
 * Created by pucheng on 2017/1/18.
 */

public interface ClearableCookieJar extends CookieJar{

    /**
     * Clear all the session cookies while maintaining the persisted ones.
     */
    void clearSession();

    /**
     * Clear all the cookies from persistence and from the cache.
     */
    void clear();

}
