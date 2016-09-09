package com.dou361.jjdxm_update;

import android.content.Context;

import com.dou361.update.ParseData;
import com.dou361.update.UpdateHelper;
import com.dou361.update.bean.Update;
import com.dou361.update.type.RequestType;

import org.json.JSONObject;

import java.util.Random;
import java.util.TreeMap;

/**
 * ========================================
 * <p/>
 * 版 权：dou361.com 版权所有 （C） 2015
 * <p/>
 * 作 者：陈冠明
 * <p/>
 * 个人网站：http://www.dou361.com
 * <p/>
 * 版 本：1.0
 * <p/>
 * 创建日期：2016/6/14
 * <p/>
 * 描 述：
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public class UpdateConfig {

    private static String checkUrl = "http://www.baidu.com";
    private static String onlineUrl = "http://www.baidu.com";
    private static String apkFile = "http://wap.apk.anzhi.com/data3/apk/201512/20/55089e385f6e9f350e6455f995ca3452_26503500.apk";

    /**get方式请求的案例*/
    public static void initGet(Context context) {
        UpdateHelper.init(context);
        UpdateHelper.getInstance()
                // 可填：请求方式
                .setMethod(RequestType.post)
                // 必填：数据更新接口，该方法一定要在setDialogLayout的前面,因为这方法里面做了重置DialogLayout的操作
                .setCheckUrl(checkUrl)
                // 可填：自定义更新弹出的dialog的布局样式，主要案例中的布局样式里面的id为（jjdxm_update_content、jjdxm_update_id_ok、jjdxm_update_id_cancel）的view类型和id不能修改，其他的都可以修改或删除
                .setDialogLayout(R.layout.custom_update_dialog)
                // 必填：用于从数据更新接口获取的数据response中。解析出Update实例。以便框架内部处理
                .setCheckJsonParser(new ParseData() {
                    @Override
                    public Update parse(String response) {
                        // 此处模拟一个Update对象
                        Update update = new Update();
                        // 必填：此apk包的下载地址
                        update.setUpdateUrl(apkFile);
                        // 必填：此apk包的版本号
                        update.setVersionCode(2);
                        // 可填：此apk包的版本号
                        update.setApkSize(12400000);
                        // 必填：此apk包的版本名称
                        update.setVersionName("2.0");
                        // 可填：此apk包的更新内容
                        update.setUpdateContent("测试更新");
                        // 可填：此apk包是否为强制更新
                        update.setForce(false);
                        return update;
                    }
                });
    }

    /**
     * {
     * "code": 0,
     * "data": {
     * "v_code": "10",
     * "v_name": "v1.0.0.16070810",
     * "v_size": 12365909,
     * "v_sha1": "7db76e18ac92bb29ff0ef012abfe178a78477534",
     * "force": false,
     * "update_content": "测试更新接口",
     * "download_url": "http://115.159.45.251/software/feibei_live1.0.0.16070810_zs.apk
     * <p/>
     * "
     * }
     * }
     * post方式请求的案例
     */
    public static void initPost(Context context) {
        UpdateHelper.init(context);
        TreeMap<String, Object> params = new TreeMap<String, Object>();
        params.put("pkname", "com.jingwang.eluxue_online");
        params.put("Action", "Apps");
        params.put("SecretId", "d021e4f5tac98U4df5Nb943Odd3a313d9f68");
        params.put("Region", "gz");
        params.put("Nonce", Integer.valueOf((new Random()).nextInt(2147483647)));
        params.put("Timestamp", Long.valueOf(System.currentTimeMillis() / 1000L));
        params.put("RequestClient", "SDK_JAVA_1.0");
        try {
            params.put("Signature", Sign.sign(Sign.makeSignPlainText(params, "POST"), "FDC9BC1AA4B387CEBBF0F9355CEC2086"));
        } catch (Exception var9) {
            var9.printStackTrace();
        }
        UpdateHelper.getInstance()
                // 可填：请求方式
                .setMethod(RequestType.post)
                // 必填：数据更新接口
                .setCheckUrl(checkUrl, params)
                // 可填：在线参数接口
                .setOnlineUrl(onlineUrl)
                // 必填：用于从数据更新接口获取的数据response中。解析出Update实例。以便框架内部处理
                .setCheckJsonParser(new ParseData() {
                    @Override
                    public Update parse(String response) {
                        // 此处模拟一个Update对象
                        Update update = new Update();
                        try {
                            JSONObject jobj = new JSONObject(response);
                            if (!jobj.isNull("data")) {
                                JSONObject job = jobj.optJSONObject("data");
                                if (!job.isNull("v_code")) {
                                    // 此apk包的版本号
                                    update.setVersionCode(Integer.valueOf(job.optString("v_code")));
                                }
                                if (!job.isNull("v_size")) {
                                    // 此apk包的大小
                                    update.setApkSize(job.optLong("v_size"));
                                }
                                if (!job.isNull("v_name")) {
                                    // 此apk包的版本名称
                                    update.setVersionName(job.optString("v_name"));
                                }
                                if (!job.isNull("update_content")) {
                                    // 此apk包的更新内容
                                    update.setUpdateContent(job.optString("update_content"));
                                }
                                if (!job.isNull("download_url")) {
                                    // 此apk包的下载地址
                                    update.setUpdateUrl(job.optString("download_url"));
                                }
                                if (!job.isNull("force")) {
                                    // 此apk包的下载地址
                                    update.setForce(job.optBoolean("force", false));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return update;
                    }
                })
                // 可填：在线参数接口
                .setOnlineJsonParser(new ParseData() {
                    @Override
                    public String parse(String httpResponse) {
                        return null;
                    }
                });
    }

}
