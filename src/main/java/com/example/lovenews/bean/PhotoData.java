package com.example.lovenews.bean;

import java.util.List;

/**
 * Created by 若兰 on 2016/1/19.
 * 一个懂得了编程乐趣的小白，希望自己
 * 能够在这个道路上走的很远，也希望自己学习到的
 * 知识可以帮助更多的人,分享就是学习的一种乐趣
 * QQ:1069584784
 * csdn:http://blog.csdn.net/wuyinlei
 */

public class PhotoData {
    public int retcode;
    public PhotosInfo data;

    public class PhotosInfo {
        public String title;
        public List<PhotoInfo> news;
    }

    public class PhotoInfo {
        public String id;
        public String listimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;
    }
}
