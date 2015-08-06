package com.satan.swipebacklayout;

import android.app.Activity;
import android.os.Bundle;

import com.satan.swipebacklibrary.SwipeBackFrameLayout;

/**
 * Created by satan on 2015/8/1.
 */
public class SwipeBackActivity extends Activity {

    private SwipeBackFrameLayout mSwipeBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipeback);
        mSwipeBack = (SwipeBackFrameLayout) findViewById(R.id.swipe_back);
        mSwipeBack.setCallBack(new SwipeBackFrameLayout.CallBack() {
            @Override
            public void onFinish() {
                finish();
            }
        });
    }
}
