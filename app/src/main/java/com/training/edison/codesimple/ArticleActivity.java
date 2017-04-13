package com.training.edison.codesimple;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.WebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class ArticleActivity extends AppCompatActivity {

    private static final String TAG = "ArticleActivity";
    private String mTitle = null;
    private String mLink = null;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mWebView = (WebView) findViewById(R.id.web_view);
        mTitle = getIntent().getStringExtra(ArticleBean.TITLE);
        mLink = getIntent().getStringExtra(ArticleBean.LINK);
        this.setTitle(mTitle);
        Log.i(TAG, "onCreate: link " + mLink);
        MyAsyncTask mAsyncTask = new MyAsyncTask();
        mAsyncTask.execute();
    }

    private class MyAsyncTask extends AsyncTask<Object, Object, String> {

        protected String doInBackground(Object... urls) {
            Document doc;
            String postBody = null;
            try {
                doc = Jsoup.connect(mLink).get();
                postBody = doc.select("div.post_body").toString();
                postBody = postBody.replaceAll("/_image", "http://tenthorange.farbox.com/_image");
                Log.i(TAG, "doInBackground: elements.toString\n" + postBody);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return postBody;
        }

        protected void onPostExecute(String result) {
            int bgIndex = (int) (Math.random() * 10 % 5);
            int resId[] = {R.drawable.bg_0, R.drawable.bg_1, R.drawable.bg_2, R.drawable.bg_3,
                    R.drawable.bg_4};
            findViewById(R.id.img_bg).setBackgroundResource(resId[bgIndex]);
            mWebView.loadDataWithBaseURL("x-data://base", result, "text/html", "utf-8", null);
        }
    }
}
