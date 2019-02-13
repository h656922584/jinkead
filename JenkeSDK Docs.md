### 1.Including the SDK
download and extract the SDK for Android. copy the qijisdk.jar file and place it in the /libs folder in your project you might need to create the directory if it doesn't already exist. Then, add the following lines to your app's build.gradle:

 ```xml
 dependencies {
  compile fileTree(include: ['*.jar'], dir: 'libs')
 }
 ```


### 2.Configure the SDK
#### 1) add permissions in AndroidManifest.xml： 
```xml
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.GET_TASKS"/>
<uses-permission android:name="android.permission.BLUETOOTH" />
```

#### 2) Configure activity
##### Add the following to the application tag of your AndroidManifest.xml:

```xml
<activity android:name="com.qiji.AgentActivity"
    android:launchMode="singleTop"
    android:configChanges="orientation|keyboardHidden|screenSize"
    android:excludeFromRecents="true"
    android:theme="@android:style/Theme.Translucent.NoTitleBar"/>
<activity android:name="com.qiji.DetailActivity"
    android:launchMode="singleInstance"
    android:configChanges="orientation|keyboardHidden|screenSize"
    android:excludeFromRecents="true"
    android:theme="@android:style/Theme.Translucent.NoTitleBar"/>
<service android:name="com.gdt.smtt.DownloadService" android:exported="false" />
<activity android:name="com.gdt.smtt.ADActivity" android:configChanges="keyboard|keyboardHidden|orientation|screenSize" />
<activity android:name="com.gdt.smtt.PortraitADActivity" android:screenOrientation="portrait" android:configChanges="keyboard|keyboardHidden|orientation|screenSize" />
<activity android:name="com.gdt.smtt.LandscapeADActivity" android:screenOrientation="landscape" android:configChanges="keyboard|keyboardHidden|orientation|screenSize" />

```

### 3. Integration code:
- #### SDK initialize:
Initialize sdk, add below code in the place while application initial, suppose to be in the very first place once programs run.

> With no parameters initialization method,Must be in the AndroidManifest. In the XML configuration app_key and channel_id

```java
/**
 * initialize
 * @param context
 */
Initialization.init(Context context);
```

> Initialization method with a parameter,If AndroidManifest. XML configuration appkey and channel will be null and void and will use the initialization method of appkey and channel
"appkey" and "channel" need to be retrieved from the backend

```java
/**
 * initialize
 * @param context
 * @param appKey
 * @param channelId
 */
Initialization.init(Context context,String appKey, String channelId);
```

- #### fetch Ad,If there is a advertising can show detection

```java
/**
 * Detect whether there is advertising
 * @param context
 * @param adStyle Advertising style(check attachment 3.1)
 * @param mHandler Get the msg.arg1 get testing results callback (check attachment 3.2)
 */
public static void fetchAd(Context context, int adStyle, Handler mHandler) {
    try {
        Initialization.fetchAd(mContext, adStyle, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.arg1) {
                    case -1:
                        Log.d("fetch result", "fail");
                        break;
                    case 1:
                        Log.d("fetch result", "success");
                        break;
                }
            });
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

> fetch Ad Example:

```java
public static void fetchAd(Context context, int adStyle, Handler mHandler) {
    try {
        Initialization.fetchAd(mContext, adStyle, mHandler);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

- #### show ad
```java
/**
 * show ad
 * @param context
 * @param adStyle Advertising style(check attachment 3.1)
 * @param handler Advertising state callback (check attachment 3.3)
 */
Initialization.StartShow(Context context, int adStyle, Handler handler);
```

> show ad Example:

```java
private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AD_STATE_SHOW:
                    Log.d("adState", "ad show");
                    break;
                case AD_STATE_CLICK:
                    Log.d("adState", "clicked");
                    break;
                case AD_STATE_CLOSE:
                    Log.d("adState", "close");
                    break;
                case AD_STATE_DOWNLOAD:
                    Log.d("adState", "start download");
                    break;
                case AD_STATE_INSTALL:
                    Log.d("adState", "download complete");
                    break;
                case AD_STATE_DOWNLOAD_COMPLETE:
                    Log.d("adState", "install success");
                    break;
                case AD_STATE_NO_ADVERT:
                    Log.d("adState", "No ads");
                    break;
                case VIDEO_PLAY_END:
                    Log.d("adState", "Video playback is completed");
                    break;
            }
        }
    };
```

> Only handler example: Complete issued incentives play a video
```java
private Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
        Log.d("mHandler", "----------");
        switch (msg.arg1) {
            case -1:
                Log.d("example", "fetch fail no ad");
                break;
            case 1:
                Log.d("example", "fetch success has ad");
                break;
        }
        switch (msg.what) {
            case AD_STATE_SHOW:  // 0
                Log.d("adState", "ad show success");
                break;
            case -1:
                Log.d("adState", "Show the failure");
                break;
            case 6:
                Log.d("adState", "Video playback is completed");
                break;
        }
    }
};  
```

Banner Use the ViewGroup:
```java
/**  
 *   
 * @param context  
 * @param mHandler  
 * @return view  
 */
public View bannerAdView(Context context, Handler mHandler)
```

> Example:

```java
BannerAdView adView = (BannerAdView) Initialize.bannerAdView(MainActivity.this, new Handler());
mMainLayout.addView(adView);
```


- #### exit app release resources:
Add the code in the onDestroy:
```java
/**
 * Exit app called when, Release resources
 * @param Activity
 */
Initialization.exit(this);
```


- #### Set the log is displayed Is not a must:
```java
/**
 * Set the log is displayed Is not a must, default no log
 *
 * warning: Can only be called after the init.
 *
 * @param Activity
 * @param boolean isOpenLog: true is open, false is close.
 */
Initialization.setDebuggable(Context context, boolean isOpenLog);
```
---
##### Attachment 3.1 Advertising style
ad style | ad style ID | describe
---|--- | ---
interstitial | 1 | pop out window half
banner | 2 | banner
full_screen | 3 | pop out window full
start_screen | 4 | initial page
video | 5 | video

##### Attachment 3.2 fetch ad result
###### msg.arg
fetch result | describe
---|--- 
1 |  Advertising success
-1 |  Advertising failed

##### Attachment 3.3 ad status
###### msg.what
ad status | ad status ID | describe
---|--- | ---
fail | -1 | Show the failure
show | 0 | ad show
click | 1 | clicked
close | 2 | close
download | 3 | start download
download_complete | 4 | download complete
install | 5 | install success
video play end | 6 | video play end

