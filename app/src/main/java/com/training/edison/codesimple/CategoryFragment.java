package com.training.edison.codesimple;

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

public class CategoryFragment extends Fragment {

    private ListView listView;
    private final String URL = "http://m.xiachufang.com/search/?keyword=茄子+土豆";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_category, container, false);
        listView = (ListView) parentView.findViewById(R.id.listView);
        new Thread(runnable).start();
        //initView();
        return parentView;
    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.i("connect().get()", val);
            initView();
        }
    };
    private final ArrayList<String> arr = new ArrayList<>();

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Document doc;
            try {
                //从URL加载Document
                doc = Jsoup.connect(URL).get();
                //使用DOM方法来遍历文档并抽取元素
                Elements title = doc.getElementsByAttributeValue("class", "content pl15");
                for (Element element : title) {
                    Elements links = element.getElementsByTag("header");
                    //其实此处也可以不用写for (Element link : links)
                    //因为按照("class","title")抽取的title，每组中只含有一组href和text
                    //示例：<h1 class="title"><a href="/post/android/2014-09-30.kai-fa-ben-bo-ke-de-ke-hu-duan-er">开发本博客的客户端（二）获取网页内容</a></h1>
                    for (Element link : links) {
                        String linkHref = link.attr("class");
                        String linkText = link.text().trim();
                        arr.add(linkText);
                        System.out.println("link：" + linkHref);
                        System.out.println("words：" + linkText);
                    }
                }
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("value", title.toString());
                msg.setData(data);
                handler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private void initView() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                arr);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "Clicked item!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
