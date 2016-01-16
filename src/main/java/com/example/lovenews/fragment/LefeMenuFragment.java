package com.example.lovenews.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lovenews.R;
import com.example.lovenews.activity.MainActivity;
import com.example.lovenews.base.impl.NewsCenterPager;
import com.example.lovenews.bean.NewsData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by 若兰 on 2016/1/15.
 * 一个懂得了编程乐趣的小白，希望自己
 * 能够在这个道路上走的很远，也希望自己学习到的
 * 知识可以帮助更多的人,分享就是学习的一种乐趣
 * QQ:1069584784
 * csdn:http://blog.csdn.net/wuyinlei
 */

public class LefeMenuFragment extends BaseFrament {

    @ViewInject(R.id.lv_list)
    private ListView lv_list;
    private List<NewsData.NewsMenuData> mMenuLists;

    private int mCurrentPos;//当前被点击的对象
    private MenuAdapter mMenuAdapter;

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initData() {

        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos = position;
                mMenuAdapter.notifyDataSetChanged();
                setCurrentMenuDetailPager(position);

                /**
                 * 隐藏SlidingMenu
                 */
                toggleSlidingMenu();
            }
        });
    }

    /**
     * 切换SlidingMenu的状态
     * 表示是不是要显示SlidingMenu
     */
    public void toggleSlidingMenu() {
        MainActivity mainUi = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUi.getSlidingMenu();
        slidingMenu.toggle();    //切换状态   显示的时候隐藏，影藏是显示
    }

    /**
     * 设置当前菜单详情页
     *
     * @param position
     */
    private void setCurrentMenuDetailPager(int position) {
        MainActivity mainUi = (MainActivity) mActivity;
        //获取主页面fragment
        ContentFragment contentFragment = mainUi.getRightFragmentTag();
        //获取新闻中心页面
        NewsCenterPager centerPager = contentFragment.getNewsCenterPager();
        //获取当前详情页
        centerPager.setCurrentMenuDetailPager(position);
    }

    /**
     * 设置解析的数据
     */
    public void setMenuData(NewsData newsData) {
        //Log.d("LefeMenuFragment", "侧边栏拿到数据" + newsData);
        mMenuLists = newsData.data;
        mMenuAdapter = new MenuAdapter();
        lv_list.setAdapter(mMenuAdapter);

    }

    /**
     * 侧边栏干数据适配器
     */
    class MenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mMenuLists.size();
        }

        @Override
        public Object getItem(int position) {
            return mMenuLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = View.inflate(mActivity, R.layout.list_menu_item, null);
            TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            NewsData.NewsMenuData menuData = mMenuLists.get(position);
            tvTitle.setText(menuData.title);

            if (mCurrentPos == position)//当前绘制的view是否被选中
            {
                //显示红色
                tvTitle.setEnabled(true);
            } else {
                //显示白色
                tvTitle.setEnabled(false);
            }
            return view;
        }
    }
}
