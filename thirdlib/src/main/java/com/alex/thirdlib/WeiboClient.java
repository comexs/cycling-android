package com.alex.thirdlib;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseRequest;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;

/**
 * Created by Administrator on 2015/9/21.
 */
public class WeiboClient implements IWeiboHandler.Request{

    private AuthInfo mAuthInfo;
    private Activity context;
    private Oauth2AccessToken mAccessToken;
    private SsoHandler mSsoHandler;
    WBAuthListener listener;
    IWeiboShareAPI mWeiboShareAPI = null;

    public void login(Activity activity, WBAuthListener listener) {
        this.context = activity;
        this.listener = listener;
        mAuthInfo = new AuthInfo(context, Constants.SINA_APP_KEY, Constants.REDIRECT_URL, null);
        mSsoHandler = new SsoHandler(activity, mAuthInfo);
        mSsoHandler.authorize(authListener);
    }

    /**
     * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
     * {@linkSsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
     * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
     * SharedPreferences 中。
     */
    private WeiboAuthListener authListener = new WeiboAuthListener() {
        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {
                // 显示 Token
                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(context, mAccessToken);
                updateTokenView(true);
                if (null != listener) {
                    listener.success(mAccessToken);
                }
            } else {
                // 以下几种情况，您会收不到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = "失败";
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancel() {
            Toast.makeText(context, "取消授权", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    /**
     * 显示当前 Token 信息。
     *
     * @param hasExisted 配置文件中是否已存在 token 信息并且合法
     */
    private void updateTokenView(boolean hasExisted) {
        // 获取当前已保存过的 Token
        mAccessToken = AccessTokenKeeper.readAccessToken(context);
//         mAccessToken.getToken(), mAccessToken.getUid()
    }

    @Override
    public void onRequest(BaseRequest baseRequest) {
//        UserInfo userInfo = ModelUtils.getCurrentUser();
//        if (null != userInfo) {
//            UMPlatformData platform = new UMPlatformData(UMPlatformData.UMedia.SINA_WEIBO, userInfo.getUserId());
//            platform.setGender(UMPlatformData.GENDER.MALE); //optional
//            platform.setName(userInfo.getUsername());
//            MobclickAgent.onSocialEvent(context, platform);
//        }
    }

    public interface WBAuthListener {
        void success(Oauth2AccessToken mAccessToken);
    }

    private void onClickSina() {
        if (mWeiboShareAPI == null) {
            mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, Constants.SINA_APP_KEY);
            // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
            // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
            // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
            mWeiboShareAPI.registerApp();
        }

        mWeiboShareAPI.handleWeiboRequest(context.getIntent(), this);
        //mWeiboShareAPI.handleWeiboResponse(context.getIntent(), this);

        // 如果未安装微博客户端，设置下载微博对应的回调
        if (!mWeiboShareAPI.isWeiboAppInstalled()) {
            Toast.makeText(context, "您未安装微博客户端", Toast.LENGTH_SHORT).show();
            return;
        }
        int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
        if (supportApi >= 10351) {
            if (mWeiboShareAPI.isWeiboAppInstalled()) {
                mWeiboShareAPI.registerApp();
            }
//            if (status == STATUS_SHARE_IMAGE) {
//                shareWeiboImage(path);
//            } else {
//                shareWeiboUrl();
//            }
        } else {
            Toast.makeText(context, "您当前的新浪微博版本太低!", Toast.LENGTH_SHORT).show();
        }
    }


    //微博分享url
    public void shareWeiboUrl() {
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.mediaObject = getWebpageObj();
        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        // 3. 发送请求消息到微博，唤起微博分享界面
        //mWeiboShareAPI.sendRequest(request);
        mWeiboShareAPI.sendRequest(context, request);
    }

    //微博分享图片
    public void shareWeiboImage(String path) {
        // 2. 初始化从第三方到微博的消息请求
        WeiboMessage weiboMessage = new WeiboMessage();
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        weiboMessage.mediaObject = getImageObj(bitmap);
        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.message = weiboMessage;
        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendRequest(context, request);
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj() {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = Constants.SHARE_TITLE;
        mediaObject.description = Constants.SHARE_CONTENT;
        // 设置 Bitmap 类型的图片到视频对象里
//        Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon);
//        mediaObject.setThumbImage(thumb);
        mediaObject.actionUrl = Constants.SHARE_URL;
        mediaObject.defaultText = Constants.SHARE_CONTENT;
        return mediaObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    private ImageObject getImageObj(Bitmap bitmap) {
        ImageObject imageObject = new ImageObject();
        // BitmapDrawable bitmapDrawable = (BitmapDrawable) mImageView.getDrawable();
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

}
