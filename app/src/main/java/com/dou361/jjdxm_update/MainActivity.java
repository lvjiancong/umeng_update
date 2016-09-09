package com.dou361.jjdxm_update;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dou361.update.UpdateHelper;
import com.dou361.update.type.UpdateType;
import com.dou361.update.listener.ForceListener;
import com.dou361.update.listener.UpdateListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button update;
    private Button ddd;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        update = (Button) findViewById(R.id.update);
        ddd = (Button) findViewById(R.id.ddd);
        update.setOnClickListener(this);
        ddd.setOnClickListener(this);
        UpdateHelper.getInstance()
                .setForceListener(new ForceListener() {
                    @Override
                    public void onUserCancel(boolean force) {
                        if (force) {
                            //退出应用
                            finish();
                        }
                    }
                })
                .check(MainActivity.this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.update) {
            UpdateHelper.getInstance()
                    .setUpdateType(UpdateType.checkupdate)
                    .setUpdateListener(new UpdateListener() {
                        @Override
                        public void noUpdate() {
                            Toast.makeText(mContext, "已经是最新版本了", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCheckError(int code, String errorMsg) {
                            Toast.makeText(mContext, "检测更新失败：" + errorMsg, Toast.LENGTH_LONG).show();
                        }
                    })
                    .check(MainActivity.this);
        } else if (v.getId() == R.id.ddd) {
        }
    }
}
