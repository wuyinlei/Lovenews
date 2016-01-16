package com.example.lovenews.bean;

import java.util.List;

/**
 * Created by 若兰 on 2016/1/16.
 * 一个懂得了编程乐趣的小白，希望自己
 * 能够在这个道路上走的很远，也希望自己学习到的
 * 知识可以帮助更多的人,分享就是学习的一种乐趣
 * QQ:1069584784
 * csdn:http://blog.csdn.net/wuyinlei
 */

public class NewsData {

    /**
     * 字段名字必须和服务器返回的字段名一致，方便gson解析
     * retcode: 200,
     * data: [],
     * extend: []
     */
    public int retcode;

    public List<NewsMenuData> data;

    //public List extend;


    /**
     * 侧边栏数据对象
     *
     * id: 10002,
     * title: "专题",
     * type: 10,
     * url: "/10006/list_1.json",
     * url1: "/10007/list1_1.json"
     */
    public class NewsMenuData {

        public String id;
        public String title;
        public int type;
        public String url;

        public List<NewsTabData> children;

        @Override
        public String toString() {
            return "NewsMenuData{" +
                    "title='" + title + '\'' +
                    ", children=" + children +
                    '}';
        }
    }

    /**
     * 新闻标签页11个数据对象
     *
     * id: 10007,
     * title: "北京",
     * type: 1,
     * url: "/10007/list_1.json"
     */
    public class NewsTabData {
        public String id;
        public String title;
        public int type;
        public String url;

        @Override
        public String toString() {
            return "NewsTabData{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", type=" + type +
                    ", url='" + url + '\'' +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "NewsData{" +
                "data=" + data +
                '}';
    }
}
