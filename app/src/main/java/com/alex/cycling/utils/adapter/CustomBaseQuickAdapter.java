package com.alex.cycling.utils.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alex.cycling.R;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * Created by zhong on 2016/8/9.
 */
public abstract class CustomBaseQuickAdapter<T> extends BaseQuickAdapter<T> {

    private View notLoadingView;

    public CustomBaseQuickAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }

    public void showNotLoading(RecyclerView mRecyclerView) {  //底部没有数据时显示
        if (mRecyclerView == null) {
            return;
        }
        notifyDataChangedAfterLoadMore(false);
        if (notLoadingView == null) {
            notLoadingView = LayoutInflater.from(mRecyclerView.getContext()).inflate(R.layout.not_loading, (ViewGroup) mRecyclerView.getParent(), false);
        }
        addFooterView(notLoadingView);
    }

    public void showCommonEmptyView(RecyclerView mRecyclerView) {  //通用的空数据视图
        if (mRecyclerView == null) {
            return;
        }
        setEmptyView(LayoutInflater.from(mRecyclerView.getContext()).inflate(R.layout.not_loading, (ViewGroup) mRecyclerView.getParent(), false));
    }

    public void showCommonLoadingView(RecyclerView mRecyclerView) {  //通用的加载视图
        if (mRecyclerView == null) {
            return;
        }
        setLoadingView(LayoutInflater.from(mRecyclerView.getContext()).inflate(R.layout.cmm_load_more, (ViewGroup) mRecyclerView.getParent(), false));
    }

}
