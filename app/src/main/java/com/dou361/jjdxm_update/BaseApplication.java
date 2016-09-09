package com.dou361.jjdxm_update;

import android.app.Application;

/**
 * @author Administrator
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /**默认的请求方式，使用get请求*/
        UpdateConfig.initGet(this);
        /**post的请求方式*/
        //UpdateConfig.initPost(this);

    }
}
