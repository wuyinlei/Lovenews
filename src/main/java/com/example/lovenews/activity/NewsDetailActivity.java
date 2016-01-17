package com.example.lovenews.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.lovenews.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class NewsDetailActivity extends AppCompatActivity {

    @ViewInject(R.id.ivBack)
    private ImageView ivBack;

    @ViewInject(R.id.ivShare)
    private ImageView ivShare;

    @ViewInject(R.id.tvSize)
    private ImageView ivSize;

    @ViewInject(R.id.webView)
    private WebView mWebView;

    @ViewInject(R.id.progressBar)
    private ProgressBar progressBar;


    String[] items = new String[]{"超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ViewUtils.inject(this);
        String url = getIntent().getStringExtra("url");

        WebSettings settings = mWebView.getSettings();

        //显示放大缩小按钮
        settings.setBuiltInZoomControls(true);
        //表示支持JS
        settings.setJavaScriptEnabled(true);
        //支持双击缩放
        settings.setUseWideViewPort(true);

        mWebView.setWebViewClient(new WebViewClient() {

            /**
             * 网页开始加载
             * @param view
             * @param url
             * @param favicon
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
            }

            /**
             * 网页加载结束
             * @param view
             * @param url
             */
            @Override
            public void onPageFinished(WebView view, String url) {

            }

            /**
             * 所有的跳转的连接都在此方法中回调
             * @param view
             * @param url
             * @return
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                //view.loadUrl(url);

                return true;
            }
        });

        // mWebView.goBack();

        mWebView.setWebChromeClient(new WebChromeClient() {

            /**
             * 网页加载进度
             * @param view
             * @param newProgress
             */
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 90) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
                Log.d("NewsDetailActivity", "newProgress:" + newProgress);
            }

            /**
             * 获取网页标题
             * @param view
             * @param title
             */
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });

        mWebView.loadUrl(url);
    }

    @OnClick({R.id.ivBack, R.id.tvSize, R.id.ivShare})
    public void onClickItem(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvSize:
                showChooseDialog();
                break;
            case R.id.ivShare:
                break;
        }
    }

    private int mCurrentChooseItem;//记录当前选中的item   点击确定前

    private int mCurrentItem = 2;//选中的item   点击确定后

    /**
     * 显示修改字体的对话框
     */
    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("字体设置");
        builder.setSingleChoiceItems(items, mCurrentItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCurrentChooseItem = which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WebSettings settings = mWebView.getSettings();
                switch (mCurrentChooseItem) {
                    case 0:
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    case 1:
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                }
                mCurrentItem = mCurrentChooseItem;
            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
    }
}
