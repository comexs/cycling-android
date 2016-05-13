package com.alex.cycling.ui.camera.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.cycling.R;
import com.alex.cycling.ui.camera.bean.Addon;
import com.alex.cycling.ui.camera.util.FilterEffect;
import com.alex.cycling.ui.camera.util.GPUImageFilterTools;
import com.alex.cycling.utils.LogUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * 贴纸适配器
 *
 * @author tongqian.ni
 */
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    List<FilterEffect> filterUris;
    private Bitmap background;
    private int selectFilter = 0;
    OnItemClickListener listener;

    public FilterAdapter(List<FilterEffect> effects, Bitmap backgroud) {
        filterUris = effects;
        this.background = backgroud;
    }

    @Override
    public int getItemCount() {
        return filterUris.size();
    }

    public void setSelectFilter(int selectFilter) {
        this.selectFilter = selectFilter;
    }

    public int getSelectFilter() {
        return selectFilter;
    }

    @Override
    public long getItemId(int position) {
        return position + 100;
    }

    @Override
    public FilterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bottom_filter, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FilterEffect effect = filterUris.get(position);
        holder.filteredImg.setImage(background);
        holder.filterName.setText(effect.getTitle());
        GPUImageFilter filter = GPUImageFilterTools.createFilterForType(holder.filteredImg.getContext(), effect.getType());
        holder.filteredImg.setFilter(filter);
        holder.filteredImg.setTag(position);
        holder.filteredImg.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null != listener) {
                if (null == v.getTag() || TextUtils.isEmpty(v.getTag().toString())) return;
                listener.onItemCick(Integer.parseInt(v.getTag().toString()));
            }
        }
    };

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public void setItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemCick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Bind(R.id.small_filter)
        GPUImageView filteredImg;
        @Bind(R.id.filter_name)
        TextView filterName;
    }

}
