package com.alex.cycling.client;

import android.app.Activity;

/**
 * Created by Administrator on 2015/11/17.
 */
public class WeixinClient  {

//    public IWXAPI api;
//
//    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
//
//    private Activity activity;
//
//    WXAuthListener listener = null;
//
//    public void initWX() {
//        api = WXAPIFactory.createWXAPI(activity, Constants.WX_APP_KEY, false);
//        api.registerApp(Constants.WX_APP_KEY);
//        api.handleIntent(activity.getIntent(), this);
//        WXEntryActivity.weixinHandler = this;
//    }
//
//    private void wxLogin() {
//        // send oauth request
//        initWX();
//        if (api.isWXAppInstalled()) {
//            int wxSdkVersion = api.getWXAppSupportAPI();
//            if (wxSdkVersion >= TIMELINE_SUPPORTED_VERSION) {
//                final SendAuth.Req req = new SendAuth.Req();
//                req.scope = "snsapi_userinfo";
//                req.state = "wechat_rabbit";
//                api.sendReq(req);
//                api.handleIntent(activity.getIntent(), this);
//            } else {
//                RtViewUtils.showToast("您当前的微信版本不支持分享");
//            }
//        } else {
//            RtViewUtils.showToast("请先安装微信");
//        }
//    }
//
//
//    public void login(Activity context, WXAuthListener listener) {
//        activity = context;
//        this.listener = listener;
//        wxLogin();
//    }
//
//    //微信的回调函数
//    @Override
//    public void onReq(BaseReq baseReq) {
//    }
//
//    @Override
//    public void onResp(BaseResp resp) {
//        switch (resp.errCode) {
//            case BaseResp.ErrCode.ERR_OK:
//                //登录回来
//                if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
//                    if (null != ((SendAuth.Resp) resp).code) {
//                        RtViewUtils.showProDialog(activity, ResUtils.getStringRes(R.string.logining));
//                        LoginApi.getInstance().getWXToken(((SendAuth.Resp) resp).code, toString(), new TokenListener());
//                    }
//                }
//                break;
//            case BaseResp.ErrCode.ERR_USER_CANCEL:
//                //授权取消
//                break;
//            case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                //授权失败
//                break;
//            default:
//                // result = R.string.errcode_unknown;
//                break;
//        }
//    }
//
//    //微信登录请求
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
//
//    public interface WXAuthListener {
//        void success(WXToken token);
//    }
}
