package com.example.lovenews.base.menudetail;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lovenews.R;
import com.example.lovenews.base.BaseMenuDetailPager;
import com.example.lovenews.bean.PhotoData;
import com.example.lovenews.contants.Contants;
import com.example.lovenews.utils.CacheUtils;
import com.example.lovenews.utils.bitmap.MyBitMapUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 若兰 on 2016/1/16.
 * 一个懂得了编程乐趣的小白，希望自己
 * 能够在这个道路上走的很远，也希望自己学习到的
 * 知识可以帮助更多的人,分享就是学习的一种乐趣
 * QQ:1069584784
 * csdn:http://blog.csdn.net/wuyinlei
 */

public class PhotoMenuDetailPager extends BaseMenuDetailPager implements View.OnClickListener {

    private ListView mListView;

    private GridView mGridView;
    private View view;

    private String url = Contants.PHOTO_URL;
    private PhotoData mPhotoData;
    private List<PhotoData.PhotoInfo> mPhotoLists;
    private PhotoAdapter mAdapter;
    private ImageView ivPic;

    public PhotoMenuDetailPager(Activity activity,ImageView ivPic) {
        super(activity);
        this.ivPic = ivPic;
        ivPic.setOnClickListener(this);
    }

    @Override
    public View initViews() {

        view = View.inflate(mActivity, R.layout.photo_menu_pager, null);
        mListView = (ListView) view.findViewById(R.id.lv_list);
        mGridView = (GridView) view.findViewById(R.id.gv_list);

        return view;
    }

    @Override
    public void initData() {
        /**
         * 先看看是否有缓存
         */
        String cache = CacheUtils.getCache(url, mActivity);
        if (!TextUtils.isEmpty(cache)) {

        }

        getDataFromServer();
    }

    /**
     * 从服务器获取数据
     */
    private void getDataFromServer() {

        new Thread() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String result = response.body().string();
                            parseData(result);
                            CacheUtils.setCache(url, result, mActivity);
                        }
                    }
                });

            }
        }.start();

/*
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                parseData(result);

                CacheUtils.setCache(url, result, mActivity);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
            }
        });
   */
    }
    /**
     * 解析数据
     * @param result
     */
    private void parseData(String result) {
        Gson gson = new Gson();
        /**
         * 解析photo数据
         */
        mPhotoData = gson.fromJson(result, PhotoData.class);

        /**
         * 获取组图列表集合
         */
        mPhotoLists = mPhotoData.data.news;

        Message message = Message.obtain();
        message.obj = mPhotoLists;
        mHandler.sendMessage(message);


    }

    /**
     * 在这里需要异步处理
     */
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<PhotoData.PhotoInfo> mPhotos= (List<PhotoData.PhotoInfo>) msg.obj;
            /**
             * 获取的组图列表如果不为空
             */
            if (mPhotos!=null) {
                mAdapter = new PhotoAdapter();

                mListView.setAdapter(mAdapter);

                mGridView.setAdapter(mAdapter);
            }
        }
    };

    @Override
    public void onClick(View v) {
        changeViews();
    }

    private boolean is_list_display = true;//是否是列表展示

    private void changeViews(){
        if (is_list_display){
            mListView.setVisibility(View.VISIBLE);
            mGridView.setVisibility(View.INVISIBLE);
            ivPic.setImageResource(R.mipmap.icon_pic_grid_type);
            is_list_display = false;
        } else {
            ivPic.setImageResource(R.mipmap.icon_pic_list_type);
            mListView.setVisibility(View.INVISIBLE);
            mGridView.setVisibility(View.VISIBLE);
            is_list_display = true;
        }
    }

    /**
     * ListView的适配器
     */
    class PhotoAdapter extends BaseAdapter {

        //private BitmapUtils mBitmapUtils;

        private MyBitMapUtils utils;
        public PhotoAdapter() {
          /*  mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.mipmap.pic_item_list_default);*/
            utils = new MyBitMapUtils();
        }

        @Override
        public int getCount() {
            return mPhotoLists.size();
        }

        @Override
        public PhotoData.PhotoInfo getItem(int position) {
            return mPhotoLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null){
                holder = new ViewHolder();
                convertView = View.inflate(mActivity,R.layout.photo_item_list,null);
                holder.tvImage = (ImageView) convertView.findViewById(R.id.ivImage);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            PhotoData.PhotoInfo photoInfo = getItem(position);
            holder.tvTitle.setText(photoInfo.title);
            utils.display(holder.tvImage,photoInfo.listimage);

            return convertView;
        }
    }

    static class ViewHolder{
        TextView tvTitle;
        ImageView tvImage;
    }
}
