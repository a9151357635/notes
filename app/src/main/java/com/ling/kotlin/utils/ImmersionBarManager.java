package com.ling.kotlin.utils;

import android.app.Activity;
import android.view.WindowManager;
import com.gyf.barlibrary.ImmersionBar;

/**
 * Name: ImmersionBarManager
 * Author: Ling
 * Comment: //状态栏管理类
 * Date: 2017-09-20 15:24
 */

public class ImmersionBarManager {


    private ImmersionBar mImmersionBar;

    public ImmersionBar getmImmersionBar() {
        return mImmersionBar;
    }

    public void setmImmersionBar(ImmersionBar mImmersionBar) {
        this.mImmersionBar = mImmersionBar;
    }

    public ImmersionBarManager(Activity mActivity) {
        mImmersionBar = ImmersionBar.with(mActivity);
    }

    /**
     * 透明状态栏。。全屏展示。。
     */
    public void transparentStatusBar() {
        mImmersionBar.transparentStatusBar().init();
    }

    /**
     * 适配状态栏。。
     */
    public void fitsSystemWindowsBar() {
        mImmersionBar.fitsSystemWindows(true).init();
    }

    /**
     * 透明状态栏。。全屏展示。。
     */
    public void transparentStatusBar(boolean input) {
        mImmersionBar.transparentStatusBar().keyboardEnable(input, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN).init();
    }

    public void transparentBar(){
        if (mImmersionBar != null)
            mImmersionBar.transparentBar();
    }


    public void onDestroy() {
        if (mImmersionBar != null)
            mImmersionBar.destroy();  //必须调用该方法，防止内存泄漏，不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
    }

//    ImmersionBar.with(this)
//            .transparentStatusBar()  //透明状态栏，不写默认透明色
//             .transparentNavigationBar()  //透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为true)
//             .transparentBar()             //透明状态栏和导航栏，不写默认状态栏为透明色，导航栏为黑色（设置此方法，fullScreen()方法自动为true）
//             .statusBarColor(R.color.colorPrimary)     //状态栏颜色，不写默认透明色
//             .navigationBarColor(R.color.colorPrimary) //导航栏颜色，不写默认黑色
//             .barColor(R.color.colorPrimary)  //同时自定义状态栏和导航栏颜色，不写默认状态栏为透明色，导航栏为黑色
//             .statusBarAlpha(0.3f)  //状态栏透明度，不写默认0.0f
//             .navigationBarAlpha(0.4f)  //导航栏透明度，不写默认0.0F
//             .barAlpha(0.3f)  //状态栏和导航栏透明度，不写默认0.0f
//             .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
//             .flymeOSStatusBarFontColor(R.color.btn3)  //修改flyme OS状态栏字体颜色
//             .fullScreen(true)      //有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
//             .hideBar(BarHide.FLAG_HIDE_BAR)  //隐藏状态栏或导航栏或两者，不写默认不隐藏
//             .addViewSupportTransformColor(toolbar)  //设置支持view变色，可以添加多个view，不指定颜色，默认和状态栏同色，还有两个重载方法
//             .titleBar(view)    //解决状态栏和布局重叠问题，任选其一
//             .titleBarMarginTop(view)     //解决状态栏和布局重叠问题，任选其一
//             .statusBarView(view)  //解决状态栏和布局重叠问题，任选其一
//             .fitsSystemWindows(true)    //解决状态栏和布局重叠问题，任选其一，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色，还有一些重载方法
//             .supportActionBar(true) //支持ActionBar使用
//             .statusBarColorTransform(R.color.orange)  //状态栏变色后的颜色
//             .navigationBarColorTransform(R.color.orange) //导航栏变色后的颜色
//             .barColorTransform(R.color.orange)  //状态栏和导航栏变色后的颜色
//             .removeSupportView(toolbar)  //移除指定view支持
//             .removeSupportAllView() //移除全部view支持
//             .navigationBarEnable(true)   //是否可以修改导航栏颜色，默认为true
//             .navigationBarWithKitkatEnable(true)  //是否可以修改安卓4.4和emui3.1手机导航栏颜色，默认为true
//             .fixMarginAtBottom(true)   //已过时，当xml里使用android:fitsSystemWindows="true"属性时,解决4.4和emui3.1手机底部有时会出现多余空白的问题，默认为false，非必须
//             .addTag("tag")  //给以上设置的参数打标记
//             .getTag("tag")  //根据tag获得沉浸式参数
//             .reset()  //重置所以沉浸式参数
//             .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
//             .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)  //单独指定软键盘模式
//             .setOnKeyboardListener(new OnKeyboardListener() {    //软键盘监听回调
//        @Override
//        public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
//            LogUtils.e(isPopup);  //isPopup为true，软键盘弹出，为false，软键盘关闭
//        }
//    })
//            .init();  //必须调用方可沉浸式
}
