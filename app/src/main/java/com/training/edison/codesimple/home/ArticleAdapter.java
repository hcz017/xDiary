package com.training.edison.codesimple.home;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.training.edison.codesimple.R;
import com.training.edison.codesimple.artical.ArticleActivity;
import com.training.edison.codesimple.artical.ArticleBean;

import java.util.List;

class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private final List<ArticleBean> mArticleBeanList;

    public ArticleAdapter(List<ArticleBean> articleBeanList) {
        mArticleBeanList = articleBeanList;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        // 设置点击事件监听
        viewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                String link = mArticleBeanList.get(position).getLink();
                String title = mArticleBeanList.get(position).getTitle();
                ArticleActivity.startActivity(v.getContext(), link, title);
            }
        });
        return viewHolder;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ArticleAdapter.ViewHolder viewHolder, int position) {
        ArticleBean articleBean = mArticleBeanList.get(position);
        viewHolder.mAtTitle.setText(articleBean.getTitle());
        viewHolder.mAtTime.setText(articleBean.getTime());
        viewHolder.mAtContent.setText(articleBean.getContent());
        String imgUrl = articleBean.getImgUrl();
        if (imgUrl != null && !imgUrl.isEmpty()) {
            Picasso.get().load(imgUrl).into(viewHolder.mAtImage);
            viewHolder.mAtImage.setVisibility(View.VISIBLE);
        }
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return mArticleBeanList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    class ViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        TextView mAtTitle;
        TextView mAtContent;
        TextView mAtTime;
        ImageView mAtImage;

        ViewHolder(View view) {
            super(view);
            mCardView = view.findViewById(R.id.card_view);
            mAtTitle = view.findViewById(R.id.at_title);
            mAtTime = view.findViewById(R.id.at_time);
            mAtContent = view.findViewById(R.id.at_content);
            mAtImage = view.findViewById(R.id.at_img);
        }
    }
}