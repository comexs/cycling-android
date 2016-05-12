package com.alex.thirdlib;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.File;

/**
 * Created by Administrator on 2015/11/17.
 */
public class WeixinClient implements IWXAPIEventHandler {

    public IWXAPI api;
    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
    private Activity context;
    WXAuthListener listener = null;
    public static final String WX_WECHAT = "wechat";
    public static final String WX_WXCIRCLE = "wxcircle";

    public void initWX() {
        api = WXAPIFactory.createWXAPI(context, Constants.WX_APP_KEY, false);
        api.registerApp(Constants.WX_APP_KEY);
        api.handleIntent(context.getIntent(), this);
//        WXEntryActivity.weixinHandler = this;
    }

    private void wxLogin() {
        // send oauth request
        initWX();
        if (api.isWXAppInstalled()) {
            int wxSdkVersion = api.getWXAppSupportAPI();
            if (wxSdkVersion >= TIMELINE_SUPPORTED_VERSION) {
                final SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "wechat_rabbit";
                api.sendReq(req);
                api.handleIntent(context.getIntent(), this);
            } else {
                Toast.makeText(context, "您当前的微信版本不支持分享", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "请先安装微信", Toast.LENGTH_LONG).show();
        }
    }

    private void shareToWeixin() {
        initWX();
        if (api.isWXAppInstalled()) {
            int wxSdkVersion = api.getWXAppSupportAPI();
            if (wxSdkVersion >= TIMELINE_SUPPORTED_VERSION) {
//                if (status == STATUS_SHARE_IMAGE) {
////                    ShareImageWX(0, path);
//                    ShareImageWXEx(path);
//                } else {
//                    ShareWX(0);
//                }
            } else {
                Toast.makeText(context, "您当前的微信版本不支持分享", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "请先安装微信", Toast.LENGTH_LONG).show();
        }
    }

    // 0表示为微信好友，1表示为微信朋友圈
    private void shareWX(int flag) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = Constants.SHARE_URL;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = Constants.SHARE_TITLE;
        msg.description = Constants.SHARE_CONTENT;
        //Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon);
        //msg.thumbData = Util.bmpToByteArray(thumb, false);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        if (flag == 0) {
            req.transaction = buildTransaction(WX_WECHAT);
        } else if (flag == 1) {
            req.transaction = buildTransaction(WX_WXCIRCLE);
        }
        req.message = msg;
        if (flag == 0) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        } else {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        api.sendReq(req);
    }

    private void shareImageWXEx(String path) {
        Intent localIntent = new Intent("android.intent.action.SEND");
        localIntent.setType("image/*");
        localIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(path)));
        localIntent.putExtra("android.intent.extra.SUBJECT", "分享标题");
        localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setClassName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        try {
            context.startActivity(localIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


    public void login(Activity context, WXAuthListener listener) {
        this.context = context;
        this.listener = listener;
        wxLogin();
    }

    //微信的回调函数
    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //登录回来
                if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
                    if (null != ((SendAuth.Resp) resp).code) {
//                        RtViewUtils.showProDialog(activity, ResUtils.getStringRes(R.string.logining));
//                        LoginApi.getInstance().getWXToken(((SendAuth.Resp) resp).code, toString(), new TokenListener());
                    }
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //授权取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //授权失败
                break;
            default:
                // result = R.string.errcode_unknown;
                break;
        }
    }

    //微信登录请求
//    class TokenListener implements Response.Listener<WXToken> {
//        @Override
//        public void onResponse(WXToken wxToken) {
//            if (null != wxToken) {
//                // 获取用户信息接口
//                //LoginApi.getInstance().otherLogin(SettingPreference.getToken(), toString(), LoginApi.WXLOGIN, wxToken.getAccess_token(), wxToken.getOpenid(), new ResponseListener(), params);
//                if (null != listener) {
//                    listener.success(wxToken);
//                    activity = null;
//                }
//            }
//        }
//    }

    public interface WXAuthListener {
        void success();
    }
}
