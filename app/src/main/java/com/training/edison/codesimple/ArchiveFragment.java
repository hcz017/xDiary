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

public class ArchiveFragment extends Fragment {

    private static final String TAG = "ArchiveFragment";
    private ListView listView;
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
                    ArrayList<String> arr = (ArrayList<String>) msg.obj;
                    initView(arr);
                    break;
                default:
            }
        }
    };

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ArrayList<String> arr = new ArrayList<>();
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
                    arr.add(title);
                }
                Message message = Message.obtain();
                message.what = GET_ARCHIVE_DONE;
                message.obj = arr;
                handler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private void initView(ArrayList<String> arr) {
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
