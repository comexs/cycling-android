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

    QQAuthListener listener;

    public void login(final Activity activity, QQAuthListener listener) {
        this.listener = listener;
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, activity.getApplicationContext());
        if (!mTencent.isSessionValid()) {
            mTencent.login(activity, "all", new IUiListener() {
                @Override
                public void onError(UiError arg0) {
                    // TODO Auto-generated method stub
                    Toast.makeText(activity, "授权失败", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onComplete(Object object) {
                    // TODO Auto-generated method stub
                    if (TextUtils.isEmpty(object.toString())) return;
                    try {
                        JSONObject jsonObject = new JSONObject(object.toString());
                        String token = jsonObject.getString("access_token");
                        String openid = jsonObject.getString("openid");
                        if (null != QQClient.this.listener) {
                            QQClient.this.listener.success(token, openid);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancel() {
                    // TODO Auto-generated method stub
                    Toast.makeText(activity, "授权取消", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
        if (null != mTencent) {
            mTencent.onActivityResult(requestCode, resultCode, data);
        }
    }

    public interface QQAuthListener {
        void success(String token, String openid);
    }

    // flag==0 表QQ,flag==1表QQZone
    public void shareQQ(Activity context, int flag) {
        Bundle bundle = new Bundle();
        // 这条分享消息被好友点击后的跳转URL。
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, Constants.SHARE_URL);
        // 分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_ SUMMARY不能全为空，最少必须有一个是有值的。
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, Constants.SHARE_TITLE);
        // 分享的图片URL
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, Constants.SHARE_IMAGE_URL);
        // bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,);
        // 分享的消息摘要，最长50个字
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, Constants.SHARE_CONTENT);
        // 手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
        bundle.putString(QQShare.SHARE_TO_QQ_EXT_STR, Constants.SHARE_TITLE);
        // 标识该消息的来源应用，值为应用名称+AppId。
        if (flag == 1) {
            ArrayList<String> mList = new ArrayList<String>();
            mList.add(Constants.SHARE_IMAGE_URL);
            bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, mList);
        }
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, context.getString(R.string.app_name) + new Date());
        mTencent.shareToQQ(context, bundle, new IUiListener() {
            @Override
            public void onError(UiError arg0) {
                // TODO Auto-generated method stub
//                failure();
            }

            @Override
            public void onComplete(Object arg0) {
                // TODO Auto-generated method stub
//                success(UMPlatformData.UMedia.TENCENT_QQ);
            }

            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                //handler.sendEmptyMessage(0);
//                cancle();
            }
        });
    }

    // flag==0 表QQ,flag==1表QQZone
    public void shareImageQQ(Activity context, int flag, String path) {
        Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, path);
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        if (flag == 0) {
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        } else {
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        }
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, context.getString(R.string.app_name) + new Date());
        mTencent.shareToQQ(context, params, new IUiListener() {
            @Override
            public void onError(UiError arg0) {
                // TODO Auto-generated method stub
                //handler.sendEmptyMessage(-1);
//                failure();
            }

            @Override
            public void onComplete(Object arg0) {
                // TODO Auto-generated method stub
                //handler.sendEmptyMessage(1);
//                success(UMPlatformData.UMedia.TENCENT_QQ);
            }

            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                //handler.sendEmptyMessage(0);
//                cancle();
            }
        });

    }
}
