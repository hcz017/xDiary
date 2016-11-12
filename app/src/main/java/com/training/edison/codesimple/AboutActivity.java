package com.training.edison.codesimple;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AboutActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.about_activity);
        // add back arrow to toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ImageButton imgGit = (ImageButton) findViewById(R.id.github);
        ImageButton imgWeibo = (ImageButton) findViewById(R.id.weibo);
        ImageButton imgZhihu = (ImageButton) findViewById(R.id.zhihu);
        imgGit.setOnClickListener(this);
        imgWeibo.setOnClickListener(this);
        imgZhihu.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zhihu:
                Intent intentZhihu = new Intent(Intent.ACTION_VIEW);
                intentZhihu.setData(Uri.parse("http://www.zhihu.com/people/shi-cheng-16-47"));
                startActivity(intentZhihu);
                break;
            case R.id.weibo:
                Intent intentWeibo = new Intent(Intent.ACTION_VIEW);
                intentWeibo.setData(Uri.parse("http://weibo.com/hcz10"));
                startActivity(intentWeibo);
                break;
            case R.id.github:
                Intent intentGit = new Intent(Intent.ACTION_VIEW);
                intentGit.setData(Uri.parse("http://github.com/hcz017"));
                startActivity(intentGit);
                break;
            default:
        }
    }
}
