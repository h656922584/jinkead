package com.jinke.jinkead_sample;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.qiji.Initialization;
import com.qiji.StringUtils;

public class MainActivity extends Activity {
    private LinearLayout mActivityMain;
    private View bannerAdView = null;

    private final int AD_STATE_FAIL = -1; // No advert
    private final int AD_STATE_SHOW = 0; // show ads
    private final int VIDEO_PLAY_END = 6; // Video playback is completed
    private final int VIDEO_PAGE_CLOSE = 7; // Video playback is completed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mActivityMain = (LinearLayout) findViewById(R.id.activity_main);
        /**
         * initialize Sdk
         * @param context
         * @param appKey
         * @param channelId
         *
         * Another way to:
         * Initialization.init();
         */
//        Initialization.init(MainActivity.this, "1", "test");
//        Initialization.init(MainActivity.this, "hGp5qw1F", "6T17w0JT"); // 海绵宝宝
        Initialization.init(MainActivity.this, "83g5nD4y", "6T17w0JT"); // 汤姆猫编程

        Initialization.setDebuggable(MainActivity.this, true);

//        数据中心初始化
//        com.Statistics.Initialization.init(this,"测试","测试");
    }

    public void showStartScreen(View view) {
        fetchAd(StringUtils.AD_STYLE_START_SCREEN);
    }

    public void showInterstitial(View view) {
        fetchAd(StringUtils.AD_STYLE_INTERSTITIAL);
    }

    public void showInterstitial2(View view) {
        showAd(StringUtils.AD_STYLE_INTERSTITIAL, new Handler());
    }

    public void showBannerAdView(View view) {
        fetchAd(StringUtils.AD_STYLE_BANNER);
    }

    public void showVideo(View view) {
        fetchAd(StringUtils.AD_STYLE_VIDEO);
    }

    /**
     * Set the log is displayed Is not a must, default no log
     * <p>
     * warning: Can only be called after the init.
     *
     * @param context
     * @param isDebug
     */
    public void openLog(View view) {
        Initialization.setDebuggable(MainActivity.this, true);
    }

    private void fetchAd(final int adStyle) {
        /**
         * fetch ad
         *
         * * @param adStyle inventory type :
         *                StringUtils.AD_STYLE_START_SCREEN  --- initial page
         *                StringUtils.AD_STYLE_INTERSTITIAL  --- pop out window half
         *                StringUtils.AD_STYLE_BANNER        --- banner
         *                StringUtils.AD_STYLE_FULL_SCREEN   --- pop out window full
         *                StringUtils.AD_STYLE_VIDEO         --- video
         * @param handler Handler is sdk callback
         *
         * msg.arg1 is fetch return value:
         *      -1: fetch fail no ad.
         *      1:  fetch success has ad.
         * msg.what is Advertising Stats return value:
         *      0: ad show success.
         *      -1: Show the failure.
         *      6: Video playback is completed.
         */
        Initialization.fetchAd(MainActivity.this, adStyle, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.d("mHandler", "----------");
                switch (msg.arg1) {
                    case -1:
                        Log.d("example", "fetch fail no ad");
                        Toast.makeText(MainActivity.this, "fetch fail no ad", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Log.d("example", "fetch success has ad");
                        /**
                         * Use display advertising method
                         */
                        showAd(adStyle, this);
                        break;
                }
                switch (msg.what) {
                    case AD_STATE_SHOW:
                        Log.d("adState", "ad show success");
                        break;
                    case AD_STATE_FAIL:
                        Log.d("adState", "Show the failure");
                        break;
                    case VIDEO_PLAY_END:
                        Log.d("adState", "Video playback is completed");
                        break;
                    case VIDEO_PAGE_CLOSE:
                        Log.d("adState", "Close video page");
                        break;
                }
            }
        });
    }

    /**
     * Display ads
     *
     * @param adStyle inventory type :
     *                StringUtils.AD_STYLE_START_SCREEN  --- initial page
     *                StringUtils.AD_STYLE_INTERSTITIAL  --- pop out window half
     *                StringUtils.AD_STYLE_BANNER        --- banner
     *                StringUtils.AD_STYLE_FULL_SCREEN   --- pop out window full
     *                StringUtils.AD_STYLE_VIDEO         --- video
     * @param handler Ad state callback
     *                <p>
     *                Another way to:
     *                Initialization.showAd(MainActivity.this, StringUtils.AD_STYLE_START_SCREEN, null);
     */
    private void showAd(final int adStyle, Handler handler) {
        if (adStyle == 2) {
            if (bannerAdView != null)
                mActivityMain.removeView(bannerAdView);
            bannerAdView = Initialization.bannerAdView(MainActivity.this, new Handler());
            if (bannerAdView != null)
                mActivityMain.addView(bannerAdView);
        } else {
            Initialization.showAd(MainActivity.this, adStyle, handler);
        }
    }

    @Override
    protected void onDestroy() {
        /**
         * Exit app called when, Release resources
         *
         * @param Need to exit the last one activity
         */
        Initialization.exit(this);
        super.onDestroy();
    }
}

