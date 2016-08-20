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
    public static final String WX_WECHAT = "wechat";
    public static final String WX_WXCIRCLE = "wxcircle";

    public void initWX() {
        api = WXAPIFactory.createWXAPI(context, ThirdConstants.WX_APP_KEY, false);
        api.registerApp(ThirdConstants.WX_APP_KEY);
        api.handleIntent(context.getIntent(), this);
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
        if (!api.isWXAppInstalled()) {
            Toast.makeText(context, "请先安装微信", Toast.LENGTH_LONG).show();
            return;
        }
        if (!api.isWXAppSupportAPI()) {
            Toast.makeText(context, "您当前的微信版本不支持分享", Toast.LENGTH_LONG).show();
            return;
        }

    }

    // 0表示为微信好友，1表示为微信朋友圈
    private void shareWX(int flag) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = ThirdConstants.SHARE_URL;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = ThirdConstants.SHARE_TITLE;
        msg.description = ThirdConstants.SHARE_CONTENT;
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


    public void login(Activity context) {
        this.context = context;
        wxLogin();
    }

    @Override
    public void onReq(BaseReq baseReq) {     //微信的回调函数

    }

    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) { //登录回来
                    if (null != ((SendAuth.Resp) resp).code) {

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

}
