package com.dou361.update;

import android.app.Activity;
import android.content.Intent;

import com.dou361.update.bean.Update;
import com.dou361.update.download.IUpdateExecutor;
import com.dou361.update.download.OnlineCheckWorker;
import com.dou361.update.download.UpdateExecutor;
import com.dou361.update.download.UpdateWorker;
import com.dou361.update.listener.OnlineCheckListener;
import com.dou361.update.listener.UpdateListener;
import com.dou361.update.server.DownloadingService;
import com.dou361.update.type.RequestType;
import com.dou361.update.type.UpdateType;
import com.dou361.update.util.UpdateConstants;
import com.dou361.update.view.UpdateDialogActivity;

public class UpdateAgent {
    private static UpdateAgent updater;
    private IUpdateExecutor executor;

    private UpdateAgent() {
        executor = UpdateExecutor.getInstance();
    }

    public static UpdateAgent getInstance() {
        if (updater == null) {
            updater = new UpdateAgent();
        }
        return updater;
    }


    /**
     * check out whether or not there is a new version on internet
     *
     * @param activity The activity who need to show update dialog
     */
    public void onlineCheck(Activity activity) {
        OnlineCheckWorker checkWorker = new OnlineCheckWorker();
        RequestType requestType = UpdateHelper.getInstance().getRequestMethod();
        checkWorker.setRequestMethod(requestType);
        checkWorker.setUrl(UpdateHelper.getInstance().getOnlineUrl());
        checkWorker.setParams(UpdateHelper.getInstance().getOnlineParams());
        checkWorker.setParser(UpdateHelper.getInstance().getOnlineJsonParser());

        final OnlineCheckListener mOnlinelistener = UpdateHelper.getInstance().getOnlineCheckListener();
        checkWorker.setUpdateListener(new OnlineCheckListener() {

            @Override
            public void hasParams(String value) {
                if (mOnlinelistener != null) {
                    mOnlinelistener.hasParams(value);
                }
            }
        });
        executor.onlineCheck(checkWorker);

    }

    /**
     * check out whether or not there is a new version on internet
     *
     * @param activity The activity who need to show update dialog
     */
    public void checkUpdate(final Activity activity) {
        UpdateWorker checkWorker = new UpdateWorker();
        RequestType requestType = UpdateHelper.getInstance().getRequestMethod();
        checkWorker.setRequestMethod(requestType);
        checkWorker.setUrl(UpdateHelper.getInstance().getCheckUrl());
        checkWorker.setParams(UpdateHelper.getInstance().getCheckParams());
        checkWorker.setParser(UpdateHelper.getInstance().getCheckJsonParser());

        final UpdateListener mUpdate = UpdateHelper.getInstance().getUpdateListener();
        checkWorker.setUpdateListener(new UpdateListener() {
            @Override
            public void hasUpdate(Update update) {
                if (mUpdate != null) {
                    mUpdate.hasUpdate(update);
                }
                if (UpdateHelper.getInstance().getUpdateType() == UpdateType.autowifidown) {
                    Intent intent = new Intent(activity, DownloadingService.class);
                    intent.putExtra(UpdateConstants.DATA_ACTION, UpdateConstants.START_DOWN);
                    intent.putExtra(UpdateConstants.DATA_UPDATE, update);
                    activity.startService(intent);
                    return;
                }

                Intent intent = new Intent(activity, UpdateDialogActivity.class);
                intent.putExtra(UpdateConstants.DATA_UPDATE, update);
                intent.putExtra(UpdateConstants.DATA_ACTION, UpdateConstants.UPDATE_TIE);
                activity.startActivity(intent);
            }

            @Override
            public void noUpdate() {
                if (mUpdate != null) {
                    mUpdate.noUpdate();
                }
            }

            @Override
            public void onCheckError(int code, String errorMsg) {
                if (mUpdate != null) {
                    mUpdate.onCheckError(code, errorMsg);
                }
            }

            @Override
            public void onUserCancel() {
                if (mUpdate != null) {
                    mUpdate.onUserCancel();
                }
            }
        });
        executor.check(checkWorker);
    }

}
