package com.training.hcz017.xdiary.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.training.hcz017.xdiary.R;
import com.training.hcz017.xdiary.article.ArticleBean;
import com.training.hcz017.xdiary.utils.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private RecyclerView mRecyclerView;
    private List<ArticleBean> articleBeanList = new ArrayList<>();

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_home, container, false);
        MyAsyncTask mAsyncTask = new MyAsyncTask();
        mAsyncTask.execute();
        mRecyclerView = parentView.findViewById(R.id.my_recycler_view);
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
                doc = Jsoup.connect(Utils.BLOG_URL).get();
                //使用DOM方法来遍历文档，并抽取元素
                //每一篇文章
                Elements elements = doc.select("div.content");
                for (Element element : elements) {
                    String title = element.select("h1.title").text().trim();//标题
                    String link = element.select("h1.title").select("a").attr("abs:href"); //链接
                    String time = element.select("span.pub_date").text();//时间
                    time = Utils.formatDate(time);
                    String imgUrl="";
                    Elements articleContent = element.select("div.p_part");
                    final Pattern pattern = Pattern.compile(Utils.IMG_SRC_REG);
                    final Matcher matcher = pattern.matcher(articleContent.toString());
                    if (matcher.find()){
                        imgUrl = matcher.group();
                    }

                    Log.d(TAG, "run: title: " + title);
                    Log.d(TAG, "run: link: " + link);
                    Log.d(TAG, "run: time: " + time);
                    Log.d(TAG, "run: imgUrl: " + imgUrl);
                    String content = (articleContent.select("p").toString()).replaceAll("<p>", "")
                            .replaceAll("</p>", "").replaceAll(Utils.IMG_URL_REG, "[图片]");
                    articleBeanList.add(new ArticleBean(title, time, content, link, imgUrl));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return articleBeanList;
        }

        protected void onPostExecute(List<ArticleBean> result) {
            //创建并设置Adapter
            ArticleAdapter mAdapter = new ArticleAdapter(articleBeanList);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
