package com.alex.cycling.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * Created by Administrator on 2015/9/21.
 */
public class WeiboClient {

//    private AuthInfo mAuthInfo;
//    private Activity context;
//
//    /**
//     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
//     */
//    private Oauth2AccessToken mAccessToken;
//
//    /**
//     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
//     */
//    private SsoHandler mSsoHandler;
//
//    WBAuthListener listener;
//
//    public void login(Activity activity, WBAuthListener listener) {
//        this.context = activity;
//        this.listener = listener;
//        mAuthInfo = new AuthInfo(context, Constants.SINA_APP_KEY, Constants.REDIRECT_URL, null);
//        mSsoHandler = new SsoHandler(activity, mAuthInfo);
//        mSsoHandler.authorize(new AuthListener());
//    }
//
//    /**
//     * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
//     * {@linkSsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
//     * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
//     * SharedPreferences 中。
//     */
//    class AuthListener implements WeiboAuthListener {
//
//        @Override
//        public void onComplete(Bundle values) {
//            // 从 Bundle 中解析 Token
//            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
//            if (mAccessToken.isSessionValid()) {
//                // 显示 Token
//                // 保存 Token 到 SharedPreferences
//                AccessTokenKeeper.writeAccessToken(context, mAccessToken);
//                updateTokenView(true);
//                if (null != listener) {
//                    listener.success(mAccessToken);
//                }
//            } else {
//                // 以下几种情况，您会收不到 Code：
//                // 1. 当您未在平台上注册的应用程序的包名与签名时；
//                // 2. 当您注册的应用程序包名与签名不正确时；
//                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
//                String code = values.getString("code");
//                String message = "失败";
//                if (!TextUtils.isEmpty(code)) {
//                    message = message + "\nObtained the code: " + code;
//                }
//                RtViewUtils.showToast(message);
////                if (null != listener) {
////                    listener.failure();
////                }
//            }
//        }
//
//        @Override
//        public void onCancel() {
//            RtViewUtils.showToast("取消授权");
//        }
//
//        @Override
//        public void onWeiboException(WeiboException e) {
//            RtViewUtils.showToast(e.getMessage());
////            if (null != listener) {
////                listener.failure();
////            }
//        }
//    }
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // SSO 授权回调
//        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
//        if (mSsoHandler != null) {
//            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
//        }
//    }
//
//    /**
//     * 显示当前 Token 信息。
//     *
//     * @param hasExisted 配置文件中是否已存在 token 信息并且合法
//     */
//    private void updateTokenView(boolean hasExisted) {
//        // 获取当前已保存过的 Token
//        mAccessToken = AccessTokenKeeper.readAccessToken(context);
//        // mAccessToken.getToken(), mAccessToken.getUid()
//    }
//
//    public interface WBAuthListener {
//        void success(Oauth2AccessToken mAccessToken);
//
//        // void failure();
//    }
}
