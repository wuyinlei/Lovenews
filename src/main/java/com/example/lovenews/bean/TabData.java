package com.example.lovenews.bean;

import java.util.List;

/**
 * Created by 若兰 on 2016/1/17.
 * 一个懂得了编程乐趣的小白，希望自己
 * 能够在这个道路上走的很远，也希望自己学习到的
 * 知识可以帮助更多的人,分享就是学习的一种乐趣
 * QQ:1069584784
 * csdn:http://blog.csdn.net/wuyinlei
 */

public class TabData {

    public int retcode;

    public TabDetail data;

    public class TabDetail {
        public String title;
        public String more;
        public List<TabNewsData> news;
        public List<TopNewsData> topnews;

        @Override
        public String toString() {
            return "TabDetail{" +
                    "title='" + title + '\'' +
                    ", news=" + news +
                    ", topnews=" + topnews +
                    '}';
        }
    }

    /**
     * 新闻列表
     */
    public class TabNewsData {
        public String id;
        public String listimage;
        public String title;
        public String pubdate;
        public String type;
        public String url;

        @Override
        public String toString() {
            return "TabNewsData{" +
                    "title='" + title + '\'' +
                    ", id='" + id + '\'' +
                    '}';
        }
    }

    /**
     * 头条新闻
     */
    public class TopNewsData {
        public String id;
        public String topimage;
        public String title;
        public String pubdate;
        public String type;
        public String url;

        @Override
        public String toString() {
            return "TopNewsData{" +
                    "title='" + title + '\'' +
                    ", id='" + id + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TabData{" +
                "retcode=" + retcode +
                ", data=" + data +
                '}';
    }
}
