package com.example.administrator.slidedellistviewdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


/**
 * Created by Administrator on 2017/7/19.
 */

public class SlideDeleteListView extends ListView {

    //删除button用popupwindow来做
    private PopupWindow popupWindow;
    private int mPopupWindowHeight;
    private int mPopupWindowWidth;

    private LayoutInflater layoutInflater;

    //判断用户是否滑动的临界值
    private int touchSlop;

    private TextView tv_del;

    /**
     * 手指按下时的x坐标
     */
    private int xDown;
    /**
     * 手指按下时的y坐标
     */
    private int yDown;
    /**
     * 手指移动时的x坐标
     */
    private int xMove;
    /**
     * 手指移动时的y坐标
     */
    private int yMove;

    /**
     * 当前手指触摸的View
     */
    private View mCurrentView;

    /**
     * 当前手指触摸的位置
     */
    private int mCurrentViewPos;

    /**
     * 当前状态是否为已滑动
     */
    private boolean isSliding = false;

    private View popupContentView;

    private DeleteItemListener mListener;

    interface DeleteItemListener{
        void deleteItem(int position);
    }

    public SlideDeleteListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        layoutInflater = LayoutInflater.from(context);

        //该临界值为android sdk中判断是否滑动的临界值
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        popupContentView = layoutInflater.inflate(R.layout.ll_del_popup, null);
        tv_del = (TextView) popupContentView.findViewById(R.id.del);
        tv_del.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.deleteItem(mCurrentViewPos);
                    dismissPopWindow();
                }
            }
        });

        popupWindow = new PopupWindow(popupContentView, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        //为了获取popupwindow的宽高
        popupWindow.getContentView().measure(0, 0);
        mPopupWindowHeight = popupWindow.getContentView().getHeight();
        mPopupWindowWidth = popupWindow.getContentView().getWidth();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                xDown = (int) ev.getX();
                yDown = (int) ev.getY();
                //如果当前已经被侧滑为删除状态,则touch事件直接向下传递
                if(popupWindow.isShowing()){
                    dismissPopWindow();
                    return false;
                }
                mCurrentViewPos = pointToPosition(xDown, yDown);
                View view = getChildAt(mCurrentViewPos - getFirstVisiblePosition());
                mCurrentView = view;
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = (int) ev.getX();
                yMove = (int) ev.getY();
                int dx = xMove - xDown;
                int dy = yMove - yDown;
                if(Math.abs(dy) < Math.abs(dx) && Math.abs(dx) > touchSlop && dx < 0){
                       isSliding = true;
                }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(isSliding){
            switch (ev.getAction()){
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    int[] location = new int[2];
                    //获取当前item的位置x和y
                    mCurrentView.getLocationOnScreen(location);

                    //设置popupwindow的动画
                    popupWindow.setAnimationStyle(R.style.popwindow_delete_btn_anim_style);

                    popupWindow.update();
                    popupWindow.showAtLocation(mCurrentView,
                            Gravity.LEFT | Gravity.TOP,
                            mCurrentView.getWidth() - mPopupWindowWidth,
                            location[1]);
                    break;
                case MotionEvent.ACTION_UP:
                    isSliding =false;
                    break;
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 隐藏popupWindow
     */
    private void dismissPopWindow()
    {
        if (popupWindow != null && popupWindow.isShowing())
        {
            popupWindow.dismiss();
        }
    }

    public void setDeleteItemListener(DeleteItemListener listener){
        this.mListener = listener;
    }
}
