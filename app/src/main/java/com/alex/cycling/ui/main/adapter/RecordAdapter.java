package com.alex.cycling.ui.main.adapter;

import android.net.Uri;
import android.text.TextUtils;

import com.alex.cycling.R;
import com.alex.cycling.service.TrackManager;
import com.alex.cycling.utils.DateUtil;
import com.alex.cycling.utils.adapter.CustomBaseQuickAdapter;
import com.alex.greendao.TrackInfo;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by comexs on 16/3/20.
 */
public class RecordAdapter extends CustomBaseQuickAdapter<TrackInfo> {

    public RecordAdapter(List<TrackInfo> data) {
        super(R.layout.item_record, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TrackInfo trackInfo) {
        helper.setText(R.id.track_name, trackInfo.getTrackName());
        helper.setText(R.id.distance, trackInfo.getTotalDis() + "km");
        helper.setText(R.id.time, DateUtil.parseDateLineToBlurStr(trackInfo.getStartTime() * 1000));
        SimpleDraweeView trackImage = helper.getView(R.id.track_image);
        if (!TextUtils.isEmpty(trackInfo.getImageUrl())) {
            trackImage.setImageURI(Uri.parse(TrackManager.getBaiduUrlByDes(trackInfo.getImageUrl(), 720, 200)));
        } else {
            TrackManager.vacuate(trackInfo.getTrackUUID());
        }
    }


}
