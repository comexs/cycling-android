package com.alex.cycling.ui.adapter;

import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.cycling.R;
import com.alex.cycling.service.TrackManager;
import com.alex.cycling.utils.DateUtil;
import com.alex.cycling.utils.LogUtils;
import com.alex.cycling.utils.adapter.SimpleAdapter;
import com.alex.cycling.utils.adapter.ViewUtils;
import com.alex.greendao.TrackInfo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by comexs on 16/3/20.
 */
public class RecordAdapter extends SimpleAdapter<TrackInfo, RecordAdapter.ViewHolder> {

    public RecordAdapter(List<TrackInfo> list) {
        super(list);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ViewUtils.inflate(R.layout.item_record, parent));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TrackInfo trackInfo = getItem(position);
        holder.trackName.setText(trackInfo.getTrackUUID());
        holder.time.setText(DateUtil.parseDateLineToBlurStr(trackInfo.getStartTime() * 1000));
        if (!TextUtils.isEmpty(trackInfo.getImageUrl())) {
            holder.trackImage.setImageURI(Uri.parse(TrackManager.getBaiduUrlByDes(trackInfo.getImageUrl(), 100, 100)));
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.track_image)
        SimpleDraweeView trackImage;
        @Bind(R.id.track_name)
        public TextView trackName;
        @Bind(R.id.time)
        public TextView time;
        @Bind(R.id.distance)
        public TextView distance;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
