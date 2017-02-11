package com.training.edison.codesimple;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArchiveFragment extends Fragment {

    private static final String TAG = "ArchiveFragment";
    private ListView listView;
    private List<ArticleBean> articleBeanList = new ArrayList<>();
    private static final int GET_ARCHIVE_DONE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_archive, container, false);
        listView = (ListView) parentView.findViewById(R.id.listView);
        new Thread(runnable).start();
        return parentView;
    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_ARCHIVE_DONE:
                    List mArticleBean = (List) msg.obj;
                    initView(mArticleBean);
                    break;
                default:
            }
        }
    };

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Document doc;
            try {
                //URL加载一个Document
                doc = Jsoup.connect("http://tenthorange.farbox.com/archive").get();
                //使用DOM方法来遍历文档，并抽取元素
                Elements items = doc.select("li.listing_item");
                for (Element item : items) {
                    String title = item.select("a").text().trim();
                    String link = item.select("a").attr("abs:href");
                    Log.d(TAG, "run: title: " + title);
                    Log.d(TAG, "run: link: " + link);
                    articleBeanList.add(new ArticleBean(title, link));
                }
                Message message = Message.obtain();
                message.what = GET_ARCHIVE_DONE;
                message.obj = articleBeanList;
                handler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private void initView(final List<ArticleBean> articleBeanList) {
        //这里代码写的超烂,我不知道怎么从一个List里面提取出ArrayList!
        ArrayList<String> arr = new ArrayList<>();
        for (int i = 0; i < articleBeanList.size(); i++) {
            String title = articleBeanList.get(i).getTitle();
            arr.add(title);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                arr);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(getActivity(), "Clicked item!", Toast.LENGTH_LONG).show();
                String link = articleBeanList.get(position).getLink();
                String title = articleBeanList.get(position).getTitle();
                Intent articlePost = new Intent(getActivity(), ArticleActivity.class);
                articlePost.putExtra(ArticleBean.LINK, link);
                articlePost.putExtra(ArticleBean.TITLE, title);
                startActivity(articlePost);
            }
        });
    }
}
