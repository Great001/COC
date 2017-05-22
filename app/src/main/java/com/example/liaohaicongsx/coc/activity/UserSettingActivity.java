package com.example.liaohaicongsx.coc.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.model.UserModel;
import com.example.liaohaicongsx.coc.util.NavigationUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;

public class UserSettingActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvExitAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        mTvExitAccount = (TextView) findViewById(R.id.tv_exit_account);
        mTvExitAccount.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_exit_account:
                showLogOutComfirm();
                break;
            default:
                break;
        }
    }

    /**
     * 退出登录确认
     */
    public void showLogOutComfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.comfirm_to_logout);
        builder.setPositiveButton(R.string.comfirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                NIMClient.getService(AuthService.class).logout();
                UserModel.getInstance().setLoginState(false);
                NavigationUtil.navigateToLoginPage(UserSettingActivity.this);
                finish();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }


}
