package com.example.lovenews.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.lovenews.R;

/**
 * Created by 若兰 on 2016/1/11.
 * 一个懂得了编程乐趣的小白，希望自己
 * 能够在这个道路上走的很远，也希望自己学习到的
 * 知识可以帮助更多的人,分享就是学习的一种乐趣
 * QQ:1069584784
 * csdn:http://blog.csdn.net/wuyinlei
 */

public abstract class BaseActivity extends AppCompatActivity {

    private RelativeLayout layoutId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);
        layoutId = (RelativeLayout) findViewById(R.id.rlContent);
        //IOC  控制反转
        View view = getLayoutInflater().inflate(getLayoutId(),layoutId,false);
        layoutId.addView(view);
    }

    /**
     * 利用控制反转
     * @return
     */
    public abstract int getLayoutId();
}
