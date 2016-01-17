package com.example.lovenews.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lovenews.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 若兰 on 2016/1/17.
 * 一个懂得了编程乐趣的小白，希望自己
 * 能够在这个道路上走的很远，也希望自己学习到的
 * 知识可以帮助更多的人,分享就是学习的一种乐趣
 * QQ:1069584784
 * csdn:http://blog.csdn.net/wuyinlei
 */

public class RefreshListView extends ListView implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    /**
     * 下拉刷新
     */
    private static final int STATE_PULL_REFRESH = 0;

    /**
     * 松开刷新
     */
    private static final int STATE_RELEASE_REFRESH = 1;

    /**
     * 正在刷新
     */
    private static final int STATE_REFRESHING = 2;

    /**
     * 当前刷新状态
     */
    private static int CURRENT_STATE = STATE_PULL_REFRESH;


    private float startY;

    /**
     * 头布局
     */
    private View mHeaderView;

    /**
     * 尾布局
     */
    private View mFooterView;

    /**
     * 头布局高度
     */
    private int measuredHeight;

    /**
     * 尾布局高度
     */
    private int mFotterViewHeight;

    /**
     * 布局控件
     */
    private ImageView ivImage;
    private ProgressBar progressBar;
    private TextView tvTitle, tvData;

    /**
     * 箭头动画
     */
    private RotateAnimation animUp, animDown;



    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);

        this.addHeaderView(mHeaderView);

        initViews();

        initAnimation();
        //测量布局
        mHeaderView.measure(0, 0);

        //获得HeaderView的高度，这个要在测量(measure)之后才能获取到
        measuredHeight = mHeaderView.getMeasuredHeight();

        //隐藏HeaderView
        mHeaderView.setPadding(0, -measuredHeight, 0, 0);
    }

    /**
     * 初始化布局控件
     */
    private void initViews() {
        tvData = (TextView) mHeaderView.findViewById(R.id.tvData);
        tvData.setText("最后刷新时间:" + getCurrentTime());
        tvTitle = (TextView) mHeaderView.findViewById(R.id.tvTitle);
        progressBar = (ProgressBar) mHeaderView.findViewById(R.id.progressBar);
        ivImage = (ImageView) mHeaderView.findViewById(R.id.ivImage);

    }

    /**
     * 点击触摸事件
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                /**
                 * 获取到按下的时候的坐标
                 */
                startY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {
                    startY = ev.getRawY();
                }

                if (CURRENT_STATE == STATE_REFRESHING) {
                    break;//正在刷新的时候，不做处理
                }
                /**
                 * 获取到移动的时候的坐标
                 */
                float endY = ev.getRawY();

                /**
                 * 移动偏移量
                 */
                float dy = endY - startY;

                //dy大于0的时候才能出来，并且是第一个元素item显示
                if (dy > 0 && getFirstVisiblePosition() == 0) {

                    //得到padding值
                    int padding = (int) (dy - measuredHeight);

                    //设置HeaderView的padding值
                    mHeaderView.setPadding(0, padding, 0, 0);

                    if (padding > 0) {
                        //状态改为松开刷新
                        CURRENT_STATE = STATE_RELEASE_REFRESH;
                        refreshState();
                    } else if (padding < 0 && CURRENT_STATE != STATE_PULL_REFRESH) {
                        CURRENT_STATE = STATE_PULL_REFRESH;
                    }

                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                //重置
                startY = -1;
                if (CURRENT_STATE == STATE_RELEASE_REFRESH) {
                    CURRENT_STATE = STATE_REFRESHING;
                    //显示
                    mHeaderView.setPadding(0, 0, 0, 0);
                    refreshState();
                } else if (CURRENT_STATE == STATE_PULL_REFRESH) {
                    //隐藏标题
                    mHeaderView.setPadding(0, -measuredHeight, 0, 0);
                }
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 根据当前状态改变刷新的时候的显示
     */
    private void refreshState() {
        switch (CURRENT_STATE) {
            case STATE_PULL_REFRESH:
                tvTitle.setText("下拉刷新");
                ivImage.setVisibility(VISIBLE);
                progressBar.setVisibility(INVISIBLE);
                ivImage.startAnimation(animDown);
                break;
            case STATE_RELEASE_REFRESH:
                tvTitle.setText("松开刷新");
                ivImage.setVisibility(VISIBLE);
                progressBar.setVisibility(INVISIBLE);
                ivImage.startAnimation(animUp);
                break;
            case STATE_REFRESHING:
                tvTitle.setText("正在刷新");
                //必须先清除动画，才能隐藏
                ivImage.clearAnimation();
                ivImage.setVisibility(INVISIBLE);
                progressBar.setVisibility(VISIBLE);
                if (mListener != null) {
                    mListener.onRefresh();
                }
                break;
            default:
                break;
        }
    }

    private void initAnimation() {
        /**
         *  箭头向上的动画
         */
        animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f
                , Animation.RELATIVE_TO_SELF, 0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);

        /**
         *    箭头向下的动画
         */
        animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f
                , Animation.RELATIVE_TO_SELF, 0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);
    }

    OnRefreshListener mListener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }


    /**
     * 下拉刷新和上拉加载更多接口
     */
    public interface OnRefreshListener {
        void onRefresh();
        void onLoadMore();
    }

    public void OnRefreshComplete(boolean success) {

        if (isLoadingMore){
            //隐藏
            mFooterView.setPadding(0,-mFotterViewHeight,0,0);
            //加载更多设置为false
            isLoadingMore = false;
        } else {
            //
            CURRENT_STATE = STATE_PULL_REFRESH;
            tvTitle.setText("下拉刷新");
            ivImage.setVisibility(VISIBLE);
            progressBar.setVisibility(INVISIBLE);

            mHeaderView.setPadding(0, -measuredHeight, 0, 0);

            if (success) {
                tvData.setText("最后刷新时间:" + getCurrentTime());
            }
        }
    }

    /**
     * 获得系统当前的时间
     * @return
     */
    public String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String currentTime = sdf.format(new Date());
        return currentTime;
    }

    /**
     * 初始化FooterView
     */
    private void initFooterView() {
        //加载FooterView布局
        mFooterView = View.inflate(getContext(), R.layout.refresh_listview_footer, null);
        //添加
        this.addFooterView(mFooterView);
        //测量mFooterView
        mFooterView.measure(0, 0);
        //获取到mFooterView的高度
        mFotterViewHeight = mFooterView.getMeasuredHeight();
        //隐藏mFooterView
        mFooterView.setPadding(0, -mFotterViewHeight, 0, 0);

        //设置滑动监听事件
        this.setOnScrollListener(this);
    }

    /**
     * 是否加载更多
     */
    private boolean isLoadingMore ;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        /**
         * SCROLL_STATE_IDLE   滑动停止
         * SCROLL_STATE_FLING  滑动完成
         * 在停止滑动并且滑动停止的时候判断
         */
        if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
            //判断是否显示的是最后一个item，并且是否是加载更多的事件
            if (getLastVisiblePosition() == getCount() -1&& !isLoadingMore) {
                //滑动到最后
                //Log.d("RefreshListView", "滑动到底了");
                //显示mFooterView
                mFooterView.setPadding(0,0,0,0);
                //改变listview显示的位置
                setSelection(getCount());
                //加载更多设置为true
                isLoadingMore = true;
                if (mListener != null){
                    //调用接口
                    mListener.onLoadMore();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    OnItemClickListener mItemClickListener;

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {

        /**
         * 把当前的mItemClickListener传递到底层，重写
         */
        super.setOnItemClickListener(this);
        mItemClickListener = listener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mItemClickListener !=null){
            //减去两个头布局getHeaderViewsCount()
            mItemClickListener.onItemClick(parent,view,position - getHeaderViewsCount(),id);
        }
    }

}
