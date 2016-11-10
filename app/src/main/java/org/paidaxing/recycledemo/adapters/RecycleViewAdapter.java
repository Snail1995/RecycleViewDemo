package org.paidaxing.recycledemo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.paidaxing.recycledemo.R;
import org.paidaxing.recycledemo.model.Item;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XIAOHONG
 * Author: XIAOHONG.
 * Date: 2016/11/9.
 * Email ${EMAIL}
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

    private List<Item> mItems;
    private Context mContext;
    private OnViewItemClickListener mItemClickListener;
    private List<Integer> heights;

    public RecycleViewAdapter(List<Item> items, Context context) {
        mItems = items;
        mContext = context;
        getRandomHeight(this.mItems);
    }


    public interface OnViewItemClickListener {
        void setOnClickListener(View view, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ret = null;
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recomment_3, parent, false);
        ret = new ViewHolder(view);
        return ret;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Item item = mItems.get(position);

        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();//得到item的LayoutParams布局参数
        params.height = heights.get(position);//把随机的高度赋予itemView布局
        holder.itemView.setLayoutParams(params);//把params设置给itemView布局

        holder.mNameTxt.setText(item.getName());
        Glide.with(mContext)
                .load(item.getImageUrl())
                // 占位图
                .placeholder(R.mipmap.ic_tree)
                // 加载错误图
                .error(R.mipmap.ic_tree)
//                让Glide既缓存全尺寸又缓存其他尺寸
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.mImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.setOnClickListener(v, position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (mItems != null) {
            ret = mItems.size();
        }
        return ret;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_frag_recommend_image)
        ImageView mImage;
        @BindView(R.id.item_frag_recommend_name)
        TextView mNameTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setItemClickListener(OnViewItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    private void getRandomHeight(List<Item> lists) {//得到随机item的高度
        heights = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {
            heights.add((int) (300 + Math.random() * 400));
        }
    }
}
