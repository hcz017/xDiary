package com.training.edison.codesimple;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private static final String TAG = "HomeFragment";
    private String mBlogUrl = "http://tenthorange.farbox.com/";
    private List<ArticleBean> articleBeanList = new ArrayList<>();

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_home, container, false);
        MyAsyncTask mAsyncTask = new MyAsyncTask();
        mAsyncTask.execute();
        mRecyclerView = (RecyclerView) parentView.findViewById(R.id.my_recycler_view);
        //创建默认的线性LayoutManager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        return parentView;
    }

    private class MyAsyncTask extends AsyncTask<Object, Object, List<ArticleBean>> {

        protected List<ArticleBean> doInBackground(Object... urls) {
            Document doc;
            try {
                //URL加载一个Document
                doc = Jsoup.connect(mBlogUrl).get();
                //使用DOM方法来遍历文档，并抽取元素
                //每一篇文章
                Elements elements = doc.select("div.content");
                for (Element element : elements) {
                    String title = element.select("h1.title").text().trim();//标题
                    String link = element.select("h1.title").select("a").attr("abs:href"); //链接
                    String time = element.select("span.pub_date").text();//时间
                    time = formatDate(time);
                    Elements articleContent = element.select("div.p_part");
                    Log.i(TAG, "run: title: " + title);
                    Log.i(TAG, "run: link: " + link);
                    Log.i(TAG, "run: time: " + time);
                    String content = (articleContent.select("p") + "").replaceAll("<p>", "")
//                            .replaceAll("</p>", "");
                            .replaceAll("</p>", "").replaceAll("[<].*[>]", "[图片]");
                    Log.i(TAG, "run: articleContent.attr:\n" + content);
                    articleBeanList.add(new ArticleBean(title, time, content, link));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return articleBeanList;
        }

        protected void onPostExecute(List<ArticleBean> result) {
            //创建并设置Adapter
            MyAdapter mAdapter = new MyAdapter(articleBeanList);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    //格式化时间
    public String formatDate(String date) {
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        SimpleDateFormat transToDate = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
        String localDate = null;
        try {
            //String转换成英文date，再转换成本机时间格式（String）
            localDate = dateFormat.format(transToDate.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return localDate;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private final List<ArticleBean> mArticleBeanList;

        public MyAdapter(List<ArticleBean> articleBeanList) {
            mArticleBeanList = articleBeanList;
        }

        //创建新View，被LayoutManager所调用
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item, viewGroup, false);
            return new ViewHolder(view);
        }

        //将数据与界面进行绑定的操作
        @Override
        public void onBindViewHolder(final MyAdapter.ViewHolder viewHolder, int position) {
            viewHolder.mAtTitle.setText(mArticleBeanList.get(position).getTitle());
            viewHolder.mAtTime.setText(mArticleBeanList.get(position).getTime());
            viewHolder.mAtContent.setText(mArticleBeanList.get(position).getContent());

            viewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getAdapterPosition();
                    String link = mArticleBeanList.get(position).getLink();
                    String title = mArticleBeanList.get(position).getTitle();
                    Intent articlePost = new Intent(getActivity(), ArticleActivity.class);
                    articlePost.putExtra(ArticleBean.LINK, link);
                    articlePost.putExtra(ArticleBean.TITLE,title);
                    startActivity(articlePost);
                }
            });
        }

        //获取数据的数量
        @Override
        public int getItemCount() {
            return mArticleBeanList.size();
        }

        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public class ViewHolder extends RecyclerView.ViewHolder {
            public final CardView mCardView;
            public final TextView mAtTitle;
            public final TextView mAtContent;
            public final TextView mAtTime;

            ViewHolder(View view) {
                super(view);
                mCardView = (CardView) view.findViewById(R.id.card_view);
                mAtTitle = (TextView) view.findViewById(R.id.at_title);
                mAtTime = (TextView) view.findViewById(R.id.at_time);
                mAtContent = (TextView) view.findViewById(R.id.at_content);
            }
        }
    }
}
