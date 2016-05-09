package com.alex.cycling.ui.camera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alex.cycling.R;
import com.alex.cycling.base.BaseActivity;
import com.alex.cycling.ui.camera.adapter.FilterAdapter;
import com.alex.cycling.ui.camera.adapter.StickerAdapter;
import com.alex.cycling.ui.camera.bean.Addon;
import com.alex.cycling.ui.camera.bean.TagItem;
import com.alex.cycling.ui.camera.imagezoom.ImageViewTouch;
import com.alex.cycling.ui.camera.util.EffectService;
import com.alex.cycling.ui.camera.util.EffectUtil;
import com.alex.cycling.ui.camera.util.FilterEffect;
import com.alex.cycling.ui.camera.util.GPUImageFilterTools;
import com.alex.cycling.ui.camera.widget.LabelSelector;
import com.alex.cycling.ui.camera.widget.LabelView;
import com.alex.cycling.ui.camera.widget.MyHighlightView;
import com.alex.cycling.ui.camera.widget.MyImageViewDrawableOverlay;
import com.alex.cycling.utils.DateUtil;
import com.alex.cycling.utils.DisplayUtil;
import com.alex.cycling.utils.FileUtil;
import com.alex.cycling.utils.ImageUtils;
import com.alex.cycling.utils.LogUtil;
import com.alex.cycling.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * 图片处理界面
 * Created by sky on 2015/7/8.
 * Weibo: http://weibo.com/2030683111
 * Email: 1132234509@qq.com
 */
public class PhotoProcessActivity extends BaseActivity {

    public static final int POST_TYPE_TAG = 0;
    public static final String PARAM_EDIT_TEXT = "PARAM_EDIT_TEXT";
    public static final int ACTION_EDIT_LABEL = 8080;
    public static final int ACTION_EDIT_LABEL_POI = 9090;
    public static final int POST_TYPE_POI = 1;

    //滤镜图片
    @Bind(R.id.gpuimage)
    GPUImageView mGPUImageView;
    @Bind(R.id.title_left_area)
    View left;
    //绘图区域
    @Bind(R.id.drawing_view_container)
    ViewGroup drawArea;
    //底部按钮
    @Bind(R.id.sticker_btn)
    TextView stickerBtn;
    @Bind(R.id.filter_btn)
    TextView filterBtn;
    @Bind(R.id.text_btn)
    TextView labelBtn;
    //工具区
    @Bind(R.id.list_tools)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar_area)
    ViewGroup toolArea;
    private MyImageViewDrawableOverlay mImageView;
    private LabelSelector labelSelector;
    //
//    //当前选择底部按钮
    private TextView currentBtn;
    //    //当前图片
    private Bitmap currentBitmap;
    //用于预览的小图片
    private Bitmap smallImageBackgroud;
    //小白点标签
    private LabelView emptyLabelView;
    private List<LabelView> labels = new ArrayList<LabelView>();
    //标签区域
    private View commonLabelArea;


    public static void newInstance(Context context, String pathUrl) {
        Intent intent = new Intent(context, PhotoProcessActivity.class);
        intent.setData(Uri.parse(pathUrl));
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_process);
        ButterKnife.bind(this);
        EffectUtil.clear();
        initView();
        initEvent();
        initSticker();

        ImageUtils.asyncLoadImage(this, getIntent().getData(), new ImageUtils.LoadImageCallback() {
            @Override
            public void callback(Bitmap result) {
                currentBitmap = result;
                mGPUImageView.setImage(currentBitmap);
            }
        });

        ImageUtils.asyncLoadSmallImage(this, getIntent().getData(), new ImageUtils.LoadImageCallback() {
            @Override
            public void callback(Bitmap result) {
                smallImageBackgroud = result;
            }
        });

    }

    private void initView() {
        //添加贴纸水印的画布
        View overlay = LayoutInflater.from(PhotoProcessActivity.this).inflate(
                R.layout.custom_drawable_overlay, null);
        mImageView = (MyImageViewDrawableOverlay) overlay.findViewById(R.id.drawable_overlay);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(DisplayUtil.getScreenWidth(),
                DisplayUtil.getScreenWidth());
        mImageView.setLayoutParams(params);
        overlay.setLayoutParams(params);
        drawArea.addView(overlay);
        //添加标签选择器
        RelativeLayout.LayoutParams rparams = new RelativeLayout.LayoutParams(DisplayUtil.getScreenWidth(), DisplayUtil.getScreenWidth());
        labelSelector = new LabelSelector(this);
        labelSelector.setLayoutParams(rparams);
        drawArea.addView(labelSelector);
        labelSelector.hide();

        //初始化滤镜图片
        mGPUImageView.setLayoutParams(rparams);

        //初始化空白标签
        emptyLabelView = new LabelView(this);
        emptyLabelView.setEmpty();
        EffectUtil.addLabelEditable(mImageView, drawArea, emptyLabelView,
                mImageView.getWidth() / 2, mImageView.getWidth() / 2);
        emptyLabelView.setVisibility(View.INVISIBLE);

        //初始化推荐标签栏
        commonLabelArea = LayoutInflater.from(PhotoProcessActivity.this).inflate(
                R.layout.custom_label_bottom, null);
        commonLabelArea.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        toolArea.addView(commonLabelArea);
        commonLabelArea.setVisibility(View.GONE);
    }

    private void initEvent() {
        labelSelector.setTxtClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.e("aaaa");
            }
        });
        labelSelector.setAddrClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.e("bbbb");
            }
        });
//        labelSelector.setTxtClicked(v -> {
//            EditTextActivity.openTextEdit(PhotoProcessActivity.this, "", 8, AppConstants.ACTION_EDIT_LABEL);
//        });
//        labelSelector.setAddrClicked(v -> {
//            EditTextActivity.openTextEdit(PhotoProcessActivity.this, "", 8, AppConstants.ACTION_EDIT_LABEL_POI);
//        });
        mImageView.setOnDrawableEventListener(wpEditListener);
        mImageView.setSingleTapListener(new ImageViewTouch.OnImageViewTouchSingleTapListener() {
            @Override
            public void onSingleTapConfirmed() {
                emptyLabelView.updateLocation((int) mImageView.getmLastMotionScrollX(),
                        (int) mImageView.getmLastMotionScrollY());
                emptyLabelView.setVisibility(View.VISIBLE);

                labelSelector.showToTop();
                drawArea.postInvalidate();
            }
        });

        labelSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                labelSelector.hide();
                emptyLabelView.updateLocation((int) labelSelector.getmLastTouchX(),
                        (int) labelSelector.getmLastTouchY());
                emptyLabelView.setVisibility(View.VISIBLE);
            }
        });
//
//        titleBar.setRightBtnOnclickListener(v -> {
//            savePicture();
//        });
    }

    @OnClick({R.id.title_left_area, R.id.sticker_btn, R.id.filter_btn, R.id.text_btn, R.id.title_right})
    void click(View v) {
        switch (v.getId()) {
            case R.id.title_left_area:   //后退
                finish();
                break;
            case R.id.sticker_btn:    //贴纸
                mRecyclerView.setVisibility(View.VISIBLE);
                labelSelector.hide();
                emptyLabelView.setVisibility(View.GONE);
                commonLabelArea.setVisibility(View.GONE);
                initSticker();
                break;
            case R.id.filter_btn:   //滤镜
                mRecyclerView.setVisibility(View.VISIBLE);
                labelSelector.hide();
                emptyLabelView.setVisibility(View.INVISIBLE);
                commonLabelArea.setVisibility(View.GONE);
                initFilter();
                break;
            case R.id.text_btn:    //标签
                mRecyclerView.setVisibility(View.GONE);
                labelSelector.showToTop();
                commonLabelArea.setVisibility(View.VISIBLE);
                break;
            case R.id.title_right:  //下一步
                savePicture();
                break;
        }
    }


    //保存图片
    private void savePicture() {
        //加滤镜
        final Bitmap newBitmap = Bitmap.createBitmap(mImageView.getWidth(), mImageView.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newBitmap);
        RectF dst = new RectF(0, 0, mImageView.getWidth(), mImageView.getHeight());
        try {
            cv.drawBitmap(mGPUImageView.capture(), null, dst, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
            cv.drawBitmap(currentBitmap, null, dst, null);
        }
        //加贴纸水印
        EffectUtil.applyOnSave(cv, mImageView);

        new SavePicToFileTask().execute(newBitmap);
    }

    private class SavePicToFileTask extends AsyncTask<Bitmap, Void, String> {
        Bitmap bitmap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProDialog("图片处理中...");
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            String fileName = null;
            try {
                bitmap = params[0];
                String picName = DateUtil.currentDate();
                fileName = ImageUtils.saveToFile(FileUtil.getCacheImgDir() + "/" + picName, bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showToast("图片处理错误，请退出相机并重试");
            }
            return fileName;
        }

        @Override
        protected void onPostExecute(String fileName) {
            super.onPostExecute(fileName);
            hideProDialog();
            if (TextUtils.isEmpty(fileName)) {
                return;
            }
            //将照片信息保存至sharedPreference
            //保存标签信息
            List<TagItem> tagInfoList = new ArrayList<TagItem>();
            for (LabelView label : labels) {
                tagInfoList.add(label.getTagInfo());
            }

            //将图片信息通过EventBus发送到MainActivity
//            FeedItem feedItem = new FeedItem(tagInfoList, fileName);
//            EventBus.getDefault().post(feedItem);
//            CameraManager.getInst().close();
            finish();
        }
    }


    public void tagClick(View v) {
        TextView textView = (TextView) v;
        TagItem tagItem = new TagItem(POST_TYPE_TAG, textView.getText().toString());
        addLabel(tagItem);
    }

    private MyImageViewDrawableOverlay.OnDrawableEventListener wpEditListener = new MyImageViewDrawableOverlay.OnDrawableEventListener() {

        @Override
        public void onMove(MyHighlightView view) {
        }

        @Override
        public void onFocusChange(MyHighlightView newFocus, MyHighlightView oldFocus) {
        }

        @Override
        public void onDown(MyHighlightView view) {

        }

        @Override
        public void onClick(MyHighlightView view) {
            labelSelector.hide();
        }

        @Override
        public void onClick(final LabelView label) {
            if (label.equals(emptyLabelView)) {
                return;
            }
//            alert("温馨提示", "是否需要删除该标签！", "确定", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    EffectUtil.removeLabelEditable(mImageView, drawArea, label);
//                    labels.remove(label);
//                }
//            }, "取消", null);
        }
    };

    private boolean setCurrentBtn(TextView btn) {
        if (currentBtn == null) {
            currentBtn = btn;
        } else if (currentBtn.equals(btn)) {
            return false;
        } else {
            currentBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
//        Drawable myImage = getResources().getDrawable(R.drawable.select_icon);
//        btn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, myImage);
        currentBtn = btn;
        return true;
    }

    //初始化贴图
    private void initSticker() {
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        StickerAdapter mAdapter = new StickerAdapter(EffectUtil.addonList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setItemClickListener(new StickerAdapter.OnItemClickListener() {
            @Override
            public void onItemCick(int position) {
                labelSelector.hide();
                Addon sticker = EffectUtil.addonList.get(position);
                EffectUtil.addStickerImage(mImageView, PhotoProcessActivity.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                                labelSelector.hide();
                            }
                        });
            }
        });
//        setCurrentBtn(stickerBtn);
    }


    //初始化滤镜
    private void initFilter() {
        final List<FilterEffect> filters = EffectService.getInst().getLocalFilters();
        final FilterAdapter adapter = new FilterAdapter(filters, smallImageBackgroud);
        mRecyclerView.setAdapter(adapter);
        adapter.setItemClickListener(new FilterAdapter.OnItemClickListener() {
            @Override
            public void onItemCick(int position) {
                labelSelector.hide();
                if (adapter.getSelectFilter() != position) {
                    adapter.setSelectFilter(position);
                    GPUImageFilter filter = GPUImageFilterTools.createFilterForType(
                            PhotoProcessActivity.this, filters.get(position).getType());
                    mGPUImageView.setFilter(filter);
                    GPUImageFilterTools.FilterAdjuster mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(filter);
                    //可调节颜色的滤镜
                    if (mFilterAdjuster.canAdjust()) {
                        mFilterAdjuster.adjust(100);  //给可调节的滤镜选一个合适的值
                    }
                }
            }
        });
    }

    //添加标签
    private void addLabel(TagItem tagItem) {
        labelSelector.hide();
        emptyLabelView.setVisibility(View.INVISIBLE);
        if (labels.size() >= 5) {
//            alert("温馨提示", "您只能添加5个标签！", "确定", null, null, null, true);
        } else {
            int left = emptyLabelView.getLeft();
            int top = emptyLabelView.getTop();
            if (labels.size() == 0 && left == 0 && top == 0) {
                left = mImageView.getWidth() / 2 - 10;
                top = mImageView.getWidth() / 2;
            }
            LabelView label = new LabelView(PhotoProcessActivity.this);
            label.init(tagItem);
            EffectUtil.addLabelEditable(mImageView, drawArea, label, left, top);
            labels.add(label);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        labelSelector.hide();
        super.onActivityResult(requestCode, resultCode, data);
        if (ACTION_EDIT_LABEL == requestCode && data != null) {
            String text = data.getStringExtra(PARAM_EDIT_TEXT);
            if (!TextUtils.isEmpty(text)) {
                TagItem tagItem = new TagItem(POST_TYPE_TAG, text);
                addLabel(tagItem);
            }
        } else if (ACTION_EDIT_LABEL_POI == requestCode && data != null) {
            String text = data.getStringExtra(PARAM_EDIT_TEXT);
            if (!TextUtils.isEmpty(text)) {
                TagItem tagItem = new TagItem(POST_TYPE_POI, text);
                addLabel(tagItem);
            }
        }
    }
}
