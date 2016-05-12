package com.alex.cycling.client;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Administrator on 2015/9/21.
 */
public class QQClient {

//    public Tencent mTencent;
//
//    QQAuthListener listener;
//
//    public void login(Activity activity, QQAuthListener listener) {
//        this.listener = listener;
//        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, activity.getApplicationContext());
//        if (!mTencent.isSessionValid()) {
//            mTencent.login(activity, "all", new IUiListener() {
//                @Override
//                public void onError(UiError arg0) {
//                    // TODO Auto-generated method stub
//                    RtViewUtils.showToast("授权失败");
//                }
//
//                @Override
//                public void onComplete(Object object) {
//                    // TODO Auto-generated method stub
//                    try {
//                        String token = new JsonParser().parse(object.toString()).getAsJsonObject().get("access_token").getAsString();
//                        String openid = new JsonParser().parse(object.toString()).getAsJsonObject().get("openid").getAsString();
//                        if (null != QQClient.this.listener) {
//                            QQClient.this.listener.success(token, openid);
//                        }
//                    } catch (Exception e) {
//                        // TODO: handle exception
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onCancel() {
//                    // TODO Auto-generated method stub
//                    RtViewUtils.showToast("授权取消");
//                }
//            });
//        }
//    }
//
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // SSO 授权回调
//        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
//        if (null != mTencent) {
//            mTencent.onActivityResult(requestCode, resultCode, data);
//        }
//    }
//
//    public interface QQAuthListener {
//        void success(String token, String openid);
//    }
}
