package com.training.edison.codesimple;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Bundle;

import com.mcxtzhang.pathanimlib.PathAnimView;
import com.mcxtzhang.pathanimlib.utils.SvgPathParser;

import java.text.ParseException;

public class RoseActivity extends Activity {
    PathAnimView storeView3;

    //SVG转-》path tools
    SvgPathParser svgPathParser = new SvgPathParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rose);

        storeView3 = (PathAnimView) findViewById(R.id.rose);

        drawRose();
    }

    public void drawRose() {
        Path path = new Path();
        String roseLine1 = getResources().getString(R.string.rose400_1);
        String roseLine2 = getResources().getString(R.string.rose400_2);
        String roseLines = roseLine1 + roseLine2;
        try {
            path = svgPathParser.parsePath(roseLines);
            storeView3.setSourcePath(path);
            storeView3.setColorBg(Color.TRANSPARENT).setColorFg(Color.RED);
            storeView3.setAnimInfinite(false).setAnimTime(2500).startAnim();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Animator animator = storeView3.getPathAnimHelper().getAnimator();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

        });
    }
}
