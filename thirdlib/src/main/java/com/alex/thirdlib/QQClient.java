package com.alex.thirdlib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2015/9/21.
 */
public class QQClient {

    public Tencent mTencent;
    public Context context;

    public void login(Activity act) {
        context = act;
        mTencent = Tencent.createInstance(ThirdConstants.QQ_APP_ID, act.getApplicationContext());
        if (!mTencent.isSessionValid()) {
            mTencent.login(act, "all", mIUiListener);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
        if (null != mTencent) {
            mTencent.onActivityResult(requestCode, resultCode, data);
        }
    }

    // flag==0 表QQ,flag==1表QQZone
    public void shareQQ(Activity ctx, int flag) {
        context = ctx;
        Bundle bundle = new Bundle();
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, ThirdConstants.SHARE_URL);
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, ThirdConstants.SHARE_TITLE);
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, ThirdConstants.SHARE_IMAGE_URL);
        // bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,);
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, ThirdConstants.SHARE_CONTENT);
        bundle.putString(QQShare.SHARE_TO_QQ_EXT_STR, ThirdConstants.SHARE_TITLE);
        if (flag == 1) {
            ArrayList<String> mList = new ArrayList<String>();
            mList.add(ThirdConstants.SHARE_IMAGE_URL);
            bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, mList);
        }
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, context.getString(R.string.app_name) + new Date());
        mTencent.shareToQQ(ctx, bundle, mIUiListener);
    }

    // flag==0 表QQ,flag==1表QQZone
    public void shareImageQQ(Activity ctx, int flag, String path) {
        context = ctx;
        Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, path);
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        if (flag == 0) {
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        } else {
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        }
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, ctx.getString(R.string.app_name) + new Date());
        mTencent.shareToQQ(ctx, params, mIUiListener);
    }

    private IUiListener mIUiListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {

        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(context, "授权错误", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            if (null == context) {
                return;
            }
            Toast.makeText(context, "授权取消", Toast.LENGTH_SHORT).show();
        }
    };

}
