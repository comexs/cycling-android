package com.alex.cycling.ui.camera.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alex.cycling.R;
import com.alex.cycling.ui.camera.bean.Addon;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 贴纸适配器
 *
 * @author tongqian.ni
 */
public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {

    List<Addon> filterUris;
    OnItemClickListener listener;

    public StickerAdapter(List<Addon> datats) {
        filterUris = datats;
    }

    @Override
    public int getItemCount() {
        return filterUris.size();
    }

    @Override
    public StickerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bottom_tool, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Addon sticker = filterUris.get(position);
        holder.container.setVisibility(View.GONE);
//        holder.logo.setImageResource(sticker.getId());
//        holder.logo.setTag(position);
//        holder.logo.setOnClickListener(clickListener);
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

        @BindView(R.id.effect_background)
        ImageView container;
//        @Bind(R.id.effect_image)
//        SimpleDraweeView logo;
    }

}
